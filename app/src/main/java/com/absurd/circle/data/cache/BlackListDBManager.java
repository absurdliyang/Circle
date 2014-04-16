package com.absurd.circle.data.cache;

import android.content.Context;

import com.absurd.circle.data.cache.util.Column;
import com.absurd.circle.data.cache.util.SQLiteTable;

/**
 * Created by absurd on 14-4-14.
 */
public class BlackListDBManager extends BaseDBManager{

    public BlackListDBManager(Context context) {
        super(context);
    }

    public void deleteAll(){
        super.deleteAll(BlackListDBInfo.TABLE_NAME);
    }




    public static class BlackListDBInfo{
        public static final String TABLE_NAME = "blacklist";

        public static final String ID = "id";

        public static final String USER_ID = "userid";

        public static final String FOLLOW_USER_ID = "followuserid";

        public static final String USER = "user";

        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(ID, Column.DataType.INTEGER)
                .addColumn(USER_ID, Column.DataType.TEXT)
                .addColumn(FOLLOW_USER_ID, Column.DataType.TEXT)
                .addColumn(USER, Column.DataType.TEXT);

    }

}
