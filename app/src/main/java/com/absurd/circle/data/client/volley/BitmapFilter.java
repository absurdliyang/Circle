package com.absurd.circle.data.client.volley;

import android.graphics.Bitmap;

/**
 * Created by absurd on 14-3-27.
 */
public interface BitmapFilter {
    /**
     * Filter the bitmap to other graphic
     * @param bitmap
     * @return
     */
    public Bitmap filter(Bitmap bitmap);
}
