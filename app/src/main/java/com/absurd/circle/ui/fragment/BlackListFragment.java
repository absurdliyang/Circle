package com.absurd.circle.ui.fragment;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.model.BlackList;
import com.absurd.circle.data.model.User;
import com.absurd.circle.ui.activity.BaseActivity;
import com.absurd.circle.ui.activity.ContactActivity;
import com.absurd.circle.ui.fragment.base.UserListFragment;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by absurd on 14-3-28.
 */
public class BlackListFragment extends UserListFragment<BlackList> {

    @Override
    protected List<User> handleResult(List<BlackList> result) {
        List<User> resList = new ArrayList<User>();
        for(BlackList black : result){
            if(black.getUser() != null){
                resList.add(black.getUser());
            }
        }
        int count = resList.size();
        ((BaseActivity)getActivity()).setActionBarTitle("黑名单 (" + count + ")");
        return resList;
    }

    @Override
    protected void loadData(int pageIndex, TableQueryCallback<BlackList> callback) {
        if (AppContext.auth != null) {
            mUserService.getBlackList(AppContext.auth.getUserId(),callback);
        }
    }

}
