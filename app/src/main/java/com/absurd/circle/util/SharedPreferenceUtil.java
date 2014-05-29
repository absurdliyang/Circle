package com.absurd.circle.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Position;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SharedPreferenceUtil {

	private static SharedPreferenceUtil mSharedPreferenceUtil;
	
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	
	private SharedPreferenceUtil(Context context,String file) {
		// TODO Auto-generated constructor stub
		mSharedPreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}
	
	public synchronized static SharedPreferenceUtil getInstance(){
		if(mSharedPreferenceUtil == null){
			mSharedPreferenceUtil = new SharedPreferenceUtil(AppContext.getContext(), AppConstant.SETTING_INFOS);
		}
		return mSharedPreferenceUtil;
	}

    public String getAuthToken(){
        return mSharedPreferences.getString("token","");
    }

    public void setAuthTokem(String token){
        mEditor.putString("token",token)
                .commit();
    }

    public void setUserId(String userId){
        mEditor.putString("userId",userId)
                .commit();
    }

    public String getUserId(){
        return mSharedPreferences.getString("userId","");
    }

    public void setLastPosition(Position position){
        mEditor.putString("lastPosition",position.getLatitude() + ":" + position.getLongitude())
                .commit();
    }

    public Position getLastPosition(){
        String strPosition = mSharedPreferences.getString("lastPosition","");
        if(!StringUtil.isEmpty(strPosition)){
            String[] temp = strPosition.split(":");
            Position position = new Position();
            position.setLatitude(Double.valueOf(temp[0]));
            position.setLongitude(Double.valueOf(temp[1]));
            return position;
        }
        return null;
    }

    public void setDefaultKeyboardHeight(int height){
        mEditor.putInt("defaultKeyboardHeight",height)
                .commit();
    }

    public int getDefaultKeyboardHeight(){
        return mSharedPreferences.getInt("defaultKeyboardHeight",400);
    }

    public void setNotificationNum(int num){
        mEditor.putInt("notificationNum", num)
                .commit();
    }

    public int getNotificationNum(){
        return mSharedPreferences.getInt("notificationNum",0);
    }

    public void setUnReadCommentNum(int num){
        mEditor.putInt("unReadCommentNum", num)
                .commit();
    }

    public int getUnReadCommentNum(){
        return mSharedPreferences.getInt("unReadCommentNum",0);
    }

    public int getUnReadPraiseNum(){
        return mSharedPreferences.getInt("unReadPraiseNum",0);
    }

    public void setUnReadPraiseNum(int num){
        mEditor.putInt("unReadPraiseNum", num)
                .commit();
    }

    public void setUnReadUserMessageNum(String userId, int num){
        mEditor.putInt("userMessage " + userId, num)
                .commit();
    }

    public int getUnReadUserMessageNum(String userId){
        return mSharedPreferences.getInt("userMessage " + userId, 0);
    }

    public HashMap<String, Integer> getUnReadUserMessages() {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        Map<String, ?> all = mSharedPreferences.getAll();
        Set<String> key = all.keySet();
        for (Iterator it = key.iterator(); it.hasNext(); ) {
            String s = (String) it.next();
            if (s.contains("userMessage")) {
                result.put(s, (Integer) all.get(s));
            }
        }
        return result;
    }

    public void clearAll(){
        mEditor.clear()
                .commit();
    }
}
