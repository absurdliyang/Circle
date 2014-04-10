package com.absurd.circle.ui.activity;


import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
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
import com.absurd.circle.data.client.AzureClient;
import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.Position;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.ui.fragment.CategoryFragment;
import com.absurd.circle.ui.fragment.HomeFragment;
import com.absurd.circle.ui.fragment.SlidingMenuFragment;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.absurd.circle.util.StringUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class HomeActivity extends SlidingFragmentActivity implements RefreshableActivity
                                ,AMapLocationListener, LocationSource {
    private CommonLog mLog = LogFactory.createLog();
    private PullToRefreshAttacher mAttacher;
    private HomeFragment mContent;
    /**
     * false MessageListFragment
     * true CategoryFragment
     */
    private boolean mStatus = false;
    private SlidingMenuFragment mSlidingMenuFragment;

    private UserService mUserService;


    //AMap backgroung
    private MapView mMapView;
    private AMap mAMap;
    private OnLocationChangedListener mOnLocationChangedListener;
    private LocationManagerProxy mLocationManagerProxy;



    public PullToRefreshAttacher getAttacher(){
        return mAttacher;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAttacher = PullToRefreshAttacher.get(this);
        // set the Above View
        if (savedInstanceState != null)
            mContent = (HomeFragment)getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null)
            mContent = new HomeFragment();

        // Init Data component
        mUserService = new UserService();
        init();
        getAuth();
        getFollowers();
        // Configur some UI control
        configureSlidingMenu();
        configureActionBar();
        // set the Above View
        setContentView(R.layout.activity_home);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mContent)
                .commit();

        // Map background
        mMapView = (MapView)findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        initAMap();

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



    private void initAMap(){
        if(mAMap == null) {
            mAMap = mMapView.getMap();
        }
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        mAMap.setLocationSource(this);
        mAMap.setMyLocationEnabled(true);
        // Setting zoom
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }


    private void init(){
        AppContext.lastPosition = AppContext.sharedPreferenceUtil.getLastPosition();
        if(AppContext.lastPosition == null){
            ///////
        }else{
            ///////
        }
    }
    // It shoeld be called when the uer firstly login
    private void getFollowers(){
        AppContext.cacheService.deleteAllFollow();
        if(AppContext.auth != null) {
            mUserService.getUserFollowers(AppContext.auth.getUserId(), new TableQueryCallback<Follow>() {
                @Override
                public void onCompleted(List<Follow> result, int count, Exception exception, ServiceFilterResponse response) {
                    if(result == null){
                        if(exception != null){
                            exception.printStackTrace();
                        }
                    }else{
                        for(Follow follow : result ){
                            AppContext.cacheService.insertFollow(follow);
                        }
                    }
                }
            });
        }
    }

    private void getAuth(){
        User u = AppContext.cacheService.getUser();
        if(u != null) {
            AppContext.auth = u;
            AppContext.userId = u.getUserId();
            AppContext.token = AppContext.sharedPreferenceUtil.getAuthToken();
            AzureClient.setToken(AppContext.token);
            //mSlidingMenuFragment.invalidateView();
            return;
        }else {
            AppContext.token = AppContext.sharedPreferenceUtil.getAuthToken();
            AppContext.userId = AppContext.sharedPreferenceUtil.getUserId();
            if (!StringUtil.isEmpty(AppContext.token) && !StringUtil.isEmpty(AppContext.userId)) {
                AzureClient.setToken(AppContext.token);
                mUserService.getUser(AppContext.userId, new TableQueryCallback<User>() {
                    @Override
                    public void onCompleted(List<User> result, int count, Exception exception, ServiceFilterResponse response) {
                        if (result == null) {
                            if (exception != null) {
                                exception.printStackTrace();
                                Toast.makeText(HomeActivity.this, "get auth info failed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AppContext.auth = result.get(0);
                            AppContext.cacheService.deleteUser();
                            AppContext.cacheService.inserUser(AppContext.auth);
                            mSlidingMenuFragment.invalidateView();
                        }
                    }
                });
            } else {
                User user = new User();
                user.setLoginType(1);
                user.setUserId(AppConstant.TEST_USER_ID);
                mUserService.insertUser(user, new TableOperationCallback<User>() {
                    @Override
                    public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                        if (entity == null) {
                            if (exception != null) {
                                exception.printStackTrace();
                                Toast.makeText(HomeActivity.this, "get auth info failed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            AppContext.auth = entity;
                            AppContext.userId = entity.getUserId();
                            AppContext.token = entity.getToken();
                            AppContext.sharedPreferenceUtil.setAuthTokem(entity.getToken());
                            AppContext.sharedPreferenceUtil.setUserId(entity.getUserId());
                            mContent.refreshTranscation();
                            getAuth();
                        }
                    }
                });
            }
        }
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
        View titleV = actionBarView.findViewById(R.id.llyt_actionbar_title);
        titleV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                CategoryFragment fragment = CategoryFragment.getInstance();
                if(fm.getBackStackEntryCount() == 0){
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                    fragment.setStatus(0);
                    ft.add(R.id.container,fragment)
                            .addToBackStack("categoryFragment")
                            .commit();
                }else{
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
        ImageView editTv = (ImageView)actionBarView.findViewById(R.id.iv_actionbar_edit);
        editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                CategoryFragment fragment = CategoryFragment.getInstance();
                if(fm.getBackStackEntryCount() == 0){
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                    fragment.setStatus(1);
                    ft.add(R.id.container,fragment)
                            .addToBackStack("categoryFragment")
                            .commit();
                }else{
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                    ft.remove(fragment).commit();
                    fm.popBackStack();
                    if(fragment.getStatus() == 1){

                    }else{
                        FragmentTransaction ft1 = fm.beginTransaction();
                        ft1.setCustomAnimations(R.anim.fragment_slide_bottom_in, R.anim.fragment_slide_bottom_out);
                        fragment.setStatus(1);
                        ft1.add(R.id.container,fragment)
                                .addToBackStack("categoryFragment")
                                .commit();
                    }
                }
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
        mMapView.onSaveInstanceState(outState);
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



    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        deactivate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        AppContext.commonLog.i("Location changed");
        if(aMapLocation == null){
            Toast.makeText(this,"Get location failed!",Toast.LENGTH_SHORT).show();
        }
        if(mOnLocationChangedListener != null) {
            mOnLocationChangedListener.onLocationChanged(aMapLocation);
            AppContext.lastPosition = new Position();
            AppContext.lastPosition.setLatitude(aMapLocation.getLatitude());
            AppContext.lastPosition.setLongitude(aMapLocation.getLongitude());
            AppContext.sharedPreferenceUtil.setLastPosition(AppContext.lastPosition);
            deactivate();
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

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListener = onLocationChangedListener;
        if(mLocationManagerProxy == null){
            mLocationManagerProxy = LocationManagerProxy.getInstance(this);
            mLocationManagerProxy.requestLocationUpdates(LocationProviderProxy.AMapNetwork,5000,10,this);
        }

    }

    @Override
    public void deactivate() {
        mOnLocationChangedListener = null;
        if(mLocationManagerProxy != null){
            mLocationManagerProxy.removeUpdates(this);
            mLocationManagerProxy.destory();
        }
        mLocationManagerProxy = null;
    }

}
