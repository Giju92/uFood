package com.example.ufoodrestaurant.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3AddressObj;
import com.example.ufoodlibrary.objects.G3OpeningHoursObj;
import com.example.ufoodlibrary.objects.G3PaymentMethods;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.BitmapUtils;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.GoogleAPIConstants;
import com.example.ufoodlibrary.utilities.JsonKey;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.example.ufoodrestaurant.activities.G3BaseActivity;
import com.example.ufoodrestaurant.adapters.G3PlaceArrayAdapter;
import com.example.ufoodrestaurant.dialogfragments.G3GetWeekTimeDialogFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import com.example.ufoodlibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Luigi on 11/04/2016.
 */
public class G3EditRestaurantProfileActivity extends G3BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        G3GetWeekTimeDialogFragment.onSaveItemListener {

    private static final int REQUEST_CAMERA = 40;
    private static final int SELECT_FILE = 50;

    private static final String MONCHECK = "monday_check";
    private static final String TUECHECK = "tuesday_check";
    private static final String WEDCHECK = "wednesday_check";
    private static final String THUCHECK = "thursday_check";
    private static final String FRICHECK = "friday_check";
    private static final String SATCHECK = "saturday_check";
    private static final String SUNCHECK = "sunday_check";

    private static final String RESTAURANT_PROFILE = "RESTAURANT_PROFILE";
    private final static int MY_WRITE_PERMISSION = 11;

    private static final String LOG_TAG = "Edit Rest's Profile Act";
    private GoogleApiClient mGoogleApiClient;
    private G3PlaceArrayAdapter mPlaceArrayAdapter;

    private Spinner mSpin;
    private TextView cuisineType;
    private EditText editName;
    private EditText editPhone;
    private EditText editMobile;
    private EditText editWebsite;
    private EditText editSeats;
    private EditText editSvcTime;
    private AutoCompleteTextView editAddress;
    private CheckBox allowtb;
    private CheckBox mMondayCheck;
    private CheckBox mTuesdayCheck;
    private CheckBox mThursdayCheck;
    private CheckBox mWednesdayCheck;
    private CheckBox mFridayCheck;
    private CheckBox mSaturdayCheck;
    private CheckBox mSundayCheck;
    private CheckBox mMoneyCheckbox;
    private CheckBox mBancomatCheckbox;
    private CheckBox mCreditCardCheckbox;
    private CheckBox mTicketCheckbox;
    private TextView mMondayTextLun;
    private TextView mTuesdayTextLun;
    private TextView mWednesdayTextLun;
    private TextView mThursdayTextLun;
    private TextView mSaturdayTextLun;
    private TextView mFridayTextLun;
    private TextView mSundayTextLun;
    private TextView mMondayTextDin;
    private TextView mTuesdayTextDin;
    private TextView mThursdayTextDin;
    private TextView mWednesdayTextDin;
    private TextView mSaturdayTextDin;
    private TextView mFridayTextDin;
    private TextView mServiceTimeLabel;
    private TextView mSeatsLabel;
    private TextView mSundayTextDin;
    private RadioButton mOneEuroRadioButton;
    private RadioButton mTwoEurosRadioButton;
    private RadioButton mThreeEurosRadioButton;

    private TextView mSupportedTicketsLabel;
    private EditText mSupportedTickets;

    private View mFocusView;
    private ImageView mProfileImage;

    private SharedPreferences mShared;

