package com.absurd.circle.support.file;

import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.TextUtils;

import com.absurd.circle.app.AppContext;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: qii
 * Date: 12-8-3
 */
public class FileManager {

    private static final String AVATAR_SMAll = "avatar_small";

    private static final String AVATAR_LARGE = "avatar_large";

    private static final String PICTURE_THUMBNAIL = "picture_thumbnail";

    private static final String PICTURE_BMIDDLE = "picture_bmiddle";

    private static final String PICTURE_LARGE = "picture_large";

    private static final String MAP = "map";

    private static final String COVER = "cover";

    private static final String EMOTION = "emotion";

    private static final String TXT2PIC = "txt2pic";

    private static final String WEBVIEW_FAVICON = "favicon";

    private static final String LOG = "log";

    private static final String CIRCLE = "circle";



    private static String getSdCardPath() {
        if (isExternalStorageMounted()) {
            File path = AppContext.getContext().getExternalCacheDir();
            if (path != null) {
                return path.getAbsolutePath();
            }
        } else {
            return "";
        }

        return "";
    }

    public File getAlbumStorageDir(String albumName) {

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            AppContext.commonLog.i("Directory not created");
        }
        return file;
    }

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

    public static String getKKConvertPicTempFile() {

        if (!isExternalStorageMounted()) {
            return "";
        } else {
            return getSdCardPath() + File.separator + "kk_convert" + System.currentTimeMillis()
                    + ".jpg";
        }
    }

    public static String getLogDir() {
        if (!isExternalStorageMounted()) {
            return "";
        } else {
            String path = getSdCardPath() + File.separator + LOG;
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }
            return path;
        }
    }

    public static String getFilePathFromUrl(String url, FileLocationMethod method) {

        if (!isExternalStorageMounted()) {
            return "";
        }

        if (TextUtils.isEmpty(url)) {
            return "";
        }

        int index = url.indexOf("//");

        String s = url.substring(index + 2);

        String oldRelativePath = s.substring(s.indexOf("/"));

        String newRelativePath = "";
        switch (method) {
            case avatar_small:
                newRelativePath = AVATAR_SMAll + oldRelativePath;
                break;
            case avatar_large:
                newRelativePath = AVATAR_LARGE + oldRelativePath;
                break;
            case picture_thumbnail:
                newRelativePath = PICTURE_THUMBNAIL + oldRelativePath;
                break;
            case picture_bmiddle:
                newRelativePath = PICTURE_BMIDDLE + oldRelativePath;
                break;
            case picture_large:
                newRelativePath = PICTURE_LARGE + oldRelativePath;
                break;
            case emotion:
                String name = new File(oldRelativePath).getName();
                newRelativePath = EMOTION + File.separator + name;
                break;
            case cover:
                newRelativePath = COVER + oldRelativePath;
                break;
            case map:
                newRelativePath = MAP + oldRelativePath;
                break;
        }

        String result = getSdCardPath() + File.separator + newRelativePath;
        if (!result.endsWith(".jpg") && !result.endsWith(".gif") && !result.endsWith(".png")) {
            result = result + ".jpg";
        }

        return result;
    }

    public static String getTxt2picPath() {
        if (!isExternalStorageMounted()) {
            return "";
        }

        String path = getSdCardPath() + File.separator + TXT2PIC;
        File file = new File(path);
        if (file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    public static File createNewFileInSDCard(String absolutePath) {
        if (!isExternalStorageMounted()) {
            AppContext.commonLog.i("sdcard unavailiable");
            return null;
        }

        File file = new File(absolutePath);
        if (file.exists()) {
            return file;
        } else {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try {
                if (file.createNewFile()) {
                    return file;
                }
            } catch (IOException e) {
                AppContext.commonLog.i(e.getMessage());
                return null;

            }

        }
        return null;

    }

    public static String getWebViewFaviconDirPath() {
        if (!TextUtils.isEmpty(getSdCardPath())) {
            String path = getSdCardPath() + File.separator + WEBVIEW_FAVICON + File.separator;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            return path;
        }
        return "";
    }

    public static String getCacheSize() {
        if (isExternalStorageMounted()) {
            String path = getSdCardPath() + File.separator;
            FileSize size = new FileSize(new File(path));
            return size.toString();
        }
        return "0MB";
    }


    public static List<String> getCachePath() {
        List<String> path = new ArrayList<String>();
        if (isExternalStorageMounted()) {
            String thumbnailPath = getSdCardPath() + File.separator + PICTURE_THUMBNAIL;
            String middlePath = getSdCardPath() + File.separator + PICTURE_BMIDDLE;
            String oriPath = getSdCardPath() + File.separator + PICTURE_LARGE;
            String largeAvatarPath = getSdCardPath() + File.separator + AVATAR_LARGE;

            path.add(thumbnailPath);
            path.add(middlePath);
            path.add(oriPath);
            path.add(largeAvatarPath);
        }
        return path;
    }

    public static String getPictureCacheSize() {
        long size = 0L;
        if (isExternalStorageMounted()) {
            String thumbnailPath = getSdCardPath() + File.separator + PICTURE_THUMBNAIL;
            String middlePath = getSdCardPath() + File.separator + PICTURE_BMIDDLE;
            String oriPath = getSdCardPath() + File.separator + PICTURE_LARGE;
            String largeAvatarPath = getSdCardPath() + File.separator + AVATAR_LARGE;
            String smallAvatarPath = getSdCardPath() + File.separator + AVATAR_SMAll;
            String coverPath = getSdCardPath() + File.separator + COVER;

            size += new FileSize(new File(thumbnailPath)).getLongSize();
            size += new FileSize(new File(middlePath)).getLongSize();
            size += new FileSize(new File(oriPath)).getLongSize();
            size += new FileSize(new File(largeAvatarPath)).getLongSize();
            size += new FileSize(new File(smallAvatarPath)).getLongSize();
            size += new FileSize(new File(coverPath)).getLongSize();

        }
        return FileSize.convertSizeToString(size);
    }

    public static boolean deleteCache() {
        String path = getSdCardPath() + File.separator;
        return deleteDirectory(new File(path));
    }

    public static boolean deletePictureCache() {
        String thumbnailPath = getSdCardPath() + File.separator + PICTURE_THUMBNAIL;
        String middlePath = getSdCardPath() + File.separator + PICTURE_BMIDDLE;
        String oriPath = getSdCardPath() + File.separator + PICTURE_LARGE;
        String largeAvatarPath = getSdCardPath() + File.separator + AVATAR_LARGE;
        String smallAvatarPath = getSdCardPath() + File.separator + AVATAR_SMAll;
        String coverPath = getSdCardPath() + File.separator + COVER;

        deleteDirectory(new File(thumbnailPath));
        deleteDirectory(new File(middlePath));
        deleteDirectory(new File(oriPath));
        deleteDirectory(new File(largeAvatarPath));
        deleteDirectory(new File(smallAvatarPath));
        deleteDirectory(new File(coverPath));

        return true;
    }

    private static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static boolean saveToPicDir(String path) {
        if (!isExternalStorageMounted()) {
            return false;
        }

        File file = new File(path);
        String name = file.getName();
        String newPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + CIRCLE
                + File.separator + name;
        try {
            FileManager.createNewFileInSDCard(newPath);
            copyFile(file, new File(newPath));
            forceRefreshSystemAlbum(newPath);
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    public static void forceRefreshSystemAlbum(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        String type = options.outMimeType;

        MediaScannerConnection
                .scanFile(AppContext.getContext(), new String[]{path}, new String[]{type},
                        null);

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
