package com.absurd.circle.ui.fragment.base;

import android.os.AsyncTask;

import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-5-6.
 */
public abstract class LocalRefreshableFragment<V> extends RefreshableFragment<V> {
    @Override
    protected BeanAdapter<V> setAdapter() {
        return null;
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<V> callback) {

    }

    @Override
    protected void handleResult(List<V> result) {

    }


    protected abstract List<V> loadDataLocal(int pageIndex);

    @Override
    public void refreshTranscation() {
        //mContentLv.smoothScrollToPosition(0);
        mCurrentPageIndex = 0;
        new LoadLocalDataTask().execute();
    }

    @Override
    public void nextPageTransaction() {
        mCurrentPageIndex++;
        new LoadLocalDataTask().execute();
    }


    public void clearList(){
        getAdapter().deleteAllItems();
    }


    public class LoadLocalDataTask extends AsyncTask<Void, Void, List<V>> {

        @Override
        protected List<V> doInBackground(Void... voids) {
            return loadDataLocal(mCurrentPageIndex);
        }

        @Override
        protected void onPostExecute(List<V> vs) {
            super.onPostExecute(vs);
            if(mCurrentPageIndex == 0){
                getAdapter().setItems(vs);
            }else{
                getAdapter().addItems(vs);
            }
        }
    }


}
