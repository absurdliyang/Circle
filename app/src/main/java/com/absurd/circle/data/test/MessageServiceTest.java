package com.absurd.circle.data.test;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.MessageService;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-3-15.
 */
public class MessageServiceTest extends BaseTestCase {
    private MessageService mMessageService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mMessageService = new MessageService(getContext(), AppConstant.TEST_USER_TOKEN);
    }

    public void testGetMessageByUser() throws Exception{
        final Object lock = new Object();
        mMessageService.getMessageByUser(1,new User(), new TableQueryCallback<Message>() {

            @Override
            public void onCompleted(List<Message> messages, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
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
