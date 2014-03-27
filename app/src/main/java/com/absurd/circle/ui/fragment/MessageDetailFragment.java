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
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.service.CommentService;
import com.absurd.circle.ui.activity.MessageDetailActivity;
import com.absurd.circle.ui.adapter.CommentAdapter;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;


/**
 * Created by absurd on 14-3-14.
 */
public class MessageDetailFragment extends Fragment{
    private ListView mContentLv;
    private TextView mEmptyTv;

    private MessageDetailActivity mMessageDetailActivity;

    private CommentService mCommentService;

    private int currentPageIndex = 1;

    public MessageDetailFragment(MessageDetailActivity messageDetailActivity){
        this.mMessageDetailActivity = messageDetailActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCommentService = new CommentService(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_message_detail,null);
        View headerView = inflater.inflate(R.layout.header_message_detail,null);
        ((TextView)headerView.findViewById(R.id.tv_header_username)).setText(mMessageDetailActivity.message.getUser().getName());
        ((TextView)headerView.findViewById(R.id.tv_header_tweet_content)).setText(mMessageDetailActivity.message.getContent());
        //new LoadCommentTask().execute(false);

        mContentLv = (ListView)rootView.findViewById(R.id.lv_comment_content);
        CommentAdapter adapter = new CommentAdapter(getActivity());
        mContentLv.addHeaderView(headerView);
        mContentLv.setAdapter(adapter);

        //Scroll down get more comments
        mContentLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if(absListView.getLastVisiblePosition() == absListView.getCount() - 1 && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    mCommentService.getComments(mMessageDetailActivity.message.getId(),1,10,true,new TableQueryCallback<Comment>(){

                        @Override
                        public void onCompleted(List<Comment> result, int count, Exception exception, ServiceFilterResponse response) {
                            if(result == null){
                                if(exception != null){
                                    exception.printStackTrace();
                                    Toast.makeText(MessageDetailFragment.this.getActivity(),"get Comment failed!",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) mContentLv.getAdapter();
                                ((CommentAdapter)headerAdapter.getWrappedAdapter()).addItems(result);
                            }
                        }
                    });
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });
        mEmptyTv = (TextView)rootView.findViewById(R.id.tv_empty);

        //Init comment list on start
        mCommentService.getComments(mMessageDetailActivity.message.getId(),currentPageIndex,10,true,new TableQueryCallback<Comment>() {
            @Override
            public void onCompleted(List<Comment> result, int count, Exception exception, ServiceFilterResponse response) {
                if(result == null){
                    if(exception != null){
                        exception.printStackTrace();
                        Toast.makeText(MessageDetailFragment.this.getActivity(),"get Comment failed!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    // HeaderViewListAdapter can not cast to BaseAdapter directly
                    HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) mContentLv.getAdapter();
                    ((CommentAdapter) headerAdapter.getWrappedAdapter()).setItems(result);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
