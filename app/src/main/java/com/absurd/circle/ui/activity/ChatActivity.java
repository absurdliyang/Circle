package com.absurd.circle.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import com.absurd.circle.ui.widget.smileypicker.SmileyPicker;
import com.absurd.circle.ui.widget.smileypicker.SmileyPickerUtility;
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
    private ImageView mChatSmileyBtn;

    private SmileyPicker mSmiley;
    private RelativeLayout mContainer;

    private User mToUser;

    private UserMessageService mUserMessageService = new UserMessageService();
    private Chat mChat = null;

    private BroadcastReceiver mChatMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            AppContext.commonLog.i("Recieve a chat message");
            if(ChatService.NEW_MESSAGE_ACTION.equals(action)){
                UserMessage userMessage = (UserMessage)intent.getExtras().get("message");
                if(userMessage.getFromUserId().equals(mToUser.getUserId())) {
                    AppContext.commonLog.i("Receive a chat message");

                    String key = "userMessage " + userMessage.getFromUserId();
                    if(AppContext.unReadUserMessageNums.containsKey(key)) {
                        int num = AppContext.unReadUserMessageNums.get(key);
                        num--;
                        AppContext.unReadUserMessageNums.put(key, num);
                        AppContext.sharedPreferenceUtil.setUnReadUserMessageNum(userMessage.getFromUserId(),num);
                    }
                    AppContext.notificationNum--;
                    AppContext.sharedPreferenceUtil.setNotificationNum(AppContext.notificationNum);

                    userMessage.setState(1);
                    AppContext.cacheService.userMessageDBManager.updateUserMessageStateByUser(mToUser.getUserId());
                    AppContext.commonLog.i(userMessage.toString());
                    if (userMessage != null && userMessage.getToUserId().equals(AppContext.auth.getUserId())) {
                        recieveMessage(userMessage);
                    }
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

        String key = "userMessage " + mToUser.getUserId();
        if(AppContext.unReadUserMessageNums.containsKey(key)){
            int num = AppContext.unReadUserMessageNums.get(key);
            AppContext.notificationNum -= num;
            AppContext.unReadUserMessageNums.put(key,0);
            AppContext.sharedPreferenceUtil.setUnReadUserMessageNum(mToUser.getUserId(),0);
            AppContext.sharedPreferenceUtil.setNotificationNum(AppContext.notificationNum);
        }
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

        mContainer = (RelativeLayout)findViewById(R.id.chat_container);
        mContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mSmiley.isShown()) {
                    hideSmileyPicker(false);
                }
                if(SmileyPickerUtility.isKeyBoardShow(ChatActivity.this)) {
                    SmileyPickerUtility.hideSoftInput(mChatContentEt);
                }
                return true;
            }
        });
        mSmiley = (SmileyPicker)findViewById(R.id.chat_smileypicker);
        mSmiley.setEditText(this, ((LinearLayout) findViewById(R.id.chat_root_layout)), mChatContentEt);

        mChatSmileyBtn = (ImageView)findViewById(R.id.iv_chat_smiley);
        mChatSmileyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSmiley.isShown()) {
                    hideSmileyPicker(true);
                } else {
                    showSmileyPicker(
                            SmileyPickerUtility.isKeyBoardShow(ChatActivity.this));
                }
            }
        });

        mChatLv = (ListView)findViewById(R.id.lv_chat);
        mChatLv.setAdapter(new ChatAdapter(ChatActivity.this, mToUser));
        mChatLv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mSmiley.isShown()) {
                    hideSmileyPicker(false);
                }
                if(SmileyPickerUtility.isKeyBoardShow(ChatActivity.this)) {
                    SmileyPickerUtility.hideSoftInput(mChatContentEt);
                }
                return false;
            }
        });
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
        if(AppContext.xmppConnectionManager.getConnection() != null) {
            mChat = AppContext.xmppConnectionManager.initChat(mToUser.getId() + "");
        }else{
            //warning(R.string.chat_not_prepared_warning);
        }
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

        if(mChat != null) {
            mUserMessageService.insertUserMessage(userMessage, new TableOperationCallback<UserMessage>() {
                @Override
                public void onCompleted(UserMessage entity, Exception exception, ServiceFilterResponse response) {
                    if (entity == null) {
                        if (exception != null) {
                            exception.printStackTrace();
                        }
                    } else {
                        AppContext.commonLog.i("insert UserMessage success");

                        AppContext.xmppConnectionManager.send(mChat, entity, mToUser.getId() + "");

                        entity.setState(1);
                        AppContext.cacheService.userMessageDBManager.insertUserMessage(entity);

                    }
                }
            });
            getAdapter().addItem(userMessage);
            // ListView scroll to bottom
            smoothToBottom();
        }else{
            warning(R.string.chat_not_prepared_warning_send_failed);
        }

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


    @Override
    public void onBackPressed() {
        if (mSmiley.isShown()) {
            hideSmileyPicker(false);
        }
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

                SmileyPickerUtility.showKeyBoard(mChatContentEt);
                mChatContentEt.postDelayed(new Runnable() {
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
}
