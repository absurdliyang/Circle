package com.absurd.circle.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.SystemUtil;

public class SettingActivity extends BaseActivity {

    private Button mLogoutBtn;

    private CheckBox mPushNotificationCb;
    private TextView mVersionTv;

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
        mPushNotificationCb = (CheckBox)findViewById(R.id.cb_push_notification);

        mVersionTv = (TextView)findViewById(R.id.tv_version);

        mVersionTv.setText(SystemUtil.getAppVersion());
    }

    private void logout(){
        AppContext.auth = null;
        AppContext.userId = null;
        AppContext.token = null;
        AppContext.lastPosition = null;
        AppContext.sharedPreferenceUtil.clearAll();
        AppContext.cacheService.clearAll();
    }

    private void cleanCache(){
        //AppContext.cacheService.clearAll();
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_play_circle:
                navigateToWeb("http://quanquanshequ.com/play.html");
                break;
            case R.id.btn_privacy:
                navigateToWeb("http://www.365high.cn/privacy.html");
                break;
            case R.id.btn_about_circle:
                navigateToWeb("http://quanquanshequ.com?wp");
                break;
            case R.id.btn_version:
                break;
            case R.id.btn_clean_cache:
                cleanCache();
                break;
            case R.id.btn_push_notification:
                if(mPushNotificationCb.isChecked()){
                    mPushNotificationCb.setChecked(false);
                }else{
                    mPushNotificationCb.setChecked(true);
                }
                break;
            case R.id.btn_send_advice:
                sendAdvice();
                break;
            default:
                break;
        }
    }

    private void sendAdvice(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"absurdliyang@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "body");
        startActivity(Intent.createChooser(emailIntent, null));
    }


    private void navigateToWeb(String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    protected String actionBarTitle() {
        return "设置";
    }




}
