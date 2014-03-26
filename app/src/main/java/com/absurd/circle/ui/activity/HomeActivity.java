package com.absurd.circle.ui.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.ui.fragment.CategoryFragment;
import com.absurd.circle.ui.fragment.MessageListFragment;
import com.absurd.circle.ui.fragment.SlidingMenuFragment;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.LogFactory;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class HomeActivity extends SlidingFragmentActivity implements RefreshableActivity{
    private CommonLog mLog = LogFactory.createLog();
    private PullToRefreshAttacher mAttacher;

    private Fragment mContent;
    /**
     * false MessageListFragment
     * true CategoryFragment
     */
    private boolean mStatus = false;

    private SlidingMenuFragment mSlidingMenuFragment;


    public PullToRefreshAttacher getAttacher(){
        return mAttacher;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAttacher = PullToRefreshAttacher.get(this);
        // set the Above View
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null)
            mContent = new MessageListFragment();

        configureSlidingMenu();
        configureActionBar();
        // set the Above View
        setContentView(R.layout.activity_home);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mContent)
                .commit();

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        mSlidingMenuFragment = new SlidingMenuFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, mSlidingMenuFragment)
                .commit();

        // customize the SlidingMenu
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        getAuth();
    }

    private void getAuth(){
        new UserService(this,AppConstant.TEST_USER_TOKEN).getUser(AppConstant.TEST_USER_ID,new TableQueryCallback<User>(){

            @Override
            public void onCompleted(List<User> result, int count, Exception exception, ServiceFilterResponse response) {
                if(result == null){
                    if(exception != null){
                        exception.printStackTrace();
                        Toast.makeText(HomeActivity.this,"get auth info failed!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    AppContext.auth = result.get(0);
                    AppContext.token = AppContext.auth.getToken();
                    mSlidingMenuFragment.invalidateView();
                }
            }
        });
    }


    private void configureSlidingMenu(){
        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidth(15);
        sm.setBehindOffset(40);
        sm.setFadeDegree(0.35f);
        sm.setBehindWidth(450);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

    private void configureActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        View actionBarView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar,null);
        View titleV = actionBarView.findViewById(R.id.llyt_actionbar_title);
        titleV.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    FragmentManager fm = getSupportFragmentManager();
                if(fm.getBackStackEntryCount() == 0){
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                    ft.add(R.id.container,CategoryFragment.getInstance())
                            .addToBackStack("categoryFragment")
                            .commit();
                }else{
                    fm.popBackStack();
                }
            }
        });
        ImageView editTv = (ImageView)actionBarView.findViewById(R.id.iv_actionbar_edit);
        editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(HomeActivity.this,EditMessageActivity.class);
            }
        });
        ImageView homeTv = (ImageView)actionBarView.findViewById(R.id.iv_actionbar_home);
        homeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        actionBar.setCustomView(actionBarView,params);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
