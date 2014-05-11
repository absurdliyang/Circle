package com.absurd.circle.util;

import com.absurd.circle.app.AppConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by absurd on 14-5-10.
 */
public class ImageProxyUtil  {

    public static String proxy(String content,double imageWidth){
        Pattern pattern = Pattern.compile("<img[^>]*?src=[\"']http://(.*?.*?)[\"'].*?>");
        Matcher matcher = pattern.matcher(content);
        String result;

        result = matcher.replaceAll("<img src=\"" + AppConstant. IMAGE_PROXY_URL + "?w=" + imageWidth + "&u=http://$1\"/>");

        return result;
    }

    public static String proxy(String content){
        return proxy(content, 408);
    }


    public static String getTarget(String url){
        return AppConstant.IMAGE_PROXY_URL + "?w=276&u=" + url;
    }

    public static String getCoverImage(String url){
        return AppConstant.IMAGE_PROXY_URL + "?w=240&u="+ url;
    }

    public static String getImageByWidth(String url, double width){
        return AppConstant.IMAGE_PROXY_URL + "?w=" + Double.toString(width) + "&u=" + (url);
    }
    public static String getImageByHeight(String url, double height){
        return AppConstant.IMAGE_PROXY_URL + "?h=" + Double.toString(height) + "&u=" + (url);
    }

    public static String getOriginal(String url){
        if (StringUtil.isEmpty(url)){
            return "";
        }

        url = url.replace("-xgimg", "");
        int first = url.indexOf("&u=") + 3;

        if (first >= 3){
            String result = "";
            result = url.substring(first, url.length() - first);
            return result;
        }else{
            return url;
        }
    }
}
