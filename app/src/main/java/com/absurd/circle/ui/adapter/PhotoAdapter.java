package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Photo;
import com.absurd.circle.ui.activity.GalleryActivity;
import com.absurd.circle.ui.activity.LoadOriginImaegAcitivty;
import com.absurd.circle.ui.activity.MyProfileActivity;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.ui.bean.PhotosBean;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.StringUtil;
import com.android.volley.toolbox.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by absurd on 14-6-10.
 */
public class PhotoAdapter extends BeanAdapter<Photo> {

    private static final String GALLERY_PHOTO_EDIT = "GALLERY_PHOTO_EDIT";

    private FragmentActivity mActivity;

    /**'
     * true for owner
     * false for guest
     */
    private boolean mPermission;

    public PhotoAdapter(Context context, List<Photo> items) {
        super(context, items);
    }

    public PhotoAdapter(FragmentActivity activity, boolean permission) {
        super(activity);
        mActivity = activity;
        this.mPermission = permission;
        if(mPermission){
            Photo photo = new Photo();
            photo.setUrl(GALLERY_PHOTO_EDIT);
            mItems.add(photo);
            notifyDataSetChanged();
        }
    }

    public PhotoAdapter(FragmentActivity activity){
        super(activity);
        mActivity = activity;
    }

    @Override
    public void setItems(List<Photo> items) {
        if(mItems != null) {
            this.mItems = items;
        }
        if(mPermission){
            Photo photo = new Photo();
            photo.setUrl(GALLERY_PHOTO_EDIT);
            this.mItems.add(photo);
        }
        notifyDataSetChanged();
    }

    @Override
    public void addItem(Photo item) {
        if(mPermission) {
            this.mItems.add(mItems.size() - 1, item);
        }else{
            this.mItems.add(item);
        }
        notifyDataSetChanged();
    }

    private class ViewHolder{
        ImageView imageView;
        ImageLoader.ImageContainer mediaRequest;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Photo photo = (Photo)getItem(i);
        ViewHolder holder = null;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_photo,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView)view.findViewById(R.id.iv_item_photo);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
            if(holder.mediaRequest != null){
                holder.mediaRequest.cancelRequest();
            }
        }
        if(!StringUtil.isEmpty(photo.getUrl())) {
            if(photo.getUrl().equals(GALLERY_PHOTO_EDIT)) {
                holder.imageView.setImageDrawable(AppContext.getContext().getResources().getDrawable(R.drawable.ic_add));
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mActivity instanceof MyProfileActivity){
                            ((MyProfileActivity)mActivity).uploadGalleryPhoto();
                        }
                    }
                });
            }else {
                final String thumbnailUrl = ImageUtil.getThumbnailUrl(photo.getUrl(), 200, true);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.imageView.setTag("loading");
                holder.mediaRequest = RequestManager.loadImage(thumbnailUrl,
                        RequestManager.getImageListener(holder.imageView, mMediaDefaultBitmap, mMediaDefaultBitmap,null));

                final Bitmap bitmap;
                if(holder.imageView.getTag().equals("loading")){
                    bitmap = null;
                }else{
                    bitmap = ((BitmapDrawable)holder.imageView.getDrawable()).getBitmap();
                }
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, Serializable> params = new HashMap<String, Serializable>();
                        PhotosBean photos = new PhotosBean();
                        List<Photo> photoList = new ArrayList<Photo>();
                        for(Photo p : mItems){
                            if(!p.getUrl().equals(GALLERY_PHOTO_EDIT)) {
                                photoList.add(p);
                            }
                        }
                        photos.setPhotos(photoList);
                        params.put("photos", photos);
                        params.put("position",i);
                        IntentUtil.startActivity(mActivity,GalleryActivity.class, params);
                    }
                });

                holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(mActivity instanceof MyProfileActivity){
                            ((MyProfileActivity)mActivity).deleteGalleryPhoto(i);
                        }
                        return true;
                    }
                });

            }
        }

        return view;
    }



}
