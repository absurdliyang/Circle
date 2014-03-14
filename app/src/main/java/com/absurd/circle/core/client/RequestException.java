package com.absurd.circle.core.client;

import java.io.IOException;

public class RequestException extends IOException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final RequestError error;
	private final int status;

	public RequestException(RequestError error,int status) {
		// TODO Auto-generated constructor stub
		super();
		this.error = error;
		this.status = status;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return error != null ? error.getDetail() + " " + status : super.getMessage();
	}
	
	public RequestError getError(){
		return this.error;
	}
	
	public int getStatus(){
		return this.status;
	}
}
