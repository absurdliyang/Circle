package com.absurd.circle.util;

import java.text.DecimalFormat;

/**
 * Created by absurd on 14-3-26.
 */
public class StringUtil {

    public static boolean isUrl(String str){
        if(str == null){
            return false;
        }
        if(str.startsWith("http")){
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str){
        return str == null || "".equals(str.trim());
    }

    public static String parseDistance(double distance){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(distance) + " km";
    }
}
