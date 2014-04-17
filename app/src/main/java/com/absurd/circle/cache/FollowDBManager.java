package com.absurd.circle.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.absurd.circle.cache.util.Column;
import com.absurd.circle.cache.util.SQLiteTable;
import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-3-30.
 */
public class FollowDBManager extends BaseDBManager{


    public FollowDBManager(Context context) {
        super(context);
    }

    public void insertFollow(Follow follow){
        ContentValues values = new ContentValues();
        values.put(FollowDBInfo.ID,follow.getId());
        values.put(FollowDBInfo.USER_ID,follow.getUserId());
        values.put(FollowDBInfo.FOLLOW_USER_ID,follow.getFollowUserId());
        values.put(FollowDBInfo.FOLLOW_USER,follow.getStrUser());
        mDatabase.insert(FollowDBInfo.TABLE_NAME,null,values);
    }

    public void deleteAll(){
        super.deleteAll(FollowDBInfo.TABLE_NAME);
    }

    public void deleteFollow(String followUserId){
        String sql = "delete from " + FollowDBInfo.TABLE_NAME + " where " + FollowDBInfo.FOLLOW_USER_ID + " = '" + followUserId + "'";
        mDatabase.execSQL(sql);
    }

    public List<Follow> getAllFollow(){
        List<Follow> resList = new ArrayList<Follow>();
        Cursor cursor = mDatabase.query(FollowDBInfo.TABLE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(1);
                String userId = cursor.getString(2);
                String followUserId = cursor.getString(3);
                String strUser = cursor.getString(4);
                User user = JsonUtil.fromJson(strUser, User.class);
                Follow follow = new  Follow();
                follow.setId(id);
                follow.setFollowUserId(followUserId);
                follow.setStrUser(strUser);
                follow.setUserId(userId);
                follow.setUser(user);
                resList.add(follow);
            }while(cursor.moveToNext());
        }
        return resList;
    }

    public Follow findFollow(String followUserId){
        String sql = "select * from " + FollowDBInfo.TABLE_NAME + " where " + FollowDBInfo.FOLLOW_USER_ID + " = '"
                + followUserId + "'";
        Cursor cursor = mDatabase.rawQuery(sql,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                int id = cursor.getInt(1);
                String userId = cursor.getString(2);
                String followId = cursor.getString(3);
                String strUser = cursor.getString(4);
                User user = JsonUtil.fromJson(strUser, User.class);
                Follow follow = new  Follow();
                follow.setId(id);
                follow.setFollowUserId(followId);
                follow.setStrUser(strUser);
                follow.setUserId(userId);
                follow.setUser(user);
                return follow;
            }
        }
        return null;
    }

    public static class FollowDBInfo {
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
}
