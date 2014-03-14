package com.absurd.circle.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by absurd on 14-3-11.
 */
public class AppData extends Application{
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
