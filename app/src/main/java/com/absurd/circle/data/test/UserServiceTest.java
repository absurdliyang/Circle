package com.absurd.circle.data.test;

import android.test.AndroidTestCase;

import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.UserService;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-3-15.
 */
public class UserServiceTest  extends BaseTestCase{
    private UserService mUserService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mUserService = new UserService(getContext());
        mLog.d("UserServiceTest");
    }

    public void testGetUsers() throws Exception {
        mLog.d("testGetUsers");
        mUserService.getUsers(new TableQueryCallback<User>() {
            @Override
            public void onCompleted(List<User> users, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if (users != null) {
                    mLog.d("get Users' size " + users.size() + " ");
                    for (User u : users) {
                        mLog.d(u.getName());
                    }
                }else{
                    mLog.d("users is null");
                }
            }
        });
        Thread.sleep(3000);
        assertNull(null);
    }
}
