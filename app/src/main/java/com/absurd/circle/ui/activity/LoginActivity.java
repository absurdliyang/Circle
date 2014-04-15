package com.absurd.circle.ui.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.SinaWeiboUser;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.data.util.JsonUtil;
import com.absurd.circle.util.IntentUtil;
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

public class LoginActivity extends ActionBarActivity {

    private LoginoutButton mLoginBtn;
    private CheckBox mIsSharedCb;

    private UserService mUserService = new UserService();

    private AuthListener mLoginListener = new AuthListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        AuthInfo authInfo = new AuthInfo(this, AppConstant.SINA_CLIENT_ID, AppConstant.SINA_CALL_BACK_URL, AppConstant.SINA_SCOPE);

        mLoginBtn = (LoginoutButton) findViewById(R.id.lbtn_weibo_login);
        mLoginBtn.setWeiboAuthInfo(authInfo, mLoginListener);
        mIsSharedCb = (CheckBox)findViewById(R.id.cb_is_share);
    }


    private void initConfig(User user){
        AppContext.sharedPreferenceUtil.setAuthTokem(user.getToken());
        AppContext.sharedPreferenceUtil.setUserId(user.getUserId());
    }


    private void login(final SinaWeiboUser sinaUser){
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
                    AppContext.commonLog.i("login success ---->" + entity.toString());
                    if(entity.getId() != 0){
                        registerSinaUser(sinaUser);
                    }else{
                        initConfig(entity);
                        IntentUtil.startActivity(LoginActivity.this, HomeActivity.class);
                    }
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

        mUserService.insertUser(user, new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                if (entity == null) {
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                } else {
                    AppContext.commonLog.i(entity.toString());
                    AppContext.commonLog.i("register success");
                    initConfig(entity);
                    IntentUtil.startActivity(LoginActivity.this, HomeActivity.class);
                }
            }
        });

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
                        AppContext.commonLog.i("get user info success ----> " + s);
                        SinaWeiboUser sinaUser = JsonUtil.fromJson(s, SinaWeiboUser.class);
                        AppContext.commonLog.i(sinaUser);
                        String userId = "sina:" + accessToken.getUid();
                        login(sinaUser);
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        e.printStackTrace();
                    }
                });
                if(mIsSharedCb.isChecked()) {
                    StatusesAPI statusesAPI = new StatusesAPI(accessToken);;
                    FriendshipsAPI friendshipsAPI = new FriendshipsAPI(accessToken);
                    statusesAPI.update(getResources().getString(R.string.share_text), null, null, new RequestListener() {
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
}
