package com.absurd.circle.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Follow;
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
public class FollowersFragment extends UserListFragment<Follow> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setMode(MODE_LOCAL);
        ((BaseActivity) getActivity()).setActionBarTitle("关注");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected List<User> handleResult(List<Follow> result) {
        List<User> resList = new ArrayList<User>();
        for(Follow follow : result){
            if(follow.getUser() != null){
                resList.add(follow.getUser());
            }
        }
        if(getCurrentPageIndex() == 0) {
            if (getMode() == UserListFragment.MODE_LOCAL) {
                if(getActivity() != null) {
                    int count = AppContext.cacheService.followDBManager.getCount();
                    ((BaseActivity) getActivity()).setActionBarTitle("关注 (" + count + ")");
                }
            } else if (getMode() == UserListFragment.MODE_NET) {
                // Change the title
                int count = resList.size();
                if(getActivity() != null) {
                    ((BaseActivity) getActivity()).setActionBarTitle("关注 (" + count + ")");
                }
                // Refresh the cache
                AppContext.cacheService.followDBManager.deleteAll();
                for(Follow follow : result){
                    AppContext.cacheService.followDBManager.insertFollow(follow);
                }
                // Bind the data with List
                List<Follow> localList = loadDataFromLocal(0);
                List<User> userList = new ArrayList<User>();
                for(Follow follow : localList){
                    if(follow.getUser() != null){
                        userList.add(follow.getUser());
                    }
                }
                return userList;
            }
        }
        return resList;
    }

    @Override
    protected void loadDataFromNet(int pageIndex, TableQueryCallback<Follow> callback) {
        if(AppContext.auth != null) {
            mUserService.getAllUserFollowers(AppContext.auth.getUserId(),callback);
        }
    }

    @Override
    protected List<Follow> loadDataFromLocal(int pageIndex) {
        return AppContext.cacheService.followDBManager.getPage(pageIndex,10);
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
