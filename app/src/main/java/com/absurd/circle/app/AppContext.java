package com.absurd.circle.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.absurd.circle.cache.CacheService;
import com.absurd.circle.data.model.Position;
import com.absurd.circle.data.model.User;
import com.absurd.circle.im.manager.XmppConnectionManager;
import com.absurd.circle.im.reciever.ChatBroadcastReciever;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.absurd.circle.util.SharedPreferenceUtil;

/**
 * Created by absurd on 14-3-11.
 */
public class AppContext extends Application{

    public static final Boolean DEBUG = true;

    private static Context mContext;

    public static CommonLog azureLog = LogFactory.createLog(AppConstant.AZURE_MOBILE_TAG);
    public static CommonLog commonLog = LogFactory.createLog(AppConstant.TAG);

    public static SharedPreferenceUtil sharedPreferenceUtil;
    public static CacheService cacheService;
    public static XmppConnectionManager xmppConnectionManager;

    public static User auth = new User();
    public static String token;
    public static String userId;

    public static Position lastPosition;

    public static int notificationNum;



    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance();
        cacheService = CacheService.getInstance(this);
        xmppConnectionManager = XmppConnectionManager.getInstance();

        notificationNum = sharedPreferenceUtil.getNotificationNum();

        // Register ChatBroadcastReciever to let ChatService always running
        // on backgound
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        ChatBroadcastReciever reciever = new ChatBroadcastReciever();
        registerReceiver(reciever, filter);
    }

    public static Context getContext(){
        return mContext;
    }

    public static boolean isAuthed(){
        if(auth == null || auth.getId() == 0){
            return false;
        }
        return true;
    }
}
