package com.absurd.circle.ui.activity;

import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.data.util.GPSUtil;
import com.absurd.circle.util.LogFactory;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.widget.LoginButton;
import com.sina.weibo.sdk.widget.LoginoutButton;

public class LoginActivity extends ActionBarActivity {
    private CommonLog mLog = LogFactory.createLog(AppConstant.TAG);

    /** UI元素列表 */
    private TextView mTokenView;
    private LoginoutButton mLoginoutBtnDefault;

    /** 登陆认证对应的listener */
    private AuthListener mLoginListener = new AuthListener();
    /** 登出操作对应的listener */
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();

    /**
     * 该按钮用于记录当前点击的是哪一个 Button，用于在 {@link #onActivityResult}
     * 函数中进行区分。通常情况下，我们的应用中只需要一个合适的 {@link LoginButton}
     * 或者 {@link LoginoutButton} 即可。
     */
    private Button mCurrentClickedButton;

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTokenView = (TextView) findViewById(R.id.result);

        GPSUtil gpsUtil = GPSUtil.getInstance(AppContext.getContext());
        if(gpsUtil.checkGPSOpened()){
            Toast.makeText(this,"gps open",Toast.LENGTH_SHORT).show();
            Location location = gpsUtil.getLocation();
            if(location != null)
                mLog.i(location.toString());
        }else{
            Toast.makeText(this,"gps on",Toast.LENGTH_SHORT).show();
        }

        // 创建授权认证信息
        AuthInfo authInfo = new AuthInfo(this, AppConstant.SINA_CLIENT_ID, AppConstant.SINA_CALL_BACK_URL, null);

        /**
         * 登录/注销按钮
         */
        // 登录/注销按钮（默认样式：蓝色）
        mLoginoutBtnDefault = (LoginoutButton) findViewById(R.id.lbtn_weibo_login);
        mLoginoutBtnDefault.setWeiboAuthInfo(authInfo, mLoginListener);
        mLoginoutBtnDefault.setLogoutListener(mLogoutListener);

        // 由于 LoginLogouButton 并不保存 Token 信息，因此，如果您想在初次
        // 进入该界面时就想让该按钮显示"注销"，请放开以下代码
        //Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
        //mLoginoutBtnSilver.setLogoutInfo(token, mLogoutListener);


        /**
         * 请注意：为每个 Button 设置一个额外的 Listener 只是为了记录当前点击的
         * 是哪一个 Button，用于在 {@link #onActivityResult} 函数中进行区分。
         * 通常情况下，我们的应用不需要调用该函数。
         */
        mLoginoutBtnDefault.setExternalOnClickListener(mButtonClickListener);


    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mCurrentClickedButton != null) {
            if (mCurrentClickedButton instanceof LoginButton) {
                ((LoginButton)mCurrentClickedButton).onActivityResult(requestCode, resultCode, data);
            } else if (mCurrentClickedButton instanceof LoginoutButton) {
                ((LoginoutButton)mCurrentClickedButton).onActivityResult(requestCode, resultCode, data);
            }
        }

        /*
        if (mLoginBtnDefault != null) {
			mLoginBtnDefault.onActivityResult(requestCode, resultCode, data);
		}
        */

        /*
        if (mLoginBtnStyle2 != null) {
            mLoginBtnStyle2.onActivityResult(requestCode, resultCode, data);
        }
        */

        /*
        if (mLoginBtnStyle3 != null) {
            mLoginBtnStyle3.onActivityResult(requestCode, resultCode, data);
        }
        */

        /*
        if (mLoginoutBtnDefault != null) {
        	mLoginoutBtnDefault.onActivityResult(requestCode, resultCode, data);
		}
		*/

        /*
        if (mLoginoutBtnSilver != null) {
            mLoginoutBtnSilver.onActivityResult(requestCode, resultCode, data);
        }
        */
    }

    /**
     * 请注意：为每个 Button 设置一个额外的 Listener 只是为了记录当前点击的
     * 是哪一个 Button，用于在 {@link #onActivityResult} 函数中进行区分。
     * 通常情况下，我们的应用不需要定义该 Listener。
     */
    private OnClickListener mButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                mCurrentClickedButton = (Button)v;
            }
        }
    };

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                mTokenView.setText(String.format(format, accessToken.getToken(), date));

                //AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
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

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        //AccessTokenKeeper.clear(LoginActivity.this);
                        mTokenView.setText(R.string.weibosdk_demo_logout_success);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            mTokenView.setText(R.string.weibosdk_demo_logout_failed);
        }
    }


}
