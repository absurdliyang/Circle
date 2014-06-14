package com.absurd.circle.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-6-10.
 */
public class Photo implements Serializable{

    @Expose
    @SerializedName("id")
    private String mId;

    @Expose
    @SerializedName("url")
    private String mUrl;

    @Expose
    @SerializedName("mediaurl")
    private String mMediaUrl;

    @Expose
    @SerializedName("userid")
    private String mUserId;

    @Expose
    @SerializedName("type")
    private int mType;


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getMediaUrl() {
        return mMediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        mMediaUrl = mediaUrl;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mId='" + mId + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mMediaUrl='" + mMediaUrl + '\'' +
                ", mUserId='" + mUserId + '\'' +
                ", mType='" + mType + '\'' +
                '}';
    }
}
