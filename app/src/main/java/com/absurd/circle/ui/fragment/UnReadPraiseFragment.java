package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Praise;
import com.absurd.circle.data.service.NotificationService;
import com.absurd.circle.ui.adapter.UnReadPraiseAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.ui.fragment.base.LocalRefreshableFragment;
import com.absurd.circle.ui.fragment.base.RefreshableFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-4-19.
 */
public class UnReadPraiseFragment extends LocalRefreshableFragment<Praise> {
    //private NotificationService mNotificationService = new NotificationService();


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentLv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    @Override
    protected BeanAdapter<Praise> setAdapter() {
        return new UnReadPraiseAdapter(this.getActivity());
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<Praise> callback) {
    }

    @Override
    protected void handleResult(List<Praise> result) {

    }

    @Override
    protected List<Praise> loadDataLocal(int pageIndex) {
        if(pageIndex == 0){
            AppContext.cacheService.praiseDBManager.updateAllRead();
        }
        return AppContext.cacheService.praiseDBManager.getPage(mCurrentPageIndex, 20);
    }

    @Override
    protected void onRefreshCallback(List<Praise> result) {
        super.onRefreshCallback(result);
        for(Praise praise : result){
            AppContext.cacheService.praiseDBManager.insertPraise(praise);
        }
    }
}
