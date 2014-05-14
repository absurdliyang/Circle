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

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.ui.activity.UserProfileActivity;
import com.absurd.circle.ui.activity.base.IProgressBarActivity;
import com.absurd.circle.ui.adapter.UserAdapter;
import com.absurd.circle.util.IntentUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by absurd on 14-3-28.
 */
public abstract class UserListFragment<V> extends Fragment {
    public static final int MODE_NET = 1;
    public static final int MODE_LOCAL = 2;
    private int mMode = 1;

    private PullToRefreshListView mContentLv;
    private UserAdapter mAdapter;

    protected UserService mUserService;

    private int mCurrentPageIndex = 0;

    private IProgressBarActivity mActivity;
    protected boolean mIsbusy = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (IProgressBarActivity)getActivity();
        mUserService = new UserService();
        View rootView = inflater.inflate(R.layout.item_list, null);
        // Init UI
        mContentLv = (PullToRefreshListView)rootView.findViewById(R.id.lv_content);
        mAdapter = new UserAdapter(getActivity());
        mContentLv.setAdapter(mAdapter);

        mContentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IntentUtil.startActivity(UserListFragment.this.getActivity(), UserProfileActivity.class, "user", (User)getAdapter().getItem(i-1));
            }
        });

        mContentLv.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                beforePullEvent(refreshView, state, direction);
                if(direction.equals(PullToRefreshBase.Mode.PULL_FROM_START)) {
                    if (state.equals(PullToRefreshBase.State.PULL_TO_REFRESH)) {
                        mContentLv.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                        mContentLv.getLoadingLayoutProxy().setRefreshingLabel("正在载入");
                        mContentLv.getLoadingLayoutProxy().setReleaseLabel("下拉刷新");
                        String label = DateUtils.formatDateTime(UserListFragment.this.getActivity(),
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
        // Init loading footer status as end
        refreshTranscation();
        return rootView;
    }


    private TableQueryCallback<V> refreshCallBack = new TableQueryCallback<V>() {
        @Override
        public void onCompleted(List<V> result, int count, Exception exception, ServiceFilterResponse response) {
            mActivity.setBusy(false);
            mIsbusy = false;
            if (result == null) {
                if (exception != null) {
                    exception.printStackTrace();
                    //Toast.makeText(UserListFragment.this.getActivity(), "getNearMessage error!", Toast.LENGTH_SHORT).show();
                }
            } else {
                List<User> userResult = handleResult(result);
                getAdapter().setItems(userResult);
            }
        }
    };

    private TableQueryCallback<V> nextPageCallBack = new TableQueryCallback<V>() {
        @Override
        public void onCompleted(List<V> result, int count, Exception exception, ServiceFilterResponse response) {
            mActivity.setBusy(false);
            mIsbusy = false;
            if (result == null) {
                if (exception != null) {
                    exception.printStackTrace();
                    //Toast.makeText(UserListFragment.this.getActivity(), "Get Users error!", Toast.LENGTH_SHORT).show();
                }
            } else {
                List<User> userResult = handleResult(result);
                getAdapter().addItems(userResult);
            }
        }
    };

    protected ListView getListView(){
        return mContentLv.getRefreshableView();
    }

    protected UserAdapter getAdapter(){
        return mAdapter;
    }

    protected int getCurrentPageIndex(){
        return mCurrentPageIndex;
    }


    public void refreshTranscation(){
        getListView().smoothScrollToPosition(0);
        mCurrentPageIndex = 0;
        if (mMode == MODE_NET) {
            if(!mIsbusy) {
                mIsbusy = true;
                mActivity.setBusy(true);
                loadDataFromNet(mCurrentPageIndex, refreshCallBack);
            }
        } else {
            List<V> resList = loadDataFromLocal(mCurrentPageIndex);
            List<User> userResult = handleResult(resList);
            getAdapter().setItems(userResult);
        }
    }

    public void nextPageTransaction(){
        if(!mIsbusy) {
            mIsbusy  = true;
            mCurrentPageIndex++;
            if (mMode == MODE_NET) {
                if(!mIsbusy) {
                    mActivity.setBusy(true);
                    mIsbusy = true;
                    loadDataFromNet(mCurrentPageIndex, nextPageCallBack);
                }
            } else {
                List<V> resList = loadDataFromLocal(mCurrentPageIndex);
                List<User> userResult = handleResult(resList);
                getAdapter().addItems(userResult);
            }
        }
    }

    protected void setMode(int mode){
        this.mMode = mode;
    }

    protected int getMode(){
        return mMode;
    }

    protected abstract List<User> handleResult(List<V> result);

    protected abstract void loadDataFromNet(int pageIndex, TableQueryCallback<V> callback);

    protected abstract List<V> loadDataFromLocal(int pageIndex);

    protected abstract void beforePullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction);
}
