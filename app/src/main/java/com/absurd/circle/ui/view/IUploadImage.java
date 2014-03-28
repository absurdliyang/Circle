package com.absurd.circle.ui.view;

import android.content.Intent;

public interface IUploadImage {
	/**
	 * Action slecting a  image by taking a picture 
	 */
	int SELECT_BY_TAKE_PHOTO = 1;
	/**
	 * Action selcting a picture from album
	 */
	int SELECT_BY_ALBUM = 2;
	/**
	 * Action croping the selected image
	 */
	int IMAGE_CROP = 3;
	/**
	 * On result for after select a image from album
	 */
	public void onResultByAlbum(Intent data);
	/**
	 * On result for after taking a new photo
	 */
	public void onResultByTake(Intent data);
	/**
	 * On result fro croping the selected photo
	 */
	public void onResultByCrop(Intent data);
}
