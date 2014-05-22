package com.absurd.circle.data.util;

import com.absurd.circle.data.model.Comment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Created by absurd on 14-3-26.
 */
public class JsonUtil {
    private static Gson gson = null;

    static {
        if(gson == null){
            // able null object
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").serializeNulls().create();
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



    public static class GsonDate{
        private Date date;

        public void setDate(Date d){
            this.date = d;
        }

        public Date getDate(){
            return date;
        }

    }


    public static void main(String args[]){
        String json = "{\n" +
                "    \"messageid\": 1587480,\n" +
                "    \"parentid\": 0,\n" +
                "    \"touserid\": \"qq:60691E49BE73581398660DB227951D66\",\n" +
                "    \"state\": 0,\n" +
                "    \"ParentText\": \"求wp用户评论！！\",\n" +
                "    \"seconds\": 0,\n" +
                "    \"parentUserName\": null,\n" +
                "    \"Id\": 1451828,\n" +
                "    \"content\": \"[围观][围观][围观]\",\n" +
                "    \"contenttype\": \"1\",\n" +
                "    \"latitude\": 39.985902,\n" +
                "    \"longitude\": 116.423534,\n" +
                "    \"weiboid\": \"\",\n" +
                "    \"locationdec\": null,\n" +
                "    \"userid\": \"sina:2237943115\",\n" +
                "    \"title\": null,\n" +
                "    \"mediaurl\": \"\",\n" +
                "    \"mediatype\": 0,\n" +
                "    \"date\": \"2014-05-22T00:06:29.293+08:00\",\n" +
                "    \"commentdate\": \"0001-01-01T00:00:00\", \n" +
                "    \"commentcount\": 0,\n" +
                "    \"praisecount\": 0,\n" +
                "    \"User\": null,\n" +
                "    \"messagetype\": null\n" +
                "    }";
        String tttt = json.replace("\"commentdate\": \"0001-01-01T00:00:00\"," ,"\"commentdate\": null,");

        Comment c = JsonUtil.fromJson(tttt,Comment.class);

        System.out.println(c.toString());
    }

}
