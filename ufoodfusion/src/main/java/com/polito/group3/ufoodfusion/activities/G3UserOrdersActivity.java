package com.polito.group3.ufoodfusion.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.objects.G3ReviewObj;
import com.example.ufoodlibrary.utilities.CustomComparator;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.ufoodfusion.adapters.G3UserOrdersAdapter;
import com.polito.group3.ufoodfusion.dialogfragments.G3UserOrderDetailsDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Luigi on 07/05/2016.
 */
public class G3UserOrdersActivity extends G3BaseActivity  {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private G3UserOrdersAdapter mAdapter;
    private TextView mNoOrdersText;
    private ArrayList<G3OrderObj> mOrdersList;
    private ArrayList<G3OrderObj> mFilteredOrdersList;
    private ArrayList<G3ReviewObj> mReviewsList;
    private ArrayList<G3RestaurantObj> mRestaurantsList;
    public final static String ORDEROBJ = "orderObj";

    private final static int REQUEST_RATE_ORDER = 0x1550;
    private boolean shouldUpdateDataset = false;
    private ProgressDialog progressD;
    private CountDownLatch latch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_list);

        //Setting Toolbar as Action Bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_without_logo);
        setSupportActionBar(myToolbar);

        //Setting Home hamburger icon
        if(myToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.order_title);
        }
        //Loading Views by ID
        loadView();

        //Load Data
        mOrdersList = new ArrayList<>();
        mFilteredOrdersList = new ArrayList<>();
        mRestaurantsList = new ArrayList<>();
        mReviewsList = new ArrayList<>();
        loadData();
        setListeners();


    }

    @Override
    protected void onResume() {

        super.onResume();

        if(shouldUpdateDataset){
            loadData();
            mAdapter.notifyDataSetChanged();
            shouldUpdateDataset = false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_RATE_ORDER && resultCode == RESULT_OK) {
            shouldUpdateDataset = true;
        }

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

    private void loadData() {

        mOrdersList.clear();
        mRestaurantsList.clear();
        mReviewsList.clear();

        progressD = TransitionHelper.getProgress(this);
        progressD.show();

        try {
            G3Application.fManager.getUserOrders(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        G3OrderObj order = data.getValue(G3OrderObj.class);
                        if(!order.isUserVisualized()){
                            setOrderVisualized(order);
                        }
                        mOrdersList.add(order);
                    }
                    getRestaurant();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if(progressD.isShowing())
                        progressD.dismiss();
                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            if(progressD.isShowing())
                progressD.dismiss();
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT);
        }

    }

    private void setOrderVisualized(G3OrderObj order) {
        order.setUserVisualized(true);

        try {
            G3Application.fManager.setUserOrderVisualized(order.getOrderid());
        } catch (NetworkDownException e) {

        }
    }


    private void getRestaurant() {

        latch = new CountDownLatch(mOrdersList.size());

        for(final G3OrderObj order: mOrdersList){

            try {
                G3Application.fManager.getRestaurantProfileWithKey(order.getRestaurantId(), new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        G3RestaurantObj temp = dataSnapshot.getValue(G3RestaurantObj.class);
                        mRestaurantsList.add(temp);
                        latch.countDown();
                        Log.d("latch", "countdown");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch (NotAuthenticatedException | NetworkDownException e) {
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
                    setRecyclerView();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

    }



    private void loadView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.order_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mNoOrdersText = (TextView) findViewById(R.id.noorders);

    }

    private void setListeners() {



    }

    private void setRecyclerView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressD.isShowing())
                    progressD.dismiss();
                if(mOrdersList != null && mRestaurantsList != null) {
                    if(mOrdersList.isEmpty()){
                        mNoOrdersText.setVisibility(View.VISIBLE);
                    }
                    else{
                        mNoOrdersText.setVisibility(View.GONE);
                        Collections.sort(mOrdersList,new CustomComparator());
                        Collections.reverse(mOrdersList);
                        mAdapter = new G3UserOrdersAdapter(mOrdersList, mRestaurantsList);
                        mRecyclerView.setAdapter(mAdapter);

                        mAdapter.setOnOrderClickListener(new G3UserOrdersAdapter.onOrderClick() {
                            @Override
                            public void onCardClick(int position) {

                                Intent intent = new Intent(getBaseContext(), G3UserRateOrderActivity.class);
                                intent.putExtra(ORDEROBJ, mOrdersList.get(position).getOrderid());
                                startActivityForResult(intent, REQUEST_RATE_ORDER);
                            }

                            @Override
                            public void onDetailsClick(int adapterPosition) {

                                G3UserOrderDetailsDialogFragment fragment = G3UserOrderDetailsDialogFragment
                                        .newInstance(mOrdersList.get(adapterPosition));
                                fragment.show(getSupportFragmentManager(), "dialogdetail");
                            }

                        });
                    }

                }
            }
        });

    }

}
