package com.absurd.circle.core.bean;

import java.io.Serializable;

public class Token implements Serializable{
	/**
	 * serialize ID
	 */
	private static final long serialVersionUID = 2708593493219338759L;
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "Token [token=" + token + "]";
	}
	
	
}
