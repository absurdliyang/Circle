package com.absurd.circle.ui.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.Praise;
import com.absurd.circle.data.service.NotificationService;
import com.absurd.circle.ui.adapter.UnReadPraiseAdapter;
import com.absurd.circle.util.StringUtil;
import com.absurd.circle.util.TimeUtil;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-3-29.
 */
public class UnReadPraiseActivity extends BaseActivity {
    private ListView mContentLv;
    private TextView mEmptyTv;

    private NotificationService mNotificationService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.item_list);
        mNotificationService = new NotificationService();
        mContentLv = (ListView)findViewById(R.id.lv_content);
        UnReadPraiseAdapter adapter = new UnReadPraiseAdapter(this);
        mContentLv.setAdapter(adapter);
        loadData();
    }
    private void loadData(){
        if(AppContext.auth != null) {
            mNotificationService.getPraises(AppContext.auth.getUserId(),new TableQueryCallback<Praise>() {
                @Override
                public void onCompleted(List<Praise> result, int count, Exception exception, ServiceFilterResponse response) {
                    if(result == null){
                        if(exception != null){
                            exception.printStackTrace();
                            Toast.makeText(UnReadPraiseActivity.this, "get Praises failed!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        for(Praise praise : result){
                            AppContext.cacheService.praiseDBManager.insertPraise(praise);
                        }

                        ((UnReadPraiseAdapter) mContentLv.getAdapter()).setItems(result);
                    }
                }
            });
        }
    }
    @Override
    protected String actionBarTitle() {
        return "赞我的";
    }


}
