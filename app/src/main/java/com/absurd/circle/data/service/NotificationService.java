package com.absurd.circle.data.service;

import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.Praise;
import com.absurd.circle.data.model.UserMessage;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

/**
 * Created by absurd on 14-3-28.
 */
public class NotificationService extends BaseService {
    public NotificationService(){

    }

    public void getUserMessages(String userId, TableQueryCallback<UserMessage> callback){
        getUserMessageTable().where().field("touserid").eq(userId).execute(callback);
    }

    public void getComments(String userId, TableQueryCallback<Comment> callback){
        getCommentTable().where().field("touserid").eq(userId).execute(callback);
    }

    public void getPraises(String userId, TableQueryCallback<Praise> callback){
        getPraiseTable().where().field("touserid").eq(userId).execute(callback);
    }
}
