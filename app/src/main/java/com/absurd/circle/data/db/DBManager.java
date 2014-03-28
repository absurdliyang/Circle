package com.absurd.circle.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by absurd on 14-3-27.
 */
public class DBManager {

    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    public DBManager(Context context){
        mDBHelper = new DBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
    }



}
