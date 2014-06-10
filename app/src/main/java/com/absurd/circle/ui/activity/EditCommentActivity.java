package com.absurd.circle.ui.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.absurd.circle.app.AppConfig;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.service.BCSService;
import com.absurd.circle.data.service.CommentService;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.view.IUploadImage;
import com.absurd.circle.ui.widget.smileypicker.SmileyPicker;
import com.absurd.circle.ui.widget.smileypicker.SmileyPickerUtility;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.StringUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import org.jivesoftware.smack.Chat;

import java.io.File;
import java.util.Calendar;

public class EditCommentActivity extends BaseActivity {

    private static final int  BROWSE_PIC = 202;

    private EditText mContentEt;
    private String mContent;
    private ImageView mMediaIv;


    private SmileyPicker mSmiley;
    private RelativeLayout mContainer;

    //private Message mMessage;
    private Comment mParentComment;
    private Comment mComment = new Comment();

    private boolean mIsbusy = false;

    private Chat mChat;

    private boolean mFlagFromUnReadComment = false;

    private Uri mImageFileUri;
    private String mPicPath;
    public EditCommentActivity(){
        setRightBtnStatus(RIGHT_TEXT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        if(getIntent().getExtras() != null) {
            mParentComment = (Comment) getIntent().getExtras().get("parentComment");
            Boolean flag = (Boolean)getIntent().getExtras().get("flag");
            if(flag != null){
                mFlagFromUnReadComment = flag;
            }
        }

        // Set custom actionbar
        setRightBtnStatus(RIGHT_TEXT);
        mContentEt = (EditText)findViewById(R.id.et_edit_comment_content);
        mContentEt.requestFocus();

        mContainer = (RelativeLayout)findViewById(R.id.edit_comment_container);
        mMediaIv = (ImageView)findViewById(R.id.iv_edit_comment_photo);

        mSmiley = (SmileyPicker)findViewById(R.id.edit_comment_smileypicker);
        mSmiley.setEditText(this, ((LinearLayout) findViewById(R.id.edit_comment_root_layout)), mContentEt);
        if(AppContext.xmppConnectionManager.getConnection() != null) {
            if(mParentComment != null){
                mChat = AppContext.xmppConnectionManager.initChat(mParentComment.getUser().getId()+ "");
            }else {
                mChat = AppContext.xmppConnectionManager.initChat(MessageDetailActivity.message.getUser().getId() + "");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mSmiley.isShown()) {
            hideSmileyPicker(false);
        }else {
            super.onBackPressed();
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
        ((LinearLayout.LayoutParams) this.mContainer.getLayoutParams()).weight
                = 1.0F;

    }



    @Override
    protected String actionBarTitle() {
        return "评论";
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
            sendComment();
        }
    }

    NotificationManager mNotificationManager = (NotificationManager)AppContext.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    public static final int NOTIFICATION_ID = 101;

    private void notificate(String title, String content){
        Notification notification = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
        notification.flags |= notification.FLAG_AUTO_CANCEL;
        //notification.contentView = new RemoteViews(getPackageName(), R.layout.layout_notification_progressbar);
        //notification.contentView.setProgressBar(R.id.pb_notification, 0, 0, true);
        notification.setLatestEventInfo(AppContext.getContext(), title, content, null);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void sendComment(){
        if(mChat == null){
            warning(R.string.chat_not_prepared_warning_send_failed);
            setBusy(false);
            mIsbusy = false;
            return;
        }else{
            if(!StringUtil.isEmpty(mPicPath)){
                postImageMessage();
            }else {
                postTextComment();
            }
        }
    }

    private void postImageMessage(){
        new UploadImageTask().execute();
    }

    private void postTextComment(){
        mComment.setUserId(AppContext.auth.getUserId());
        if(AppContext.lastPosition != null){
            mComment.setLatitude(AppContext.lastPosition.getLatitude());
            mComment.setLongitude(AppContext.lastPosition.getLongitude());
        }
        if(mParentComment != null){
            mComment.setParentId(mParentComment.getId());
            mComment.setParentText(mParentComment.getContent());
            mComment.setMessageId(mParentComment.getMessageId());
            mComment.setToUserId(mParentComment.getUserId());
        }else {
            if (MessageDetailActivity.message != null) {
                mComment.setMessageId(MessageDetailActivity.message.getId());
                mComment.setToUserId(MessageDetailActivity.message.getUserId());
            }
        }

        mComment.setContent(mContent);
        mComment.setDate(Calendar.getInstance().getTime());
        mComment.setWeiboId("");
        mComment.setMediaType(0);

        CommentService service = new CommentService();

        String title = AppContext.getContext().getString(R.string.notification_sending_message);
        notificate(title, mComment.getContent());

        service.insertComment(mComment, new TableOperationCallback<Comment>() {
            @Override
            public void onCompleted(Comment entity, Exception exception, ServiceFilterResponse response) {
                mNotificationManager.cancel(NOTIFICATION_ID);
                if (entity == null) {
                    if (exception != null) {
                        exception.printStackTrace();
                    }
                    //warning(R.string.send_comment_failed);
                    Toast.makeText(AppContext.getContext(), R.string.send_comment_failed, Toast.LENGTH_SHORT).show();
                    return;
                }
                AppContext.commonLog.i(entity.toString());
                AppContext.commonLog.i("Add mComment success!");
                if(!mFlagFromUnReadComment) {
                    MessageDetailActivity.message.incCommentCount();
                }
                setBusy(false);
                mIsbusy = false;
                //warning(R.string.send_comment_success);
                Toast.makeText(AppContext.getContext(), R.string.send_comment_success, Toast.LENGTH_SHORT).show();

                if(mParentComment == null) {
                    AppContext.xmppConnectionManager.send(mChat, mComment, MessageDetailActivity.message.getUser().getId() + "");
                }else{
                    AppContext.xmppConnectionManager.send(mChat, mComment, mParentComment.getUser().getId() + "");
                }
            }
        });
        EditCommentActivity.this.finish();
    }

    private boolean invalidateContent(){
        mContent = mContentEt.getText().toString();
        if(StringUtil.isEmpty(mContent)){
            Toast.makeText(this,R.string.warning_comment_content_null,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onBtnClick(View view){
        switch(view.getId()) {
            case R.id.iv_edit_comment_btn_emotion:
                if (mSmiley.isShown()) {
                    hideSmileyPicker(true);
                } else {
                    showSmileyPicker(
                            SmileyPickerUtility.isKeyBoardShow(this));
                }
                break;
            case R.id.iv_edit_comment_btn_galley:
                onGallary();
                break;
            case R.id.iv_edit_comment_btn_photo:
                onTakePhoto();
                break;

        }
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
            if (IntentUtil.isIntentSafe(EditCommentActivity.this, i)) {
                startActivityForResult(i, IUploadImage.SELECT_BY_TAKE_PHOTO);
            } else {
                Toast.makeText(EditCommentActivity.this,
                        getString(R.string.dont_have_camera_app), Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(EditCommentActivity.this, getString(R.string.cant_insert_album),
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


    private String mImageUrl;

    public class UploadImageTask extends AsyncTask<Void,Void,Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String title = AppContext.getContext().getString(R.string.notificaiont_uploading_pic);
            notificate(title, mComment.getContent());
            setBusy(false);
            EditCommentActivity.this.finish();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(mPicPath != null){
                String loadPicPath = ImageUtil.compressPic(EditCommentActivity.this, mPicPath, AppConfig.COMPRESS_PIC_QUALITY);
                File f = new File(loadPicPath);
                mImageUrl = BCSService.uploadImageByFile(f);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mComment.setMediaUrl(mImageUrl);
            mNotificationManager.cancel(NOTIFICATION_ID);
            postTextComment();
        }
    }

}
