package com.absurd.circle.ui.fragment;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Praise;
import com.absurd.circle.data.service.NotificationService;
import com.absurd.circle.ui.adapter.UnReadPraiseAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.ui.fragment.base.RefreshableFragment;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-4-19.
 */
public class UnReadPraiseFragment extends RefreshableFragment<Praise> {
    private NotificationService mNotificationService = new NotificationService();

    @Override
    protected BeanAdapter<Praise> setAdapter() {
        return new UnReadPraiseAdapter(this.getActivity());
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<Praise> callback) {
        if(AppContext.auth != null) {
            mNotificationService.getPraises(AppContext.auth.getUserId(),pageIndex, 10, callback);
        }
    }

    @Override
    protected void handleResult(List<Praise> result) {

    }

    @Override
    protected void onRefreshCallback(List<Praise> result) {
        super.onRefreshCallback(result);
        for(Praise praise : result){
            AppContext.cacheService.praiseDBManager.insertPraise(praise);
        }
    }
}
