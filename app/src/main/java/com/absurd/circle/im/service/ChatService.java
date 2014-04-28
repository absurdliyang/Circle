package com.absurd.circle.im.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.absurd.circle.app.AppContext;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * Created by absurd on 14-4-27.
 */
public class ChatService extends Service {
    public static final String NEW_MESSAGE_ACTION = "newMessage";

    PacketListener mPacketListener = new PacketListener() {
        @Override
        public void processPacket(Packet packet) {
            Message message = (Message)packet;
            AppContext.commonLog.i("Recieve a packet");
            if (message != null && message.getBody() != null
                    && !message.getBody().equals("null")) {
                AppContext.commonLog.i(message.getBody());
                Intent intent = new Intent(NEW_MESSAGE_ACTION);
                sendBroadcast(intent);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        initChatManager();
        AppContext.commonLog.i("Chat service start");

    }

    private void initChatManager(){
        if(!AppContext.xmppConnectionManager.isInit()){
            AppContext.xmppConnectionManager.init();
        }
        AppContext.xmppConnectionManager.getConnection()
                .addPacketListener(mPacketListener,new MessageTypeFilter(Message.Type.chat));
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
