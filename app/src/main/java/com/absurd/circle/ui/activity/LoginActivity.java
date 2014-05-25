package com.absurd.circle.ui.activity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.AzureClient;
import com.absurd.circle.data.client.volley.GsonRequest;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.SinaWeiboUser;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.data.util.JsonUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.StringUtil;
import com.absurd.circle.util.SystemUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.annotations.Expose;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.widget.LoginoutButton;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.Calendar;

public class LoginActivity extends ActionBarActivity {

    public static final int QQ_LOGIN_REQUEST_CODE = 124;

    private Button mCurrentBtn;
    private LoginoutButton mSinaLoginBtn;
    private Button mQQLoginBtn;
    private CheckBox mIsSharedCb;

    private ProgressDialog mLoginProgressDialog;

    private Tencent mTencent;

    private UserService mUserService = new UserService();

    private AuthListener mLoginListener = new AuthListener();

    private QQWeiboUser mQQUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.umeng.common.Log.LOG = true;
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
        //getNewVersionFormUmeng();


        UmengUpdateAgent.update(this);    //从服务器获取更新信息

        AppContext.userId = AppContext.sharedPreferenceUtil.getUserId();
        AppContext.commonLog.i(AppContext.userId);
        if(!StringUtil.isEmpty(AppContext.userId)){
            IntentUtil.startActivity(this,HomeActivity.class);
            this.finish();
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        initUI();

    }

