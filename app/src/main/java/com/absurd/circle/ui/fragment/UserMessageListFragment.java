package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.ui.activity.UserDynamicActivity;
import com.absurd.circle.ui.adapter.MessageAdapter;
import com.absurd.circle.ui.fragment.base.MessageListFragment;
import com.absurd.circle.util.StringUtil;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

/**
 * Created by absurd on 14-3-28.
 */
public class UserMessageListFragment extends MessageListFragment {

    private UserDynamicActivity mUserMessageActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mUserMessageActivity = (UserDynamicActivity)getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<Message> callback) {
        if(!StringUtil.isEmpty(mUserMessageActivity.userId)){
            if (AppContext.auth != null) {
                mMessageService.getMessageByUser(pageIndex, mUserMessageActivity.userId, false, callback);
            } else {
                AppContext.commonLog.i("AppContext auth is null!");
            }
        }else{
            mMessageService.getMessagesOfFollowUsers(pageIndex,callback);
        }

    }

}
