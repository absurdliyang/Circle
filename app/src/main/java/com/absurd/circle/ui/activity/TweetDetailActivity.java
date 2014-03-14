package com.absurd.circle.ui.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.absurd.circle.app.R;
import com.absurd.circle.core.bean.Message;
import com.absurd.circle.ui.fragment.TweetDetailFragment;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class TweetDetailActivity extends BaseActivity {

    private PullToRefreshAttacher mAttacher;
    public Message tweet;

    public PullToRefreshAttacher getAttacher(){
        return mAttacher;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        mAttacher = PullToRefreshAttacher.get(this);
        tweet = (Message)getIntent().getSerializableExtra("messageId");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flyt_comment_container,new TweetDetailFragment(this))
                .commit();


    }


}
