package com.absurd.circle.im.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.ChatMessageType;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.Praise;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.data.util.JsonUtil;
import com.absurd.circle.util.StringUtil;


import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.OfflineMessageManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;

import java.util.Iterator;

/**
 * Created by absurd on 14-4-27.
 */
public class ChatService extends Service {
    public static final String NEW_MESSAGE_ACTION = "com.absurd.circle.im.chatReciever";

    PacketListener mPacketListener = new PacketListener() {
        @Override
        public void processPacket(Packet packet) {
            Message message = (Message)packet;
            AppContext.commonLog.i("Recieve a packet");
            saveRecievedMessageBody(message);
            AppContext.commonLog.i(message.toString());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        new ChatLoginTask().execute();
    }


    private void getOfflineMessages(){
        OfflineMessageManager offlineManager = new OfflineMessageManager(AppContext.xmppConnectionManager.getConnection());
        try {
            Iterator<Message> it = offlineManager.getMessages();
            // Fix the fucking bug of asmack ( ServiceDiscoveryManager.getInstanceFor return null)
            new ServiceDiscoveryManager(AppContext.xmppConnectionManager.getConnection());

            AppContext.commonLog.i("get offlien count "  + offlineManager.getMessageCount() + "");
            while(it.hasNext()) {
                Message message = it.next();
                AppContext.commonLog.i(message.getBody().toString());
                saveRecievedMessageBody(message);
            }
            offlineManager.deleteMessages();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

    }

    private void saveRecievedMessageBody(Message message){
        if (message != null && message.getBody() != null
                && !message.getBody().equals("null")) {
            String body = message.getBody();
            AppContext.commonLog.i(body);

            Praise p= JsonUtil.fromJson(body, Praise.class);
            p.setState(false);
            AppContext.cacheService.praiseDBManager.insertPraise(p);
            if(!StringUtil.isEmpty(message.getSubject())) {
                if (message.getSubject().equals(ChatMessageType.USERMESSAGE)) {
                    UserMessage userMessage = JsonUtil.fromJson(body, UserMessage.class);
                    userMessage.setState(0);
                    AppContext.cacheService.userMessageDBManager.insertUserMessage(userMessage);

                    Intent intent = new Intent(NEW_MESSAGE_ACTION);
                    intent.putExtra("message", userMessage);
                    sendBroadcast(intent);
                    AppContext.commonLog.i("sendBroadCast");
                } else if (message.getSubject().equals(ChatMessageType.COMMENT)) {

                    Comment comment = JsonUtil.fromJson(body, Comment.class);
                    comment.setState(0);
                    AppContext.cacheService.commnetDBManager.insertComment(comment);
                } else if (message.getSubject().equals(ChatMessageType.PRAISE)) {
                    Praise praise = JsonUtil.fromJson(body, Praise.class);
                    praise.setState(false);
                    AppContext.cacheService.praiseDBManager.insertPraise(praise);
                }
            }
        }
    }


    public class ChatLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(AppContext.isAuthed()) {
                if(!AppContext.xmppConnectionManager.isInit()){
                    AppContext.xmppConnectionManager.init();
                }
                if (!AppContext.xmppConnectionManager.getConnection().isAuthenticated()) {
                    AppContext.xmppConnectionManager.login(AppContext.auth.getId() + "", AppContext.auth.getId() + "");
                }
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if(b){
                AppContext.commonLog.i("chat login success");
                AppContext.xmppConnectionManager.getConnection()
                    .addPacketListener(mPacketListener, null);
                getOfflineMessages();
                Presence presence = new Presence(Presence.Type.available);
                AppContext.xmppConnectionManager.getConnection().sendPacket(presence);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
