package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.ui.activity.HomeActivity;
import com.absurd.circle.ui.fragment.base.MessageListFragment;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

/**
 * Created by absurd on 14-3-28.
 */
public class HomeFragment extends MessageListFragment {

    private HomeActivity mHomeActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHomeActivity = (HomeActivity)getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void loadData(int pageIndex,TableQueryCallback<Message> callback){
        mMessageService.getNearMessage(pageIndex, AppContext.lastPosition.getLatitude(), AppContext.lastPosition.getLongitude(),
                mHomeActivity.distanceFilter * 1000 , mHomeActivity.categoryFilter, true, "1", callback);
    }

}
