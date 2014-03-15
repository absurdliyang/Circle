package com.absurd.circle.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by absurd on 14-3-14.
 */
public class Tweet implements Serializable{
    private static final int WEIBO = 1;
    private static final int BUSINESS = 2;
    private static final int HELP = 3;
    private static final int LIVE = 4;
    private static final int SALES = 5;
    private static final int FOOD = 6;
    private static final int PARTY = 7;
    private static final int FRIEND = 8;
    private static final int SHOW = 9;
    private static final int MOOD = 10;

    public enum TweetType {
        WEIBO,
        BUSINESS,
        HELP,
        LIVE,
        SALES,
        FOOD,
        PARTY,
        FRIEND,
        SHOW,
        MOOD
    }

    @SerializedName("id")
    private int mId;

    @SerializedName("content")
    public String mContent;

    @SerializedName("contenttype")
    private int mContentType;

    @SerializedName("latitude")
    private double mLatitude;

    @SerializedName("longitude")
    private double mLongitude;

    @SerializedName("weiboid")
    private String mWeiboId;

    @SerializedName("locationdec")
    private String mLocationDec;

    @SerializedName("userid")
    private int mUserId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("mediaurl")
    private String mMediaUrl;

    @SerializedName("mediatype")
    public int mMediaType;

    @SerializedName("date")
    private Date mDate;

    @SerializedName("commentdate")
    private Date mCommentDate;

    @SerializedName("commentcount")
    private int mCommentCount;

    @SerializedName("praisecount")
    private int mPraiseCount;

    @SerializedName("user")
    private User mUser;

    @SerializedName("messagetype")
    private int mMessagetType;

    public static int getHelp() {
        return HELP;
    }

    public static int getWeibo() {
        return WEIBO;
    }

    public static int getBusiness() {
        return BUSINESS;
    }

    public static int getLive() {
        return LIVE;
    }

    public static int getFood() {
        return FOOD;
    }

    public static int getSales() {
        return SALES;
    }

    public static int getParty() {
        return PARTY;
    }

    public static int getFriend() {
        return FRIEND;
    }

    public static int getMood() {
        return MOOD;
    }

    public static int getShow() {
        return SHOW;
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

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
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

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public int getMessagetType() {
        return mMessagetType;
    }

    public void setMessagetType(int messagetType) {
        mMessagetType = messagetType;
    }
}
