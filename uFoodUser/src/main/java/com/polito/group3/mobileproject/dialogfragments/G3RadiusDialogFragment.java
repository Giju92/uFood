package com.polito.group3.mobileproject.dialogfragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.utilities.RestaurantFilters;
import com.polito.group3.mobileproject.activities.G3UserSearchRestaurant;


/**
 * Created by Alfonso-LAPTOP on 05/05/2016.
 */
public class G3RadiusDialogFragment extends DialogFragment {


    private View v;
    private CheckBox m250;
    private CheckBox m500;
    private CheckBox m1;
    private CheckBox m2;
    private CheckBox m5;
    private LinearLayout m250lay;
    private LinearLayout m500lay;
    private LinearLayout m1lay;
    private LinearLayout m2lay;
    private LinearLayout m5lay;
    private RestaurantFilters mFilter;
    private OnCompleteRadiusListener mListener;

    public static G3RadiusDialogFragment newInstance() {

        return new G3RadiusDialogFragment();
    }

    public interface OnCompleteRadiusListener {
        public void onCompleteRadius();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteRadiusListener)activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dialog_radius, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mFilter = G3UserSearchRestaurant.getRestaurantFilters();
        initView();
        initData();
        setListeners();

        return v;
    }

    private void initView() {

        m250 = (CheckBox) v.findViewById(R.id.radio250);
        m500 = (CheckBox) v.findViewById(R.id.radio500);
        m1 = (CheckBox) v.findViewById(R.id.radio1);
        m2 = (CheckBox) v.findViewById(R.id.radio2);
        m5 = (CheckBox) v.findViewById(R.id.radio5);

        m250lay = (LinearLayout) v.findViewById(R.id.lay250);
        m500lay = (LinearLayout) v.findViewById(R.id.lay500);
        m1lay = (LinearLayout) v.findViewById(R.id.lay1);
        m2lay = (LinearLayout) v.findViewById(R.id.lay2);
        m5lay = (LinearLayout) v.findViewById(R.id.lay5);
    }

    private void initData() {

        int radius = mFilter.getRadius();
        switch(radius){
            case 250:
                m250.setChecked(true);
                ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                m500.setChecked(false);
                ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m1.setChecked(false);
                ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m2.setChecked(false);
                ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m5.setChecked(false);
                ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 500:
                m250.setChecked(false);
                ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m500.setChecked(true);
                ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                m1.setChecked(false);
                ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m2.setChecked(false);
                ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m5.setChecked(false);
                ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 1000:
                m250.setChecked(false);
                ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m500.setChecked(false);
                ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m1.setChecked(true);
                ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                m2.setChecked(false);
                ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m5.setChecked(false);
                ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 2000:
                m250.setChecked(false);
                m500.setChecked(false);
                m1.setChecked(false);
                ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m2.setChecked(true);
                ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                m5.setChecked(false);
                ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 5000:
                m250.setChecked(false);
                ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m500.setChecked(false);
                ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m1.setChecked(false);
                ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m2.setChecked(false);
                ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                m5.setChecked(true);
                ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                break;
            default:
                m250.setChecked(false);
                m500.setChecked(false);
                m1.setChecked(false);
                m2.setChecked(false);
                m5.setChecked(false);
                break;
        }

    }

    private void setListeners() {

        m250.setOnClickListener(mlistener);
        m500.setOnClickListener(mlistener);
        m1.setOnClickListener(mlistener);
        m2.setOnClickListener(mlistener);
        m5.setOnClickListener(mlistener);
        m250lay.setOnClickListener(mlistener);
        m500lay.setOnClickListener(mlistener);
        m1lay.setOnClickListener(mlistener);
        m2lay.setOnClickListener(mlistener);
        m5lay.setOnClickListener(mlistener);

    }

    CompoundButton.OnClickListener mlistener = new CompoundButton.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v instanceof CheckBox) {
                int id = v.getId();

                switch (id) {
                    case R.id.radio250:
                        m250.setChecked(true);
                        mFilter.setRadius(250);
//                        ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
//                        m500.setChecked(false);
//                        ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
//                        m1.setChecked(false);
//                        ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
//                        m2.setChecked(false);
//                        ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
//                        m5.setChecked(false);
//                        ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        G3UserSearchRestaurant.setmRestaurantFilters(mFilter);
                        mListener.onCompleteRadius();
                        dismiss();
                        break;
                    case R.id.radio500:
                        m250.setChecked(false);
                        ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m500.setChecked(true);
                        mFilter.setRadius(500);
                        ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        m1.setChecked(false);
                        ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m2.setChecked(false);
                        ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m5.setChecked(false);
                        ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        G3UserSearchRestaurant.setmRestaurantFilters(mFilter);
                        mListener.onCompleteRadius();
                        dismiss();
                        break;
                    case R.id.radio1:
                        m250.setChecked(false);
                        ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m500.setChecked(false);
                        ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m1.setChecked(true);
                        mFilter.setRadius(1000);
                        ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        m2.setChecked(false);
                        ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m5.setChecked(false);
                        ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        G3UserSearchRestaurant.setmRestaurantFilters(mFilter);
                        mListener.onCompleteRadius();
                        dismiss();
                        break;
                    case R.id.radio2:
                        m250.setChecked(false);
                        ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m500.setChecked(false);
                        ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m1.setChecked(false);
                        ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m2.setChecked(true);
                        mFilter.setRadius(2000);
                        ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        m5.setChecked(false);
                        ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        G3UserSearchRestaurant.setmRestaurantFilters(mFilter);
                        mListener.onCompleteRadius();
                        dismiss();
                        break;
                    case R.id.radio5:
                        m250.setChecked(false);
                        ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m500.setChecked(false);
                        ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m1.setChecked(false);
                        ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m2.setChecked(false);
                        ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m5.setChecked(true);
                        mFilter.setRadius(5000);
                        ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        G3UserSearchRestaurant.setmRestaurantFilters(mFilter);
                        mListener.onCompleteRadius();
                        dismiss();
                        break;
                    default:
                        break;
                }
            }
            if(v instanceof LinearLayout){
                int id = v.getId();

                switch (id) {
                    case R.id.lay250:
                        m250.setChecked(true);
                        mFilter.setRadius(250);
                        ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        m500.setChecked(false);
                        ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m1.setChecked(false);
                        ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m2.setChecked(false);
                        ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m5.setChecked(false);
                        ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        G3UserSearchRestaurant.setmRestaurantFilters(mFilter);
                        mListener.onCompleteRadius();
                        dismiss();
                        break;
                    case R.id.lay500:
                        m250.setChecked(false);
                        ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m500.setChecked(true);
                        mFilter.setRadius(500);
                        ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        m1.setChecked(false);
                        ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m2.setChecked(false);
                        ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m5.setChecked(false);
                        ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        G3UserSearchRestaurant.setmRestaurantFilters(mFilter);
                        mListener.onCompleteRadius();
                        dismiss();
                        break;
                    case R.id.lay1:
                        m250.setChecked(false);
                        ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m500.setChecked(false);
                        ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m1.setChecked(true);
                        mFilter.setRadius(1000);
                        ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        m2.setChecked(false);
                        ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m5.setChecked(false);
                        ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        G3UserSearchRestaurant.setmRestaurantFilters(mFilter);
                        mListener.onCompleteRadius();
                        dismiss();
                        break;
                    case R.id.lay2:
                        m250.setChecked(false);
                        ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m500.setChecked(false);
                        ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m1.setChecked(false);
                        ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m2.setChecked(true);
                        mFilter.setRadius(2000);
                        ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        m5.setChecked(false);
                        ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        G3UserSearchRestaurant.setmRestaurantFilters(mFilter);
                        mListener.onCompleteRadius();
                        dismiss();
                        break;
                    case R.id.lay5:
                        m250.setChecked(false);
                        ((LinearLayout) m250.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m500.setChecked(false);
                        ((LinearLayout) m500.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m1.setChecked(false);
                        ((LinearLayout) m1.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m2.setChecked(false);
                        ((LinearLayout) m2.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        m5.setChecked(true);
                        mFilter.setRadius(5000);
                        ((LinearLayout) m5.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        G3UserSearchRestaurant.setmRestaurantFilters(mFilter);
                        mListener.onCompleteRadius();
                        dismiss();
                        break;
                    default:
                        break;
                }
            }
        }

    };



}
