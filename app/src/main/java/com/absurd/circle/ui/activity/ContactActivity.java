package com.absurd.circle.ui.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.absurd.circle.app.R;
import com.absurd.circle.ui.fragment.BlackListFragment;
import com.absurd.circle.ui.fragment.FollowersFragment;
import com.absurd.circle.ui.fragment.NotificationListFragment;

public class ContactActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container,new FollowersFragment())
                .commit();
    }

    @Override
    protected String actionBarTitle() {
        return "关注";
    }



}
