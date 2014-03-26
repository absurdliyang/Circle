package com.absurd.circle.util;

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
}
