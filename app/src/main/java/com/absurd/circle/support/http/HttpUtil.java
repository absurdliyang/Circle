package com.absurd.circle.support.http;


import com.absurd.circle.support.file.FileDownloaderHttpHelper;

public class HttpUtil {

    private static HttpUtil httpUtility = new HttpUtil();

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        return httpUtility;
    }


    public boolean executeDownloadTask(String url, String path, FileDownloaderHttpHelper.DownloadListener downloadListener) {
        return !Thread.currentThread().isInterrupted() && new JavaHttpUtil().doGetSaveFile(url, path, downloadListener);
    }

}

