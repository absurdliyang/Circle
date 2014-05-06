package com.absurd.circle.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.absurd.circle.data.util.JsonUtil;
import com.absurd.circle.im.service.ChatService;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.adapter.ChatAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.util.StringUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.util.List;
import java.util.Calendar;


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
            AppContext.commonLog.i("Recieve a chat message");
            if(ChatService.NEW_MESSAGE_ACTION.equals(action)){
                AppContext.commonLog.i("Receive a chat message");
                UserMessage userMessage = (UserMessage)intent.getExtras().get("message");
                userMessage.setState(1);
                AppContext.cacheService.userMessageDBManager.updateUserMessageStateByUser(mToUser.getUserId());
                AppContext.commonLog.i(userMessage.toString());
                if(userMessage != null && userMessage.getToUserId().equals(AppContext.auth.getUserId())){
                    recieveMessage(userMessage);
                }
            }
        }
    };

    public ChatActivity(){
        setRightBtnStatus(RIGHT_TEXT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToUser = (User)getIntent().getExtras().get("touser");
        mActionBarTitleTv.setText(mToUser.getName());

        initUI();
        initChat();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ChatService.NEW_MESSAGE_ACTION);
        registerReceiver(mChatMessageReceiver, intentFilter);
    }

    private void initUI(){

        mChatContentEt = (EditText)findViewById(R.id.et_chat_content);
        mChatSendBtn = (Button)findViewById(R.id.btn_chat_send);
        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        mChatLv = (ListView)findViewById(R.id.lv_chat);
        mChatLv.setAdapter(new ChatAdapter(ChatActivity.this,mToUser));
        List<UserMessage> historyMessages = AppContext.cacheService.userMessageDBManager.getUserHistoryMessages(mToUser.getUserId());
        AppContext.cacheService.userMessageDBManager.updateUserMessageStateByUser(mToUser.getUserId());
        getAdapter().setItems(historyMessages);
        smoothToBottom();

        /**
        mUserMessageService.getUserMessages(AppContext.userId,mToUser.getUserId(),new TableQueryCallback<UserMessage>() {
            @Override
                        exception.printStackTrace();
                    }
                }else{
                    ((BeanAdapter)mChatLv.getAdapter()).setItems(result);
                }
            }
        });
         **/
    }

    private void initChat(){
        mChat = AppContext.xmppConnectionManager.initChat(mToUser.getId() + "");
    }


    private void sendMessage(){
        UserMessage userMessage = new UserMessage();
        String chatContent = mChatContentEt.getText().toString();
        if(StringUtil.isEmpty(chatContent)){
            warning(R.string.chat_content_null);
            return;
        }
        mChatContentEt.setText("");
        userMessage.setContent(chatContent);
        userMessage.setToUserId(mToUser.getUserId());
        userMessage.setToUserName(mToUser.getName());
        userMessage.setFromUserId(AppContext.auth.getUserId());
        userMessage.setFromUserName(AppContext.auth.getName());
        userMessage.setDate(Calendar.getInstance().getTime());

        AppContext.xmppConnectionManager.send(mChat, userMessage, mToUser.getUserId());


        mUserMessageService.insertUserMessage(userMessage,new TableOperationCallback<UserMessage>() {
            @Override
            public void onCompleted(UserMessage entity, Exception exception, ServiceFilterResponse response) {
                if(entity == null){
                    if(exception != null){
                        exception.printStackTrace();
                    }
                }else{
                    AppContext.commonLog.i("insert UserMessage success");
                }
            }
        });
        userMessage.setState(1);
        AppContext.cacheService.userMessageDBManager.insertUserMessage(userMessage);

        getAdapter().addItem(userMessage);
        // ListView scroll to bottom
        smoothToBottom();
    }


    private void recieveMessage(UserMessage userMessage){
        getAdapter().addItem(userMessage);
        // ListView scroll to bottom
        smoothToBottom();
        mChatLv.invalidateViews();
    }


    private void smoothToBottom(){
        mChatLv.setSelection(getAdapter().getItems().size() - 1);
    }


    private BeanAdapter getAdapter(){
        return (BeanAdapter)mChatLv.getAdapter();
    }


    @Override
    protected String actionBarTitle() {
        return "";
    }

    @Override
    protected String setRightBtnTxt() {
        return "清除记录";
    }

    @Override
    public void onRightBtnClicked(View view) {
        super.onRightBtnClicked(view);
        AppContext.cacheService.userMessageDBManager.deleteUserHistory(mToUser.getUserId());
        getAdapter().deleteAllItems();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mChatMessageReceiver);
    }

}
