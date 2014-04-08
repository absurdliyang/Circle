package com.absurd.circle.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by absurd on 14-4-8.
 */
public class FunsCount {
    @Expose
    @SerializedName("funscount")
    private int mFunsCount;

    @Expose
    @SerializedName("followcount")
    private int mFollowCount;

    @Expose
    @SerializedName("messagecount")
    private int mMessageCount;

    public int getFunsCount() {
        return mFunsCount;
    }

    public void setFunsCount(int funsCount) {
        mFunsCount = funsCount;
    }

    public int getMessageCount() {
        return mMessageCount;
    }

    public void setMessageCount(int messageCount) {
        mMessageCount = messageCount;
    }

    public int getFollowCount() {
        return mFollowCount;
    }

    public void setFollowCount(int followCount) {
        mFollowCount = followCount;
    }


    @Override
    public String toString() {
        return "FunsCount{" +
                "mFunsCount=" + mFunsCount +
                ", mFollowCount=" + mFollowCount +
                ", mMessageCount=" + mMessageCount +
                '}';
    }
}
