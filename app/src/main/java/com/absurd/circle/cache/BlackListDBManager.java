package com.absurd.circle.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.cache.util.Column;
import com.absurd.circle.cache.util.SQLiteTable;
import com.absurd.circle.data.model.BlackList;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

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

    public int getCount(){
        Cursor cursor = mDatabase.query(BlackListDBInfo.TABLE_NAME,null,null,null,null,null,null);
        return cursor.getCount();
    }

    public void insertBlackList(BlackList black){
        ContentValues values = new ContentValues();
        values.put(BlackListDBInfo.ID, black.getId());
        values.put(BlackListDBInfo.USER_ID, black.getUserId());
        values.put(BlackListDBInfo.FOLLOW_USER_ID, black.getFollowUserId());
        values.put(BlackListDBInfo.USER, black.getStrUser());
        mDatabase.insert(BlackListDBInfo.TABLE_NAME,null, values);
    }

    public void deleteBlackList(String id){
        String sql = "delete from " + BlackListDBInfo.TABLE_NAME + " where " + BlackListDBInfo.ID + " = '" + id + "'";
        mDatabase.execSQL(sql);
    }


    public BlackList findBlackList(String followUserId){
        String sql = "select * from " + BlackListDBInfo.TABLE_NAME + " where " + BlackListDBInfo.FOLLOW_USER_ID + " = '"
                + followUserId + "'";
        Cursor cursor = mDatabase.rawQuery(sql,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                return parseBlackList(cursor);
            }
        }
        return null;
    }


    public List<BlackList> findAlllackList(){
        List<BlackList> resList = new ArrayList<BlackList>();
        Cursor cursor = mDatabase.query(BlackListDBInfo.TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                resList.add(parseBlackList(cursor));
            }while(cursor.moveToNext());
        }
        return resList;
    }

    public List<BlackList> getPage(int pageIndex, int pageSize){
        List<BlackList> resList = new ArrayList<BlackList>();
        String sql= "select * from " + BlackListDBInfo.TABLE_NAME +
                " Limit "+String.valueOf(pageSize)+ " Offset " +String.valueOf(pageIndex * pageSize);
        AppContext.commonLog.i(sql);
        Cursor cursor = mDatabase.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                AppContext.commonLog.i(parseBlackList(cursor).toString());
                resList.add(parseBlackList(cursor));
            }while(cursor.moveToNext());
        }
        return resList;
    }


    private BlackList parseBlackList(Cursor cursor){
        BlackList blackList = new BlackList();
        blackList.setId(cursor.getString(1));
        blackList.setUserId(cursor.getString(2));
        blackList.setFollowUserId(cursor.getString(3));
        String strUser = cursor.getString(4);
        User user = JsonUtil.fromJson(strUser,User.class);
        blackList.setUser(user);
        return blackList;
    }


    public static class BlackListDBInfo{
        public static final String TABLE_NAME = "blacklist";

        public static final String ID = "id";

        public static final String USER_ID = "userid";

        public static final String FOLLOW_USER_ID = "followuserid";

        public static final String USER = "user";

        public static final SQLiteTable TABLE = new SQLiteTable(TABLE_NAME)
                .addColumn(ID, Column.DataType.TEXT)
                .addColumn(USER_ID, Column.DataType.TEXT)
                .addColumn(FOLLOW_USER_ID, Column.DataType.TEXT)
                .addColumn(USER, Column.DataType.TEXT);

    }

}
