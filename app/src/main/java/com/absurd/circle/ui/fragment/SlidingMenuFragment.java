package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.ContactActivity;
import com.absurd.circle.ui.activity.HomeActivity;
import com.absurd.circle.ui.activity.NotificationActivity;
import com.absurd.circle.ui.activity.SettingActivity;
import com.absurd.circle.util.IntentUtil;

public class SlidingMenuFragment extends Fragment {
    private HomeActivity mHomeActivity;

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
        rootView.findViewById(R.id.llyt_drawer_menu_message).setOnClickListener(new View.OnClickListener() {
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
        return rootView;
    }
}
