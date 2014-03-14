package com.absurd.circle.core.util;

import java.io.UnsupportedEncodingException;



public abstract class EncodeUtils {
	
	private static final String CHARSET_UTF8 = "utf-8";
	
	/**
	 * Decode base64 encoded string
	 * @param content
	 * @return
	 */
	public static final byte[] fromBase64(final String content){
		return Base64.decode(content);
	}
	
	
	/**
	 * Base64 encode given byte array
	 * @param content
	 * @return
	 */
	public static final String toBase64(final byte[] content){
		return Base64.encodeBytes(content);
	}
	/**
	 * Base64 encode given String
	 * @param content
	 * @return
	 */
	public static final String toBase64(final String content){
		byte[] bytes;
		
		try{
			bytes = content.getBytes(CHARSET_UTF8);
		}catch(UnsupportedEncodingException e){
			bytes = content.getBytes();
		}
		return toBase64(bytes);
	}
	
	
}
