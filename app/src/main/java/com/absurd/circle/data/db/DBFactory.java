package com.absurd.circle.data.db;

import android.content.Context;

/**
 * Created by absurd on 14-3-27.
 */
public class DBFactory {

    public static final String USER_TABLE_NAME = "user_table";


    public static DBManager getInstance(Context context,String tableName){
        if(tableName.equals(USER_TABLE_NAME)){
            return new DBUser(context);
        }else{
            return new DBUser(context);
        }
    }

}
