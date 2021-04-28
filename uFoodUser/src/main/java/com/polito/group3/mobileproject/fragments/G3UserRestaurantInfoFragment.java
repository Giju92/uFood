package com.polito.group3.mobileproject.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3OpeningHoursObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.polito.group3.mobileproject.activities.G3UserRestaurantActivity;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Mattia on 27/04/2016.
 */
public class G3UserRestaurantInfoFragment extends android.support.v4.app.Fragment {

    private View v;
    private Context mCtx;
    private G3RestaurantObj mRestaurant;

    private TextView mTitle;


    private TextView mAddressLabel;
    private TextView mAddressText;
    private TextView mPhoneLabel;
    private TextView mMobilePhoneText;
    private TextView mPhoneText;
    private TextView mEmailLabel;
    private TextView mEmailText;
    private TextView mWebSiteLabel;
    private TextView mWebSiteText;
    private TextView mOpeningHoursLabel;
    private TextView mMondayLabel;
    private TextView mTuesdayLabel;
    private TextView mWednesdayLabel;
    private TextView mThursdayLabel;
    private TextView mFridayLabel;
    private TextView mSaturdayLabel;
    private TextView mSundayLabel;

    private TextView mOpenLunchLabel;
    private TextView mOpenMonLunchText;
    private TextView mOpenTueLunchText;
    private TextView mOpenWedLunchText;
    private TextView mOpenThuLunchText;
    private TextView mOpenFriLunchText;
    private TextView mOpenSatLunchText;
    private TextView mOpenSunLunchText;

    private TextView mCloseLunchLabel;
    private TextView mCloseMonLunchText;
    private TextView mCloseTueLunchText;
    private TextView mCloseWedLunchText;
    private TextView mCloseThuLunchText;
    private TextView mCloseFriLunchText;
    private TextView mCloseSatLunchText;
    private TextView mCloseSunLunchText;

    private TextView mOpenDinnerLabel;
    private TextView mOpenMonDinnerText;
    private TextView mOpenTueDinnerText;
    private TextView mOpenWedDinnerText;
    private TextView mOpenThuDinnerText;
    private TextView mOpenFriDinnerText;
    private TextView mOpenSatDinnerText;
    private TextView mOpenSunDinnerText;

    private TextView mCloseDinnerLabel;
    private TextView mCloseMonDinnerText;
    private TextView mCloseTueDinnerText;
    private TextView mCloseWedDinnerText;
    private TextView mCloseThuDinnerText;
    private TextView mCloseFriDinnerText;
    private TextView mCloseSatDinnerText;
    private TextView mCloseSunDinnerText;

    private TextView mPaymentMethodsLabel;
    private TextView mMoneyLabel;
    private TextView mCreditCardLabel;
    private TextView mBancomatLabel;
    private TextView mTicketLabel;
    private TextView mSupportedTicketsText;

    private LinearLayout mWebSiteLinearLayout;
    private LinearLayout mPaymentLinearLayout;

    SupportMapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        getActivity().findViewById(R.id.infobutton).setClickable(false);

        mCtx = this.getContext();

        v = inflater.inflate(R.layout.user_restaurant_info_fragment, container, false);

        loadView();

        initData();

        setMapFragment();

