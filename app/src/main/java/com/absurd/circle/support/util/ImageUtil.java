package com.absurd.circle.support.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;

import com.absurd.circle.support.file.FileDownloaderHttpHelper;
import com.absurd.circle.support.http.HttpUtil;

import java.io.File;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Created by absurd on 14-5-10.
 */
public class ImageUtil {

    public static boolean getBitmapFromNetWork(String url, String path, FileDownloaderHttpHelper.DownloadListener downloadListener) {
        for (int i = 0; i < 3; i++) {
            if (HttpUtil.getInstance().executeDownloadTask(url, path, downloadListener)) {
                return true;
            }
            new File(path).delete();
        }

        return false;
    }

    public static boolean isThisBitmapCanRead(String path) {
        File file = new File(path);

        if (!file.exists()) {
            return false;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        if (width == -1 || height == -1) {
            return false;
        }

        return true;
    }


    public static boolean isThisBitmapTooLargeToRead(String path) {
        File file = new File(path);

        if (!file.exists()) {
            return false;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        if (width == -1 || height == -1) {
            return false;
        }

        if (width > getBitmapMaxWidthAndMaxHeight() || height > getBitmapMaxWidthAndMaxHeight()) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isThisPictureGif(String url) {
        if(url != null && !url.isEmpty()){
            if(url.endsWith(".gif")){
                return true;
            }
        }
        return false;
    }

    public static int getBitmapMaxWidthAndMaxHeight() {
        int[] maxSizeArray = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);

        if (maxSizeArray[0] == 0) {
            GLES10.glGetIntegerv(GL11.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);
        }
//        return maxSizeArray[0];
        return 2048;
    }


    public static Bitmap decodeBitmapFromSDCard(String path,
                                                int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);

    }


    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (height > reqHeight && reqHeight != 0) {
                inSampleSize = (int) Math.floor((double) height / (double) reqHeight);
            }

            int tmp = 0;

            if (width > reqWidth && reqWidth != 0) {
                tmp = (int) Math.floor((double) width / (double) reqWidth);
            }

            inSampleSize = Math.max(inSampleSize, tmp);

        }
        int roundedSize;
        if (inSampleSize <= 8) {
            roundedSize = 1;
            while (roundedSize < inSampleSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (inSampleSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

}
