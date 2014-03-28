package com.absurd.circle.ui.view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.util.StringUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;



public class PhotoFragment extends DialogFragment{
	public static Uri uri;
	public static Intent data;
	private IUploadImage mIUploadImage;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        PhotoDialog mDialog = new PhotoDialog(this.getActivity());
        mDialog.setOnGallaryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAlbum();
                PhotoFragment.this.dismiss();
            }
        });
        mDialog.setOnTakePhotoClickListner(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTake();
                PhotoFragment.this.dismiss();
            }
        });
        return mDialog;
    }
	
	public PhotoFragment setIUploadImage(IUploadImage i){
		this.mIUploadImage = i;
		return this;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == IUploadImage.SELECT_BY_ALBUM){
				mIUploadImage.onResultByAlbum(data);
			}else if(requestCode == IUploadImage.IMAGE_CROP){
				mIUploadImage.onResultByCrop(data);
			}else if(requestCode == IUploadImage.SELECT_BY_TAKE_PHOTO){
				mIUploadImage.onResultByTake(data);
			}
		}
	}
	
	
	/**
	 * On taking a photo
	 */
	private void onTake(){
		String savePath = "";
		String storageState = Environment.getExternalStorageState();		
		if(storageState.equals(Environment.MEDIA_MOUNTED)){
			savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConstant.TAKE_PHOTO_PATH;
			File savedir = new File(savePath);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		}

		if(StringUtil.isEmpty(savePath)){
            Toast.makeText(getActivity(), "Create save photo path error!", Toast.LENGTH_SHORT).show();
            return;
		}

		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String fileName = "circle_" + timeStamp + ".jpg";
		File out = new File(savePath, fileName);
		Uri uri = Uri.fromFile(out);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (hasImageCaptureBug()) {
		    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/tmp")));
		} else {
		    intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		PhotoFragment.uri = uri;
		PhotoFragment.data = intent;
		getActivity().startActivityForResult(intent, IUploadImage.SELECT_BY_TAKE_PHOTO);
	}
	
	
	/**
	 * On select album
	 */
	private void onAlbum(){
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		getActivity().startActivityForResult(intent,IUploadImage.SELECT_BY_ALBUM);
	}
	
	
	public static boolean hasImageCaptureBug() {
	    // list of known devices that have the bug
	    ArrayList<String> devices = new ArrayList<String>();
	    devices.add("android-devphone1/dream_devphone/dream");
	    devices.add("generic/sdk/generic");
	    devices.add("vodafone/vfpioneer/sapphire");
	    devices.add("tmobile/kila/dream");
	    devices.add("verizon/voles/sholes");
	    devices.add("google_ion/google_ion/sapphire");
	
	    return devices.contains(android.os.Build.BRAND + "/" + android.os.Build.PRODUCT + "/"
	            + android.os.Build.DEVICE);
	
	}
}