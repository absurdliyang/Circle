package com.absurd.circle.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.im.service.ChatService;
import com.absurd.circle.ui.activity.ChatActivity;
import com.absurd.circle.ui.activity.UnReadCommentActivity;
import com.absurd.circle.ui.activity.UnReadPraiseActivity;
import com.absurd.circle.ui.activity.base.INotificationChangedListener;
import com.absurd.circle.ui.adapter.UserMessageAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.ui.fragment.base.LocalRefreshableFragment;
import com.absurd.circle.ui.view.LoadingFooter;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

/**
 * Created by absurd on 14-4-19.
 */
public class NotificationFragment extends LocalRefreshableFragment<UserMessage>
            implements INotificationChangedListener{

    private TextView mUnReadCommentNumTv;
    private TextView mUnReadPraiseNumTv;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentLv.setMode(PullToRefreshBase.Mode.DISABLED);

        if(ChatService.getInstance() != null) {
            ChatService.getInstance().setUnreadItemChangedListener(this);
        }
    }

    @Override
    public void configureContentLv(ListView listView) {
        super.configureContentLv(listView);
        listView.setDivider(AppContext.getContext().getResources().getDrawable(R.drawable.listview_divider));
        listView.setDividerHeight(1);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(28,0,28,0);
        mContentLv.setLayoutParams(params);

        mContentLv.setOnLastItemVisibleListener(null);
        mLoadingFooter.setState(LoadingFooter.State.TheEnd);
    }

    @Override
    protected BeanAdapter<UserMessage> setAdapter() {
        return new UserMessageAdapter(this.getActivity());
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<UserMessage> callback) {
    }

    @Override
    protected void handleResult(List<UserMessage> result) {

    }


    @Override
    protected View initHeader() {
        View header = LayoutInflater.from(this.getActivity()).inflate(R.layout.header_notification_list,null);
        ImageView commentBtn = (ImageView)header.findViewById(R.id.iv_header_comment);
        ImageView praiseBtn = (ImageView)header.findViewById(R.id.iv_header_praise);
        commentBtn.setImageBitmap(ImageUtil.roundBitmap(
                ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.notification_comment)).getBitmap()
        ));
        praiseBtn.setImageBitmap(ImageUtil.roundBitmap(
                ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.notification_praise)).getBitmap()
        ));
        header.findViewById(R.id.llyt_header_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(NotificationFragment.this.getActivity(), UnReadCommentActivity.class);
            }
        });
        header.findViewById(R.id.llyt_header_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(NotificationFragment.this.getActivity(), UnReadPraiseActivity.class);
            }
        });
        mUnReadCommentNumTv = (TextView)header.findViewById(R.id.tv_unread_comment_num);
        mUnReadPraiseNumTv = (TextView)header.findViewById(R.id.tv_unread_praise_num);
        updateNotificationNum();
        return header;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNotificationNum();
    }

    @Override
    public void onNotificationChanged() {
        updateNotificationNum();
    }

    public static final int UPDATE_UNREAD_ITEM_NUM = 4;

    private Handler mUpdateNotificationNumHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_UNREAD_ITEM_NUM:
                    if(AppContext.unReadCommentNum == 0){
                        mUnReadCommentNumTv.setVisibility(View.GONE);
                    }else{
                        mUnReadCommentNumTv.setVisibility(View.VISIBLE);
                        mUnReadCommentNumTv.setText(AppContext.unReadCommentNum + "");
                    }

                    if(AppContext.unReadPraiseNum == 0){
                        mUnReadPraiseNumTv.setVisibility(View.GONE);
                    }else{
                        mUnReadPraiseNumTv.setVisibility(View.VISIBLE);
                        mUnReadPraiseNumTv.setText(AppContext.unReadPraiseNum + "");
                    }
                    mAdapter.notifyDataSetChanged();
                    mContentLv.getRefreshableView().invalidateViews();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void updateNotificationNum(){
        Message message = new Message();
        message.what = UPDATE_UNREAD_ITEM_NUM;
        mUpdateNotificationNumHandler.sendMessage(message);
    }


    @Override
    protected List<UserMessage> loadDataLocal(int pageIndex) {
        if(AppContext.auth != null) {
            AppContext.commonLog.i("load Data local");
            List<UserMessage> allMessages = AppContext.cacheService.userMessageDBManager
                    .getAllUserMessages();
            Map<String, UserMessage> userLatestDate = new HashMap<String,UserMessage>();
            for(UserMessage userMessage: allMessages){
                String key = "";
                if(userMessage.getFromUserId().equals(AppContext.auth.getUserId())){
                    key = userMessage.getFromUserId() + userMessage.getToUserId();
                }else{
                    key = userMessage.getToUserId() + userMessage.getFromUserId();
                }
                if(!userLatestDate.containsKey(key)){
                    userLatestDate.put(key, userMessage);
                }else{
                    Calendar curretnCalendar = Calendar.getInstance();
                    curretnCalendar.setTime(userMessage.getDate());
                    Calendar latestCalendar = Calendar.getInstance();
                    latestCalendar.setTime(userLatestDate.get(key).getDate());
                    if(curretnCalendar.compareTo(latestCalendar) == 1){
                        userLatestDate.put(key, userMessage);
                    }
                }
            }
            List<UserMessage> result = new ArrayList<UserMessage>();
            java.util.Iterator it = userLatestDate.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                result.add((UserMessage) entry.getValue());
            }
            return result;
        }
        return new ArrayList<UserMessage>();
    }




    @Override
    protected void onListItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        super.onListItemClick(adapterView, view, i, l);
        if(i  >= 2) {
            UserMessage item = (UserMessage) mAdapter.getItem(i - 2);
            String userId = null;
            if(item.getFromUserId().equals(AppContext.auth.getUserId())) {
                userId = item.getToUserId();
            }else{
                userId = item.getFromUserId();
            }
            User user = AppContext.cacheService.userDBManager.getUser(userId);
            if (user != null) {
                IntentUtil.startActivity(this.getActivity(), ChatActivity.class, "touser", user);
            }
        }
    }

}
