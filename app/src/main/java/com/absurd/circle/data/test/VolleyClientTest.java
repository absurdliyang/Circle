package com.absurd.circle.data.test;

import com.absurd.circle.data.util.LocationHelper;

/**
 * Created by absurd on 14-3-15.
 */
public class VolleyClientTest extends BaseTestCase{

    public void testClient() throws Exception {
        double latitude = 65.9667;
        double longitude= -18.5333;
        LocationHelper.getLocationInfo(latitude,longitude);
        Thread.sleep(5000);
        assertNull(null);



    }
}
