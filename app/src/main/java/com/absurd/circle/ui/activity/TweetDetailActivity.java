package com.absurd.circle.ui.activity;

import android.os.Bundle;

import com.absurd.circle.app.R;
import com.absurd.circle.core.bean.Message;
import com.absurd.circle.ui.fragment.MessageDetailFragment;

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
        setContentView(R.layout.activity_message_detail);
        mAttacher = PullToRefreshAttacher.get(this);
        tweet = (Message)getIntent().getSerializableExtra("messageId");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flyt_comment_container,new MessageDetailFragment(this))
                .commit();


    }

    @Override
    protected String setActionBarTitle() {
        return "详情";
    }


}
