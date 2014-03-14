package com.absurd.circle.core.bean;

import java.io.Serializable;
import java.util.Date;


/**
 * ��Ϣ�б� message item
 * @author absurd
 *
 */
public class MessageItem implements Serializable{

	/**
	 * serialize ID
	 */
	private static final long serialVersionUID = 798810035771632510L;

	private String content;
	
	private String location;
	
	private String media;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	@Override
	public String toString() {
		return "MessageItem [content=" + content + ", location=" + location
				+ ", media=" + media + "]";
	}

	
	
	
	
}
