package com.absurd.circle.core.bean;

import java.util.List;


public abstract class PageBase {
	private int count;
	
	private String next;
	
	private String previous;
	
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}
	
	public abstract List<?> getResults();
}
