package com.absurd.circle.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-4-22.
 */
public class QQUser implements Serializable {
    @Expose
    @SerializedName("is_yellow_year_vip")
    private String mIsYellowYearVip;

    @Expose
    @SerializedName("ret")
    private int mRet;

    @Expose
    @SerializedName("figureurl_qq_1")
    private String mFigureurlQq1;

    @Expose
    @SerializedName("figureurl_qq_2")
    private String mFigureurlQq2;

    @Expose
    @SerializedName("nickname")
    private String mNickname;

    @Expose
    @SerializedName("yellow_vip_level")
    private String mYellowVipLevel;

    @Expose
    @SerializedName("msg")
    private String mMsg;

    @Expose
    @SerializedName("figureurl_1")
    private String mFigureUrl1;

    @Expose
    @SerializedName("vip")
    private String mVip;

    @Expose
    @SerializedName("level")
    private String mLevel;

    @Expose
    @SerializedName("figureurl_2")
    private String mFigureUrl2;

    @Expose
    @SerializedName("is_yellow_vip")
    private String mIsYellowVip;

    @Expose
    @SerializedName("gender")
    private String mGender;

    @Expose
    @SerializedName("figureurl")
    private String mFigureUrl;


    public String getIsYellowYearVip() {
        return mIsYellowYearVip;
    }

    public void setIsYellowYearVip(String isYellowYearVip) {
        mIsYellowYearVip = isYellowYearVip;
    }

    public int getRet() {
        return mRet;
    }

    public void setRet(int ret) {
        mRet = ret;
    }

    public String getFigureurlQq2() {
        return mFigureurlQq2;
    }

    public void setFigureurlQq2(String figureurlQq2) {
        mFigureurlQq2 = figureurlQq2;
    }

    public String getFigureurlQq1() {
        return mFigureurlQq1;
    }

    public void setFigureurlQq1(String figureurlQq1) {
        mFigureurlQq1 = figureurlQq1;
    }

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String nickname) {
        mNickname = nickname;
    }

    public String getYellowVipLevel() {
        return mYellowVipLevel;
    }

    public void setYellowVipLevel(String yellowVipLevel) {
        mYellowVipLevel = yellowVipLevel;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }

    public String getFigureUrl1() {
        return mFigureUrl1;
    }

    public void setFigureUrl1(String figureUrl1) {
        mFigureUrl1 = figureUrl1;
    }

    public String getLevel() {
        return mLevel;
    }

    public void setLevel(String level) {
        mLevel = level;
    }

    public String getVip() {
        return mVip;
    }

    public void setVip(String vip) {
        mVip = vip;
    }

    public String getFigureUrl2() {
        return mFigureUrl2;
    }

    public void setFigureUrl2(String figureUrl2) {
        mFigureUrl2 = figureUrl2;
    }

    public String getIsYellowVip() {
        return mIsYellowVip;
    }

    public void setIsYellowVip(String isYellowVip) {
        mIsYellowVip = isYellowVip;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getFigureUrl() {
        return mFigureUrl;
    }

    public void setFigureUrl(String figureUrl) {
        mFigureUrl = figureUrl;
    }
}
