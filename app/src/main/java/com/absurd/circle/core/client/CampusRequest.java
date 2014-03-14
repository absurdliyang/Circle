package com.absurd.circle.core.client;

import com.absurd.circle.core.util.UrlUtils;

import java.lang.reflect.Type;
import java.util.Map;


public class CampusRequest {
	private String uri;
	
	private Map<String,String> params;
	
	private Type type;
	
	private Type arrayType;
	
	public CampusRequest setArrayType(Type arrayType){
		this.arrayType = arrayType;
		return this;
	}
	public Type getArrayType(){
		return this.arrayType;
	}
	
	
	public CampusRequest setType(Type type){
		this.type = type;
		return this;
	}
	
	public Type getType(){
		return this.type;
	}
	
	public CampusRequest() {
		// TODO Auto-generated constructor stub
	}
	
	public CampusRequest setUri(String uri){
		this.uri = uri;
		return this;
	}
	
	public String getUri(){
		return uri;
	}
	
	public CampusRequest setParams(Map<String,String> params){
		this.params = params;
		return this;
	}
	
	public Map<String,String> getParams(){
		return this.params;
	}
	
	/**
	 * 
	 * Add request params to uri
	 * 
	 * @param uri
	 */
	public void addParams(final StringBuilder uri){
		UrlUtils.addParams(getParams(), uri);
	}
	
	/**
	 * Generate full uri
	 * 
	 * @return
	 */
	public String generateUri(){
		final String baseUri = uri;
		if(baseUri == null)
			return null;
		if(baseUri.indexOf('?') != -1)
			return baseUri;
		final StringBuilder params = new StringBuilder();
		addParams(params);
		if(params.length() > 0)
			return baseUri + '?' + params;
		else
			return baseUri;
	}
	
	
	
	
	
	public int hashCode(){
		final String fullUri = generateUri();
		return fullUri != null ? fullUri.hashCode() : super.hashCode();
	}
	
	public boolean equals(final Object obj){
		if(obj == this)
			return true;
		if(!(obj instanceof CampusRequest))
			return false;
		final String fullUri = generateUri();
		final String objUri = ((CampusRequest) obj).generateUri();
		return fullUri != null && objUri != null && fullUri.equals(objUri);
	}
	
	public String toString(){
		final String fullUri = generateUri();
		return fullUri != null ? fullUri : super.toString();
	}
	
	
	
}	
