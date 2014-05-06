package com.absurd.circle.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.cache.util.Column;
import com.absurd.circle.cache.util.SQLiteTable;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.Praise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-4-13.
 */
public class PraiseDBManager extends BaseDBManager {

    public PraiseDBManager(Context context) {
        super(context);
    }

    public void insertPraise(Praise praise) {
        ContentValues value = new ContentValues();
        value.put(PraiseDBInfo.ID, praise.getUserId());
        value.put(PraiseDBInfo.MESSAGE_ID, praise.getMessageId());
        value.put(PraiseDBInfo.TO_USER_ID, praise.getToUserId());
        value.put(PraiseDBInfo.STATE, praise.isState());
        value.put(PraiseDBInfo.USER_ID, praise.getUserId());
        value.put(PraiseDBInfo.DATE, praise.getDate().getTime());
        value.put(PraiseDBInfo.PARENT_TEXT, praise.getParentText());
        mDatabase.insert(PraiseDBInfo.TABLE_NAME, null, value);
    }


    public List<Praise> getPage(int pageIndex, int pageSize) {
        List<Praise> resList = new ArrayList<Praise>();
        String sql = "select * from " + PraiseDBInfo.TABLE_NAME +
                " Limit " + String.valueOf(pageSize) + " Offset " + String.valueOf(pageIndex * pageSize);
        AppContext.commonLog.i(sql);
        Cursor cursor = mDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                AppContext.commonLog.i(parsePraise(cursor).toString());
                resList.add(parsePraise(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return resList;
    }

    public List<Praise> getAll() {
        List<Praise> resList = new ArrayList<Praise>();
        Cursor cursor = mDatabase.query(PraiseDBInfo.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                resList.add(parsePraise(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return resList;
    }

    public List<Praise> getUnReadPraises() {
        List<Praise> resList = new ArrayList<Praise>();
        String sql = "select * from " + PraiseDBInfo.TABLE_NAME + " where " + PraiseDBInfo.STATE + " = '"
                + "0" + "'";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                resList.add(parsePraise(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return resList;
    }

    public void updatePraiseState(int praiseId) {
        String sql = "update " + PraiseDBInfo.TABLE_NAME + " set state = "
                + "1 " + "where id = " + praiseId;
        mDatabase.execSQL(sql);
    }

    public void updateAllRead() {
        String sql = "update " + PraiseDBInfo.TABLE_NAME + " set state = "
                + "1 " + "where " + PraiseDBInfo.STATE + " = 0";
        mDatabase.execSQL(sql);
    }



    public void deleteAll(){
        deleteAll(PraiseDBInfo.TABLE_NAME);
    }



    private Praise parsePraise(Cursor cursor){
        Praise praise = new Praise();
        praise.setId(cursor.getInt(1));
        praise.setMessageId(cursor.getInt(2));
        praise.setToUserId(cursor.getString(3));
        if(cursor.getInt(4) == 1){
            praise.setState(true);
        }else{
            praise.setState(false);
        }
        praise.setUserId(cursor.getString(5));
        praise.setDate(new java.sql.Date(cursor.getLong(6)));
        praise.setParentText(cursor.getString(7));
        return praise;
    }


    public static class PraiseDBInfo{

        public static final String TABLE_NAME = "praise";

        public static final String ID = "id";

        public static final String MESSAGE_ID = "messageid";

        public static final String TO_USER_ID = "touserid";

        public static final String STATE = "state";

        public static final String USER_ID = "userid";

        public static final String DATE = "date";

        public static final String PARENT_TEXT = "parenttext";



        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(ID, Column.DataType.INTEGER)
                .addColumn(MESSAGE_ID, Column.DataType.INTEGER)
                .addColumn(TO_USER_ID, Column.DataType.TEXT)
                .addColumn(STATE, Column.DataType.INTEGER)
                .addColumn(USER_ID, Column.DataType.TEXT)
                .addColumn(DATE, Column.DataType.INTEGER)
                .addColumn(PARENT_TEXT, Column.DataType.TEXT);
    }
}
