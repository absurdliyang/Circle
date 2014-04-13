package com.absurd.circle.data.cache;

import android.content.Context;

import com.absurd.circle.data.cache.util.Column;
import com.absurd.circle.data.cache.util.SQLiteTable;

/**
 * Created by absurd on 14-4-13.
 */
public class CommentDBManager extends BaseDBManager {

    public CommentDBManager(Context context) {
        super(context);
    }



    public static class CommentDBInfo{
        public static final String TABLE_NAME = "comment";

        public static final String ID = "id";

        public static final String CONTENT = "content";

        public static final String CONTENT_TYPE = "contenttype";

        public static final String LATITUDE = "latitude";

        public static final String LONGITUDE = "longitude";

        public static final String WEIBO_ID = "weiboid";

        public static final String LOCATION_DEC = "locationdec";

        public static final String USER_ID = "userid";

        public static final String TITLE = "title";

        public static final String MEDIA_URL = "mediaurl";

        public static final String MEDIA_TYPE = "mediatype";

        public static final String DATE =  "date";

        public static final String COMMENT_DATE = "commentdate";

        public static final String COMMENT_COUNT = "commentcount";

        public static final String PRAISE_COUNT = "praisecount";

        public static final String USER = "user";

        public static final String MESSAGE_TYPE = "messagetype";

        public static final String MESSAEG_ID = "messageid";

        public static final String PARENT_ID = "parentid";

        public static final String TO_USER_ID = "touserid";

        public static final String STATE = "state";

        public static final String PARENT_TEXT = "parenttext";

        public static final String SECONDS = "seconds";





        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(ID, Column.DataType.INTEGER)
                .addColumn(CONTENT, Column.DataType.TEXT)
                .addColumn(CONTENT_TYPE, Column.DataType.INTEGER)
                .addColumn(LATITUDE, Column.DataType.REAL)
                .addColumn(LONGITUDE, Column.DataType.REAL)
                .addColumn(WEIBO_ID, Column.DataType.TEXT)
                .addColumn(LOCATION_DEC, Column.DataType.TEXT)
                .addColumn(USER_ID, Column.DataType.TEXT)
                .addColumn(TITLE, Column.DataType.TEXT)
                .addColumn(MEDIA_URL, Column.DataType.TEXT)
                .addColumn(MEDIA_TYPE, Column.DataType.INTEGER)
                .addColumn(DATE, Column.DataType.INTEGER)
                .addColumn(COMMENT_DATE, Column.DataType.INTEGER)
                .addColumn(COMMENT_COUNT, Column.DataType.INTEGER)
                .addColumn(PRAISE_COUNT, Column.DataType.INTEGER)
                .addColumn(USER, Column.DataType.TEXT)
                .addColumn(MESSAGE_TYPE, Column.DataType.INTEGER)
                .addColumn(MESSAEG_ID, Column.DataType.INTEGER)
                .addColumn(PARENT_ID, Column.DataType.INTEGER)
                .addColumn(TO_USER_ID, Column.DataType.TEXT)
                .addColumn(STATE, Column.DataType.INTEGER)
                .addColumn(PARENT_TEXT, Column.DataType.TEXT)
                .addColumn(SECONDS, Column.DataType.INTEGER);
    }

}
