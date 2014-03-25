package com.absurd.circle.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by absurd on 14-3-25.
 */
public class ReportMessage implements Serializable{

    @SerializedName("id")
    private int mId;

    @SerializedName("fromuserid")
    private String mFromUserId;

    @SerializedName("fromusername")
    private String mFromUserName;

    @SerializedName("touserid")
    private String mToUserId;

    @SerializedName("messageid")
    private int mMessageId;

    @SerializedName("content")
    private String mContent;

    @SerializedName("date")
    private Date mDate;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("isverify")
    private int mVerify;

    @SerializedName("deviceid")
    private String mDeviceId;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
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

    public int getMessageId() {
        return mMessageId;
    }

    public void setMessageId(int messageId) {
        mMessageId = messageId;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getVerify() {
        return mVerify;
    }

    public void setVerify(int verify) {
        mVerify = verify;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }
}
