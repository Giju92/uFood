package com.polito.group3.mobileproject.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.RestaurantComparator;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.mobileproject.R;
import com.polito.group3.mobileproject.adapters.G3CardReastaurantAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Mattia on 20/07/2016.
 */
public class G3FavouritesActivity extends G3BaseActivity {

    private Context mCtx;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mNoFavoritesText;
    private G3CardReastaurantAdapter mAdapter;
    public final static String RESTAUANTOBJ = "restaurantObj";
    private G3FavouritesActivity mActivity;
    private ArrayList<G3RestaurantObj> mSet;
    private ProgressDialog progressD;
    private CountDownLatch latch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ufoodlibrary.R.layout.activity_user_favorites);

        mCtx = getApplicationContext();

        mActivity = this;
        //Setting Toolbar as Action Bar
        Toolbar myToolbar = (Toolbar) findViewById(com.example.ufoodlibrary.R.id.toolbar_without_logo);
        setSupportActionBar(myToolbar);

        //Setting Home hamburger icon
        if (myToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(com.example.ufoodlibrary.R.string.favourites);
        }
        loadView();

        mSet = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        mSet.clear();

        progressD = TransitionHelper.getProgress(this);
        progressD.show();

        try {
            G3Application.fManager.getFavouriteRestaurant(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    latch = new CountDownLatch((int) dataSnapshot.getChildrenCount());
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("latch", "wait");

                                latch.await();

                                Log.d("latch", "release");
                                Collections.sort(mSet, new RestaurantComparator.NameComparator());
                                setRecyclerView();
                                progressD.dismiss();


                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    t.start();

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String restaurantid = data.getValue(String.class);
                        //mRestIdSet.add(restaurantid);
                        try {
                            G3Application.fManager.getRestaurantProfileWithKey(restaurantid, new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    G3RestaurantObj restaurant = dataSnapshot.getValue(G3RestaurantObj.class);
                                    mSet.add(restaurant);
                                    latch.countDown();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } catch (NotAuthenticatedException | NetworkDownException e) {
                            if (progressD.isShowing())
                                progressD.dismiss();
                            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (progressD.isShowing())
                        progressD.dismiss();
                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            if (progressD.isShowing())
                progressD.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT);
        }



    }

    private void loadView() {

        mRecyclerView = (RecyclerView) findViewById(com.example.ufoodlibrary.R.id.favorites_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mNoFavoritesText = (TextView) findViewById(R.id.nofavorites);

    }

    private void setRecyclerView() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mSet.isEmpty()){
                    mNoFavoritesText.setVisibility(View.VISIBLE);
                } else {
                    mAdapter = new G3CardReastaurantAdapter(mSet);
                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.setOnCardRestaurantListener(new G3CardReastaurantAdapter.onCardRestaurantClick() {
                        @Override
                        public void onCardClick(int position, ImageView image) {
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Intent intent = new Intent(getBaseContext(), G3UserRestaurantActivity.class);
                                intent.putExtra(RESTAUANTOBJ, mSet.get(position));
                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                        makeSceneTransitionAnimation(mActivity, (View) image, "cardImageTransition");
                                startActivity(intent, options.toBundle());
                            } else {
                                Intent intent = new Intent(getBaseContext(), G3UserRestaurantActivity.class);
                                intent.putExtra(RESTAUANTOBJ, mSet.get(position));
                                startActivity(intent);
                            }

                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
