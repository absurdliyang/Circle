package com.absurd.circle.util;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.client.volley.RequestManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by absurd on 14-5-3.
 */
public class FileUtil {

    public static boolean isExternalStorageMounted() {

        boolean canRead = Environment.getExternalStorageDirectory().canRead();
        boolean onlyRead = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean unMounted = Environment.getExternalStorageState().equals(
                Environment.MEDIA_UNMOUNTED);

        return !(!canRead || onlyRead || unMounted);
    }


    public static String getUploadPicTempFile() {

        if (!isExternalStorageMounted()) {
            return "";
        } else {
            return getSdCardPath() + File.separator + "upload.jpg";
        }
    }

    /**
     * install weiciyuan, open app and login in, Android system will create cache dir.
     * then open cache dir (/sdcard dir/Android/data/org.qii.weiciyuan) with Root Explorer,
     * uninstall weiciyuan and reinstall it, the new weiciyuan app will have the bug it can't
     * read cache dir again, so I have to tell user to delete that cache dir
     */
    private static volatile boolean cantReadBecauseOfAndroidBugPermissionProblem = false;


    private static String getSdCardPath() {
        if (isExternalStorageMounted()) {
            File path = AppContext.getContext().getExternalCacheDir();
            if (path != null) {
                return path.getAbsolutePath();
            }else{
                return "";
            }
            /**
             else {
                if (!cantReadBecauseOfAndroidBugPermissionProblem) {
                    cantReadBecauseOfAndroidBugPermissionProblem = true;
                    final Activity activity = AppContext.getContext().getActivity();
                    if (activity == null || activity.isFinishing()) {
                        GlobalContext.getInstance().getUIHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GlobalContext.getInstance(),
                                        R.string.please_deleted_cache_dir, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                        return "";
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(activity)
                                    .setTitle(R.string.something_error)
                                    .setMessage(R.string.please_deleted_cache_dir)
                                    .setPositiveButton(R.string.ok,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {

                                                }
                                            })
                                    .show();

                        }
                    });
                }
            }
        }

             **/
        }else{
            return "";
        }

    }

    public static String getSrcSavePath(String path){
        String storageState = Environment.getExternalStorageState();
        if(storageState.equals(Environment.MEDIA_MOUNTED)){
            String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + path;
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            return savePath;
        }else{
            return null;
        }
    }

    public  static String getPicPathFromUri(Uri uri, Activity activity) {
        String value = uri.getPath();
        if (value.startsWith("/external")) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else {
            return value;
        }
    }

    public static boolean savePic(File file){
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConstant.SAVE_PHOTO_PATH;
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
        }else{
            return false;
        }

        if (StringUtil.isEmpty(savePath)) {
            AppContext.commonLog.i("Create save photo path error!");
            return false;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "circle_" + timeStamp + ".jpg";
        File out = new File(savePath, fileName);

        try {
            copyFile(file, out);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    private static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } finally {
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
        }
    }

}
