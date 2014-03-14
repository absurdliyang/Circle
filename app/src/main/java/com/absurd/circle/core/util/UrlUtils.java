package com.absurd.circle.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

public class UrlUtils {
	
	private static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
	/**
	 * add param
	 * @param name
	 * @param value
	 * @param uri
	 */
	public static void addParam(final String name,final String value,
			final StringBuilder uri){
		if(uri.length() > 0)
			uri.append("&");
		uri.append(encode(name)).append('=');
		if(value != null)
			uri.append(encode(value));
	}
	
	
	/**
	 * add params
	 * @param params
	 * @param uri
	 */
	public static void addParams(final Map<String,String> params,
			final StringBuilder uri){
		if(params == null || params.isEmpty())
			return;
		for(Entry<String, String> param : params.entrySet())
			addParam(param.getKey(),param.getValue(),uri);
	}
	
	/**
	 * URL-encode value using 'ISO-8859-1' character set
	 *
	 * @param value
	 * @return encoded value
	 */
	public static String encode(final String value) {
		try {
			return URLEncoder.encode(value, "UTF_8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	
}
