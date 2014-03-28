package com.absurd.circle.data.service;

import android.content.Context;

import com.absurd.circle.data.model.BlackList;
import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.User;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.Calendar;
import java.util.List;

/**
 * Created by absurd on 14-3-14.
 */
public class UserService extends BaseService {

    public void getUsers(TableQueryCallback<User> mCallback){
        getUserTable().where().execute(mCallback);
    }


    public void insertUser(User user, TableOperationCallback<User> callback){
        getUserTable().insert(user,callback);
    }

    public void getUser(String userId,TableQueryCallback<User> callback){
        getUserTable().where().field("userId").eq(userId).execute(callback);
    }

    public void getUserFollowers(String userId, TableQueryCallback<Follow> callback){
        getFollowTable().where().field("userId").eq(userId).execute(callback);
    }

    public void getUserFuns(String userId, TableQueryCallback<Follow> callback){
        getFollowTable().where().field("followuserid").eq(userId).execute(callback);
    }

    public void getBlackList(String userId, TableQueryCallback<BlackList> callback){
        getBlackListTable().where().field("userid").eq(userId).execute(callback);
    }

}
