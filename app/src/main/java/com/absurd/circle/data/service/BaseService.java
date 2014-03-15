package com.absurd.circle.data.service;

import android.content.Context;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.client.AzureClient;
import com.absurd.circle.data.model.Tweet;
import com.absurd.circle.data.model.User;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;

import java.io.Serializable;

/**
 * Created by absurd on 14-3-14.
 */
public class BaseService{
    protected CommonLog mLog = LogFactory.createLog(AppConstant.TAG);
    private MobileServiceClient mClient = AzureClient.getInstance(AppContext.getContext());

    protected MobileServiceTable<Tweet> mTweetTable;
    protected MobileServiceTable<User> mUserTable;

    public BaseService(){
        initTable();
    }

    public BaseService(Context context){
        mClient = AzureClient.getInstance(context);
        initTable();
    }

    public void initTable(){
        mTweetTable = mClient.getTable(Tweet.class);
        mUserTable = mClient.getTable(User.class);
    }

    public MobileServiceTable<User> getUserTable(){
        return mUserTable;
    }

    public MobileServiceTable<Tweet> getTweetTable(){
        return mTweetTable;
    }



}
