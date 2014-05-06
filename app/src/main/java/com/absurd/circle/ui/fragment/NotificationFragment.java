package com.absurd.circle.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.ui.activity.ChatActivity;
import com.absurd.circle.ui.activity.UnReadCommentActivity;
import com.absurd.circle.ui.activity.UnReadPraiseActivity;
import com.absurd.circle.ui.adapter.UserMessageAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.ui.fragment.base.LocalRefreshableFragment;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

/**
 * Created by absurd on 14-4-19.
 */
public class NotificationFragment extends LocalRefreshableFragment<UserMessage> {
    //private NotificationService mNotificationService = new NotificationService();


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentLv.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    @Override
    protected BeanAdapter<UserMessage> setAdapter() {
        return new UserMessageAdapter(this.getActivity());
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<UserMessage> callback) {
        if(AppContext.auth != null) {
            //mNotificationService.getUserMessages(AppContext.auth.getUserId(),pageIndex, 10, callback);
            AppContext.cacheService.userMessageDBManager.getAllUserMessages();
        }
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
        return header;
    }


    @Override
    protected List<UserMessage> loadDataLocal(int pageIndex) {
        if(AppContext.auth != null) {
            List<UserMessage> allMessages = AppContext.cacheService.userMessageDBManager
                    .getAllNotificationUserMessages(AppContext.auth.getUserId());
            Map<String, UserMessage> userLatestDate = new HashMap<String,UserMessage>();
            for(UserMessage userMessage: allMessages){
                if(!userLatestDate.containsKey(userMessage.getFromUserId())){
                    userLatestDate.put(userMessage.getFromUserId(), userMessage);
                }else{
                    Calendar curretnCalendar = Calendar.getInstance();
                    curretnCalendar.setTime(userMessage.getDate());
                    Calendar latestCalendar = Calendar.getInstance();
                    latestCalendar.setTime(userLatestDate.get(userMessage.getFromUserId()).getDate());
                    if(curretnCalendar.compareTo(latestCalendar) == 1){
                        userLatestDate.put(userMessage.getFromUserId(), userMessage);
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
        User user = AppContext.cacheService.userDBManager.getUser(((UserMessage)mAdapter.getItem(i - 2)).getFromUserId());
        if(user != null){
            IntentUtil.startActivity(this.getActivity(), ChatActivity.class, "touser", user);
        }
    }
}
