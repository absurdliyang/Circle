package com.absurd.circle.core.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;

import Database.Database;

public class NotificationDB extends Database{
	private static final CommonLog log = LogFactory.createLog(AppConstant.TAG);
	
	public static final String TABLE = "NotificationDB";
	public static final String TABLE_NAME = "tb_notification";
	
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_FROM_USER_ID = "from_user_id";
	public static final String COLUMN_FROM_USER_NAME = "from_user_name";
	public static final String COLUMN_FROM_USER_AVATAR = "from_user_avatar";
	
	public static final String COLUMN_MESSAGE_ID = "message_id";
	public static final String COLUMN_MESSAGE_USERID = "user_id";
	public static final String COLUMN_MESSAGE_USERNAME = "user_name";
	public static final String COLUMN_MESSAGE_USERAVATAR = "user_avatar";
	public static final String COLUMN_MESSAGE_GROUPID = "group_id";
	public static final String COLUMN_MESSAGE_GROUPNAME = "group_name";
	public static final String COLUMN_MESSAGE_GROUPAVATAR = "group_avatar";
	public static final String COLUMN_MESSAGE_CREATED = "created";
	public static final String COLUMN_MESSAGE_CONTENT = "content";
	public static final String COLUMN_MESSAGE_MEDIA = "media";
	public static final String COLUMN_MESSAGE_COMMENTCOUNT = "comment_count";
	public static final String COLUMN_MESSAGE_PRAISECOUNT = "praise_count";
	public static final String COLUMN_MESSAGE_COLLECTCOUNT = "collect_count";
	public static final String COLUMN_MESSAGE_FORWARDCOUNT = "forward_count";
	
	
	public void saveToDB(ContentValues values) {
		// TODO Auto-generated method stub
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
				COLUMN_TYPE + " integer," + 
				COLUMN_FROM_USER_ID + " integer," + 
				COLUMN_FROM_USER_NAME + " text," +
				COLUMN_FROM_USER_AVATAR + " text," +
				COLUMN_MESSAGE_ID + " integer," + 
				COLUMN_MESSAGE_USERID + " integer," + 
				COLUMN_MESSAGE_USERAVATAR + " text," +
				COLUMN_MESSAGE_USERNAME + " text," +
				COLUMN_MESSAGE_GROUPID + " integer," +
				COLUMN_MESSAGE_GROUPNAME + " text," +
				COLUMN_MESSAGE_GROUPAVATAR + " text," +
				COLUMN_MESSAGE_CREATED + " text," +
				COLUMN_MESSAGE_CONTENT + " text," +
				COLUMN_MESSAGE_MEDIA + " text," +
				COLUMN_MESSAGE_COMMENTCOUNT + " integer," +
				COLUMN_MESSAGE_PRAISECOUNT + " integer," +
				COLUMN_MESSAGE_COLLECTCOUNT + " integer," +
				COLUMN_MESSAGE_FORWARDCOUNT + " integer" +
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
