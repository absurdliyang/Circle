package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.ui.fragment.MessageDetailFragment;
import com.absurd.circle.ui.fragment.base.MessageListFragment;
import com.absurd.circle.ui.view.ItemDialog;

import java.util.ArrayList;
import java.util.List;


public class MessageDetailActivity extends BaseActivity {

    public static Message message;

    private MessageDetailFragment mMessageDetailFragment;

    public MessageDetailActivity(){
        setRightBtnStatus(RIGHT_MORE_BTN);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        // Set custom actionbar
        int indexId = (Integer)getIntent().getSerializableExtra("messageIndexId");
        message = MessageListFragment.messages.get(indexId);

        mMessageDetailFragment = new MessageDetailFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flyt_comment_container, mMessageDetailFragment)
                .commit();
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
