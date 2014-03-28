package com.absurd.circle.data.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        value.put(UserDBInfo.USER_ID,userId);
        value.put(UserDBInfo.JSON,json);
        mDatabase.insert(UserDBInfo.TABLE_NAME,null,value);
    }

    public void inserUser(User user){
        ContentValues value = new ContentValues();
        value.put(UserDBInfo.USER_ID,user.getUserId());
        value.put(UserDBInfo.JSON,JsonUtil.toJson(user));
        mDatabase.insert(UserDBInfo.TABLE_NAME,null,value);
    }

    public void deleteUser(){
        String sql = "delete from " + UserDBInfo.TABLE_NAME;
        mDatabase.execSQL(sql);
    }

    public User getUser(){
        Cursor cursor = mDatabase.query(UserDBInfo.TABLE_NAME,null,null,null,null,null,null);
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
        value.put(MessageDBInfo.MESSAGE_ID,message.getId() + "");
        value.put(MessageDBInfo.JSON,JsonUtil.toJson(message));
        mDatabase.insert(MessageDBInfo.TABLE_NAME,null,value);
    }


    public void deleteAllMessage(){
        String sql = "delete from " + MessageDBInfo.TABLE_NAME;
        mDatabase.execSQL(sql);
    }

    public List<Message> getAllMessages(){
        List<Message> resList = new ArrayList<Message>();
        Cursor cursor = mDatabase.query(MessageDBInfo.TABLE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String json = cursor.getString(2);
                Message message = JsonUtil.fromJson(json, Message.class);
                resList.add(message);
            }while(cursor.moveToNext());
        }
        return resList;
    }
}
