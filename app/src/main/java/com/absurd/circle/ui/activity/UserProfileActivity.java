package com.absurd.circle.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.BlackList;
import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.FunsCount;
import com.absurd.circle.data.model.Photo;
import com.absurd.circle.data.model.ReportMessage;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.NotificationService;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.adapter.PhotoAdapter;
import com.absurd.circle.ui.view.ItemDialog;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.TimeUtil;
import com.android.volley.Response;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends BaseActivity {
    private Bitmap mAvatarDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_avatar)).getBitmap();
    private Bitmap mBackgroundDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_user_background)).getBitmap();
    private Bitmap mFemaleBitmap = ((BitmapDrawable)AppContext.getContext().getResources().getDrawable(R.drawable.user_profile_female)).getBitmap();
    private Bitmap mMaleBitmap = ((BitmapDrawable)AppContext.getContext().getResources().getDrawable(R.drawable.user_profile_male)).getBitmap();


    private User mUser;
    private Follow mFollow;
    private BlackList mBlackList;
    private List<Photo> mUserPhotos;

    private ImageView mUserBackGroundIv;
    private ImageView mAvatarIv;
    private TextView mUsernameTv;
    private TextView mAgeTv;
    private ImageView mSexIv;
    private TextView mLevelTv;
    private GridView mPhotoGv;

    private TextView mIdTv;
    private TextView mDescTv;
    private TextView mInterestTv;
    private TextView mJobTv;
    private TextView mBadRecordTv;
    private TextView mFunsCountTv;
    private PhotoAdapter mPhotoAdapter;

    private View mBottomBar;
    private View mAddFollowBtn;
    private TextView mAddFollowTextTv;
    private View mSendMessageBtn;

    public UserProfileActivity() {
        // Set custom actionbar
        setRightBtnStatus(RIGHT_MORE_BTN);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mUser = (User)getIntent().getExtras().get("user");


        mUserBackGroundIv = (ImageView)findViewById(R.id.iv_user_profile_background);
        mAvatarIv = (ImageView)findViewById(R.id.iv_user_profile_avatar);
        mSexIv = (ImageView)findViewById(R.id.iv_user_profile_sex);
        mAgeTv = (TextView)findViewById(R.id.tv_user_profile_age);
        mUsernameTv = (TextView)findViewById(R.id.tv_user_profile_username);
        mLevelTv = (TextView)findViewById(R.id.tv_user_profile_level);
        mPhotoGv = (GridView)findViewById(R.id.gv_photo);
        mPhotoAdapter = new PhotoAdapter(this);
        mPhotoGv.setAdapter(mPhotoAdapter);


        mIdTv = (TextView)findViewById(R.id.tv_user_profile_id);
        mDescTv = (TextView)findViewById(R.id.tv_user_profile_description);
        mInterestTv = (TextView)findViewById(R.id.tv_user_profile_interest);
        mJobTv = (TextView)findViewById(R.id.tv_user_profile_job);
        mBadRecordTv = (TextView)findViewById(R.id.tv_user_profile_bad_record);
        mFunsCountTv = (TextView)findViewById(R.id.tv_user_profile_info);


        // Get Data
        UserService userService = new UserService();
        userService.getFunsCount(mUser.getUserId(), new Response.Listener<FunsCount>() {
            @Override
            public void onResponse(FunsCount funsCount) {
                setBusy(false);
                AppContext.commonLog.i(funsCount.toString());
                mFunsCountTv.setText(String.format("关注 %d     粉丝 %d    动态 %d  ",
                        funsCount.getFollowCount(),
                        funsCount.getFunsCount(),
                        funsCount.getMessageCount()));
            }
        });
        userService.getPhotoes(mUser.getUserId(), new TableQueryCallback<Photo>() {
            @Override
            public void onCompleted(List<Photo> result, int count, Exception exception, ServiceFilterResponse response) {
                if(result == null || result.isEmpty()){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                    AppContext.commonLog.i("User photos is null");
                }else {
                    for(Photo photo : result){
                        AppContext.commonLog.i("Photo --> " + photo.getUrl());
                    }
                    mUserPhotos = result;
                    mPhotoAdapter.setItems(mUserPhotos);
                }
            }
        });
        RequestManager.loadImage(mUser.getAvatar(),RequestManager.getImageListener(mAvatarIv,
                mAvatarDefaultBitmap,mAvatarDefaultBitmap,new BitmapFilter() {
                    @Override
                    public Bitmap filter(Bitmap bitmap) {
                        return ImageUtil.roundBitmap(bitmap);
                    }
                }));
        if(mUser.getBackgroundImage() != null) {
            RequestManager.loadImage(mUser.getBackgroundImage(), RequestManager.getImageListener(mUserBackGroundIv,
                    mBackgroundDefaultBitmap, mBackgroundDefaultBitmap, null));
        }else{
            mUserBackGroundIv.setImageBitmap(mBackgroundDefaultBitmap);
        }
        if(mUser.getSex().equals("f")){
            mSexIv.setImageBitmap(mFemaleBitmap);
        }else{
            mSexIv.setImageBitmap(mMaleBitmap);
        }
        mUsernameTv.setText(mUser.getName());
        mLevelTv.setText("LV. " + mUser.getLevel());
        mAgeTv.setText(TimeUtil.toAge(mUser.getAge()));
        mIdTv.setText(mUser.getId() + "");
        mDescTv.setText(mUser.getDescription());
        mInterestTv.setText(mUser.getHobby());
        mJobTv.setText(mUser.getProfession());

        initBottomBar();

    }


    private void initBottomBar(){
        mBottomBar = findViewById(R.id.llyt_user_profile_bottom_bar);
        mAddFollowBtn = findViewById(R.id.llyt_bar_btn_add_follow);
        mAddFollowTextTv = (TextView)findViewById(R.id.tv_btn_add_follow);

        mSendMessageBtn = findViewById(R.id.llyt_bar_btn_send_message);

        if(mUser.getUserId().equals(AppContext.auth.getUserId())){
            mBottomBar.setVisibility(View.GONE);
        }
        mFollow = AppContext.cacheService.followDBManager.findFollow(mUser.getUserId());
        if(mFollow != null){
            mAddFollowTextTv.setText("取消关注");
        }

        mBlackList = AppContext.cacheService.blackListDBManager.findBlackList(mUser.getUserId());

        mAddFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFollow();
                setBusy(true);
            }
        });

        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(UserProfileActivity.this, ChatActivity.class, "touser", mUser);
            }
        });
    }

    @Override
    protected String actionBarTitle() {
        return "用户资料";
    }


    public void onUserDynamicClick(View view){
        IntentUtil.startActivity(this,UserDynamicActivity.class,"userId",mUser.getUserId());
    }

    public void addFollow(){
        UserService service = new UserService();
        if(mAddFollowTextTv.getText().equals("添加关注")){
            final Follow follow = new Follow();
            follow.setUserId(AppContext.auth.getUserId());
            follow.setFollowUserId(mUser.getUserId());
            follow.setUser(mUser);
            service.insertFollower(follow,new TableOperationCallback<Follow>() {
                @Override
                public void onCompleted(Follow entity, Exception exception, ServiceFilterResponse response) {
                    setBusy(false);
                    if(entity == null){
                        if(exception != null){
                            exception.printStackTrace();
                        }
                    }else{
                        AppContext.commonLog.i(entity.toString());
                        AppContext.cacheService.followDBManager.insertFollow(entity);
                        mAddFollowTextTv.setText("取消关注");
                        mFollow = entity;
                        warning(R.string.add_follow_success);
                    }
                }
            });
        }else{
            service.deleteFollower(mFollow,new TableDeleteCallback() {
                @Override
                public void onCompleted(Exception exception, ServiceFilterResponse response) {
                    setBusy(false);
                    if(exception != null){
                        exception.printStackTrace();
                    }else{
                        AppContext.cacheService.followDBManager.deleteFollow(mFollow.getFollowUserId());
                        mAddFollowTextTv.setText("添加关注");
                        mFollow = null;
                        warning(R.string.cancel_follow_success);
                    }
                }
            });
        }

    }

    @Override
    public void onMoreClicked(View view){
        List<String> items = new ArrayList<String>();
        if(mBlackList == null) {
            items.add("加入黑名单");
        }else{
            items.add("取消黑名单");
        }
        items.add("举报此人");
        items.add("屏蔽此人说说");
        final ItemDialog dialog = new ItemDialog(this, items);
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        if(mBlackList == null) {
                            addBlackList();
                        }else{
                            deleteBlackList();
                        }
                        break;
                    case 1:
                        reportUser();
                        break;
                    case 2:
                        break;
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }


    private void addBlackList(){
        final BlackList black = new BlackList();
        black.setUserId(AppContext.userId);
        black.setFollowUserId(mUser.getUserId());
        UserService service = new UserService();
        service.insertBlackList(black, new TableOperationCallback<BlackList>() {
            @Override
            public void onCompleted(BlackList entity, Exception exception, ServiceFilterResponse response) {
                if(entity == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                    warning(R.string.add_black_list_failed);
                }else{
                    AppContext.cacheService.blackListDBManager.insertBlackList(black);
                    mBlackList = entity;
                    warning(R.string.add_black_list_success);
                }
            }
        });
    }

    private void deleteBlackList(){
        UserService userService = new UserService();
        userService.deleteBlackList(mBlackList,new TableDeleteCallback() {
            @Override
            public void onCompleted(Exception exception, ServiceFilterResponse response) {
                if(exception != null){
                    exception.printStackTrace();
                    warning(R.string.delete_black_list_failed);
                }else{
                    warning(R.string.delete_black_list_success);
                    AppContext.cacheService.blackListDBManager.deleteBlackList(mBlackList.getId());
                    mBlackList = null;
                }
            }
        });
    }

    private void reportUser(){
        NotificationService service = new NotificationService();
        final ReportMessage report = new ReportMessage();
        final List<String> items = new ArrayList<String>();
        items.add("骚扰信息");
        items.add("垃圾广告");
        items.add("色请相关");
        items.add("盗用他人资料");
        final ItemDialog dialog = new ItemDialog(this, items);
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                report.setContent(i + "");
                dialog.cancel();
            }
        });
        dialog.show();
        report.setFromUserId(AppContext.userId);
        report.setFromUserName(AppContext.auth.getName());
        report.setMessageId(0);
        report.setToUserId(mUser.getUserId());
        report.setDeviceId(mUser.getQq());
        service.insertReportMessage(report,new TableOperationCallback<ReportMessage>() {
            @Override
            public void onCompleted(ReportMessage entity, Exception exception, ServiceFilterResponse response) {
                if(entity == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                }else{
                    warning(R.string.report_user_success);
                }
            }
        });

    }

}
