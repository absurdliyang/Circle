package com.absurd.circle.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by absurd on 14-3-14.
 */
public class User implements Serializable{

    @SerializedName("id")
    private int mId;

    @SerializedName("userid")
    private String mUserId;

    @SerializedName("name")
    private String mName;

    @SerializedName("loginname")
    private String mLoginName;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("sex")
    private String mSex;

    @SerializedName("age")
    private Date mAge;

    @SerializedName("permission")
    private int mPermission;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("qq")
    private String mQq;

    @SerializedName("phone")
    private String mPhone;

    @SerializedName("location")
    private String mLocation;

    @SerializedName("date")
    private String mDate;

    @SerializedName("avatar")
    private String mAvatar;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("token")
    private String mToken;

    @SerializedName("channelUri")
    private String mChannelUri;

    @SerializedName("osname")
    private String mOsName;

    @SerializedName("loginType")
    private int mLoginType;

    @SerializedName("hobby")
    private String mHobby;

    @SerializedName("profession")
    private String mProfession;

    @SerializedName("backgroundImage")
    private String mBackgroundImage;

    @SerializedName("city")
    private String mCity;

    @SerializedName("lastlogindate")
    private Date mLastLoginDate;

    @SerializedName("corpse")
    private int mCorpse;

    @SerializedName("appver")
    private String appVer;

    @SerializedName("isanonymity")
    private int mIsAnnoymity;


    @Override
    public String toString() {
        return mName + "------------" + mUserId;
    }

    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLoginName() {
        return mLoginName;
    }

    public void setLoginName(String loginName) {
        mLoginName = loginName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String sex) {
        mSex = sex;
    }

    public Date getAge() {
        return mAge;
    }

    public void setAge(Date age) {
        mAge = age;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public int getPermission() {
        return mPermission;
    }

    public void setPermission(int permission) {
        mPermission = permission;
    }

    public String getQq() {
        return mQq;
    }

    public void setQq(String qq) {
        mQq = qq;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public String getOsName() {
        return mOsName;
    }

    public void setOsName(String osName) {
        mOsName = osName;
    }

    public String getChannelUri() {
        return mChannelUri;
    }

    public void setChannelUri(String channelUri) {
        mChannelUri = channelUri;
    }

    public int getLoginType() {
        return mLoginType;
    }

    public void setLoginType(int loginType) {
        mLoginType = loginType;
    }

    public String getHobby() {
        return mHobby;
    }

    public void setHobby(String hobby) {
        mHobby = hobby;
    }

    public String getProfession() {
        return mProfession;
    }

    public void setProfession(String profession) {
        mProfession = profession;
    }

    public String getBackgroundImage() {
        return mBackgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        mBackgroundImage = backgroundImage;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public Date getLastLoginDate() {
        return mLastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        mLastLoginDate = lastLoginDate;
    }

    public int getCorpse() {
        return mCorpse;
    }

    public void setCorpse(int corpse) {
        mCorpse = corpse;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public int getIsAnnoymity() {
        return mIsAnnoymity;
    }

    public void setIsAnnoymity(int isAnnoymity) {
        mIsAnnoymity = isAnnoymity;
    }



}
