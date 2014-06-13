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
public class FunsFragment extends UserListFragment<Follow> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setMode(UserListFragment.MODE_NET);
        //((BaseActivity) getActivity()).setActionBarTitle("粉丝");
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    int count = 0;
    @Override
    protected List<User> handleResult(List<Follow> result) {
        List<User> resList = new ArrayList<User>();
        for(Follow follow : result){
            if(follow.getUser() != null){
                resList.add(follow.getUser());
            }
        }
        if(mCurrentPageIndex == 0) {
            count = resList.size();
        }else{
            count += resList.size();
        }
        if(getActivity() != null) {
            //((BaseActivity) getActivity()).setActionBarTitle("粉丝 (" + count + ")");
        }
        return resList;
    }

    @Override
    protected void loadDataFromNet(int pageIndex, TableQueryCallback<Follow> callback) {
        if(AppContext.auth != null) {
            mUserService.getUserFuns(AppContext.auth.getUserId(),pageIndex, 10, callback);
        }
    }

    @Override
    protected List<Follow> loadDataFromLocal(int pageIndex) {
        return null;
    }

    @Override
    protected void beforePullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {

    }
}