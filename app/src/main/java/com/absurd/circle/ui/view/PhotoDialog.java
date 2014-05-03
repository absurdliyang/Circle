package com.absurd.circle.ui.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.absurd.circle.app.R;


public class PhotoDialog extends AlertDialog {

    private TextView mGallaryTv;
    private TextView mTakePhotoTv;


	public PhotoDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.dia_photo, null);
		this.setContentView(layout);
        mGallaryTv = (TextView)layout.findViewById(R.id.tv_dia_photo_gallary);
        mTakePhotoTv = (TextView)layout.findViewById(R.id.tv_dia_photo_take_photo);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
	}


    public void setOnGallaryClickListener(View.OnClickListener listener){
        mGallaryTv.setOnClickListener(listener);
    }

    public void setOnTakePhotoClickListner(View.OnClickListener listener){
        mTakePhotoTv.setOnClickListener(listener);
    }


}
