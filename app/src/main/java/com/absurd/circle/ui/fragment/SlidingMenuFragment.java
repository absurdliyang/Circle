package com.absurd.circle.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.ui.activity.ContactActivity;
import com.absurd.circle.ui.activity.DiscoveryActivity;
import com.absurd.circle.ui.activity.HomeActivity;
import com.absurd.circle.ui.activity.MyProfileActivity;
import com.absurd.circle.ui.activity.NotificationActivity;
import com.absurd.circle.ui.activity.SettingActivity;
import com.absurd.circle.ui.activity.UserDynamicActivity;
import com.absurd.circle.ui.activity.UserProfileActivity;
import com.absurd.circle.ui.adapter.UserMessageAdapter;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.StringUtil;

public class SlidingMenuFragment extends Fragment {
    protected Bitmap mAvatarDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_avatar)).getBitmap();

    private HomeActivity mHomeActivity;

    private ImageView mAvatarIv;

    private TextView mUsernameTv;

    private TextView mNotificationTv;

    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Init HomeActivity
        mHomeActivity = (HomeActivity)getActivity();

        mRootView = inflater.inflate(R.layout.fragment_sliding_menu,null);
        mRootView.findViewById(R.id.llyt_drawer_menu_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeActivity.toggle();
            }
        });

        mAvatarIv = (ImageView)mRootView.findViewById(R.id.iv_drawer_user_avatar);
        mUsernameTv = (TextView)mRootView.findViewById(R.id.tv_drawer_username);
        if(AppContext.auth != null) {
            invalidateView();
        }
        return mRootView;
    }

    public void invalidateView(){
        if(AppContext.auth != null) {
            if (!StringUtil.isEmpty(AppContext.auth.getAvatar())) {
                RequestManager.loadImage(AppContext.auth.getAvatar(),
                        RequestManager.getImageListener(mAvatarIv, mAvatarDefaultBitmap, mAvatarDefaultBitmap, new BitmapFilter() {
                            @Override
                            public Bitmap filter(Bitmap bitmap) {
                                return ImageUtil.roundBitmap(bitmap);
                            }
                        })
                );
            }
            mAvatarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtil.startActivity(SlidingMenuFragment.this.getActivity(), MyProfileActivity.class);
                }
            });
            mUsernameTv.setText(AppContext.auth.getName());
        }

        mRootView.findViewById(R.id.llyt_drawer_menu_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeActivity.toggle();
                IntentUtil.startActivity(mHomeActivity, NotificationActivity.class);
            }
        });
        mRootView.findViewById(R.id.llyt_drawer_menu_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeActivity.toggle();
                IntentUtil.startActivity(mHomeActivity, ContactActivity.class);
            }
        });
        mRootView.findViewById(R.id.llyt_drawer_menu_discovery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeActivity.toggle();
                IntentUtil.startActivity(mHomeActivity, DiscoveryActivity.class);
            }
        });
        mRootView.findViewById(R.id.llyt_drawer_menu_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeActivity.toggle();
                SettingActivity.homeActivity = mHomeActivity;
                IntentUtil.startActivity(mHomeActivity, SettingActivity.class);
            }
        });


        mNotificationTv = (TextView)mRootView.findViewById(R.id.tv_notification_num);
        if(AppContext.notificationNum == 0){
            mNotificationTv.setVisibility(View.GONE);
        }else {
            mNotificationTv.setVisibility(View.VISIBLE);
            mNotificationTv.setText(AppContext.notificationNum + "");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        invalidateView();
    }
}
