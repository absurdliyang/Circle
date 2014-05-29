package com.absurd.circle.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.client.volley.RequestManager;
import com.absurd.circle.ui.activity.base.IProgressBarActivity;
import com.absurd.circle.ui.adapter.base.BeanAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-4-19.
 */
public abstract class RefreshableFragment<V> extends Fragment {
    protected PullToRefreshListView mContentLv;
    protected BeanAdapter<V> mAdapter;
    protected View mHeader;

    protected IProgressBarActivity mActivity;
    protected boolean mIsBusy = false;

    protected int mCurrentPageIndex = 0;


    public void configureContentLv(ListView listView){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_list, null);
        mActivity = (IProgressBarActivity)getActivity();
        mHeader = initHeader();
        mAdapter = setAdapter();
        configurePullToRefreshView(rootView);
        ListView listView = mContentLv.getRefreshableView();
        configureContentLv(listView);
        refreshTranscation();
        return rootView;
    }


    protected void configurePullToRefreshView(View view){
        mContentLv = (PullToRefreshListView)view.findViewById(R.id.lv_content);
        mContentLv.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                if(direction.equals(PullToRefreshBase.Mode.PULL_FROM_START)) {
                    if (state.equals(PullToRefreshBase.State.PULL_TO_REFRESH)) {
                        mContentLv.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                        mContentLv.getLoadingLayoutProxy().setRefreshingLabel("正在载入");
                        mContentLv.getLoadingLayoutProxy().setReleaseLabel("下拉刷新");
                        String label = DateUtils.formatDateTime(RefreshableFragment.this.getActivity(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        mContentLv.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                        refreshTranscation();
                    }
                }else if(direction.equals(PullToRefreshBase.Mode.PULL_FROM_END)){
                    mContentLv.getLoadingLayoutProxy().setPullLabel("加载更多");
                    mContentLv.getLoadingLayoutProxy().setRefreshingLabel("正在载入");
                    mContentLv.getLoadingLayoutProxy().setReleaseLabel("加载更多");
                    if (state.equals(PullToRefreshBase.State.PULL_TO_REFRESH)) {
                        nextPageTransaction();
                    }
                }
            }
        });

        mContentLv.setAdapter(mAdapter);
        if(mHeader != null){
            ListView listView = mContentLv.getRefreshableView();
            listView.addHeaderView(mHeader);
        }
        mContentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onListItemClick(adapterView,view,i,l);
            }
        });
    }

    protected void onListItemClick(AdapterView<?> adapterView, View view, int i, long l){

    }

    protected BeanAdapter<V> getAdapter(){
        return mAdapter;
    }

    protected abstract BeanAdapter<V> setAdapter();

    protected View initHeader(){
        return null;
    }

    private TableQueryCallback<V> mRefreshCallBack = new TableQueryCallback<V>() {
        @Override
        public void onCompleted(List<V> result, int count, Exception exception, ServiceFilterResponse response) {
            if (result == null) {
                if (exception != null) {
                    exception.printStackTrace();
                    AppContext.commonLog.i("Query get list error!");
                }
            } else {
                handleResult(result);
                getAdapter().setItems(result);
                onRefreshCallback(result);
            }
        }
    };

    private TableQueryCallback<V> mNextPageCallBack = new TableQueryCallback<V>() {
        @Override
        public void onCompleted(List<V> result, int count, Exception exception, ServiceFilterResponse response) {
            if (result == null) {
                if (exception != null) {
                    exception.printStackTrace();
                    AppContext.commonLog.i("Query get list error!");
                }
            } else {
                AppContext.commonLog.i("result ---- > " + result);
                getAdapter().addItems(result);
                onNextPageCallback(result);
            }
        }
    };



    public void refreshTranscation(){
        if(!mIsBusy) {
            //mContentLv.smoothScrollToPosition(0);
            mCurrentPageIndex = 0;
            mActivity.setBusy(true);
            mIsBusy = true;
            loadData(mCurrentPageIndex, mRefreshCallBack);
        }
    }

    public void nextPageTransaction(){
        if(!mIsBusy) {
            mCurrentPageIndex++;
            mActivity.setBusy(true);
            mIsBusy = true;
            loadData(mCurrentPageIndex, mNextPageCallBack);
        }
    }

    protected abstract void loadData(int pageIndex,TableQueryCallback<V> callback);
    protected abstract void handleResult(List<V> result);

    protected void onRefreshCallback(List<V> result){
        mActivity.setBusy(false);
        mIsBusy = false;
    }

    protected void onNextPageCallback(List<V> result){
        mActivity.setBusy(false);
        mIsBusy = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }


}
