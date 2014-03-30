package com.absurd.circle.data.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by absurd on 14-3-27.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "circle_db";
    private static final int DB_VERSION = 1;


    public DBHelper(Context context){
        super(context,DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        MessageDBManager.TABLE.create(sqLiteDatabase);
        UserDBManager.TABLE.create(sqLiteDatabase);
        FollowDBManager.TABLE.create(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
