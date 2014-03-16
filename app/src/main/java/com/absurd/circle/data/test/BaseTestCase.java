package com.absurd.circle.data.test;

import android.os.AsyncTask;
import android.test.AndroidTestCase;

import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;

/**
 * Created by absurd on 14-3-15.
 */
public class BaseTestCase extends AndroidTestCase{
    private static final String TAG = "CircleDataTest";
    protected CommonLog mLog = LogFactory.createLog(TAG);

}
