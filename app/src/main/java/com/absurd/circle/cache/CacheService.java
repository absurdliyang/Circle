package com.absurd.circle.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by absurd on 14-3-27.
 */
public class CacheService {
    private static CacheService mCacheService;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    public PraiseDBManager praiseDBManager;
    public UserDBManager userDBManager;
    public MessageDBManager messageDBManager;
    public CommentDBManager commnetDBManager;
    public FollowDBManager followDBManager;
    public UserMessageDBManager userMessageDBManager;
    public BlackListDBManager blackListDBManager;

    private CacheService(Context context){
        mDBHelper = new DBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();

        praiseDBManager = new PraiseDBManager(context);
        userDBManager = new UserDBManager(context);
        messageDBManager = new MessageDBManager(context);
        commnetDBManager = new CommentDBManager(context);
        followDBManager = new FollowDBManager(context);
        userMessageDBManager = new UserMessageDBManager(context);
        blackListDBManager = new BlackListDBManager(context);

    }

    public synchronized static CacheService getInstance(Context context){
        if(mCacheService == null)
            mCacheService = new CacheService(context);
        return mCacheService;
    }


    public void clearAll(){
        praiseDBManager.deleteAll();
        userDBManager.deleteAll();
        messageDBManager.deleteAll();
        commnetDBManager.deleteAll();
        followDBManager.deleteAll();
        userMessageDBManager.deleteAll();
        blackListDBManager.deleteAll();

    }


}
