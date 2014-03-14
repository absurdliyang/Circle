package com.absurd.circle.core.bean;

import java.io.Serializable;

public class Academy implements Serializable{
	/**
	 * serialize ID
	 */
	private static final long serialVersionUID = 1476913526955296535L;

	private int id;
	
	private String name;
	
	private String description;
	
	private School school;
	
	public void setSchool(School school){
		this.school = school;
	}
	public School getSchool(){
		return school;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
