package com.absurd.circle.data.test;

import android.os.AsyncTask;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.Calendar;
import java.util.List;

/**
 * Created by absurd on 14-3-15.
 */
public class UserServiceTest  extends BaseTestCase{
    private UserService mUserService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mUserService = new UserService();
        mLog.d("UserServiceTest");
    }
    /**
    public void testInsertUser() throws Exception{
        final Boolean lock = false;
        User user = new User();
        user.setLoginType(1);
        user.setName(" ");
        user.setSex("m");
        user.setUserId("qq:E192CA14D108CE509B20D0B6630BA8E4");
        user.setLoginName("testtest");
        user.setPassword("testtest");
        Calendar calendar = Calendar.getInstance();
        user.setDate(new java.sql.Date(calendar.getTimeInMillis()));
        user.setLastLoginDate(new java.sql.Date(calendar.getTimeInMillis()));
        mUserService.insertUser(user,new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User user, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if(user != null){
                    mLog.d(user.getName() + "---" + user.getId() +" ------------" +  user.getToken() + "---" +user.getLoginName() + "------" + user.getPassword());
                }else{
                    mLog.d("error");
                }
                if(e != null){
                    e.printStackTrace();
                }
                synchronized (lock){
                    lock.notify();
                }
            }
        });
        synchronized (lock){
            lock.wait();
        }
    }
**/
    public void testLogin() throws Exception{
        final Boolean lock = false;
        User loginUser = new User();
        loginUser.setLoginName("testtest");
        loginUser.setPassword("testtest");
        mUserService.insertUser(loginUser,new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User user, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if(user != null){
                    mLog.d(user.getName() + "---" + user.getId() +" ------------" +  user.getToken() + "---" +user.getLoginName() + "------" + user.getPassword());
                }else{
                    mLog.d("error");
                }
                if(e != null){
                    e.printStackTrace();
                }
                synchronized (lock){
                    lock.notify();
                }
            }
        });
        synchronized (lock){
            lock.wait();
        }
    }
    public void testUser() throws Exception {
        final Object lock = new Object();
        mUserService.getUser("qq:7F234A6D96FB2B9A45F2AD02B196AC37",new TableQueryCallback<User>() {
            @Override
            public void onCompleted(List<User> users, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if(users == null){
                    if(e != null){
                        e.printStackTrace();
                    }
                }else{
                    mLog.i(users.get(0).getName());
                }
                synchronized (lock){
                    lock.notify();
                }
            }
        });
        synchronized (lock){
            lock.wait();
        }
    }
}
