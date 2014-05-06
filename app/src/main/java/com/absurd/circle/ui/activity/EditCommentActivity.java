package com.absurd.circle.ui.activity;

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
        mChat = AppContext.xmppConnectionManager.initChat(mParentComment.getUserId());
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

    private void sendComment(){
        Comment comment = new Comment();
        if(AppContext.lastPosition != null){
            comment.setLatitude(AppContext.lastPosition.getLatitude());
            comment.setLongitude(AppContext.lastPosition.getLongitude());
            //comment.setLocationDec("");
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
        /**
        comment.setDate(Calendar.getInstance().getTime());
        comment.setWeiboId("");
        comment.setMediaType(0);
        comment.setMediaUrl("");
         **/
        AppContext.xmppConnectionManager.send(mChat, comment, mParentComment.getUserId());
        CommentService service = new CommentService();
        setBusy(true);

        service.insertComment(comment, new TableOperationCallback<Comment>() {
            @Override
            public void onCompleted(Comment entity, Exception exception, ServiceFilterResponse response) {
                if(entity == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                    EditCommentActivity.this.finish();
                    warning(R.string.send_comment_failed);
                    return;
                }
                AppContext.commonLog.i(entity.toString());
                AppContext.commonLog.i("Add comment success!");
                MessageDetailActivity.message.incCommentCount();
                setBusy(false);
                warning(R.string.send_comment_success);
                EditCommentActivity.this.finish();
            }
        });
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
