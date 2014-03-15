package com.absurd.circle.data.util;


import com.absurd.circle.app.AppConstant;
import com.absurd.circle.data.client.volley.GsonRequest;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.annotations.SerializedName;

/**
 * Created by absurd on 14-3-15.
 */
public class LocationHelper {

    public static BaiduServiceGeoCodeResult getLocationInfo(double lat, double lng){
        String url = String.format(AppConstant.BAIDU_GEOCODER_URL,Double.toString(lat),Double.toString(lng));
        GsonRequest<BaiduServiceGeoCoder> gsonRequest = new GsonRequest<BaiduServiceGeoCoder>(url,BaiduServiceGeoCoder.class,null,
                new Response.Listener<BaiduServiceGeoCoder>() {
                    @Override
                    public void onResponse(BaiduServiceGeoCoder baiduServiceGeoCoder) {
                        CommonLog log = LogFactory.createLog();
                        log.i(baiduServiceGeoCoder.getStatus());
                        log.i(baiduServiceGeoCoder.getResult());

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        RequestManager.addRequest(gsonRequest,"position");
        return null;
    }

    public static class BaiduServiceGeoCoder{
        @SerializedName("status")
        private String mStatus;
        @SerializedName("result")
        private BaiduServiceGeoCodeResult mResult;

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

        public BaiduServiceGeoCodeResult getResult() {
            return mResult;
        }

        public void setResult(BaiduServiceGeoCodeResult result) {
            mResult = result;
        }

        @Override
        public String toString() {
            return "BaiduServiceGeoCoder{" +
                    "mStatus='" + mStatus + '\'' +
                    ", mResult=" + mResult +
                    '}';
        }
    }

    public static class BaiduServiceGeoCodeResult{
        @SerializedName("location")
        private Location mLocation;

        @SerializedName("formatted_address")
        private String mFormattedAddress;

        @SerializedName("business")
        private String mBusiness;

        @SerializedName("addressComponent")
        private AddressComponent mAddressComponent;

        @SerializedName("cityCode")
        private String mCityCode;

        public Location getLocation() {
            return mLocation;
        }

        public void setLocation(Location location) {
            mLocation = location;
        }

        public String getFormattedAddress() {
            return mFormattedAddress;
        }

        public void setFormattedAddress(String formattedAddress) {
            mFormattedAddress = formattedAddress;
        }

        public String getBusiness() {
            return mBusiness;
        }

        public void setBusiness(String business) {
            mBusiness = business;
        }

        public AddressComponent getAddressComponent() {
            return mAddressComponent;
        }

        public void setAddressComponent(AddressComponent addressComponent) {
            mAddressComponent = addressComponent;
        }

        public String getCityCode() {
            return mCityCode;
        }

        public void setCityCode(String cityCode) {
            mCityCode = cityCode;
        }

        @Override
        public String toString() {
            return "BaiduServiceGeoCodeResult{" +
                    "mLocation=" + mLocation +
                    ", mFormattedAddress='" + mFormattedAddress + '\'' +
                    ", mBusiness='" + mBusiness + '\'' +
                    ", mAddressComponent=" + mAddressComponent +
                    ", mCityCode='" + mCityCode + '\'' +
                    '}';
        }
    }

    public static class AddressComponent{
        @SerializedName("city")
        private String mCity;

        @SerializedName("district")
        private String mDistrict;

        @SerializedName("province")
        private String mProvince;

        @SerializedName("street")
        private String mStreet;

        @SerializedName("streetNumber")
        private String mStreetNumber;

        public String getCity() {
            return mCity;
        }

        public void setCity(String city) {
            mCity = city;
        }

        public String getDistrict() {
            return mDistrict;
        }

        public void setDistrict(String district) {
            mDistrict = district;
        }

        public String getProvince() {
            return mProvince;
        }

        public void setProvince(String province) {
            mProvince = province;
        }

        public String getStreet() {
            return mStreet;
        }

        public void setStreet(String street) {
            mStreet = street;
        }

        public String getStreetNumber() {
            return mStreetNumber;
        }

        public void setStreetNumber(String streetNumber) {
            mStreetNumber = streetNumber;
        }

        @Override
        public String toString() {
            return "AddressComponent{" +
                    "mCity='" + mCity + '\'' +
                    ", mDistrict='" + mDistrict + '\'' +
                    ", mProvince='" + mProvince + '\'' +
                    ", mStreet='" + mStreet + '\'' +
                    ", mStreetNumber='" + mStreetNumber + '\'' +
                    '}';
        }
    }

    public static class Location{
        @SerializedName("lat")
        private double mLat;

        @SerializedName("lng")
        private double mLng;

        public double getLat() {
            return mLat;
        }

        public void setLat(double lat) {
            mLat = lat;
        }

        public double getLng() {
            return mLng;
        }

        public void setLng(double lng) {
            mLng = lng;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "mLat=" + mLat +
                    ", mLng=" + mLng +
                    '}';
        }
    }

}
