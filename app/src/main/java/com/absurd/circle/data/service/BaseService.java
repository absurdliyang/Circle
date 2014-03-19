package com.absurd.circle.data.service;

import android.content.Context;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.client.AzureClient;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.model.UserLocation;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;

/**
 * Created by absurd on 14-3-14.
 */
public class BaseService{
    protected CommonLog mLog = LogFactory.createLog(AppConstant.TAG);
    private MobileServiceClient mClient = AzureClient.getInstance(AppContext.getContext());

    private static final String TABLE_USERS = "Users";
    private static final String TABLE_MESSAGE= "Message";


    protected MobileServiceTable<Message> mMessageTable;
    protected MobileServiceTable<User> mUserTable;
    protected MobileServiceTable<UserLocation> mUserLocationTable;

    public BaseService(){
        initTable();
    }

    public BaseService(Context context){
        mClient = AzureClient.getInstance(context);
        initTable();
    }

    public BaseService(Context context, String token){
        this(context);
        MobileServiceUser user = new MobileServiceUser("");
        user.setAuthenticationToken(token);
        mClient.setCurrentUser(user);
    }

    public void initTable(){
        mMessageTable = mClient.getTable(TABLE_MESSAGE,Message.class);
        mUserTable = mClient.getTable(TABLE_USERS,User.class);
        mUserLocationTable = mClient.getTable(UserLocation.class);
    }

    public MobileServiceTable<User> getUserTable(){
        return mUserTable;
    }

    public MobileServiceTable<Message> getMessageTable(){
        return mMessageTable;
    }

    public MobileServiceTable<UserLocation> getUserLocationTable(){
        return mUserLocationTable;
    }

}
