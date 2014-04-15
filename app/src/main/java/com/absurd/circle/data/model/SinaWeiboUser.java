package com.absurd.circle.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-4-16.
 */
public class SinaWeiboUser implements Serializable {

    @Expose
    @SerializedName("id")
    private String mId;

    @Expose
    @SerializedName("idstr")
    private String mIdStr;

    @Expose
    @SerializedName("screen_name")
    private String mScreenName;

    @Expose
    @SerializedName("name")
    private String mName;

    @Expose
    @SerializedName("province")
    private String mProvince;

    @Expose
    @SerializedName("city")
    private String mCity;

    @Expose
    @SerializedName("location")
    private String mLocation;

    @Expose
    @SerializedName("description")
    private String mDescription;

    @Expose
    @SerializedName("url")
    private String mUrl;

    @Expose
    @SerializedName("profile_image_url")
    private String mProfileImageUrl;

    @Expose
    @SerializedName("domain")
    private String mDomain;

    @Expose
    @SerializedName("weihao")
    private String mWeihao;

    @Expose
    @SerializedName("gender")
    private String mGender;

    @Expose
    @SerializedName("followers_count")
    private int mFollowersCount;

    @Expose
    @SerializedName("friends_count")
    private int mFriendsCount;

    @Expose
    @SerializedName("statuses_count")
    private int mStatusesCount;

    @Expose
    @SerializedName("favourites_count")
    private long mFavouritesCount;

    @Expose
    @SerializedName("created_at")
    private String mCreatedAt;

    @Expose
    @SerializedName("following")
    private boolean mFollowing;

    @Expose
    @SerializedName("allow_all_act_msg")
    private boolean isAllowAllActMsg;

    @Expose
    @SerializedName("geo_enabled")
    private boolean isGeoEnabled;

    @Expose
    @SerializedName("verified")
    private boolean isVerified;

    @Expose
    @SerializedName("verified_type")
    private String mVerifiedType;

    @Expose
    @SerializedName("remark")
    private String mRemark;

    @Expose
    @SerializedName("allow_all_comment")
    private boolean isAllowAllComment;

    @Expose
    @SerializedName("avatar_large")
    private String mAvatarLarge;

    @Expose
    @SerializedName("verified_reason")
    private String mVerifiedReason;

    @Expose
    @SerializedName("follow_me")
    private boolean isFollowMe;

    @Expose
    @SerializedName("online_status")
    private int mOnLineStatus;

    @Expose
    @SerializedName("bi_followers_count")
    private int mBiFollowersCount;

    @Expose
    @SerializedName("lang")
    private String mLang;


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getIdStr() {
        return mIdStr;
    }

    public void setIdStr(String idStr) {
        mIdStr = idStr;
    }

    public String getScreenName() {
        return mScreenName;
    }

    public void setScreenName(String screenName) {
        mScreenName = screenName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getProvince() {
        return mProvince;
    }

    public void setProvince(String province) {
        mProvince = province;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getProfileImageUrl() {
        return mProfileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        mProfileImageUrl = profileImageUrl;
    }

    public String getDomain() {
        return mDomain;
    }

    public void setDomain(String domain) {
        mDomain = domain;
    }

    public String getWeihao() {
        return mWeihao;
    }

    public void setWeihao(String weihao) {
        mWeihao = weihao;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public int getFollowersCount() {
        return mFollowersCount;
    }

    public void setFollowersCount(int followersCount) {
        mFollowersCount = followersCount;
    }

    public int getFriendsCount() {
        return mFriendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        mFriendsCount = friendsCount;
    }

    public int getStatusesCount() {
        return mStatusesCount;
    }

    public void setStatusesCount(int statusesCount) {
        mStatusesCount = statusesCount;
    }

    public long getFavouritesCount() {
        return mFavouritesCount;
    }

    public void setFavouritesCount(long favouritesCount) {
        mFavouritesCount = favouritesCount;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public boolean isFollowing() {
        return mFollowing;
    }

    public void setFollowing(boolean following) {
        mFollowing = following;
    }

    public boolean isAllowAllActMsg() {
        return isAllowAllActMsg;
    }

    public void setAllowAllActMsg(boolean isAllowAllActMsg) {
        this.isAllowAllActMsg = isAllowAllActMsg;
    }

    public boolean isGeoEnabled() {
        return isGeoEnabled;
    }

    public void setGeoEnabled(boolean isGeoEnabled) {
        this.isGeoEnabled = isGeoEnabled;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getVerifiedType() {
        return mVerifiedType;
    }

    public void setVerifiedType(String verifiedType) {
        mVerifiedType = verifiedType;
    }

    public String getRemark() {
        return mRemark;
    }

    public void setRemark(String remark) {
        mRemark = remark;
    }

    public boolean isAllowAllComment() {
        return isAllowAllComment;
    }

    public void setAllowAllComment(boolean isAllowAllComment) {
        this.isAllowAllComment = isAllowAllComment;
    }

    public String getAvatarLarge() {
        return mAvatarLarge;
    }

    public void setAvatarLarge(String avatarLarge) {
        mAvatarLarge = avatarLarge;
    }

    public String getVerifiedReason() {
        return mVerifiedReason;
    }

    public void setVerifiedReason(String verifiedReason) {
        mVerifiedReason = verifiedReason;
    }

    public boolean isFollowMe() {
        return isFollowMe;
    }

    public void setFollowMe(boolean isFollowMe) {
        this.isFollowMe = isFollowMe;
    }

    public int getOnLineStatus() {
        return mOnLineStatus;
    }

    public void setOnLineStatus(int onLineStatus) {
        mOnLineStatus = onLineStatus;
    }

    public int getBiFollowersCount() {
        return mBiFollowersCount;
    }

    public void setBiFollowersCount(int biFollowersCount) {
        mBiFollowersCount = biFollowersCount;
    }

    public String getLang() {
        return mLang;
    }

    public void setLang(String lang) {
        mLang = lang;
    }


    @Override
    public String toString() {
        return "SinaWeiboUser{" +
                "mId='" + mId + '\'' +
                ", mIdStr='" + mIdStr + '\'' +
                ", mScreenName='" + mScreenName + '\'' +
                ", mName='" + mName + '\'' +
                ", mProvince='" + mProvince + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mProfileImageUrl='" + mProfileImageUrl + '\'' +
                ", mDomain='" + mDomain + '\'' +
                ", mWeihao='" + mWeihao + '\'' +
                ", mGender='" + mGender + '\'' +
                ", mFollowersCount=" + mFollowersCount +
                ", mFriendsCount=" + mFriendsCount +
                ", mStatusesCount=" + mStatusesCount +
                ", mFavouritesCount=" + mFavouritesCount +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                ", mFollowing=" + mFollowing +
                ", isAllowAllActMsg=" + isAllowAllActMsg +
                ", isGeoEnabled=" + isGeoEnabled +
                ", isVerified=" + isVerified +
                ", mVerifiedType='" + mVerifiedType + '\'' +
                ", mRemark='" + mRemark + '\'' +
                ", isAllowAllComment=" + isAllowAllComment +
                ", mAvatarLarge='" + mAvatarLarge + '\'' +
                ", mVerifiedReason='" + mVerifiedReason + '\'' +
                ", isFollowMe=" + isFollowMe +
                ", mOnLineStatus=" + mOnLineStatus +
                ", mBiFollowersCount=" + mBiFollowersCount +
                ", mLang='" + mLang + '\'' +
                '}';
    }
}
