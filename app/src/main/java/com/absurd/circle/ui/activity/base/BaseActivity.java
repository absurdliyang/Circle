package com.absurd.circle.ui.activity.base;



import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.ui.view.ItemDialog;
import com.absurd.circle.ui.widget.AppMsg;
import com.absurd.circle.util.NetworkUtil;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public abstract class BaseActivity extends SwipeBackActivity implements IProgressBarActivity{

    public static final int RIGHT_TEXT = 1;
    public static final int RIGHT_MORE_BTN = 2;
    public static final int RIGHT_BLANK = 3;

    private String mTitle;
    protected TextView mActionBarTitleTv;
    private TextView mActionBarRightBtn;

    private View mActionBarView;

    private ImageView mActionBarBackIv;
    private ImageView mActionBarMoreIv;
    private ProgressBar mProgressBar;

    protected int mRightBtnStatus = 3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext.currentActivity = this;

        mActionBarView = LayoutInflater.from(this).inflate(R.layout.layout_custom_actionbar,null);
        mActionBarTitleTv = (TextView)mActionBarView.findViewById(R.id.tv_custom_actionbar_title);
        mActionBarRightBtn = (TextView)mActionBarView.findViewById(R.id.tv_custom_actionbar_right_btn);
        mActionBarBackIv = (ImageView)mActionBarView.findViewById(R.id.iv_custom_actionbar_back);
        mActionBarMoreIv = (ImageView)mActionBarView.findViewById(R.id.iv_custom_actionbar_more);
        mProgressBar = (ProgressBar)mActionBarView.findViewById(R.id.pb_custom_actionbar_title);

        configureActionBar();
        mTitle = actionBarTitle();
        mActionBarTitleTv.setText(mTitle);
        mActionBarRightBtn.setText(setRightBtnTxt());
        mActionBarRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRightBtnClicked(view);
            }
        });
        mActionBarBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClicked(view);
            }
        });
        mActionBarMoreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMoreClicked(view);
            }
        });

        setBusy(false);
        if(!NetworkUtil.isNetConnected()){
            warning(R.string.network_disconnected);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    private void configureActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);


        if(mRightBtnStatus == RIGHT_MORE_BTN){
            mActionBarRightBtn.setVisibility(View.GONE);
        }else if(mRightBtnStatus == RIGHT_TEXT){
            mActionBarMoreIv.setVisibility(View.GONE);
        }else if(mRightBtnStatus == RIGHT_BLANK){
            mActionBarMoreIv.setVisibility(View.GONE);
            mActionBarRightBtn.setVisibility(View.GONE);
        }
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mActionBarView,params);
    }


    @Override
    public void setBusy(boolean busy) {
        if(busy){
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // When the activity's life is over, don't forget to cancel the requests
        RequestManager.cancelAll(this);
    }

    protected abstract String actionBarTitle();

    public void setActionBarTitle(String title){
        mActionBarTitleTv.setText(title);
    }

    protected String setRightBtnTxt(){
        return "";
    }


    public void onRightBtnClicked(View view){

    }


    public void onBackClicked(View view){
        this.onBackPressed();
    }

    public void onMoreClicked(View view){
    }

    public void setRightBtnStatus(int status){
        this.mRightBtnStatus = status;
    }

    public void warning(String content){
        
        AppMsg.makeText(AppContext.currentActivity,content,AppMsg.STYLE_ALERT).show();
    }

    public void warning(int resId){
        String content = AppContext.getContext().getString(resId);
        warning(content);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_out, R.anim.push_right_out);
    }
}
