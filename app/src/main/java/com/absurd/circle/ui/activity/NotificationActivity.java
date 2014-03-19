package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.fragment.NotificationListFragment;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class NotificationActivity extends BaseActivity implements RefreshableActivity{
    private PullToRefreshAttacher mAttacher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAttacher = PullToRefreshAttacher.get(this);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container,new NotificationListFragment())
                .commit();
    }

    @Override
    protected String setActionBarTitle() {
        return "消息中心";
    }


    @Override
    public PullToRefreshAttacher getAttacher() {
        return mAttacher;
    }
}
