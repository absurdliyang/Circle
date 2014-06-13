package com.absurd.circle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.adapter.ImagePagerAdapter;

import java.util.List;

/**
 * Created by absurd on 14-6-13.
 */
public class GalleryActivity extends BaseActivity{
    private ViewPager mViewPager;

    private List<String> mUrls;


    @Override
    protected String actionBarTitle() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        mUrls = (List<String>)intent.getSerializableExtra("urls");

        mViewPager = (ViewPager)findViewById(R.id.vp_gallery);
        ImagePagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager(), mUrls);
        mViewPager.setAdapter(adapter);
    }


}
