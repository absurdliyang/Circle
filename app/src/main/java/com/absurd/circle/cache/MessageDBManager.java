package com.absurd.circle.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.absurd.circle.cache.util.Column;
import com.absurd.circle.cache.util.SQLiteTable;
import com.absurd.circle.data.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-3-29.
 */
public class MessageDBManager extends BaseDBManager{


    public MessageDBManager(Context context) {
        super(context);
    }

    public void insertMessage(Message message){
        ContentValues value = new ContentValues();
        value.put(MessageDBInfo.ID, message.getId());
        value.put(MessageDBInfo.CONTENT, message.getContent());
        value.put(MessageDBInfo.CONTENT_TYPE, message.getMessagetType());
        value.put(MessageDBInfo.LATITUDE, message.getLatitude());
        value.put(MessageDBInfo.LONGITUDE, message.getLongitude());
        value.put(MessageDBInfo.WEIBO_ID, message.getWeiboId());
        value.put(MessageDBInfo.LOCATION_DEC, message.getLocationDec());
        value.put(MessageDBInfo.USER_ID, message.getUserId());
        value.put(MessageDBInfo.TITLE, message.getTitle());
        value.put(MessageDBInfo.MEDIA_URL, message.getMediaUrl());
        value.put(MessageDBInfo.MEDIA_TYPE, message.getMessagetType());
        if(message.getDate() != null) {
            value.put(MessageDBInfo.DATE, message.getDate().getTime());
        }
        if(message.getCommentDate() != null) {
            value.put(MessageDBInfo.COMMENT_DATE, message.getCommentDate().getTime());
        }
        value.put(MessageDBInfo.COMMENT_COUNT, message.getCommentCount());
        value.put(MessageDBInfo.PRAISE_COUNT, message.getPraiseCount());
        value.put(MessageDBInfo.USER, message.getStrUser());
        value.put(MessageDBInfo.MESSAGE_TYPE, message.getMessagetType());
        mDatabase.insert(MessageDBInfo.TABLE_NAME,null,value);
    }

    public void deleteAll(){
        super.deleteAll(MessageDBInfo.TABLE_NAME);
    }

    public List<Message> getAllMessages(){
        List<Message> resList = new ArrayList<Message>();
        Cursor cursor = mDatabase.query(MessageDBInfo.TABLE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Message message = new Message();
                message.setId(cursor.getInt(1));
                message.setContent(cursor.getString(2));
                message.setContentType(cursor.getInt(3));
                message.setLatitude(cursor.getDouble(4));
                message.setLongitude(cursor.getDouble(5));
                message.setWeiboId(cursor.getString(6));
                message.setLocationDec(cursor.getString(7));
                message.setUserId(cursor.getString(8));
                message.setTitle(cursor.getString(9));
                message.setMediaUrl(cursor.getString(10));
                message.setMediaType(cursor.getInt(11));
                message.setDate(new java.sql.Date(cursor.getLong(12)));
                message.setCommentDate(new java.sql.Date(cursor.getLong(13)));
                message.setCommentCount(cursor.getInt(14));
                message.setPraiseCount(cursor.getInt(15));
                message.setUser(cursor.getString(16));
                message.setMessagetType(cursor.getInt(17));
                resList.add(message);
            }while(cursor.moveToNext());
        }
        return resList;
    }

    public static class MessageDBInfo{

        public static final String TABLE_NAME = "message";

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

        public static final String JSON = "json";

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
                .addColumn(MESSAGE_TYPE, Column.DataType.INTEGER);

    }
}
