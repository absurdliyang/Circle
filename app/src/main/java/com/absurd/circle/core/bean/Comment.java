package com.absurd.circle.core.bean;

import java.io.Serializable;
import java.util.Date;



public class Comment implements Serializable{
	/**
	 * serialize ID
	 */
	private static final long serialVersionUID = -1269566638768398874L;

	private int id;
	
	private Date created;
	
	private String content;
	
	private UserItem user;
	
	private int message;
	
	private UserItem reply;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public UserItem getUser() {
		return user;
	}

	public void setUser(UserItem user) {
		this.user = user;
	}

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public UserItem getReply() {
		return reply;
	}

	public void setReply(UserItem reply) {
		this.reply = reply;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", created=" + created + ", content="
				+ content + ", user=" + user + ", message=" + message
				+ ", reply=" + reply + "]";
	}

	
	
}
