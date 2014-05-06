package com.absurd.circle.im.reciever;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.im.service.ChatService;

/**
 * Created by absurd on 14-5-6.
 */
public class ChatBroadcastReciever extends BroadcastReceiver {

    private boolean mIsService = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_TIME_TICK)){
            ActivityManager manager  = (ActivityManager) AppContext.getContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            //AppContext.commonLog.i("start chatBroascastReciever");
            for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                //AppContext.commonLog.i(service.service.getClassName());
                if("com.absurd.circle.im.service.ChatService".equals(service.service.getClassName())){
                    mIsService = true;
                    break;
                }else{
                    mIsService = false;
                }
            }
            //AppContext.commonLog.i("mIsService " + mIsService);
            if(!mIsService){
                Intent i = new Intent(context, ChatService.class);
                context.startService(i);
            }
        }
    }
}
