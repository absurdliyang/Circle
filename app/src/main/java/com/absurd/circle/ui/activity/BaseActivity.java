package com.absurd.circle.ui.activity;



import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.absurd.circle.app.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public abstract class BaseActivity extends SwipeBackActivity {

    private String mTitle;
    private TextView mActionBarTitleTv;
    private TextView mActionBarRightBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        configureActionBar();
        mTitle = setActionBarTitle();
        mActionBarTitleTv.setText(mTitle);
        mActionBarRightBtn.setText(setRightBtnTxt());
        mActionBarRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRightBtnClicked(view);
            }
        });
        return true;
    }

    private void configureActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        View actionBarView = LayoutInflater.from(this).inflate(R.layout.layout_custom_actionbar,null);
        mActionBarTitleTv = (TextView)actionBarView.findViewById(R.id.tv_custom_actionbar_title);
        mActionBarRightBtn = (TextView)actionBarView.findViewById(R.id.tv_custom_actionbar_right_btn);
        actionBar.setCustomView(actionBarView,params);
    }

    protected abstract String setActionBarTitle();

    protected String setRightBtnTxt(){
        return "";
    }

    public void onRightBtnClicked(View view){

    }


}
