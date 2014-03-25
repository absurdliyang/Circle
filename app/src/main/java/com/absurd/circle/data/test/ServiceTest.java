package com.absurd.circle.data.test;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.data.model.Message;
import com.absurd.circle.data.model.User;
import com.absurd.circle.data.service.BaseService;
import com.absurd.circle.data.service.UserService;
import com.microsoft.windowsazure.mobileservices.MobileServiceSystemProperty;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * Created by absurd on 14-3-14.
 */
public class ServiceTest extends BaseTestCase{

    private UserService mUserService;

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testTable() throws Exception {
        BaseService baseService = new BaseService();
        MobileServiceTable<Message> messageTable = baseService.getMessageTable();
        EnumSet<MobileServiceSystemProperty> messageProperties = messageTable.getSystemProperties();
        mLog.i(Arrays.toString(messageProperties.toArray()));

        EnumSet<MobileServiceSystemProperty> userProperties = baseService.getUserTable().getSystemProperties();
        mLog.i(Arrays.toString(userProperties.toArray()));

        EnumSet<MobileServiceSystemProperty> userLocationProperties = baseService.getUserLocationTable().getSystemProperties();
        mLog.i(Arrays.toString(userLocationProperties.toArray()));

    }
}