    private HashMap<String, G3OpeningHoursObj> openingHours = new HashMap<>();
    private String mImagePath = null;
    private String mName = null;
    private int mType = -1; // -1 Not set
    private double mLatitude = -1;
    private double mLongitude = -1;
    private String mAddress = null;
    private String mPhone = null;
    private String mMobile = null;
    private String mWebsite = null;
    private String mSeats = null;
    private String mSvcTime = null;
    private boolean mAllowTB = false;
    private int mPriceCategory = 1;
    private G3PaymentMethods mPaymentMethods;
    private Bitmap bmpTosave;
    private ProgressDialog progress;
    private G3RestaurantObj mRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_restaurant);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.collapse_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar_layout);
        collapsingToolbar.setTitle(getString(R.string.edit_restaurant_profile_toolbar_title));
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.ToolbarTheme);

        loadView();
        setListeners();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPemission();
        }

        List<String> list = new ArrayList<String>();
        G3RestaurantObj.restaurantCategory[] cate = G3RestaurantObj.restaurantCategory.values();
        list.add(getResources().getString(R.string.selectcat));
        for (G3RestaurantObj.restaurantCategory c : cate) {
            list.add(c.getString());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(G3Application.getAppContext(),
                R.layout.spinner_edit_restaurant_profile, list);
        mSpin.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        mGoogleApiClient = new GoogleApiClient.Builder(G3EditRestaurantProfileActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GoogleAPIConstants.GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        editAddress.setThreshold(3);
        mPlaceArrayAdapter = new G3PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                null, null);
        editAddress.setAdapter(mPlaceArrayAdapter);

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
                    if(progress.isShowing())
                        progress.dismiss();
                    loadData();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if(progress.isShowing())
                        progress.dismiss();
                    Toast.makeText(getApplicationContext(),databaseError.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            if(progress.isShowing())
                progress.dismiss();
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void loadView() {

        mSpin = (Spinner) findViewById(R.id.spinnercuisine);

        editName = (EditText) findViewById(R.id.editname);
        editPhone = (EditText) findViewById(R.id.editphone);
        editMobile = (EditText) findViewById(R.id.editmobile);
        editWebsite = (EditText) findViewById(R.id.editwww);
        editSeats = (EditText) findViewById(R.id.seats);
        editSvcTime = (EditText) findViewById(R.id.serviceTime);

        cuisineType = (TextView) findViewById(R.id.cuisine);

        editAddress = (AutoCompleteTextView) findViewById(R.id.editaddress);

        allowtb = (CheckBox) findViewById(R.id.allowtb);
        mMoneyCheckbox = (CheckBox) findViewById(R.id.moneycheckbox);
        mBancomatCheckbox = (CheckBox) findViewById(R.id.bancomatcheckbox);
        mCreditCardCheckbox = (CheckBox) findViewById(R.id.creditcardcheckbox);
        mTicketCheckbox = (CheckBox) findViewById(R.id.ticketcheckbox);
        mMondayCheck = (CheckBox) findViewById(R.id.checkMonday);
        mTuesdayCheck = (CheckBox) findViewById(R.id.checkTuesday);
        mWednesdayCheck = (CheckBox) findViewById(R.id.checkWednesday);
        mThursdayCheck = (CheckBox) findViewById(R.id.checkThursday);
        mFridayCheck = (CheckBox) findViewById(R.id.checkFriday);
        mSaturdayCheck = (CheckBox) findViewById(R.id.checkSaturday);
        mSundayCheck = (CheckBox) findViewById(R.id.checkSunday);


        mMondayTextLun = (TextView) findViewById(R.id.hrsMonLunch);
        mTuesdayTextLun = (TextView) findViewById(R.id.hrsTueLunch);
        mThursdayTextLun = (TextView) findViewById(R.id.hrsThuLunch);
        mWednesdayTextLun = (TextView) findViewById(R.id.hrsWedLunch);
        mFridayTextLun = (TextView) findViewById(R.id.hrsFriLunch);
        mSaturdayTextLun = (TextView) findViewById(R.id.hrsSatLunch);
        mSundayTextLun = (TextView) findViewById(R.id.hrsSunLunch);

        mMondayTextDin = (TextView) findViewById(R.id.hrsMonDinner);
        mTuesdayTextDin = (TextView) findViewById(R.id.hrsTueDinner);
        mThursdayTextDin = (TextView) findViewById(R.id.hrsThuDinner);
        mWednesdayTextDin = (TextView) findViewById(R.id.hrsWedDinner);
        mFridayTextDin = (TextView) findViewById(R.id.hrsFriDinner);
        mSaturdayTextDin = (TextView) findViewById(R.id.hrsSatDinner);
        mSundayTextDin = (TextView) findViewById(R.id.hrsSunDinner);

        mServiceTimeLabel = (TextView) findViewById(R.id.servicetimelabel);
        mSeatsLabel = (TextView) findViewById(R.id.seatslabel);

        mProfileImage = (ImageView) findViewById(R.id.restpicture);

        mOneEuroRadioButton = (RadioButton) findViewById(R.id.oneeuroradio);
        mTwoEurosRadioButton = (RadioButton) findViewById(R.id.twoeurosradio);
        mThreeEurosRadioButton = (RadioButton) findViewById(R.id.threeeurosradio);

        mMoneyCheckbox = (CheckBox) findViewById(R.id.moneycheckbox);
        mBancomatCheckbox = (CheckBox) findViewById(R.id.bancomatcheckbox);
        mCreditCardCheckbox = (CheckBox) findViewById(R.id.creditcardcheckbox);
        mTicketCheckbox = (CheckBox) findViewById(R.id.ticketcheckbox);
        mSupportedTicketsLabel = (TextView) findViewById(R.id.supportedticketslabel);
        mSupportedTickets = (EditText) findViewById(R.id.supportedtickets);

    }

    private void setListeners() {

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editName.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mName = s.toString();
            }
        });

        mSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    TextView tx = (TextView) view;
                    if (!tx.getText().equals(getResources().getString(R.string.selectcat))) {
                        mType = G3RestaurantObj.restaurantCategory.getRestaurantCategoryFromString(tx.getText().toString()).getNumber();
                        cuisineType.setError(null);
                    } else
                        mType = -1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().isEmpty()) {
                    mLongitude = -1;
                    mLatitude = -1;
                    editAddress.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editAddress.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editAddress.setOnItemClickListener(mAutocompleteClickListener);

        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editPhone.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editPhone.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mPhone = s.toString();
            }
        });

        editMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editPhone.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editPhone.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mMobile = s.toString();
            }
        });


        editWebsite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mWebsite = s.toString();
            }
        });

        allowtb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editSeats.setVisibility(View.VISIBLE);
                    mSeatsLabel.setVisibility(View.VISIBLE);
                    mAllowTB = true;
                } else {
                    editSeats.setVisibility(View.GONE);
                    mSeatsLabel.setVisibility(View.GONE);
                    mAllowTB = false;
                }
            }
        });

        editSeats.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editSeats.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editSeats.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSeats = s.toString();
            }
        });

        editSvcTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSvcTime = s.toString();
            }
        });

        mOneEuroRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPriceCategory = 1;
            }
        });
        mTwoEurosRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPriceCategory = 2;
            }
        });
        mThreeEurosRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPriceCategory = 3;
            }
        });

        mMoneyCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    mPaymentMethods.setMoney(true);
                } else {
                    mPaymentMethods.setMoney(false);
                }

            }
        });

        mBancomatCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    mPaymentMethods.setBancomat(true);
                } else {
                    mPaymentMethods.setBancomat(false);
                }

            }
        });

        mCreditCardCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    mPaymentMethods.setCreditCard(true);
                } else {
                    mPaymentMethods.setCreditCard(false);
                }

            }
        });

        mTicketCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    mSupportedTicketsLabel.setVisibility(View.VISIBLE);
                    mSupportedTickets.setVisibility(View.VISIBLE);
                    mPaymentMethods.setTicket(true);
                } else {
                    mSupportedTicketsLabel.setVisibility(View.GONE);
                    mSupportedTickets.setVisibility(View.GONE);
                    mPaymentMethods.setTicket(false);
                }

            }
        });

        mSupportedTickets.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPaymentMethods.setTicketsSupported(s.toString());
            }
        });

        mMondayCheck.setOnCheckedChangeListener(this.WeekCheckListener);
        mTuesdayCheck.setOnCheckedChangeListener(this.WeekCheckListener);
        mWednesdayCheck.setOnCheckedChangeListener(this.WeekCheckListener);
        mFridayCheck.setOnCheckedChangeListener(this.WeekCheckListener);
        mThursdayCheck.setOnCheckedChangeListener(this.WeekCheckListener);
        mSaturdayCheck.setOnCheckedChangeListener(this.WeekCheckListener);
        mSundayCheck.setOnCheckedChangeListener(this.WeekCheckListener);



    }

    private void saveData() {
        G3AddressObj addressObj = new G3AddressObj(mAddress, mLatitude, mLongitude);

        int seats = (mSeats != null && !mSeats.isEmpty()) ? Integer.valueOf(mSeats) : 0;
        int svcTime = (mSvcTime != null && !mSvcTime.isEmpty()) ? Integer.valueOf(mSvcTime) : 0;

        mRestaurant.setName(mName);
        mRestaurant.setPhotoPath(mImagePath);
        String cat = G3RestaurantObj.restaurantCategory.getRestaurantCategoryFromInt(mType).name();
        mRestaurant.setCategory(cat);
        mRestaurant.setAddressObj(addressObj);
        mRestaurant.setPhoneNumber(mPhone);
        mRestaurant.setMobileNumber(mMobile);
        mRestaurant.setWebsite(mWebsite);
        mRestaurant.setOpeningHours(openingHours);
        mRestaurant.setAllowTableBooking(mAllowTB);
        mRestaurant.setSeats(seats);
        mRestaurant.setServiceTime(svcTime);
        mRestaurant.setPriceRating(mPriceCategory);
        mRestaurant.setPaymentMethods(mPaymentMethods);

        SaveImage();

    }

    private void SaveImage() {

        progress = TransitionHelper.getProgress(this);
        progress.show();

        if(!G3Application.fManager.isSignedin()){
            progress.dismiss();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorprogress), Toast.LENGTH_SHORT).show();
        }
        String accountid = G3Application.fManager.getCurrentId();
        if(bmpTosave != null){

            try {
                G3Application.fManager.saveImageToFirebaseStorage("img_" + accountid + ".jpg", bmpTosave, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        SaveProfile(null);
                    }
                }, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        SaveProfile(taskSnapshot.getDownloadUrl().toString());
                    }
                });
            } catch (NetworkDownException e) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        else{
            SaveProfile(mImagePath);
        }
    }

    private void SaveProfile(String imagepath) {
        if(mRestaurant != null) {
            mRestaurant.setPhotoPath(imagepath);
            try {
                G3Application.fManager.saveRestaurantProfile(mRestaurant, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.successprogress), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), G3RestaurantProfileActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            } catch (NotAuthenticatedException e) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (NetworkDownException e) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorprogress), Toast.LENGTH_SHORT).show();
        }
    }

    protected CompoundButton.OnCheckedChangeListener WeekCheckListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            final String MONDAY = getResources().getString(R.string.monday);
            final String TUESDAY = getResources().getString(R.string.tuesday);
            final String THURSDAY = getResources().getString(R.string.thursday);
            final String WEDNESDAY = getResources().getString(R.string.wednesday);
            final String FRIDAY = getResources().getString(R.string.friday);
            final String SATURDAY = getResources().getString(R.string.saturday);
            final String SUNDAY = getResources().getString(R.string.sunday);

            if(buttonView.isPressed()) {
                if (isChecked) {
                    G3GetWeekTimeDialogFragment mDialog = G3GetWeekTimeDialogFragment.newInstance(buttonView.getText().toString());
                    mDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
                    mDialog.show(getSupportFragmentManager(), "settime");
                } else {
                    if (buttonView.getText().toString().equals(MONDAY)) {
                        mMondayTextLun.setText(null);
                        mMondayTextDin.setText(null);
                        mMondayTextLun.setError(null);
                        openingHours.remove(getKey(MONDAY) + JsonKey.LUNCHTAG);
                        openingHours.remove(getKey(MONDAY) + JsonKey.DINNERTAG);
                    } else if (buttonView.getText().toString().equals(TUESDAY)) {
                        mTuesdayTextLun.setText(null);
                        mTuesdayTextDin.setText(null);
                        mTuesdayTextLun.setError(null);
                        openingHours.remove(getKey(TUESDAY) + JsonKey.LUNCHTAG);
                        openingHours.remove(getKey(TUESDAY) + JsonKey.DINNERTAG);
                    } else if (buttonView.getText().toString().equals(THURSDAY)) {
                        mThursdayTextLun.setText(null);
                        mThursdayTextDin.setText(null);
                        mThursdayTextLun.setError(null);
                        openingHours.remove(getKey(THURSDAY) + JsonKey.LUNCHTAG);
                        openingHours.remove(getKey(THURSDAY) + JsonKey.DINNERTAG);
                    } else if (buttonView.getText().toString().equals(WEDNESDAY)) {
                        mWednesdayTextLun.setText(null);
                        mWednesdayTextDin.setText(null);
                        mWednesdayTextLun.setError(null);
                        openingHours.remove(getKey(WEDNESDAY) + JsonKey.LUNCHTAG);
                        openingHours.remove(getKey(WEDNESDAY) + JsonKey.DINNERTAG);
                    } else if (buttonView.getText().toString().equals(FRIDAY)) {
                        mFridayTextLun.setText(null);
                        mFridayTextDin.setText(null);
                        mFridayTextLun.setError(null);
                        openingHours.remove(getKey(FRIDAY) + JsonKey.LUNCHTAG);
                        openingHours.remove(getKey(FRIDAY) + JsonKey.DINNERTAG);
                    } else if (buttonView.getText().toString().equals(SATURDAY)) {
                        mSaturdayTextLun.setText(null);
                        mSaturdayTextDin.setText(null);
                        mSaturdayTextLun.setError(null);
                        openingHours.remove(getKey(SATURDAY) + JsonKey.LUNCHTAG);
                        openingHours.remove(getKey(SATURDAY) + JsonKey.DINNERTAG);
                    } else if (buttonView.getText().toString().equals(SUNDAY)) {
                        mSundayTextLun.setText(null);
                        mSundayTextDin.setText(null);
                        mSundayTextLun.setError(null);
                        openingHours.remove(getKey(SUNDAY) + JsonKey.LUNCHTAG);
                        openingHours.remove(getKey(SUNDAY) + JsonKey.DINNERTAG);
                    }
                }
            }

        }
    };

    private void loadData() {



            if (mRestaurant != null) {

                mName = mRestaurant.getName();
                editName.setText(mRestaurant.getName());

                mImagePath = mRestaurant.getPhotoPath();

                if(mImagePath != null && mImagePath.length()>0)
                {
                    Picasso.with(G3Application.getAppContext()).load(mImagePath).placeholder(R.drawable.default_restaurant)
                            .into(mProfileImage);
                }else {
                    Picasso.with(G3Application.getAppContext()).load(R.drawable.default_restaurant)
                            .into(mProfileImage);
                }

                mType = mRestaurant.getCategoryAsEnum().getNumber();
                mSpin.setSelection(mRestaurant.getCategoryAsEnum().getNumber()+1);

                editAddress.setText(mRestaurant.getAddressObj().getLong_address());
                mAddress = mRestaurant.getAddressObj().getLong_address();
                mLatitude = mRestaurant.getAddressObj().getLatitude();
                mLongitude = mRestaurant.getAddressObj().getLongitude();

                mPhone = mRestaurant.getPhoneNumber();
                editPhone.setText(mRestaurant.getPhoneNumber());

                mMobile = mRestaurant.getMobileNumber();
                editMobile.setText(mRestaurant.getMobileNumber());

                mWebsite = mRestaurant.getWebsite();
                editWebsite.setText(mRestaurant.getWebsite());

                openingHours = mRestaurant.getOpeningHours();
                if (openingHours.containsKey(G3OpeningHoursObj.MONDAY_LUNCH) ||
                        openingHours.containsKey(G3OpeningHoursObj.MONDAY_DINNER)) {
                    mMondayCheck.setChecked(true);
                    mMondayTextLun.setText(getLunchHours(openingHours.get(G3OpeningHoursObj.MONDAY_LUNCH)));
                    mMondayTextDin.setText(getDinnerHours(openingHours.get(G3OpeningHoursObj.MONDAY_DINNER)));
                }
                if (openingHours.containsKey(G3OpeningHoursObj.TUESDAY_LUNCH) ||
                        openingHours.containsKey(G3OpeningHoursObj.TUESDAY_DINNER)) {
                    mTuesdayCheck.setChecked(true);
                    mTuesdayTextLun.setText(getLunchHours(openingHours.get(G3OpeningHoursObj.TUESDAY_LUNCH)));
                    mTuesdayTextDin.setText(getDinnerHours(openingHours.get(G3OpeningHoursObj.TUESDAY_DINNER)));
                }
                if (openingHours.containsKey(G3OpeningHoursObj.WEDNESDAY_LUNCH) ||
                        openingHours.containsKey(G3OpeningHoursObj.WEDNESDAY_DINNER)) {
                    mWednesdayCheck.setChecked(true);
                    mWednesdayTextLun.setText(getLunchHours(openingHours.get(G3OpeningHoursObj.WEDNESDAY_LUNCH)));
                    mWednesdayTextDin.setText(getDinnerHours(openingHours.get(G3OpeningHoursObj.WEDNESDAY_DINNER)));
                }
                if (openingHours.containsKey(G3OpeningHoursObj.THURSDAY_LUNCH) ||
                        openingHours.containsKey(G3OpeningHoursObj.THURSDAY_DINNER)) {
                    mThursdayCheck.setChecked(true);
                    mThursdayTextLun.setText(getLunchHours(openingHours.get(G3OpeningHoursObj.THURSDAY_LUNCH)));
                    mThursdayTextDin.setText(getDinnerHours(openingHours.get(G3OpeningHoursObj.THURSDAY_DINNER)));
                }
                if (openingHours.containsKey(G3OpeningHoursObj.FRIDAY_LUNCH) ||
                        openingHours.containsKey(G3OpeningHoursObj.FRIDAY_DINNER)) {
                    mFridayCheck.setChecked(true);
                    mFridayTextLun.setText(getLunchHours(openingHours.get(G3OpeningHoursObj.FRIDAY_LUNCH)));
                    mFridayTextDin.setText(getDinnerHours(openingHours.get(G3OpeningHoursObj.FRIDAY_DINNER)));
                }
                if (openingHours.containsKey(G3OpeningHoursObj.SATURDAY_LUNCH) ||
                        openingHours.containsKey(G3OpeningHoursObj.SATURDAY_DINNER)) {
                    mSaturdayCheck.setChecked(true);
                    mSaturdayTextLun.setText(getLunchHours(openingHours.get(G3OpeningHoursObj.SATURDAY_LUNCH)));
                    mSaturdayTextDin.setText(getDinnerHours(openingHours.get(G3OpeningHoursObj.SATURDAY_DINNER)));
                }
                if (openingHours.containsKey(G3OpeningHoursObj.SUNDAY_LUNCH) ||
                        openingHours.containsKey(G3OpeningHoursObj.SUNDAY_DINNER)) {
                    mSundayCheck.setChecked(true);
                    mSundayTextLun.setText(getLunchHours(openingHours.get(G3OpeningHoursObj.SUNDAY_LUNCH)));
                    mSundayTextDin.setText(getDinnerHours(openingHours.get(G3OpeningHoursObj.SUNDAY_DINNER)));
                }

                mAllowTB = mRestaurant.isAllowTableBooking();
                if (mAllowTB) {
                    allowtb.setChecked(true);
                    editSeats.setVisibility(View.VISIBLE);
                    mSeatsLabel.setVisibility(View.VISIBLE);
                    if (mRestaurant.getSeats() != 0) {
                        editSeats.setText(String.valueOf(mRestaurant.getSeats()));
                        mSeats = String.valueOf(mRestaurant.getSeats());
                    }

                }

                editSvcTime.setText(String.valueOf(mRestaurant.getServiceTime()));

                mPriceCategory = mRestaurant.getPriceRating();


                if(mPriceCategory == 2){
                    mTwoEurosRadioButton.setChecked(true);
                } else {
                    if (mPriceCategory == 3) {
                        mThreeEurosRadioButton.setChecked(true);
                    }  else {
                        mOneEuroRadioButton.setChecked(true);
                    }

                }

                if(mRestaurant.getPaymentMethods() != null) {
                    mPaymentMethods = mRestaurant.getPaymentMethods();
                    if (mPaymentMethods.isMoney()) {
                        mMoneyCheckbox.setChecked(true);
                    } else {
                        mMoneyCheckbox.setChecked(false);
                    }
                    if (mPaymentMethods.isCreditCard()) {
                        mCreditCardCheckbox.setChecked(true);
                    } else {
                        mCreditCardCheckbox.setChecked(false);
                    }
                    if (mPaymentMethods.isBancomat()) {
                        mBancomatCheckbox.setChecked(true);
                    } else {
                        mBancomatCheckbox.setChecked(false);
                    }
                    if (mPaymentMethods.isTicket()) {
                        mTicketCheckbox.setChecked(true);
                        mSupportedTicketsLabel.setVisibility(View.VISIBLE);
                        mSupportedTickets.setVisibility(View.VISIBLE);
                        mSupportedTickets.setText(mPaymentMethods.getTicketsSupported());

                    } else {
                        mTicketCheckbox.setChecked(false);
                        mSupportedTicketsLabel.setVisibility(View.GONE);
                        mSupportedTickets.setVisibility(View.GONE);
                    }
                }

            }

    }


    private String getLunchHours(G3OpeningHoursObj oh) {

        if (oh == null)
            return "";
        int lfh = oh.getFrom() / 100;
        int lfm = oh.getFrom() % 100;
        int lth = oh.getTo() / 100;
        int ltm = oh.getTo() % 100;
        return getString(R.string.lunch) + " " + getString(R.string.from) + " "
                + String.format("%02d:%02d", lfh, lfm) + " " + getString(R.string.to) + " "
                + String.format("%02d:%02d", lth, ltm);
    }

    private String getDinnerHours(G3OpeningHoursObj oh) {

        if (oh == null)
            return "";
        int dfh = oh.getFrom() / 100;
        int dfm = oh.getFrom() % 100;
        int dth = oh.getTo() / 100;
        int dtm = oh.getTo() % 100;
        return getString(R.string.dinner) + " " + getString(R.string.from) + " "
                + String.format("%02d:%02d", dfh, dfm) + " " + getString(R.string.to) + " "
                + String.format("%02d:%02d", dth, dtm);
    }

    private void checkForm() {

        mFocusView = null;

        if (editName.getText().toString().isEmpty()) { // name not correct
            editName.setError(getResources().getString(R.string.nameerror));
            mFocusView = editName;
            mFocusView.requestFocus();
            return;
        }

        if (mType == -1) { // no type select
            cuisineType.setError(getResources().getString(R.string.typeerror));
            mFocusView = cuisineType;
            mFocusView.requestFocus();
            return;
        }

        if (editAddress.getText().toString().isEmpty()) {
            editAddress.setError(getString(R.string.addresserror));
            mFocusView = editAddress;
            mFocusView.requestFocus();
            return;
        } else if (mLatitude == -1 || mLongitude == -1) {
            editAddress.setError(getString(R.string.address_not_found));
            mFocusView = editAddress;
            mFocusView.requestFocus();
            return;
        } else if ((mAddress.split("\\s*,\\s*.")).length <= 2) {
            editAddress.setError(getString(R.string.incomplete_address));
            mFocusView = editAddress;
            mFocusView.requestFocus();
            return;
        }

        if (editPhone.getText().toString().isEmpty() && editMobile.getText().toString().isEmpty()) {
            editPhone.setError(getString(R.string.phoneerror));
            mFocusView = editPhone;
            mFocusView.requestFocus();
            return;
        }


        if (mMondayCheck.isChecked() && mMondayTextLun.getText().toString().isEmpty()
                && mMondayTextDin.getText().toString().isEmpty()) {
            mMondayTextLun.setError(getString(R.string.openinghourserror));
            mFocusView = mMondayTextLun;
            mFocusView.requestFocus();
            return;
        }

        if (mTuesdayCheck.isChecked() && mTuesdayTextLun.getText().toString().isEmpty()
                && mTuesdayTextDin.getText().toString().isEmpty()) {
            mTuesdayTextLun.setError(getString(R.string.openinghourserror));
            mFocusView = mTuesdayTextLun;
            mFocusView.requestFocus();
            return;
        }

        if (mWednesdayCheck.isChecked() && mWednesdayTextLun.getText().toString().isEmpty()
                && mWednesdayTextDin.getText().toString().isEmpty()) {
            mWednesdayTextLun.setError(getString(R.string.openinghourserror));
            mFocusView = mWednesdayTextLun;
            mFocusView.requestFocus();
            return;
        }

        if (mThursdayCheck.isChecked() && mThursdayTextLun.getText().toString().isEmpty()
                && mThursdayTextDin.getText().toString().isEmpty()) {
            mThursdayTextLun.setError(getString(R.string.openinghourserror));
            mFocusView = mThursdayTextLun;
            mFocusView.requestFocus();
            return;
        }

        if (mFridayCheck.isChecked() && mFridayTextLun.getText().toString().isEmpty()
                && mFridayTextDin.getText().toString().isEmpty()) {
            mFridayTextLun.setError(getString(R.string.openinghourserror));
            mFocusView = mFridayTextLun;
            mFocusView.requestFocus();
            return;
        }

        if (mSaturdayCheck.isChecked() && mSaturdayTextLun.getText().toString().isEmpty()
                && mSaturdayTextDin.getText().toString().isEmpty()) {
            mSaturdayTextLun.setError(getString(R.string.openinghourserror));
            mFocusView = mSaturdayTextLun;
            mFocusView.requestFocus();
            return;
        }

        if (mSundayCheck.isChecked() && mSundayTextLun.getText().toString().isEmpty()
                && mSundayTextDin.getText().toString().isEmpty()) {
            mSundayTextLun.setError(getString(R.string.openinghourserror));
            mFocusView = mSundayTextLun;
            mFocusView.requestFocus();
            return;
        }

        if (editSvcTime.getText().toString().isEmpty()) {
            editSvcTime.setError(getResources().getString(R.string.servicetime_error));
            mFocusView = editSvcTime;
            mFocusView.requestFocus();
            return;
        }

        if (allowtb.isChecked() && editSeats.getText().toString().isEmpty()) {
            editSeats.setError(getString(R.string.seatserror));
            mFocusView = editSeats;
            mFocusView.requestFocus();
            return;
        }

        if (mTicketCheckbox.isChecked() && mSupportedTickets.getText().toString().isEmpty()) {
            mSupportedTickets.setError(getString(R.string.ticketserror));
            mFocusView = mSupportedTickets;
            mFocusView.requestFocus();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(G3EditRestaurantProfileActivity.this);
        builder.setMessage(R.string.are_you_sure).setTitle(R.string.save);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        saveData();

                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_restaurant_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case com.example.ufoodlibrary.R.id.saveprofile:
                checkForm();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final G3PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            mLatitude = place.getLatLng().latitude;
            mLongitude = place.getLatLng().longitude;
            mAddress = place.getAddress().toString();
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onSaveItemClick(String mWeekDay, int lfh, int lfm, int lth, int ltm, int dfh,
                                int dfm, int dth, int dtm) {

        String key = getKey(mWeekDay);

        boolean lunch = false;
        boolean dinner = false;

        String str_lunch = null;
        String str_dinner = null;

        if (lfh != -1 && lth != -1) {
            if (lfh < lth || (lfh == lth && lfm < ltm)) {
                lunch = true;
                str_lunch = (getString(R.string.lunch) + " " + getString(R.string.from) + " "
                        + String.format("%02d:%02d", lfh, lfm)) + " " + getString(R.string.to)
                        + " " + String.format("%02d:%02d", lth, ltm);
            }
        }

        if (dfh != -1 && dth != -1) {
            if (dfh < dth || (dfh == dth && dfm < dtm)) {
                dinner = true;
                str_dinner = (getString(R.string.dinner) + " " + getString(R.string.from) + " "
                        + String.format("%02d:%02d", dfh, dfm)) + " " + getString(R.string.to)
                        + " " + String.format("%02d:%02d", dth, dtm);
            }
        }

        if (lunch && dinner) {
            if (lth < dfh || (lth == dfh && ltm < dfm)) {
                setString(mWeekDay, str_lunch, str_dinner);
                openingHours.remove(key + JsonKey.LUNCHTAG);
                openingHours.remove(key + JsonKey.DINNERTAG);
                openingHours.put(key + JsonKey.LUNCHTAG,
                        new G3OpeningHoursObj(G3OpeningHoursObj.Day.getDayFromString(key),
                                key + JsonKey.LUNCHTAG, 100 * lfh + lfm, 100 * lth + ltm));
                openingHours.put(key + JsonKey.DINNERTAG,
                        new G3OpeningHoursObj(G3OpeningHoursObj.Day.getDayFromString(key),
                                key + JsonKey.DINNERTAG, 100 * dfh + dfm, 100 * dth + dtm));
            }
        } else if (lunch && !dinner) {
            setString(mWeekDay, str_lunch, null);
            openingHours.remove(key + JsonKey.LUNCHTAG);
            openingHours.remove(key + JsonKey.DINNERTAG);
            openingHours.put(key + JsonKey.LUNCHTAG,
                    new G3OpeningHoursObj(G3OpeningHoursObj.Day.getDayFromString(key),
                            key + JsonKey.LUNCHTAG, 100 * lfh + lfm, 100 * lth + ltm));
        } else if (!lunch && dinner) {
            setString(mWeekDay, null, str_dinner);
            openingHours.remove(key + JsonKey.LUNCHTAG);
            openingHours.remove(key + JsonKey.DINNERTAG);
            openingHours.put(key + JsonKey.DINNERTAG,
                    new G3OpeningHoursObj(G3OpeningHoursObj.Day.getDayFromString(key),
                            key + JsonKey.DINNERTAG, 100 * dfh + dfm, 100 * dth + dtm));
        } else {
            setString(mWeekDay, null, null);
            openingHours.remove(key + JsonKey.LUNCHTAG);
            openingHours.remove(key + JsonKey.DINNERTAG);
        }
    }

    private String getKey(String mWeekDay) {
        String key = null;
        if (mWeekDay.equalsIgnoreCase(getString(R.string.monday))) {
            key = G3OpeningHoursObj.Day.getDayFromInt(0).getString();
        } else if (mWeekDay.equalsIgnoreCase(getString(R.string.tuesday))) {
            key = G3OpeningHoursObj.Day.getDayFromInt(1).getString();
        } else if (mWeekDay.equalsIgnoreCase(getString(R.string.wednesday))) {
            key = G3OpeningHoursObj.Day.getDayFromInt(2).getString();
        } else if (mWeekDay.equalsIgnoreCase(getString(R.string.thursday))) {
            key = G3OpeningHoursObj.Day.getDayFromInt(3).getString();
        } else if (mWeekDay.equalsIgnoreCase(getString(R.string.friday))) {
            key = G3OpeningHoursObj.Day.getDayFromInt(4).getString();
        } else if (mWeekDay.equalsIgnoreCase(getString(R.string.saturday))) {
            key = G3OpeningHoursObj.Day.getDayFromInt(5).getString();
        } else if (mWeekDay.equalsIgnoreCase(getString(R.string.sunday))) {
            key = G3OpeningHoursObj.Day.getDayFromInt(6).getString();
        }
        return key;
    }

    private void setString(String mWeekDay, String lunch, String dinner) {

        final String MONDAY = getResources().getString(R.string.monday);
        final String TUESDAY = getResources().getString(R.string.tuesday);
        final String THURSDAY = getResources().getString(R.string.thursday);
        final String WEDNESDAY = getResources().getString(R.string.wednesday);
        final String FRIDAY = getResources().getString(R.string.friday);
        final String SATURDAY = getResources().getString(R.string.saturday);
        final String SUNDAY = getResources().getString(R.string.sunday);

        if (mWeekDay.equals(MONDAY)) {

            mMondayTextLun.setText(lunch);
            mMondayTextDin.setText(dinner);

        } else if (mWeekDay.equals(TUESDAY)) {

            mTuesdayTextLun.setText(lunch);
            mTuesdayTextDin.setText(dinner);

        } else if (mWeekDay.equals(WEDNESDAY)) {

            mWednesdayTextLun.setText(lunch);
            mWednesdayTextDin.setText(dinner);

        } else if (mWeekDay.equals(THURSDAY)) {

            mThursdayTextLun.setText(lunch);
            mThursdayTextDin.setText(dinner);

        } else if (mWeekDay.equals(FRIDAY)) {

            mFridayTextLun.setText(lunch);
            mFridayTextDin.setText(dinner);

        } else if (mWeekDay.equals(SATURDAY)) {

            mSaturdayTextLun.setText(lunch);
            mSaturdayTextDin.setText(dinner);

        } else if (mWeekDay.equals(SUNDAY)) {

            mSundayTextLun.setText(lunch);
            mSundayTextDin.setText(dinner);

        }
    }

    public void selectImage(View v) {
        final CharSequence[] items = {getString(R.string.camera), getString(R.string.library), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(G3EditRestaurantProfileActivity.this);


        TextView title = new TextView(this);
        title.setText(R.string.title_add_photo);
        title.setTextColor(getResources().getColor(R.color.white));
        title.setPadding(30, 10, 10, 10);
        title.setBackgroundColor(getResources().getColor(R.color.primary));
        title.setTextSize(30);//TODO cercare come imporre l'AppCompact

        builder.setCustomTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.camera))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals(getString(R.string.library))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mProfileImage.setImageBitmap(thumbnail);
                bmpTosave = thumbnail;
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                mProfileImage.setImageBitmap(bm);
                bmpTosave = bm;
            }
        }

    }


    private void checkPemission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_WRITE_PERMISSION);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_WRITE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    checkPemission();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
