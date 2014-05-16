package com.absurd.circle.util;



import java.io.Serializable;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;

/**
 * Created by absurd on 14-3-14.
 */
public class IntentUtil {
    public static void startActivity(FragmentActivity context,Class<?> cls,Map<String,? extends Serializable> params){
        Intent intent=new Intent();
        intent.setClass(context,cls);
        if(params != null){
            for(Map.Entry<String,?> entry : params.entrySet()){
                intent.putExtra(entry.getKey(),(Serializable)entry.getValue());
            }
        }
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_in);

    }

    public static void startActivity(FragmentActivity context, Class<?> cls,boolean b){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_in);
    }

    public static void startActivity(FragmentActivity context, Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_in);
    }




    public static void startActivity(FragmentActivity context,Class<?> cls,String key,Serializable value){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, value);
        intent.putExtras(bundle);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_in);

    }

    public static boolean isIntentSafe(Activity activity, Intent intent) {
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > 0;
    }


}
