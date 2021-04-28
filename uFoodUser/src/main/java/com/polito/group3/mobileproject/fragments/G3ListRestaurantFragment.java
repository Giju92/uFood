package com.polito.group3.mobileproject.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.polito.group3.mobileproject.adapters.G3CardReastaurantAdapter;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alfonso on 21/04/2016.
 */
public class G3ListRestaurantFragment extends Fragment {

    private Context mCtx;
    private View v;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private G3CardReastaurantAdapter mAdapter;
    private ArrayList<G3RestaurantObj> mSet;
    OnFragmentChangedListener mCallback;

    public interface OnFragmentChangedListener {

        public void onButtonClicked(String name);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnFragmentChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentChangedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCtx = this.getContext();
        v = inflater.inflate(R.layout.user_restaurant_fragment, container, false);
        mSet = new ArrayList<>();
        initData();

        return v;
    }

    private void initData() {


//        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerRestaurant);
//        mRecyclerView.setHasFixedSize(true);
//
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        ItemDecoration idec = new ItemDecoration(this.getContext());
//        mRecyclerView.addItemDecoration(idec);
//
//        //TODO load data
//
//
//
//
//
//
//        mAdapter = new G3CardReastaurantAdapter(mSet);
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnCardRestaurantListener(new G3CardReastaurantAdapter.onCardRestaurantClick() {
//
//            @Override
//            public void onCardClick() {
//                mCallback.onButtonClicked("menu");
//            }
//        });
    }

}
