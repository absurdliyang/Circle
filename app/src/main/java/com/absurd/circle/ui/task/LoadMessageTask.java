package com.absurd.circle.ui.task;

import android.os.AsyncTask;

import com.absurd.circle.core.bean.Message;
import com.absurd.circle.core.bean.MessagePage;
import com.absurd.circle.core.service.MessageService;

import java.util.Collections;
import java.util.List;

/**
 * Created by absurd on 14-3-12.
 */
public abstract class LoadMessageTask extends AsyncTask<Boolean,Void,List<Message>> {

    private boolean mStatus = false;

    private MessagePage mCurrentPage;

    public LoadMessageTask(MessagePage curPage){
        mCurrentPage = curPage;
    }


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
        }
        if(mCurrentPage != null)
            res = mCurrentPage.getResults();
        return res;
    }

    @Override
    protected void onPostExecute(List<Message> messages) {
        super.onPostExecute(messages);
        if(!mStatus){
            onRefreshComplete(messages);
        }else{
            onGetNextPageComplete(messages);
        }
    }

    public abstract void onRefreshComplete(List<Message> messages);

    public abstract void onGetNextPageComplete(List<Message> messages);



    private boolean hasNext(){
        return (mCurrentPage == null || mCurrentPage.getNext() == null) ? false : true;
    }
}
