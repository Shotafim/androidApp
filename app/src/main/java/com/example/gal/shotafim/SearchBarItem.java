package com.example.gal.shotafim;

public class SearchBarItem {
    private String mName;
    private int mImage;
    private boolean mIsUser;

    public SearchBarItem(String mName, int mImage, boolean isitUser) {
        this.mName = mName;
        this.mImage = mImage;
        mIsUser = isitUser;
    }

    public String getmName() {
        return mName;
    }

    public boolean ismIsUser() {
        return mIsUser;
    }

    public void setmIsUser(boolean mIsUser) {
        this.mIsUser = mIsUser;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmImage() {
        return mImage;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }
}
