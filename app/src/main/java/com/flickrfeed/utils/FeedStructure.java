package com.flickrfeed.utils;

/**
 * Created by VIJAYAKUMAR MUNIAPPA on 31-08-2016.
 */
public class FeedStructure {

    public String mImageUrl ;
    public  String mImageName;
    public boolean isSelected = false ;

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmImageName() {
        return mImageName;
    }

    public void setmImageName(String mImageName) {
        this.mImageName = mImageName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
