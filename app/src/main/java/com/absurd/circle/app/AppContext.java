package com.absurd.circle.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;

import com.absurd.circle.cache.CacheService;
import com.absurd.circle.data.model.Position;
import com.absurd.circle.data.model.User;
import com.absurd.circle.im.manager.XmppConnectionManager;
import com.absurd.circle.im.reciever.ChatBroadcastReciever;
import com.absurd.circle.im.service.ChatService;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.absurd.circle.util.SharedPreferenceUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by absurd on 14-3-11.
 */
public class AppContext extends Application{


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
    public static int unReadCommentNum;
    public static int unReadPraiseNum;
    public static HashMap<String, Integer> unReadUserMessageNums;

    public static FragmentActivity currentActivity;



    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance();
        cacheService = CacheService.getInstance(this);
        xmppConnectionManager = XmppConnectionManager.getInstance();

        notificationNum = sharedPreferenceUtil.getNotificationNum();
        unReadCommentNum = sharedPreferenceUtil.getUnReadCommentNum();
        unReadPraiseNum = sharedPreferenceUtil.getUnReadPraiseNum();
        unReadUserMessageNums = sharedPreferenceUtil.getUnReadUserMessages();

        AppContext.commonLog.i("notificationNum --> " + notificationNum + " unReadCommentNum --> " + unReadCommentNum + " unReadPraiseNum --> " + unReadPraiseNum);

        // Register ChatBroadcastReciever to let ChatService always running
        // on backgound
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        ChatBroadcastReciever reciever = new ChatBroadcastReciever();
        registerReceiver(reciever, filter);

        // Start chat service
        Intent chatServiceIntent = new Intent(this, ChatService.class);
        this.startService(chatServiceIntent);
    }

    public static void logUnReadUserMessages(){
        Set<String> key = unReadUserMessageNums.keySet();
        for (Iterator it = key.iterator(); it.hasNext(); ) {
            String s = (String) it.next();
            if (s.contains("userMessage")) {
                AppContext.commonLog.i(unReadUserMessageNums.get(s));
            }
        }

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
