package com.absurd.circle.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.BitmapFilter;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.FunsCount;
import com.absurd.circle.data.model.Photo;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.BCSService;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.adapter.PhotoAdapter;
import com.absurd.circle.ui.fragment.BlackListFragment;
import com.absurd.circle.ui.fragment.FollowersFragment;
import com.absurd.circle.ui.fragment.FunsFragment;
import com.absurd.circle.ui.view.IUploadImage;
import com.absurd.circle.ui.view.ItemDialog;
import com.absurd.circle.ui.view.PhotoFragment;
import com.absurd.circle.util.FileUtil;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.NetworkUtil;
import com.absurd.circle.util.StringUtil;
import com.absurd.circle.util.TimeUtil;
import com.absurd.circle.video.Config;
import com.android.volley.Response;
import com.microsoft.windowsazure.mobileservices.AsyncTaskUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

/**
 * Created by absurd on 14-3-29.
 */
public class MyProfileActivity extends BaseActivity implements IUploadImage{
    private Bitmap mAvatarDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_avatar)).getBitmap();
    private Bitmap mBackgroundDefaultBitmap = ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.default_user_background)).getBitmap();
    private Bitmap mFemaleBitmap = ((BitmapDrawable)AppContext.getContext().getResources().getDrawable(R.drawable.user_profile_female)).getBitmap();
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
    private GridView mPhotoGv;
    private PhotoAdapter mPhotoAdapter;

    private PhotoFragment mPhotoFragment;
    private PhotoFragment mMediaFragment;
    private ArrayList<Photo> mUserPhotos;

    private boolean  mPhotoSelectedStatus;
    private boolean mIsGalleryFragment = false;
    private Uri mImageUri;
    private String mImageUrl;
    private String mVedioUrl;
    private String mPicPath;
    private String mVedioPath;
    private String mThumbnailUrl;


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
        mPhotoGv = (GridView)findViewById(R.id.gv_photo_edit);
        mPhotoAdapter = new PhotoAdapter(this,true);
        mPhotoGv.setAdapter(mPhotoAdapter);

        mPhotoFragment = new PhotoFragment();
        mPhotoFragment.setIUploadImage(this);
        mMediaFragment = new PhotoFragment(2);
        mMediaFragment.setIUploadImage(new GalleyUploadImage());
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

            mUserService.getPhotoes(user.getUserId(), new TableQueryCallback<Photo>() {
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
                        mUserPhotos = (ArrayList<Photo>)result;
                        mPhotoAdapter.setItems(mUserPhotos);
                    }
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
            if(user.getSex() != null) {
                if (user.getSex() != null) {
                    if (user.getSex().equals("m")) {
                        mSexIv.setImageBitmap(mMaleBitmap);
                        mSexTv.setText("男");
                    } else {
                        mSexIv.setImageBitmap(mFemaleBitmap);
                        mSexTv.setText("女");
                    }
                }
            }
            mUsernameTv.setText(user.getName());
            mLevelTv.setText("LV. " + user.getLevel());
            mAgeTv.setText(TimeUtil.toAge(user.getAge()));

            mIdTv.setText(user.getId() + "");
            mNicknameTv.setText(user.getName());
            mDescTv.setText(user.getDescription());
            mInterestTv.setText(user.getHobby());
            mJobTv.setText(user.getProfession());
            mPhoneTv.setText(user.getPhone());
            mBirthdayTv.setText(user.getAge().toString());

            mAvatarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPhotoFragment.setTitle("更换头像");
                    mIsGalleryFragment = false;
                    mPhotoSelectedStatus = true;
                    mPhotoFragment.show(getSupportFragmentManager(),null);
                }
            });

            mUserBackGroundIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPhotoFragment.setTitle("更换背景");
                    mIsGalleryFragment = false;
                    mPhotoSelectedStatus = false;
                    mPhotoFragment.show(getSupportFragmentManager(),null);
                }
            });

        }
    }

    private void invalidateView(){
        if(user != null) {
            if (user.getSex().equals("m")) {
                mSexIv.setImageBitmap(mMaleBitmap);
                mSexTv.setText("男");
            } else {
                mSexIv.setImageBitmap(mFemaleBitmap);
                mSexTv.setText("女");
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
        }
    }

    public void onProfileItemClick(View view){
        String tag = "";
        String value = "";
        switch(view.getId()){
            case R.id.btn_item_my_profile_nickname:
                tag = "nickname";
                value = AppContext.auth.getName();
                break;
            case R.id.btn_item_my_profile_description:
                tag = "description";
                value = AppContext.auth.getDescription();
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
                value = AppContext.auth.getHobby();
                break;
            case R.id.btn_item_my_profile_job:
                tag = "job";
                value = AppContext.auth.getProfession();
                break;
            case R.id.btn_item_my_profile_phone:
                tag = "phone";
                value = AppContext.auth.getPhone();
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
                            user.setSex("m");
                        }else{
                            user.setSex("f");
                        }
                        sexsDialog.cancel();
                        invalidateView();
                    }
                });
                sexsDialog.show();
                return;
        }
        Map<String, String> params = new HashMap<String,String>();
        params.put("tag",tag);
        params.put("value",value);
        IntentUtil.startActivity(this, EditItemActivity.class, params);
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
        mUserService.updateUser(user, new TableOperationCallback<User>() {
            @Override
            public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                setBusy(false);
                if (entity == null) {
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                    warning(R.string.update_user_profile_failed);
                    return;
                } else {
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
        mImageUri = data.getData();
        cropPhoto(mImageUri);
    }
    @Override
    public void onResultByTake(Intent data) {
        // TODO Auto-generated method stub
        Uri u = null;
        if (PhotoFragment.hasImageCaptureBug()) {
            File fi = new File("/sdcard/tmp");
            try {
                u = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(this.getContentResolver(), fi.getAbsolutePath(), null, null));
                mImageUri = u;
                if (!fi.delete()) {
                    AppContext.commonLog.i("Failed to delete " + fi);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            u = PhotoFragment.uri;
            mImageUri = u;
        }
        cropPhoto(u);
    }
    @Override
    public void onResultByCrop(Intent data) {
        // TODO Auto-generated method stub
        mPicPath = FileUtil.getPicPathFromUri(mImageUri, this);
        Bundle extras = data.getExtras();
        if (extras != null){
            Bitmap photo = extras.getParcelable("data");
            if(photo != null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                if(mPhotoSelectedStatus) {
                    mAvatarIv.setImageBitmap(ImageUtil.roundBitmap(photo));
                }else{
                    mUserBackGroundIv.setImageBitmap(photo);
                }
                if(NetworkUtil.isNetConnected()) {
                    AsyncTaskUtil.addTaskInPool(new ImageUploadTask(mIsGalleryFragment));
                }else{
                    warning(R.string.net_disconnected_warning_upload_failed);
                }
            }
        }
    }

    @Override
    public void onResultByRecorder(Intent data) {

    }

    /**
     * picture zooming
     *
     */
    private void cropPhoto(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        if(mPhotoSelectedStatus) {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
        }else{
            intent.putExtra("aspectX", 2);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 500);
            intent.putExtra("outputY", 250);
        }
        // save the croped picture
        Uri saveUri = mPhotoFragment.getUriPath();
        mImageUri = saveUri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
        intent.putExtra("return-data", true);
        this.startActivityForResult(intent, IUploadImage.IMAGE_CROP);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mIsGalleryFragment){
            mMediaFragment.onActivityResult(requestCode, resultCode, data);
        }else {
            mPhotoFragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void uploadGalleryPhoto(){
        mIsGalleryFragment = true;
        mMediaFragment.setTitle("上传相册图片");
        mMediaFragment.show(getSupportFragmentManager(),null);
    }

    public void deleteGalleryPhoto(final int position){
        List<String> items = new ArrayList<String>();
        items.add("删除");
        final ItemDialog dialog = new ItemDialog(this,items);
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        final Photo item = mUserPhotos.get(position);
                        mUserService.deletePhoto(item,new TableDeleteCallback() {
                            @Override
                            public void onCompleted(Exception exception, ServiceFilterResponse response) {
                                if(exception != null){
                                    exception.printStackTrace();
                                    return;
                                }
                                mPhotoAdapter.deleteItem(item);
                                warning(R.string.delete_gallery_photo_success);
                            }
                        });
                        break;
                    default:
                        break;
                }
                dialog.cancel();
            }
        });
        dialog.show();

    }




    public class GalleyUploadImage implements IUploadImage{
        @Override
        public void onResultByAlbum(Intent data) {
            mPicPath = FileUtil.getPicPathFromUri(data.getData(), MyProfileActivity.this);
            if(NetworkUtil.isNetConnected()) {
                AsyncTaskUtil.addTaskInPool(new ImageUploadTask(mIsGalleryFragment),0);

            }else{
                warning(R.string.net_disconnected_warning_upload_failed);
            }
        }

        @Override
        public void onResultByTake(Intent data) {

        }

        @Override
        public void onResultByCrop(Intent data) {

        }

        @Override
        public void onResultByRecorder(Intent data) {
            File file = (File)data.getSerializableExtra(Config.VIDEO_RESULT_DATA);
            if(file != null) {
                mVedioPath = file.getAbsolutePath();
                AppContext.commonLog.i("Video absolute file path --> " + file.getAbsolutePath());

                if(NetworkUtil.isNetConnected()) {
                    AsyncTaskUtil.addTaskInPool(new ImageUploadTask(mIsGalleryFragment),1);
                }else{
                    warning(R.string.net_disconnected_warning_upload_failed);
                }
            }
        }
    }


    public class ImageUploadTask extends AsyncTask<Object, Void, Boolean> {

        private int mMediaType;
        private boolean mIsGallery;

        public ImageUploadTask(Boolean isVedio){
            this.mIsGallery = isVedio;
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            // TODO Auto-generated method stub
            if(mIsGallery) {
                if(params == null){
                    return false;
                }
                mMediaType = (Integer)params[0];
                if (mMediaType == 1) {
                    return uploadVedio();
                } else {
                    return uploadPhoto();
                }
            }else{
                return uploadPhoto();
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result == false){
                AppContext.commonLog.i("Add photo failed");
                if(mIsGallery) {
                    if (mPhotoSelectedStatus) {
                        warning(R.string.change_avatar_failed);
                    } else {
                        warning(R.string.change_background_failed);
                    }
                }
                return;
            }
            if(mIsGallery){
                warning(R.string.upload_gallery_success);
                if(mMediaType == 0){
                    insertPhoto(mMediaType);
                }else{
                    AsyncTaskUtil.addTaskInPool(new AsyncTask() {
                        @Override
                        protected Object doInBackground(Object[] objects) {
                            final Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(mVedioPath, MediaStore.Video.Thumbnails.MINI_KIND);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            thumbnail.compress(Bitmap.CompressFormat.JPEG, 1, stream);
                            byte[] bytes = stream.toByteArray();
                            try {
                                mThumbnailUrl = BCSService.uploadImageByByteStream(bytes);
                            } catch (IOException e) {
                                e.printStackTrace();
                                AppContext.commonLog.i(e.toString());
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);
                            insertPhoto(1);
                        }
                    });
                }
            }else {
                if (mPhotoSelectedStatus) {
                    AppContext.auth.setAvatar(mImageUrl);
                    AppContext.cacheService.userDBManager.updateUser(AppContext.auth);
                    warning(R.string.change_avatar_success);
                } else {
                    AppContext.auth.setBackgroundImage(mImageUrl);
                    AppContext.cacheService.userDBManager.updateUser(AppContext.auth);
                    warning(R.string.change_background_success);
                }
            }
        }


        private void insertPhoto(int type){
            Photo photo = new Photo();
            if(type == 0) {
                photo.setType(0);
                photo.setUrl(mImageUrl);
            }else if(type == 1){
                photo.setType(1);
                photo.setUrl(mVedioUrl);
                photo.setMediaUrl(mThumbnailUrl);
            }
            photo.setUserId(AppContext.auth.getUserId());
            mUserService.insertPhoto(photo, new TableOperationCallback<Photo>() {
                @Override
                public void onCompleted(Photo entity, Exception exception, ServiceFilterResponse response) {
                    if(entity == null){
                        if(exception != null){
                            exception.printStackTrace();
                        }
                        return;
                    }
                    AppContext.commonLog.i("Add photo success");
                    mPhotoAdapter.addItem(entity);
                }
            });
        }

        private boolean uploadPhoto(){
            if (StringUtil.isEmpty(mPicPath)) {
                return false;
            }
            String loadPicPath = ImageUtil.compressPic(MyProfileActivity.this, mPicPath, 1);
            File f = new File(loadPicPath);
            mImageUrl = BCSService.uploadImageByFile(f);
            return true;
        }
        private boolean uploadVedio(){
            if (StringUtil.isEmpty(mVedioPath)) {
                return false;
            }
            File f = new File(mVedioPath);
            mVedioUrl = BCSService.uploadImageByFile(f);
            return true;
        }
    }





}
