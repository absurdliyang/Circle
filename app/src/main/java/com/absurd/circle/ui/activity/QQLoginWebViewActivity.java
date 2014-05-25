package com.absurd.circle.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.GsonRequest;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.util.StringUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.annotations.Expose;

import java.io.Serializable;


public class QQLoginWebViewActivity extends BaseActivity {

    private WebView mWebView;

    private String mOpenId;
    private String mAccessToken;

    public QQLoginWebViewActivity(){
        setRightBtnStatus(RIGHT_TEXT);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = (WebView)findViewById(R.id.wv);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                AppContext.commonLog.i("onPageStarted");
                AppContext.commonLog.i(url);
                if(url.contains("auth://tauth.qq.com/?")){
                    Intent intent = new Intent();
                    intent.putExtra("openId", mOpenId);
                    intent.putExtra("accessToken", mAccessToken);
                    setResult(123, intent);
                    finish();
                }
                if(url.contains(AppConstant.QQ_AUTH_CALLBACK)){
                    getAccessToken(url);
                }
            }

        });

        mWebView.loadUrl(AppConstant.QQ_LOGIN_URL);

    }

    private void getAccessToken(String url){
        mOpenId = getQueryString(url,"openid");
        mAccessToken = getQueryString(url, "access_token");
        AppContext.commonLog.i("openid --> " + mAccessToken + " access_token --> " + mOpenId);
        //getUserInfo(openId,accessToken);
    }




    private String getQueryString(String url, String key){
        String retVal = "";
        String query = "";
        String abUrl = url;
        abUrl = url;
        query = abUrl.substring(abUrl.indexOf("?") + 1);
        query = query.replace("?", "");
        String[] querys = query.split("&");
        for(String str : querys){
            String[] vals = str.split("=");
            if(vals[0].toLowerCase().equals(key.toLowerCase())){
                retVal = vals[1];
                break;
            }
        }
        return retVal;
    }



    @Override
    protected String actionBarTitle() {
        return "QQ登录";
    }

    @Override
    protected String setRightBtnTxt() {
        return "取消";
    }


    @Override
    public void onRightBtnClicked(View view) {
        super.onRightBtnClicked(view);
        this.onBackPressed();
    }

}
