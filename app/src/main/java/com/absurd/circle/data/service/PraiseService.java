package com.absurd.circle.data.service;

import android.content.Context;

import com.absurd.circle.data.model.Praise;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

/**
 * Created by absurd on 14-3-26.
 */
public class PraiseService extends BaseService {


    public void insertPraise(Praise praise, TableOperationCallback<Praise> callback){
        getPraiseTable().insert(praise,callback);
    }


    public void isPraised(int messageid, int userid, TableQueryCallback<Praise> callback){
        getPraiseTable().where();
    }

}
