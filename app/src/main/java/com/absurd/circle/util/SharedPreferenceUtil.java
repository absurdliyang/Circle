package com.absurd.circle.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Position;

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

    public void clearAll(){
        mEditor.clear()
                .commit();
    }
}
