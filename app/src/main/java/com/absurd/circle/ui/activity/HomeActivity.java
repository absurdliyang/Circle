package com.absurd.circle.ui.activity;


import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.AzureClient;
import com.absurd.circle.data.model.Position;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.im.service.ChatService;
import com.absurd.circle.ui.activity.base.INotificationChangedListener;
import com.absurd.circle.ui.activity.base.IProgressBarActivity;
import com.absurd.circle.ui.fragment.CategoryFragment;
import com.absurd.circle.ui.fragment.HomeFragment;
import com.absurd.circle.ui.fragment.SlidingMenuFragment;
import com.absurd.circle.ui.widget.AppMsg;
import com.absurd.circle.util.AnimationUtil;
import com.absurd.circle.util.NetworkUtil;
import com.absurd.circle.util.StringUtil;
import com.absurd.circle.util.SystemUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Calendar;

public class HomeActivity extends SlidingFragmentActivity
        implements IProgressBarActivity, INotificationChangedListener, AMapLocationListener{
    private HomeFragment mContent;

    private SlidingMenuFragment mSlidingMenuFragment;
    private ProgressBar mProgressBar;

    private UserService mUserService = new UserService();

    private boolean mIsExitPressed = false;

    //AMap backgroung
    private MapView mMapView;
    private AMap mAMap;
    private LocationManagerProxy mLocationManagerProxy;
    private AMapLocation mAMapLocation;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext.currentActivity = this;
        // set the Above View
        setContentView(R.layout.activity_home);

        getAuth();


        //Init the NotificationChangedListener in ChatService
        if(ChatService.getInstance() != null) {
            ChatService.getInstance().setNotificationChangedListener(this);
        }

        // set the Above View
        if (savedInstanceState != null)
            mContent = (HomeFragment)getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null)
            mContent = new HomeFragment();

        init();
        //getFollowers();
        // Configur some UI control
        initUI(savedInstanceState);
        if(!NetworkUtil.isNetConnected()){
            warning(R.string.network_disconnected);
            setBusy(false);
        }

        updateUserInfo();
        //initAMap();
        // Get user's current location

        mLocationManagerProxy = LocationManagerProxy.getInstance(HomeActivity.this);
        updateLocation();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAMapLocation == null) {
                    warning(R.string.location_failed);
                    stopLocation();// 销毁掉定位
                }
            }
        }, 12000);


    }

    private void stopLocation() {
        if (mLocationManagerProxy != null) {
            mLocationManagerProxy.removeUpdates(this);
            //mLocationManagerProxy.destory();
        }
        mLocationManagerProxy = null;
    }


    private void updateLocation(){
        if(NetworkUtil.isNetConnected()) {
            setBusy(true);
        }else{
            warning(R.string.network_disconnected);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 防止在界面初始化时阻塞UI线程
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mLocationManagerProxy.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, HomeActivity.this);
            }
        }).start();
    }

    private void initUI(Bundle savedInstanceState){
        configureSlidingMenu();
        configureActionBar();

        mProgressBar = (ProgressBar)findViewById(R.id.pb_action_bar);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mContent)
                .commit();

        // Map background
        //mMapView = (MapView)findViewById(R.id.map_view);
        //mMapView.onCreate(savedInstanceState);
        //new Thread(new InitMapThread()).start();

        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        mSlidingMenuFragment = new SlidingMenuFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, mSlidingMenuFragment)
                .commit();

        // customize the SlidingMenu
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }


    private void init(){
        AppContext.lastPosition = AppContext.sharedPreferenceUtil.getLastPosition();
    }




    private boolean getAuth(){
        AppContext.userId = AppContext.sharedPreferenceUtil.getUserId();
        AppContext.commonLog.i(AppContext.userId);
        if(StringUtil.isEmpty(AppContext.userId))
            return false;
        User u = AppContext.cacheService.userDBManager.getUser(AppContext.userId);
        if(u != null) {
            AppContext.auth = u;
            AppContext.userId = u.getUserId();
            AppContext.token = AppContext.sharedPreferenceUtil.getAuthToken();
            AzureClient.initClient(AppContext.getContext());
            AzureClient.setToken(AppContext.token);
            return true;
        }
        return false;
    }


    private void updateUserInfo(){
        mUserService.getUser(AppContext.auth.getUserId(), new TableQueryCallback<User>() {
            @Override
            public void onCompleted(List<User> result, int count, Exception exception, ServiceFilterResponse response) {
                if(result == null || result.isEmpty()){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                }else{
                    AppContext.auth = result.get(0);
                    AppContext.auth.setAppVer(SystemUtil.getAppVersion());
                    AppContext.auth.setOsName(AppConstant.OS_NAME);
                    AppContext.auth.setLastLoginDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                    if(AppContext.lastPosition != null){
                        AppContext.auth.setLatitude(AppContext.lastPosition.getLatitude());
                        AppContext.auth.setLongitude(AppContext.lastPosition.getLongitude());
                    }
                    AppContext.cacheService.userDBManager.updateUser(result.get(0));
                    mUserService.updateUser(AppContext.auth, new TableOperationCallback<User>() {
                        @Override
                        public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                            if(entity == null){
                                if(exception != null){
                                    exception.printStackTrace();
                                }
                            }else{
                                AppContext.commonLog.i("Update user info success");
                            }
                        }
                    });
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
        Rect rect = new Rect();
        getWindowManager().getDefaultDisplay().getRectSize(rect);
        sm.setBehindWidth((int)(rect.width() * 0.7));
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
        View titleV = actionBarView.findViewById(R.id.llyt_custom_actionbar_title);
        final ImageView arrowIv = (ImageView)actionBarView.findViewById(R.id.iv_actionbar_title);
        CategoryFragment.getInstance().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                FragmentManager fm = getSupportFragmentManager();
                CategoryFragment fragment = CategoryFragment.getInstance();
                Animation rotateBackAnimation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.arrow_rotate_back);
                startAnimation(arrowIv,rotateBackAnimation);

                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                ft.remove(fragment).commit();
                fm.popBackStack();

                if(fragment.hasChanged) {
                    mContent.refreshTranscation();
                    fragment.hasChanged = false;
                }
                return true;
            }
        });
        titleV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getSupportFragmentManager();
                CategoryFragment fragment = CategoryFragment.getInstance();
                if(fm.getBackStackEntryCount() == 0){
                    Animation rotateAnimation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.arrow_rotate);
                    startAnimation(arrowIv, rotateAnimation);

                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                    fragment.setStatus(0);
                    ft.add(R.id.container,fragment)
                            .addToBackStack("categoryFragment")
                            .commit();
                }else{
                    Animation rotateBackAnimation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.arrow_rotate_back);
                    startAnimation(arrowIv,rotateBackAnimation);

                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                    ft.remove(fragment).commit();
                    fm.popBackStack();
                    if(fragment.getStatus() == 0){
                        if(fragment.hasChanged) {
                            mContent.refreshTranscation();
                            fragment.hasChanged = false;
                        }
                    }else{
                        Animation rotateAnimation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.arrow_rotate);
                        startAnimation(arrowIv, rotateAnimation);

                        FragmentTransaction ft1 = fm.beginTransaction();
                        ft1.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                        fragment.setStatus(0);
                        ft1.add(R.id.container,fragment)
                                .addToBackStack("categoryFragment")
                                .commit();
                    }
                }
            }
        });
        LinearLayout editView = (LinearLayout)actionBarView.findViewById(R.id.llyt_actionbar_edit);
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                CategoryFragment fragment = CategoryFragment.getInstance();
                if (fm.getBackStackEntryCount() == 0) {
                    Animation rotateAnimation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.arrow_rotate);
                    startAnimation(arrowIv, rotateAnimation);

                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                    fragment.setStatus(1);
                    ft.add(R.id.container, fragment)
                            .addToBackStack("categoryFragment")
                            .commit();
                } else {
                    Animation rotateBackAnimation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.arrow_rotate_back);
                    startAnimation(arrowIv,rotateBackAnimation);

                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                    ft.remove(fragment).commit();
                    fm.popBackStack();
                    if (fragment.getStatus() == 1) {

                    } else {
                        Animation rotateAnimation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.arrow_rotate);
                        startAnimation(arrowIv, rotateAnimation);

                        FragmentTransaction ft1 = fm.beginTransaction();
                        ft1.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                        fragment.setStatus(1);
                        ft1.add(R.id.container, fragment)
                                .addToBackStack("categoryFragment")
                                .commit();
                    }
                }
            }
        });
        FrameLayout homeView = (FrameLayout)actionBarView.findViewById(R.id.flyt_actionbar_home);
        homeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        actionBar.setCustomView(actionBarView,params);
    }


    private void startAnimation(View view, Animation animation){
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        AccelerateInterpolator interpolator = new AccelerateInterpolator();
        animation.setInterpolator(interpolator);
        view.startAnimation(animation);

    }

    @Override
    public void onBackPressed() {
        if(!mIsExitPressed) {
            warning(R.string.exit_mention);
            mIsExitPressed = true;
        }else{
            this.finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        //mMapView.onSaveInstanceState(outState);
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



    public void setBusy(boolean busy){
        if(busy){
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("BaseScreen");


        // Invalidate the notification on the actionbar
        updateNotificationNum();
        //mMapView.onResume();
    }

    @Override
    public void onNotificationChanged() {
        updateNotificationNum();
    }

    public static final int UPDATE_NOTIFICATIO_NUM = 1;

    private Handler mUpdateNotificationNumHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_NOTIFICATIO_NUM:
                    TextView notificationNumTv = (TextView)findViewById(R.id.tv_tv_ab_notification_num);
                    if(AppContext.notificationNum == 0){
                        notificationNumTv.setVisibility(View.GONE);
                    }else{
                        notificationNumTv.setVisibility(View.VISIBLE);
                        notificationNumTv.setText(AppContext.notificationNum + "");
                    }

                    mSlidingMenuFragment.invalidateView();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void updateNotificationNum(){
        Message message = new Message();
        message.what = UPDATE_NOTIFICATIO_NUM;
        mUpdateNotificationNumHandler.sendMessage(message);
    }

    @Override
    protected void onPause(){
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("BaseScreen");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        AppContext.commonLog.i("Location changed");
        this.mAMapLocation = aMapLocation;
        if(aMapLocation == null){
            AppContext.commonLog.i("Get location failed");
            warning(R.string.location_success);
        }else{
            AppContext.commonLog.i("Get location success!");
            warning(R.string.update_location);
            AppContext.lastPosition = new Position();
            AppContext.lastPosition.setLatitude(aMapLocation.getLatitude());
            AppContext.lastPosition.setLongitude(aMapLocation.getLongitude());
            AppContext.sharedPreferenceUtil.setLastPosition(AppContext.lastPosition);

            // Cancel refresh location
            mLocationManagerProxy.destory();
            mContent.refreshTranscation();
            //deactivate();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
    }

    public void warning(String content){
        AppMsg.makeText(AppContext.currentActivity, content, AppMsg.STYLE_ALERT).show();
    }

    public void warning(int resId){
        String content = AppContext.getContext().getString(resId);
        warning(content);
    }


}
