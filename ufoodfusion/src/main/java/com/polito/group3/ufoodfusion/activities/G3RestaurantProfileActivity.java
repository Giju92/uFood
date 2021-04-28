package com.polito.group3.ufoodfusion.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3OpeningHoursObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.ufoodfusion.R;
import com.polito.group3.ufoodfusion.dialogfragments.G3ConnectonDownDialogFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Mattia on 21/05/2016.
 */
public class G3RestaurantProfileActivity extends G3BaseActivity implements G3ConnectonDownDialogFragment.onTryConnect{

    private G3RestaurantObj mRestaurant;

    private Context mCtx;

    private LinearLayout mSiteLinearLayout;
    private LinearLayout mPaymentLinearLayout;
    private LinearLayout mTableBookingLinearLayout;

    private ImageView mToolbarPic;

    private TextView mRestNameAndTypeText;

    private TextView mAddressLabel;
    private TextView mAddressText;
    private TextView mPhoneLabel;
    private TextView mMobilePhoneText;
    private TextView mPhoneText;
    private TextView mEmailLabel;
    private TextView mEmailText;
    private TextView mWebSiteLabel;
    private TextView mWebSiteText;
    private TextView mSeatsLabel;
    private TextView mSeatsText;
    private TextView mServiceTimeText;
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

    private TextView mPriceRatingText;

    private TextView mPaymentMethodsLabel;
    private TextView mMoneyLabel;
    private TextView mCreditCardLabel;
    private TextView mBancomatLabel;
    private TextView mTicketLabel;
    private TextView mSupportedTicketsText;
    private ProgressDialog progress;
    private G3RestaurantObj mRestaurantObj;

