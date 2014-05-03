package com.absurd.circle.util;

import static android.media.ExifInterface.*;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Color.WHITE;
import static android.graphics.PorterDuff.Mode.DST_IN;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Created by absurd on 14-3-26.
 */
public class ImageUtil {
    private static final String TAG = "ImageUtils";

    /**
     * Get a bitmap from the image path
     *
     * @param imagePath
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final String imagePath) {
        return getBitmap(imagePath, 1);
    }

    /**
     * Get a bitmap from the image path
     *
     * @param imagePath
     * @param sampleSize
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final String imagePath, int sampleSize) {
        final Options options = new Options();
        options.inDither = false;
        options.inSampleSize = sampleSize;

        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(imagePath, "r");
            return BitmapFactory.decodeFileDescriptor(file.getFD(), null,
                    options);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            return null;
        } finally {
            if (file != null)
                try {
                    file.close();
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage(), e);
                }
        }
    }

    /**
     * Get a bitmap from the image
     *
     * @param image
     * @param sampleSize
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final byte[] image, int sampleSize) {
        final Options options = new Options();
        options.inDither = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }

    /**
     * Get scale for image of size and max height/width
     *
     * @param size
     * @param width
     * @param height
     * @return scale
     */
    public static int getScale(Point size, int width, int height) {
        if (size.x > width || size.y > height)
            return Math.max(Math.round((float) size.y / (float) height),
                    Math.round((float) size.x / (float) width));
        else
            return 1;
    }

