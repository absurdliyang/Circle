package com.absurd.circle.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by absurd on 14-3-25.
 */
public class UserMessage implements Serializable {
    @Expose
    @SerializedName("id")
    private int mId;

    @Expose
    @SerializedName("mediaurl")
    private String mMediaUrl;

    @Expose
    @SerializedName("mediatype")
    private int mMediaType;

    @Expose
    @SerializedName("content")
    private String mContent;

    @Expose
    @SerializedName("fromuserid")
    private String mFromUserId;

    @Expose
    @SerializedName("fromusername")
    private String mFromUserName;

    @Expose
    @SerializedName("touserid")
    private String mToUserId;

    @Expose
    @SerializedName("ToUserName")
    private String mToUserName;

    @Expose
    @SerializedName("date")
    private Date mDate;

    @Expose
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
