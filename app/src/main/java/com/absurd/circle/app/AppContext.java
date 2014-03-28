package com.absurd.circle.app;

import android.app.Application;
import android.content.Context;

import com.absurd.circle.data.cache.CacheService;
import com.absurd.circle.data.model.Position;
import com.absurd.circle.data.model.User;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.absurd.circle.util.SharedPreferenceUtil;

/**
 * Created by absurd on 14-3-11.
 */
public class AppContext extends Application{
    private static Context mContext;

    public static CommonLog azureLog = LogFactory.createLog(AppConstant.AZURE_MOBILE_TAG);
    public static CommonLog commonLog = LogFactory.createLog(AppConstant.TAG);

    public static SharedPreferenceUtil sharedPreferenceUtil;
    public static CacheService cacheService;

    public static User auth = new User();
    public static String token;
    public static String userId;

    public static Position lastPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance();
        cacheService = CacheService.getInstance(this);
    }

    public static Context getContext(){
        return mContext;
    }
}
