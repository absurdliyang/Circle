package com.absurd.circle.ui.bean;

import com.absurd.circle.data.model.Photo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by absurd on 14-6-14.
 */
public class PhotosBean implements Serializable {

    private List<Photo> mPhotos;

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }
}