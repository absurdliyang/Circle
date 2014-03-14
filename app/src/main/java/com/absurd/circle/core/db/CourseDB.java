package com.absurd.circle.core.db;

import android.content.ContentValues;
import android.database.Cursor;


import com.absurd.circle.app.AppConstant;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;

import Database.Database;

public class CourseDB extends Database{
	private static CommonLog log = LogFactory.createLog(AppConstant.TAG);
    public static final String TABLE = "CourseDB";
    public static final String TABLE_NAME = "tb_course";
    
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COURSENAME = "course_name";
    public static final String COLUMN_COURSETEACHER = "course_teacher";
    public static final String COLUMN_STARTWEEK = "startweek";
    public static final String COLUMN_ENDWEEK = "endweek";
    public static final String COLUMN_COURSECLASSROOM = "course_classroom";
    public static final String COLUMN_SINGLEDOUBLEWEEK = "single_double_week";
    public static final String COLUMN_DAYOFWEEK = "day_of_week";
    public static final String COLUMN_STARTCLASSNUM = "start_class_num";
    public static final String COLUMN_ENDCLASSNUM = "end_class_num";
    
    
    
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
		String sql = "Create Table " + TABLE_NAME +
				"("+
				Database._ID + "integer primary key,"+
				COLUMN_COURSENAME + " text," +
				COLUMN_COURSETEACHER + " text," +
				COLUMN_STARTWEEK + " text,"+
				COLUMN_ENDWEEK + " text,"+
				COLUMN_COURSECLASSROOM + " text,"+
				COLUMN_SINGLEDOUBLEWEEK + " interger,"+
				COLUMN_DAYOFWEEK + " integer,"+
				COLUMN_STARTCLASSNUM + " integer,"+
				COLUMN_ENDCLASSNUM + " integer"+		
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
