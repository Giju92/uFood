package com.example.ufoodrestaurant.dialogfragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

/**
 * Created by Giovanni on 07/06/2016.
 */
public class G3OrdersFilterDialogFragment extends android.support.v4.app.DialogFragment{


    private RadioButton mTakeAwayRadioButton;
    private RadioButton mTableBookingRadioButton;
    private RadioButton mAllTypesRadioButton;
    private RadioButton mPendingRadioButton;
    private RadioButton mCancelledRadioButton;
    private RadioButton mDoneRadioButton;
    private RadioButton mAllStatusesRadioButton;
    private Button mApplyButtom;
    private static String mtypefilter;
    private static String mstatusfilter;


    public static G3OrdersFilterDialogFragment newInstance(Context context, String typefilter, String statusfilter) {
        G3OrdersFilterDialogFragment fragment = new G3OrdersFilterDialogFragment();
        mtypefilter = typefilter;
        mstatusfilter = statusfilter;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(com.example.ufoodlibrary.R.layout.ordersfilter_dialogfragment, container, false);


        mTakeAwayRadioButton=(RadioButton)v.findViewById(com.example.ufoodlibrary.R.id.takeawayradiobutton);
        mTableBookingRadioButton=(RadioButton)v.findViewById(com.example.ufoodlibrary.R.id.tableradiobutton);
        mAllTypesRadioButton=(RadioButton)v.findViewById(com.example.ufoodlibrary.R.id.alltypesradiobutton);
        mPendingRadioButton=(RadioButton)v.findViewById(com.example.ufoodlibrary.R.id.pendingradiobutton);
        mCancelledRadioButton=(RadioButton)v.findViewById(com.example.ufoodlibrary.R.id.cancelledradiobutton);
        mDoneRadioButton=(RadioButton)v.findViewById(com.example.ufoodlibrary.R.id.doneradiobutton);
        mAllStatusesRadioButton=(RadioButton)v.findViewById(com.example.ufoodlibrary.R.id.allstatusesradiobutton);
        mApplyButtom=(Button)v.findViewById(com.example.ufoodlibrary.R.id.applybutton);


        getDialog().setTitle(getResources().getString(com.example.ufoodlibrary.R.string.orders_filter));
        loadData();
        setlisteners();
        return v;
    }

    private void setlisteners() {
        mTakeAwayRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtypefilter="takeaway";
            }
        });
        mTableBookingRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtypefilter="tablebooking";
            }
        });
        mAllTypesRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtypefilter="all";
            }
        });
        mAllStatusesRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mstatusfilter="all";
            }
        });
        mPendingRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mstatusfilter="pending";
            }
        });
        mCancelledRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mstatusfilter="cancelled";
            }
        });
        mDoneRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mstatusfilter="done";
            }
        });


        mApplyButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("type", mtypefilter);
                intent.putExtra("status", mstatusfilter);
                getTargetFragment().onActivityResult(1, 1, intent);
                dismiss();
            }
        });
    }

    private void loadData() {
        switch(mtypefilter){
            case "all":
                mAllTypesRadioButton.setChecked(true);
                break;
            case "takeaway":
                mTakeAwayRadioButton.setChecked(true);
                break;
            case "tablebooking":
                mTableBookingRadioButton.setChecked(true);
                break;
            default:
                break;
        }

        switch(mstatusfilter){
            case "all":
                mAllStatusesRadioButton.setChecked(true);
                break;
            case "pending":
                mPendingRadioButton.setChecked(true);
                break;
            case "cancelled":
                mCancelledRadioButton.setChecked(true);
                break;
            case "done":
                mDoneRadioButton.setChecked(true);
            default:
                break;
        }


    }
}
