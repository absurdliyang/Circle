package com.absurd.circle.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.absurd.circle.ui.fragment.LoadingOriginImageFragment;

import java.util.List;

/**
 * Created by absurd on 14-6-14.
 */
public class ImagePagerAdapter extends FragmentPagerAdapter {


    private List<String> mUrls;
    private LoadingOriginImageFragment mFragment = new LoadingOriginImageFragment();

    public ImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ImagePagerAdapter(FragmentManager fm, List<String> urls){
        super(fm);
        this.mUrls = urls;
    }

    @Override
    public Fragment getItem(int position) {
        mFragment.setImageUrl(mUrls.get(position));
        return mFragment;
    }

    @Override
    public int getCount() {
        return mUrls.size();
    }
}
