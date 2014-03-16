package com.absurd.circle.data.test;

import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.BaseService;
import com.absurd.circle.data.service.UserService;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;

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
            mLog.d(service.getMessageTable().getTableName());
        }else{
            mLog.d("userTable is null");
        }
        assertNull(null);

    }

}
