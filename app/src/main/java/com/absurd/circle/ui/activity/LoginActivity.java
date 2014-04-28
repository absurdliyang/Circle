package com.absurd.circle.ui.activity;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import com.absurd.circle.data.model.QQUser;
import com.absurd.circle.data.model.SinaWeiboUser;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.data.util.JsonUtil;
import com.absurd.circle.util.IntentUtil;
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

public class LoginActivity extends ActionBarActivity {

    private Button mCurrentBtn;
    private LoginoutButton mSinaLoginBtn;
    private Button mQQLoginBtn;
    private CheckBox mIsSharedCb;

    private Tencent mTencent;


    private UserService mUserService = new UserService();

    private AuthListener mLoginListener = new AuthListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mTencent = Tencent.createInstance(AppConstant.QQ_ID, AppContext.getContext());

        AuthInfo authInfo = new AuthInfo(this, AppConstant.SINA_CLIENT_ID, AppConstant.SINA_CALL_BACK_URL, AppConstant.SINA_SCOPE);

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
                }else {
                    User user = result.get(0);
                    AppContext.cacheService.userDBManager.insertUser(user);
                    AppContext.commonLog.i("get User info success ----> " + user.toString());
                    IntentUtil.startActivity(LoginActivity.this, HomeActivity.class);
                    LoginActivity.this.finish();
                }
            }
        });
    }

    private void registerSinaUser(SinaWeiboUser sinaUser){
        User user = new User();
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
                } else {
                    AppContext.commonLog.i("register user success ----> " + entity.toString());
                    AppContext.cacheService.userDBManager.insertUser(entity);
                    IntentUtil.startActivity(LoginActivity.this, HomeActivity.class);
                    LoginActivity.this.finish();
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
                }else{
                    initConfig(entity);
                    if(entity.getId() != 0){
                        registerSinaUser(sinaUser);
                    }else{
                        getUserInfo(entity.getUserId());
                    }
                }
            }
        });
    }

    private void loginQQUser(final QQUser qqUser){
        String userId = "qq:" + mTencent.getOpenId();
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
                }else{
                    initConfig(entity);
                    if(entity.getId() != 0){
                        registerQQUser(qqUser);
                    }else{
                        mTencent.logout(AppContext.getContext());
                        getUserInfo(entity.getUserId());
                    }
                }
            }
        });
    }

    private void registerQQUser(QQUser qqUser){
        User user = new User();
        user.setLoginType(1);
        user.setName(qqUser.getNickname());
        if(qqUser.getGender().equals("男")){
            user.setSex("m");
        }else{
            user.setSex("f");
        }
        user.setUserId("qq:" + mTencent.getOpenId());
        user.setAvatar(qqUser.getFigureUrl2());
        user.setOsName(AppConstant.OS_NAME);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        user.setDate(new java.sql.Date(calendar.getTimeInMillis()));
        user.setAge(new java.sql.Date(calendar.getTimeInMillis()));
        user.setLastLoginDate(new java.sql.Date(calendar.getTimeInMillis()));
        mTencent.logout(AppContext.getContext());


        mUserService.updateUser(user, new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                if (entity == null) {
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                } else {
                    AppContext.commonLog.i("register user success ----> " + entity.toString());
                    AppContext.cacheService.userDBManager.insertUser(entity);
                    IntentUtil.startActivity(LoginActivity.this, HomeActivity.class);
                    LoginActivity.this.finish();
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
                }

                @Override
                public void onCancel() {
                    AppContext.commonLog.i("QQ login is canceled");
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
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        e.printStackTrace();
                    }
                });
                if(mIsSharedCb.isChecked()) {
                    StatusesAPI statusesAPI = new StatusesAPI(accessToken);;
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
        }
        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
        }
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

}
