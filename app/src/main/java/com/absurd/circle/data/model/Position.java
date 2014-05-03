package com.absurd.circle.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-3-15.
 */
public class Position implements Serializable{

    @Expose
    @SerializedName("lat")
    private double mLatitude;

    @Expose
    @SerializedName("lng")
    private double mLongitude;

    public Position(){}

    public Position(double lat, double lng){
        this.mLatitude = lat;
        this.mLongitude = lng;
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

    @Override
    public String toString() {
        return "Position{" +
                "mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                '}';
    }
}
