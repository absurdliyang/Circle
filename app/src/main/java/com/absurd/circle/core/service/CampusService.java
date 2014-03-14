package com.absurd.circle.core.service;


import com.absurd.circle.app.AppConstant;
import com.absurd.circle.core.client.CampusClient;
import com.absurd.circle.core.client.CampusRequest;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;

public class CampusService {
	
	protected static CommonLog log = LogFactory.createLog(AppConstant.TAG);
	
	private CampusClient client;
	
	
	public CampusService() {
		// TODO Auto-generated constructor stub
		this(new CampusClient());
	}
	public CampusService(CampusClient client) {
		// TODO Auto-generated constructor stub
		this.client = client;
	}

	
	public CampusClient getClient(){
		//this.client = client.setCredential(token);
		return this.client;
	}
	
	
	public CampusRequest createRequest(){
		return new CampusRequest();
	}
	
	
}
