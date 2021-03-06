package com.absurd.circle.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-3-25.
 */
public class AppRecommend implements Serializable {

    @Expose
    @SerializedName("id")
    private int mId;

    @Expose
    @SerializedName("name")
    private String mName;

    @Expose
    @SerializedName("dec")
    private String mDec;

    @Expose
    @SerializedName("number")
    private String mNumber;

    @Expose
    @SerializedName("icon")
    private String mIcon;

    @Expose
    @SerializedName("link")
    private String mLink;

    @Expose
    @SerializedName("isShow")
    private boolean mShow;

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

    public String getDec() {
        return mDec;
    }

    public void setDec(String dec) {
        mDec = dec;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public boolean isShow() {
        return mShow;
    }

    public void setShow(boolean show) {
        mShow = show;
    }
}