    private void initUI(){
        mTencent = Tencent.createInstance(AppConstant.QQ_ID, AppContext.getContext());

        AuthInfo authInfo = new AuthInfo(this, AppConstant.SINA_CLIENT_ID, AppConstant.SINA_CALL_BACK_URL, AppConstant.SINA_SCOPE);

        mLoginProgressDialog = new ProgressDialog(this);
        mLoginProgressDialog.setMessage("正在登陆......");
        mLoginProgressDialog.setCancelable(false);
        mLoginProgressDialog.setCanceledOnTouchOutside(false);

        mSinaLoginBtn = (LoginoutButton) findViewById(R.id.lbtn_weibo_login);
        mSinaLoginBtn.setWeiboAuthInfo(authInfo, mLoginListener);
        mSinaLoginBtn.setExternalOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof Button) {
                    mCurrentBtn = (Button) view;
                }
            }
        });
        mQQLoginBtn = (Button)findViewById(R.id.btn_qq_login);
        mQQLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginProgressDialog.show();
                //authQQUser();
                //IntentUtil.startActivity(LoginActivity.this, QQLoginWebViewActivity.class);
                Intent intent = new Intent(LoginActivity.this, QQLoginWebViewActivity.class);
                startActivityForResult(intent, QQ_LOGIN_REQUEST_CODE);

            }
        });

        mIsSharedCb = (CheckBox)findViewById(R.id.cb_is_share);
        mIsSharedCb.setChecked(true);

    }


    private void initConfig(User user){
        AppContext.sharedPreferenceUtil.setAuthTokem(user.getToken());
        AppContext.sharedPreferenceUtil.setUserId(user.getUserId());
        AzureClient.setToken(user.getToken());
    }

    private void getUserInfo(String userId){
        mUserService.getUser(userId, new TableQueryCallback<User>() {
            @Override
            public void onCompleted(List<User> result, int count, Exception exception, ServiceFilterResponse response) {
                if(result == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                    mLoginProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this ,R.string.login_failed, Toast.LENGTH_SHORT).show();
                }else {
                    AppContext.commonLog.i(result.get(0).toString());
                    User user = result.get(0);
                    user.setOsName(AppConstant.OS_NAME);
                    user.setAppVer(SystemUtil.getAppVersion());
                    //updateUserInfo(user);
                    AppContext.cacheService.userDBManager.insertUser(user);
                    AppContext.commonLog.i("get User info success ----> " + user.toString());
                    getFollowers(user.getUserId());
                    mLoginProgressDialog.dismiss();
                    LoginActivity.this.finish();
                    IntentUtil.startActivity(LoginActivity.this, HomeActivity.class);
                }
            }
        });
    }

    private void updateUserInfo(User user){
        mUserService.updateUser(user, new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                if(entity == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                }else {
                    AppContext.commonLog.i("Update user info success");
                }
            }
        });
    }

    private void registerSinaUser(final User user, SinaWeiboUser sinaUser){
        user.setLoginType(1);
        user.setName(sinaUser.getName());
        user.setSex(sinaUser.getGender());
        user.setUserId("sina:" + sinaUser.getId());
        user.setDescription(sinaUser.getDescription());
        user.setLocation(sinaUser.getLocation());
        user.setAvatar(sinaUser.getAvatarLarge());
        user.setOsName(AppConstant.OS_NAME);
        Calendar calendar = Calendar.getInstance();
        user.setDate(new java.sql.Date(calendar.getTimeInMillis()));
        user.setAge(new java.sql.Date(calendar.getTimeInMillis()));
        user.setLastLoginDate(new java.sql.Date(calendar.getTimeInMillis()));

        mUserService.updateUser(user, new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                if (entity == null) {
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                    mLoginProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this ,R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    AppContext.commonLog.i("register user success ----> " + entity.toString());
                    AppContext.cacheService.userDBManager.insertUser(entity);
                    IntentUtil.startActivity(LoginActivity.this, HomeActivity.class);
                    getFollowers(user.getUserId());
                    mLoginProgressDialog.dismiss();
                    LoginActivity.this.finish();
                    IntentUtil.startActivity(LoginActivity.this, HomeActivity.class);
                }
            }
        });
    }



    private void loginSinaUser(final SinaWeiboUser sinaUser){
        String userId = "sina:" + sinaUser.getId();
        User user = new User();
        user.setUserId(userId);
        user.setLoginType(1);
        mUserService.insertUser(user, new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                if(entity == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                    mLoginProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this ,R.string.login_failed, Toast.LENGTH_SHORT).show();
                }else{
                    initConfig(entity);
                    if(entity.getId() != 0){
                        registerSinaUser(entity, sinaUser);
                    }else{
                        getUserInfo(entity.getUserId());
                    }
                    mSinaLoginBtn.logout();
                }
            }
        });
    }

    private void loginQQUser(){
        if(mQQUser == null || mQQUser.getData() == null){
            Toast.makeText(AppContext.getContext(), R.string.qq_auth_failed, Toast.LENGTH_SHORT).show();
            mLoginProgressDialog.dismiss();
            return;
        }
        String userId = "qq:" + mQQUser.getData().getOpenid();
        final User user = new User();
        user.setUserId(userId);
        user.setLoginType(1);
        mUserService.insertUser(user, new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                if (entity == null) {
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                    mLoginProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    initConfig(entity);
                    if (entity.getId() != 0) {
                        registerQQUser(entity);
                    } else {
                        // mTencent.logout(AppContext.getContext());
                        getUserInfo(entity.getUserId());
                    }
                    mTencent.logout(AppContext.getContext());
                }
            }
        });
    }

    private void registerQQUser(final User user){
        user.setLoginType(1);
        if(StringUtil.isEmpty(mQQUser.getData().getNick())){
            user.setName("圈圈用户");
        }else {
            user.setName(mQQUser.getData().getNick());
        }
        if(mQQUser.getData().getSex() == 1){
            user.setSex("m");
        }else{
            user.setSex("f");
        }
        user.setUserId("qq:" + mQQUser.getData().getOpenid());
        user.setAvatar(mQQUser.getData().getHead() + "/100");
        user.setOsName(AppConstant.OS_NAME);
        user.setAppVer(SystemUtil.getAppVersion());
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        user.setDate(new java.sql.Date(calendar.getTimeInMillis()));
        if(mQQUser.getData().getBirth_year() > 0 && mQQUser.getData().getBirth_month() > 0 && mQQUser.getData().getBirth_day() > 0) {
            Calendar ageCalendar = Calendar.getInstance();
            ageCalendar.set(mQQUser.getData().getBirth_year(), mQQUser.getData().getBirth_month(), mQQUser.getData().getBirth_day());
            user.setAge(new java.sql.Date(calendar.getTimeInMillis()));
        }
        user.setLastLoginDate(new java.sql.Date(calendar.getTimeInMillis()));

        mUserService.updateUser(user, new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                if (entity == null) {
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                    mLoginProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    AppContext.commonLog.i("register user success ----> " + entity.toString());
                    AppContext.cacheService.userDBManager.insertUser(entity);
                    IntentUtil.startActivity(LoginActivity.this, HomeActivity.class);
                    getFollowers(user.getUserId());
                    mLoginProgressDialog.dismiss();
                    LoginActivity.this.finish();
                    IntentUtil.startActivity(LoginActivity.this, HomeActivity.class);
                }
            }
        });
    }

    private void shareToQQ(){
        String url = String.format(String.format(AppConstant.QQ_SHARE_URL,mTencent.getAppId(),mTencent.getAccessToken(),
                mTencent.getOpenId(), AppConstant.QQ_SHARE_CONTENT));
        AppContext.commonLog.i(url);
        GsonRequest<SharedQQ> gsonRequest = new GsonRequest<SharedQQ>(url, SharedQQ.class, null, new Response.Listener<SharedQQ>() {
            @Override
            public void onResponse(SharedQQ sharedQQ) {
                AppContext.commonLog.i("QQ shared success");
                AppContext.commonLog.i(sharedQQ.toString());
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.commonLog.i(volleyError.toString());
            }
        });
        RequestManager.addRequest(gsonRequest, "shareToQQ");
    }

    /**
    private void authQQUser(){
        if(!mTencent.isSessionValid()){
            mTencent.login(this, AppConstant.QQ_SCOPE, new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    AppContext.commonLog.i("QQ login success");
                    AppContext.commonLog.i("token --> " + mTencent.getQQToken());
                    final UserInfo userInfo = new UserInfo(LoginActivity.this, mTencent.getQQToken());
                    userInfo.getUserInfo(new IUiListener() {
                        @Override
                        public void onComplete(Object o) {
                            QQUser qqUser =  JsonUtil.fromJson(o.toString(), QQUser.class);
                            loginQQUser(qqUser);
                            if(mIsSharedCb.isChecked())
                                shareToQQ();
                        }

                        @Override
                        public void onError(UiError uiError) {
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
                }

                @Override
                public void onError(UiError uiError) {
                    AppContext.commonLog.i("QQ login error");
                    AppContext.commonLog.i(uiError.errorMessage);
                    mLoginProgressDialog.dismiss();
                }

                @Override
                public void onCancel() {
                    AppContext.commonLog.i("QQ login is canceled");
                    mLoginProgressDialog.dismiss();

                }
            });
        }

    }
     **/

    private void getQQUserInfo(String openId, String accessToken){
        String url = String.format(AppConstant.QQ_USER_INFO_URL, accessToken,AppConstant.QQ_ID, openId );

        GsonRequest<QQWeiboUser> request = new GsonRequest<QQWeiboUser>(url,QQWeiboUser.class,null,
                new Response.Listener<QQWeiboUser>() {
                    @Override
                    public void onResponse(QQWeiboUser qqWeiboUser) {
                        AppContext.commonLog.i(qqWeiboUser.toString());
                        mQQUser = qqWeiboUser;
                        loginQQUser();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
        RequestManager.addRequest(request, "getQQUserInfo");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResult(requestCode, resultCode, data);
        if(mCurrentBtn != null){
            if(mCurrentBtn instanceof LoginoutButton){
                ((LoginoutButton)mCurrentBtn).onActivityResult(requestCode, resultCode, data);
            }
        }
        if(requestCode == QQ_LOGIN_REQUEST_CODE){
            if(data != null) {
                String openId = data.getStringExtra("openId");
                String accessToken = data.getStringExtra("accessToken");
                if (StringUtil.isEmpty(openId) || StringUtil.isEmpty(accessToken)) {
                    Toast.makeText(AppContext.getContext(), R.string.qq_auth_failed, Toast.LENGTH_SHORT).show();
                    mLoginProgressDialog.dismiss();
                } else {
                    AppContext.commonLog.i("getExtrad --> " + openId + " " + accessToken);
                    getQQUserInfo(openId, accessToken);
                }
            }else{
                Toast.makeText(AppContext.getContext(), R.string.qq_auth_failed, Toast.LENGTH_SHORT).show();
                mLoginProgressDialog.dismiss();
            }
        }
    }

    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            mLoginProgressDialog.show();
            final Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                AppContext.commonLog.i(accessToken);
                UsersAPI userAPI = new UsersAPI(accessToken);
                userAPI.show(Long.parseLong(accessToken.getUid()),new RequestListener() {
                    @Override
                    public void onComplete(String s) {
                        SinaWeiboUser sinaUser = JsonUtil.fromJson(s, SinaWeiboUser.class);
                        AppContext.commonLog.i(sinaUser);
                        loginSinaUser(sinaUser);
                        if(mIsSharedCb.isChecked()) {
                            StatusesAPI statusesAPI = new StatusesAPI(accessToken);
                            FriendshipsAPI friendshipsAPI = new FriendshipsAPI(accessToken);
                            statusesAPI.upload(getResources().getString(R.string.share_text), ((BitmapDrawable)AppContext.getContext().getResources().getDrawable(R.drawable.login_page_bg)).getBitmap(), null, null, new RequestListener() {
                                @Override
                                public void onComplete(String s) {
                                    AppContext.commonLog.i("send weibo success ----> " + s);
                                }

                                @Override
                                public void onWeiboException(WeiboException e) {
                                    e.printStackTrace();
                                }
                            });

                            friendshipsAPI.create(3477997655L, "圈圈官博", new RequestListener() {
                                @Override
                                public void onComplete(String s) {
                                    AppContext.commonLog.i("crate a new friendship success! ----> " + s);
                                }

                                @Override
                                public void onWeiboException(WeiboException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        e.printStackTrace();
                    }
                });

            }
        }
        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            mLoginProgressDialog.dismiss();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
            mLoginProgressDialog.dismiss();

        }
    }


    // It should be called when the uer firstly login
    private void getFollowers(String userId){
        AppContext.cacheService.followDBManager.deleteAll();
        if(AppContext.auth != null) {
            mUserService.getAllUserFollowers(userId, new TableQueryCallback<Follow>() {
                @Override
                public void onCompleted(List<Follow> result, int count, Exception exception, ServiceFilterResponse response) {
                    if(result == null){
                        if(exception != null){
                            exception.printStackTrace();
                        }
                    }else{
                        for(Follow follow : result ){
                            AppContext.cacheService.followDBManager.insertFollow(follow);
                        }
                    }
                }
            });
        }
    }


    /**
     * Fix the fucking bug when menu key down
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_MENU){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class SharedQQ{
        @Expose
        private int ret;
        @Expose
        private String msg;

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "SharedQQ{" +
                    "ret=" + ret +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("BaseScreen");
    }

    @Override
    protected void onPause(){
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("BaseScreen");
    }






    public static class QQWeiboUser implements Serializable{

        @Expose
        public UserInfo data;

        public UserInfo getData() {
            return data;
        }

        public void setData(UserInfo data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "QQWeiboUser{" +
                    "data=" + data +
                    '}';
        }
    }

    public static class UserInfo{
        @Expose
        public String name ;
        @Expose
        public String openid ;
        @Expose
        public String nick ;
        @Expose
        public String head ;
        @Expose
        public String location ;
        @Expose
        public int isvip ;
        @Expose
        public int isent ;
        @Expose
        public String introduction ;
        @Expose
        public int birth_year ;
        @Expose
        public int birth_month ;
        @Expose
        public int birth_day ;
        @Expose
        public String country_code ;
        @Expose
        public String province_code ;
        @Expose
        public String city_code ;
        @Expose
        public int sex ;
        @Expose
        public int fansnum;
        @Expose
        public int idolnum ;
        @Expose
        public int tweetnum  ;
        @Expose
        public String email ;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getTweetnum() {
            return tweetnum;
        }

        public void setTweetnum(int tweetnum) {
            this.tweetnum = tweetnum;
        }

        public int getIdolnum() {
            return idolnum;
        }

        public void setIdolnum(int idolnum) {
            this.idolnum = idolnum;
        }

        public int getFansnum() {
            return fansnum;
        }

        public void setFansnum(int fansnum) {
            this.fansnum = fansnum;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getCity_code() {
            return city_code;
        }

        public void setCity_code(String city_code) {
            this.city_code = city_code;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getProvince_code() {
            return province_code;
        }

        public void setProvince_code(String province_code) {
            this.province_code = province_code;
        }

        public int getBirth_day() {
            return birth_day;
        }

        public void setBirth_day(int birth_day) {
            this.birth_day = birth_day;
        }

        public int getBirth_year() {
            return birth_year;
        }

        public void setBirth_year(int birth_year) {
            this.birth_year = birth_year;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getBirth_month() {
            return birth_month;
        }

        public void setBirth_month(int birth_month) {
            this.birth_month = birth_month;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getIsvip() {
            return isvip;
        }

        public void setIsvip(int isvip) {
            this.isvip = isvip;
        }

        public int getIsent() {
            return isent;
        }

        public void setIsent(int isent) {
            this.isent = isent;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "name='" + name + '\'' +
                    ", openid='" + openid + '\'' +
                    ", nick='" + nick + '\'' +
                    ", head='" + head + '\'' +
                    ", location='" + location + '\'' +
                    ", isvip=" + isvip +
                    ", isent=" + isent +
                    ", introduction='" + introduction + '\'' +
                    ", birth_year=" + birth_year +
                    ", birth_month=" + birth_month +
                    ", birth_day=" + birth_day +
                    ", country_code='" + country_code + '\'' +
                    ", province_code='" + province_code + '\'' +
                    ", city_code='" + city_code + '\'' +
                    ", sex=" + sex +
                    ", fansnum=" + fansnum +
                    ", idolnum=" + idolnum +
                    ", tweetnum=" + tweetnum +
                    ", email='" + email + '\'' +
                    '}';
        }
    }



}
