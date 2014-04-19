package com.absurd.circle.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.data.service.NotificationService;
import com.absurd.circle.ui.activity.UnReadCommentActivity;
import com.absurd.circle.ui.activity.UnReadPraiseActivity;
import com.absurd.circle.ui.adapter.UserMessageAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.ui.fragment.base.RefreshableFragment;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-4-19.
 */
public class NotificationFragment extends RefreshableFragment<UserMessage> {
    private NotificationService mNotificationService = new NotificationService();

    @Override
    protected BeanAdapter<UserMessage> setAdapter() {
        return new UserMessageAdapter(this.getActivity());
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<UserMessage> callback) {
        if(AppContext.auth != null) {
            mNotificationService.getUserMessages(AppContext.auth.getUserId(),pageIndex, 10, callback);
        }
    }

    @Override
    protected void handleResult(List<UserMessage> result) {

    }

    @Override
    protected View initHeader() {
        View header = LayoutInflater.from(this.getActivity()).inflate(R.layout.header_notification_list,null);
        ImageView commentBtn = (ImageView)header.findViewById(R.id.iv_header_comment);
        ImageView praiseBtn = (ImageView)header.findViewById(R.id.iv_header_praise);
        commentBtn.setImageBitmap(ImageUtil.roundBitmap(
                ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.notification_comment)).getBitmap()
        ));
        praiseBtn.setImageBitmap(ImageUtil.roundBitmap(
                ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.notification_praise)).getBitmap()
        ));
        header.findViewById(R.id.llyt_header_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(NotificationFragment.this.getActivity(), UnReadCommentActivity.class);
            }
        });
        header.findViewById(R.id.llyt_header_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(NotificationFragment.this.getActivity(), UnReadPraiseActivity.class);
            }
        });
        return header;
    }
}
