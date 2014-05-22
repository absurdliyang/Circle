package com.absurd.circle.im.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.ChatMessageType;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.Praise;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.data.util.JsonUtil;
import com.absurd.circle.ui.activity.NotificationActivity;
import com.absurd.circle.util.NetworkUtil;
import com.absurd.circle.util.StringUtil;
import com.microsoft.windowsazure.mobileservices.AsyncTaskUtil;


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
            AppContext.commonLog.i("body " + message.getBody() + " to " + message.getTo() + " from " + message.getFrom());
            saveRecievedMessageBody(message);
        }
    };

    private void showNotification(String text){
        String title = "圈圈";
        Notification.Builder builder = new Notification.Builder(this)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher);

        PendingIntent pendIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationActivity.class), 0);

        Notification notification = builder.getNotification();
        notification.setLatestEventInfo(this, title,text, pendIntent);

        NotificationManager notificationManager = (NotificationManager) AppContext.getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (NetworkUtil.isNetConnected()) {
            new ChatLoginTask().execute();
        }
    }


    private void getOfflineMessages(){
        OfflineMessageManager offlineManager = new OfflineMessageManager(AppContext.xmppConnectionManager.getConnection());
        try {
            Iterator<Message> it = offlineManager.getMessages();
            // Fix the fucking bug of asmack ( ServiceDiscoveryManager.getInstanceFor return null)
            new ServiceDiscoveryManager(AppContext.xmppConnectionManager.getConnection());

            AppContext.commonLog.i("get offline count "  + offlineManager.getMessageCount() + "");
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
            if(!StringUtil.isEmpty(message.getSubject())) {
                AppContext.commonLog.i("Message subject --> " + message.getSubject());
                AppContext.notificationNum ++;
                AppContext.sharedPreferenceUtil.setNotificationNum(AppContext.notificationNum);
                String text = "";
                if (message.getSubject().equals(ChatMessageType.USERMESSAGE)) {
                    UserMessage userMessage = JsonUtil.fromJson(body, UserMessage.class);
                    userMessage.setState(0);
                    AppContext.cacheService.userMessageDBManager.insertUserMessage(userMessage);

                    text = userMessage.getFromUserName() + "发来了新消息";
                    Intent intent = new Intent(NEW_MESSAGE_ACTION);
                    intent.putExtra("message", userMessage);
                    sendBroadcast(intent);
                    AppContext.commonLog.i("sendBroadCast");
                } else if (message.getSubject().equals(ChatMessageType.COMMENT)) {
                    AppContext.commonLog.i("New Comment Notification");
                    // Fix the bug android can not recieve the commnet json package
                    // from wp and ios platform
                    String formatBody = body.replace("\"commentdate\": \"0001-01-01T00:00:00\"," ,"\"commentdate\": null,");
                    AppContext.commonLog.i(formatBody);
                    Comment comment = JsonUtil.fromJson(formatBody, Comment.class);
                    AppContext.commonLog.i(comment.toString());
                    comment.setState(0);
                    text = "动态有了新的评论";
                    AppContext.cacheService.commnetDBManager.insertComment(comment);
                } else if (message.getSubject().equals(ChatMessageType.PRAISE)) {
                    Praise praise = JsonUtil.fromJson(body, Praise.class);
                    praise.setState(false);
                    text = "动态有了新的赞";
                    AppContext.cacheService.praiseDBManager.insertPraise(praise);
                }
                showNotification(text);
            }
        }
    }


    public class ChatLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            //AppContext.commonLog.i("ChatService --> " + "try login");
            if(!AppContext.xmppConnectionManager.isInit()){
                AppContext.xmppConnectionManager.init();
            }
            if (!AppContext.xmppConnectionManager.getConnection().isAuthenticated()) {
                if(AppContext.isAuthed()) {
                    AppContext.xmppConnectionManager.login(AppContext.auth.getId() + "", AppContext.auth.getId() + "");
                }
            }
            if(!AppContext.xmppConnectionManager.isInit() || !AppContext.xmppConnectionManager.getConnection().isAuthenticated()){
                return false;
            }else{
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if(b){
                if(AppContext.xmppConnectionManager.getConnection().isConnected()) {
                    AppContext.commonLog.i("chat login success");
                    AppContext.xmppConnectionManager.getConnection()
                            .addPacketListener(mPacketListener, null);
                    AsyncTaskUtil.addTaskInPool(new GetOfflineMessageTask());
                    Presence presence = new Presence(Presence.Type.available);
                    AppContext.xmppConnectionManager.getConnection().sendPacket(presence);
                }
            }else{
                AsyncTaskUtil.addTaskInPool(new ChatLoginTask());
            }
        }
    }


    public class GetOfflineMessageTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            getOfflineMessages();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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
