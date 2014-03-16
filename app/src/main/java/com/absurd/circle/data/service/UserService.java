package com.absurd.circle.data.service;

import android.content.Context;

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
    public UserService(Context context){
        super(context);
    }

    public UserService(){
        super();
    }

    public UserService(Context context, String token) {
        super(context, token);
    }

    public void getUsers(TableQueryCallback<User> mCallback){
        mUserTable.where().execute(mCallback);
    }


    public void insertUser(User user, TableOperationCallback<User> callback){
        mUserTable.insert(user,callback);
    }

    public void getUser(String userId,TableQueryCallback<User> callback){
        mUserTable.where().field("userId").eq(userId).execute(callback);
    }


}
