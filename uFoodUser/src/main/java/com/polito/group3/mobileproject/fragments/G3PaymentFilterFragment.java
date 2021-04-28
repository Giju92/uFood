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
public class G3PaymentFilterFragment extends Fragment{

    private Context mCtx;
    private View v;
    private CheckBox mCrediCard;
    private CheckBox mMoney;
    private CheckBox mBancomant;
    private CheckBox mTicket;
    private LinearLayout mCrediCardLay;
    private LinearLayout mMoneyLay;
    private LinearLayout mBancomantLay;
    private LinearLayout mTicketLay;
    private RestaurantFilters mFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCtx = this.getContext();
        v = inflater.inflate(R.layout.fragment_payment_filter, container, false);
        mFilter = G3FilterDialogFragment.getFilter();
        initView();
        setListeners();

        return v;
    }

    private void setListeners() {

        mBancomant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilter.setAcceptBancomatFilter(isChecked);
                if(isChecked)
                    ((LinearLayout) mBancomant.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    ((LinearLayout) mBancomant.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
        mMoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilter.setAcceptMoneyFilter(isChecked);
                if(isChecked)
                    ((LinearLayout) mMoney.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    ((LinearLayout) mMoney.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
        mCrediCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilter.setAcceptCreditCardFilter(isChecked);
                if(isChecked)
                    ((LinearLayout) mCrediCard.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    ((LinearLayout) mCrediCard.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
        mTicket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilter.setAcceptTicketFilter(isChecked);
                if(isChecked)
                    ((LinearLayout) mTicket.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    ((LinearLayout) mTicket.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        mBancomantLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBancomant.setChecked(!mBancomant.isChecked());
            }
        });
        mMoneyLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoney.setChecked(!mMoney.isChecked());
            }
        });
        mTicketLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTicket.setChecked(!mTicket.isChecked());
            }
        });
        mCrediCardLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrediCard.setChecked(!mCrediCard.isChecked());
            }
        });
    }

    private void initView() {
        
        mBancomant = (CheckBox) v.findViewById(R.id.cbbancomat);
        mCrediCard = (CheckBox) v.findViewById(R.id.cbcreditcard);
        mMoney = (CheckBox) v.findViewById(R.id.cbmoney);
        mTicket = (CheckBox) v.findViewById(R.id.cbticket);
        mCrediCardLay = (LinearLayout) v.findViewById(R.id.creditlay);
        mBancomantLay = (LinearLayout) v.findViewById(R.id.bancomatlay);
        mMoneyLay = (LinearLayout) v.findViewById(R.id.moneylay);
        mTicketLay = (LinearLayout) v.findViewById(R.id.ticketlay);

        if(mFilter.getAcceptBancomatFilter()){
            mBancomant.setChecked(true);
            ((LinearLayout) mBancomant.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
        }
        if(mFilter.getAcceptCreditCardFilter()){
            mCrediCard.setChecked(true);
            ((LinearLayout) mCrediCard.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
        }
        if(mFilter.getAcceptMoneyFilter()){
            mMoney.setChecked(true);
            ((LinearLayout) mMoney.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
        }
        if(mFilter.getAcceptTicketFilter()){
            mTicket.setChecked(true);
            ((LinearLayout) mTicket.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
        }

    }
}
