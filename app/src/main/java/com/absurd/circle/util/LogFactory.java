package com.absurd.circle.util;

/**
 * Created by absurd on 14-3-11.
 */
public class LogFactory {

    private static CommonLog log = null;

    public static CommonLog createLog(){
        if(log == null)
            log = new CommonLog();
        return log;
    }

    public static CommonLog createLog(String tag){
        if(log == null)
            log = new CommonLog();
        if(tag == null || tag.length() == 0)
            log.setTag(tag);
        return log;
    }
}
