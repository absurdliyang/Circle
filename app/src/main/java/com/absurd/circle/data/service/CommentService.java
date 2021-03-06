package com.absurd.circle.data.service;

import android.content.Context;

import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.val;

import com.absurd.circle.data.model.Comment;
import com.microsoft.windowsazure.mobileservices.QueryOrder;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

/**
 * Created by absurd on 14-3-26.
 */
public class CommentService extends BaseService {

    /**
     *
     * @param messageId
     * @param pageIndex
     * @param count
     * @param order true by time otherwise by date
     * @param callback
     */
    public void getComments(int messageId, int pageIndex, int count, boolean order, TableQueryCallback<Comment> callback){
        QueryOrder queryOrder;
        if(order) {
            queryOrder = QueryOrder.Ascending;
        }else{
            queryOrder = QueryOrder.Descending;
        }
        getCommentTable().where()
                .field("messageid").eq(val(messageId))
                .skip(pageIndex * count)
                .top(count)
                .orderBy("id", queryOrder)
                .execute(callback);
    }


    public void insertComment(Comment comment, TableOperationCallback<Comment> callback){
        getCommentTable().insert(comment,callback);
    }


}
