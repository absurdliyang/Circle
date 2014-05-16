package com.absurd.circle.data.service;

import android.content.Context;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.client.volley.GsonRequest;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.BlackList;
import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.FunsCount;
import com.absurd.circle.data.model.User;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
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
        getUserTable().insert(user, callback);
    }

    public void deleteUser(User user, TableDeleteCallback callback){
        getUserTable().delete(user, callback);
    }

    public void getUser(String userId,TableQueryCallback<User> callback){
        getUserTable().where().field("userId").eq(userId).execute(callback);
    }

    public void updateUser(User user, TableOperationCallback<User> callback){
        getUserTable().update(user,callback);
    }


    public void getUserFuns(String userId, int pageIndex, int pageSize,  TableQueryCallback<Follow> callback){
        getFollowTable().where().field("followuserid").eq(userId)
                .skip(pageIndex * pageSize)
                .execute(callback);
    }

    public void getAllBlackList(String userId,TableQueryCallback<BlackList> callback){
        getBlackListTable().where().field("userid").eq(userId)
                .execute(callback);
    }
    public void getUserFollowers(String userId, int pageIndex, int pageSize, TableQueryCallback<Follow> callback){
        getFollowTable().where().field("userId").eq(userId)
                .skip(pageIndex * pageSize)
                .execute(callback);
    }

    public void getAllUserFollowers(String userId, TableQueryCallback<Follow> callback){
        getFollowTable().where().field("userId").eq(userId)
                .execute(callback);
    }

    public void insertFollower(Follow follow, TableOperationCallback<Follow> callback){
        getFollowTable().insert(follow,callback);
    }

    public void deleteFollower(Follow follow, TableDeleteCallback callback){
        getFollowTable().delete(follow,callback);
    }

    public void getFunsCount(String userId,Response.Listener<FunsCount> responseListener){
        String url = "https://incircle.azure-mobile.net/api/getfunscount?userid=" + userId;

        GsonRequest<FunsCount> gsonReqeust = new GsonRequest<FunsCount>(url,FunsCount.class,null,
                responseListener,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.commonLog.i("Get funs count error");
            }
        });
        RequestManager.addRequest(gsonReqeust,"getFunsCount");
    }

    public void insertBlackList(BlackList black, TableOperationCallback<BlackList> callback){
        getBlackListTable().insert(black, callback);
    }

    public void deleteBlackList(BlackList black, TableDeleteCallback callback){
        getBlackListTable().delete(black, callback);
    }


    /**
     *
     * @param longitude
     * @param latitude
     * @param around
     * @param type -1 the charts 0 all people 1 female  2 male
     * @param pageIndex
     * @param responseListener
     */
    public void getNearPeople(double longitude, double latitude, double around, int type,
                              int pageIndex, Response.Listener<User.GsonUser> responseListener){
        String url = "https://incircle.azure-mobile.net/api/getnearpeople?longitude=" + Double.toString(longitude)
                + "&latitude=" + Double.toString(latitude)
                + "&around=" + Double.toString(around)
                + "&page=" + pageIndex
                + "&type=" + type;
        AppContext.commonLog.i(url);
        GsonRequest<User.GsonUser> gsonRequest = new GsonRequest<User.GsonUser>(url, User.GsonUser.class, null,
                responseListener, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.commonLog.i("Get near people error");
                volleyError.printStackTrace();
            }
        });
        RequestManager.addRequest(gsonRequest,"getNearPeople");
    }



}
