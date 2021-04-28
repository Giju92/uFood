package com.polito.group3.ufoodfusion.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.TransitionHelper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.ufoodfusion.R;
import com.polito.group3.ufoodfusion.adapters.G3TabsAdapter;
import com.polito.group3.ufoodfusion.dialogfragments.G3ConnectonDownDialogFragment;
import com.polito.group3.ufoodfusion.fragments.G3FragmentLifecycle;


/**
 * Created by Alfonso on 01-Apr-16.
 */
public class G3TabsActivity extends G3RestaurantBaseActivityWithDrawer implements G3ConnectonDownDialogFragment.onTryConnect {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private G3TabsAdapter mAdapter;
    private static G3RestaurantObj mRestaurant;
    private Context mCtx;
    private ProgressDialog progress;
    private boolean notification = false;
    private boolean initialLoadCompleted = false;
    private G3ConnectonDownDialogFragment newFragment;
    private static int currentPosition;

    @Override
    public void onNewIntent(Intent intent){
        Bundle extras = intent.getExtras();
        if (extras != null) {
            // We have clicked on push notification
            if(extras.containsKey("orders"))
            {
                // Extract the extra-data from the Notification
                notification = extras.getBoolean("orders");

                // If it's not the first start of the activity and where are not on order's tab let's switch to it
                if (initialLoadCompleted && currentPosition != 1) {
                    mViewPager.setCurrentItem(1, true);
                }
            }

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        mCtx = getApplicationContext();
        currentPosition = 0;

        onNewIntent(getIntent());

        setAlarmManager();

        setTitle(null);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        //Setting Home hamburger icon
        if(myToolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle(null);

        }

        setDrawerList();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void loadData(){
        progress = TransitionHelper.getProgress(this);
        progress.show();
        try {
            G3Application.fManager.getRestaurantProfile(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mRestaurant = dataSnapshot.getValue(G3RestaurantObj.class);
                    progress.dismiss();
                    initdata();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    progress.dismiss();

                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            progress.dismiss();
            showDialogNetworkDown();
        }
    }

    private void initdata() {
        if(mRestaurant !=null){
            mAdapter = new G3TabsAdapter(getSupportFragmentManager(), 2, notification);
            mViewPager = (ViewPager)findViewById(R.id.viewpager);
            mViewPager.setAdapter(mAdapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    G3FragmentLifecycle fragmentToHide = (G3FragmentLifecycle) mAdapter.instantiateItem(mViewPager, currentPosition);
                    fragmentToHide.onPauseFragment();

                    G3FragmentLifecycle fragmentToShow = (G3FragmentLifecycle) mAdapter.instantiateItem(mViewPager, position);
                    fragmentToShow.onResumeFragment();

                    currentPosition = position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            mTabLayout = (TabLayout)findViewById(R.id.tablayout);
            setTab();
        }

    }

    private void setTab() {
        if(progress.isShowing())
            progress.dismiss();
        mTabLayout.removeAllTabs();
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.menu_white));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.delivery_white));

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.light_grey));
        mTabLayout.setSelectedTabIndicatorHeight(10);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(notification){
            mViewPager.setCurrentItem(1, true);
        }

        initialLoadCompleted = true;
    }

    public static G3RestaurantObj getmRestaurant() {
        return mRestaurant;
    }

    public boolean getNotification(){
        return notification;
    }

    private void showDialogNetworkDown() {

        newFragment = G3ConnectonDownDialogFragment.newInstance();
        newFragment.setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
        newFragment.show(getSupportFragmentManager(),"");
    }
    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.close_app))
                .setMessage(getResources().getString(R.string.are_you_sure_close_app))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }

                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();


    }

    @Override
    public void onTryClick() {
        loadData();
    }

    public static int getCurrentFragmentPosition() {
        return currentPosition;
    }

}