package com.absurd.circle.ui.view;

import android.app.Dialog;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.absurd.circle.app.R;


public class PhotoDialog extends Dialog {

    private TextView mGallaryTv;
    private TextView mTakePhotoTv;
    private TextView mRecordVedioTv;


	public PhotoDialog(Context context, int mode) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.dia_photo, null);
		this.setContentView(layout);
        mGallaryTv = (TextView)layout.findViewById(R.id.tv_dia_photo_gallary);
        mTakePhotoTv = (TextView)layout.findViewById(R.id.tv_dia_photo_take_photo);
        mRecordVedioTv = (TextView)layout.findViewById(R.id.tv_dia_video_record);
		setCancelable(true);
		setCanceledOnTouchOutside(true);

        if(mode == 1){
            mRecordVedioTv.setVisibility(View.GONE);
            mTakePhotoTv.setVisibility(View.VISIBLE);
        }else{
            mRecordVedioTv.setVisibility(View.VISIBLE);
            mTakePhotoTv.setVisibility(View.GONE);
        }
	}


    public void setOnGallaryClickListener(View.OnClickListener listener){
        mGallaryTv.setOnClickListener(listener);
    }

    public void setOnTakePhotoClickListner(View.OnClickListener listener){
        mTakePhotoTv.setOnClickListener(listener);
    }

    public void setOnRecordVedioClickListener(View.OnClickListener listener){
        mRecordVedioTv.setOnClickListener(listener);
    }

}
