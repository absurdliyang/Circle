package com.absurd.circle.data.service;

import android.content.Context;

import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.val;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.model.User;
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
        mMessageTable.where().field("userid").eq(val(AppConstant.TEST_USER_ID))
            .parameter("type","user")
            .parameter("userID", AppConstant.TEST_USER_ID)
            .parameter("page",pageIndex + "")
            .parameter("isMe","true")
            .execute(callback);
    }

    public void getNearMessage(int pageIndex, TableQueryCallback<Message> callback){
        mMessageTable.where().parameter("type","near")
                .parameter("page",pageIndex + "")
                .parameter("latitude",Double.toString(114.78558))
                .parameter("longitude",Double.toString(38.1974))
                .parameter("around",1000 + "")
                .parameter("isnew","true")
                .parameter("contenttypelist","1,2,3")
                .parameter("citycode","fdfd").skip(10).top(20).execute(callback);
    }

}
