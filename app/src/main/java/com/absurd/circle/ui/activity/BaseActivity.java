package com.absurd.circle.ui.activity;



import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.absurd.circle.app.R;

public abstract class BaseActivity extends ActionBarActivity {

    private String mTitle;
    private TextView mActionBarTitleTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureActionBar();
        mTitle = setActionBarTitle();
        mActionBarTitleTv.setText(mTitle);
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



    private void configureActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        View actionBarView = LayoutInflater.from(this).inflate(R.layout.layout_custom_actionbar,null);
        mActionBarTitleTv = (TextView)actionBarView.findViewById(R.id.tv_custom_actionbar_title);
        actionBar.setCustomView(actionBarView,params);
    }

    protected abstract String setActionBarTitle();


}