    /**
     * Get size of image
     *
     * @param imagePath
     * @return size
     */
    public static Point getSize(final String imagePath) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;

        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(imagePath, "r");
            BitmapFactory.decodeFileDescriptor(file.getFD(), null, options);
            return new Point(options.outWidth, options.outHeight);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            return null;
        } finally {
            if (file != null)
                try {
                    file.close();
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage(), e);
                }
        }
    }

    /**
     * Get size of image
     *
     * @param image
     * @return size
     */
    public static Point getSize(final byte[] image) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(image, 0, image.length, options);
        return new Point(options.outWidth, options.outHeight);
    }

    /**
     * Get bitmap with maximum height or width
     *
     * @param imagePath
     * @param width
     * @param height
     * @return image
     */
    public static Bitmap getBitmap(final String imagePath, int width, int height) {
        Point size = getSize(imagePath);
        return getBitmap(imagePath, getScale(size, width, height));
    }

    /**
     * Get bitmap with maximum height or width
     *
     * @param image
     * @param width
     * @param height
     * @return image
     */
    public static Bitmap getBitmap(final byte[] image, int width, int height) {
        Point size = getSize(image);
        return getBitmap(image, getScale(size, width, height));
    }

    /**
     * Get bitmap with maximum height or width
     *
     * @param image
     * @param width
     * @param height
     * @return image
     */
    public static Bitmap getBitmap(final File image, int width, int height) {
        return getBitmap(image.getAbsolutePath(), width, height);
    }

    /**
     * Get a bitmap from the image file
     *
     * @param image
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final File image) {
        return getBitmap(image.getAbsolutePath());
    }

    /**
     * Load a {@link Bitmap} from the given path and set it on the given
     * {@link ImageView}
     *
     * @param imagePath
     * @param view
     */
    public static void setImage(final String imagePath, final ImageView view) {
        setImage(new File(imagePath), view);
    }

    /**
     * Load a {@link Bitmap} from the given {@link File} and set it on the given
     * {@link ImageView}
     *
     * @param image
     * @param view
     */
    public static void setImage(final File image, final ImageView view) {
        Bitmap bitmap = getBitmap(image);
        if (bitmap != null)
            view.setImageBitmap(bitmap);
    }

    /**
     * Round the corners of a {@link Bitmap}
     *
     * @param source
     * @param radius
     * @return rounded corner bitmap
     */
    public static Bitmap roundCorners(final Bitmap source, final float radius) {
        int width = source.getWidth();
        int height = source.getHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(WHITE);

        Bitmap clipped = Bitmap.createBitmap(width, height, ARGB_8888);
        Canvas canvas = new Canvas(clipped);
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius,
                paint);
        paint.setXfermode(new PorterDuffXfermode(DST_IN));

        Bitmap rounded = Bitmap.createBitmap(width, height, ARGB_8888);
        canvas = new Canvas(rounded);
        canvas.drawBitmap(source, 0, 0, null);
        canvas.drawBitmap(clipped, 0, 0, paint);

        source.recycle();
        clipped.recycle();

        return rounded;
    }

    /**
     * from rectangle to round bitmap
     * @param bitmap
     * @return
     */
    public static Bitmap roundBitmap(Bitmap bitmap){
        if(bitmap == null)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 通过文件名获得图片文件
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName){
        FileInputStream in = null;
        Bitmap bitmap = null;
        try{
            in = context.openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(in);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(OutOfMemoryError e){
            e.printStackTrace();
        }finally{
            try{
                if(in != null)
                    in.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    /**
     * 保存图片
     * @param context
     * @param fileName
     * @param bitmap
     * @throws IOException
     */
    public static void saveImage(Context context,String fileName, Bitmap bitmap) throws IOException{
        saveImage(context,fileName,bitmap,100);
    }

    public static void saveImage(Context context, String fileName, Bitmap bitmap, int quality) throws IOException{
        if(bitmap == null || fileName == null || context == null){
            return;
        }
        FileOutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, quality, stream);
        byte[] bytes = stream.toByteArray();
        out.write(bytes);
        out.close();
    }


    /**
     * Read stream to a byte[]
     * @param stream
     * @return
     * @throws IOException
     */
    public static byte[] readStream(InputStream stream) throws IOException{
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while((len = stream.read(buffer)) != -1){
            outStream.write(buffer,0,len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        stream.close();
        return data;
    }


    public static Bitmap getWriteWeiboPictureThumblr(String filePath) {
        try {
            //actionbar button image width and height is 32 dip
            int reqWidth = 32;
            int reqHeight = 32;

            if (!FileUtil.isExternalStorageMounted()) {
                return null;
            }

            boolean fileExist = new File(filePath).exists();

            if (!fileExist) {
                return null;
            }


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;
            options.inInputShareable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            if (bitmap == null) {
                //this picture is broken,so delete it
                new File(filePath).delete();
                return null;
            }


            int[] size = calcResize(bitmap.getWidth(), bitmap.getHeight(), reqWidth, reqHeight);
            if (size[0] > 0 && size[1] > 0) {
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, size[0], size[1], true);
                if (scaledBitmap != bitmap) {
                    bitmap.recycle();
                    bitmap = scaledBitmap;
                }
            }

            /**

            Bitmap roundedBitmap = ImageEditUtility.getRoundedCornerBitmap(bitmap);
            if (roundedBitmap != bitmap) {
                bitmap.recycle();
                bitmap = roundedBitmap;
            }

            int exifRotation = ImageUtility.getFileExifRotation(filePath);
            if (exifRotation != 0) {
                Matrix mtx = new Matrix();
                mtx.postRotate(exifRotation);
                Bitmap adjustedBitmap = Bitmap.createBitmap(roundedBitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), mtx, true);
                if (adjustedBitmap != bitmap) {
                    bitmap.recycle();
                    bitmap = adjustedBitmap;
                }
            }
             **/

            return bitmap;
        } catch (OutOfMemoryError ignored) {
            ignored.printStackTrace();
            return null;
        }
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

    private static int[] calcResize(int actualWidth, int actualHeight, int reqWidth, int reqHeight) {


        int height = actualHeight;
        int width = actualWidth;


        float betweenWidth = ((float) reqWidth) / (float) actualWidth;
        float betweenHeight = ((float) reqHeight) / (float) actualHeight;

        float min = Math.min(betweenHeight, betweenWidth);

        height = (int) (min * actualHeight);
        width = (int) (min * actualWidth);

        return new int[]{width, height};
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


    public static int[] getBitmapSize(String path) {
        int[] result = {-1, -1};
        File file = new File(path);

        if (!file.exists()) {
            return result;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        if (width > 0 && height > 0) {
            result[0] = width;
            result[1] = height;
        }

        return result;
    }

    public static String compressPic(Context context, String picPath, int quality) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;

        switch (quality) {
            case 1:
                return picPath;
            case 2:
                options.inSampleSize = 2;
                break;
            case 3:
                options.inSampleSize = 4;
                break;
            case 4:
                options.inSampleSize = 2;

                if (NetworkUtil.isWifiConnected()) {
                    return picPath;
                }
                break;
        }

        Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
        int exifRotation = ImageUtil.getFileExifRotation(picPath);
        if (exifRotation != 0) {
            //TODO write EXIF instead of rotating bitmap.
            Matrix mtx = new Matrix();
            mtx.postRotate(exifRotation);
            Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), mtx, true);
            if (adjustedBitmap != bitmap) {
                bitmap.recycle();
                bitmap = adjustedBitmap;
            }
        }
        FileOutputStream stream = null;
        String tmp = FileUtil.getUploadPicTempFile();
        try {
            new File(tmp).getParentFile().mkdirs();
            new File(tmp).createNewFile();
            stream = new FileOutputStream(new File(tmp));
        } catch (IOException ignored) {

        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        if (stream != null) {
            try {
                stream.close();
                bitmap.recycle();
            } catch (IOException ignored) {

            }
        }
        return tmp;
    }

    public static int getFileExifRotation(String filePath) {
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(TAG_ORIENTATION,
                    ORIENTATION_NORMAL);
            switch (orientation) {
                case ORIENTATION_ROTATE_90:
                    return 90;
                case ORIENTATION_ROTATE_180:
                    return 180;
                case ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            return 0;
        }
    }


}
