package com.polito.group3.mobileproject.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3OrderItem;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.polito.group3.mobileproject.activities.G3BasketActivity;
import com.polito.group3.mobileproject.activities.G3UserRestaurantActivity;
import com.polito.group3.mobileproject.adapters.G3BasketAdapter;


import java.util.ArrayList;

/**
 * Created by Mattia on 12/05/2016.
 */
public class G3OrderSummaryFragment extends android.support.v4.app.Fragment {


    private Context mCtx;
    private View v;
    private G3OrderObj mOrder;

    private RecyclerView mRecycler;
    private G3BasketAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView mOrderTotalItems;
    private TextView mOrderTotalCost;
    private TextView mOrderSummary;
    private Button mChangeButton;
    private Button mCheckoutButton;


    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private boolean change = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCtx = this.getContext();

        v = inflater.inflate(R.layout.fragment_order_summary, container, false);


        loadView();

        loadData();

        setListeners();

        return v;
    }


    private void loadView() {
        mRecycler = (RecyclerView) v.findViewById(R.id.basketRecycler);
        mOrderTotalCost = (TextView) v.findViewById(R.id.ordertotalcost);
        mOrderSummary = (TextView) v.findViewById(R.id.ordersummary);
        mOrderTotalItems = (TextView) getActivity().findViewById(R.id.ordertotalitem);
        mChangeButton = (Button) v.findViewById(R.id.changebtn);
        mCheckoutButton = (Button) v.findViewById(R.id.buttoncheckout);

    }

    private void loadData() {
        mOrder = G3UserRestaurantActivity.getOrder();
        mRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mCtx);
        mRecycler.setLayoutManager(mLayoutManager);

        ArrayList<G3OrderItem> mList = new ArrayList<>();
        mList.addAll(mOrder.getOrderItems());

        mAdapter = new G3BasketAdapter(mList, mOrder, mCtx);
        mRecycler.setAdapter(mAdapter);


        mOrderTotalCost.setText(getString(R.string.total_price) +  " " + String.format("%.2f", mOrder.getTotalOrderPrice())+" " + getString(R.string.euro_symbol));

        if (mOrder.getOrderItems().size() == 0) {
            mOrderTotalItems.setVisibility(View.GONE);
            mCheckoutButton.setVisibility(View.GONE);
            mChangeButton.setVisibility(View.GONE);
            mOrderTotalCost.setVisibility(View.GONE);

            mOrderSummary.setText(getString(R.string.noitemsinbasket));

        }
    }

    private void setListeners() {

        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setChange();
                change = !change;
                ((G3BasketActivity) getActivity()).setChange(change);
                if (change) {
                    mChangeButton.setText(getString(R.string.confirmChange));
                }
                if (!change) {
                    mChangeButton.setText(getString(R.string.change));
                    mOrderTotalItems.setText(getString(R.string.order_total) + ": " + mOrder.getOrderItems().size());
                    mOrderTotalCost.setText(getString(R.string.total_price) + " " + getString(R.string.euro_symbol) + " " + String.format("%.2f", mOrder.getTotalOrderPrice()));
                    if (mOrder.getOrderItems().size() == 0) {
                        mCheckoutButton.setVisibility(View.GONE);
                        mChangeButton.setVisibility(View.GONE);
                        mOrderTotalCost.setVisibility(View.GONE);
                        mOrderTotalItems.setVisibility(View.GONE);
                        mOrderSummary.setText(getString(R.string.noitemsinbasket));
                    }
                }

            }
        });

        mCheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change) {
                    Toast.makeText(mCtx, R.string.confirmBefore, Toast.LENGTH_LONG).show();
                    return;
                }
                if (mOrder.getOrderItems().size() > 0) {
                    G3OrderCheckoutFragment mFragment = new G3OrderCheckoutFragment();
                    if (mFragment != null) {

                        mFragmentManager = getActivity().getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.nestedfragment, mFragment, "USER_CHECKOUT_SUMMARY").commit();
                    }
                }
            }

        });
    }


}