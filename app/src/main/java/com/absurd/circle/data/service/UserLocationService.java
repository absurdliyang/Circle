package com.absurd.circle.data.service;

import android.content.Context;
import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.val;

import com.absurd.circle.data.model.UserLocation;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

/**
 * Created by absurd on 14-3-15.
 */
public class UserLocationService extends BaseService{
    public UserLocationService() {
    }

    public UserLocationService(Context context) {
        super(context);
    }

    public UserLocationService(Context context, String token) {
        super(context, token);
    }

    public void getUserLocation(String userid,TableQueryCallback<UserLocation> callback){
        getUserLocationTable().where().field("userid").eq(val(userid)).execute(callback);
    }

}
