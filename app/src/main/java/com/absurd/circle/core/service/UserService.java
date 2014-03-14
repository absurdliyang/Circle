package com.absurd.circle.core.service;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.Auth;
import com.absurd.circle.core.bean.Token;
import com.absurd.circle.core.bean.User;
import com.absurd.circle.core.bean.UserPage;
import com.absurd.circle.core.client.CampusClient;
import com.absurd.circle.core.client.CampusRequest;
import com.absurd.circle.core.client.CampusResponse;
import com.absurd.circle.core.client.Urls;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;


public class UserService extends CampusService{
	CommonLog log = LogFactory.createLog(AppConstant.TAG);
	
	
	public UserService() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public UserService(CampusClient client){
		super(client);
	}
	/**
	 * login
	 * @param username
	 * @param password
	 * @return
	 */
	public Token login(String username,String password){
		Token token = null;
		String url = Urls.USER_LOGIN;
		log.i(url);
		try {
			String jsonObject = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"; 
			token = getClient().post(url,Token.class,jsonObject,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return token;
		}
		return token;
	}
	/**
	 * register a new user
	 * @param username
	 * @param email
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	public User register(String username,String email,String password) throws Exception{
		User user = null;
		String url = Urls.USER_REGISTER;
			String jsonObject =  "{\"username\":\"" + username + "\",\"email\":\""  + email + "\",\"password\":\"" + password + "\"}";
			log.i(jsonObject);
			user = getClient().post(url, User.class, jsonObject,(NameValuePair[])null);
		return user;
	}
	
	/**
	 * get current user's profile
	 * @return
	 */
	public User getProfile(){
		User user;
		if(!Auth.isAuthed())
			return null;
		String url = Urls.USER_PROFILE_BY_NAME + Auth.username + '/';
		log.i(url);
		try{
			user = getClient().setCredential(Auth.token).get(url,User.class,(NameValuePair[])null);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		log.i(user.toString());
		return user;
	}
	/**
	 * get username's profile
	 * @param username
	 * @return
	 */
	public User getProfile(String username){
		User user;
		if(!Auth.isAuthed())
			return null;
		log.i(Auth.token);
		String url = Urls.USER_PROFILE_BY_NAME + username + '/';
		log.i(url);
		try{
			user = getClient().setCredential(Auth.token).get(url,User.class,(NameValuePair[])null);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return user;
	}
	
	/**
	 * get id's profile
	 * @param id
	 * @return
	 */
	public User getProfile(int id){
		User user;
		if(!Auth.isAuthed())
			return null;
		String url = Urls.USER_PROFILE_BY_ID + id + '/'	;
		log.i(url);
		try{
			user = getClient().setCredential(Auth.token).get(url,User.class,(NameValuePair[])null);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return user;
	}
	
	/**
	 * Update authed user's profile
	 */
	public User update(User user){
		if(!Auth.isAuthed())
			return null;
		Map<String,String> params = new HashMap<String,String>();
		if(user != null){
			if(user.getNickName() != null)
				params.put("nick_name",user.getNickName());
			if(user.getAvatar() != null)
				params.put("avatar", user.getAvatar());
			params.put("sex", user.getSex()+"");
			if(user.getSchool() != null)
				params.put("school",user.getSchool());
			if(user.getAcademy() != null)
				params.put("academy",user.getAcademy());
			if(user.getGrade() != null)
				params.put("grade",user.getGrade());
			if(user.getRealName() != null)
				params.put("real_name", user.getRealName());
			if(user.getLoveState() != null)
				params.put("love_state",user.getLoveState());
			if(user.getDescription() != null)
				params.put("description", user.getDescription());
			if(user.getPushId() != null)
				params.put("push_id",user.getPushId());
		}
		CampusRequest request = createRequest();
		request.setUri(Urls.USER_PROFILE_UPDATE)
			.setParams(params)
			.setType(User.class);
		log.i(request.getUri());
		CampusResponse response = null;
		try {
			response = getClient().setCredential(Auth.token).post(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(response != null)
			return (User)response.getBody();
		return null;
	}
	
	
	/**
	 * get group's users
	 * @param groupId
	 * @return
	 */
	public UserPage getUsersByGroup(int groupId){
		UserPage page = null;
		String url = Urls.USERS_GROUP + groupId + '/';
		log.i(url);
		try {
			page = getClient().setCredential(Auth.token).get(url,UserPage.class,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return page;
		}
		return page;
	}
	/**
	 * get next page 
	 * @param url
	 * @return
	 */
	public UserPage getNext(String url){
		UserPage result;
		try {
			result = getClient().get(url,UserPage.class,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}

	
	
}
 
