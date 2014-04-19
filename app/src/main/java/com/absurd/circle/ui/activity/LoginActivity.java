package com.absurd.circle.ui.activity;

import java.util.List;

import android.content.Intent;
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

    private Button mCurrentBtn;
    private LoginoutButton mSinaLoginBtn;
    private Button mQQLoginBtn;
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

        mIsSharedCb = (CheckBox)findViewById(R.id.cb_is_share);
    }


    private void initConfig(User user){
        AppContext.sharedPreferenceUtil.setAuthTokem(user.getToken());
        AppContext.sharedPreferenceUtil.setUserId(user.getUserId());
        AzureClient.setToken(user.getToken());
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
