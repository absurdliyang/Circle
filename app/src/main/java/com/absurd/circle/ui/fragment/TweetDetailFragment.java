package com.absurd.circle.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.R;
import com.absurd.circle.core.bean.Comment;
import com.absurd.circle.core.bean.CommentPage;
import com.absurd.circle.core.service.CommentService;
import com.absurd.circle.ui.activity.TweetDetailActivity;
import com.absurd.circle.ui.adapter.CommentAdapter;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;

import java.util.Collections;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by absurd on 14-3-14.
 */
public class TweetDetailFragment extends Fragment{
    private CommonLog mLog = LogFactory.createLog(AppConstant.TAG);
    private ListView mContentLv;
    private TextView mEmptyTv;

    private PullToRefreshAttacher mAttacher;
    private CommentPage mCurrentPage = new CommentPage();

    private TweetDetailActivity mTweetDetailActivity;

    public TweetDetailFragment(TweetDetailActivity tweetDetailActivity){
        this.mTweetDetailActivity = tweetDetailActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tweet_detail,null);
        View headerView = inflater.inflate(R.layout.header_tweet_detail,null);
        ((TextView)headerView.findViewById(R.id.tv_header_username)).setText(mTweetDetailActivity.tweet.getUser().getNickName());
        ((TextView)headerView.findViewById(R.id.tv_header_tweet_content)).setText(mTweetDetailActivity.tweet.getContent());
        new LoadCommentTask().execute(false);
        mContentLv = (ListView)rootView.findViewById(R.id.lv_comment_content);
        CommentAdapter adapter = new CommentAdapter(getActivity());
        mContentLv.addHeaderView(headerView);
        mContentLv.setAdapter(adapter);
        mContentLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if(absListView.getLastVisiblePosition() == absListView.getCount() - 1 && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    new LoadCommentTask().execute(true);
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });
        mEmptyTv = (TextView)rootView.findViewById(R.id.tv_empty);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAttacher = mTweetDetailActivity.getAttacher();
        mAttacher.addRefreshableView(mContentLv,new PullToRefreshAttacher.OnRefreshListener() {
            @Override
            public void onRefreshStarted(View view) {
                mLog.i("onRefreshStarted");
                new LoadCommentTask().execute(false);
            }
        });

    }

    private class LoadCommentTask extends AsyncTask<Boolean,Void,List<Comment>> {

        private boolean mStatus = false;

        @Override
        protected List<Comment> doInBackground(Boolean... booleans) {
            mStatus = booleans[0];
            List<Comment> res = Collections.emptyList();
            CommentService service = new CommentService();
            if(!mStatus){
                // up
                mCurrentPage = service.getMessage(mTweetDetailActivity.tweet.getId());
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
        protected void onPostExecute(List<Comment> comments) {
            mLog.i("onPostExecute");
            if(mAttacher.isRefreshing())
                mAttacher.setRefreshing(false);
            if(!mStatus){
                // the method get Adapter when listView add headerView or footerView
                HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter)mContentLv.getAdapter();
                ((CommentAdapter) headerAdapter.getWrappedAdapter()).setItems(comments);
            }else{
                if(comments != null)
                    ((CommentAdapter)mContentLv.getAdapter()).addItems(comments);
            }

        }
    }

    private boolean hasNext() {
        return (mCurrentPage == null || mCurrentPage.getNext() == null) ? false : true;
    }
}
