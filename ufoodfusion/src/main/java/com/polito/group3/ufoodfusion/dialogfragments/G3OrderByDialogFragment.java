package com.polito.group3.ufoodfusion.dialogfragments;

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
import android.widget.LinearLayout;

import com.example.ufoodlibrary.R;
import com.polito.group3.ufoodfusion.activities.G3UserSearchRestaurant;

/**
 * Created by Alfonso-LAPTOP on 04/05/2016.
 */
public class G3OrderByDialogFragment extends DialogFragment {

    private View parent;
    private OnCompleteOrderListener mListener;
    private LinearLayout mDistanceLay;
    private LinearLayout mPriceLay;
    private LinearLayout mRankingLay;
    private LinearLayout mAzLay;
    private CheckBox mDistanceCheck;
    private CheckBox mPriceCheck;
    private CheckBox mRankingCheck;
    private CheckBox mAzCheck;
    private int order;


    public G3OrderByDialogFragment() {

    }

    public interface OnCompleteOrderListener {
        public void onCompleteOrdering(int orderby);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteOrderListener)activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    public static G3OrderByDialogFragment newInstance() {

        return new G3OrderByDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.dialog_orderby, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        initView();
        initData();
        setListeners();

        return parent;
    }

    private void initView() {
        mAzCheck = (CheckBox) parent.findViewById(R.id.radioaz);
        mAzLay = (LinearLayout) parent.findViewById(R.id.azlay);
        mDistanceCheck = (CheckBox) parent.findViewById(R.id.radiodistance);
        mDistanceLay = (LinearLayout) parent.findViewById(R.id.distancelay);
        mPriceCheck = (CheckBox) parent.findViewById(R.id.radioprice);
        mPriceLay = (LinearLayout) parent.findViewById(R.id.pricelay);
        mRankingCheck = (CheckBox) parent.findViewById(R.id.radioranking);
        mRankingLay = (LinearLayout) parent.findViewById(R.id.rankinglay);
    }

    private void setListeners() {
        mDistanceLay.setOnClickListener(mlistener);
        mPriceLay.setOnClickListener(mlistener);
        mRankingLay.setOnClickListener(mlistener);
        mAzLay.setOnClickListener(mlistener);
        mDistanceCheck.setOnClickListener(mlistener);
        mPriceCheck.setOnClickListener(mlistener);
        mRankingCheck.setOnClickListener(mlistener);
        mAzCheck.setOnClickListener(mlistener);
    }

    private void initData() {

        order = G3UserSearchRestaurant.getCurrentOrder();

        switch(order){
            case 0:
                mDistanceCheck.setChecked(true);
                ((LinearLayout) mDistanceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                break;
            case 1:
                mPriceCheck.setChecked(true);
                ((LinearLayout) mPriceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                break;
            case 2:
                mRankingCheck.setChecked(true);
                ((LinearLayout) mRankingCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                break;
            case 3:
                mAzCheck.setChecked(true);
                ((LinearLayout) mAzCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                break;
        }

    }

    View.OnClickListener mlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v instanceof CheckBox) {
                int id = v.getId();

                switch (id) {
                    case R.id.radiodistance:
                        mDistanceCheck.setChecked(true);
                        mListener.onCompleteOrdering(0);
                        ((LinearLayout) mDistanceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mPriceCheck.setChecked(false);
                        ((LinearLayout) mPriceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mRankingCheck.setChecked(false);
                        ((LinearLayout) mRankingCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mAzCheck.setChecked(false);
                        ((LinearLayout) mAzCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        dismiss();
                        break;
                    case R.id.radioprice:
                        mDistanceCheck.setChecked(false);
                        ((LinearLayout) mDistanceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mPriceCheck.setChecked(true);
                        mListener.onCompleteOrdering(1);
                        ((LinearLayout) mPriceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mRankingCheck.setChecked(false);
                        ((LinearLayout) mRankingCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mAzCheck.setChecked(false);
                        ((LinearLayout) mAzCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        dismiss();
                        break;
                    case R.id.radioranking:
                        mDistanceCheck.setChecked(false);
                        ((LinearLayout) mDistanceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mPriceCheck.setChecked(false);
                        ((LinearLayout) mPriceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mRankingCheck.setChecked(true);
                        mListener.onCompleteOrdering(2);
                        ((LinearLayout) mRankingCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mAzCheck.setChecked(false);
                        ((LinearLayout) mAzCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        dismiss();
                        break;
                    case R.id.radioaz:
                        mDistanceCheck.setChecked(false);
                        ((LinearLayout) mDistanceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mPriceCheck.setChecked(false);
                        ((LinearLayout) mPriceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mRankingCheck.setChecked(false);
                        ((LinearLayout) mRankingCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mAzCheck.setChecked(true);
                        mListener.onCompleteOrdering(3);
                        ((LinearLayout) mAzCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        dismiss();
                        break;
                }
            }
            if(v instanceof LinearLayout) {
                int id = v.getId();
                switch (id) {
                    case R.id.distancelay:
                        mDistanceCheck.setChecked(true);
                        ((LinearLayout) mDistanceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mPriceCheck.setChecked(false);
                        ((LinearLayout) mPriceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mRankingCheck.setChecked(false);
                        ((LinearLayout) mRankingCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mAzCheck.setChecked(false);
                        ((LinearLayout) mAzCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mListener.onCompleteOrdering(0);
                        dismiss();
                        break;
                    case R.id.pricelay:
                        mDistanceCheck.setChecked(false);
                        ((LinearLayout) mDistanceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mPriceCheck.setChecked(true);
                        ((LinearLayout) mPriceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mRankingCheck.setChecked(false);
                        ((LinearLayout) mRankingCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mAzCheck.setChecked(false);
                        ((LinearLayout) mAzCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mListener.onCompleteOrdering(1);
                        dismiss();
                        break;
                    case R.id.rankinglay:
                        mDistanceCheck.setChecked(false);
                        ((LinearLayout) mDistanceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mPriceCheck.setChecked(false);
                        ((LinearLayout) mPriceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mRankingCheck.setChecked(true);
                        ((LinearLayout) mRankingCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mAzCheck.setChecked(false);
                        ((LinearLayout) mAzCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mListener.onCompleteOrdering(2);
                        dismiss();
                        break;
                    case R.id.azlay:
                        mDistanceCheck.setChecked(false);
                        ((LinearLayout) mDistanceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mPriceCheck.setChecked(false);
                        ((LinearLayout) mPriceCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mRankingCheck.setChecked(false);
                        ((LinearLayout) mRankingCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mAzCheck.setChecked(true);
                        ((LinearLayout) mAzCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mListener.onCompleteOrdering(3);
                        dismiss();
                        break;
                }
            }
        }

    };


}