        return v;
    }

    private void setMapFragment() {

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {

                CameraPosition position = CameraPosition.builder()
                        .target(new LatLng(mRestaurant.getAddressObj().getLatitude(),
                                mRestaurant.getAddressObj().getLongitude()))
                        .zoom(16f)
                        .bearing(0.0f)
                        .tilt(0.0f)
                        .build();

                map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(mRestaurant.getAddressObj().getLatitude(),
                                mRestaurant.getAddressObj().getLongitude()))
                        .title(mRestaurant.getName()));
            }
        });

    }

    private void loadView() {

        mAddressLabel = (TextView) v.findViewById(R.id.addresslabel);
        mAddressText = (TextView) v.findViewById(R.id.addresstext);
        mPhoneLabel = (TextView) v.findViewById(R.id.phonelabel);
        mMobilePhoneText = (TextView) v.findViewById(R.id.mobilephonetext);
        mPhoneText = (TextView) v.findViewById(R.id.phonetext);
        mEmailLabel = (TextView) v.findViewById(R.id.emaillabel);
        mEmailText = (TextView) v.findViewById(R.id.emailtext);
        mWebSiteLabel = (TextView) v.findViewById(R.id.websitelabel);
        mWebSiteText = (TextView) v.findViewById(R.id.websitetext);
        mOpeningHoursLabel = (TextView) v.findViewById(R.id.openinghourslabel);
        mMondayLabel  = (TextView) v.findViewById(R.id.mondaylabel);
        mTuesdayLabel = (TextView) v.findViewById(R.id.tuesdaylabel);
        mWednesdayLabel = (TextView) v.findViewById(R.id.wednesdaylabel);
        mThursdayLabel = (TextView) v.findViewById(R.id.thursdaylabel);
        mFridayLabel = (TextView) v.findViewById(R.id.fridaylabel);
        mSaturdayLabel = (TextView) v.findViewById(R.id.saturdaylabel);
        mSundayLabel = (TextView) v.findViewById(R.id.sundaylabel);

        mOpenLunchLabel = (TextView) v.findViewById(R.id.openlunchlabel);
        mOpenMonLunchText = (TextView) v.findViewById(R.id.openmonlunchtext);
        mOpenTueLunchText = (TextView) v.findViewById(R.id.opentuelunchtext);
        mOpenWedLunchText = (TextView) v.findViewById(R.id.openwedlunchtext);
        mOpenThuLunchText = (TextView) v.findViewById(R.id.openthulunchtext);
        mOpenFriLunchText = (TextView) v.findViewById(R.id.openfrilunchtext);
        mOpenSatLunchText = (TextView) v.findViewById(R.id.opensatlunchtext);
        mOpenSunLunchText = (TextView) v.findViewById(R.id.opensunlunchtext);

        mCloseLunchLabel = (TextView) v.findViewById(R.id.closelunchlabel);
        mCloseMonLunchText = (TextView) v.findViewById(R.id.closemonlunchtext);
        mCloseTueLunchText = (TextView) v.findViewById(R.id.closetuelunchtext);
        mCloseWedLunchText = (TextView) v.findViewById(R.id.closewedlunchtext);
        mCloseThuLunchText = (TextView) v.findViewById(R.id.closethulunchtext);
        mCloseFriLunchText = (TextView) v.findViewById(R.id.closefrilunchtext);
        mCloseSatLunchText = (TextView) v.findViewById(R.id.closesatlunchtext);
        mCloseSunLunchText = (TextView) v.findViewById(R.id.closesunlunchtext);

        mOpenDinnerLabel = (TextView) v.findViewById(R.id.opendinnerlabel);
        mOpenMonDinnerText = (TextView) v.findViewById(R.id.openmondinnertext);
        mOpenTueDinnerText = (TextView) v.findViewById(R.id.opentuedinnertext);
        mOpenWedDinnerText = (TextView) v.findViewById(R.id.openweddinnertext);
        mOpenThuDinnerText = (TextView) v.findViewById(R.id.openthudinnertext);
        mOpenFriDinnerText = (TextView) v.findViewById(R.id.openfridinnertext);
        mOpenSatDinnerText = (TextView) v.findViewById(R.id.opensatdinnertext);
        mOpenSunDinnerText = (TextView) v.findViewById(R.id.opensundinnertext);

        mCloseDinnerLabel = (TextView) v.findViewById(R.id.closedinnerlabel);
        mCloseMonDinnerText = (TextView) v.findViewById(R.id.closemondinnertext);
        mCloseTueDinnerText = (TextView) v.findViewById(R.id.closetuedinnertext);
        mCloseWedDinnerText = (TextView) v.findViewById(R.id.closeweddinnertext);
        mCloseThuDinnerText = (TextView) v.findViewById(R.id.closethudinnertext);
        mCloseFriDinnerText = (TextView) v.findViewById(R.id.closefridinnertext);
        mCloseSatDinnerText = (TextView) v.findViewById(R.id.closesatdinnertext);
        mCloseSunDinnerText = (TextView) v.findViewById(R.id.closesundinnertext);

        mPaymentMethodsLabel  = (TextView) v.findViewById(R.id.paymentmethodslabel);
        mMoneyLabel = (TextView) v.findViewById(R.id.moneylabel);
        mCreditCardLabel = (TextView) v.findViewById(R.id.creditcardlabel);
        mBancomatLabel = (TextView) v.findViewById(R.id.bancomatlabel);
        mTicketLabel = (TextView) v.findViewById(R.id.ticketlabel);
        mSupportedTicketsText = (TextView) v.findViewById(R.id.supportedticketstext);

        mWebSiteLinearLayout = (LinearLayout) v.findViewById(R.id.websitelinearlayout);
        mPaymentLinearLayout =  (LinearLayout)  v.findViewById(R.id.paymentlinearlayout);


    }


    private void initData() {

        LinearLayout categorybar = (LinearLayout)getActivity().findViewById(R.id.categorybar);
        categorybar.setVisibility(View.GONE);
        mTitle = (TextView)getActivity().findViewById(R.id.nestedfragmentlabel);
        mTitle.setText(R.string.info);

        getActivity().findViewById(R.id.infobutton).setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.secondary_text));
        TextView tw = (TextView) getActivity().findViewById(R.id.infotextview);
        tw.setTextColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.black));


        mRestaurant = ((G3UserRestaurantActivity)getActivity()).getmRestaurantObj();
        mAddressLabel.setText(R.string.addresslabel);
        if(mRestaurant.getAddressObj().getCivic()!=0)
            mAddressText.setText(mRestaurant.getAddressObj().getRoute()+", "+mRestaurant.getAddressObj().getCivic()+" - "+mRestaurant.getAddressObj().getCity());
        else
            mAddressText.setText(mRestaurant.getAddressObj().getRoute()+" - "+mRestaurant.getAddressObj().getCity());
        mPhoneLabel.setText(R.string.phonelabel);
        if(mRestaurant.getMobileNumber() != null) {
            if (!mRestaurant.getMobileNumber().equals("")) {
                mMobilePhoneText.setText(mRestaurant.getMobileNumber());
            } else {
                mMobilePhoneText.setVisibility(View.GONE);
            }
        }
        else{
            mMobilePhoneText.setVisibility(View.GONE);
        }

        if(mRestaurant.getPhoneNumber() != null) {
            if (!mRestaurant.getPhoneNumber().equals("")) {
                mPhoneText.setText(mRestaurant.getPhoneNumber());
            } else {
                mPhoneText.setVisibility(View.GONE);
            }
        }
        else{
            mPhoneText.setVisibility(View.GONE);
        }

        mEmailLabel.setText(R.string.Email);
        mEmailText.setText(mRestaurant.getEmail());

        if(mRestaurant.getWebsite() != null && !mRestaurant.getWebsite().equals("")){
            mWebSiteLabel.setText(R.string.Website);
            mWebSiteText.setText(mRestaurant.getWebsite());
        } else{
            mWebSiteLinearLayout.setVisibility(View.GONE);
            mWebSiteLabel.setVisibility(View.GONE);
            mWebSiteText.setVisibility(View.GONE);
        }

        mOpeningHoursLabel.setText(R.string.openinghours);
        mMondayLabel.setText(R.string.mon);
        mTuesdayLabel.setText(R.string.tue);
        mWednesdayLabel.setText(R.string.wed);
        mThursdayLabel.setText(R.string.thu);
        mFridayLabel.setText(R.string.fri);
        mSaturdayLabel.setText(R.string.sat);
        mSundayLabel.setText(R.string.sun);

        mOpenLunchLabel.setText(R.string.open);
        mCloseLunchLabel.setText(R.string.close);
        mOpenDinnerLabel.setText(R.string.open);
        mCloseDinnerLabel.setText(R.string.close);

        HashMap<String, G3OpeningHoursObj> openinghours = mRestaurant.getOpeningHours();


        //opening hours, to finish
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.MONDAY_LUNCH)){
            mOpenMonLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.MONDAY_LUNCH).getFrom()));
            mCloseMonLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.MONDAY_LUNCH).getTo()));
        } else {
            mOpenMonLunchText.setText("-");
            mCloseMonLunchText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.MONDAY_DINNER)){
            mOpenMonDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.MONDAY_DINNER).getFrom()));
            mCloseMonDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.MONDAY_DINNER).getTo()));
        }else {
            mOpenMonDinnerText.setText("-");
            mCloseMonDinnerText.setText("-");

        }

        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.TUESDAY_LUNCH)){
            mOpenTueLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.TUESDAY_LUNCH).getFrom()));
            mCloseTueLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.TUESDAY_LUNCH).getTo()));
        }else {
            mOpenTueLunchText.setText("-");
            mCloseTueLunchText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.TUESDAY_DINNER)){
            mOpenTueDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.TUESDAY_DINNER).getFrom()));
            mCloseTueDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.TUESDAY_DINNER).getTo()));
        }else {
            mOpenTueDinnerText.setText("-");
            mCloseTueDinnerText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.WEDNESDAY_LUNCH)){
            mOpenWedLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.WEDNESDAY_LUNCH).getFrom()));
            mCloseWedLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.WEDNESDAY_LUNCH).getTo()));
        }else {
            mOpenWedLunchText.setText("-");
            mCloseWedLunchText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.WEDNESDAY_DINNER)){
            mOpenWedDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.WEDNESDAY_DINNER).getFrom()));
            mCloseWedDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.WEDNESDAY_DINNER).getTo()));
        }else {
            mOpenWedDinnerText.setText("-");
            mCloseWedDinnerText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.THURSDAY_LUNCH)){
            mOpenThuLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.THURSDAY_LUNCH).getFrom()));
            mCloseThuLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.THURSDAY_LUNCH).getTo()));
        }else {
            mOpenThuLunchText.setText("-");
            mCloseThuLunchText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.THURSDAY_DINNER)){
            mOpenThuDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.THURSDAY_DINNER).getFrom()));
            mCloseThuDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.THURSDAY_DINNER).getTo()));
        }else {
            mOpenThuDinnerText.setText("-");
            mCloseThuDinnerText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.FRIDAY_LUNCH)){
            mOpenFriLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.FRIDAY_LUNCH).getFrom()));
            mCloseFriLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.FRIDAY_LUNCH).getTo()));
        }else {
            mOpenFriLunchText.setText("-");
            mCloseFriLunchText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.FRIDAY_DINNER)){
            mOpenFriDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.FRIDAY_DINNER).getFrom()));
            mCloseFriDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.FRIDAY_DINNER).getTo()));
        }else {
            mOpenFriDinnerText.setText("-");
            mCloseFriDinnerText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.SATURDAY_LUNCH)){
            mOpenSatLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.SATURDAY_LUNCH).getFrom()));
            mCloseSatLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.SATURDAY_LUNCH).getTo()));
        }else {
            mOpenSatLunchText.setText("-");
            mCloseSatLunchText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.SATURDAY_DINNER)){
            mOpenSatDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.SATURDAY_DINNER).getFrom()));
            mCloseSatDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.SATURDAY_DINNER).getTo()));
        }else {
            mOpenSatDinnerText.setText("-");
            mCloseSatDinnerText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.SUNDAY_LUNCH)){
            mOpenSunLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.SUNDAY_LUNCH).getFrom()));
            mCloseSunLunchText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.SUNDAY_LUNCH).getTo()));
        }else {
            mOpenSunLunchText.setText("-");
            mCloseSunLunchText.setText("-");

        }
        if(mRestaurant.getOpeningHours().containsKey(G3OpeningHoursObj.SUNDAY_DINNER)){
            mOpenSunDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.SUNDAY_DINNER).getFrom()));
            mCloseSunDinnerText.setText(getformattedhoursint(openinghours.get(G3OpeningHoursObj.SUNDAY_DINNER).getTo()));
        }else {
            mOpenSunDinnerText.setText("-");
            mCloseSunDinnerText.setText("-");

        }

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch(day){
            case Calendar.MONDAY:
                mMondayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenMonLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseMonLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenMonDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseMonDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                break;
            case Calendar.TUESDAY:
                mTuesdayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenTueLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseTueLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenTueDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseTueDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                break;
            case Calendar.WEDNESDAY:
                mWednesdayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenWedLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseWedLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenWedDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseWedDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                break;
            case Calendar.THURSDAY:
                mThursdayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenThuLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseThuLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenThuDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseThuDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                break;
            case Calendar.FRIDAY:
                mFridayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenFriLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseFriLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenFriDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseFriDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                break;
            case Calendar.SATURDAY:
                mSaturdayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenSatLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseSatLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenSatDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseSatDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                break;
            case Calendar.SUNDAY:
                mSundayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenSunLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseSunLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mOpenSunDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                mCloseSunDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, R.color.accent));
                break;
        }

        if (mRestaurant.getPaymentMethods() != null) {
            mPaymentMethodsLabel.setText(R.string.paymentmethods);
            mPaymentMethodsLabel.setVisibility(View.VISIBLE);

            if (mRestaurant.getPaymentMethods().isMoney()) {
                mMoneyLabel.setText(R.string.money);
                mMoneyLabel.setVisibility(View.VISIBLE);
            } else{
                mMoneyLabel.setVisibility(View.GONE);
            }

            if (mRestaurant.getPaymentMethods().isCreditCard()) {
                mCreditCardLabel.setText(R.string.credit_card);
                mCreditCardLabel.setVisibility(View.VISIBLE);
            }else{
                mCreditCardLabel.setVisibility(View.GONE);
            }

            if (mRestaurant.getPaymentMethods().isBancomat()) {
                mBancomatLabel.setText(R.string.bancomat);
                mBancomatLabel.setVisibility(View.VISIBLE);
            }else{
                mBancomatLabel.setVisibility(View.GONE);
            }

            if (mRestaurant.getPaymentMethods().isTicket()) {
                mTicketLabel.setText(R.string.ticket);
                mTicketLabel.setVisibility(View.VISIBLE);
                if (mRestaurant.getPaymentMethods().getTicketsSupported() != null) {
                    mSupportedTicketsText.setText(mRestaurant.getPaymentMethods().getTicketsSupported());
                    mSupportedTicketsText.setVisibility(View.VISIBLE);
                }
            } else{
                mTicketLabel.setVisibility(View.GONE);
                mSupportedTicketsText.setVisibility(View.GONE);
            }
        }else{
            mPaymentLinearLayout.setVisibility(View.GONE);
            mPaymentMethodsLabel.setVisibility(View.GONE);
            mMoneyLabel.setVisibility(View.GONE);
            mCreditCardLabel.setVisibility(View.GONE);
            mBancomatLabel.setVisibility(View.GONE);
            mTicketLabel.setVisibility(View.GONE);
            mSupportedTicketsText.setVisibility(View.GONE);
        }

    }

    private String getformattedhoursint(int hour) {
        DecimalFormat df = new DecimalFormat("00");
        return (df.format(hour/100) + ":" + df.format(hour%100));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().findViewById(R.id.infobutton).setClickable(true);
        getActivity().findViewById(R.id.infobutton).setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.light_grey));
        TextView tw = (TextView) getActivity().findViewById(R.id.infotextview);
        tw.setTextColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.secondary_text));
    }



}
