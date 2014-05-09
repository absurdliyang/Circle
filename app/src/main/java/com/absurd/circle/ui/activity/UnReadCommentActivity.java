package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.fragment.UnReadCommentFragment;

/**
 * Created by absurd on 14-3-29.
 */
public class UnReadCommentActivity extends BaseActivity {

    public UnReadCommentActivity(){
        setRightBtnStatus(RIGHT_TEXT);
    }

    UnReadCommentFragment mUnReadCommentFragment = new UnReadCommentFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.item_list);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, mUnReadCommentFragment)
                .commit();

    }
    @Override
    protected String actionBarTitle() {
        return "评论我的";
    }

    @Override
    protected String setRightBtnTxt() {
        return "清空";

    }

    @Override
    public void onRightBtnClicked(View view) {
        super.onRightBtnClicked(view);
        AppContext.cacheService.commnetDBManager.deleteAll();
        mUnReadCommentFragment.clearList();
    }


}
