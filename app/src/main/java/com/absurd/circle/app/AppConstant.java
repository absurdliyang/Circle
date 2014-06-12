package com.absurd.circle.app;

/**
 * Created by absurd on 14-3-12.
 */
public interface AppConstant {

    public static final String TAG = "Circle";
    public static final String SETTING_INFOS = "circle_setting";
    public static final String OS_NAME = "android";
    public static final String AZURE_MOBILE_TAG = "AzureMobileService";

    /**
     * Default position
     */
    public static final double DEFAULT_LONGITUDE = 114.499431;
    public static final double DEFAULT_LATITUDE = 38.143398;


    /**
     * Azure Setting
     */
    public static final String AZURE_APP_URL = "https://incircle.azure-mobile.net/";
    public static final String AZURE_APP_KEY = "MAkLcGohZvbPjwEVDXZixPyYGlerDW93";
    public static final String ACCOUNT_NAME = "circle";
    public static final String ACCOUNT_KEY = "eMsSCf1ps3iLkpbrF7q4HnC8+M32r8FDRsreW3/U/ULmHxNzV0giXVW9zf1KK2ZUmdvvjThcH3lrc8ZJXd3zRw==";

    /**
     * Badidu authentication infos
     * Campus
     */
    public static final String BAIDU_API_KEY = "X5QlEt746l0sXLmuTqKo5a7u";
    public static final String BAIDU_SECRET_KEY = "Ku0yGkDm4RW5SlHtpSuFtPR44IUBzgia";

    /**
     * BCS Constants
     */
    public static final String BCS_ACCESS_KEY = "3de4772c4c4d00162c355b7f0d803f41";
    public static final String BCS_SECURE_KEY = "A96c98e72bbcfd46e2f94095039af09e";



    /**
     * Sina Oauth
     */
    public static final String SINA_CALL_BACK_URL = "http://www.365high.cn/oauth.html";
    public static final String SINA_CLIENT_ID = "1398557097";
    public static final String SINA_SCOPE =
            "direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    /**
     * QQ Oauth
     */
    public static final String QQ_ID = "100487816";
    //public static final String QQ_SCOPE = "get_user_info,get_simple_userinfo,get_user_profile,get_app_friends,upload_photo,"
    //            + "add_share,add_topic,list_album,upload_pic,add_album,set_user_face,get_vip_info,get_vip_rich_info,get_intimate_friends_weibo,match_nick_tips_weibo";
    public static final String QQ_SCOPE = "all";
    public static final String QQ_LOGIN_URL = "http://openmobile.qq.com/oauth2.0/m_show?which=Login&ucheck=1&fall_to_wv=1&sdkv=1.4&status_userip=fe80%3A%3Aec89%3Af5ff%3Afe15%3A4ee%25p2p0&response_type=token&scope=all&redirect_uri=auth%3A%2F%2Ftauth.qq.com%2F&cancel_display=1&status_machine=Lenovo+P770&switch=1&display=mobile&sdkp=a&client_id=100487816&status_version=16&status_os=4.1.1a";
    public static final String QQ_AUTH_CALLBACK = "login/proxy.htm";
    public static final String QQ_USER_INFO_URL = "https://graph.qq.com/user/get_info?access_token=%s&oauth_consumer_key=%s&openid=%s&format=json";

    public static final String QQ_SHARE_URL = "https://graph.qq.com/share/add_share?oauth_consumer_key=%s&access_token=%s&openid=%s&format=json&title=圈圈&url=http://quanquanshequ.com/&site=http://quanquanshequ.com/&fromurl=http://quanquanshequ.com/&comment=%s&summary=你身边的生活社区&images=http://quanquanshequ.com/imgs/appicon_132px.png";
    public static final String QQ_SHARE_CONTENT = "圈圈是一个很有趣又实用的附近生活社区APP，推荐给大家。下载地址：http://url.cn/N3iPeV @圈圈官博";


    /**
     * Baidu locatio service
     */
    public static final String BAIDU_GEOCODER_URL= "http://api.map.baidu.com/geocoder?output=json&location=%s,%s&key=EGFpCFxPKo3hLG5u0xSnNljbgAGFGynb";

    public static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6MH0.eyJleHAiOjE0MjcyNzIzMTYuMzEyLCJpc3MiOiJ1cm46bWljcm9zb2Z0OndpbmRvd3MtYXp1cmU6enVtbyIsInZlciI6MSwiYXVkIjoiIiwidWlkIjoicXE6RTE5MkNBMTREMTA4Q0U1MDlCMjBEMEI2NjMwQkE4RTQifQ.b797TOr2Cu_V0nCBec4qPSe26MrRWt9UuURABEKMl9Q";
    public static final String TEST_USER_ID = "sina:2350634675";

    /**
     * AMap key
     */
    public static final String AMAP_KEY = "80837d6d2302e9567a379416f49bee46";

    public static final String TAKE_PHOTO_PATH = "/circle/Camera/";


    /**
     * Image proxy url
     */
    public static final String IMAGE_PROXY_URL = "http://proxy.xgres.com/proxyImg.php";



}
