package com.absurd.circle.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by absurd on 14-3-25.
 */
public class Follow implements Serializable{

    @SerializedName("id")
    private int mId;

    @SerializedName("userid")
    private String mUserId;

    @SerializedName("followuserid")
    private String mFollowUserId;

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

    public String getFollowUserId() {
        return mFollowUserId;
    }

    public void setFollowUserId(String followUserId) {
        mFollowUserId = followUserId;
    }
}
