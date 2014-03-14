package com.absurd.circle.core.db;



import android.content.ContentValues;
import android.database.Cursor;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;

import Database.Database;

public class UserDB extends Database{
    private CommonLog log = LogFactory.createLog(AppConstant.TAG);
	public static final String TABLE = "UserDB";
	public static final String TABLE_NAME = "tb_user";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NICKNAME = "nick_name";
	public static final String COLUMN_REALNAME = "real_name";
	public static final String COLUMN_SEX = "sex";
	public static final String COLUMN_BIRTHDAY = "birthday";
	public static final String COLUMN_LOVESTATE = "love_state";
	public static final String COLUMN_AVATAR = "avatar";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_SCHOOL = "school";
	public static final String COLUMN_ACADEMY = "academy";
	public static final String COLUMN_GRADE = "grade";
	public static final String COLUMN_JOINEDGROUPCOUNT = "joined_group_count";
	public static final String COLUMN_CREATEDGROUPCOUNT = "created_group_count";
	public static final String COLUMN_MESSAGECOUNT = "message_count";
	public static final String COLUMN_GROUP_CREATED_ID = "group_created_id";
	public static final String COLUMN_GROUP_CREATED_NAME = "group_created_name";
	public static final String COLUMN_GROUP_CREATED_DESCRIPTION = "group_created_description";
	public static final String COLUMN_GROUP_CREATED_AVATAR = "group_created_avatar";
	public static final String COLUMN_GROUP_CREATED_COVER = "group_created_cover";
	public static final String COLUMN_GROUP_CREATED_CREATED = "group_created_created";
	public static final String COLUMN_GROUP_CREATED_TOTAL = "group_created_total";
	
	
	
	public void saveToDB(ContentValues values){
		insert(TABLE_NAME,null,values);
	}
	
	public Cursor readFromDB(){
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
				" (" + 
				Database._ID + " integer primary key," + 
				COLUMN_ID + " integer," + 
				COLUMN_NICKNAME + " text," +
				COLUMN_REALNAME + " text," +
				COLUMN_SEX + " integer," +
				COLUMN_BIRTHDAY + " text," +
				COLUMN_LOVESTATE + " text," +
				COLUMN_AVATAR + " text," +
				COLUMN_DESCRIPTION + " text," +
				COLUMN_SCHOOL + " text," + 
				COLUMN_ACADEMY + " text," +
				COLUMN_GRADE + " text," +
				COLUMN_JOINEDGROUPCOUNT + " integer," +
				COLUMN_CREATEDGROUPCOUNT + " integer," +
				COLUMN_MESSAGECOUNT + " integer,"  +
				COLUMN_GROUP_CREATED_ID + " integer," + 
				COLUMN_GROUP_CREATED_NAME + " text," + 
				COLUMN_GROUP_CREATED_DESCRIPTION + " text," +
				COLUMN_GROUP_CREATED_AVATAR + " text," +
				COLUMN_GROUP_CREATED_COVER + " text," +
				COLUMN_GROUP_CREATED_CREATED + " text," +
				COLUMN_GROUP_CREATED_TOTAL + " integer" + 
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
