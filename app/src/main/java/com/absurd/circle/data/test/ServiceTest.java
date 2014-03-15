package com.absurd.circle.data.test;

import android.test.AndroidTestCase;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.BaseService;
import com.absurd.circle.data.service.UserService;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;

import junit.framework.TestCase;

/**
 * Created by absurd on 14-3-14.
 */
public class ServiceTest extends BaseTestCase{

    private UserService mUserService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mUserService = new UserService(getContext());
    }

    public void testService() throws Exception {
        BaseService service = new BaseService();
        MobileServiceTable<User> userTable =  service.getUserTable();
        if(userTable != null) {
            mLog.d(userTable.getTableName());
            mLog.d(service.getTweetTable().getTableName());
        }else{
            mLog.d("userTable is null");
        }
        assertNull(null);

    }

}
