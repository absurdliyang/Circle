package com.absurd.circle.core.service;

import com.absurd.circle.app.Auth;
import com.absurd.circle.core.bean.GroupItem;
import com.absurd.circle.core.bean.Message;
import com.absurd.circle.core.bean.MessagePage;
import com.absurd.circle.core.client.CampusClient;
import com.absurd.circle.core.client.CampusRequest;
import com.absurd.circle.core.client.CampusResponse;
import com.absurd.circle.core.client.Urls;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;

public class MessageService extends CampusService {
	
	
	public MessageService() {
		
		// TODO Auto-generated constructor stub
		super();
	}
	
	public MessageService(CampusClient client){
		super(client);
	}
	
	/**
	 * get public timeline
	 * @return
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 */
	public MessagePage getPublic() {
		CampusRequest request = createRequest();
		request.setUri(Urls.MESSAGES_PULBIC_TIMELINE)
		.setParams(null)
		.setType(MessagePage.class);
		CampusResponse response = null;
		try {
			response = getClient().get(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(response != null)
			return (MessagePage)response.getBody();
		return null;
	}
	
	public Message createMessage(Message message){
		Map<String,String> params = new HashMap<String,String>();
		params.put("content",message.getContent());
		if(message.getGroup() != null)
			params.put("from_group",message.getGroup().getId() + "");
		//params.put("lat",Double.toString(message.getLatitude()));
		//params.put("long", Double.toString(message.getLongitude()));
		if(message.getMedia() != null)
			params.put("media", message.getMedia());
		StringBuilder s = new StringBuilder();
		if(message.getGroups() != null && !message.getGroups().isEmpty()){
			for(GroupItem g : message.getGroups())
				s.append(g.getId() + "").append(",");
			s.deleteCharAt(s.length() - 1);
			params.put("to_groups", s.toString());
		}
		
		CampusRequest request = createRequest();
		request.setUri(Urls.MESSAGE_CRAETE)
		.setParams(params)
		.setType(Message.class);
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
			return (Message)response.getBody();
		return null;
	}
	
	
	/**
	 * get user's timeline
	 * @param userId
	 * @return
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 */
	public MessagePage getUserById(int userId) 
			throws ClientProtocolException, IOException{
		MessagePage result;
		try {
			String url = Urls.MESSAGES_USER_TIMELINE_BY_ID + userId + '/';
			log.i(url);
			result = getClient().setCredential(Auth.token).get(url,MessagePage.class,(NameValuePair[])null);
			log.i(result.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * get username's timeline
	 * @param username
	 * @return
	 */
	public MessagePage getUserByName(String username){
		MessagePage result;
		String url = Urls.MESSAGES_USER_TIMELINE_NY_NAME + username + '/';
		log.i(url);
		try {
			result = getClient().setCredential(Auth.token).get(url,MessagePage.class,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
		
	}
	
	/**
	 * get current authed user's timeline
	 * @return
	 */
	public MessagePage getUser(){
		return getUserByName(Auth.username);
	}
	
	/**
	 * get currentpage's next page
	 * @param url
	 * @return
	 */
	public MessagePage getNext(String url){
		MessagePage result;
		try {
			result = getClient().get(url,MessagePage.class,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * get group's timeline
	 * @param id
	 * @return
	 */
	public MessagePage getGroup(int id){
		MessagePage result;
		try{
			String url = Urls.MESSAGES_GROUP_TIMELINE + id + '/';
			log.i(url);
			result = getClient().setCredential(Auth.token).get(url,MessagePage.class,(NameValuePair[])null);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * Get group's public timeline
	 * @param id
	 * @return
	 */
	public MessagePage getGroupPublic(int id){
		MessagePage result;
		try{
			String url = Urls.MESSAGES_GROUP_TIMELINE_PUBLIC + id + '/';
			log.i(url);
			result = getClient().setCredential(Auth.token).get(url,MessagePage.class,(NameValuePair[])null);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * Praise a message
	 * @param messageId
	 * @return
	 */
	public Message praiseMessage(int messageId){
		Message result = null;
		String url = Urls.MESSAGE_PRAISE + messageId + '/';
		log.i(url);
		try {
			result = getClient().setCredential(Auth.token).post(url,Message.class,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Cancel praising a message
	 * @param messageId
	 * @return
	 */
	public boolean depraiseMessage(int messageId){
		String url = Urls.MESSAGE_DEPRAISE + messageId + '/';
		try {
			getClient().setCredential(Auth.token).delete(url,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * delete a message
	 */
	public boolean deleteMessage(int messageId){
		String url = Urls.MESSAGE_DELETE + messageId + '/';
		try {
			getClient().setCredential(Auth.token).delete(url, (NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
}
