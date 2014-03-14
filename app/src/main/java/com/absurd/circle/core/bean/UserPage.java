package com.absurd.circle.core.bean;

import java.util.List;

public class UserPage extends PageBase{
	
	private List<UserItem> results;

	public void setResults(List<UserItem> results){
		this.results = results;
	}
	
	
	@Override
	public List<UserItem> getResults() {
		// TODO Auto-generated method stub
		return results;
	}
}
