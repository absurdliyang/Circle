package com.absurd.circle.core.bean;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

public class UserItem  implements Serializable{
	/**
	 * serialize ID
	 */
	private static final long serialVersionUID = -1143182077705439821L;

	private int id;
	
	@SerializedName("nick_name")
	private String nickName;
	
	private String avatar;
	
	@SerializedName("userType")
	private char userType;
	
	@SerializedName("push_id")
	private String pushId;
	
	

	public String getPushId() {
		return pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public char getUserType() {
		return userType;
	}

	public void setUserType(char userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "UserProfileItem [id=" + id + ", nickName=" + nickName
				+ ", avatar=" + avatar + ", userType=" + userType + "]";
	}

}
