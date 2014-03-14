package com.absurd.circle.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.R;
import com.absurd.circle.core.bean.Message;
import com.absurd.circle.core.bean.MessagePage;
import com.absurd.circle.core.service.MessageService;
import com.absurd.circle.ui.activity.HomeActivity;
import com.absurd.circle.ui.activity.TweetDetailActivity;
import com.absurd.circle.ui.adapter.MessageAdapter;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.LogFactory;


import java.util.Collections;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by absurd on 14-3-12.
 */
public class TweetListFragment extends Fragment {
    private CommonLog mLog = LogFactory.createLog(AppConstant.TAG);
    private ListView mContentLv;
    private TextView mEmptyTv;

    private PullToRefreshAttacher mAttacher;
    private MessagePage mCurrentPage = new MessagePage();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_list,null);
        new LoadMessageTask().execute(false);
        mContentLv = (ListView)rootView.findViewById(R.id.lv_content);
        MessageAdapter adapter = new MessageAdapter(getActivity());
        mContentLv.setAdapter(adapter);
        mContentLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if(absListView.getLastVisiblePosition() == absListView.getCount() - 1 && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    new LoadMessageTask().execute(true);
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });
        mContentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IntentUtil.startActivity(TweetListFragment.this.getActivity(), TweetDetailActivity.class,"messageId",(Message)mContentLv.getAdapter().getItem(i));
            }
        });
        mEmptyTv = (TextView)rootView.findViewById(R.id.tv_empty);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAttacher = ((HomeActivity)getActivity()).getAttacher();
        mAttacher.addRefreshableView(mContentLv,new PullToRefreshAttacher.OnRefreshListener() {
            @Override
            public void onRefreshStarted(View view) {
                mLog.i("onRefreshStarted");
                new LoadMessageTask().execute(false);
            }
        });

    }

    private class LoadMessageTask extends AsyncTask<Boolean,Void,List<Message>>{

        private boolean mStatus = false;

        @Override
        protected List<Message> doInBackground(Boolean... booleans) {
            mStatus = booleans[0];
            List<Message> res = Collections.emptyList();
            MessageService service = new MessageService();
            if(!mStatus){
                // up
                mCurrentPage = service.getPublic();
            }else{
                // down
                if(hasNext())
                    mCurrentPage = service.getNext(mCurrentPage.getNext());
                else
                    return null;
            }
            if(mCurrentPage != null)
                res = mCurrentPage.getResults();
            return res;
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            mLog.i("onPostExecute");
            if(mAttacher.isRefreshing())
                mAttacher.setRefreshing(false);
            if(!mStatus){
                ((MessageAdapter)mContentLv.getAdapter()).setItems(messages);
            }else{
                if(messages != null)
                    ((MessageAdapter)mContentLv.getAdapter()).addItems(messages);
            }

        }
    }

    private boolean hasNext() {
        return (mCurrentPage == null || mCurrentPage.getNext() == null) ? false : true;
    }

}
