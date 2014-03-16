package com.absurd.circle.data.service;

import android.content.Context;

import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.val;

import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.model.User;
import com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;


/**
 * Created by absurd on 14-3-14.
 */
public class MessageService extends BaseService{
    public MessageService(Context context){
        super(context);
    }
    public MessageService(){
        super();
    }

    public MessageService(Context context, String token) {
        super(context, token);
    }


    public void getMessageByUser(int pageIndex, User user,TableQueryCallback<Message> callback){
        mMessageTable.where().field("userid").eq(val(""))
                .parameter("type","user")
                .parameter("userID","1157673")
                .parameter("page",pageIndex + "")
                .parameter("isMe","false")
                .execute(callback);
    }


}
