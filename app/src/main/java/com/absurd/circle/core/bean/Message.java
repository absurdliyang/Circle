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
import android.graphics.drawable.Drawable;

import com.absurd.circle.core.db.MessageDB;
import com.google.gson.annotations.SerializedName;

public class Message implements Serializable{
	/**
	 * serialize ID
	 */
	private static final long serialVersionUID = 3297723782828432144L;

	private int id;
	
	private UserItem user;
	
	// If group is not null, then this message is a public message
	private GroupItem group;
	
	private Date created;
	
	private String content;
	
	private String media;
	
	private double longitude;
	
	private double latitude;
	
	private String location;
	
	@SerializedName("comment_count")
	private int commentCount;
	
	@SerializedName("praise_count")
	private int praiseCount;
	
	@SerializedName("collect_count")
	private int collectCount;
	
	@SerializedName("forward_count")
	private int forwardCount;
	
	private List<GroupItem> groups;
	
	private int forward;
	
	@SerializedName("praise")
	private List<UserItem> praisers;
	
	public List<UserItem> getPraisers(){
		return this.praisers;
	}
	
	public void setPraisers(List<UserItem> praisers){
		this.praisers = praisers;
	}
	/**
	 * Check if current authed user is this message's praiser 
	 * @param userId
	 * @return
	 */
	public boolean isPraiser(int userId){
		if(praisers != null)
			for(UserItem user: praisers)
				if(user.getId() == userId)
					return true;
		return false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserItem getUser() {
		return user;
	}

	public void setUser(UserItem user) {
		this.user = user;
	}

	public GroupItem getGroup() {
		return group;
	}

	public void setGroup(GroupItem group) {
		this.group = group;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}
	
	public double getLongitude(){
		return this.longitude;
	}

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	public double getLatitude(){
		return this.latitude;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public int getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(int collectCount) {
		this.collectCount = collectCount;
	}

	public int getForwardCount() {
		return forwardCount;
	}

	public void setForwardCount(int forwardCount) {
		this.forwardCount = forwardCount;
	}

	public List<GroupItem> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupItem> groups) {
		this.groups = groups;
	}

	public int getForward() {
		return forward;
	}

	public void setForward(int forward) {
		this.forward = forward;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", user=" + user + ", group=" + group
				+ ", created=" + created + ", content=" + content + ", media="
				+ media + ", location=" + location + ", commentCount="
				+ commentCount + ", praiseCount=" + praiseCount
				+ ", collectCount=" + collectCount + ", forwardCount="
				+ forwardCount + ", groups=" + groups + ", forward=" + forward
				+ "]";
	}

	/**
	 * Check if this message a public message
	 * @return
	 */
	public boolean isPublic(){
		return this.group != null ? true : false;
	}

	/**
	 * Save message to sqlite cache
	 * @return whether success or failed
	 */
	public synchronized boolean saveToDB(){
		MessageDB db = (MessageDB)DAOHelper.getInstance().getTable(MessageDB.TABLE);
		db.open();
		ContentValues value = new ContentValues();
		value.put(MessageDB.COLUMN_ID, this.id);
		value.put(MessageDB.COLUMN_USERID,this.user.getId());
		value.put(MessageDB.COLUMN_USERNAME,this.user.getNickName());
		value.put(MessageDB.COLUMN_USERAVATAR,this.user.getAvatar());
		value.put(MessageDB.COLUMN_GROUPID,this.group.getId());
		value.put(MessageDB.COLUMN_GROUPNAME,this.group.getName());
		value.put(MessageDB.COLUMN_GROUPAVATAR,this.group.getAvatar());
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		String strCreated = formater.format(this.created);
		value.put(MessageDB.COLUMN_CREATED,strCreated);
		value.put(MessageDB.COLUMN_CONTENT, this.content);
		value.put(MessageDB.COLUMN_MEDIA,this.media);
		value.put(MessageDB.COLUMN_COMMENTCOUNT, this.commentCount);
		value.put(MessageDB.COLUMN_PRAISECOUNT,this.praiseCount);
		value.put(MessageDB.COLUMN_COLLECTCOUNT,this.collectCount);
		value.put(MessageDB.COLUMN_FORWARDCOUNT,this.forwardCount);
		
		db.saveToDB(value);
		db.close();
		return true;
	}
	/**
	 * Get mesages from sqlite cache
	 * @return the Messages list
	 */
	public static synchronized List<Message> readFromDB(){
		MessageDB db = (MessageDB)DAOHelper.getInstance().getTable(MessageDB.TABLE);
		db.open();
		Cursor cursor = db.readFromDB();
		int indexId = cursor.getColumnIndex(MessageDB.COLUMN_ID);
		int indexUserId = cursor.getColumnIndex(MessageDB.COLUMN_USERID);
		int indexUsername = cursor.getColumnIndex(MessageDB.COLUMN_USERNAME);
		int indexUserAvatar = cursor.getColumnIndex(MessageDB.COLUMN_USERAVATAR);
		int indexGroupId = cursor.getColumnIndex(MessageDB.COLUMN_GROUPID);
		int indexGroupName = cursor.getColumnIndex(MessageDB.COLUMN_GROUPNAME);
		int indexGroupAvatar = cursor.getColumnIndex(MessageDB.COLUMN_GROUPAVATAR);
		int indexCreated = cursor.getColumnIndex(MessageDB.COLUMN_CREATED);
		int indexContent = cursor.getColumnIndex(MessageDB.COLUMN_CONTENT);
		int indexMedia = cursor.getColumnIndex(MessageDB.COLUMN_MEDIA);
		int indexCommentCount = cursor.getColumnIndex(MessageDB.COLUMN_COMMENTCOUNT);
		int indexPraiseCount = cursor.getColumnIndex(MessageDB.COLUMN_PRAISECOUNT);
		int indexCollectCount = cursor.getColumnIndex(MessageDB.COLUMN_COLLECTCOUNT);
		int indexForwardCount = cursor.getColumnIndex(MessageDB.COLUMN_FORWARDCOUNT);
		
		List<Message> listMessage = new ArrayList<Message>();
		while(cursor.moveToNext()){
			Message m = new Message();
			m.setId(cursor.getInt(indexId));
			UserItem u = new UserItem();
			u.setId(cursor.getInt(indexUserId));
			u.setNickName(cursor.getString(indexUsername));
			u.setAvatar(cursor.getString(indexUserAvatar));
			m.setUser(u);
			GroupItem g = new GroupItem();
			g.setId(cursor.getInt(indexGroupId));
			g.setName(cursor.getString(indexGroupName));
			g.setAvatar(cursor.getString(indexGroupAvatar));
			m.setGroup(g);
			m.setContent(cursor.getString(indexContent));
			String tmp = cursor.getString(indexCreated);
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
			Date date = null;
			try {
				date = formater.parse(tmp);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			m.setCreated(date);
			m.setMedia(cursor.getString(indexMedia));
			m.setCommentCount(cursor.getInt(indexCommentCount));
			m.setPraiseCount(cursor.getInt(indexPraiseCount));
			m.setCollectCount(cursor.getInt(indexCollectCount));
			m.setForwardCount(cursor.getInt(indexForwardCount));
			listMessage.add(m);
		}
		cursor.close();
		db.close();
		return listMessage;
		
	}
	
	

	
}
