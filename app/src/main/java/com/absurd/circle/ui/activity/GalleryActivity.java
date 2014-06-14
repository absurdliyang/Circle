package com.absurd.circle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Photo;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.adapter.ImagePagerAdapter;
import com.absurd.circle.ui.bean.PhotosBean;
import com.absurd.circle.util.FileUtil;
import com.absurd.circle.util.StringUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by absurd on 14-6-13.
 */
public class GalleryActivity extends BaseActivity{
    private ViewPager mViewPager;

    private List<Photo> mPhotos;
    private TextView mGalleryPositionTv;
    private TextView mGallerySumTv;

    private int mCurrengViewPosition;

    public GalleryActivity(){
        setRightBtnStatus(RIGHT_TEXT);
    }

    @Override
    protected String actionBarTitle() {
        return "相册";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        PhotosBean bean = (PhotosBean)intent.getSerializableExtra("photos");
        if(bean != null){
            mPhotos = bean.getPhotos();
        }
        mCurrengViewPosition = (Integer)intent.getSerializableExtra("position");

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mViewPager = (ViewPager)findViewById(R.id.vp_gallery);
        ImagePagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager(), mPhotos);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mCurrengViewPosition);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrengViewPosition = position;
                mGalleryPositionTv.setText(position + 1 + "");
            }
        });
        mGalleryPositionTv = (TextView)findViewById(R.id.tv_gallery_position);
        mGallerySumTv = (TextView)findViewById(R.id.tv_gallery_sum);
        mGallerySumTv.setText(mPhotos.size() + "");
        mGalleryPositionTv.setText(mCurrengViewPosition + 1 + "");

    }


    @Override
    protected String setRightBtnTxt() {
        return "保存";
    }

    @Override
    public void onRightBtnClicked(View view) {
        super.onRightBtnClicked(view);
        savePic();
    }

    private void savePic(){
        File file = RequestManager.getCachedImageFile(mPhotos.get(mCurrengViewPosition).getUrl());
        if(FileUtil.savePic(file)){
            Toast.makeText(AppContext.getContext(),R.string.save_pic_success,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(AppContext.getContext(),R.string.save_pic_failed,Toast.LENGTH_SHORT).show();
        }
    }
}
