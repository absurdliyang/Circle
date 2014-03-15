package com.absurd.circle.app;

/**
 * Created by absurd on 14-3-12.
 */
public interface AppConstant {
    public static final String TAG = "Circle";

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
     * setting
     * Campus
     */
    public static final String SETTING_INFOS = "campus_setting";
    public static final String TOKEN = "token";
    public static final String USERNAME = "username";

    public static final String AZURE_APP_URL = "https://incircle.azure-mobile.net/";
    public static final String AZURE_APP_KEY = "MAkLcGohZvbPjwEVDXZixPyYGlerDW93";


}
