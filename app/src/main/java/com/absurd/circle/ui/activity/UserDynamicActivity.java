package com.absurd.circle.ui.activity;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.fragment.UserMessageListFragment;


public class UserDynamicActivity extends BaseActivity {


    public String userId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = (String)getIntent().getExtras().get("userId");
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

}
