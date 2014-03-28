package com.absurd.circle.ui.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.UserMessage;
import com.absurd.circle.data.service.NotificationService;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.ui.adapter.CommentAdapter;
import com.absurd.circle.ui.adapter.UserMessageAdapter;
import com.absurd.circle.ui.fragment.NotificationListFragment;
import com.absurd.circle.ui.view.LoadingFooter;
import com.absurd.circle.util.ImageUtil;
import com.absurd.circle.util.IntentUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class NotificationActivity extends BaseActivity{
    private ListView mContentLv;
    private TextView mEmptyTv;

    private NotificationService mNotificationService;

    private int mCurrentPageIndex = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);
        mNotificationService = new NotificationService();
        mContentLv = (ListView)findViewById(R.id.lv_content);
        UserMessageAdapter adapter = new UserMessageAdapter(this);
        View header = LayoutInflater.from(this).inflate(R.layout.header_notification_list,null);
        ImageView commentBtn = (ImageView)header.findViewById(R.id.iv_header_comment);
        ImageView praiseBtn = (ImageView)header.findViewById(R.id.iv_header_praise);
        commentBtn.setImageBitmap(ImageUtil.roundBitmap(
                ((BitmapDrawable)AppContext.getContext().getResources().getDrawable(R.drawable.notification_comment)).getBitmap()
        ));
        praiseBtn.setImageBitmap(ImageUtil.roundBitmap(
                ((BitmapDrawable) AppContext.getContext().getResources().getDrawable(R.drawable.notification_praise)).getBitmap()
        ));
        header.findViewById(R.id.llyt_header_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(NotificationActivity.this,UnReadCommentActivity.class);
            }
        });
        header.findViewById(R.id.llyt_header_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(NotificationActivity.this,UnReadPraiseActivity.class);
            }
        });
        mContentLv.addHeaderView(header);
        mContentLv.setAdapter(adapter);
        loadData();
    }

    private void loadData(){
        if(AppContext.auth != null) {
            mNotificationService.getUserMessages(AppContext.auth.getUserId(),new TableQueryCallback<UserMessage>() {
                @Override
                public void onCompleted(List<UserMessage> result, int count, Exception exception, ServiceFilterResponse response) {
                    if(result == null){
                        if(exception != null){
                            exception.printStackTrace();
                            Toast.makeText(NotificationActivity.this, "get UserMessages failed!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        // HeaderViewListAdapter can not cast to BaseAdapter directly
                        HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) mContentLv.getAdapter();
                        ((UserMessageAdapter) headerAdapter.getWrappedAdapter()).setItems(result);
                    }
                }
            });
        }
    }

    @Override
    protected String actionBarTitle() {
        return "消息中心";
    }


}
