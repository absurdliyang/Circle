package com.absurd.circle.core.util;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.List;
import java.util.Map;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * json����������
 * @author absurd
 *
 */
public class JsonUtil {
	private static Gson gson = null;
	
	static {
		if(gson == null){
			// able null object
			gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").serializeNulls().create();
		}
	}
	// ˽�й��캯��
	private JsonUtil(){
		
	}
	
	
	/**
	 * jsonStrת��Ϊbean
	 * @param jsonStr
	 * @param type
	 * @return
	 */
	public static <V> V fromJson(String jsonStr,Type type){
		if(jsonStr.startsWith("{")){
			return gson.fromJson(jsonStr, type);
		}
		return null;
	}
	
	/**
	 * object ת��Ϊjson
	 * @return
	 */
	public static String toJson(final Object object){
			return gson.toJson(object);
	}

		
	/**
	 * ��json��ʽת����list����
	 * @param jsonStr
	 * @return
	 */
	public static List<?> jsonToList(String jsonStr){
		List<?> objList = null;
		if(gson != null){
			Type type = new TypeToken<List<?>>(){}.getType();
			objList = gson.fromJson(jsonStr,type);
		}
		return objList;
	}
	
		
}
