package com.absurd.circle.data.service;

import android.content.Context;

import com.absurd.circle.data.model.Tweet;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-3-14.
 */
public class TweetService extends BaseService{
    public TweetService(Context context){
        super(context);
    }
    public TweetService(){
        super();
    }

    public void getTweets(TableQueryCallback<Tweet> callback){
        mTweetTable.where().parameter("type","id").parameter("id","15").execute(callback);
    }

}
