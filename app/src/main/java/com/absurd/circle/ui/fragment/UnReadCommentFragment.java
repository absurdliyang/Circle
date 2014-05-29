package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.absurd.circle.app.AppConfig;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Comment;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.service.MessageService;
import com.absurd.circle.data.service.NotificationService;
import com.absurd.circle.ui.activity.EditCommentActivity;
import com.absurd.circle.ui.activity.MessageDetailActivity;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.adapter.UnReadCommentAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.ui.fragment.base.LocalRefreshableFragment;
import com.absurd.circle.ui.fragment.base.RefreshableFragment;
import com.absurd.circle.ui.view.ItemDialog;
import com.absurd.circle.util.IntentUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by absurd on 14-4-19.
 */
public class UnReadCommentFragment extends LocalRefreshableFragment<Comment>{
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

    @Override
    protected void onListItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        super.onListItemClick(adapterView, view, i, l);
        final Comment comment = (Comment)mAdapter.getItem(i-1);
        List<String> items = new ArrayList<String>();
        items.add("回复");
        items.add("查看这条说说");
        final ItemDialog dialog = new ItemDialog(UnReadCommentFragment.this.getActivity(),items);
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        HashMap<String, Serializable> params = new HashMap<String, Serializable>();
                        params.put("parentComment",comment);
                        params.put("flag",true);
                        IntentUtil.startActivity(UnReadCommentFragment.this.getActivity(),EditCommentActivity.class, params);

                        break;
                    case 1:
                        HashMap<String, Serializable> params1 = new HashMap<String, Serializable>();
                        params1.put("flag",true);
                        params1.put("messageId",comment.getMessageId());
                        IntentUtil.startActivity(UnReadCommentFragment.this.getActivity(),MessageDetailActivity.class,params1);

                        break;
                    default:
                        break;
                }
                dialog.cancel();
            }
        });
        if(comment != null) {
            dialog.show();
        }
    }
}
