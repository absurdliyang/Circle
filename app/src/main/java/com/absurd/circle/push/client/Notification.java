package com.absurd.circle.push.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Database.DAOHelper;

import android.content.ContentValues;
import android.database.Cursor;

import com.absurd.circle.core.bean.GroupItem;
import com.absurd.circle.core.bean.Message;
import com.absurd.circle.core.bean.UserItem;
import com.absurd.circle.core.db.NotificationDB;


public class Notification{
	
	private int id;
	
	/**
	 * 1  ��
	 * 2 ����
	 * 3 ��������
	 * 4 �ظ�
	 */
	private int type;
	
	private int fromUserId;
	
	private String fromUserName;
	
	private String fromUserAvatar;
	
	private String Content;
	
	private Message message;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	
	

	public String getFromUserAvatar() {
		return fromUserAvatar;
	}

	public void setFromUserAvatar(String fromUserAvatar) {
		this.fromUserAvatar = fromUserAvatar;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", type=" + type + ", fromUserId="
				+ fromUserId + ", fromUserName=" + fromUserName + ", Content="
				+ Content + ", message=" + message + "]";
	}
	
	
	public synchronized boolean saveToDB(){
		NotificationDB db = (NotificationDB)DAOHelper.getInstance().getTable(NotificationDB.TABLE);
		db.open();
		ContentValues value = new ContentValues();
		value.put(NotificationDB.COLUMN_ID, this.id);
		value.put(NotificationDB.COLUMN_FROM_USER_ID, this.fromUserId);
		value.put(NotificationDB.COLUMN_FROM_USER_NAME,this.fromUserName);
		value.put(NotificationDB.COLUMN_FROM_USER_AVATAR,this.fromUserAvatar);
		value.put(NotificationDB.COLUMN_TYPE,this.type);
		if(type != 3){
			value.put(NotificationDB.COLUMN_MESSAGE_ID,this.message.getId());
			if(message.getUser() != null){
				value.put(NotificationDB.COLUMN_MESSAGE_USERID,this.message.getUser().getId());
				value.put(NotificationDB.COLUMN_MESSAGE_USERNAME,this.message.getUser().getNickName());
				value.put(NotificationDB.COLUMN_MESSAGE_USERAVATAR,this.message.getUser().getAvatar());
			}
			if(this.message.getGroup() != null){
				value.put(NotificationDB.COLUMN_MESSAGE_GROUPID,this.message.getGroup().getId());
				value.put(NotificationDB.COLUMN_MESSAGE_GROUPNAME,this.message.getGroup().getName());
				value.put(NotificationDB.COLUMN_MESSAGE_GROUPAVATAR,this.message.getGroup().getAvatar());
			}
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
			String strCreated = formater.format(this.message.getCreated());
			value.put(NotificationDB.COLUMN_MESSAGE_CREATED,strCreated);
			value.put(NotificationDB.COLUMN_MESSAGE_CONTENT, this.message.getContent());
			value.put(NotificationDB.COLUMN_MESSAGE_MEDIA,this.message.getMedia());
			value.put(NotificationDB.COLUMN_MESSAGE_COMMENTCOUNT, this.message.getCommentCount());
			value.put(NotificationDB.COLUMN_MESSAGE_PRAISECOUNT,this.message.getPraiseCount());
			value.put(NotificationDB.COLUMN_MESSAGE_COLLECTCOUNT,this.message.getCollectCount());
			value.put(NotificationDB.COLUMN_MESSAGE_FORWARDCOUNT,this.message.getForwardCount());
		}
		
		db.saveToDB(value);
		db.close();
		return true;
	}
	
	
	public static synchronized List<Notification> readFromDB(){
		NotificationDB db = (NotificationDB)DAOHelper.getInstance().getTable(NotificationDB.TABLE);
		db.open();
		Cursor cursor = db.readFromDB();
		
		int indexId = cursor.getColumnIndex(NotificationDB.COLUMN_ID);
		int indexType = cursor.getColumnIndex(NotificationDB.COLUMN_TYPE);
		int indexFromUserId = cursor.getColumnIndex(NotificationDB.COLUMN_FROM_USER_ID);
		int indexFromUserName = cursor.getColumnIndex(NotificationDB.COLUMN_FROM_USER_NAME);
		int indexFromUserAvatar = cursor.getColumnIndex(NotificationDB.COLUMN_FROM_USER_AVATAR);
		int indexMessageId = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_ID);
		int indexUserId = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_USERID);
		int indexUsername = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_USERNAME);
		int indexUserAvatar = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_USERAVATAR);
		int indexGroupId = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_GROUPID);
		int indexGroupName = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_GROUPNAME);
		int indexGroupAvatar = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_GROUPAVATAR);
		int indexCreated = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_CREATED);
		int indexContent = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_CONTENT);
		int indexMedia = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_MEDIA);
		int indexCommentCount = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_COMMENTCOUNT);
		int indexPraiseCount = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_PRAISECOUNT);
		int indexCollectCount = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_COLLECTCOUNT);
		int indexForwardCount = cursor.getColumnIndex(NotificationDB.COLUMN_MESSAGE_FORWARDCOUNT);
		
		
		List<Notification> listNotification = new ArrayList<Notification>();
		while(cursor.moveToNext()){
			Notification m = new Notification();
			m.setId(cursor.getInt(indexId));
			m.setType(cursor.getInt(indexType));
			m.setFromUserId(cursor.getInt(indexFromUserId));
			m.setFromUserName(cursor.getString(indexFromUserName));
			m.setFromUserAvatar(cursor.getString(indexFromUserAvatar));
			if(m.getType() != 3){
				m.message = new Message();
				m.setId(cursor.getInt(indexMessageId));
				UserItem u = new UserItem();
				u.setId(cursor.getInt(indexUserId));
				u.setNickName(cursor.getString(indexUsername));
				u.setAvatar(cursor.getString(indexUserAvatar));
				m.message.setUser(u);
				GroupItem g = new GroupItem();
				g.setId(cursor.getInt(indexGroupId));
				g.setName(cursor.getString(indexGroupName));
				g.setAvatar(cursor.getString(indexGroupAvatar));
				m.message.setGroup(g);
				m.message.setContent(cursor.getString(indexContent));
				String tmp = cursor.getString(indexCreated);
				SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
				Date date = null;
				try {
					date = formater.parse(tmp);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				m.message.setCreated(date);
				m.message.setMedia(cursor.getString(indexMedia));
				m.message.setCommentCount(cursor.getInt(indexCommentCount));
				m.message.setPraiseCount(cursor.getInt(indexPraiseCount));
				m.message.setCollectCount(cursor.getInt(indexCollectCount));
				m.message.setForwardCount(cursor.getInt(indexForwardCount));
			}
			listNotification.add(m);
		}
		cursor.close();
		db.close();
		return listNotification;
		
		
	}
	
	
	
}
