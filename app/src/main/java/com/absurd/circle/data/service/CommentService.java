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
    public CommentService(Context context, String token) {
        super(context, token);
    }

    /**
     *
     * @param messageId
     * @param pageIndex
     * @param count
     * @param order true by time otherwise by date
     * @param callback
     */
    public void getComments(int messageId, int pageIndex, int count, boolean order, TableQueryCallback<Comment> callback){
        String orderItem = null;
        if(order){
            orderItem = "date";
        }else{
            orderItem = "id";
        }
        getCommentTable().where()
                .field("messageid").eq(val(messageId))
                .skip(pageIndex * count)
                .top(count)
                .orderBy(orderItem, QueryOrder.Ascending)
                .execute(callback);
    }


    public void addComment(Comment comment, TableOperationCallback<Comment> callback){
        getCommentTable().insert(comment,callback);
    }


}
