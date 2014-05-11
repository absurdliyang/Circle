package com.absurd.circle.ui.activity.gallery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.util.AnimationUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by absurd on 14-5-10.
 */
public class GalleryAnimationActivity extends FragmentActivity {


    private ArrayList<AnimationRect> rectList;

    private ArrayList<String> urls = new ArrayList<String>();

    private ViewPager pager;

    private TextView position;

    private int initPosition;

    private View background;

    private ColorDrawable backgroundColor;

    public static Intent newIntent(Message msg, ArrayList<AnimationRect> rectList,
                                   int initPosition) {
        Intent intent = new Intent(AppContext.getContext(), GalleryAnimationActivity.class);
        intent.putExtra("msg", msg);
        intent.putExtra("rect", rectList);
        intent.putExtra("position", initPosition);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galleryactivity_animation_layout);

        rectList = getIntent().getParcelableArrayListExtra("rect");
        Message msg = (Message)getIntent().getSerializableExtra("msg");
        urls.add(msg.getMediaUrl());

        position = (TextView) findViewById(R.id.position);
        initPosition = getIntent().getIntExtra("position", 0);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager()));
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                GalleryAnimationActivity.this.position.setText(String.valueOf(position + 1));
            }
        });
        pager.setCurrentItem(getIntent().getIntExtra("position", 0));
        pager.setOffscreenPageLimit(1);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());

        TextView sum = (TextView) findViewById(R.id.sum);
        sum.setText(String.valueOf(urls.size()));

        background = AnimationUtil.getAppContentView(this);
    }

    private HashMap<Integer, ContainerFragment> fragmentMap
            = new HashMap<Integer, ContainerFragment>();

    private boolean alreadyAnimateIn = false;

    private class ImagePagerAdapter extends FragmentPagerAdapter {

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            ContainerFragment fragment = fragmentMap.get(position);
            if (fragment == null) {

                boolean animateIn = (initPosition == position) && !alreadyAnimateIn;
                fragment = ContainerFragment
                        .newInstance(urls.get(position), rectList.get(position), animateIn,
                                initPosition == position);
                alreadyAnimateIn = true;
                fragmentMap.put(position, fragment);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return urls.size();
        }
    }


    public void showBackgroundImmediately() {
        if (background.getBackground() == null) {
            backgroundColor = new ColorDrawable(Color.BLACK);
            background.setBackground(backgroundColor);
        }
    }

    public ObjectAnimator showBackgroundAnimate() {
        backgroundColor = new ColorDrawable(Color.BLACK);
        background.setBackground(backgroundColor);
        ObjectAnimator bgAnim = ObjectAnimator
                .ofInt(backgroundColor, "alpha", 0, 255);
        bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                background.setBackground(backgroundColor);
            }
        });
        return bgAnim;
    }

    @Override
    public void onBackPressed() {

        ContainerFragment fragment = fragmentMap.get(pager.getCurrentItem());
        if (fragment != null && fragment.canAnimateCloseActivity()) {
            backgroundColor = new ColorDrawable(Color.BLACK);
            ObjectAnimator bgAnim = ObjectAnimator.ofInt(backgroundColor, "alpha", 0);
            bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    background.setBackground(backgroundColor);
                }
            });
            bgAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    GalleryAnimationActivity.super.onBackPressed();
                    overridePendingTransition(-1, -1);
                }
            });
            fragment.animationExit(bgAnim);
        } else {
            super.onBackPressed();
        }
    }

}
