package com.polito.group3.mobileproject.dialogfragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.polito.group3.mobileproject.adapters.G3BasketAdapter;
import com.polito.group3.mobileproject.adapters.G3OrderDetailsAdapter;

import java.util.ArrayList;

/**
 * Created by Luigi on 10/05/2016.
 */
public class G3UserOrderDetailsDialogFragment extends android.support.v4.app.DialogFragment {

    private RecyclerView mRecycleView;
    private G3BasketAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static G3OrderObj mOrder;
//    private static int mPosition;
//    private static Context mContext;

    public static G3UserOrderDetailsDialogFragment newInstance(G3OrderObj orderObj) {
        G3UserOrderDetailsDialogFragment fragment = new G3UserOrderDetailsDialogFragment();
        mOrder = orderObj;
//        mPosition = position;
//        mContext = context;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.user_orders_details_dialog, container, false);
        getDialog().setTitle(getResources().getString(R.string.details));

        mRecycleView = (RecyclerView) v.findViewById(R.id.orderDetailsRecycler);

        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new G3BasketAdapter(new ArrayList<>(mOrder.getOrderItems()), mOrder, getContext());
        mRecycleView.setAdapter(mAdapter);

        return v;

    }

}
