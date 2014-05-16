package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.util.IntentUtil;

public class DiscoveryActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_friendmessage:
                IntentUtil.startActivity(this,UserDynamicActivity.class,true);
                break;
            case R.id.iv_nearpeople:
                IntentUtil.startActivity(this,NearbyPeopleActivity.class,true);
                break;
            case R.id.iv_talent:
                IntentUtil.startActivity(this,NearbyTalentsActivity.class,true);
                break;
            default:
                break;

        }
    }

    @Override
    protected String actionBarTitle() {
        return "发现";
    }


}
