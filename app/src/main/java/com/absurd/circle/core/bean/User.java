package com.absurd.circle.core.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Database.DAOHelper;

import android.content.ContentValues;
import android.database.Cursor;

import com.absurd.circle.core.db.UserDB;
import com.google.gson.annotations.SerializedName;

public class User implements Serializable{

	/**
	 * serialize ID
	 */
	private static final long serialVersionUID = -7659249297181819074L;

	private int id;
	
	@SerializedName("nick_name")
	private String nickName;
	
	@SerializedName("real_name")
	private String realName;
		
	private char sex = 'M';
	
	private Date birthday;
	@SerializedName("love_state")
	private String loveState;
	


	@SerializedName("user_type")
	private char userType;
	
	private String avatar;
	
	private String description;

	private String school;
	
	private String academy;
	
	private String grade;
	
	@SerializedName("joined_group_count")
	private int joinedGroupCount;
	
	@SerializedName("created_group_count")
	private int createdGroupCount;
	
	@SerializedName("message_count")
	private int messageCount;
	
	@SerializedName("created_group")
	private List<Group> createdGroup;
	
	@SerializedName("push_id")
	private String pushId;
	
	
	public String getPushId() {
		return pushId;
	}
	public void setPushId(String pushId) {
		this.pushId = pushId;
	}
	public List<Group> getCreatedGroup() {
		return createdGroup;
	}
	public void setCreatedGroup(List<Group> createdGroup) {
		this.createdGroup = createdGroup;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	public String getLoveState() {
		return loveState;
	}
	public void setLoveState(String loveState) {
		this.loveState = loveState;
	}

	
	
	public int getJoinedGroupCount() {
		return joinedGroupCount;
	}
	public void setJoinedGroupCount(int joinedGroupCount) {
		this.joinedGroupCount = joinedGroupCount;
	}
	public int getCreatedGroupCount() {
		return createdGroupCount;
	}
	public void setCreatedGroupCount(int createdGroupCount) {
		this.createdGroupCount = createdGroupCount;
	}
	public int getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(int message_count) {
		this.messageCount = message_count;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public char getUserType() {
		return userType;
	}

	public void setUserType(char userType) {
		this.userType = userType;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getAcademy() {
		return academy;
	}

	public void setAcademy(String academy) {
		this.academy = academy;
	}

	
	
	@Override
	public String toString() {
		return "User [id=" + id + ", nickName=" + nickName + ", realName="
				+ realName + ", sex=" + sex + ", birthday=" + birthday
				+ ", loveState=" + loveState + ", userType=" + userType
				+ ", avatar=" + avatar + ", description=" + description
				+ ", school=" + school + ", academy=" + academy + ", grade="
				+ grade + ", joinedGroupCount=" + joinedGroupCount
				+ ", createdGroupCount=" + createdGroupCount
				+ ", messageCount=" + messageCount + "]";
	}
	public UserItem toUserItem(){
		UserItem u = new UserItem();
		u.setAvatar(this.getAvatar());
		u.setId(this.getId());
		u.setNickName(this.getNickName());
		u.setUserType(this.getUserType());
		return u;
	}
	
	
	public synchronized boolean saveToDB(){
		UserDB db = (UserDB)DAOHelper.getInstance().getTable(UserDB.TABLE);
		db.open();
		ContentValues value = new ContentValues();
		value.put(UserDB.COLUMN_ID, this.id);
		value.put(UserDB.COLUMN_NICKNAME, this.nickName);
		value.put(UserDB.COLUMN_REALNAME,this.realName);
		value.put(UserDB.COLUMN_AVATAR,this.avatar);
		value.put(UserDB.COLUMN_SEX,(int)this.sex);
		if(this.birthday != null){
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			String strBirth = formater.format(this.birthday);
			value.put(UserDB.COLUMN_BIRTHDAY, strBirth);
		}
		value.put(UserDB.COLUMN_DESCRIPTION,this.description);
		value.put(UserDB.COLUMN_SCHOOL,this.getSchool());
		value.put(UserDB.COLUMN_ACADEMY,this.getAcademy());
		value.put(UserDB.COLUMN_GRADE,this.getGrade());
		value.put(UserDB.COLUMN_JOINEDGROUPCOUNT,this.joinedGroupCount);
		value.put(UserDB.COLUMN_CREATEDGROUPCOUNT,this.createdGroupCount);
		value.put(UserDB.COLUMN_MESSAGECOUNT, this.messageCount);
		value.put(UserDB.COLUMN_LOVESTATE, this.loveState);
		if(this.getCreatedGroup() != null && !this.getCreatedGroup().isEmpty()){
			value.put(UserDB.COLUMN_GROUP_CREATED_ID, this.getCreatedGroup().get(0).getId());
			value.put(UserDB.COLUMN_GROUP_CREATED_NAME,this.getCreatedGroup().get(0).getName());
			value.put(UserDB.COLUMN_GROUP_CREATED_DESCRIPTION,this.getCreatedGroup().get(0).getDescription());
			value.put(UserDB.COLUMN_GROUP_CREATED_AVATAR,this.getCreatedGroup().get(0).getDescription());
			value.put(UserDB.COLUMN_GROUP_CREATED_COVER,this.getCreatedGroup().get(0).getCover());
			value.put(UserDB.COLUMN_GROUP_CREATED_CREATED,this.getCreatedGroup().get(0).getCreated());
			value.put(UserDB.COLUMN_GROUP_CREATED_TOTAL,this.getCreatedGroup().get(0).getTotal()+ "");
		}else{
			value.put(UserDB.COLUMN_GROUP_CREATED_ID,-1);
		}
		db.saveToDB(value);
		db.close();
		return true;
	}
	
	
	
	public static synchronized User readFromDB(){
		UserDB db = (UserDB)DAOHelper.getInstance().getTable(UserDB.TABLE);
		db.open();
		Cursor cursor = db.readFromDB();
		int indexId = cursor.getColumnIndex(UserDB.COLUMN_ID);
		int indexNickName = cursor.getColumnIndex(UserDB.COLUMN_NICKNAME);
		int indexRealName = cursor.getColumnIndex(UserDB.COLUMN_REALNAME);
		int indexSex = cursor.getColumnIndex(UserDB.COLUMN_SEX);
		int indexBirthday = cursor.getColumnIndex(UserDB.COLUMN_BIRTHDAY);
		int indexAvatar = cursor.getColumnIndex(UserDB.COLUMN_AVATAR);
		int indexDescription = cursor.getColumnIndex(UserDB.COLUMN_DESCRIPTION);
		int indexSchool = cursor.getColumnIndex(UserDB.COLUMN_SCHOOL);
		int indexAcademy = cursor.getColumnIndex(UserDB.COLUMN_ACADEMY);
		int indexGrade = cursor.getColumnIndex(UserDB.COLUMN_GRADE);
		int indexJoinedGroupCount = cursor.getColumnIndex(UserDB.COLUMN_JOINEDGROUPCOUNT);
		int indexCreatedGroupCount = cursor.getColumnIndex(UserDB.COLUMN_CREATEDGROUPCOUNT);
		int indexMessageCount = cursor.getColumnIndex(UserDB.COLUMN_MESSAGECOUNT);
		int indexLoveState = cursor.getColumnIndex(UserDB.COLUMN_LOVESTATE);
		int indexCreateGroupId = cursor.getColumnIndex(UserDB.COLUMN_GROUP_CREATED_ID);
		int indexCreatedGroupName = cursor.getColumnIndex(UserDB.COLUMN_GROUP_CREATED_NAME);
		int indexCreatedGroupDescription = cursor.getColumnIndex(UserDB.COLUMN_GROUP_CREATED_DESCRIPTION);
		int indexCreatedGroupAvatar = cursor.getColumnIndex(UserDB.COLUMN_GROUP_CREATED_AVATAR);
		int indexCreatedGroupCover = cursor.getColumnIndex(UserDB.COLUMN_GROUP_CREATED_COVER);
		int indexCreatedGroupCreated = cursor.getColumnIndex(UserDB.COLUMN_GROUP_CREATED_CREATED);
		int indexCreatedGrouTotal = cursor.getColumnIndex(UserDB.COLUMN_GROUP_CREATED_TOTAL);
		User u = null;
		if(cursor.moveToFirst()){
			u = new User();
			u.setId(cursor.getInt(indexId));
			u.setNickName(cursor.getString(indexNickName));
			u.setRealName(cursor.getString(indexRealName));
			u.setSex((char)cursor.getInt(indexSex));
			String strBirth = cursor.getString(indexBirthday);
			if(strBirth != null){
				SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
				try {
					u.setBirthday(formater.parse(strBirth));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
			u.setAvatar(cursor.getString(indexAvatar));
			u.setDescription(cursor.getString(indexDescription));
			u.setSchool(cursor.getString(indexSchool));
			u.setAcademy(cursor.getString(indexAcademy));
			u.setGrade(cursor.getString(indexGrade));
			u.setJoinedGroupCount(cursor.getInt(indexJoinedGroupCount));
			u.setCreatedGroupCount(cursor.getInt(indexCreatedGroupCount));
			u.setMessageCount(cursor.getInt(indexMessageCount));
			u.setLoveState(cursor.getString(indexLoveState));
			if(cursor.getInt(indexCreateGroupId) != -1){
				Group created = new Group();
				created.setId(cursor.getInt(indexCreateGroupId));
				created.setName(cursor.getString(indexCreatedGroupName));
				created.setDescription(cursor.getString(indexCreatedGroupDescription));
				created.setAvatar(cursor.getString(indexCreatedGroupAvatar));
				created.setCover(cursor.getString(indexCreatedGroupCover));
				created.setCreated(cursor.getString(indexCreatedGroupCreated));
				created.setTotal(cursor.getInt(indexCreatedGrouTotal));
				UserItem item = new UserItem();
				item.setAvatar(cursor.getString(indexAvatar));
				item.setId(cursor.getInt(indexId));
				item.setNickName(cursor.getString(indexNickName));
				created.setFounder(item);
				u.createdGroup = new ArrayList<Group>();
				u.createdGroup.add(created);
			}
		}
		cursor.close();
		db.close();
		return u;
	}
	


}
