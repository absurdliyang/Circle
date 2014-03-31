package com.absurd.circle.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-3-25.
 */
public class Group implements Serializable {

    @Expose
    @SerializedName("id")
    private int mId;

    @Expose
    @SerializedName("name")
    private String mName;

    @Expose
    @SerializedName("total")
    private int mTotal;

    @Expose
    @SerializedName("current")
    private int mCurrent;

    @Expose
    @SerializedName("description")
    private String mDescription;

    @Expose
    @SerializedName("userid")
    private int mUserId;

    @Expose
    @SerializedName("latitude")
    private double mLatitude;

    @Expose
    @SerializedName("longitude")
    private double mLongitude;

    @Expose
    @SerializedName("imageurl")
    private String mImageUrl;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int total) {
        mTotal = total;
    }

    public int getCurrent() {
        return mCurrent;
    }

    public void setCurrent(int current) {
        mCurrent = current;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
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

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
