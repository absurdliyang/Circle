package com.absurd.circle.util;

import android.os.Build;
import android.util.TypedValue;

import com.absurd.circle.app.AppContext;

/**
 * Created by absurd on 14-5-3.
 */
public class SystemUtil {

    public static boolean isKK() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }


    public static int dip2px(int dipValue) {
        float reSize = AppContext.getContext().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    public static int px2dip(int pxValue) {
        float reSize = AppContext.getContext().getResources().getDisplayMetrics().density;
        return (int) ((pxValue / reSize) + 0.5);
    }

    public static float sp2px(int spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                AppContext.getContext().getResources().getDisplayMetrics());
    }
}
