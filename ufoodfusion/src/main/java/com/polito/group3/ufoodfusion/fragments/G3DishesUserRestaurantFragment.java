package com.polito.group3.ufoodfusion.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3MenuItem;
import com.example.ufoodlibrary.objects.G3MenuObj;
import com.example.ufoodlibrary.utilities.ItemDecoration;
import com.polito.group3.ufoodfusion.activities.G3UserRestaurantActivity;
import com.polito.group3.ufoodfusion.adapters.G3DishMenuUserAdapter;
import com.polito.group3.ufoodfusion.dialogfragments.G3PurchaseDialogFragment;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alfonso on 23-Apr-16.
 */
public class G3DishesUserRestaurantFragment extends Fragment {

    private Context mCtx;
    private View v;

    private G3MenuObj mMenu;
    private String category;

    public TextView mTitle;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private G3DishMenuUserAdapter mAdapter;
    private LinearLayout mTotaldish;
    private boolean dailymenu = false;
    private ArrayList<G3MenuItem> mDataSet;
    private TextView mDishPriceText;
    private ImageButton mPurchasebutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCtx = this.getContext();

        category = getArguments().getString("CATEGORY");

        if(category.equals(getResources().getString(R.string.daily_menu))){
            dailymenu = true;
        }

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
        categorybar.setVisibility(View.VISIBLE);
        mTitle = (TextView)getActivity().findViewById(R.id.nestedfragmentlabel);
        mTitle.setText(category);

        getActivity().findViewById(R.id.menubutton).setBackgroundColor(ContextCompat.getColor(mCtx, R.color.secondary_text));
        TextView tw = (TextView) getActivity().findViewById(R.id.menutextview);
        tw.setTextColor(ContextCompat.getColor(mCtx, R.color.black));


        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerMenu);
        mTotaldish = (LinearLayout) v.findViewById(R.id.dailymenutotal);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        ItemDecoration idec = new ItemDecoration(this.getContext());
        mRecyclerView.addItemDecoration(idec);

        mDataSet = new ArrayList<>();

        double dailyprice=0;
        String ingredient= "";
        if(dailymenu){
            List<G3MenuItem> mList =  ((G3UserRestaurantActivity) getActivity()).getmRestaurantObj().getmDailymenu().getMenuitems();
            boolean first = true;
            for (G3MenuItem temp : mList) {
                dailyprice += temp.getPrice();
                if(first){
                    ingredient += temp.getName();
                    first = false;
                }
                else{
                    ingredient += ", "+temp.getName();
                }

                mDataSet.add(temp);
            }
            mAdapter = new G3DishMenuUserAdapter(mDataSet, mCtx,true);
            mTotaldish.setVisibility(View.VISIBLE);
            mDishPriceText = (TextView) mTotaldish.findViewById(R.id.dishpricetext);
            mPurchasebutton = (ImageButton) mTotaldish.findViewById(R.id.purchasebutton);
            mDishPriceText.setText(String.format("%.2f", dailyprice) + " €");
            final double finalDailyprice = dailyprice;
            final String finalIngredient = ingredient;
            mPurchasebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    G3MenuItem item = new G3MenuItem();
                    item.setName(getResources().getString(R.string.daily_menu));
                    item.setPrice(finalDailyprice);
                    item.setIngredients(finalIngredient);
                    G3PurchaseDialogFragment mDialogFrag;
                    mDialogFrag = G3PurchaseDialogFragment.newInstance(item);
                    mDialogFrag.setCancelable(true);
                    mDialogFrag.show(getFragmentManager(), "dialogadditemtocart");
                }
            });
        }
        else{
            mTotaldish.setVisibility(View.INVISIBLE);

            mMenu = ((G3UserRestaurantActivity) getActivity()).getmRestaurantObj().getMenu();

            for (G3MenuItem temp : mMenu.getMenuItems()) {
                if (temp.getCategory().equals(category) && temp.isAvailable()) {
                    mDataSet.add(temp);
                }

            }
            mAdapter = new G3DishMenuUserAdapter(mDataSet, mCtx,false);
        }




        mAdapter.setOnAddItemToCartListener(new G3DishMenuUserAdapter.onAddItemToCart() {
            @Override
            public void addItem(G3MenuItem item) {

                G3PurchaseDialogFragment mDialogFrag;
                mDialogFrag = G3PurchaseDialogFragment.newInstance(item);
                mDialogFrag.setCancelable(true);
                mDialogFrag.show(getFragmentManager(), "dialogadditemtocart");

            }
        });
        mRecyclerView.setAdapter(mAdapter);



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().findViewById(R.id.menubutton).setBackgroundColor(ContextCompat.getColor(mCtx, R.color.light_grey));
        TextView tw = (TextView) getActivity().findViewById(R.id.menutextview);
        tw.setTextColor(ContextCompat.getColor(mCtx, R.color.secondary_text));
    }

    private void loadView() {


    }


}
