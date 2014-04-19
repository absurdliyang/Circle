package com.absurd.circle.data.service;

import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.Praise;
import com.absurd.circle.data.model.ReportMessage;
import com.absurd.circle.data.model.UserMessage;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

/**
 * Created by absurd on 14-3-28.
 */
public class NotificationService extends BaseService {
    public NotificationService(){

    }

    public void getUserMessages(String userId, int pageIndex, int pageSize, TableQueryCallback<UserMessage> callback){
        getUserMessageTable().where().field("touserid").eq(userId)
                .skip(pageIndex * pageSize).execute(callback);
    }

    public void getComments(String userId, int pageIndex, int pageSize, TableQueryCallback<Comment> callback){
        getCommentTable().where().field("touserid").eq(userId)
                .skip(pageIndex * pageSize).execute(callback);
    }

    public void getPraises(String userId, int pageIndex, int pageSize,TableQueryCallback<Praise> callback){
        getPraiseTable().where().field("touserid").eq(userId)
                .skip(pageIndex * pageSize).execute(callback);
    }

    public void insertReportMessage(ReportMessage reportMessage, TableOperationCallback<ReportMessage> callback){
        getReportTable().insert(reportMessage, callback);
    }

}
