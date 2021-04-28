package com.polito.group3.ufoodfusion.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3ReviewObj;
import com.example.ufoodlibrary.objects.G3UserObj;
import com.example.ufoodlibrary.utilities.CustomComparatorReviews;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.ItemDecoration;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.ufoodfusion.adapters.G3ReviewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Mattia on 30/05/2016.
 */
public class G3ReviewsActivity extends G3BaseActivity{

    private RecyclerView mRecyclerView;
    private G3ReviewAdapter mAdapter;
    private ArrayList<G3ReviewObj> mReviews;
    private ArrayList<G3OrderObj> mOrder;
    private ArrayList<G3UserObj> mUsers;
    private TextView mNoReviews;
    private ProgressDialog progressD;
    private CountDownLatch latch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ufoodlibrary.R.layout.reviews_activity);

        Toolbar toolbar = (Toolbar) findViewById(com.example.ufoodlibrary.R.id.toolbar_without_logo);
        setSupportActionBar(toolbar);
        setTitle(com.example.ufoodlibrary.R.string.reviews);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mReviews = new ArrayList<>();
        mOrder = new ArrayList<>();
        mUsers = new ArrayList<>();
        loadView();

        loadData();


    }

    private void loadData() {

        progressD = TransitionHelper.getProgress(this);
        progressD.show();

        try {
            G3Application.fManager.getRestaurantReviews(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        G3ReviewObj temp = data.getValue(G3ReviewObj.class);
                        mReviews.add(temp);
                    }
                    getOrders();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if(progressD.isShowing())
                        progressD.dismiss();

                    Toast.makeText(getApplicationContext(),databaseError.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {

            if(progressD.isShowing())
                progressD.dismiss();

            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }


    }

    private void getOrders() {

        latch = new CountDownLatch(mReviews.size());

        for(final G3ReviewObj reviewObj: mReviews){

            try {
                G3Application.fManager.getOrderWithKey(reviewObj.getOrderId(), new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        G3OrderObj temp = dataSnapshot.getValue(G3OrderObj.class);
                        mOrder.add(temp);
                        latch.countDown();
                        Log.d("latch", "countdown");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch ( NetworkDownException e) {
                e.printStackTrace();
            }

        }


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("latch", "wait");

                    latch.await();
                    Log.d("latch", "release");
                    getUsers();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

    }

    private void getUsers(){

        latch = new CountDownLatch(mOrder.size());

        for(final G3OrderObj orderObj: mOrder){

            try {
                G3Application.fManager.getUserProfileWithKey(orderObj.getUserid(), new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        G3UserObj temp = dataSnapshot.getValue(G3UserObj.class);
                        mUsers.add(temp);
                        latch.countDown();
                        Log.d("latch", "countdown");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch ( NetworkDownException  e) {
                e.printStackTrace();
            }

        }


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("latch", "wait");

                    latch.await();
                    Log.d("latch", "release");
                    if(progressD.isShowing())
                        progressD.dismiss();
                    initData();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

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


    private void loadView() {
        mRecyclerView = (RecyclerView) findViewById(com.example.ufoodlibrary.R.id.recyclerReview);
        mNoReviews = (TextView) findViewById(com.example.ufoodlibrary.R.id.noreviewstext);

    }

    private void initData() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ItemDecoration idec = new ItemDecoration(getApplicationContext());
                mRecyclerView.addItemDecoration(idec);
                mRecyclerView.setHasFixedSize(true);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                if (mReviews.isEmpty()) {
                    mNoReviews.setVisibility(View.VISIBLE);
                } else {
                    mNoReviews.setVisibility(View.GONE);
                    Collections.sort(mReviews, new CustomComparatorReviews());
                    mAdapter = new G3ReviewAdapter(mReviews, mOrder, mUsers, G3ReviewsActivity.this);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnSaveReplyClickListener(new G3ReviewAdapter.OnSaveReplyClickListener() {
                        @Override
                        public void onSaveReplyClick(String reviewId, final String reply, final int position) {
                            final ProgressDialog progressD = TransitionHelper.getProgress(G3ReviewsActivity.this);
                            progressD.show();
                            try {
                                G3Application.fManager.addReplyToReview(reviewId, reply,
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mReviews.get(position).setReply(reply);
                                                mAdapter.notifyItemChanged(position);
                                                progressD.dismiss();
                                            }
                                        });
                            } catch (NetworkDownException e) {
                                Toast.makeText(G3ReviewsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressD.dismiss();
                            }
                        }
                    });
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        
    }

}
