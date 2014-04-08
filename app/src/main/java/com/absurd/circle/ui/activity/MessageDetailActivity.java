package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.ui.fragment.MessageDetailFragment;


public class MessageDetailActivity extends BaseActivity {

    public Message message;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        // Set custom actionbar
        setRightBtnStatus(RIGHT_MORE_BTN);
        message = (Message)getIntent().getSerializableExtra("messageId");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flyt_comment_container,new MessageDetailFragment())
                .commit();


    }

    @Override
    protected String actionBarTitle() {
        return "详情";
    }

    @Override
    public void onMoreClicked(View view) {
        super.onMoreClicked(view);

    }
}
