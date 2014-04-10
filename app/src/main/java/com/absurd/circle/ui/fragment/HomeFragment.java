package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.ui.activity.HomeActivity;
import com.absurd.circle.ui.adapter.MessageAdapter;
import com.absurd.circle.ui.fragment.base.MessageListFragment;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Message> messages = AppContext.cacheService.getAllMessages();
        if(messages != null && !messages.isEmpty()){
            HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter)mContentLv.getAdapter();
            ((MessageAdapter) headerAdapter.getWrappedAdapter()).setItems(messages);
            MessageListFragment.messages = messages;
        }
    }

    @Override
    protected void loadData(int pageIndex,TableQueryCallback<Message> callback){
        CategoryFragment categoryFragment = CategoryFragment.getInstance();
        mMessageService.getNearMessage(pageIndex, AppContext.lastPosition.getLatitude(), AppContext.lastPosition.getLongitude(),
                categoryFragment.distanceFilter * 1000 , categoryFragment.categoryFilter, categoryFragment.orderFilter, "1", callback);
    }


    @Override
    protected void handleResult(List<Message> result) {
        super.handleResult(result);
        AppContext.cacheService.deleteAllMessage();
        for(Message message : result){
            AppContext.cacheService.inserMessage(message);
        }
    }


}
