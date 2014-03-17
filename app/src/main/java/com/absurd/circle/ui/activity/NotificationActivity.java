package com.absurd.circle.ui.activity;

import android.os.Bundle;

import com.absurd.circle.app.R;

public class NotificationActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    @Override
    protected String setActionBarTitle() {
        return "消息中心";
    }


}
