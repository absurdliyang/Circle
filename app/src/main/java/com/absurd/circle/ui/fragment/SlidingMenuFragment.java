package com.absurd.circle.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.ui.activity.ContactActivity;
import com.absurd.circle.ui.activity.HomeActivity;
import com.absurd.circle.ui.activity.ImageDetailActivity;
import com.absurd.circle.ui.activity.NotificationActivity;
import com.absurd.circle.ui.activity.SettingActivity;
import com.absurd.circle.util.IntentUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SlidingMenuFragment extends Fragment {
    private HomeActivity mHomeActivity;

    private ImageView mAvatarIv;

    public SlidingMenuFragment(HomeActivity homeActivity){
        this.mHomeActivity = homeActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sliding_menu,null);
        rootView.findViewById(R.id.llyt_drawer_menu_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeActivity.toggle();
            }
        });
        rootView.findViewById(R.id.llyt_drawer_menu_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeActivity.toggle();
                IntentUtil.startActivity(mHomeActivity, NotificationActivity.class);
            }
        });
        rootView.findViewById(R.id.llyt_drawer_menu_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeActivity.toggle();
                IntentUtil.startActivity(mHomeActivity, ContactActivity.class);
            }
        });
        rootView.findViewById(R.id.llyt_drawer_menu_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeActivity.toggle();
                IntentUtil.startActivity(mHomeActivity, SettingActivity.class);
            }
        });
        mAvatarIv = (ImageView)rootView.findViewById(R.id.iv_drawer_user_avatar);
        RequestManager.loadImage("http://qlogo4.store.qq.com/qzone/1271320063/1271320063/100?1318045820",
                RequestManager.getImageListener(mAvatarIv,null,null));
        mAvatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SlidingMenuFragment.this.getActivity(), ImageDetailActivity.class);
                SlidingMenuFragment.this.startActivity(intent);
            }
        });
        return rootView;
    }
}
