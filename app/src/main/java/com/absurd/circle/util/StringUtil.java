package com.absurd.circle.util;

import java.text.DecimalFormat;

/**
 * Created by absurd on 14-3-26.
 */
public class StringUtil {

    public static boolean isUrl(String str){
        if(str.startsWith("http")){
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str){
        if(str == null || str.equals("")){
            return true;
        }
        return false;
    }

    public static String parseDistance(double distance){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(distance) + " km";
    }
}
