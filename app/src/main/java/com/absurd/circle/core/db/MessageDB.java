package com.absurd.circle.core.db;


import android.content.ContentValues;
import android.database.Cursor;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;

import Database.Database;

public class MessageDB extends Database{
	private static CommonLog log = LogFactory.createLog(AppConstant.TAG);
	public static final String TABLE = "MessageDB";
	public static final String TABLE_NAME = "tb_message";
	
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_USERID = "user_id";
	public static final String COLUMN_USERNAME = "user_name";
	public static final String COLUMN_USERAVATAR = "user_avatar";
	public static final String COLUMN_GROUPID = "group_id";
	public static final String COLUMN_GROUPNAME = "group_name";
	public static final String COLUMN_GROUPAVATAR = "group_avatar";
	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_MEDIA = "media";
	public static final String COLUMN_COMMENTCOUNT = "comment_count";
	public static final String COLUMN_PRAISECOUNT = "praise_count";
	public static final String COLUMN_COLLECTCOUNT = "collect_count";
	public static final String COLUMN_FORWARDCOUNT = "forward_count";
	
	
	public void saveToDB(ContentValues values){
		insert(TABLE_NAME,null,values);
		
	}
	
	public Cursor readFromDB() {
		String whereCmd = null;
		String groupCmd = null;
		String orderCmd = null;
		return query(TABLE_NAME,null,whereCmd,null,groupCmd,null,orderCmd);
	}
	
	public void clearAll(){
		open();
		delete(TABLE_NAME,null,null);
		close();
	}
	
	@Override
	public String createTableCommander() {
		// TODO Auto-generated method stub
		String sql =  "Create Table " + TABLE_NAME +
				"(" +
				Database._ID + " integer primary key, " +
				COLUMN_ID + " integer," +
				COLUMN_USERID + " integer," + 
				COLUMN_USERAVATAR + " text," +
				COLUMN_USERNAME + " text," +
				COLUMN_GROUPID + " integer," +
				COLUMN_GROUPNAME + " text," +
				COLUMN_GROUPAVATAR + " text," +
				COLUMN_CREATED + " text," +
				COLUMN_CONTENT + " text," +
				COLUMN_MEDIA + " text," +
				COLUMN_COMMENTCOUNT + " integer," +
				COLUMN_PRAISECOUNT + " integer," +
				COLUMN_COLLECTCOUNT + " integer," +
				COLUMN_FORWARDCOUNT + " integer" +
				")";
		log.i(sql);
		return sql;
	}
	
	

	@Override
	public String upgradeTableCommander() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
