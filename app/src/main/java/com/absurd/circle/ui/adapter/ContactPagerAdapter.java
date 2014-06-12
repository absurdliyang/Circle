package com.absurd.circle.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.absurd.circle.ui.fragment.BlackListFragment;
import com.absurd.circle.ui.fragment.FollowersFragment;
import com.absurd.circle.ui.fragment.FunsFragment;

public class ContactPagerAdapter extends FragmentPagerAdapter{
    protected static final String[] CONTENT = new String[]{"关注","粉丝","黑名单"};

    private int mCount = CONTENT.length;

    public ContactPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }


    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        Fragment ft = null;
        switch(arg0){
            case 0:
                ft = new FollowersFragment();
                break;
            case 1:
                ft = new FunsFragment();
                break;
            case 2:
                ft = new BlackListFragment();
                break;
        }
        return ft;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return ContactPagerAdapter.CONTENT[position % CONTENT.length];
    }


}