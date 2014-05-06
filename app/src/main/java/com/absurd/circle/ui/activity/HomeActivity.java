package com.absurd.circle.ui.activity;


import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.AzureClient;
import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.Position;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.im.manager.XmppConnectionManager;
import com.absurd.circle.im.service.ChatService;
import com.absurd.circle.ui.activity.base.IProgressBarActivity;
import com.absurd.circle.ui.fragment.CategoryFragment;
import com.absurd.circle.ui.fragment.HomeFragment;
import com.absurd.circle.ui.fragment.SlidingMenuFragment;
import com.absurd.circle.ui.widget.AppMsg;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.LogFactory;
import com.absurd.circle.util.NetworkUtil;
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
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.OfflineMessageManager;

import java.util.Iterator;
import java.util.List;


public class HomeActivity extends SlidingFragmentActivity
        implements IProgressBarActivity, AMapLocationListener, LocationSource {
    private HomeFragment mContent;
    /**
     * false MessageListFragment
     * true CategoryFragment
     */
    private boolean mStatus = false;
    private SlidingMenuFragment mSlidingMenuFragment;
    private ProgressBar mProgressBar;

    private UserService mUserService;


    //AMap backgroung
    private MapView mMapView;
    private AMap mAMap;
    private OnLocationChangedListener mOnLocationChangedListener;
    private LocationManagerProxy mLocationManagerProxy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!getAuth()) {
            IntentUtil.startActivity(this,LoginActivity.class);
            this.finish();
        }


        // set the Above View
        if (savedInstanceState != null)
            mContent = (HomeFragment)getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null)
            mContent = new HomeFragment();

        // Init Data component
        mUserService = new UserService();
        init();
        //getFollowers();
        // Configur some UI control
        initUI(savedInstanceState);
        if(!NetworkUtil.isNetConnected()){
            warning(R.string.network_disconnected);
            setBusy(false);
        }

        //new ChatLoginTask().execute();
        Intent chatServiceIntent = new Intent(HomeActivity.this, ChatService.class);
        HomeActivity.this.startService(chatServiceIntent);


        //initAMap();

    }

    private void initUI(Bundle savedInstanceState){
        configureSlidingMenu();
        configureActionBar();

        // set the Above View
        setContentView(R.layout.activity_home);
        mProgressBar = (ProgressBar)findViewById(R.id.pb_action_bar);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mContent)
                .commit();

        // Map background
        mMapView = (MapView)findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
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


    private void initAMap(){
        if(mAMap == null) {
            mAMap = mMapView.getMap();
        }
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        mAMap.setLocationSource(this);
        mAMap.setMyLocationEnabled(true);
        // Setting zoom
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(18));
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


    public class ChatLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(AppContext.isAuthed()) {
                AppContext.xmppConnectionManager.init();
                if (!AppContext.xmppConnectionManager.getConnection().isAuthenticated()) {
                    AppContext.xmppConnectionManager.login(AppContext.auth.getId() + "", AppContext.auth.getId() + "");
                }
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if(b){
                AppContext.commonLog.i("Chat login success");
                Intent chatServiceIntent = new Intent(HomeActivity.this, ChatService.class);
                HomeActivity.this.startService(chatServiceIntent);
                getOfflineMessage();
            }
        }
    }

    private void getOfflineMessage(){
        OfflineMessageManager offlineManager = new OfflineMessageManager(AppContext.xmppConnectionManager.getConnection());
        try {
            Iterator<Message> it = offlineManager.getMessages();
            AppContext.commonLog.i("have offline messages" + it.hasNext());

            AppContext.commonLog.i(offlineManager.supportsFlexibleRetrieval() + "");
            AppContext.commonLog.i("get offlien count "  + offlineManager.getMessageCount() + "");
            while(it.hasNext()) {
                Message message = it.next();
                AppContext.commonLog.i(message.toString());
            }
            offlineManager.deleteMessages();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

    }


    public void setBusy(boolean busy){
        if(busy){
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.GONE);
        }
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
            //Toast.makeText(this,"Get location failed!",Toast.LENGTH_SHORT).show();
            warning(R.string.location_success);
        }
        if(mOnLocationChangedListener != null) {
            AppContext.commonLog.i("Get location success!");
            mOnLocationChangedListener.onLocationChanged(aMapLocation);
            AppContext.lastPosition = new Position();
            AppContext.lastPosition.setLatitude(aMapLocation.getLatitude());
            AppContext.lastPosition.setLongitude(aMapLocation.getLongitude());
            AppContext.sharedPreferenceUtil.setLastPosition(AppContext.lastPosition);

            //mContent.refreshTranscation();
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mLocationManagerProxy.requestLocationUpdates(LocationProviderProxy.AMapNetwork,5000,10,HomeActivity.this);
                }
            }).start();
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


    public void warning(String content){
        AppMsg.makeText(this, content, AppMsg.STYLE_ALERT).show();
    }

    public void warning(int resId){
        String content = AppContext.getContext().getString(resId);
        warning(content);
    }

}
