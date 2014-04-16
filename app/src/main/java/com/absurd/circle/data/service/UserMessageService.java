package com.absurd.circle.data.service;

import com.absurd.circle.data.model.UserMessage;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;


/**
 * Created by absurd on 14-4-16.
 */
public class UserMessageService extends BaseService {

    public void getUserMessages(String toUserId, String fromUserId, TableQueryCallback<UserMessage> callback){
        getUserMessageTable().where()
                .field("touserid").eq(toUserId)
                .and().field("fromuserid").eq(fromUserId)
                .execute(callback);
    }

    public void updateUserMessageState(UserMessage userMessage, TableOperationCallback<UserMessage> callback){
        getUserMessageTable().update(userMessage,callback);
    }

    public void insertUserMessage(UserMessage userMessage, TableOperationCallback<UserMessage> callback){
        getUserMessageTable().insert(userMessage, callback);
    }


}
