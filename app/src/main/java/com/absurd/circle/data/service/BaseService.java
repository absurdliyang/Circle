package com.absurd.circle.data.service;

import android.content.Context;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.client.AzureClient;
import com.absurd.circle.data.model.BlackList;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.model.Praise;
import com.absurd.circle.data.model.ReportMessage;
import com.absurd.circle.data.model.Score;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.model.UserLocation;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;

/**
 * Created by absurd on 14-3-14.
 */
public class BaseService{
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_MESSAGE= "Message";
    public static final String TABLE_USER_MESSAGE = "UserMessage";
    public static final String TABLE_COMMENT = "Comment";
    public static final String TABLE_FOLLOW = "Follow";
    public static final String TABLE_BLACK_LIST = "BlackList";
    public static final String TABLE_PRAISE = "Praise";
    public static final String TABLE_REPORT_MESSAGE = "ReportMessage";
    public static final String TABLE_SCORE = "Score";
    public static final String TABLE_USER_LOCATION = "UserLocation";


    protected CommonLog mLog = LogFactory.createLog(AppConstant.TAG);
    protected MobileServiceClient mClient = AzureClient.getInstance(AppContext.getContext());


    public BaseService(){
    }

    public BaseService(String token) {
        MobileServiceUser user = new MobileServiceUser("");
        user.setAuthenticationToken(token);
        mClient.setCurrentUser(user);
    }


    protected MobileServiceTable<User> getUserTable(){
        return mClient.getTable(TABLE_USERS,User.class);
    }

    protected MobileServiceTable<Message> getMessageTable(){
        return mClient.getTable(TABLE_MESSAGE,Message.class);
    }

    protected MobileServiceTable<UserLocation> getUserLocationTable(){
        return mClient.getTable(TABLE_USER_LOCATION,UserLocation.class);
    }

    protected MobileServiceTable<UserMessage> getUserMessageTable(){
        return mClient.getTable(TABLE_USER_MESSAGE,UserMessage.class);
    }

    protected MobileServiceTable<Comment> getCommentTable(){
        return mClient.getTable(TABLE_COMMENT, Comment.class);
    }

    protected MobileServiceTable<Follow> getFollowTable(){
        return mClient.getTable(TABLE_FOLLOW, Follow.class);
    }

    protected MobileServiceTable<BlackList> getBlackListTable(){
        return mClient.getTable(TABLE_BLACK_LIST,BlackList.class);
    }

    protected MobileServiceTable<Praise> getPraiseTable(){
        return mClient.getTable(TABLE_PRAISE,Praise.class);
    }

    protected MobileServiceTable<ReportMessage> getReportTable(){
        return mClient.getTable(TABLE_REPORT_MESSAGE,ReportMessage.class);
    }

    protected MobileServiceTable<Score> getScoreTable(){
        return mClient.getTable(TABLE_SCORE, Score.class);
    }


}
