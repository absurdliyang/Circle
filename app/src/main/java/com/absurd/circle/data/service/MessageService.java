package com.absurd.circle.data.service;

import android.content.Context;


import com.absurd.circle.data.model.Message;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;


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

    public void getNearMessage(int pageIndex, double latitude, double longitude, double around,
                               List<Integer> messageTypes, boolean isNew, String cityCode,
                               TableQueryCallback<Message> callback){
        String strMessageTypes = "";
        if(messageTypes != null){
            int length = messageTypes.size();
            for(int i = 0; i < length - 1; i++){
                strMessageTypes += messageTypes.get(0) + ",";
            }
            strMessageTypes += messageTypes.get(length-1) + "";
        }
        getMessageTable().where()
                .parameter("type", "near")
                .parameter("page",pageIndex + "")
                .parameter("latitude",Double.toString(latitude))
                .parameter("longitude",Double.toString(longitude))
                .parameter("around",around + "")
                .parameter("isnew",isNew + "")
                .parameter("contenttypelist",strMessageTypes)
                .parameter("citycode", cityCode).execute(callback);
    }

    public void getMessageByUser(int pageIndex,String userId, boolean isMe,TableQueryCallback<Message> callback){
        getMessageTable().where()
            .parameter("type","user")
            .parameter("userID", userId)
            .parameter("page", pageIndex + "")
            .parameter("isMe",isMe + "")
            .execute(callback);
    }

    public void getMessageById(int messageId, TableQueryCallback<Message> callback){
        getMessageTable().where()
                .parameter("type","id")
                .parameter("id",messageId + "")
                .execute(callback);
    }


    public void addMessage(Message message,TableOperationCallback<Message> callback){
        getMessageTable().insert(message,callback);
    }



}
