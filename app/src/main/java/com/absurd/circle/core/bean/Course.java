package com.absurd.circle.core.bean;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;


import com.absurd.circle.core.db.CourseDB;

import Database.DAOHelper;

public class Course implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6902456848699586348L;

	private int id;
	
	private String courseName;
	
	private String courseTeacher;
	
	private String startWeek;
	
	private String endWeek;
	
	private String courseClassroom;
	
	private String coursePlace;
	
	public String getCoursePlace() {
		return coursePlace;
	}

	public void setCoursePlace(String coursePlace) {
		this.coursePlace = coursePlace;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// 1 single 2 both 3 double
	private int singleDoubleWeek;
	
	// 1-7
	private int dayOfWeek;
	
	// 1-12
	private int startClassNum;
	
	//1-12
	private int endClassNum;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseTeacher() {
		return courseTeacher;
	}

	public void setCourseTeacher(String courseTeacher) {
		this.courseTeacher = courseTeacher;
	}

	public String getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(String startWeek) {
		this.startWeek = startWeek;
	}

	public String getEndWeek() {
		return endWeek;
	}

	public void setEndWeek(String endWeek) {
		this.endWeek = endWeek;
	}

	public String getCourseClassroom() {
		return courseClassroom;
	}

	public void setCourseClassroom(String courseClassroom) {
		this.courseClassroom = courseClassroom;
	}

	public int getSingleDoubleWeek() {
		return singleDoubleWeek;
	}

	public void setSingleDoubleWeek(int singleDoubleWeek) {
		this.singleDoubleWeek = singleDoubleWeek;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getStartClassNum() {
		return startClassNum;
	}

	public void setStartClassNum(int startClassNum) {
		this.startClassNum = startClassNum;
	}

	public int getEndClassNum() {
		return endClassNum;
	}

	public void setEndClassNum(int endClassNum) {
		this.endClassNum = endClassNum;
	}

	@Override
	public String toString() {
		return "CourseInfo [id=" + id + ", courseName=" + courseName
				+ ", courseTeacher=" + courseTeacher + ", startWeek="
				+ startWeek + ", endWeek=" + endWeek + ", courseClassroom="
				+ courseClassroom + ", singleDoubleWeek=" + singleDoubleWeek
				+ ", dayOfWeek=" + dayOfWeek + ", startClassNum="
				+ startClassNum + ", endClassNum=" + endClassNum + "]";
	}
	
	public synchronized boolean saveToDB(){
		CourseDB db = (CourseDB)DAOHelper.getInstance().getTable(CourseDB.TABLE);
		db.open();
		ContentValues value = new ContentValues();
		value.put(CourseDB.COLUMN_ID, this.id);
		value.put(CourseDB.COLUMN_COURSENAME, this.courseName);
		value.put(CourseDB.COLUMN_COURSETEACHER, this.courseTeacher);
		value.put(CourseDB.COLUMN_STARTWEEK, this.startWeek);
		value.put(CourseDB.COLUMN_ENDWEEK, this.endWeek);
		value.put(CourseDB.COLUMN_COURSECLASSROOM, this.courseClassroom);
		
		
		
		
		
		if(this.singleDoubleWeek == 1){
		   
		   value.put(CourseDB.COLUMN_SINGLEDOUBLEWEEK,"����");
		}else if(this.singleDoubleWeek == 2){
			
			value.put(CourseDB.COLUMN_SINGLEDOUBLEWEEK, "ÿ��");
		}else if(this.singleDoubleWeek == 3){
			
			value.put(CourseDB.COLUMN_SINGLEDOUBLEWEEK,"˫��");
		}
		
		
		
		//value.put(CourseDB.COLUMN_SINGLEDOUBLEWEEK, this.singleDoubleWeek);
		value.put(CourseDB.COLUMN_DAYOFWEEK, this.dayOfWeek);
		value.put(CourseDB.COLUMN_STARTCLASSNUM, this.startClassNum);
		value.put(CourseDB.COLUMN_ENDCLASSNUM, this.endClassNum);
		
		db.saveToDB(value);
		db.close();
		return true;
	}
	
	public static synchronized Course readFromDB(){
		CourseDB db = (CourseDB) DAOHelper.getInstance().getTable(CourseDB.TABLE);		
		db.open();
	    Cursor cursor = db.readFromDB();
	    
	    int indexId = cursor.getColumnIndex(CourseDB.COLUMN_ID);
	    int indexCourseName = cursor.getColumnIndex(CourseDB.COLUMN_COURSENAME);
	    int indexCourseTeacher = cursor.getColumnIndex(CourseDB.COLUMN_COURSETEACHER);
	    int indexStartWeek = cursor.getColumnIndex(CourseDB.COLUMN_STARTWEEK);
	    int indexEndWeek = cursor.getColumnIndex(CourseDB.COLUMN_ENDWEEK);
	    int indexCourseClassroom = cursor.getColumnIndex(CourseDB.COLUMN_COURSECLASSROOM);
	    int indexSingleDoubleWeek = cursor.getColumnIndex(CourseDB.COLUMN_SINGLEDOUBLEWEEK);
	    int indexDayOfWeek = cursor.getColumnIndex(CourseDB.COLUMN_DAYOFWEEK);
	    int indexStartClassNum = cursor.getColumnIndex(CourseDB.COLUMN_STARTCLASSNUM);
	    int indexEndClassNum = cursor.getColumnIndex(CourseDB.COLUMN_ENDCLASSNUM);
	    
	    Course c = null;
	    if(cursor.moveToFirst()){
	    	c = new Course();
	    	c.setId(cursor.getInt(indexId));
	    	c.setCourseName(cursor.getString(indexCourseName));
	    	c.setCourseTeacher(cursor.getString(indexCourseTeacher));
	    	c.setStartWeek(cursor.getString(indexStartWeek));
	    	c.setEndWeek(cursor.getString(indexEndWeek));
	    	c.setCourseClassroom(cursor.getString(indexCourseClassroom));
	    	
	    	
	    	
	    	
	    	c.setSingleDoubleWeek(cursor.getInt(indexSingleDoubleWeek));
	    	c.setDayOfWeek(cursor.getInt(indexDayOfWeek));
	    	c.setStartClassNum(cursor.getInt(indexStartClassNum));
	    	c.setEndClassNum(cursor.getInt(indexEndClassNum));
	    }
	    
	    cursor.close();
		db.close();
		return c;
	}
	
	
	
}
