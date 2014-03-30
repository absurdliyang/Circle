package com.absurd.circle.data.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-3-27.
 */
public class CacheService {
    private static CacheService mCacheService;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    private CacheService(Context context){
        mDBHelper = new DBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public synchronized static CacheService getInstance(Context context){
        if(mCacheService == null)
            mCacheService = new CacheService(context);
        return mCacheService;
    }

    public void insertUser(String userId, String json){
        ContentValues value = new ContentValues();
        value.put(UserDBManager.USER_ID,userId);
        value.put(UserDBManager.JSON,json);
        mDatabase.insert(UserDBManager.TABLE_NAME,null,value);
    }

    public void inserUser(User user){
        ContentValues value = new ContentValues();
        value.put(UserDBManager.USER_ID,user.getUserId());
        value.put(UserDBManager.JSON,JsonUtil.toJson(user));
        mDatabase.insert(UserDBManager.TABLE_NAME,null,value);
    }

    public void deleteUser(){
        String sql = "delete from " + UserDBManager.TABLE_NAME;
        mDatabase.execSQL(sql);
    }

    public User getUser(){
        Cursor cursor = mDatabase.query(UserDBManager.TABLE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            cursor.move(0);
            String id = cursor.getString(1);
            String json = cursor.getString(2);
            User user = JsonUtil.fromJson(json,User.class);
            return user;
        }
        return null;
    }


    public void inserMessage(Message message){
        ContentValues value = new ContentValues();
        value.put(MessageDBManager.MESSAGE_ID,message.getId() + "");
        value.put(MessageDBManager.JSON,JsonUtil.toJson(message));
        mDatabase.insert(MessageDBManager.TABLE_NAME,null,value);
    }


    public void deleteAllMessage(){
        String sql = "delete from " + MessageDBManager.TABLE_NAME;
        mDatabase.execSQL(sql);
    }

    public List<Message> getAllMessages(){
        List<Message> resList = new ArrayList<Message>();
        Cursor cursor = mDatabase.query(MessageDBManager.TABLE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String json = cursor.getString(2);
                Message message = JsonUtil.fromJson(json, Message.class);
                resList.add(message);
            }while(cursor.moveToNext());
        }
        return resList;
    }

    public void insertFollow(Follow follow){
        ContentValues values = new ContentValues();
        values.put(FollowDBManager.ID,follow.getId());
        values.put(FollowDBManager.USER_ID,follow.getUserId());
        values.put(FollowDBManager.FOLLOW_USER_ID,follow.getFollowUserId());
        values.put(FollowDBManager.FOLLOW_USER,follow.getStrUser());
        mDatabase.insert(FollowDBManager.TABLE_NAME,null,values);
    }

    public void deleteAllFollow(){
        String sql = "delete from " + FollowDBManager.TABLE_NAME;
        mDatabase.execSQL(sql);
    }

    public void deleteFollow(String followUserId){
        String sql = "delete from " + FollowDBManager.TABLE_NAME + " where " + FollowDBManager.FOLLOW_USER_ID + " = " + followUserId;
        mDatabase.execSQL(sql);
    }

    public List<Follow> getAllFollow(){
        List<Follow> resList = new ArrayList<Follow>();
        Cursor cursor = mDatabase.query(FollowDBManager.TABLE_NAME,null,null,null,null,null,null);
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
        String sql = "select * from " + FollowDBManager.TABLE_NAME + " where " + FollowDBManager.FOLLOW_USER_ID + " = '"
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

}
