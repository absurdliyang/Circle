package com.absurd.circle.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.service.CommentService;
import com.absurd.circle.ui.fragment.base.MessageListFragment;
import com.absurd.circle.util.StringUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

public class EditCommentActivity extends BaseActivity{

    private EditText mContentEt;
    private String mContent;

    //private Message mMessage;
    private Comment mParentComment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        //mMessage = (Message)getIntent().getExtras().get("message");
        if(getIntent().getExtras() != null)
            mParentComment = (Comment)getIntent().getExtras().get("parentComment");
        // Set custom actionbar
        setRightBtnStatus(RIGHT_TEXT);
        mContentEt = (EditText)findViewById(R.id.et_edit_comment_content);
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
        if(invalidateContent()){
            sendComment();
        }
    }

    private void sendComment(){
        Comment comment = new Comment();
        if(AppContext.lastPosition != null){
            comment.setLatitude(AppContext.lastPosition.getLatitude());
            comment.setLongitude(AppContext.lastPosition.getLongitude());
            comment.setLocationDec("");
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

        CommentService service = new CommentService();
        service.insertComment(comment, new TableOperationCallback<Comment>() {
            @Override
            public void onCompleted(Comment entity, Exception exception, ServiceFilterResponse response) {
                if(entity == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                    return;
                }
                AppContext.commonLog.i(entity.toString());
                AppContext.commonLog.i("Add comment success!");
                MessageDetailActivity.message.incCommentCount();
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

    }
}
