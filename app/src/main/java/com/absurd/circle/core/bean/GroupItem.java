package com.absurd.circle.core.bean;

import java.io.Serializable;

import android.graphics.drawable.Drawable;


public class GroupItem implements Serializable{
	/**
	 * serialize ID
	 */
	private static final long serialVersionUID = -4409040253466017999L;

	private int id;
	
	private String name;
	
	private String avatar;
	
	private String description;
	
	private int total;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "GroupItem [id=" + id + ", name=" + name + ", avatar=" + avatar
				+ ", description=" + description + ", total=" + total + "]";
	}

}
