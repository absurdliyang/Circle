package com.absurd.circle.ui.activity;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.FunsCount;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.ui.view.ItemDialog;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.TimeUtil;
import com.android.volley.Response;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

/**
 * Created by absurd on 14-3-29.
 */
public class MyProfileActivity extends BaseActivity{
    private Bitmap mAvatarDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_avatar)).getBitmap();
    private Bitmap mBackgroundDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_user_background)).getBitmap();
    private Bitmap mFemailBitmap = ((BitmapDrawable)AppContext.getContext().getResources().getDrawable(R.drawable.user_profile_female)).getBitmap();
    private Bitmap mMaleBitmap = ((BitmapDrawable)AppContext.getContext().getResources().getDrawable(R.drawable.user_profile_male)).getBitmap();

    private UserService mUserService = new UserService();
    public static User user;

    private ImageView mUserBackGroundIv;
    private ImageView mAvatarIv;
    private TextView mFunsCountTv;
    private TextView mUsernameTv;
    private TextView mAgeTv;
    private ImageView mSexIv;

    private TextView mIdTv;
    private TextView mNicknameTv;
    private TextView mDescTv;
    private TextView mBirthdayTv;
    private TextView mSexTv;
    private TextView mPhoneTv;
    private TextView mInterestTv;
    private TextView mJobTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        user = (User)AppContext.auth.clone();
        // Init custom actionbar
        setRightBtnStatus(RIGHT_TEXT);

        initView();
        bindData();


    }

    private void initView(){
        mUserBackGroundIv = (ImageView)findViewById(R.id.iv_my_profile_background);
        mAvatarIv = (ImageView)findViewById(R.id.iv_my_profile_avatar);
        mSexIv = (ImageView)findViewById(R.id.iv_my_profile_sex);
        mAgeTv = (TextView)findViewById(R.id.tv_my_profile_age);
        mUsernameTv = (TextView)findViewById(R.id.tv_my_profile_username);
        mFunsCountTv = (TextView)findViewById(R.id.tv_my_profile_info);

        mIdTv = (TextView)findViewById(R.id.tv_my_profile_id);
        mNicknameTv = (TextView)findViewById(R.id.tv_my_profile_nickname);
        mDescTv = (TextView)findViewById(R.id.tv_my_profile_description);
        mInterestTv = (TextView)findViewById(R.id.tv_my_profile_interest);
        mJobTv = (TextView)findViewById(R.id.tv_my_profile_job);
        mBirthdayTv = (TextView)findViewById(R.id.tv_my_profile_birthday);
        mSexTv = (TextView)findViewById(R.id.tv_my_profile_sex);
        mPhoneTv = (TextView)findViewById(R.id.tv_my_profile_phone);
    }

    private void bindData(){
        if(user != null) {
            mUserService.getFunsCount(user.getUserId(), new Response.Listener<FunsCount>() {
                @Override
                public void onResponse(FunsCount funsCount) {
                    AppContext.commonLog.i(funsCount.toString());
                    mFunsCountTv.setText(String.format("关注 %d     粉丝 %d    动态 %d  ",
                            funsCount.getFollowCount(),
                            funsCount.getFunsCount(),
                            funsCount.getMessageCount()));
                }
            });
            RequestManager.loadImage(user.getAvatar(), RequestManager.getImageListener(mAvatarIv,
                    mAvatarDefaultBitmap, mAvatarDefaultBitmap, new BitmapFilter() {
                        @Override
                        public Bitmap filter(Bitmap bitmap) {
                            return ImageUtil.roundBitmap(bitmap);
                        }
                    }
            ));
            if (user.getBackgroundImage() != null) {
                RequestManager.loadImage(user.getBackgroundImage(), RequestManager.getImageListener(mUserBackGroundIv,
                        mBackgroundDefaultBitmap, mBackgroundDefaultBitmap, null));
            } else {
                mUserBackGroundIv.setImageBitmap(mBackgroundDefaultBitmap);
            }
            if (user.getSex() == "m") {
                mSexIv.setImageBitmap(mMaleBitmap);
                mSexTv.setText("女");
            } else {
                mSexIv.setImageBitmap(mFemailBitmap);
                mSexTv.setText("男");
            }
            mUsernameTv.setText(user.getName());
            mAgeTv.setText(TimeUtil.toAge(user.getAge()));

            mIdTv.setText(user.getId() + "");
            mNicknameTv.setText(user.getName());
            mDescTv.setText(user.getDescription());
            mInterestTv.setText(user.getHobby());
            mJobTv.setText(user.getProfession());
            mBirthdayTv.setText(user.getAge().toString());

        }
    }

    private void invalidateView(){
        bindData();
    }

    public void onProfileItemClick(View view){
        String tag = "";
        switch(view.getId()){
            case R.id.btn_item_my_profile_nickname:
                tag = "nickname";
                break;
            case R.id.btn_item_my_profile_description:
                tag = "description";
                break;
            case R.id.btn_item_my_profile_birthday:
                tag = "birhday";
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(user.getAge());
                AppContext.commonLog.i(calendar.getTime().toString());
                new DatePickerDialog(MyProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            java.util.Date utilDate = format.parse(i + "-" + i2 + "-" + i3);
                            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                            user.setAge(sqlDate);
                            invalidateView();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                return;
            case R.id.btn_item_my_profile_interest:
                tag = "interest";
                break;
            case R.id.btn_item_my_profile_job:
                tag = "job";
                break;
            case R.id.btn_item_my_profile_phone:
                tag = "phone";
                break;
            case R.id.btn_item_my_profile_sex:
                tag = "sex";
                List<String> sexs = new ArrayList<String>();
                sexs.add("男");
                sexs.add("女");
                final ItemDialog sexsDialog = new ItemDialog(MyProfileActivity.this, sexs);
                sexsDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i == 0){
                            user.setSex("f");
                        }else{
                            user.setSex("m");
                        }
                        sexsDialog.cancel();
                        invalidateView();
                    }
                });
                sexsDialog.show();
                return;
        }
        IntentUtil.startActivity(this, EditItemActivity.class, "tag", tag);
    }

    public void onMyDynamicClick(View view){
        if(user != null) {
            IntentUtil.startActivity(this, UserDynamicActivity.class, "userId", user.getUserId());
        }
    }

    @Override
    protected String actionBarTitle() {
        return "我的资料";
    }

    @Override
    protected String setRightBtnTxt() {
        return "保存";
    }

    @Override
    public void onRightBtnClicked(View view) {
        super.onRightBtnClicked(view);
        mUserService.updateUser(user,new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                if(entity == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                    warning(R.string.update_user_profile_failed);
                    return;
                }else{
                    AppContext.auth = entity;
                    warning(R.string.update_user_profile_success);
                    AppContext.cacheService.userDBManager.updateUser(entity);
                    invalidateView();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateView();
    }
}
