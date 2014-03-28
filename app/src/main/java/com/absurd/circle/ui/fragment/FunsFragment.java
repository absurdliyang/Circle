package com.absurd.circle.ui.fragment;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.Follow;
import com.absurd.circle.data.model.User;
import com.absurd.circle.ui.activity.BaseActivity;
import com.absurd.circle.ui.fragment.base.UserListFragment;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-3-28.
 */
public class FunsFragment extends UserListFragment<Follow> {
    @Override
    protected List<User> handleResult(List<Follow> result) {
        List<User> resList = new ArrayList<User>();
        for(Follow follow : result){
            if(follow.getUser() != null){
                resList.add(follow.getUser());
            }
        }int count = resList.size();
        ((BaseActivity)getActivity()).setActionBarTitle("粉丝 (" + count + ")");
        return resList;
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<Follow> callback) {
        if(AppContext.auth != null) {
            mUserService.getUserFuns(AppContext.auth.getUserId(),callback);
        }
    }
}
