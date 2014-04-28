package com.absurd.circle.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.data.service.UserMessageService;
import com.absurd.circle.im.service.ChatService;
import com.absurd.circle.ui.adapter.ChatAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.util.List;


/**
 * Created by absurd on 14-4-16.
 */
public class ChatActivity extends BaseActivity {

    private EditText mChatContentEt;
    private Button mChatSendBtn;
    private ListView mChatLv;

    private User mToUser;

    private UserMessageService mUserMessageService = new UserMessageService();
    private Chat mChat = null;

    private BroadcastReceiver mChatMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(ChatService.NEW_MESSAGE_ACTION.equals(action)){
                AppContext.commonLog.i("Receive a chat message");
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToUser = (User)getIntent().getExtras().get("touser");
        mActionBarTitleTv.setText(mToUser.getName());


        registerReceiver(mChatMessageReceiver,new IntentFilter());
        initUI();

        mChat = AppContext.xmppConnectionManager.getConnection().getChatManager()
                .createChat("778692@incircle/Smack", null);
        AppContext.xmppConnectionManager.getConnection().getChatManager()
                .addChatListener(new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean b) {
                        chat.addMessageListener(new MessageListener() {
                            @Override
                            public void processMessage(Chat chat, Message message) {
                                AppContext.commonLog.i(message.getBody());
                            }
                        });
                    }
                });

    }

    private void initUI(){

        mChatContentEt = (EditText)findViewById(R.id.et_chat_content);
        mChatSendBtn = (Button)findViewById(R.id.btn_chat_send);
        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message();
                message.setBody("chat content");
                try {
                    mChat.sendMessage(message);
                } catch (XMPPException e) {
                    e.printStackTrace();
                    return;
                }
                AppContext.commonLog.i("Send message success");
            }
        });
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
        return "";
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mChatMessageReceiver);
    }


}
