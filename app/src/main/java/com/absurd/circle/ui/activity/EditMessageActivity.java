package com.absurd.circle.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.model.MessageType;
import com.absurd.circle.data.model.Position;
import com.absurd.circle.data.service.BCSService;
import com.absurd.circle.data.service.MessageService;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.view.IUploadImage;
import com.absurd.circle.ui.view.KeyboardControlEditText;
import com.absurd.circle.ui.widget.smileypicker.SmileyPicker;
import com.absurd.circle.ui.widget.smileypicker.SmileyPickerUtility;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.NetworkUtil;
import com.absurd.circle.util.StringUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import java.io.File;

public class EditMessageActivity extends BaseActivity implements AMapLocationListener{

    private static final int  BROWSE_PIC = 200;

    private LocationManagerProxy mLocationManagerProxy;

    private TextView mLocationTv;
    private ImageView mMediaIv;
    private CheckBox mIsAnonyCb;
    private KeyboardControlEditText mContentEt;
    private SmileyPicker mSmiley;
    private RelativeLayout mContainer;


    private String mContent;
    private String mImageUrl;

    private int mContentType;

    private Uri mImageFileUri;
    private String mPicPath;


    private Message mMessage = new Message();

    private boolean mIsbusy = false;

    public EditMessageActivity(){
        // Set custom actionbar
        setRightBtnStatus(RIGHT_TEXT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentType = (Integer)getIntent().getExtras().get("contentType");

        setContentView(R.layout.activity_edit_message);

        mLocationTv = (TextView)findViewById(R.id.tv_edit_msg_location);
        mMediaIv = (ImageView)findViewById(R.id.iv_edit_msg_photo);
        mMediaIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!StringUtil.isEmpty(mPicPath)){
                    Intent intent = new Intent(EditMessageActivity.this, BrowseImageActivity.class);
                    intent.putExtra("path",mPicPath);
                    startActivityForResult(intent,BROWSE_PIC);
                }
            }
        });
        mIsAnonyCb = (CheckBox)findViewById(R.id.cb_edit_msg_is_anony);
        mContentEt = (KeyboardControlEditText)findViewById(R.id.et_edit_msg_content);
        mContentEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSmileyPicker(true);
            }
        });
        mContentEt.requestFocus();

        mContainer = (RelativeLayout)findViewById(R.id.edit_msg_container);

        mSmiley = (SmileyPicker)findViewById(R.id.edit_msg_smileypicker);
        mSmiley.setEditText(this, ((LinearLayout) findViewById(R.id.root_layout)), mContentEt);


        initView();
        // Get user's current location
        mLocationManagerProxy = LocationManagerProxy.getInstance(EditMessageActivity.this);
        updateLocation();

    }


    @Override
    public void onBackPressed() {
        if (mSmiley.isShown()) {
            hideSmileyPicker(false);
        }
        /**
        else if (!TextUtils.isEmpty(content.getText().toString()) && canShowSaveDraftDialog()) {
            SaveDraftDialog dialog = new SaveDraftDialog();
            dialog.show(getFragmentManager(), "");
        }
         */
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                mLocationManagerProxy.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, EditMessageActivity.this);
            }
        }).start();
    }


    @Override
    protected String actionBarTitle() {
        return "我说";
    }

    @Override
    protected String setRightBtnTxt() {
        return "发送";
    }

    @Override
    public void onRightBtnClicked(View view) {
        if(invalidateContent() && !mIsbusy){
            setBusy(true);
            mIsbusy = true;
            sendMessage();
        }
    }

    private void initFacesView(){
        //mFacesAdapter = new FacesAdapter(this);
        /*
        mFacesGv.setAdapter(mFacesAdapter);
        mFacesGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SpannableString ss = new SpannableString(view.getTag().toString());
                Drawable drawable = getResources().getDrawable((int)mFacesAdapter.getItemId(i));
                drawable.setBounds(0,0,35,35);
                ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                ss.setSpan(span, 0, view.getTag().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mContentEt.getText().insert(mContentEt.getSelectionStart(), ss);
            }
        });
        **/
    }

    private void initView(){
        switch(mContentType){
            case MessageType.WEIBO:
                mContentEt.setHint(R.string.weibo_description);
                break;
            case MessageType.FOOD:
                mContentEt.setHint(R.string.food_description);
                mIsAnonyCb.setVisibility(View.GONE);
                break;
            case MessageType.MOOD:
                mContentEt.setHint(R.string.mood_description);
                break;
            case MessageType.SHOW:
                mContentEt.setHint(R.string.show_description);
                mIsAnonyCb.setVisibility(View.GONE);
                break;
            case MessageType.PARTY:
                mContentEt.setHint(R.string.party_description);
                mIsAnonyCb.setVisibility(View.GONE);
                break;
            case MessageType.FRIEND:
                mContentEt.setHint(R.string.friend_description);
                mIsAnonyCb.setVisibility(View.GONE);
                break;
        }
    }

    private boolean invalidateContent(){
        mContent = mContentEt.getText().toString();
        if(StringUtil.isEmpty(mContent)){
            AppContext.commonLog.i("Message content can not be null!");
            warning(R.string.warning_message_null);
            return false;
        }
        mContent = mContent.trim();
        if(mContentType == MessageType.SHOW && StringUtil.isEmpty(mImageUrl)){
            AppContext.commonLog.i("Image can not be null");
            warning(R.string.warinig_image_null);
            return false;
        }
        return true;
    }


    private void sendMessage(){
        mMessage.setMessagetType(0);
        if(mIsAnonyCb.isChecked()) {
            mMessage.setMessagetType(1);
        }
        mMessage.setContentType(mContentType);
        mMessage.setContent(mContent);
        if(AppContext.lastPosition != null){
            mMessage.setLatitude(AppContext.lastPosition.getLatitude());
            mMessage.setLongitude(AppContext.lastPosition.getLongitude());
            mMessage.setLocationDec("");
        }

        if(!StringUtil.isEmpty(mPicPath)){
            postImageMessage();
        }else {
            postTextMessage();
        }
    }

    private void postTextMessage(){
        MessageService messageService = new MessageService();
        messageService.addMessage(mMessage, new TableOperationCallback<Message>() {
            @Override
            public void onCompleted(Message entity, Exception exception, ServiceFilterResponse response) {
                EditMessageActivity.this.finish();
                setBusy(false);
                if (entity == null) {
                    if (exception != null) {
                        exception.printStackTrace();
                        warning(R.string.send_message_failed);
                    }
                } else {
                    AppContext.commonLog.i(entity.getContent());
                    warning(R.string.send_message_success);
                }
            }
        });
    }

    private void postImageMessage(){
        new UploadImageTask().execute();
    }

    public void onBtnClick(View view){
        switch(view.getId()){
            case R.id.iv_edit_msg_btn_photo:
                onTakePhoto();
                break;
            case R.id.iv_edit_msg_btn_galley:
                onGallary();
                break;
            case R.id.iv_edit_msg_btn_emotion:
                if (mSmiley.isShown()) {
                    hideSmileyPicker(true);
                } else {
                    showSmileyPicker(
                            SmileyPickerUtility.isKeyBoardShow(EditMessageActivity.this));
                }
                break;
            default:
                break;
        }
    }


    private void showSmileyPicker(boolean showAnimation) {
        this.mSmiley.show(this, showAnimation);
        lockContainerHeight(SmileyPickerUtility.getAppContentHeight(this));

    }

    public void hideSmileyPicker(boolean showKeyBoard) {
        if (this.mSmiley.isShown()) {
            if (showKeyBoard) {
                //this time softkeyboard is hidden
                LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) this
                        .mContainer.getLayoutParams();
                localLayoutParams.height = mSmiley.getTop();
                localLayoutParams.weight = 0.0F;
                this.mSmiley.hide(this);

                SmileyPickerUtility.showKeyBoard(mContentEt);
                mContentEt.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unlockContainerHeightDelayed();
                    }
                }, 200L);
            } else {
                this.mSmiley.hide(this);
                unlockContainerHeightDelayed();
            }
        }

    }

    private void lockContainerHeight(int paramInt) {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) this.mContainer
                .getLayoutParams();
        localLayoutParams.height = paramInt;
        localLayoutParams.weight = 0.0F;
    }

    public void unlockContainerHeightDelayed() {

        ((LinearLayout.LayoutParams) EditMessageActivity.this.mContainer.getLayoutParams()).weight
                = 1.0F;

    }


    /**
     * On taking a photo
     */
    private void onTakePhoto(){
        mImageFileUri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new ContentValues());
        if (mImageFileUri != null) {
            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageFileUri);
            if (IntentUtil.isIntentSafe(EditMessageActivity.this, i)) {
                startActivityForResult(i, IUploadImage.SELECT_BY_TAKE_PHOTO);
            } else {
                Toast.makeText(EditMessageActivity.this,
                        getString(R.string.dont_have_camera_app), Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(EditMessageActivity.this, getString(R.string.cant_insert_album),
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * On select album
     */
    private void onGallary(){
        // support for android kitkat 4.4
        /**
        if (SystemUtil.isKK()) {
            //
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, );
        } else {
            Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(choosePictureIntent, IUploadImage.SELECT_BY_ALBUM);
        }
         **/

        Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(choosePictureIntent, IUploadImage.SELECT_BY_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == IUploadImage.SELECT_BY_ALBUM){
                onResultByGallary(data);
            }else if(requestCode == IUploadImage.SELECT_BY_TAKE_PHOTO){
                onResultByTake(data);
            }else if(requestCode == BROWSE_PIC){
                boolean deleted = data.getBooleanExtra("deleted", false);
                if (deleted) {
                    mPicPath = "";
                    mMediaIv.setImageBitmap(null);
                }
            }
        }else{
            AppContext.commonLog.i("resultCode is " + resultCode);
        }
    }

    public void onResultByGallary(Intent intent) {
        // TODO Auto-generated method stub
        String picPath = getPicPathFromUri(intent.getData(), this);
        enablePicture(picPath);
    }



    public void onResultByTake(Intent data) {
        // TODO Auto-generated method stub
        mPicPath = getPicPathFromUri(mImageFileUri, this);
        enablePicture(mPicPath);
    }

    public  String getPicPathFromUri(Uri uri, Activity activity) {
        String value = uri.getPath();

        if (value.startsWith("/external")) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else {
            return value;
        }
    }

    private void enablePicture(String picPath) {
        Bitmap bitmap = ImageUtil.getWriteWeiboPictureThumblr(picPath);
        if (bitmap != null) {
            mMediaIv.setImageBitmap(bitmap);
            mPicPath = picPath;
        }
    }

    public class UploadImageTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            if(mPicPath != null){
                String loadPicPath = ImageUtil.compressPic(EditMessageActivity.this, mPicPath, 3);
                File f = new File(loadPicPath);
                mImageUrl = BCSService.uploadImageByFile(f);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mMessage.setMediaUrl(mImageUrl);
            postTextMessage();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        AppContext.commonLog.i("Location changed");
        setBusy(false);
        if(aMapLocation == null){
            AppContext.commonLog.i("Get user's location error");
            return;
        }
        String locationDesc = aMapLocation.getProvince() + " " + aMapLocation.getCity() + aMapLocation.getDistrict();
        AppContext.commonLog.i(locationDesc + " " + aMapLocation.getLongitude() + " " + aMapLocation.getLatitude());
        mLocationTv.setText(locationDesc);
        AppContext.lastPosition = new Position(aMapLocation.getLatitude(),aMapLocation.getLongitude());
        AppContext.sharedPreferenceUtil.setLastPosition(AppContext.lastPosition);
        // Cancel refresh location
        mLocationManagerProxy.destory();
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


}
