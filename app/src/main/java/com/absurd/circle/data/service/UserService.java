package com.absurd.circle.data.service;

import android.content.Context;

import com.absurd.circle.data.model.User;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

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

    public void getUsers(TableQueryCallback<User> mCallback){
        mUserTable.where().execute(mCallback);
    }

    public void getUsers(){
        mUserTable.where().field("userid").eq(778692).execute(new TableQueryCallback<User>() {
            @Override
            public void onCompleted(List<User> users, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if(e == null) {
                    for (User u : users) {
                        mLog.i(u.getName());
                    }
                }else{
                    e.printStackTrace();
                }
            }
        });
    }


}
