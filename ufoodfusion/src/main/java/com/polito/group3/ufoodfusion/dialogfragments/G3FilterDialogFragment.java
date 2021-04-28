package com.polito.group3.ufoodfusion.dialogfragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.utilities.RestaurantFilters;
import com.polito.group3.ufoodfusion.activities.G3MapSearchActivity;
import com.polito.group3.ufoodfusion.activities.G3UserSearchRestaurant;
import com.polito.group3.ufoodfusion.adapters.G3FilterAdapter;
import com.polito.group3.ufoodfusion.fragments.G3BudgetFilterFragment;
import com.polito.group3.ufoodfusion.fragments.G3CusinesFilterFragment;
import com.polito.group3.ufoodfusion.fragments.G3PaymentFilterFragment;

/**
 * Created by Alfonso on 27/04/2016.
 */
public class G3FilterDialogFragment extends DialogFragment {

    private Toolbar toolbar;
    private View parent;
    private final Dialog dialog;
    private RecyclerView mRecycler;
    private LinearLayoutManager mLayoutManager;
    private G3FilterAdapter mAdapter;
    private static RestaurantFilters mFilter;
    String[] toSet;
    public G3FilterDialogFragment() {
        dialog = null;
    }


    public static G3FilterDialogFragment newInstance() {
        return new G3FilterDialogFragment();
    }

    public interface OnCompleteListener {
        public void onCompleteFilters();
    }

    private OnCompleteListener mListener;

    // make sure the Activity implemented it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.dialog_fragment_filter, container, false);
        toSet = getResources().getStringArray(R.array.filters);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mFilter = G3UserSearchRestaurant.getRestaurantFilters() != null ?
                new RestaurantFilters(G3UserSearchRestaurant.getRestaurantFilters()) :
                new RestaurantFilters(G3MapSearchActivity.getRestaurantFilters());
        initView();
        initData();
        setListeners();

        return parent;
    }

    @Override
    public void onResume() {
        super.onResume();
        Fragment fragment = new G3CusinesFilterFragment();
        getChildFragmentManager().beginTransaction()
                .add(R.id.filterFrame, fragment).commit();

    }

    private void initView() {
        toolbar = (Toolbar) parent.findViewById(R.id.toolbar_without_logo);
        mRecycler = (RecyclerView) parent.findViewById(R.id.filterRecycler);

    }

    private void initData() {
        toolbar.setTitle(R.string.filter);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.inflateMenu(R.menu.menu_filter_dialog);
        mRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecycler.setLayoutManager(mLayoutManager);

        mAdapter = new G3FilterAdapter(getContext(),toSet,mLayoutManager);
        mRecycler.setAdapter(mAdapter);
    }

    private void setListeners() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mAdapter.setOnfilterSelectListener(new G3FilterAdapter.onFilterSelect() {
            @Override
            public void onFilterClick(String filter) {
                setFragment(filter);
            }
        });

        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.save) {
                            G3UserSearchRestaurant.setmRestaurantFilters(mFilter);
                            G3MapSearchActivity.setmRestaurantFilters(mFilter);
                            mListener.onCompleteFilters();
                            dismiss();
                            return true;
                        }
                        if (id == R.id.clear) {
                            int r = mFilter.getRadius();
                            mFilter = new RestaurantFilters();
                            mFilter.setRadius(r);
                            onResume();
                            return true;
                        }
                        return false;
                    }
                });

    }

    private void setFragment(String filter) {


        if(filter.equalsIgnoreCase(toSet[0])){
            Fragment fragment = new G3CusinesFilterFragment();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.filterFrame, fragment).commit();

        }
        if(filter.equalsIgnoreCase(toSet[1])){

            Fragment fragment = new G3BudgetFilterFragment();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.filterFrame, fragment).commit();
        }
        if(filter.equalsIgnoreCase(toSet[2])){

            Fragment fragment = new G3PaymentFilterFragment();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.filterFrame, fragment).commit();
        }

    }

    public static RestaurantFilters getFilter(){
        return mFilter;
    }
    public static void setFilter(RestaurantFilters filter){

        mFilter = filter;
    }


}
