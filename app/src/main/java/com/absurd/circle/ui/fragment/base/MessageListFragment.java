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
public class MessageListFragment extends Fragment{
    private CommonLog mLog = LogFactory.createLog(AppConstant.TAG);
    protected PullToRefreshListView mContentLv;
    protected MessageAdapter mAdapter;

    private LoadingFooter mLoadingFooter;

    protected MessageService mMessageService;

    private int mCurrentPageIndex = 0;

    public static List<Message> messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //mHomeActivity = (HomeActivity)getActivity();
        mMessageService = new MessageService();
        View rootView = inflater.inflate(R.layout.item_list, null);
        // Init UI
        mContentLv = (PullToRefreshListView)rootView.findViewById(R.id.lv_content);
        mAdapter = new MessageAdapter(getActivity());
        mLoadingFooter = new LoadingFooter(getActivity());
        //mContentLv.addFooterView(mLoadingFooter.getView());
        mContentLv.setAdapter(mAdapter);
        mContentLv.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                if(direction.equals(PullToRefreshBase.Mode.PULL_FROM_START)) {
                    if (state.equals(PullToRefreshBase.State.PULL_TO_REFRESH)) {
                        mContentLv.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                        mContentLv.getLoadingLayoutProxy().setRefreshingLabel("正在载入");
                        String label = DateUtils.formatDateTime(MessageListFragment.this.getActivity(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        mContentLv.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                        refreshTranscation();
                    }
                }else if(direction.equals(PullToRefreshBase.Mode.PULL_FROM_END)){
                    if (state.equals(PullToRefreshBase.State.PULL_TO_REFRESH)) {
                        nextPageTransaction();
                    }
                }
            }
        });


        /**
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
         **/
        mContentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IntentUtil.startActivity(MessageListFragment.this.getActivity(), MessageDetailActivity.class,"messageIndexId",i);

            }
        });
        // Get Data
        if(AppContext.lastPosition != null) {
            refreshTranscation();
        }else{
            Toast.makeText(this.getActivity(),"Last Position is null",Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }



    private TableQueryCallback<Message> refreshCallBack = new TableQueryCallback<Message>() {
        @Override
        public void onCompleted(List<Message> result, int count, Exception exception, ServiceFilterResponse response) {
            /**
            if(mAttacher.isRefreshing()){
                mAttacher.setRefreshComplete();
            }
             **/
            if (result == null) {
                if (exception != null) {
                    exception.printStackTrace();
                    AppContext.commonLog.i("Get NearMessage error!");
                }
            } else {
                handleResult(result);
                getAdapter().setItems(result);
                MessageListFragment.messages = result;
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
                    AppContext.commonLog.i("Get message error!");
                }
            } else {
                AppContext.commonLog.i("result ---- > " + result);
                getAdapter().addItems(result);
                MessageListFragment.messages = getAdapter().getItems();
            }
        }
    };


    protected MessageAdapter getAdapter(){
        /**
        HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter)mContentLv.getAdapter();
        if(headerAdapter != null){
            return (MessageAdapter) headerAdapter.getWrappedAdapter();
        }

        return null;
         **/
        return mAdapter;
    }


    public void refreshTranscation(){
        //mContentLv.smoothScrollToPosition(0);
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

    protected void handleResult(List<Message> result){

    }

    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Keep the static member messages' consistency
        MessageListFragment.messages = getAdapter().getItems();
        // Keep the consistency between UI View and static member messages
        getAdapter().notifyDataSetChanged();
    }


}
