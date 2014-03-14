package com.absurd.circle.core.bean;

import java.util.List;

public class MessagePage extends PageBase{
	private List<Message> results;
	
	public void setResults(List<Message> results){
		this.results = results;
	}
	
	@Override
	public List<Message> getResults() {
		// TODO Auto-generated method stub
		return results;
	}

	@Override
	public String toString() {
		String r = "";
		for(Object result:results){
			r += ((Message) result).toString();
			
		}
		return r; 
	}

	
}
