package com.absurd.circle.ui.activity;

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
import com.absurd.circle.data.model.QQUser;
import com.absurd.circle.data.model.SinaWeiboUser;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.data.util.JsonUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.StringUtil;
import com.absurd.circle.util.SystemUtil;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
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
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class LoginActivity extends ActionBarActivity {

    private Button mCurrentBtn;
    private LoginoutButton mSinaLoginBtn;
    private Button mQQLoginBtn;
    private CheckBox mIsSharedCb;

    private ProgressDialog mLoginProgressDialog;

    private Tencent mTencent;

    private UserService mUserService = new UserService();

    private AuthListener mLoginListener = new AuthListener();



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
                authQQUser();
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
        java.util.Calendar calendar = java.util.Calendar.getInstance();
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

    private void loginQQUser(final QQUser qqUser){
        String userId = "qq:" + mTencent.getOpenId();
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
                        registerQQUser(entity, qqUser);
                    } else {
                        // mTencent.logout(AppContext.getContext());
                        getUserInfo(entity.getUserId());
                    }
                    mTencent.logout(AppContext.getContext());
                }
            }
        });
    }

    private void registerQQUser(final User user,QQUser qqUser){
        user.setLoginType(1);
        if(StringUtil.isEmpty(qqUser.getNickname())){
            user.setName("圈圈用户");
        }else {
            user.setName(qqUser.getNickname());
        }
        if(qqUser.getGender().equals("男")){
            user.setSex("m");
        }else{
            user.setSex("f");
        }
        user.setUserId("qq:" + mTencent.getOpenId());
        user.setAvatar(qqUser.getFigureUrl2());
        user.setOsName(AppConstant.OS_NAME);
        user.setAppVer(SystemUtil.getAppVersion());
        java.util.Calendar calendar = java.util.Calendar.getInstance();
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

    private void authQQUser(){
        if(!mTencent.isSessionValid()){
            mTencent.login(this, AppConstant.QQ_SCOPE, new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    AppContext.commonLog.i("QQ login success");
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResult(requestCode, resultCode, data);
        if(mCurrentBtn != null){
            if(mCurrentBtn instanceof LoginoutButton){
                ((LoginoutButton)mCurrentBtn).onActivityResult(requestCode, resultCode, data);
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

    private void getNewVersionFormUmeng(){
        /** 开始调用自动更新函数 **/
        UmengUpdateAgent.update(this);    //从服务器获取更新信息
        UmengUpdateAgent.setUpdateOnlyWifi(false);    //是否在只在wifi下提示更新，默认为 true
        UmengUpdateAgent.setUpdateAutoPopup(true);    //是否自动弹出更新对话框
        UmengUpdateAgent.setDownloadListener(null);    //下载新版本过程事件监听，可设为 null
        UmengUpdateAgent.setDialogListener(null);    //用户点击更新对话框按钮的回调事件，直接 null
        //从服务器获取更新信息的回调函数
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case 0: // 有更新
                        UmengUpdateAgent.showUpdateDialog(LoginActivity.this, updateInfo);
                        break;
                    case 1: // 无更新
                        break;
                    case 2: // 如果设置为wifi下更新且wifi无法打开时调用
                        break;
                    case 3: // 连接超时
                        break;
                }
            }
        });
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

}
