package com.absurd.circle.data.cache;

import com.absurd.circle.data.cache.util.Column;
import com.absurd.circle.data.cache.util.SQLiteTable;

/**
 * Created by absurd on 14-3-30.
 */
public class FollowDBManager {

        private FollowDBManager() {
        }

        public static final String TABLE_NAME = "follow";

        public static final String ID = "id";

        public static final String USER_ID = "userid";

        public static final String FOLLOW_USER_ID = "followuserid";

        public static final String FOLLOW_USER = "followuser";

        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(ID, Column.DataType.INTEGER)
                .addColumn(USER_ID, Column.DataType.TEXT)
                .addColumn(FOLLOW_USER_ID, Column.DataType.TEXT)
                .addColumn(FOLLOW_USER, Column.DataType.TEXT);
    }
