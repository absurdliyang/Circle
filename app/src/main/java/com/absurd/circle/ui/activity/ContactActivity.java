package com.absurd.circle.ui.activity;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.absurd.circle.app.R;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.fragment.BlackListFragment;
import com.absurd.circle.ui.fragment.FollowersFragment;
import com.absurd.circle.ui.fragment.FunsFragment;
import com.absurd.circle.ui.view.ItemDialog;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends BaseActivity {

    public ContactActivity(){
        setRightBtnStatus(RIGHT_MORE_BTN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container,new FollowersFragment())
                .commit();
    }

    @Override
    protected String actionBarTitle() {
        return "关注";
    }


    @Override
    public void onMoreClicked(View view) {
        super.onMoreClicked(view);
        List<String> items = new ArrayList<String>();
        items.add("关注");
        items.add("粉丝");
        items.add("黑名单");
        final ItemDialog dialog = new ItemDialog(this,items);
        final FragmentManager fm = getSupportFragmentManager();
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        fm.beginTransaction().replace(R.id.container,new FollowersFragment())
                                .commit();
                        break;
                    case 1:
                        fm.beginTransaction().replace(R.id.container,new FunsFragment())
                                .commit();
                        break;
                    case 2:
                        fm.beginTransaction().replace(R.id.container,new BlackListFragment())
                                .commit();
                        break;
                }
                dialog.cancel();
            }
        });
        dialog.show();

    }



}
