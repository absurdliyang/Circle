package com.absurd.circle.data.test;

import com.absurd.circle.data.model.Tweet;
import com.absurd.circle.data.service.TweetService;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-3-15.
 */
public class TweetServiceTest extends BaseTestCase {
    private TweetService mTweetService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mTweetService = new TweetService(getContext());
    }

    public void testGetTweets() throws Exception {
        mTweetService.getTweets(new TableQueryCallback<Tweet>() {
            @Override
            public void onCompleted(List<Tweet> tweets, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if(tweets != null){
                    for(Tweet t : tweets){
                        mLog.d(t.getContent());
                    }
                }else{
                    mLog.d("tweets is null");
                }
            }
        });
        Thread.sleep(5000);

    }
}
