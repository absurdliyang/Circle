package com.absurd.circle.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.absurd.circle.data.model.Photo;
import com.absurd.circle.ui.fragment.LoadingOriginImageFragment;

import java.util.List;

/**
 * Created by absurd on 14-6-14.
 */
public class ImagePagerAdapter extends FragmentPagerAdapter {


    private List<Photo> mPhotos;
    public ImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ImagePagerAdapter(FragmentManager fm, List<Photo> urls){
        super(fm);
        this.mPhotos = urls;
    }

    @Override
    public Fragment getItem(int position) {
        LoadingOriginImageFragment fragment = new LoadingOriginImageFragment();
        fragment.setImageUrl(mPhotos.get(position).getUrl());
        return fragment;
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }
}
