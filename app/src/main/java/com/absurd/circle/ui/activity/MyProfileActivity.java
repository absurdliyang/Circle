package com.absurd.circle.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.view.IUploadImage;
import com.absurd.circle.ui.view.ItemDialog;
import com.absurd.circle.ui.view.PhotoFragment;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.TimeUtil;
import com.android.volley.Response;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

/**
 * Created by absurd on 14-3-29.
 */
public class MyProfileActivity extends BaseActivity implements IUploadImage{
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
    private TextView mLevelTv;

    private TextView mIdTv;
    private TextView mNicknameTv;
    private TextView mDescTv;
    private TextView mBirthdayTv;
    private TextView mSexTv;
    private TextView mPhoneTv;
    private TextView mInterestTv;
    private TextView mJobTv;

    private PhotoFragment mPhotoFragment;

    public MyProfileActivity(){
        setRightBtnStatus(RIGHT_TEXT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        user = (User)AppContext.auth.clone();
        AppContext.commonLog.i(user.toString());
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
        mLevelTv = (TextView)findViewById(R.id.tv_my_profile_level);

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
                    setBusy(false);
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
                mSexIv.setImageBitmap(mFemailBitmap);
                mSexTv.setText("女");
            } else {
                mSexIv.setImageBitmap(mMaleBitmap);
                mSexTv.setText("男");
            }
            mUsernameTv.setText(user.getName());
            mLevelTv.setText("LV. " + user.getLevel());
            mAgeTv.setText(TimeUtil.toAge(user.getAge()));

            mIdTv.setText(user.getId() + "");
            mNicknameTv.setText(user.getName());
            mDescTv.setText(user.getDescription());
            mInterestTv.setText(user.getHobby());
            mJobTv.setText(user.getProfession());
            mBirthdayTv.setText(user.getAge().toString());

            mAvatarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPhotoFragment = new PhotoFragment();
                    mPhotoFragment.setIUploadImage(MyProfileActivity.this);
                    mPhotoFragment.show(getSupportFragmentManager(),null);

                }
            });

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
        setBusy(true);
        mUserService.updateUser(user,new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                setBusy(false);
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


    @Override
    public void onResultByAlbum(Intent data) {
        // TODO Auto-generated method stub
        if(data == null){
            Toast.makeText(this, "选择图片出错", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri photoUri = data.getData();
        cropPhoto(photoUri);
    }
    @Override
    public void onResultByTake(Intent data) {
        // TODO Auto-generated method stub
        Uri u = null;
        if (PhotoFragment.hasImageCaptureBug()) {
            File fi = new File("/sdcard/tmp");
            try {
                u = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(this.getContentResolver(), fi.getAbsolutePath(), null, null));
                if (!fi.delete()) {
                    AppContext.commonLog.i("Failed to delete " + fi);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            u = PhotoFragment.uri;
        }
        cropPhoto(u);
    }
    @Override
    public void onResultByCrop(Intent data) {
        // TODO Auto-generated method stub
        Bundle extras = data.getExtras();
        if (extras != null)
        {
            Bitmap photo = extras.getParcelable("data");
            if(photo != null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                mAvatarIv.setImageBitmap(ImageUtil.roundBitmap(photo));
            }
        }
    }

    /**
     * picture zooming
     *
     */
    private void cropPhoto(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true);
        this.startActivityForResult(intent, IUploadImage.IMAGE_CROP);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoFragment.onActivityResult(requestCode, resultCode, data);
    }
}
