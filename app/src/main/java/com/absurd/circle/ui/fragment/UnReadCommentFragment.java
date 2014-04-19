package com.absurd.circle.ui.fragment;

import android.widget.Toast;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.service.NotificationService;
import com.absurd.circle.ui.adapter.UnReadCommentAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.ui.fragment.base.RefreshableFragment;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-4-19.
 */
public class UnReadCommentFragment extends RefreshableFragment<Comment>{
    private NotificationService mNotificationService = new NotificationService();

    @Override
    protected BeanAdapter<Comment> setAdapter() {
        return new UnReadCommentAdapter(this.getActivity());
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<Comment> callback) {
        if(AppContext.auth != null) {
            mNotificationService.getComments(AppContext.auth.getUserId(),pageIndex, 10, callback);
        }
    }

    @Override
    protected void handleResult(List<Comment> result) {

    }
}
