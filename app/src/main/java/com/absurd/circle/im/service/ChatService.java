package com.absurd.circle.im.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.UserMessage;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.io.Serializable;
import java.util.Calendar;

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
            if (message != null && message.getBody() != null
                    && !message.getBody().equals("null")) {
                AppContext.commonLog.i(message.getBody());
                AppContext.commonLog.i("Recevie a message  --> " + message.getBody().toString() + " From --> " + message.getFrom());

                UserMessage userMessage = new UserMessage();
                String strTo = message.getTo().toString();
                userMessage.setToUserId(strTo.substring(0,strTo.indexOf('@')));
                userMessage.setDate(Calendar.getInstance().getTime());
                String strFrom = message.getFrom().toString();
                userMessage.setFromUserId(strFrom.substring(0,strFrom.indexOf('@')));
                userMessage.setContent(message.getBody().toString());
                userMessage.setState(0);

                AppContext.cacheService.userMessageDBManager.insertUserMessage(userMessage);
                Intent intent = new Intent(NEW_MESSAGE_ACTION);
                intent.putExtra("message",userMessage);
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
                .addPacketListener(mPacketListener, null);
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
