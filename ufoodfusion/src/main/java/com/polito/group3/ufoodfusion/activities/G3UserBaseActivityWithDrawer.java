package com.polito.group3.ufoodfusion.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3DrawerObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.ItemDecoration;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.polito.group3.ufoodfusion.adapters.G3DrawerAdapter;


import java.util.ArrayList;

/**
 * Created by Alfonso on 06/05/2016.
 */
public class G3UserBaseActivityWithDrawer extends G3BaseActivity{

    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private RecyclerView.LayoutManager mDrawerLayoutManager;
    private G3DrawerAdapter mDrawerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<G3DrawerObj> mDrawObjListt;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
    }

    protected void setDrawerList() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);

        mDrawerList.setHasFixedSize(true);
        mDrawerLayoutManager = new LinearLayoutManager(this);

        mDrawObjListt = new ArrayList<G3DrawerObj>();
        mDrawObjListt.add(new G3DrawerObj(R.drawable.img_profile, getResources().getString(R.string.profile)));
        mDrawObjListt.add(new G3DrawerObj(R.drawable.review_icon, getResources().getString(R.string.orders)));
        mDrawObjListt.add(new G3DrawerObj(R.drawable.preferite_icon,getResources().getString(R.string.favourites)));
        mDrawObjListt.add(new G3DrawerObj(R.drawable.about_icon, getResources().getString(R.string.about)));
        mDrawObjListt.add(new G3DrawerObj(R.drawable.logout_icon, getResources().getString(R.string.logout)));
        ItemDecoration idec = new ItemDecoration(this);
        mDrawerList.addItemDecoration(idec);

        mDrawerAdapter = new G3DrawerAdapter(R.drawable.new_logo,"uFood",mDrawObjListt  );
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setLayoutManager(mDrawerLayoutManager);

        mDrawerAdapter.setOnItemClickListener(new G3DrawerAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                TextView tv = (TextView) v;
                if (tv.getText().equals(getResources().getString(R.string.profile))) {
                    Intent intent = new Intent(getBaseContext(), G3ShowEditProfile.class);
                    startActivity(intent);

                }
                if (tv.getText().equals(getResources().getString(R.string.orders))) {
                    Intent intent = new Intent(getBaseContext(), G3UserOrdersActivity.class);
                    startActivity(intent);

                }
                if (tv.getText().equals(getResources().getString(R.string.favourites))) {
                    Intent intent = new Intent(getBaseContext(), G3FavouritesActivity.class);
                    startActivity(intent);
                }
                if (tv.getText().equals(getResources().getString(R.string.about))) {
                    Intent intent = new Intent(getBaseContext(), G3AboutActivity.class);
                    startActivity(intent);
                }
                if (tv.getText().equals(getResources().getString(R.string.logout))) {
                    mDrawerLayout.closeDrawers();
                    final ProgressDialog progress = TransitionHelper.getProgress(mActivity);
                    progress.show();
                    try {
                        G3Application.fManager.signOut(new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                                progress.dismiss();
                                if(firebaseAuth.getCurrentUser() == null){
                                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.successprogress),Toast.LENGTH_SHORT).show();
                                    setDrawerList();
                                }
                            }
                        });
                    } catch (NotAuthenticatedException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    } catch (NetworkDownException e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.add,R.string.edit){

            @Override
            public void onDrawerOpened(View drawerView) {

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if(!G3Application.fManager.isSignedin()){
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_perm_identity_white_24dp, this.getTheme());
            mDrawerToggle.setHomeAsUpIndicator(drawable);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        }
        else{
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        mDrawerToggle.syncState();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if(mDrawerToggle != null){
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(mDrawerToggle != null){
            mDrawerToggle.onConfigurationChanged(newConfig);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(!G3Application.fManager.isSignedin()){
            Intent intent = new Intent(this, G3LoginActivity.class);
            startActivity(intent);
        }else{
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.home).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
}
