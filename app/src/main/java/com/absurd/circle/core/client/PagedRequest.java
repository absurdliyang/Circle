package com.absurd.circle.core.client;


import com.absurd.circle.core.util.UrlUtils;

public class PagedRequest<V> extends CampusRequest{
	
	
	private static final String PARAM_PAGE = "page";
	private static final String PARAM_PER_PAGE = "per_page";
	
	/**
	 * First Page
	 */
	public static final int PAGE_FIRST = 1;
	
	/**
	 * Default page size
	 */
	public static final int PAGE_SIZE = 100;
	
	private final int pageSize;
	
	private final int page;
	
	public PagedRequest(){
		this(PAGE_FIRST,PAGE_SIZE);
	}
	
	public PagedRequest(int start,int size){
		page = start;
		pageSize = size;
	}
	
	public int getPageSize(){
		return pageSize;
	}
	
	public int getPage(){
		return page;
	}
	
	
	@Override
	public void addParams(StringBuilder uri) {
		// TODO Auto-generated method stub
		super.addParams(uri);
		final int size = getPageSize();
		if(size > 0)
			UrlUtils.addParam(PARAM_PER_PAGE, Integer.toString(size), uri);
		final int number = getPage();
		if(number > 0)
			UrlUtils.addParam(PARAM_PAGE,Integer.toString(number),uri);
	}
	
	
}
