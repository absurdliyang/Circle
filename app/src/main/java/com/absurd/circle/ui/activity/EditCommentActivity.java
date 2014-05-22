package com.absurd.circle.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.service.CommentService;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.widget.smileypicker.SmileyPicker;
import com.absurd.circle.ui.widget.smileypicker.SmileyPickerUtility;
import com.absurd.circle.util.StringUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import org.jivesoftware.smack.Chat;
import java.util.Calendar;

public class EditCommentActivity extends BaseActivity {

    private EditText mContentEt;
    private String mContent;

    private SmileyPicker mSmiley;
    private RelativeLayout mContainer;

    //private Message mMessage;
    private Comment mParentComment;

    private boolean mIsbusy = false;

    private Chat mChat;

    public EditCommentActivity(){
        setRightBtnStatus(RIGHT_TEXT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        if(getIntent().getExtras() != null)
            mParentComment = (Comment)getIntent().getExtras().get("parentComment");

        // Set custom actionbar
        setRightBtnStatus(RIGHT_TEXT);
        mContentEt = (EditText)findViewById(R.id.et_edit_comment_content);
        mContentEt.requestFocus();

        mContainer = (RelativeLayout)findViewById(R.id.edit_comment_container);

        mSmiley = (SmileyPicker)findViewById(R.id.edit_comment_smileypicker);
        mSmiley.setEditText(this, ((LinearLayout) findViewById(R.id.edit_comment_root_layout)), mContentEt);
        if(AppContext.xmppConnectionManager.getConnection() != null) {
            mChat = AppContext.xmppConnectionManager.initChat(MessageDetailActivity.message.getUser().getId() + "");
        }
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
            final Comment comment = new Comment();
            comment.setUserId(AppContext.auth.getUserId());
            if(AppContext.lastPosition != null){
                comment.setLatitude(AppContext.lastPosition.getLatitude());
                comment.setLongitude(AppContext.lastPosition.getLongitude());
            }
            if(MessageDetailActivity.message != null){
                comment.setMessageId(MessageDetailActivity.message.getId());
                comment.setToUserId(MessageDetailActivity.message.getUserId());
            }
            comment.setContent(mContent);

            if(mParentComment != null){
                comment.setParentId(mParentComment.getId());
                comment.setParentText(mParentComment.getContent());
            }
            comment.setDate(Calendar.getInstance().getTime());
            comment.setWeiboId("");
            comment.setMediaType(0);
            comment.setMediaUrl("");

            CommentService service = new CommentService();

            String title = AppContext.getContext().getString(R.string.notification_sending_message);
            notificate(title,comment.getContent());

            service.insertComment(comment, new TableOperationCallback<Comment>() {
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
                    AppContext.commonLog.i("Add comment success!");
                    MessageDetailActivity.message.incCommentCount();
                    setBusy(false);
                    mIsbusy = false;
                    //warning(R.string.send_comment_success);
                    Toast.makeText(AppContext.getContext(), R.string.send_comment_success, Toast.LENGTH_SHORT).show();

                    AppContext.xmppConnectionManager.send(mChat, comment, MessageDetailActivity.message.getUser().getId() + "");
                }
            });
            EditCommentActivity.this.finish();
        }
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
        if (mSmiley.isShown()) {
            hideSmileyPicker(true);
        } else {
            showSmileyPicker(
                    SmileyPickerUtility.isKeyBoardShow(this));
        }
    }
}
