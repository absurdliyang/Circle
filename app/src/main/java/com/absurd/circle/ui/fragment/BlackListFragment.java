package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.BlackList;
import com.absurd.circle.data.model.User;
import com.absurd.circle.ui.activity.base.BaseActivity;
import com.absurd.circle.ui.fragment.base.UserListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-3-28.
 */
public class BlackListFragment extends UserListFragment<BlackList> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setMode(MODE_LOCAL);
        //((BaseActivity) getActivity()).setActionBarTitle("黑名单");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected List<User> handleResult(List<BlackList> result) {
        List<User> resList = new ArrayList<User>();
        for(BlackList black : result){
            if(black.getUser() != null){
                resList.add(black.getUser());
            }
        }
        if(getCurrentPageIndex() == 0) {
            if (getMode() == UserListFragment.MODE_LOCAL) {
                if(getActivity() != null) {
                    int count = AppContext.cacheService.blackListDBManager.getCount();
                    //((BaseActivity) getActivity()).setActionBarTitle("黑名单 (" + count + ")");
                }
            } else if (getMode() == UserListFragment.MODE_NET) {
                // Change the title
                if(getActivity() != null) {
                    int count = resList.size();
                    //((BaseActivity) getActivity()).setActionBarTitle("黑名单 (" + count + ")");
                }
                // Refresh the cache
                AppContext.cacheService.blackListDBManager.deleteAll();
                for(BlackList black : result){
                    AppContext.cacheService.blackListDBManager.insertBlackList(black);
                }
                // Bind the data with List
                List<BlackList> localList = loadDataFromLocal(0);
                List<User> userList = new ArrayList<User>();
                for(BlackList black : localList){
                    if(black.getUser() != null){
                        userList.add(black.getUser());
                    }
                }
                return userList;
            }
        }
        return resList;
    }

    @Override
    protected void loadDataFromNet(int pageIndex, TableQueryCallback<BlackList> callback) {
        if (AppContext.auth != null) {
            mUserService.getAllBlackList(AppContext.auth.getUserId(),callback);
        }
    }

    @Override
    protected List<BlackList> loadDataFromLocal(int pageIndex) {
        return AppContext.cacheService.blackListDBManager.getPage(pageIndex,10);
    }

    @Override
    protected void beforePullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
        if(direction.equals(PullToRefreshBase.Mode.PULL_FROM_START)){
            setMode(MODE_NET);
        }else if(direction.equals(PullToRefreshBase.Mode.PULL_FROM_END)){
            setMode(MODE_LOCAL);
        }
    }

}
