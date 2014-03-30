package com.absurd.circle.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.TimeUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

public class UserProfileActivity extends BaseActivity {
    private Bitmap mAvatarDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_avatar)).getBitmap();
    private Bitmap mBackgroundDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_user_background)).getBitmap();
    private Bitmap mFemailBitmap = ((BitmapDrawable)AppContext.getContext().getResources().getDrawable(R.drawable.user_profile_female)).getBitmap();
    private Bitmap mMaleBitmap = ((BitmapDrawable)AppContext.getContext().getResources().getDrawable(R.drawable.user_profile_male)).getBitmap();


    private User mUser;
    private Follow mFollow;

    private ImageView mUserBackGroundIv;
    private ImageView mAvatarIv;
    private TextView mUsernameTv;
    private TextView mAgeTv;
    private ImageView mSexIv;

    private TextView mIdTv;
    private TextView mDescTv;
    private TextView mInterestTv;
    private TextView mJobTv;
    private TextView mBadRecordTv;

    private TextView mAddFolloeBtn;

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

        mIdTv = (TextView)findViewById(R.id.tv_user_profile_id);
        mDescTv = (TextView)findViewById(R.id.tv_user_profile_description);
        mInterestTv = (TextView)findViewById(R.id.tv_user_profile_interest);
        mJobTv = (TextView)findViewById(R.id.tv_user_profile_job);
        mBadRecordTv = (TextView)findViewById(R.id.tv_user_profile_bad_record);

        mAddFolloeBtn = (TextView)findViewById(R.id.tv_user_profile_add_follow_btn);

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
        if(mUser.getSex() == "男"){
            mSexIv.setImageBitmap(mMaleBitmap);
        }else{
            mSexIv.setImageBitmap(mFemailBitmap);
        }
        mUsernameTv.setText(mUser.getName());
        mAgeTv.setText(TimeUtil.toAge(mUser.getAge()));
        mIdTv.setText(mUser.getId() + "");
        mDescTv.setText(mUser.getDescription());
        mInterestTv.setText(mUser.getHobby());
        mJobTv.setText(mUser.getProfession());

        if(mUser.getUserId().equals(AppContext.auth.getUserId())){
            mAddFolloeBtn.setVisibility(View.GONE);
        }
        mFollow = AppContext.cacheService.findFollow(mUser.getUserId());
        if(mFollow != null){
            mAddFolloeBtn.setText("取消关注");
        }
    }


    @Override
    protected String actionBarTitle() {
        return "用户资料";
    }


    public void onUserDynamicClick(View view){
        IntentUtil.startActivity(this,UserDynamicActivity.class,"userId",mUser.getUserId());
    }

    public void onAddFollowClick(View view){
        UserService service = new UserService();
        if(mAddFolloeBtn.getText().equals("添加关注")){
            final Follow follow = new Follow();
            follow.setUserId(AppContext.auth.getUserId());
            follow.setFollowUserId(mUser.getUserId());
            service.insertFollower(follow,new TableOperationCallback<Follow>() {
                @Override
                public void onCompleted(Follow entity, Exception exception, ServiceFilterResponse response) {
                    if(entity == null){
                        if(exception != null){
                            exception.printStackTrace();
                        }
                    }else{
                        AppContext.cacheService.insertFollow(follow);
                        mAddFolloeBtn.setText("取消关注");
                    }
                }
            });
        }else{
            service.deleteFollower(mFollow,new TableDeleteCallback() {
                @Override
                public void onCompleted(Exception exception, ServiceFilterResponse response) {
                    if(exception != null){
                        exception.printStackTrace();
                    }else{
                        AppContext.cacheService.deleteFollow(mFollow.getFollowUserId());
                    }
                }
            });
        }

    }



    public void onSendMessageClick(View view){

    }
}
