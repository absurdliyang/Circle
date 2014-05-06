
package com.absurd.circle.data.client.volley;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.util.ImageUtil;
import com.android.volley.toolbox.ImageLoader;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Issac on 7/19/13.
 */
public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        return ImageUtil.getBitmapSize(bitmap);
    }

    @Override
    public Bitmap getBitmap(String url) {
        //AppContext.commonLog.i("get " + url + " cache");
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
        //AppContext.commonLog.i(url + " put to cache");
    }
}
