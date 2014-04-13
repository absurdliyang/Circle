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
    @Expose
    @SerializedName("id")
    private int mId;

    @Expose
    @SerializedName("messageid")
    private int mMessageId;

    @Expose
    @SerializedName("touserid")
    private String mToUserId;

    @Expose
    @SerializedName("state")
    private boolean mState;

    @Expose
    @SerializedName("userid")
    private String mUserId;

    @Expose
    @SerializedName("date")
    private Date mDate;

    @Expose
    @SerializedName("ParentText")
    private String mParentText;

    private String mStrUser;

    private User mUser;

    public Praise(int id){
        this.mId = id;
    }

    public Praise(){

    }

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


    @Override
    public String toString() {
        return "Praise{" +
                "mId=" + mId +
                ", mMessageId=" + mMessageId +
                ", mToUserId='" + mToUserId + '\'' +
                ", mState=" + mState +
                ", mUserId='" + mUserId + '\'' +
                ", mDate=" + mDate +
                ", mParentText='" + mParentText + '\'' +
                ", mStrUser='" + mStrUser + '\'' +
                ", mUser=" + mUser +
                '}';
    }
}
