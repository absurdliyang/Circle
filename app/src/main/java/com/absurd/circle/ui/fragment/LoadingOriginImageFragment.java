package com.absurd.circle.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.RequestManager;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by absurd on 14-6-14.
 */
public class LoadingOriginImageFragment extends Fragment {


    private PhotoView mPhotoView;
    private Bitmap mThumbnailBitmap;
    private String mOriginalImageUrl;
    private Bitmap mBitmap;
    private ProgressBar mLoadingPb;

    private Bitmap mMediaDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_media)).getBitmap();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gallery_general_layout,null);

        mPhotoView = (PhotoView)rootView.findViewById(R.id.animation);
        mLoadingPb = (ProgressBar)rootView.findViewById(R.id.pb_loading_image);
        mLoadingPb.setVisibility(View.VISIBLE);


        if(mThumbnailBitmap != null){
            mPhotoView.setImageBitmap(mThumbnailBitmap);
        }else{
            AppContext.commonLog.i("bitmap is null");
        }

        mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                LoadingOriginImageFragment.this.getActivity().onBackPressed();
            }
        });

        if(mOriginalImageUrl == null){
            mLoadingPb.setVisibility(View.INVISIBLE);
            loadImagError();
        }else {
            RequestManager.loadImage(mOriginalImageUrl,
                    new ImageLoader.ImageListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loadImagError();
                        }

                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            if (response.getBitmap() != null) {
                                mLoadingPb.setVisibility(View.INVISIBLE);
                                mBitmap = response.getBitmap();
                                if (!isImmediate && mMediaDefaultBitmap != null) {
                                    /**
                                     TransitionDrawable transitionDrawable = new TransitionDrawable(
                                     new Drawable[]{
                                     new BitmapDrawable(AppContext.getContext().getResources(), mMediaDefaultBitmap),
                                     new BitmapDrawable(AppContext.getContext().getResources(),
                                     response.getBitmap())
                                     }
                                     );
                                     transitionDrawable.setCrossFadeEnabled(true);
                                     mPhotoView.setImageDrawable(transitionDrawable);
                                     transitionDrawable.startTransition(100);
                                     **/
                                    mPhotoView.setImageBitmap(mBitmap);
                                } else {
                                    mPhotoView.setImageBitmap(response.getBitmap());
                                }
                            } else {

                            }
                        }
                    }
            );
        }
        return rootView;
    }

    private void loadImagError(){
        String strMention = AppContext.getContext().getString(R.string.image_downloading_failed);
        Toast.makeText(LoadingOriginImageFragment.this.getActivity(), strMention, Toast.LENGTH_SHORT).show();
    }

    public void setImageUrl(String url){
        this.mOriginalImageUrl = url;
    }

}
