package com.absurd.circle.data.test;

import com.absurd.circle.data.util.LocationHelper;

/**
 * Created by absurd on 14-3-15.
 */
public class VolleyClientTest extends BaseTestCase{

    public void testClient() throws Exception {
        double latitude = 40.057098;
        double longitude= 116.307175;
        LocationHelper.getLocationInfo(latitude,longitude);
        Thread.sleep(5000);
        assertNull(null);



    }
}
