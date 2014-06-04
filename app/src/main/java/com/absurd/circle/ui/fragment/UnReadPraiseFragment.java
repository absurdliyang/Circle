package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.absurd.circle.app.AppConfig;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
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
    public void configureContentLv(ListView listView) {
        super.configureContentLv(listView);
        listView.setDivider(AppContext.getContext().getResources().getDrawable(R.drawable.listview_divider));
        listView.setDividerHeight(1);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(28,0,28,0);
        mContentLv.setLayoutParams(params);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentLv.setMode(PullToRefreshBase.Mode.DISABLED);
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
        return AppContext.cacheService.praiseDBManager.getPage(mCurrentPageIndex, AppConfig.CUSTOM_PAGE_SIZE);
    }

    @Override
    protected void onRefreshCallback(List<Praise> result) {
        super.onRefreshCallback(result);
        for(Praise praise : result){
            AppContext.cacheService.praiseDBManager.insertPraise(praise);
        }
    }
}
