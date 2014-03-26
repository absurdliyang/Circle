package com.absurd.circle.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.R;
import com.absurd.circle.core.service.MessageService;
import com.absurd.circle.ui.activity.RefreshableActivity;
import com.absurd.circle.ui.adapter.BeanAdapter;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;

import java.util.Collections;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

/**
 * Created by absurd on 14-3-12.
 */
public abstract class PagedItemFragment<E> extends Fragment{
    private CommonLog mLog = LogFactory.createLog(AppConstant.TAG);

    protected RefreshableActivity mActivity;
    protected PullToRefreshAttacher mAttacher;

    protected ListView mContentLv;
    protected TextView mEmptyTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_list,null);
        mContentLv = (ListView)rootView.findViewById(R.id.lv_content);
        mEmptyTv = (TextView)rootView.findViewById(R.id.tv_empty);
        BeanAdapter adapter = getAdapter();
        mContentLv.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (RefreshableActivity)getActivity();
        mAttacher = mActivity.getAttacher();mAttacher.addRefreshableView(mContentLv,new PullToRefreshAttacher.OnRefreshListener() {
            @Override
            public void onRefreshStarted(View view) {
                mLog.i("onRefreshStarted");
                //new LoadDataTask().execute(false);
                getFirstPage();
            }
        });
    }

    private class LoadDataTask extends AsyncTask<Boolean,Void,List<E>>{
        private boolean mStatus;
        @Override
        protected List<E> doInBackground(Boolean... booleans) {
            mStatus = booleans[0];
            List<E> res = Collections.emptyList();
            MessageService service = new MessageService();
            if(!mStatus){
                // Get the first page data
                res = getFirstPage();
            }else{
                if(hasNext()) {
                    // Get the next page data
                    res = getNextPage();
                }else
                    return null;
            }
            return res;
        }

        @Override
        protected void onPostExecute(List<E> datas) {
            if(mAttacher.isRefreshing())
                mAttacher.setRefreshing(false);
            if(!mStatus){
                ((BeanAdapter)mContentLv.getAdapter()).setItems(datas);
            }else{
                if(datas != null)
                    ((BeanAdapter)mContentLv.getAdapter()).addItems(datas);
            }
        }
    }

    protected abstract BeanAdapter getAdapter();
    protected abstract boolean hasNext();
    protected abstract List<E> loadData(int pageIndex);
    protected List<E> getFirstPage(){
        return loadData(1);
    }
    protected  List<E> getNextPage(){
        return null;
    }



}
