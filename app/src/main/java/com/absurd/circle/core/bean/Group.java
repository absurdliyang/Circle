package com.absurd.circle.core.bean;

import java.io.Serializable;

public class Group implements Serializable{
	/**
	 * serialize ID
	 */
	private static final long serialVersionUID = 6444217954371113339L;

	private int id;
	
	private String name;
	
	private String description;
	
	private String avatar;
	
	private String cover;
	
	private UserItem founder;
	
	private String created;
	
	private int total;
	
	

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public UserItem getFounder() {
		return founder;
	}

	public void setFounder(UserItem founder) {
		this.founder = founder;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "GroupProfile [id=" + id + ", name=" + name + ", description="
				+ description + ", avatar=" + avatar + ", founder=" + founder
				+ ", created=" + created + ", total=" + total + "]";
	}

	public GroupItem toGroupItem(){
		GroupItem groupItem = new GroupItem();
		groupItem.setId(this.getId());
		groupItem.setName(this.getName());
		groupItem.setDescription(this.getDescription());
		groupItem.setTotal(this.getTotal());
		return groupItem;
	}

}