    private File localfile;
    private G3ConnectonDownDialogFragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ufoodlibrary.R.layout.activity_profile_restaurant);
        final Toolbar toolbar = (Toolbar) findViewById(com.example.ufoodlibrary.R.id.collapse_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(com.example.ufoodlibrary.R.id.collapse_toolbar_layout);
        collapsingToolbar.setTitle(getString(com.example.ufoodlibrary.R.string.profile));
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.ToolbarTheme);

        mCtx = this.getApplicationContext();
        loadView();
        loadDataRestaurant();

    }

    private void loadDataRestaurant() {

        progress = TransitionHelper.getProgress(this);
        progress.show();

        try {
            G3Application.fManager.getRestaurantProfile(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mRestaurant = dataSnapshot.getValue(G3RestaurantObj.class);
                    initData();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),databaseError.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            if(progress.isShowing())
                progress.dismiss();
            showDialogNetworkDown();
        }
    }

    private void showDialogNetworkDown() {

        newFragment = G3ConnectonDownDialogFragment.newInstance();
        newFragment.setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
        newFragment.show(getSupportFragmentManager(),"");
    }

    private void loadView() {
        mToolbarPic = (ImageView) findViewById(com.example.ufoodlibrary.R.id.restpic);

        mRestNameAndTypeText = (TextView) findViewById(com.example.ufoodlibrary.R.id.restnameandtypetext);

        mSiteLinearLayout = (LinearLayout)  findViewById(com.example.ufoodlibrary.R.id.sitelinearlayout);
        mPaymentLinearLayout =  (LinearLayout)  findViewById(com.example.ufoodlibrary.R.id.paymentlinearlayout);
        mTableBookingLinearLayout = (LinearLayout) findViewById(com.example.ufoodlibrary.R.id.tablebookinglinearlayout);

        mAddressLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.addresslabel);
        mAddressText = (TextView) findViewById(com.example.ufoodlibrary.R.id.addresstext);
        mPhoneLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.phonelabel);
        mMobilePhoneText = (TextView) findViewById(com.example.ufoodlibrary.R.id.mobilephonetext);
        mPhoneText = (TextView) findViewById(com.example.ufoodlibrary.R.id.phonetext);
        mEmailLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.emaillabel);
        mEmailText = (TextView) findViewById(com.example.ufoodlibrary.R.id.emailtext);
        mWebSiteLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.websitelabel);
        mWebSiteText = (TextView) findViewById(com.example.ufoodlibrary.R.id.websitetext);
        mOpeningHoursLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.openinghourslabel);
        mMondayLabel  = (TextView) findViewById(com.example.ufoodlibrary.R.id.mondaylabel);
        mTuesdayLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.tuesdaylabel);
        mWednesdayLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.wednesdaylabel);
        mThursdayLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.thursdaylabel);
        mFridayLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.fridaylabel);
        mSaturdayLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.saturdaylabel);
        mSundayLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.sundaylabel);

        mOpenLunchLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.openlunchlabel);
        mOpenMonLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.openmonlunchtext);
        mOpenTueLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.opentuelunchtext);
        mOpenWedLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.openwedlunchtext);
        mOpenThuLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.openthulunchtext);
        mOpenFriLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.openfrilunchtext);
        mOpenSatLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.opensatlunchtext);
        mOpenSunLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.opensunlunchtext);

        mCloseLunchLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.closelunchlabel);
        mCloseMonLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closemonlunchtext);
        mCloseTueLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closetuelunchtext);
        mCloseWedLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closewedlunchtext);
        mCloseThuLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closethulunchtext);
        mCloseFriLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closefrilunchtext);
        mCloseSatLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closesatlunchtext);
        mCloseSunLunchText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closesunlunchtext);

        mOpenDinnerLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.opendinnerlabel);
        mOpenMonDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.openmondinnertext);
        mOpenTueDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.opentuedinnertext);
        mOpenWedDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.openweddinnertext);
        mOpenThuDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.openthudinnertext);
        mOpenFriDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.openfridinnertext);
        mOpenSatDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.opensatdinnertext);
        mOpenSunDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.opensundinnertext);

        mCloseDinnerLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.closedinnerlabel);
        mCloseMonDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closemondinnertext);
        mCloseTueDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closetuedinnertext);
        mCloseWedDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closeweddinnertext);
        mCloseThuDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closethudinnertext);
        mCloseFriDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closefridinnertext);
        mCloseSatDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closesatdinnertext);
        mCloseSunDinnerText = (TextView) findViewById(com.example.ufoodlibrary.R.id.closesundinnertext);

        mSeatsLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.seatslabel);
        mSeatsText = (TextView) findViewById(com.example.ufoodlibrary.R.id.seatstext);
        mServiceTimeText = (TextView) findViewById(com.example.ufoodlibrary.R.id.servicetimetext);

        mPriceRatingText = (TextView) findViewById(com.example.ufoodlibrary.R.id.priceratingtext);

        mPaymentMethodsLabel  = (TextView) findViewById(com.example.ufoodlibrary.R.id.paymentmethodslabel);
        mMoneyLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.moneylabel);
        mCreditCardLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.creditcardlabel);
        mBancomatLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.bancomatlabel);
        mTicketLabel = (TextView) findViewById(com.example.ufoodlibrary.R.id.ticketlabel);
        mSupportedTicketsText = (TextView) findViewById(com.example.ufoodlibrary.R.id.supportedticketstext);

    }

    private void initData() {

        if(mRestaurant != null) {
            String mImagePath = mRestaurant.getPhotoPath();
            if(mImagePath != null && mImagePath.length()>0)
            {
                Picasso.with(G3Application.getAppContext()).load(mImagePath).placeholder(R.drawable.default_restaurant)
                        .into(mToolbarPic);
            }else {
                Picasso.with(G3Application.getAppContext()).load(R.drawable.default_restaurant)
                        .into(mToolbarPic);
            }

            mRestNameAndTypeText.setText(mRestaurant.getName() + " (" +  mRestaurant.getCategoryAsEnum().getString() + ")");
            mAddressLabel.setText(com.example.ufoodlibrary.R.string.addresslabel);
            mAddressText.setText(mRestaurant.getAddressObj().getShortAddress()  + " - " + mRestaurant.getAddressObj().getCity());

            mPhoneLabel.setText(com.example.ufoodlibrary.R.string.phonelabel);
            if(mRestaurant.getMobileNumber() != null) {
                if (!mRestaurant.getMobileNumber().equals("")) {
                    mMobilePhoneText.setText(mRestaurant.getMobileNumber());
                } else {
                    mMobilePhoneText.setVisibility(View.GONE);
                }
            } else {
                mMobilePhoneText.setVisibility(View.GONE);
            }
            if(mRestaurant.getPhoneNumber() != null) {
                if (!mRestaurant.getPhoneNumber().equals("")) {
                    mPhoneText.setText(mRestaurant.getPhoneNumber());
                } else {
                    mPhoneText.setVisibility(View.GONE);
                }
            }
            mEmailLabel.setText(com.example.ufoodlibrary.R.string.Email);
            mEmailText.setText(mRestaurant.getEmail());

            if (mRestaurant.getWebsite() != null && !mRestaurant.getWebsite().equals("")) {
                mWebSiteLabel.setText(com.example.ufoodlibrary.R.string.Website);
                mWebSiteText.setText(mRestaurant.getWebsite());
            } else {
                mSiteLinearLayout.setVisibility(View.GONE);
                mWebSiteLabel.setVisibility(View.GONE);
                mWebSiteText.setVisibility(View.GONE);
            }

            if(mRestaurant.isAllowTableBooking()){
                mSeatsLabel.setVisibility(View.VISIBLE);
                mSeatsText.setText(String.valueOf(mRestaurant.getSeats()));
                mSeatsText.setVisibility(View.VISIBLE);
            } else{
                mTableBookingLinearLayout.setVisibility(View.GONE);
                mSeatsLabel.setVisibility(View.GONE);
                mSeatsText.setVisibility(View.GONE);
            }

            String servicetime;

            if(mRestaurant.getServiceTime() > 0 && mRestaurant.getServiceTime()<60) {
                servicetime = String.valueOf(mRestaurant.getServiceTime()) + " " + getResources().getString(com.example.ufoodlibrary.R.string.minutes);
                mServiceTimeText.setText(servicetime);
            } else {
                if(mRestaurant.getServiceTime()%60 == 0) {
                    servicetime = String.valueOf(mRestaurant.getServiceTime() / 60) + " " + getResources().getString(com.example.ufoodlibrary.R.string.hours);
                    mServiceTimeText.setText(servicetime);
                } else {
                    servicetime = String.valueOf(mRestaurant.getServiceTime() / 60) + " " + getResources().getString(com.example.ufoodlibrary.R.string.hours) + " and " + mRestaurant.getServiceTime() % 60 + " " + getResources().getString(com.example.ufoodlibrary.R.string.minutes);
                    mServiceTimeText.setText(servicetime);
                }
            }

            mOpeningHoursLabel.setText(com.example.ufoodlibrary.R.string.openinghours);
            mMondayLabel.setText(com.example.ufoodlibrary.R.string.mon);
            mTuesdayLabel.setText(com.example.ufoodlibrary.R.string.tue);
            mWednesdayLabel.setText(com.example.ufoodlibrary.R.string.wed);
            mThursdayLabel.setText(com.example.ufoodlibrary.R.string.thu);
            mFridayLabel.setText(com.example.ufoodlibrary.R.string.fri);
            mSaturdayLabel.setText(com.example.ufoodlibrary.R.string.sat);
            mSundayLabel.setText(com.example.ufoodlibrary.R.string.sun);

            mOpenLunchLabel.setText(com.example.ufoodlibrary.R.string.open);
            mCloseLunchLabel.setText(com.example.ufoodlibrary.R.string.close);
            mOpenDinnerLabel.setText(com.example.ufoodlibrary.R.string.open);
            mCloseDinnerLabel.setText(com.example.ufoodlibrary.R.string.close);

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
            switch (day) {
                case Calendar.MONDAY:
                    mMondayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenMonLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseMonLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenMonDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseMonDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    break;
                case Calendar.TUESDAY:
                    mTuesdayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenTueLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseTueLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenTueDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseTueDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    break;
                case Calendar.WEDNESDAY:
                    mWednesdayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenWedLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseWedLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenWedDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseWedDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    break;
                case Calendar.THURSDAY:
                    mThursdayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenThuLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseThuLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenThuDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseThuDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    break;
                case Calendar.FRIDAY:
                    mFridayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenFriLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseFriLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenFriDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseFriDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    break;
                case Calendar.SATURDAY:
                    mSaturdayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenSatLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseSatLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenSatDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseSatDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    break;
                case Calendar.SUNDAY:
                    mSundayLabel.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenSunLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseSunLunchText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mOpenSunDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    mCloseSunDinnerText.setBackgroundColor(ContextCompat.getColor(mCtx, com.example.ufoodlibrary.R.color.accent));
                    break;
            }

            int pricerating = mRestaurant.getPriceRating();

            switch (pricerating){
                case 1:
                    mPriceRatingText.setText("€");
                    break;
                case 2:
                    mPriceRatingText.setText("€€");
                    break;
                case 3:
                    mPriceRatingText.setText("€€€");
                    break;
                default:
                    break;
            }





            if (mRestaurant.getPaymentMethods() != null) {
                mPaymentMethodsLabel.setText(com.example.ufoodlibrary.R.string.paymentmethods);
                mPaymentMethodsLabel.setVisibility(View.VISIBLE);

                if (mRestaurant.getPaymentMethods().isMoney()) {
                    mMoneyLabel.setText(com.example.ufoodlibrary.R.string.money);
                    mMoneyLabel.setVisibility(View.VISIBLE);
                } else {
                    mMoneyLabel.setVisibility(View.GONE);
                }

                if (mRestaurant.getPaymentMethods().isCreditCard()) {
                    mCreditCardLabel.setText(com.example.ufoodlibrary.R.string.credit_card);
                    mCreditCardLabel.setVisibility(View.VISIBLE);
                } else {
                    mCreditCardLabel.setVisibility(View.GONE);
                }

                if (mRestaurant.getPaymentMethods().isBancomat()) {
                    mBancomatLabel.setText(com.example.ufoodlibrary.R.string.bancomat);
                    mBancomatLabel.setVisibility(View.VISIBLE);
                } else {
                    mBancomatLabel.setVisibility(View.GONE);
                }

                if (mRestaurant.getPaymentMethods().isTicket()) {
                    mTicketLabel.setText(com.example.ufoodlibrary.R.string.ticket);
                    mTicketLabel.setVisibility(View.VISIBLE);
                    if (mRestaurant.getPaymentMethods().getTicketsSupported() != null) {
                        mSupportedTicketsText.setText(mRestaurant.getPaymentMethods().getTicketsSupported());
                        mSupportedTicketsText.setVisibility(View.VISIBLE);
                    }
                } else {
                    mTicketLabel.setVisibility(View.GONE);
                    mSupportedTicketsText.setVisibility(View.GONE);
                }
            } else{
                mPaymentLinearLayout.setVisibility(View.GONE);
                mPaymentMethodsLabel.setVisibility(View.GONE);
                mMoneyLabel.setVisibility(View.GONE);
                mCreditCardLabel.setVisibility(View.GONE);
                mBancomatLabel.setVisibility(View.GONE);
                mTicketLabel.setVisibility(View.GONE);
                mSupportedTicketsText.setVisibility(View.GONE);
            }
        }

        if(progress.isShowing()){
            progress.dismiss();
        }

    }


    private String getformattedhoursint(int hour) {
        DecimalFormat df = new DecimalFormat("00");
        return (df.format(hour/100) + ":" + df.format(hour%100));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_restaurant_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case com.example.ufoodlibrary.R.id.showEdit:
                openEdit();
                return true;
            case android.R.id.home:
                Intent intent = new Intent(this, G3TabsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void openEdit() {
        Intent intent = new Intent(this, G3EditRestaurantProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTryClick() {

        loadDataRestaurant();
    }
}
