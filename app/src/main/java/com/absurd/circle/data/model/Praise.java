package com.absurd.circle.data.model;

import com.absurd.circle.data.util.JsonUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by absurd on 14-3-25.
 */
public class Praise implements Serializable {
    @SerializedName("id")
    private int mId;

    @SerializedName("messageid")
    private int mMessageId;

    @SerializedName("touserid")
    private String mToUserId;

    @SerializedName("state")
    private boolean mState;

    @SerializedName("userid")
    private String mUserId;

    @SerializedName("date")
    private Date mDate;

    @SerializedName("ParentText")
    private String mParentText;

    @SerializedName("User")
    private String mStrUser;

    @Expose
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

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getStrUser() {
        return mStrUser;
    }

    public void setStrUser(String strUser) {
        mStrUser = strUser;
    }

    public String getParentText() {
        return mParentText;
    }

    public void setParentText(String parentText) {
        mParentText = parentText;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getToUserId() {
        return mToUserId;
    }

    public void setToUserId(String toUserId) {
        mToUserId = toUserId;
    }

    public boolean isState() {
        return mState;
    }

    public void setState(boolean state) {
        mState = state;
    }

    public int getMessageId() {
        return mMessageId;
    }

    public void setMessageId(int messageId) {
        mMessageId = messageId;
    }
}
