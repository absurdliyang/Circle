package com.absurd.circle.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-3-25.
 */
public class Comment extends Message implements Serializable {

    @Expose
    @SerializedName("messageid")
    private int mMessageId;

    @Expose
    @SerializedName("parentid")
    private int mParentId;

    @Expose
    @SerializedName("touserid")
    private String mToUserId;

    @Expose
    @SerializedName("state")
    private int mState;


    private String mParentText;

    @Expose
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

    public String getToUserId() {
        return mToUserId;
    }

    public void setToUserId(String toUserId) {
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

    @Override
    public String toString() {
        return "Message{" +
                "mId=" + mId +
                ", mContent='" + mContent + '\'' +
                ", mContentType=" + mContentType +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mWeiboId='" + mWeiboId + '\'' +
                ", mLocationDec='" + mLocationDec + '\'' +
                ", mUserId='" + mUserId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mMediaUrl='" + mMediaUrl + '\'' +
                ", mMediaType=" + mMediaType +
                ", mDate=" + mDate +
                ", mCommentDate=" + mCommentDate +
                ", mCommentCount=" + mCommentCount +
                ", mPraiseCount=" + mPraiseCount +
                ", mStrUser='" + mStrUser + '\'' +
                ", mMessagetType=" + mMessagetType +
                ", mUser=" + mUser +
                ", mMessageId=" + mMessageId +
                ", mParentId=" + mParentId +
                ", mToUserId='" + mToUserId + '\'' +
                ", mState=" + mState +
                ", mParentText='" + mParentText + '\'' +
                ", mSeconds=" + mSeconds +
                '}';

    }
}
