package com.example.ufoodlibrary.objects;

/**
 * Created by Alfonso-LAPTOP on 12/04/2016.
 */
public class G3DrawerObj {

    private String mNameDrawer;
    private int mImageDrawer;

    public G3DrawerObj(){

    }

    public G3DrawerObj(int mImageDrawer, String mNameDrawer) {
        this.mImageDrawer = mImageDrawer;
        this.mNameDrawer = mNameDrawer;
    }

    public int getmImageDrawer() {
        return mImageDrawer;
    }

    public void setmImageDrawer(int mImageDrawer) {
        this.mImageDrawer = mImageDrawer;
    }

    public String getmNameDrawer() {
        return mNameDrawer;
    }

    public void setmNameDrawer(String mNameDrawer) {
        this.mNameDrawer = mNameDrawer;
    }
}
