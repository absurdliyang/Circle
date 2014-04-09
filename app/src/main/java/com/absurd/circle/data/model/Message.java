package com.absurd.circle.data.model;

import com.absurd.circle.data.util.JsonUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by absurd on 14-3-14.
 */
public class Message implements Serializable{
    @Expose
    @SerializedName("id")
    protected int mId;

    @Expose
    @SerializedName("content")
    protected String mContent;

    @Expose
    @SerializedName("contenttype")
    protected int mContentType;

    @Expose
    @SerializedName("latitude")
    protected double mLatitude;

    @Expose
    @SerializedName("longitude")
    protected double mLongitude;

    @Expose
    @SerializedName("weiboid")
    protected String mWeiboId;

    @Expose
    @SerializedName("locationdec")
    protected String mLocationDec;

    @Expose
    @SerializedName("userid")
    protected String mUserId;

    @Expose
    @SerializedName("title")
    protected String mTitle;

    @Expose
    @SerializedName("mediaurl")
    protected String mMediaUrl;

    @Expose
    @SerializedName("mediatype")
    protected int mMediaType;

    @Expose
    @SerializedName("date")
    protected Date mDate;

    @Expose
    @SerializedName("commentdate")
    protected Date mCommentDate;

    @Expose
    @SerializedName("commentcount")
    protected int mCommentCount;

    @Expose
    @SerializedName("praisecount")
    protected int mPraiseCount;

    @Expose
    @SerializedName("User")
    protected String mStrUser;

    @Expose
    @SerializedName("messagetype")
    protected int mMessagetType;

    protected User mUser;

    public User getUser(){
        User user = null;
        if(mUser != null)
            return mUser;
        if(mStrUser != null){
            user = JsonUtil.fromJson(mStrUser,User.class);
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

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getContentType() {
        return mContentType;
    }

    public void setContentType(int contentType) {
        mContentType = contentType;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getLocationDec() {
        return mLocationDec;
    }

    public void setLocationDec(String locationDec) {
        mLocationDec = locationDec;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getWeiboId() {
        return mWeiboId;
    }

    public void setWeiboId(String weiboId) {
        mWeiboId = weiboId;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        mMediaUrl = mediaUrl;
    }

    public int getMediaType() {
        return mMediaType;
    }

    public void setMediaType(int mediaType) {
        mMediaType = mediaType;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getCommentDate() {
        return mCommentDate;
    }

    public void setCommentDate(Date commentDate) {
        mCommentDate = commentDate;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }

    public int getPraiseCount() {
        return mPraiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        mPraiseCount = praiseCount;
    }

    public String getStrUser() {
        return mStrUser;
    }

    public void setUser(String user) {
        mStrUser = user;
    }

    public int getMessagetType() {
        return mMessagetType;
    }

    public void setMessagetType(int messagetType) {
        mMessagetType = messagetType;
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
                '}';
    }
}
