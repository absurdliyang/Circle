package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.service.MessageService;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.fragment.MessageDetailFragment;
import com.absurd.circle.ui.fragment.base.MessageListFragment;
import com.absurd.circle.util.IntentUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;


public class MessageDetailActivity extends BaseActivity {

    public int mIndexId;
    public static Message message;

    private MessageDetailFragment mMessageDetailFragment;

    public boolean mFlagFromUnReadComment;
    private int mMessageId;

    public MessageDetailActivity(){
        setRightBtnStatus(RIGHT_MORE_BTN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        Boolean flag = (Boolean)getIntent().getExtras().get("flag");
        if(flag == null) {
            // Set custom actionbar
            mIndexId = (Integer) getIntent().getSerializableExtra("messageIndexId");
            message = MessageListFragment.messages.get(mIndexId);
        }else{
            mFlagFromUnReadComment = flag;
            mMessageId = (Integer)getIntent().getExtras().get("messageId");
        }
        if(mFlagFromUnReadComment){
            MessageService service = new MessageService();
            setBusy(true);
            service.getMessageById(mMessageId, new TableQueryCallback<Message>() {
                @Override
                public void onCompleted(List<Message> result, int count, Exception exception, ServiceFilterResponse response) {
                    setBusy(false);
                    if(result == null){
                        if(exception != null){
                            exception.printStackTrace();
                        }
                        MessageDetailActivity.this.warning(R.string.message_not_exit);
                    }
                    if(!MessageDetailActivity.this.isFinishing()) {
                        MessageDetailActivity.message = result.get(0);
                        mMessageDetailFragment = new MessageDetailFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.flyt_comment_container, mMessageDetailFragment)
                                .commit();
                    }
                }
            });
        }else {
            mMessageDetailFragment = new MessageDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flyt_comment_container, mMessageDetailFragment)
                    .commit();
        }
    }

    @Override
    protected String actionBarTitle() {
        return "详情";
    }

    @Override
    public void onMoreClicked(View view) {
        super.onMoreClicked(view);
        mMessageDetailFragment.onMoreClicked(view);

    }


}
