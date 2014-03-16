package com.absurd.circle.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-3-15.
 */
public class UserLocation implements Serializable {
    @SerializedName("id")
    private int mId;

    @SerializedName("userid")
    private String mUserId;

    @SerializedName("latitude")
    private double mLatitude;

    @SerializedName("longitude")
    private double mLongitude;

    @SerializedName("locationdec")
    private String mLocationDec;

    public String getLocationDec() {
        return mLocationDec;
    }

    public void setLocationDec(String locationDec) {
        mLocationDec = locationDec;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
