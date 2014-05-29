package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.absurd.circle.app.AppConfig;
import com.absurd.circle.app.AppContext;
import com.absurd.circle.app.R;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.ui.activity.UserProfileActivity;
import com.absurd.circle.ui.adapter.NearbyUserAdapter;
import com.absurd.circle.ui.adapter.base.BeanAdapter;
import com.absurd.circle.ui.fragment.base.RefreshableFragment;
import com.absurd.circle.ui.view.ItemDialog;
import com.absurd.circle.util.IntentUtil;
import com.android.volley.Response;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-5-16.
 */
public class NearbyUserFragment extends RefreshableFragment<User> {

    private int mType = 0;


    @Override
    public void configureContentLv(ListView listView) {
        listView.setDivider(AppContext.getContext().getResources().getDrawable(R.drawable.listview_divider));
        listView.setDividerHeight(1);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(28,0,28,0);
        mContentLv.setLayoutParams(params);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentLv.setDividerDrawable(AppContext.getContext().getResources().getDrawable(R.drawable.item_divider));
    }

    @Override
    protected BeanAdapter<User> setAdapter() {
        return new NearbyUserAdapter(this.getActivity(),mType);
    }

    public NearbyUserFragment(){

    }

    public NearbyUserFragment(int type){
        this.mType = type;
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<User> callback) {
        UserService userService = new UserService();
        if(AppContext.lastPosition != null) {
            userService.getNearPeople(AppContext.lastPosition.getLongitude(), AppContext.lastPosition.getLatitude(),
                    AppConfig.NEARBY_PEOPLE_DISTANCE, mType, pageIndex,new Response.Listener<User.GsonUser>() {
                        @Override
                        public void onResponse(User.GsonUser result) {

                            if (result.getUsers() != null) {
                                if(mCurrentPageIndex == 0) {
                                    getAdapter().setItems(result.getUsers());
                                }else {
                                    getAdapter().addItems(result.getUsers());
                                }
                            }
                            mActivity.setBusy(false);
                            mIsBusy = false;
                        }
                    }
            );
        }
    }

    @Override
    protected void handleResult(List<User> result) {

    }

    @Override
    protected void onListItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        super.onListItemClick(adapterView, view, i, l);
        IntentUtil.startActivity(getActivity(), UserProfileActivity.class, "user", (User)getAdapter().getItem(i-1));
    }

    public void onMoreClicked(View view) {
        List<String> items = new ArrayList<String>();
        items.add("只看女生");
        items.add("只看男生");
        items.add("查看全部");
        final ItemDialog dialog = new ItemDialog(getActivity(),items);
        dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        NearbyUserFragment.this.mType = 1;
                        refreshTranscation();
                        break;
                    case 1:
                        NearbyUserFragment.this.mType = 2;
                        refreshTranscation();
                        break;
                    case 2:
                        NearbyUserFragment.this.mType = 0;
                        refreshTranscation();
                        break;
                    default:
                        break;
                }
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
