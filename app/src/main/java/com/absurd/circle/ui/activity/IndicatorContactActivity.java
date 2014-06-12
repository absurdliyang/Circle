package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.adapter.ContactPagerAdapter;
import com.absurd.circle.ui.fragment.FollowersFragment;
import com.absurd.circle.ui.indicator.TabPageIndicator;

/**
 * Created by absurd on 14-6-12.
 */
public class IndicatorContactActivity extends BaseActivity {

    protected ContactPagerAdapter mAdapter;
    protected ViewPager mPager;
    protected TabPageIndicator mIndicator;


    public IndicatorContactActivity(){
        setRightBtnStatus(RIGHT_BLANK);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mAdapter = new ContactPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager)findViewById(R.id.vp_contact);
        mPager.setAdapter(mAdapter);
        mIndicator = (TabPageIndicator)findViewById(R.id.tpi_contact);
        mIndicator.setViewPager(mPager);

    }

    @Override
    protected String actionBarTitle() {
        return "联系人";
    }


}
