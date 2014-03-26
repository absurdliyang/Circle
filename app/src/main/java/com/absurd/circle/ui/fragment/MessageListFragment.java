package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.model.MessageType;
import com.absurd.circle.data.service.MessageService;
import com.absurd.circle.ui.activity.HomeActivity;
import com.absurd.circle.ui.activity.MessageDetailActivity;
import com.absurd.circle.ui.adapter.MessageAdapter;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.IntentUtil;
import com.absurd.circle.util.LogFactory;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;


import java.util.ArrayList;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by absurd on 14-3-12.
 */
public class MessageListFragment extends Fragment {
    private CommonLog mLog = LogFactory.createLog(AppConstant.TAG);
    private ListView mContentLv;
    private TextView mEmptyTv;

    private PullToRefreshAttacher mAttacher;
    //private MessagePage mCurrentPage = new MessagePage();
    private MessageService mMessageService;

    private int mCurrentPageIndex = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMessageService = new MessageService(getActivity(), AppContext.token);
        View rootView = inflater.inflate(R.layout.item_list,null);
        //new LoadMessageTask().execute(false);
        List<Integer> messageTypes = new ArrayList<Integer>();
        messageTypes.add(MessageType.WEIBO);
        mMessageService.getNearMessage(mCurrentPageIndex,38.246900000000004,114.78558,1000,messageTypes,true,"1",new TableQueryCallback<Message>() {
            @Override
            public void onCompleted(List<Message> result, int count, Exception exception, ServiceFilterResponse response) {
                if(result == null){
                    if(exception != null){
                        exception.printStackTrace();
                        Toast.makeText(MessageListFragment.this.getActivity(),"getNearMessage error!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    ((MessageAdapter)mContentLv.getAdapter()).setItems(result);
                }
            }
        });

        mContentLv = (ListView)rootView.findViewById(R.id.lv_content);
        MessageAdapter adapter = new MessageAdapter(getActivity());
        mContentLv.setAdapter(adapter);
        mContentLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if(absListView.getLastVisiblePosition() == absListView.getCount() - 1 && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    //new LoadMessageTask().execute(true);
                    List<Integer> messageTypes = new ArrayList<Integer>();
                    messageTypes.add(MessageType.WEIBO);
                    mMessageService.getNearMessage(mCurrentPageIndex++,38.246900000000004,114.78558,1000,messageTypes,true,"1",new TableQueryCallback<Message>() {
                        @Override
                        public void onCompleted(List<Message> result, int count, Exception exception, ServiceFilterResponse response) {
                            if(result == null){
                                if(exception != null){
                                    exception.printStackTrace();
                                    Toast.makeText(MessageListFragment.this.getActivity(),"getNearMessage error!",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                ((MessageAdapter)mContentLv.getAdapter()).addItems(result);
                            }
                        }
                    });
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
                //new LoadMessageTask().execute(false);
                List<Integer> messageTypes = new ArrayList<Integer>();
                messageTypes.add(MessageType.WEIBO);
                mMessageService.getNearMessage(mCurrentPageIndex,38.246900000000004,114.78558,1000,messageTypes,true,"1",new TableQueryCallback<Message>() {
                    @Override
                    public void onCompleted(List<Message> result, int count, Exception exception, ServiceFilterResponse response) {
                        mAttacher.setRefreshComplete();
                        if(result == null){
                            if(exception != null){
                                exception.printStackTrace();
                                Toast.makeText(MessageListFragment.this.getActivity(),"getNearMessage error!",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            ((MessageAdapter)mContentLv.getAdapter()).setItems(result);
                        }
                    }
                });

            }
        });

    }
    /**
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
        **/

}
