package com.absurd.circle.data.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.absurd.circle.data.cache.util.Column;
import com.absurd.circle.data.cache.util.SQLiteTable;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-4-13.
 */
public class UserMessageDBManager extends BaseDBManager {

    public UserMessageDBManager(Context context) {
        super(context);
    }


    public void insertUserMessage(UserMessage userMessage){
        ContentValues value = new ContentValues();
        value.put(UserMessageDBInfo.ID, userMessage.getId());
        value.put(UserMessageDBInfo.MEDIA_URL, userMessage.getMediaUrl());
        value.put(UserMessageDBInfo.MEDIA_TYPE, userMessage.getMediaType());
        value.put(UserMessageDBInfo.CONTENT, userMessage.getContent());
        value.put(UserMessageDBInfo.FROM_USER_ID, userMessage.getFromUserId());
        value.put(UserMessageDBInfo.FROM_USER_NAME, userMessage.getFromUserName());
        value.put(UserMessageDBInfo.TO_USER_ID, userMessage.getToUserId());
        value.put(UserMessageDBInfo.TO_USER_NAME, userMessage.getToUserName());
        if(userMessage.getDate() != null) {
            value.put(UserMessageDBInfo.DATE, userMessage.getDate().getTime());
        }
        value.put(UserMessageDBInfo.STATE, userMessage.getState());
        mDatabase.insert(UserMessageDBInfo.TABLE_NAME, null, value);
    }


    public List<UserMessage> getAllUserMessages(){
        List<UserMessage> resList = new ArrayList<UserMessage>();
        Cursor cursor = mDatabase.query(UserMessageDBInfo.TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                resList.add(parseUserMessage(cursor));
            }while(cursor.moveToNext());
        }
        return resList;
    }

    public List<UserMessage> getUnReadUserMessages(){
        List<UserMessage> resList = new ArrayList<UserMessage>();
        String sql = "select * from " + UserMessageDBInfo.TABLE_NAME + " where " + UserMessageDBInfo.STATE + " = '"
                + "0" + "'";
        Cursor cursor = mDatabase.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                resList.add(parseUserMessage(cursor));
            }while(cursor.moveToNext());
        }
        return resList;
    }

    public void updateCommentState(int userMessageId) {
        String sql = "update " + UserMessageDBInfo.TABLE_NAME + " set state = "
                + "1 " + "where id = " + userMessageId;
        mDatabase.execSQL(sql);
    }

    public void deleteAll(){
        deleteAll(UserMessageDBInfo.TABLE_NAME);
    }


    private UserMessage parseUserMessage(Cursor cursor){
        UserMessage userMessage = new UserMessage();
        userMessage.setId(cursor.getInt(1));
        userMessage.setMediaUrl(cursor.getString(2));
        userMessage.setMediaType(cursor.getInt(3));
        userMessage.setContent(cursor.getString(4));
        userMessage.setFromUserId(cursor.getString(5));
        userMessage.setFromUserName(cursor.getString(6));
        userMessage.setToUserId(cursor.getString(7));
        userMessage.setToUserName(cursor.getString(8));
        userMessage.setDate(new java.sql.Date(cursor.getLong(9)));
        userMessage.setState(cursor.getInt(10));
        return userMessage;
    }

    public static class UserMessageDBInfo{
        public static final String TABLE_NAME = "usermessage";

        public static final String ID = "id";

        public static final String MEDIA_URL = "meidaurl";

        public static final String MEDIA_TYPE = "mediatype";

        public static final String CONTENT = "content";

        public static final String FROM_USER_ID = "fromuserid";

        public static final String FROM_USER_NAME = "fromusername";

        public static final String TO_USER_ID = "touserid";

        public static final String TO_USER_NAME = "tousername";

        public static final String DATE = "date";

        public static final String STATE = "state";


        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(ID, Column.DataType.INTEGER)
                .addColumn(MEDIA_URL, Column.DataType.TEXT)
                .addColumn(MEDIA_TYPE, Column.DataType.INTEGER)
                .addColumn(CONTENT, Column.DataType.TEXT)
                .addColumn(FROM_USER_ID, Column.DataType.TEXT)
                .addColumn(FROM_USER_NAME, Column.DataType.TEXT)
                .addColumn(TO_USER_ID, Column.DataType.TEXT)
                .addColumn(TO_USER_NAME, Column.DataType.TEXT)
                .addColumn(DATE, Column.DataType.INTEGER)
                .addColumn(STATE, Column.DataType.INTEGER);
    }
}
