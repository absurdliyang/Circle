package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.ui.activity.UserMessageActivity;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

/**
 * Created by absurd on 14-3-28.
 */
public class UserMessageListFragment extends MessageListFragment {

    private UserMessageActivity mUserMessageActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mUserMessageActivity = (UserMessageActivity)getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<Message> callback) {
        if(AppContext.auth != null) {
            mMessageService.getMessageByUser(pageIndex, mUserMessageActivity.userId,false, callback);
        }else{
            AppContext.commonLog.i("AppContext auth is null!");
        }
    }

}
