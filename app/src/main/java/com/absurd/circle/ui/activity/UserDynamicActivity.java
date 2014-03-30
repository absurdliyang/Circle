package com.absurd.circle.ui.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.ui.fragment.NotificationListFragment;
import com.absurd.circle.ui.fragment.UserMessageListFragment;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class UserDynamicActivity extends BaseActivity implements RefreshableActivity{

    private PullToRefreshAttacher mPullToRefreshAttacher;

    public String userId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = (String)getIntent().getExtras().get("userId");
        mPullToRefreshAttacher = PullToRefreshAttacher.get(this);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, new UserMessageListFragment())
                .commit();
    }



    @Override
    protected String actionBarTitle() {
        if(AppContext.auth != null && AppContext.auth.getUserId().equals(userId)){
            return "我的动态";
        }else{
            return "动态详情";
        }

    }

    @Override
    public PullToRefreshAttacher getAttacher() {
        return mPullToRefreshAttacher;
    }
}
