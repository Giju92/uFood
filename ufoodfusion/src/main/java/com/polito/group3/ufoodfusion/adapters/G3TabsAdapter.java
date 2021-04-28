package com.polito.group3.ufoodfusion.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.polito.group3.ufoodfusion.fragments.G3MenuFragment;
import com.polito.group3.ufoodfusion.fragments.G3OrderFragment;


/**
 * Created by Alfonso on 01-Apr-16.
 */
public class G3TabsAdapter extends FragmentPagerAdapter {

    private int mNumOfTabs;
    private boolean notify;

    public G3TabsAdapter(FragmentManager fm, int numtab, boolean notification){
        super(fm);
        this.mNumOfTabs = numtab;
        this.notify = notification;
    }



    @Override
    public int getCount() {

        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                G3MenuFragment frag = new G3MenuFragment();
                return frag;

            case 1:

                G3OrderFragment frag2 = new G3OrderFragment();
                return frag2;

            default:
                return null;
        }
    }
}
