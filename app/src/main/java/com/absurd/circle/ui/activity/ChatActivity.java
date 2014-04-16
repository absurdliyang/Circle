package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.data.service.UserMessageService;
import com.absurd.circle.ui.adapter.ChatAdapter;
import com.absurd.circle.ui.adapter.UserMessageAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;


/**
 * Created by absurd on 14-4-16.
 */
public class ChatActivity extends BaseActivity {

    private User mToUser;

    private ListView mChatLv;

    private UserMessageService mUserMessageService = new UserMessageService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToUser = (User)getIntent().getExtras().get("touser");

        mChatLv = (ListView)findViewById(R.id.lv_chat);
        mChatLv.setAdapter(new ChatAdapter(ChatActivity.this,mToUser));
        mUserMessageService.getUserMessages(AppContext.userId,mToUser.getUserId(),new TableQueryCallback<UserMessage>() {
            @Override
            public void onCompleted(List<UserMessage> result, int count, Exception exception, ServiceFilterResponse response) {
                if(result == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                }else{
                    ((BeanAdapter)mChatLv.getAdapter()).setItems(result);
                }
            }
        });

    }

    @Override
    protected String actionBarTitle() {
        return mToUser.getName();
    }
}
