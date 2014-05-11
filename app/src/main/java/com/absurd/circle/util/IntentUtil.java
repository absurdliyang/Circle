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
/**
 * Created by absurd on 14-3-14.
 */
public class IntentUtil {
    public static void startActivity(Context activity,Class<?> cls,Map<String,? extends Serializable> params){
        Intent intent=new Intent();
        intent.setClass(activity,cls);
        if(params != null){
            for(Map.Entry<String,?> entry : params.entrySet()){
                intent.putExtra(entry.getKey(),(Serializable)entry.getValue());
            }
        }
        activity.startActivity(intent);
    }

    public static void startActivity(Context activity, Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(activity,cls);
        activity.startActivity(intent);
    }



    public static void startActivity(Context context,Class<?> cls,String key,Serializable value){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, value);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    public static boolean isIntentSafe(Activity activity, Intent intent) {
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > 0;
    }


}
