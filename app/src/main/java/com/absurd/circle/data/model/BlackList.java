package com.absurd.circle.data.model;

import com.absurd.circle.data.util.JsonUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-3-25.
 */
public class BlackList implements Serializable{
    @Expose
    @SerializedName("id")
    private String mId;

    @Expose
    @SerializedName("userid")
    private String mUserId;

    @Expose
    @SerializedName("followuserid")
    private String mFollowUserId;

    @Expose
    @SerializedName("User")
    private String mStrUser;

    private User mUser;

    public User getUser(){
        User user = null;
        if(mUser != null)
            return mUser;
        if(mStrUser != null){
            user = JsonUtil.fromJson(mStrUser, User.class);
        }
        return user;
    }

    public void setUser(User user){
        this.mUser = user;
        mStrUser = JsonUtil.toJson(user);
    }


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getFollowUserId() {
        return mFollowUserId;
    }

    public void setFollowUserId(String followUserId) {
        mFollowUserId = followUserId;
    }

    public String getStrUser() {
        return mStrUser;
    }

    public void setStrUser(String strUser) {
        mStrUser = strUser;
    }
}
