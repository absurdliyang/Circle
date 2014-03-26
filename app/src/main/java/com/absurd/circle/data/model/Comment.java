package com.absurd.circle.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-3-25.
 */
public class Comment extends Message implements Serializable {

    @SerializedName("messageid")
    private int mMessageId;

    @SerializedName("parentid")
    private int mParentId;

    @SerializedName("touserid")
    private int mToUserId;

    @SerializedName("state")
    private int mState;

    @SerializedName("ParentText")
    private String mParentText;

    @SerializedName("seconds")
    private int mSeconds;

    public int getMessageId() {
        return mMessageId;
    }

    public void setMessageId(int messageId) {
        mMessageId = messageId;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setParentId(int parentId) {
        mParentId = parentId;
    }

    public int getToUserId() {
        return mToUserId;
    }

    public void setToUserId(int toUserId) {
        mToUserId = toUserId;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public String getParentText() {
        return mParentText;
    }

    public void setParentText(String parentText) {
        mParentText = parentText;
    }

    public int getSeconds() {
        return mSeconds;
    }

    public void setSeconds(int seconds) {
        mSeconds = seconds;
    }
}
