package com.absurd.circle.util;

import android.os.Environment;

import com.absurd.circle.app.AppContext;

import java.io.File;

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

}
