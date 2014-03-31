package com.absurd.circle.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-3-25.
 */
public class AppVer implements Serializable {

    @Expose
    @SerializedName("id")
    private int mId;

    @Expose
    @SerializedName("appver")
    private double mAppVerNumber;

    @Expose
    @SerializedName("updatecontent")
    private String mUpdateContent;

    @Expose
    @SerializedName("isforce")
    private boolean mForce;

    public double getAppVerNumber() {
        return mAppVerNumber;
    }

    public void setAppVerNumber(double appVerNumber) {
        mAppVerNumber = appVerNumber;
    }

    public String getUpdateContent() {
        return mUpdateContent;
    }

    public void setUpdateContent(String updateContent) {
        mUpdateContent = updateContent;
    }

    public boolean isForce() {
        return mForce;
    }

    public void setForce(boolean force) {
        mForce = force;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
