package com.absurd.circle.data.cache;

import com.absurd.circle.data.cache.util.Column;
import com.absurd.circle.data.cache.util.SQLiteTable;

/**
 * Created by absurd on 14-3-29.
 */
public class MessageDBInfo {

    private MessageDBInfo() {
    }

    public static final String TABLE_NAME = "message";

    public static final String MESSAGE_ID = "id";

    public static final String JSON = "json";

    public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
            .addColumn(MESSAGE_ID, Column.DataType.INTEGER)
            .addColumn(JSON, Column.DataType.TEXT);
}
