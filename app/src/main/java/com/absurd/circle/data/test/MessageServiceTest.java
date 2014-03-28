package com.absurd.circle.data.test;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.model.MessageType;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.MessageService;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-3-15.
 */
public class MessageServiceTest extends BaseTestCase {
    private MessageService mMessageService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mMessageService = new MessageService();
    }

    public void testGetMessageByUser() throws Exception{
        final Object lock = new Object();
        mMessageService.getMessageByUser(0,"sina:3345514604",false, new TableQueryCallback<Message>() {

            @Override
            public void onCompleted(List<Message> messages, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if(messages == null){
                    if(e != null){
                        e.printStackTrace();
                    }
                }else{
                    for(Message message : messages){
                        mLog.i(message.getContent());
                    }
                }
                synchronized (lock){
                    lock.notify();
                }
            }
        });
        synchronized (lock){
            lock.wait();
        }
    }

    public void testGetNearMessage() throws Exception{
        final Object lock = new Object();
        List<Integer> messageTypes = new ArrayList<Integer>();
        messageTypes.add(MessageType.WEIBO);
        mMessageService.getNearMessage(1,38.246900000000004,114.78558,1000,messageTypes,true,"1",
                new TableQueryCallback<Message>() {
            @Override
            public void onCompleted(List<Message> messages, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if(messages == null){
                    if(e != null){
                        e.printStackTrace();
                    }
                }else{
                    for(Message message : messages){
                        mLog.i(message.getContent());
                    }
                }
                synchronized (lock){
                    lock.notify();
                }
            }
        });
        synchronized (lock){
            lock.wait();
        }
    }

    public void testGetMessageById() throws Exception {
        final Object lock = new Object();
        mMessageService.getMessageById(1265611,new TableQueryCallback<Message>() {
            @Override
            public void onCompleted(List<Message> messages, int count, Exception e, ServiceFilterResponse response) {
                if(messages == null){
                    if(e != null){
                        e.printStackTrace();
                    }
                }else{
                    for(Message message : messages){
                        mLog.i(message.getContent());
                        mLog.i(message.getUser().getName());
                    }
                }
                synchronized (lock){
                    lock.notify();
                }
            }
        });
        synchronized (lock){
            lock.wait();
        }
    }
}
