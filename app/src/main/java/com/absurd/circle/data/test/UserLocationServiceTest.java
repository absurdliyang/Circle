package com.absurd.circle.data.test;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.data.model.UserLocation;
import com.absurd.circle.data.service.UserLocationService;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

/**
 * Created by absurd on 14-3-15.
 */
public class UserLocationServiceTest extends BaseTestCase {

    private UserLocationService mService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mService = new UserLocationService(getContext(), AppConstant.TEST_USER_TOKEN);
    }

    public void testGetUserLocation() throws Exception {
        final Object lock = new Object();
        mService.getUserLocation("112343", new TableQueryCallback<UserLocation>() {
            @Override
            public void onCompleted(List<UserLocation> userLocations, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
                if (userLocations != null) {
                    mLog.d("getUserLocation success");
                    mLog.d(userLocations.size() + "");
                    for (UserLocation ul : userLocations) {
                        mLog.d(ul.getLatitude() + "");
                    }
                } else {
                    mLog.d("user location is null");
                    if (e != null) {
                        e.printStackTrace();
                    }
                    mLog.d("userLocations is null");
                }
                synchronized (lock){
                    lock.notify();
                }
            }
        });
        synchronized (lock){
            lock.wait();
        }

    }
}
