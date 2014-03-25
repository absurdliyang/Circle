package com.absurd.circle.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by absurd on 14-3-25.
 */
public class UserMessage implements Serializable {
    @SerializedName("id")
    private int mId;

    @SerializedName("mediaurl")
    private String mMediaUrl;

    @SerializedName("mediatype")
    private int mMediaType;

    @SerializedName("content")
    private String mContent;

    @SerializedName("fromuserid")
    private String mFromUserId;

    @SerializedName("fromusername")
    private String mFromUserName;

    @SerializedName("touserid")
    private String mToUserId;

    @SerializedName("tousername")
    private String mToUserName;

    @SerializedName("date")
    private Date mDate;

    @SerializedName("state")
    private int mState;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        mMediaUrl = mediaUrl;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getMediaType() {
        return mMediaType;
    }

    public void setMediaType(int mediaType) {
        mMediaType = mediaType;
    }

    public String getFromUserId() {
        return mFromUserId;
    }

    public void setFromUserId(String fromUserId) {
        mFromUserId = fromUserId;
    }

    public String getFromUserName() {
        return mFromUserName;
    }

    public void setFromUserName(String fromUserName) {
        mFromUserName = fromUserName;
    }

    public String getToUserId() {
        return mToUserId;
    }

    public void setToUserId(String toUserId) {
        mToUserId = toUserId;
    }

    public String getToUserName() {
        return mToUserName;
    }

    public void setToUserName(String toUserName) {
        mToUserName = toUserName;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }
}
