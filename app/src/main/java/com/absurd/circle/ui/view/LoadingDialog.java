package com.absurd.circle.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.absurd.circle.app.R;

/**
 * Created by absurd on 14-5-2.
 */
public class LoadingDialog extends AlertDialog {
    private TextView mDescTv;
    private String mStrDesc;

    public LoadingDialog(Context context, String strDesc) {
        super(context);
        mStrDesc = strDesc;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dia_loading);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        mDescTv = (TextView)findViewById(R.id.tv_dia_loading_desc);
        mDescTv.setText(mStrDesc);
    }

}
