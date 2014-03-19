package com.absurd.circle.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.absurd.circle.app.AppContext;

/**
 * Created by absurd on 14-3-20.
 */
public class NetworkUtil {

    public static boolean isNetConnected(){
        ConnectivityManager cm = (ConnectivityManager) AppContext
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null){
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if(infos != null){
                for(NetworkInfo info : infos){
                    if(info.isConnected())
                        return true;
                }
            }
        }
        return false;
    }


    public static boolean isWifiConnected(){
        ConnectivityManager cm = (ConnectivityManager)AppContext.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null){
            NetworkInfo info  = cm.getActiveNetworkInfo();
            if(info != null && info.getType() == ConnectivityManager.TYPE_WIFI){
            return true;
        }
    }
        return false;
    }


}
