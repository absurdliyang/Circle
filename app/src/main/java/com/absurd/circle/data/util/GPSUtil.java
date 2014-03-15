package com.absurd.circle.data.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;

/**
 * Created by absurd on 14-3-15.
 */
public class GPSUtil {
    private CommonLog mLog = LogFactory.createLog(AppConstant.TAG);
    private Context mContext;

    private LocationManager mLocationManager;

    private Location mLocation = null;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location != null){
                mLocation = location;
            }
            mLocationManager.removeUpdates(mLocationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private static GPSUtil mGPSUtil;

    public static GPSUtil getInstance(Context context){
        if(mGPSUtil == null)
            mGPSUtil = new GPSUtil(context);
        return mGPSUtil;
    }

    private GPSUtil(Context context){
        this.mContext = context;
        mLocationManager = (LocationManager)mContext.getSystemService(mContext.LOCATION_SERVICE);
    }

    public boolean checkGPSOpened(){
        if (mLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            mLog.i("gps opened");
            return true;
        }
        mLog.i("gps closed");
        return false;
    }

    public void updateLocation()
    {
        // 查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
        String provider = mLocationManager.getBestProvider(criteria, true); // 获取GPS信息
        mLocation = mLocationManager.getLastKnownLocation(provider); // 通过GPS获取位置
        // 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
        mLocationManager.requestLocationUpdates(provider, 100 * 1000, 500,mLocationListener);
    }



    public Location getLocation(){
        if(checkGPSOpened()) {
            updateLocation();
            return mLocation;
        }else{
            return null;
        }
    }

}
