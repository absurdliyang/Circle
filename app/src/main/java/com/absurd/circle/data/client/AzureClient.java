package com.absurd.circle.data.client;

import android.app.Activity;
import android.content.Context;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.app.AppContext;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

/**
 * Created by absurd on 14-3-14.
 */
public class AzureClient {
    private static MobileServiceClient mClient;

    public synchronized static MobileServiceClient getInstance(Context context){
        if(mClient == null)
            initClient(context);
        return mClient;
    }

    private static void initClient(Context context){
        try {
            mClient = new MobileServiceClient(AppConstant.AZURE_APP_URL,AppConstant.AZURE_APP_KEY, context);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


}
