package com.absurd.circle.core.service;

import com.absurd.circle.app.Auth;
import com.absurd.circle.core.bean.Group;
import com.absurd.circle.core.bean.GroupPage;
import com.absurd.circle.core.client.CampusClient;
import com.absurd.circle.core.client.CampusRequest;
import com.absurd.circle.core.client.CampusResponse;
import com.absurd.circle.core.client.Urls;
import com.absurd.circle.core.util.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;


public class GroupService extends CampusService{
	public GroupService() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public GroupService(CampusClient client){
		super(client);
	}
	
	/**
	 * get user's groups by user id
	 * @param userId
	 * @return
	 */
	public GroupPage getGroupsByUserid(final int userId){
		GroupPage page = null;
		String url = Urls.GROUPS_USER_BY_ID + userId + '/';
		log.i("getGroupsByUserid " + url);
		try {
			page = getClient().setCredential(Auth.token).get(url,GroupPage.class,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return page;
	}
	/**
	 * get user's groups by username
	 * @param username
	 * @return
	 */
	public GroupPage getGroupsByUsername(final String username){
		GroupPage page = null;
		String url = Urls.GROUPS_USER_BY_NAME + username + '/';
		log.i("getGroupsByUsername " + url);
		try{
			page = getClient().setCredential(Auth.token).get(url,GroupPage.class,(NameValuePair[])null);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return page;
	}
	/**
	 * get current user's groups
	 * @return
	 */
	public GroupPage getGroups(){
		return getGroupsByUsername(Auth.username);
	}
	
	/**
	 * Get all groups
	 * @return
	 */
	public GroupPage getAllGroup(){
		GroupPage page = null;
		String url = Urls.GROUPS_LIST;
		log.i("getAllGroups" + url);
		try {
			page = getClient().get(url,GroupPage.class,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return page;
	}
	
	public Group createGroup(Group group){
		Map<String,String>params = new HashMap<String,String>();
		params.put("name",group.getName());
		params.put("avatar",group.getAvatar());
		CampusRequest request = new CampusRequest().setUri(Urls.GROUP_CREATE)
				.setParams(params)
				.setType(Group.class);
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
			return (Group)response.getBody();
		return null;
	}
	
	/**
	 * Update group profile
	 * @param group
	 * @return
	 */
	public Group update(Group group){
		Group result;
		String url = Urls.GROUP_PROFILE + group.getId() + '/';
		log.i(url);
		String strJson = JsonUtil.toJson(group);
		try {
			result = getClient().setCredential(Auth.token).put(url,Group.class,strJson);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
		
	}
	
	/**
	 * get next page
	 * @param url
	 * @return
	 */
	public GroupPage getNext(final String url){
		GroupPage result;
		try {
			result = getClient().get(url,GroupPage.class,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
		
	}
	
	/**
	 * Get group's detail infos
	 * @param groupId
	 * @return
	 */
	public Group getDetail(final int groupId) {
		Group result;
		String url = Urls.GROUP_PROFILE + groupId + '/';
		try {
			result = getClient().setCredential(Auth.token).get(url,Group.class,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * Add a group
	 */
	public boolean addGroup(final int groupId){
		Map<String,String> params = new HashMap<String,String>();
		params.put("group", groupId+"");
		CampusRequest request = new CampusRequest()
			.setUri(Urls.GROUP_ADD)
			.setParams(params);
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
			return true;
		return false;
	}
	
	/**
	 * Exit the given group
	 */
	public boolean exitGroup(final int groupId){
		Map<String,String> params = new HashMap<String,String>();
		params.put("group",groupId + "");
		CampusRequest request = new CampusRequest()
			.setUri(Urls.GROUP_EXIT)
			.setParams(params);
		try {
			getClient().setCredential(Auth.token)
					.delete(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
}
