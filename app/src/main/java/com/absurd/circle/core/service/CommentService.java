package com.absurd.circle.core.service;


import com.absurd.circle.app.Auth;
import com.absurd.circle.core.bean.Comment;
import com.absurd.circle.core.bean.CommentPage;
import com.absurd.circle.core.client.CampusClient;
import com.absurd.circle.core.client.CampusRequest;
import com.absurd.circle.core.client.CampusResponse;
import com.absurd.circle.core.client.Urls;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;



public class CommentService extends CampusService{
	
	public CommentService() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public CommentService(CampusClient client){
		super(client);
	}
	
	public CommentPage getMessage(int messageId){
		CommentPage result;
		String url = Urls.COMMENTS_MESSAGE_LIST + messageId + '/';
		log.i(url);
		try {
			result = getClient().setCredential(Auth.token).get(url,CommentPage.class,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
		
	}
	
	
	public CommentPage getNext(String url){
		CommentPage result;
		try {
			result = getClient().get(url,CommentPage.class,(NameValuePair[])null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * Create a comment
	 * @param comment
	 * @return
	 */
	public Comment createComment(Comment comment){
		Map<String,String> params = new HashMap<String,String>();
		params.put("content", comment.getContent());
		params.put("message",comment.getMessage() + "");
		if(comment.getReply() != null)
			params.put("reply",comment.getReply().getId() + "");
		CampusRequest request = new CampusRequest()
		.setUri(Urls.COMMENT_CREATE)
		.setType(Comment.class)
		.setParams(params);
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
			return (Comment)response.getBody();
		return null;
		
	}
	
	/**
	 * Create a reply
	 * @param reply
	 * @return
	 */
	public Comment createReply(Comment reply){
		Map<String,String> params = new HashMap<String,String>();
		params.put("content",reply.getContent());
		params.put("comment", reply.getReply().getId() + "");
		CampusRequest request = new CampusRequest()
		.setUri(Urls.COMMENT_REPLY)
		.setType(Comment.class)
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
			return (Comment)response.getBody();
		return null;
	}
	
	/**
	 * Delete a comment
	 * @param commentId
	 * @return
	 */
	public boolean deleteComment(int commentId){
		String url = Urls.COMMENT_DELETE + commentId + '/';
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
