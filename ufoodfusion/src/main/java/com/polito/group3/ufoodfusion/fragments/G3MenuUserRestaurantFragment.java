package com.polito.group3.ufoodfusion.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3MenuItemCategory;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.ItemDecoration;
import com.polito.group3.ufoodfusion.activities.G3UserRestaurantActivity;
import com.polito.group3.ufoodfusion.adapters.G3MenuUserAdapter;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Alfonso on 23-Apr-16.
 */
public class G3MenuUserRestaurantFragment extends Fragment {

    private Context mCtx;
    private View v;

    public ImageView mRestaurantImage;
    public TextView mNameRestaurant;
    public TextView mTypeKitchen;
    public TextView mDistance;
    public TextView mTitle;
    private LinearLayout mTotaldish;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private G3MenuUserAdapter mAdapter;
    private ArrayList<String> mSet;
    private Toolbar toolbar;
    private G3RestaurantObj mRestaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().findViewById(R.id.menubutton).setClickable(false);

        mCtx = this.getContext();

        v = inflater.inflate(R.layout.user_menu_restaurant_fragment, container, false);

//        //Setting Toolbar as Action Bar
//        toolbar =  (Toolbar) v.findViewById(R.id.toolbar);
//
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
//
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setHasOptionsMenu(true);

        loadView();

        initData();


        return v;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                FragmentManager fm = getFragmentManager();
//                if (fm.getBackStackEntryCount() > 0) {
//                    Log.i("MainActivity", "popping backstack");
//                    fm.popBackStack();
//                } else {
//                    Log.i("MainActivity", "nothing on backstack");
//                }
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void initData() {

        LinearLayout categorybar = (LinearLayout)getActivity().findViewById(R.id.categorybar);
        categorybar.setVisibility(View.GONE);
        mTitle = (TextView)getActivity().findViewById(R.id.nestedfragmentlabel);
        mTitle.setText(R.string.menu);

        getActivity().findViewById(R.id.menubutton).setBackgroundColor(ContextCompat.getColor(mCtx, R.color.secondary_text));
        TextView tw = (TextView) getActivity().findViewById(R.id.menutextview);
        tw.setTextColor(ContextCompat.getColor(mCtx, R.color.black));

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerMenu);
        mTotaldish = (LinearLayout) v.findViewById(R.id.dailymenutotal);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
//        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        ItemDecoration idec = new ItemDecoration(this.getContext());
        mRecyclerView.addItemDecoration(idec);

        mRestaurant = ((G3UserRestaurantActivity)getActivity()).getmRestaurantObj();

        ArrayList<String> mDataSet = new ArrayList<>();
        if (mRestaurant.getMenu() != null) {
            HashMap<String,G3MenuItemCategory> mCategories = mRestaurant.getMenu().getCategories();

            ArrayList<String> tempDataSet = new ArrayList<>();
            if(mCategories != null) {

                for(int i=0; i<mCategories.size();i++){
                    tempDataSet.add("");
                }

                for (G3MenuItemCategory temp : mCategories.values()) {
                    tempDataSet.add(temp.getPriority(),temp.getName());
                }

                for(int i=0;i<tempDataSet.size();i++){
                    if(!tempDataSet.get(i).equals("")){
                        mDataSet.add(tempDataSet.get(i));
                    }
                }
            }
        }

        if(mRestaurant.getmDailymenu() != null && mRestaurant.getmDailymenu().getMenuSize() != 0){
            mDataSet.add(0, getResources().getString(R.string.daily_menu));
        }

        mAdapter = new G3MenuUserAdapter(mDataSet);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnCardRestaurantListener(new G3MenuUserAdapter.onCategoryClick() {

            @Override
            public void onCatClick(View v, String category) {
                Fragment mFragment = new G3DishesUserRestaurantFragment();

                FragmentTransaction t=getActivity().getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString("CATEGORY",category);
                mFragment.setArguments(args);
                t.replace(R.id.nestedfragment, mFragment, "USER_FOODS");
//                t.addToBackStack(null);
                t.commit();
            }
        });
        mTotaldish.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().findViewById(R.id.menubutton).setClickable(true);
        getActivity().findViewById(R.id.menubutton).setBackgroundColor(ContextCompat.getColor(mCtx, R.color.light_grey));
        TextView tw = (TextView) getActivity().findViewById(R.id.menutextview);
        tw.setTextColor(ContextCompat.getColor(mCtx, R.color.secondary_text));
    }

    private void loadView() {



    }



}
