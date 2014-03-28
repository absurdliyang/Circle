package com.absurd.circle.data.test;

import com.absurd.circle.app.AppContext;
import com.absurd.circle.data.service.BlobStorgeService;

/**
 * Created by absurd on 14-3-28.
 */
public class BlobServiceTest extends BaseTestCase {
    public void testGetSas() throws Exception {
        BlobStorgeService mService = new BlobStorgeService();
        //mService.getSasForNewBlob(BlobStorgeService.PICS_CONTAINER_NAME,"test");
        //String sas = mService.getLoadedBlob().getAsJsonPrimitive("sasUrl").toString();
        //AppContext.commonLog.i(sas);
    }
}
