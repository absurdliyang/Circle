package com.absurd.circle.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.service.MessageService;
import com.absurd.circle.ui.activity.MessageDetailActivity;
import com.absurd.circle.ui.activity.RefreshableActivity;
import com.absurd.circle.ui.adapter.MessageAdapter;
import com.absurd.circle.ui.view.LoadingFooter;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.LogFactory;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by absurd on 14-3-12.
 */
public class MessageListFragment extends Fragment{
    private CommonLog mLog = LogFactory.createLog(AppConstant.TAG);
    private ListView mContentLv;
    private TextView mEmptyTv;

    private PullToRefreshAttacher mAttacher;
    private LoadingFooter mLoadingFooter;

    protected MessageService mMessageService;

    private int mCurrentPageIndex = 0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //mHomeActivity = (HomeActivity)getActivity();
        mMessageService = new MessageService();
        View rootView = inflater.inflate(R.layout.item_list, null);
        // Init UI
        mContentLv = (ListView)rootView.findViewById(R.id.lv_content);
        MessageAdapter adapter = new MessageAdapter(getActivity());
        mLoadingFooter = new LoadingFooter(getActivity());
        mContentLv.addFooterView(mLoadingFooter.getView());
        mContentLv.setAdapter(adapter);
        mContentLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if(absListView.getLastVisiblePosition() == absListView.getCount() - 1 && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    nextPageTransaction();
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }
        });
        mContentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IntentUtil.startActivity(MessageListFragment.this.getActivity(), MessageDetailActivity.class,"messageId",(Message)mContentLv.getAdapter().getItem(i));
            }
        });
        mEmptyTv = (TextView)rootView.findViewById(R.id.tv_empty);
        // Get Data
        if(AppContext.lastPosition != null) {
            refreshTranscation();
        }else{
            Toast.makeText(this.getActivity(),"Last Position is null",Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAttacher = ((RefreshableActivity)getActivity()).getAttacher();
        mAttacher.addRefreshableView(mContentLv,new PullToRefreshAttacher.OnRefreshListener() {
            @Override
            public void onRefreshStarted(View view) {
                refreshTranscation();
            }
        });

    }

    private TableQueryCallback<Message> refreshCallBack = new TableQueryCallback<Message>() {
        @Override
        public void onCompleted(List<Message> result, int count, Exception exception, ServiceFilterResponse response) {
            if(mLoadingFooter != null && mLoadingFooter.getState() == LoadingFooter.State.Loading){
                mLoadingFooter.setState(LoadingFooter.State.TheEnd);
            }
            if (result == null) {
                if (exception != null) {
                    exception.printStackTrace();
                    Toast.makeText(MessageListFragment.this.getActivity(), "getNearMessage error!", Toast.LENGTH_SHORT).show();
                }
            } else {
                HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter)mContentLv.getAdapter();
                ((MessageAdapter) headerAdapter.getWrappedAdapter()).setItems(result);
            }
        }
    };

    private TableQueryCallback<Message> nextPageCallBack = new TableQueryCallback<Message>() {
        @Override
        public void onCompleted(List<Message> result, int count, Exception exception, ServiceFilterResponse response) {
            if(mLoadingFooter != null && mLoadingFooter.getState() == LoadingFooter.State.Loading){
                mLoadingFooter.setState(LoadingFooter.State.TheEnd);
            }
            if (result == null) {
                if (exception != null) {
                    exception.printStackTrace();
                    Toast.makeText(MessageListFragment.this.getActivity(), "Get message error!", Toast.LENGTH_SHORT).show();
                }
            } else {
                HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter)mContentLv.getAdapter();
                ((MessageAdapter) headerAdapter.getWrappedAdapter()).addItems(result);
            }
        }
    };



    public void refreshTranscation(){
        mContentLv.smoothScrollToPosition(0);
        mCurrentPageIndex = 0;
        loadData(mCurrentPageIndex, refreshCallBack);
    }

    public void nextPageTransaction(){
        mCurrentPageIndex++;
        mLoadingFooter.setState(LoadingFooter.State.Loading);
        loadData(mCurrentPageIndex, nextPageCallBack);
    }

    protected void loadData(int pageIndex,TableQueryCallback<Message> callback){
    }

}
