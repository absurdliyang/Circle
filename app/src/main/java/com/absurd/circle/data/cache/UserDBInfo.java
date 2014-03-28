package com.absurd.circle.data.cache;

import android.content.Context;

import com.absurd.circle.data.cache.util.Column;
import com.absurd.circle.data.cache.util.SQLiteTable;

/**
 * Created by absurd on 14-3-27.
 */
public class UserDBInfo {
    public UserDBInfo(){

    }
    public static final String TABLE_NAME = "user";

    public static final String USER_ID = "id";

    public static final String JSON = "json";

    public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
            .addColumn(USER_ID, Column.DataType.INTEGER)
            .addColumn(JSON, Column.DataType.TEXT);
}
