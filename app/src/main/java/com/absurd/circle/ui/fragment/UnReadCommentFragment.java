package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.absurd.circle.app.AppConfig;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.service.NotificationService;
import com.absurd.circle.ui.adapter.UnReadCommentAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.ui.fragment.base.LocalRefreshableFragment;
import com.absurd.circle.ui.fragment.base.RefreshableFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-4-19.
 */
public class UnReadCommentFragment extends LocalRefreshableFragment<Comment>{
    //private NotificationService mNotificationService = new NotificationService();


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentLv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    @Override
    protected BeanAdapter<Comment> setAdapter() {
        return new UnReadCommentAdapter(this.getActivity());
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<Comment> callback) {

    }

    @Override
    protected void handleResult(List<Comment> result) {

    }

    @Override
    protected List<Comment> loadDataLocal(int pageIndex) {
        if(pageIndex == 0) {
            AppContext.cacheService.commnetDBManager.updateAllRead();
        }
        return AppContext.cacheService.commnetDBManager.getPage(mCurrentPageIndex, AppConfig.CUSTOM_PAGE_SIZE);
    }
}
