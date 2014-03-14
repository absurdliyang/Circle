package com.absurd.circle.core.client;


public interface Urls {
	String BASIC_URL = "http://1.campusv.duapp.com/api/circle";
	

	/**
	 * user register
	 */
	String USER_REGISTER = BASIC_URL
			+ "/user/";
	/**
	 * user's profile by id
	 */
	String USER_PROFILE_BY_ID = BASIC_URL
			+ "/user/profile/id/";
	/**
	 * user's profile by username
	 */
	String USER_PROFILE_BY_NAME = BASIC_URL
			+ "/user/profile/username/";
	String USER_PROFILE_UPDATE = BASIC_URL
			+ "/user/profile/update/";
	/**
	 * user login
	 */
	String USER_LOGIN = BASIC_URL
			+ "/api-token-auth/";
	/**
	 * list group's users
	 */
	String USERS_GROUP = BASIC_URL
			+ "/users/group/";
	
	
	
	/**
	 * create a message  
	 */
	String MESSAGE_CRAETE = BASIC_URL
			+ "/message/";
	/**
	 * delete a message
	 */
	String MESSAGE_DELETE = BASIC_URL
			+"/message/";
	/**
	 * praise a message
	 */
	String MESSAGE_PRAISE = BASIC_URL
			+ "/message/praise/";
	String MESSAGE_DEPRAISE = BASIC_URL
			+ "/message/depraise/";
	/**
	 * praised messages
	 */
	String MESSAGE_PRAISED = BASIC_URL
			+ "/messages/praised/";

	/**
	 * collect a message
	 */
	String MESSAGE_COLLECT = BASIC_URL
			+ "/message/collect/";
	/**
	 * collected messages
	 */
	String MESSAGES_COLLECTED = BASIC_URL
			+ "/messages/collected/";
	/**
	 * forward a message
	 */
	String MESSAGE_FORWARD = BASIC_URL
			+ "/message/forward/";
	
	/**
	 * public time_line
	 */
	String MESSAGES_PULBIC_TIMELINE = BASIC_URL 
			+ "/messages/public_timeline/";
	/**
	 * user's time_line
	 */
	String MESSAGES_USER_TIMELINE_BY_ID = BASIC_URL 
			+ "/messages/user_timeline/id/";
	String MESSAGES_USER_TIMELINE_NY_NAME = BASIC_URL
			+ "/messages/user_timeline/username/";
	/**
	 * group's time_line
	 */
	String MESSAGES_GROUP_TIMELINE = BASIC_URL 
			+ "/messages/group_timeline/";
	
	String MESSAGES_GROUP_TIMELINE_PUBLIC = BASIC_URL
			+ "/messages/group_timeline/public/";
	

	
	
	/**
	 * user's groups filter by userid
	 */
	String GROUPS_USER_BY_ID = BASIC_URL
			+ "/groups/user/id/";
	/**
	 * user's groups filter by username
	 */
	String GROUPS_USER_BY_NAME = BASIC_URL
			+"/groups/user/username/";
	/**
	 * all groups
	 */
	String GROUPS_LIST = BASIC_URL
			+ "/groups/";
	/**
	 * create a group
	 */
	String GROUP_CREATE = BASIC_URL
			+"/group/";
	/**
	 * group's profile
	 */
	String GROUP_PROFILE = BASIC_URL
			+"/group/";
	/**
	 * user add a group
	 */
	String GROUP_ADD = BASIC_URL
			+"/group/add/";
	/**
	 * user exit a group
	 */
	String GROUP_EXIT = BASIC_URL
			+"/group/exit";
	
	
	
	
	
	/**
	 * message's comments
	 */
	String COMMENTS_MESSAGE_LIST = BASIC_URL
			+ "/comments/message/";
	/**
	 * create a comment
	 */
	String COMMENT_CREATE = BASIC_URL
			+ "/comment/";
	/**
	 * reply a message
	 */
	String COMMENT_REPLY = BASIC_URL
			+"/reply/";
	/**
	 * delete a comment
	 */
	String COMMENT_DELETE = BASIC_URL
			+ "/comment/";
	
	
}
