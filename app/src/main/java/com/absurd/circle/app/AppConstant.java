package com.absurd.circle.app;

/**
 * Created by absurd on 14-3-12.
 */
public interface AppConstant {
    public static final String TAG = "Circle";
    public static final String AZURE_MOBILE_TAG = "AzureMobileService";

    /**
     * Badidu authentication infos
     * Campus
     */
    public static final String API_KEY = "X5QlEt746l0sXLmuTqKo5a7u";
    public static final String SECRET_KEY = "Ku0yGkDm4RW5SlHtpSuFtPR44IUBzgia";

    /**
     * Sina Oauth
     */
    public static final String SINA_AUTH_API_URL = "https://api.weibo.com/oauth2/authorize?client_id={0}&response_type=code&redirect_uri={1}&state=mobile&scope=follow_app_official_microblog&display=mobile";
    public static final String SINA_CALL_BACK_URL = "http://www.365high.cn/oauth.html";
    public static final String SINA_CLIENT_ID = "1398557097";
    public static final String SINA_WEIBO_SSO_URL = "sinaweibosso://login?redirect_uri={0}&callback_uri=sinaweibosso.4029269314&client_id={1}&scope=follow_app_official_microblog";
    public static final String SINA_CLIENT_SECRET = "d878c36c3489781818575de07ae77f0d";
    public static final String GET_ACCESS_TOKEN_URL = "https://api.weibo.com/oauth2/access_token?client_id={0}&client_secret={1}&grant_type=authorization_code&redirect_uri={2}&code={3}";
    /**
     * Baidu locatio service
     */
    public static final String BAIDU_GEOCODER_URL= "http://api.map.baidu.com/geocoder?output=json&location=%s,%s&key=EGFpCFxPKo3hLG5u0xSnNljbgAGFGynb";

    public static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6MH0.eyJleHAiOjE0MjcyNzIzMTYuMzEyLCJpc3MiOiJ1cm46bWljcm9zb2Z0OndpbmRvd3MtYXp1cmU6enVtbyIsInZlciI6MSwiYXVkIjoiIiwidWlkIjoicXE6RTE5MkNBMTREMTA4Q0U1MDlCMjBEMEI2NjMwQkE4RTQifQ.b797TOr2Cu_V0nCBec4qPSe26MrRWt9UuURABEKMl9Q";
    public static final String TEST_USER_ID = "qq:7F234A6D96FB2B9A45F2AD02B196AC37";

    /**
     * AMap key
     */
    public static final String AMAP_KEY = "80837d6d2302e9567a379416f49bee46";

    /**
     * setting
     * Campus
     */
    public static final String SETTING_INFOS = "campus_setting";
    public static final String TOKEN = "token";
    public static final String USERNAME = "username";

    public static final String AZURE_APP_URL = "https://incircle.azure-mobile.net/";
    public static final String AZURE_APP_KEY = "MAkLcGohZvbPjwEVDXZixPyYGlerDW93";


}
