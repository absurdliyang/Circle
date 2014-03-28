package com.absurd.circle.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.model.Position;
import com.absurd.circle.data.service.MessageService;
import com.absurd.circle.ui.view.IUploadImage;
import com.absurd.circle.ui.view.PhotoFragment;
import com.absurd.circle.util.StringUtil;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditMessageActivity extends BaseActivity implements AMapLocationListener{

    private LocationManagerProxy mLocationManagerProxy;

    private TextView mLocationTv;
    private ImageView mMediaIv;
    private CheckBox mIsAnonyCb;
    private EditText mTitleEt;
    private EditText mContentEt;

    private String mTitle;
    private String mContent;

    private int mContentType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentType = (Integer)getIntent().getExtras().get("contentType");

        setContentView(R.layout.activity_edit_message);
        mLocationTv = (TextView)findViewById(R.id.tv_edit_msg_location);
        mMediaIv = (ImageView)findViewById(R.id.iv_edit_msg_photo);
        mIsAnonyCb = (CheckBox)findViewById(R.id.cb_edit_msg_is_anony);
        mTitleEt = (EditText)findViewById(R.id.et_edit_msg_title);
        mContentEt = (EditText)findViewById(R.id.et_edit_msg_content);

        // Get user's current location
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.requestLocationUpdates(LocationProviderProxy.AMapNetwork,5000,10,this);
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
        if(invalidateTitle() && invalidateContent()){
            sendMessage();
        }else{
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean invalidateTitle(){
        mTitle = mTitleEt.getText().toString();
        if(StringUtil.isEmpty(mTitle)){
            return false;
        }
        return true;
    }

    private boolean invalidateContent(){
        mContent = mContentEt.getText().toString();
        if(StringUtil.isEmpty(mContent)){
            return false;
        }
        mContent = mContent.trim();
        mContent = "#" + mTitle + "# " + mContent;
        return true;
    }


    private void sendMessage(){
        Message message = new Message();
        message.setMessagetType(0);
        if(mIsAnonyCb.isChecked()) {
            message.setMessagetType(1);
        }

        message.setContentType(mContentType);
        message.setTitle(mTitle);
        message.setContent(mContent);
        if(AppContext.lastPosition != null){
            message.setLatitude(AppContext.lastPosition.getLatitude());
            message.setLongitude(AppContext.lastPosition.getLongitude());
            message.setLocationDec("");
        }
        MessageService messageService = new MessageService();
        messageService.addMessage(message,new TableOperationCallback<Message>() {
            @Override
            public void onCompleted(Message entity, Exception exception, ServiceFilterResponse response) {
                if(entity == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                }else{
                    AppContext.commonLog.i(entity.getContent());
                }
            }
        });


    }

    public void onBtnClick(View view){
        switch(view.getId()){
            case R.id.iv_edit_msg_btn_emotion:
                break;
            case R.id.iv_edit_msg_btn_galley:
                onGallary();
                break;
            case R.id.iv_edit_msg_btn_photo:
                onTakePhoto();
                break;
            default:
                break;
        }
    }


    /**
     * On taking a photo
     */
    private void onTakePhoto(){
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if(storageState.equals(Environment.MEDIA_MOUNTED)){
            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConstant.TAKE_PHOTO_PATH;
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }
        if(StringUtil.isEmpty(savePath)){
            Toast.makeText(this, "Create save photo path error!", Toast.LENGTH_SHORT).show();
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "circle_" + timeStamp + ".jpg";
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (PhotoFragment.hasImageCaptureBug()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/tmp")));
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        PhotoFragment.uri = uri;
        PhotoFragment.data = intent;
        startActivityForResult(intent, IUploadImage.SELECT_BY_TAKE_PHOTO);
    }

    /**
     * On select album
     */
    private void onGallary(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IUploadImage.SELECT_BY_ALBUM);
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
            }
        }else{
            AppContext.commonLog.i("resultCode is " + resultCode);
        }
    }

    public void onResultByGallary(Intent data) {
        // TODO Auto-generated method stub
        if(data == null){
            Toast.makeText(this, "data is null", Toast.LENGTH_SHORT).show();
            return;
        }
        // Get photo by uri
        ContentResolver resolver = getContentResolver();
        Uri uri = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver,uri);
            if(bitmap != null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                mMediaIv.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onResultByTake(Intent data) {
        // TODO Auto-generated method stub
        Bundle extras = data.getExtras();
        if(extras != null){
            Bitmap photo = extras.getParcelable("data");
            if(photo != null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                mMediaIv.setImageBitmap(photo);
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        AppContext.commonLog.i("Location changed");
        if(aMapLocation == null){
            AppContext.commonLog.i("Get user's location error");
        }
        mLocationTv.setText(aMapLocation.getLatitude() + aMapLocation.getLongitude() + "");
        AppContext.sharedPreferenceUtil.setLastPosition(new Position(aMapLocation.getLatitude(),aMapLocation.getLongitude()));
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
