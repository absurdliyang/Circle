package com.absurd.circle.app;

import android.app.Application;
import android.content.Context;

import com.absurd.circle.data.model.User;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;

/**
 * Created by absurd on 14-3-11.
 */
public class AppContext extends Application{
    private static Context mContext;

    public static CommonLog azureLog = LogFactory.createLog(AppConstant.AZURE_MOBILE_TAG);
    public static CommonLog commonLog = LogFactory.createLog(AppConstant.TAG);

    public static User auth = new User();
    public static String token;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
