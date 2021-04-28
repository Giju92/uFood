package com.polito.group3.mobileproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.utilities.RestaurantFilters;
import com.polito.group3.mobileproject.dialogfragments.G3FilterDialogFragment;

/**
 * Created by Alfonso on 27/04/2016.
 */
public class G3BudgetFilterFragment extends Fragment{

    private Context mCtx;
    private View v;
    private RestaurantFilters mFilter;
    private CheckBox m1euro;
    private CheckBox m2euro;
    private CheckBox m3euro;
    private LinearLayout m1eurolay;
    private LinearLayout m2eurolay;
    private LinearLayout m3eurolay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCtx = this.getContext();
        v = inflater.inflate(R.layout.fragment_filter_budget, container, false);
        mFilter = G3FilterDialogFragment.getFilter();
        initView();
        setListeners();
        return v;
    }



    private void initView() {

        m1euro = (CheckBox) v.findViewById(R.id.cb1euro);
        m2euro = (CheckBox) v.findViewById(R.id.cb2euro);
        m3euro = (CheckBox) v.findViewById(R.id.cb3euro);
        m1eurolay = (LinearLayout) v.findViewById(R.id.euro1layout);
        m2eurolay = (LinearLayout) v.findViewById(R.id.euro2layout);
        m3eurolay = (LinearLayout) v.findViewById(R.id.euro3layout);

        if(mFilter.getPriceCatLowFilter()){
            m1euro.setChecked(true);
            ((LinearLayout) m1euro.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
        }
        if(mFilter.getPriceCatMedFilter()){
            m2euro.setChecked(true);
            ((LinearLayout) m2euro.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
        }
        if(mFilter.getPriceCatHighFilter()){
            m3euro.setChecked(true);
            ((LinearLayout) m3euro.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
        }

    }

    private void setListeners() {

        m1euro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilter.setPriceCatLowFilter(isChecked);
                if(isChecked)
                    ((LinearLayout) m1euro.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    ((LinearLayout) m1euro.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        m2euro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilter.setPriceCatMedFilter(isChecked); //TODO
                if(isChecked)
                    ((LinearLayout) m2euro.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    ((LinearLayout) m2euro.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        m3euro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilter.setPriceCatHighFilter(isChecked);
                if(isChecked)
                    ((LinearLayout) m3euro.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    ((LinearLayout) m3euro.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        m1eurolay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1euro.setChecked(!m1euro.isChecked());
            }
        });
        m2eurolay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m2euro.setChecked(!m2euro.isChecked());
            }
        });
        m3eurolay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m3euro.setChecked(!m3euro.isChecked());
            }
        });

    }

}
