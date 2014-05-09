package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.fragment.NotificationFragment;


public class NotificationActivity extends BaseActivity {

    public NotificationActivity(){
        setRightBtnStatus(RIGHT_TEXT);
    }

    NotificationFragment mNotificationFragment = new NotificationFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, mNotificationFragment)
                .commit();
    }

    @Override
    protected String actionBarTitle() {
        return "消息中心";
    }

    @Override
    protected String setRightBtnTxt() {
        return "清空";

    }

    @Override
    public void onRightBtnClicked(View view) {
        super.onRightBtnClicked(view);
        AppContext.cacheService.userMessageDBManager.deleteAll();
        mNotificationFragment.clearList();
    }
}
