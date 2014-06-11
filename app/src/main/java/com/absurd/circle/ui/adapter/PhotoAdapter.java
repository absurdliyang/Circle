package com.absurd.circle.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.absurd.circle.ui.activity.LoadOriginImaegAcitivty;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.StringUtil;
import com.android.volley.toolbox.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by absurd on 14-6-10.
 */
public class PhotoAdapter extends BeanAdapter<Photo> {

    private FragmentActivity mActivity;

    public PhotoAdapter(Context context, List<Photo> items) {
        super(context, items);
    }

    public PhotoAdapter(Context context) {
        super(context);
    }

    public PhotoAdapter(FragmentActivity activity){
        super(activity);
        mActivity = activity;
    }

    @Override
    public void setItems(List<Photo> items) {
        super.setItems(items);
    }

    private class ViewHolder{
        ImageView imageView;
        ImageLoader.ImageContainer mediaRequest;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
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
            if(photo.getUrl().equals("add")) {
                holder.imageView.setImageDrawable(AppContext.getContext().getResources().getDrawable(R.drawable.ic_add));
                holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
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
                        Intent intent = new Intent(mContext, LoadOriginImaegAcitivty.class);
                        intent.putExtra("mediaUrl", photo.getUrl());
                        if(bitmap != null) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] bytes = stream.toByteArray();
                            intent.putExtra("thumbnailBitmap", bytes);
                        }
                        mActivity.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_in);
                    }
                });
            }
        }

        return view;
    }
}
