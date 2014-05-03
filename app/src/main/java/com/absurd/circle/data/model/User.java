package com.absurd.circle.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by absurd on 14-3-14.
 */
public class User implements Serializable, Cloneable{

    @Expose
    @SerializedName("id")
    private int mId;

    @Expose
    @SerializedName("userid")
    private String mUserId;

    @Expose
    @SerializedName("name")
    private String mName;

    @Expose
    @SerializedName("loginname")
    private String mLoginName;

    @Expose
    @SerializedName("password")
    private String mPassword;

    @Expose
    @SerializedName("sex")
    private String mSex;

    @Expose
    @SerializedName("age")
    private Date mAge;

    @Expose
    @SerializedName("permission")
    private int mPermission;

    @Expose
    @SerializedName("email")
    private String mEmail;

    @Expose
    @SerializedName("qq")
    private String mQq;

    @Expose
    @SerializedName("phone")
    private String mPhone;

    @Expose
    @SerializedName("location")
    private String mLocation;

    @Expose
    @SerializedName("date")
    private Date mDate;

    @Expose
    @SerializedName("avatar")
    private String mAvatar;

    @Expose
    @SerializedName("description")
    private String mDescription;

    @Expose
    @SerializedName("token")
    private String mToken;

    @Expose
    @SerializedName("channelUri")
    private String mChannelUri;

    @Expose
    @SerializedName("osname")
    private String mOsName;

    @Expose
    @SerializedName("loginType")
    private int mLoginType;

    @Expose
    @SerializedName("hobby")
    private String mHobby;

    @Expose
    @SerializedName("profession")
    private String mProfession;

    @Expose
    @SerializedName("backgroundImage")
    private String mBackgroundImage;

    @Expose
    @SerializedName("city")
    private String mCity;

    @Expose
    @SerializedName("lastlogindate")
    private Date mLastLoginDate;

    @Expose
    @SerializedName("corpse")
    private int mCorpse;

    @Expose
    @SerializedName("appver")
    private String mAppVer;

    @Expose
    @SerializedName("isanonymity")
    private int mIsAnnoymity;

    @Expose
    @SerializedName("level")
    private int mLevel;



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
        return mAppVer;
    }

    public void setAppVer(String appVer) {
        this.mAppVer = appVer;
    }

    public int getIsAnnoymity() {
        return mIsAnnoymity;
    }

    public void setIsAnnoymity(int isAnnoymity) {
        mIsAnnoymity = isAnnoymity;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    @Override
    public Object clone(){
        User user = null;
        try {
            user = (User)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "mId=" + mId +
                ", mUserId='" + mUserId + '\'' +
                ", mName='" + mName + '\'' +
                ", mLoginName='" + mLoginName + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mSex='" + mSex + '\'' +
                ", mAge=" + mAge +
                ", mPermission=" + mPermission +
                ", mEmail='" + mEmail + '\'' +
                ", mQq='" + mQq + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mDate=" + mDate +
                ", mAvatar='" + mAvatar + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mToken='" + mToken + '\'' +
                ", mChannelUri='" + mChannelUri + '\'' +
                ", mOsName='" + mOsName + '\'' +
                ", mLoginType=" + mLoginType +
                ", mHobby='" + mHobby + '\'' +
                ", mProfession='" + mProfession + '\'' +
                ", mBackgroundImage='" + mBackgroundImage + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mLastLoginDate=" + mLastLoginDate +
                ", mCorpse=" + mCorpse +
                ", mAppVer='" + mAppVer + '\'' +
                ", mIsAnnoymity=" + mIsAnnoymity +
                ", mLevel=" + mLevel +
                '}';
    }
}
