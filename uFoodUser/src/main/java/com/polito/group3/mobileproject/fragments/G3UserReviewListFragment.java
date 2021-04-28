package com.polito.group3.mobileproject.fragments;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.objects.G3ReviewObj;
import com.example.ufoodlibrary.objects.G3UserObj;
import com.example.ufoodlibrary.utilities.CustomComparatorReviews;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.ItemDecoration;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.mobileproject.activities.G3UserRestaurantActivity;
import com.polito.group3.mobileproject.adapters.G3UserReviewAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Mattia on 09/05/2016.
 */
public class G3UserReviewListFragment extends Fragment {
    private Context mCtx;
    private View v;

    public TextView mTitle;

    private TextView noReviewText;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private G3UserReviewAdapter mAdapter;
    private ArrayList<G3ReviewObj> mSet;
    private G3RestaurantObj mRestaurant;
    private ProgressDialog progressD;
    private CountDownLatch latch;
    private ArrayList<G3UserObj> mUsersDataSet;
    private ArrayList<G3OrderObj> mOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().findViewById(R.id.reviewsbutton).setClickable(false);

        mCtx = this.getActivity();

        v = inflater.inflate(R.layout.user_review_list_fragment, container, false);

        mRestaurant = G3UserRestaurantActivity.getRestautant();
        mUsersDataSet = new ArrayList<>();
        mOrders = new ArrayList<>();
        mSet = new ArrayList<>();

        loadData();

        return v;
    }


    private void initData() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout categorybar = (LinearLayout) getActivity().findViewById(R.id.categorybar);
                categorybar.setVisibility(View.GONE);
                mTitle = (TextView) getActivity().findViewById(R.id.nestedfragmentlabel);
                mTitle.setText(R.string.reviews);

                getActivity().findViewById(R.id.reviewsbutton).setBackground(getResources().getDrawable(R.color.secondary_text));
                TextView tw = (TextView) getActivity().findViewById(R.id.reviewtextview);
                tw.setTextColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.black));


                mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerReview);
                noReviewText = (TextView) v.findViewById(R.id.noreviewstext);

                mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setAutoMeasureEnabled(true);
//        mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setNestedScrollingEnabled(false);

                ItemDecoration idec = new ItemDecoration(getActivity());
                mRecyclerView.addItemDecoration(idec);

//                mRestaurant = ((G3UserRestaurantActivity) getActivity()).getmRestaurantObj();
                if(!mSet.isEmpty()) {
                    Collections.sort(mSet, new CustomComparatorReviews());
                    mAdapter = new G3UserReviewAdapter(mSet, mUsersDataSet);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    noReviewText.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }

            }
        });



    }

    private void loadData(){

        if(mRestaurant.getNumOfReviews() != 0) {

            progressD = TransitionHelper.getProgress(getActivity());
            progressD.show();

            try {
                G3Application.fManager.getRestaurantReviewsbyKeyRestaurant(mRestaurant.getId(), new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            G3ReviewObj temp = data.getValue(G3ReviewObj.class);
                            mSet.add(temp);
                        }
                        getUsers();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (progressD.isShowing()) {
                            progressD.dismiss();
                        }
                        Toast.makeText(getContext(), getResources().getString(R.string.errorprogress), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (NetworkDownException e) {
                if (progressD.isShowing()) {
                    progressD.dismiss();
                }
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            initData();
        }

    }


    private void getUsers(){

        latch = new CountDownLatch(mSet.size());

        for(final G3ReviewObj reviewObj: mSet){

            try {
                G3Application.fManager.getUserProfileWithKey(reviewObj.getUsername(), new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        G3UserObj temp = dataSnapshot.getValue(G3UserObj.class);
                        mUsersDataSet.add(temp);
                        latch.countDown();
                        Log.d("latch", "countdown");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch ( NetworkDownException  e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().findViewById(R.id.reviewsbutton).setClickable(true);
        getActivity().findViewById(R.id.reviewsbutton).setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.light_grey));
        TextView tw = (TextView) getActivity().findViewById(R.id.reviewtextview);
        tw.setTextColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.secondary_text));

    }


}
