package com.absurd.circle.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.absurd.circle.app.R;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.ui.activity.UserDynamicActivity;
import com.absurd.circle.ui.activity.UserProfileActivity;
import com.absurd.circle.ui.adapter.UserAdapter;
import com.absurd.circle.ui.view.LoadingFooter;
import com.absurd.circle.util.IntentUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;


/**
 * Created by absurd on 14-3-28.
 */
public abstract class UserListFragment<V> extends Fragment {

    private ListView mContentLv;
    private TextView mEmptyTv;

    private LoadingFooter mLoadingFooter;

    protected UserService mUserService;

    private int mCurrentPageIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //mHomeActivity = (HomeActivity)getActivity();
        mUserService = new UserService();
        View rootView = inflater.inflate(R.layout.item_list, null);
        // Init UI
        mContentLv = (ListView)rootView.findViewById(R.id.lv_content);
        final UserAdapter adapter = new UserAdapter(getActivity());
        mLoadingFooter = new LoadingFooter(getActivity());
        mContentLv.addFooterView(mLoadingFooter.getView());
        mContentLv.setAdapter(adapter);
        /**
        mContentLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (absListView.getLastVisiblePosition() == absListView.getCount() - 1 && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    nextPageTransaction();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mLoadingFooter.getState() == LoadingFooter.State.Loading
                        || mLoadingFooter.getState() == LoadingFooter.State.TheEnd)
                    return;
                if (firstVisibleItem + visibleItemCount >= totalItemCount
                    && totalItemCount != 0
                    && totalItemCount != mContentLv.getHeaderViewsCount()
                    + mContentLv.getFooterViewsCount() && adapter.getCount() > 0) {
                    nextPageTransaction();
                }

            }
        });
         **/
        mContentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IntentUtil.startActivity(UserListFragment.this.getActivity(), UserProfileActivity.class, "user", (User)mContentLv.getAdapter().getItem(i));
            }
        });
        mEmptyTv = (TextView)rootView.findViewById(R.id.tv_empty);
        // Init loading footer status as end
        mLoadingFooter.setState(LoadingFooter.State.TheEnd);
        refreshTranscation();
        return rootView;
    }


    private TableQueryCallback<V> refreshCallBack = new TableQueryCallback<V>() {
        @Override
        public void onCompleted(List<V> result, int count, Exception exception, ServiceFilterResponse response) {
            if(mLoadingFooter != null && mLoadingFooter.getState() == LoadingFooter.State.Loading){
                mLoadingFooter.setState(LoadingFooter.State.TheEnd);
            }
            if (result == null) {
                if (exception != null) {
                    exception.printStackTrace();
                    Toast.makeText(UserListFragment.this.getActivity(), "getNearMessage error!", Toast.LENGTH_SHORT).show();
                }
            } else {
                List<User> userResult = handleResult(result);
                HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter)mContentLv.getAdapter();
                ((UserAdapter) headerAdapter.getWrappedAdapter()).setItems(userResult);
            }
        }
    };

    private TableQueryCallback<V> nextPageCallBack = new TableQueryCallback<V>() {
        @Override
        public void onCompleted(List<V> result, int count, Exception exception, ServiceFilterResponse response) {
            if(mLoadingFooter != null && mLoadingFooter.getState() == LoadingFooter.State.Loading){
                mLoadingFooter.setState(LoadingFooter.State.TheEnd);
            }
            if (result == null) {
                if (exception != null) {
                    exception.printStackTrace();
                    Toast.makeText(UserListFragment.this.getActivity(), "Get Users error!", Toast.LENGTH_SHORT).show();
                }
            } else {
                List<User> userResult = handleResult(result);
                HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter)mContentLv.getAdapter();
                ((UserAdapter) headerAdapter.getWrappedAdapter()).addItems(userResult);
            }
        }
    };

    public void refreshTranscation(){
        mContentLv.smoothScrollToPosition(0);
        //mCurrentPageIndex = 0;
        loadData(mCurrentPageIndex, refreshCallBack);
    }

    public void nextPageTransaction(){
        mCurrentPageIndex++;
        mLoadingFooter.setState(LoadingFooter.State.Loading);
        //loadData(mCurrentPageIndex, nextPageCallBack);
    }

    protected abstract List<User> handleResult(List<V> result);

    protected abstract void loadData(int pageIndex,TableQueryCallback<V> callback);

}
