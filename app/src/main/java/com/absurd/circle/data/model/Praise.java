package com.absurd.circle.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by absurd on 14-3-25.
 */
public class Praise implements Serializable {
    @SerializedName("id")
    private int mId;

    @SerializedName("messageid")
    private int mMessageId;

    @SerializedName("touserid")
    private int mToUserId;

    @SerializedName("state")
    private boolean mState;

    @SerializedName("userid")
    private String mUserId;

    @SerializedName("date")
    private Date mDate;



}
