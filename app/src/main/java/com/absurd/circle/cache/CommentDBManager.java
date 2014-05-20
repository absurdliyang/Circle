package com.absurd.circle.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.cache.util.Column;
import com.absurd.circle.cache.util.SQLiteTable;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.Follow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-4-13.
 */
public class CommentDBManager extends BaseDBManager {

    public CommentDBManager(Context context) {
        super(context);
    }

    public void insertComment(Comment comment){
        ContentValues value = new ContentValues();
        value.put(CommentDBInfo.ID, comment.getId());
        value.put(CommentDBInfo.CONTENT, comment.getContent());
        value.put(CommentDBInfo.CONTENT_TYPE, comment.getMessagetType());
        value.put(CommentDBInfo.LATITUDE, comment.getLatitude());
        value.put(CommentDBInfo.LONGITUDE, comment.getLongitude());
        value.put(CommentDBInfo.WEIBO_ID, comment.getWeiboId());
        value.put(CommentDBInfo.LOCATION_DEC, comment.getLocationDec());
        value.put(CommentDBInfo.USER_ID, comment.getUserId());
        value.put(CommentDBInfo.TITLE, comment.getTitle());
        value.put(CommentDBInfo.MEDIA_URL, comment.getMediaUrl());
        value.put(CommentDBInfo.MEDIA_TYPE, comment.getMessagetType());
        if(comment.getDate() != null) {
            value.put(CommentDBInfo.DATE, comment.getDate().getTime());
        }
        if(comment.getCommentDate() != null) {
            value.put(CommentDBInfo.COMMENT_DATE, comment.getCommentDate().getTime());
        }
        value.put(CommentDBInfo.COMMENT_COUNT, comment.getCommentCount());
        value.put(CommentDBInfo.PRAISE_COUNT, comment.getPraiseCount());
        value.put(CommentDBInfo.USER, comment.getStrUser());
        value.put(CommentDBInfo.MESSAGE_TYPE, comment.getMessagetType());

        value.put(CommentDBInfo.MESSAGE_ID, comment.getMessageId());
        value.put(CommentDBInfo.PARENT_ID, comment.getParentId());
        value.put(CommentDBInfo.TO_USER_ID, comment.getToUserId());
        value.put(CommentDBInfo.STATE, comment.getState());
        value.put(CommentDBInfo.PARENT_TEXT, comment.getParentText());
        value.put(CommentDBInfo.SECONDS, comment.getSeconds());

        mDatabase.insert(CommentDBInfo.TABLE_NAME, null, value);
    }

    public List<Comment> getPage(int pageIndex, int pageSize){
        List<Comment> resList = new ArrayList<Comment>();
        String sql= "select * from " + CommentDBInfo.TABLE_NAME + " order by _id desc" +
                " Limit "+String.valueOf(pageSize)+ " Offset " +String.valueOf(pageIndex * pageSize);
        AppContext.commonLog.i(sql);
        Cursor cursor = mDatabase.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                AppContext.commonLog.i(parseComment(cursor).toString());
                resList.add(parseComment(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return resList;
    }


    public List<Comment> getAllComent(){
        List<Comment> resList = new ArrayList<Comment>();
        Cursor cursor = mDatabase.query(CommentDBInfo.TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                resList.add(parseComment(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return resList;
    }

    public List<Comment> getUnreadComment(){
        List<Comment> resList = new ArrayList<Comment>();
        String sql = "select * from " + CommentDBInfo.TABLE_NAME + " where " + CommentDBInfo.STATE + " = '"
                + "0" + "'";
        Cursor cursor = mDatabase.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                resList.add(parseComment(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return resList;

    }

    public void updateCommentState(int commentId) {
        String sql = "update " + CommentDBInfo.TABLE_NAME + " set state = "
                + "1 " + "where id = " + commentId;
        mDatabase.execSQL(sql);
    }

    public void updateAllRead(){
        String sql = "update " + CommentDBInfo.TABLE_NAME + " set state = "
                + "1 " + "where " + CommentDBInfo.STATE + " = 0";
        mDatabase.execSQL(sql);
    }

    public void deleteAll(){
        deleteAll(CommentDBInfo.TABLE_NAME);
    }

    private Comment parseComment(Cursor cursor){
        Comment comment = new Comment();
        comment.setId(cursor.getInt(1));
        comment.setContent(cursor.getString(2));
        comment.setContentType(cursor.getInt(3));
        comment.setLatitude(cursor.getDouble(4));
        comment.setLongitude(cursor.getDouble(5));
        comment.setWeiboId(cursor.getString(6));
        comment.setLocationDec(cursor.getString(7));
        comment.setUserId(cursor.getString(8));
        comment.setTitle(cursor.getString(9));
        comment.setMediaUrl(cursor.getString(10));
        comment.setMediaType(cursor.getInt(11));
        comment.setDate(new java.sql.Date(cursor.getLong(12)));
        comment.setCommentDate(new java.sql.Date(cursor.getLong(13)));
        comment.setCommentCount(cursor.getInt(14));
        comment.setPraiseCount(cursor.getInt(15));
        comment.setUser(cursor.getString(16));
        comment.setMessagetType(cursor.getInt(17));

        comment.setMessageId(cursor.getInt(18));
        comment.setParentId(cursor.getInt(19));
        comment.setToUserId(cursor.getString(20));
        comment.setState(cursor.getInt(21));
        comment.setParentText(cursor.getString(22));
        comment.setSeconds(cursor.getInt(23));
        return  comment;
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

        public static final String MESSAGE_ID = "messageid";

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
                .addColumn(MESSAGE_ID, Column.DataType.INTEGER)
                .addColumn(PARENT_ID, Column.DataType.INTEGER)
                .addColumn(TO_USER_ID, Column.DataType.TEXT)
                .addColumn(STATE, Column.DataType.INTEGER)
                .addColumn(PARENT_TEXT, Column.DataType.TEXT)
                .addColumn(SECONDS, Column.DataType.INTEGER);
    }

}
