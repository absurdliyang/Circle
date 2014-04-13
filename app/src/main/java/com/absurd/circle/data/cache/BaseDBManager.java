package com.absurd.circle.data.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by absurd on 14-3-30.
 */
public class BaseDBManager {

    protected DBHelper mDBHelper;
    protected SQLiteDatabase mDatabase;

    public BaseDBManager(Context context){
        mDBHelper = new DBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    private void deleteAll(){

    }

}
