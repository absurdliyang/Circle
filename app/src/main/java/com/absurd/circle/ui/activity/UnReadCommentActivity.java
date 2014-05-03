package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.fragment.UnReadCommentFragment;

/**
 * Created by absurd on 14-3-29.
 */
public class UnReadCommentActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.item_list);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, new UnReadCommentFragment())
                .commit();

    }
    @Override
    protected String actionBarTitle() {
        return "评论我的";
    }


}
