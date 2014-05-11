package com.absurd.circle.support.asyncdrawable;

/**
 * Created by absurd on 14-5-10.
 */
public class BitmapDownloader {

    static volatile boolean pauseDownloadWork = false;

    static final Object pauseDownloadWorkLock = new Object();
    /**
     * Pause any ongoing background work. This can be used as a temporary
     * measure to improve performance. For example background work could
     * be paused when a ListView or GridView is being scrolled using a
     * {@link android.widget.AbsListView.OnScrollListener} to keep
     * scrolling smooth.
     * <p/>
     * If work is paused, be sure setPauseDownloadWork(false) is called again
     * before your fragment or activity is destroyed (for example during
     * {@link android.app.Activity#onPause()}), or there is a risk the
     * background thread will never finish.
     */
    public void setPauseDownloadWork(boolean pauseWork) {
        synchronized (pauseDownloadWorkLock) {
            BitmapDownloader.pauseDownloadWork = pauseWork;
            if (!BitmapDownloader.pauseDownloadWork) {
                pauseDownloadWorkLock.notifyAll();
            }
        }
    }

}
