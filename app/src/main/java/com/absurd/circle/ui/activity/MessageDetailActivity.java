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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        // Set custom actionbar
        setRightBtnStatus(RIGHT_MORE_BTN);
        int indexId = (Integer)getIntent().getSerializableExtra("messageIndexId");
        message = MessageListFragment.messages.get(indexId);

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
        List<String> items = new ArrayList<String>();
        items.add("倒叙查看评论");
        items.add("举报该信息");
        items.add("复制信息");
        final ItemDialog dialog = new ItemDialog(this,items);
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.cancel();
            }
        });
        dialog.show();
    }


}
