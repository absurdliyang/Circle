package com.absurd.circle.ui.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.fragment.NearbyUserFragment;
import com.absurd.circle.ui.fragment.UserMessageListFragment;

public class NearbyPeopleActivity extends BaseActivity {

    private NearbyUserFragment mNearbyUserFragment;

    public NearbyPeopleActivity(){
        setRightBtnStatus(RIGHT_MORE_BTN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        mNearbyUserFragment = new NearbyUserFragment();
        fm.beginTransaction().replace(R.id.container, mNearbyUserFragment)
                .commit();
    }

    @Override
    protected String actionBarTitle() {
        return "附近的人";
    }

    @Override
    public void onMoreClicked(View view) {
        super.onMoreClicked(view);
        mNearbyUserFragment.onMoreClicked(view);
    }
}
