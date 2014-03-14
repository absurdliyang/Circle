package com.absurd.circle.core.bean;

import java.io.Serializable;

public class School implements Serializable{
	/**
	 * serialize ID
	 */
	private static final long serialVersionUID = -3263869370763957064L;
	
	private int id;
	private String name;
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
	@Override
	public String toString() {
		return "School [id=" + id + ", name=" + name + "]";
	}
	
	
	
}
