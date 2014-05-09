package com.absurd.circle.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.service.MessageService;
import com.absurd.circle.ui.activity.MessageDetailActivity;
import com.absurd.circle.ui.adapter.MessageAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.ui.view.LoadingFooter;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.LogFactory;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;


/**
 * Created by absurd on 14-3-12.
 */
public abstract class MessageListFragment extends RefreshableFragment<Message>{

    protected MessageService mMessageService = new MessageService();

    public static List<Message> messages;

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<Message> callback) {

    }

    @Override
    protected void handleResult(List<Message> result) {

    }

    @Override
    protected void onRefreshCallback(List<Message> result) {
        super.onRefreshCallback(result);
        MessageListFragment.messages = result;
    }

    @Override
    protected void onNextPageCallback(List<Message> result) {
        super.onNextPageCallback(result);
        MessageListFragment.messages = getAdapter().getItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Keep the static member messages' consistency
        MessageListFragment.messages = getAdapter().getItems();
        // Keep the consistency between UI View and static member messages
        getAdapter().notifyDataSetChanged();
    }

    @Override
    protected BeanAdapter<Message> setAdapter() {
        return new MessageAdapter(this.getActivity());
    }

    @Override
    protected void onListItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        super.onListItemClick(adapterView, view, i, l);
        IntentUtil.startActivity(MessageListFragment.this.getActivity(), MessageDetailActivity.class,"messageIndexId",i-1);
    }


}
