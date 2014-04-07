package com.absurd.circle.data.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by absurd on 14-3-26.
 */
public class JsonUtil {
    private static Gson gson = null;

    static {
        if(gson == null){
            // able null object
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").serializeNulls().create();
        }
    }

    private JsonUtil(){

    }


    public static <V> V fromJson(String jsonStr,Type type){
        if(jsonStr != null) {
            if (jsonStr.startsWith("{")) {
                return gson.fromJson(jsonStr, type);
            }
            return null;
        }
        return null;
    }


    public static String toJson(final Object object){
        return gson.toJson(object);
    }


    public static List<?> jsonToList(String jsonStr){
        List<?> objList = null;
        if(gson != null){
            Type type = new TypeToken<List<?>>(){}.getType();
            objList = gson.fromJson(jsonStr,type);
        }
        return objList;
    }

}
