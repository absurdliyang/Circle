package com.absurd.circle.data.client.volley;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.util.CacheUtil;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.LogFactory;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by absurd on 14-3-15.
 */
public class RequestManager {
    private static CommonLog mLog = LogFactory.createLog();

    public static RequestQueue mRequestQueue = newRequestQueue();

    // 取运行内存阈值的1/3作为图片缓存
    private static final int MEM_CACHE_SIZE = 1024 * 1024 * ((ActivityManager) AppContext.getContext()
            .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() / 3;

    private static ImageLoader mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(
            MEM_CACHE_SIZE));

    private static DiskBasedCache mDiskCache = (DiskBasedCache) mRequestQueue.getCache();

    private RequestManager(){

    }

    private static Cache openCache() {
        return new DiskBasedCache(CacheUtil.getExternalCacheDir(AppContext.getContext()),
                10 * 1024 * 1024);
    }

    private static RequestQueue newRequestQueue() {
        RequestQueue requestQueue = new RequestQueue(openCache(), new BasicNetwork(new HurlStack()));
        requestQueue.start();
        return requestQueue;
    }

    public static void addRequest(Request request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public static File getCachedImageFile(String url) {
        return mDiskCache.getFileForKey(url);
    }

    public static ImageLoader.ImageContainer loadImage(String requestUrl,
                                                       ImageLoader.ImageListener imageListener) {
        return loadImage(requestUrl, imageListener, 0, 0);
    }

    public static ImageLoader.ImageContainer loadImage(String requestUrl,
                                                       ImageLoader.ImageListener imageListener, int maxWidth, int maxHeight) {
        return mImageLoader.get(requestUrl, imageListener, maxWidth, maxHeight);
    }

    public static ImageLoader.ImageListener getImageListener(final ImageView view, final Bitmap defaultImageBitmap,
                                                             final Bitmap errorImageBitmap, final BitmapFilter bitmapFilter) {
        return new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageBitmap != null) {
                    if (bitmapFilter != null) {
                        view.setImageBitmap(bitmapFilter.filter(errorImageBitmap));
                    }else{
                        view.setImageBitmap(errorImageBitmap);
                    }
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    //mLog.i("get iamge success");
                    if (!isImmediate && defaultImageBitmap != null) {
                        TransitionDrawable transitionDrawable;
                        if(bitmapFilter != null) {
                            transitionDrawable = new TransitionDrawable(
                                    new Drawable[]{
                                            new BitmapDrawable(AppContext.getContext().getResources(),bitmapFilter.filter(defaultImageBitmap)),
                                            new BitmapDrawable(AppContext.getContext().getResources(),bitmapFilter.filter(response.getBitmap()))
                                    }
                            );
                        }else{
                            transitionDrawable = new TransitionDrawable(
                                    new Drawable[]{
                                            new BitmapDrawable(AppContext.getContext().getResources(),defaultImageBitmap),
                                            new BitmapDrawable(AppContext.getContext().getResources(),response.getBitmap())
                                    }
                            );
                        }
                        transitionDrawable.setCrossFadeEnabled(true);
                        view.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(100);
                    } else {
                        if(bitmapFilter != null) {
                            view.setImageBitmap(bitmapFilter.filter(response.getBitmap()));
                        }else{
                            view.setImageBitmap(response.getBitmap());
                        }
                    }
                } else if (defaultImageBitmap != null) {
                    if(bitmapFilter != null) {
                        view.setImageBitmap(bitmapFilter.filter(defaultImageBitmap));
                    }else{
                        view.setImageBitmap(defaultImageBitmap);
                    }
                }
            }
        };
    }


}
