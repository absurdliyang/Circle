package com.absurd.circle.app;


import com.absurd.circle.core.bean.User;
import com.absurd.circle.core.bean.UserItem;

public abstract class Auth {
	/**
	 * current user's token
	 */
	public static String token;
	
	public static String username;
	
	public static User user;
	
	private static UserItem userItem;
	
	public static boolean isAuthed(){
		return token != null ? true : false;
	}
	
	
	public static UserItem getUserItem(){
		if(user != null){
			if(userItem == null){
				userItem = new UserItem();
				userItem.setAvatar(user.getAvatar());
				userItem.setId(user.getId());
				userItem.setNickName(user.getNickName());
				userItem.setUserType(user.getUserType());
			}
			return userItem;
		}
		return null;
		
	}
}
