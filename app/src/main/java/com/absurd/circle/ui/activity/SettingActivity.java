package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.util.IntentUtil;

public class SettingActivity extends BaseActivity {

    private Button mLogoutBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mLogoutBtn = (Button)findViewById(R.id.btn_logout);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                IntentUtil.startActivity(SettingActivity.this, LoginActivity.class);
                SettingActivity.this.finish();
            }
        });
    }

    private void logout(){
        AppContext.auth = null;
        AppContext.userId = null;
        AppContext.token = null;
        AppContext.lastPosition = null;
        AppContext.sharedPreferenceUtil.clearAll();
        AppContext.cacheService.clearAll();
    }

    @Override
    protected String actionBarTitle() {
        return "设置";
    }




}
