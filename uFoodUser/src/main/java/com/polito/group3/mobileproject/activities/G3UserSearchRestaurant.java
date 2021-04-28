package com.polito.group3.mobileproject.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.GeocodeAddressIntentService;
import com.example.ufoodlibrary.utilities.GoogleAPIConstants;
import com.example.ufoodlibrary.utilities.RestaurantComparator;
import com.example.ufoodlibrary.utilities.RestaurantFilters;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.example.ufoodlibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.mobileproject.adapters.G3CardReastaurantAdapter;
import com.polito.group3.mobileproject.adapters.G3PlaceArrayAdapter;
import com.polito.group3.mobileproject.dialogfragments.G3FilterDialogFragment;
import com.polito.group3.mobileproject.dialogfragments.G3OrderByDialogFragment;
import com.polito.group3.mobileproject.dialogfragments.G3RadiusDialogFragment;
import com.polito.group3.mobileproject.services.G3UserNotifyService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alfonso on 20/04/2016.
 */

public class G3UserSearchRestaurant extends G3UserBaseActivityWithDrawer implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,G3FilterDialogFragment.OnCompleteListener,
        G3OrderByDialogFragment.OnCompleteOrderListener,
        G3RadiusDialogFragment.OnCompleteRadiusListener {

    private Context mCtx;
    private AutoCompleteTextView edtSearch;
    private ImageView myLocationIcon;
    private TextView notFoundText;
    private TextView noRestaurantText;
    private TextView orderLabel;
    private TextView radiuslabel;
    private TextView noNetworkText;
    private ImageView noRestaurantImage;
    private Button retryButton;
    private ProgressDialog progress;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout orderby;
    private LinearLayout filter;
    private Button floatingButton;
    private CoordinatorLayout bottomToolbar;

    private static int currentOrder = 0;
    private boolean isSearchOpened = false; // "true" when search bar is focused and user's typing
                                            // an address
    private boolean mRequestingLocationUpdates = false; // "true" when a device's location query
                                                        // is to be performed (i.e. at first start
                                                        // of the activity or when user clicks
                                                        // "my location" icon.
    private Location mCurrentLocation; // Location retrieved by GPS or network
    private static LatLng currentLatLng; // Location actually used to filtering restaurants
    private LatLng lastValidLatLng; // Last valid location searched to be restored when the user
                                    // abort the search
    private String lastValidAddress = ""; // The street address of last valid location searched
    private static RestaurantFilters mRestaurantFilters;
    private G3UserSearchRestaurant mActivity;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private G3PlaceArrayAdapter mPlaceArrayAdapter;
    private AddressResultReceiver mResultReceiver;
    private Handler mTimeoutHandler = new Handler();

    // Keys for storing activity state in the Bundle.
    private final static String RESTAURANTS_KEY = "restaurants-key";
    private final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    private final static String LOCATION_KEY = "location-key";
    private final static String LAST_ADDRESS_KEY = "last-address-key";
    private final static String LAST_LATLNG_KEY = "last-latlng-key";
    private final static String CURRENT_LATLNG_KEY = "current-latlng-key";
    private final static String FILTERS_KEY = "filters-key";
    public final static String RESTAUANTOBJ = "restaurantObj";

    private static final String LOG_TAG = "Search Restaurant Act";
    private static final int MY_LOCATION_PERMISSION = 1;
    private static final int FILTER_RECEIVED = 12345;

    private G3CardReastaurantAdapter mAdapter;
    private ArrayList<G3RestaurantObj> mFakeSet = new ArrayList<>();
    private ArrayList<G3RestaurantObj> mGeoFireSet;
    private ArrayList<G3RestaurantObj> mFilteredSet;

    private ScheduledExecutorService scheduleTaskExecutor;
    private volatile boolean refreshDataNeeded = false;

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(getApplicationContext(), G3UserNotifyService.class);
        startService(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_restaurant);

        mCtx = getApplicationContext();
        mActivity = this;
        //Setting Toolbar as Action Bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        if (myToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Loading Views by ID
        loadView();

        // Retrieving device's coordinates and load eventually saved instance's state
        loadData(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPemission();
        } else {
            continueSetting();
        }



    }

    private void continueSetting(){

        // Load Restaurant's Data From Firebase
        loadRestaurantsFromFirebase();

        buildGoogleApiClient();

        setRecyclerView();

        setSearchBar();

        setListeners();

        // Start a background thread that every 60 seconds force an update of data from
        // Firebase to be executed on the next search
        scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        scheduleTaskExecutor.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                refreshDataNeeded = true;
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    private void loadRestaurantsFromFirebase() {
        // Show Progress Dialog
        progress.show();

        try {
            G3Application.fManager.getRestaurants(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Hide network errors if previously showed and enable search bar
                    noNetworkText.setVisibility(View.GONE);
                    retryButton.setVisibility(View.GONE);
                    bottomToolbar.setVisibility(View.VISIBLE);
                    enableSearchBar(true);

                    // Load into array restaurant's data received from Firebase
                    for (DataSnapshot data: dataSnapshot.getChildren()) {
                        G3RestaurantObj r = data.getValue(G3RestaurantObj.class);
                        mFakeSet.add(r);
                    }

                    // Start searching for device's location
                    setLoadingLocation(true);
                    mRequestingLocationUpdates = true;
                    checkLocationSettingsAndStartUpdates();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Disable search bar and dismiss Progress Dialog
                    enableSearchBar(false);
                    progress.dismiss();

                    // Show network error text and a retry button
                    noNetworkText.setVisibility(View.VISIBLE);
                    retryButton.setVisibility(View.VISIBLE);
                    bottomToolbar.setVisibility(View.GONE);
                }
            });

        } catch (NetworkDownException e) {
            // Disable search bar and dismiss Progress Dialog
            enableSearchBar(false);
            progress.dismiss();

            // Show network error text and a retry button
            noNetworkText.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);
            bottomToolbar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putParcelable(LAST_LATLNG_KEY, lastValidLatLng);
        savedInstanceState.putParcelable(CURRENT_LATLNG_KEY, currentLatLng);
        savedInstanceState.putString(LAST_ADDRESS_KEY, lastValidAddress);
        savedInstanceState.putParcelable(FILTERS_KEY, mRestaurantFilters);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.filter_action:
                G3FilterDialogFragment mDialogFrag;
                mDialogFrag = G3FilterDialogFragment.newInstance();
                mDialogFrag.setCancelable(false);
                mDialogFrag.show(getSupportFragmentManager(), "dialogfilter");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // When an address search is still opened and user presses back button let's
        // stop the search and reload previous coordinates and address
        if (isSearchOpened) {
            edtSearch.clearFocus();
            edtSearch.setText(lastValidAddress);
            currentLatLng = lastValidLatLng;
            isSearchOpened = false;
            return;
        }
        super.onBackPressed();

    }

    @Override
    public void onResume() {
        super.onResume();

        setDrawerList();

        // Within onPause(), we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                progress.show();
                setLoadingLocation(true);
                startLocationUpdates();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                stopLocationUpdates();
            }
        }
    }

    private void checkPemission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_PERMISSION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_PERMISSION);

            }
        } else {
            continueSetting();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    continueSetting();
                    // permission was granted, yay! Do the contacts-related task you need to do.
                } else {
                    checkPemission();
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // Builds a GoogleApiClient
    private synchronized void buildGoogleApiClient() {
        Log.i(LOG_TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, GoogleAPIConstants.GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API) // Address suggestions
                .addApi(LocationServices.API) // Device localization
                .build();

        createLocationRequest();
        buildLocationSettingsRequest();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG_TAG, "Google Places API connected.");

        // Set the adapter for address suggestions
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google API connection failed with error code: "
                + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "Google API connection suspended.");
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    // Sets up the location request.
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(GoogleAPIConstants.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(GoogleAPIConstants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    // Build a LocationSettingsRequest that is used for checking if a device has the needed location settings.
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    // Check if the device's location settings are adequate for the app's needs using the
    // checkLocationSettingsAndStartUpdates method, with the results provided through a PendingResult.
    private void checkLocationSettingsAndStartUpdates() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(mLocationSettingsCallback);
    }

    private ResultCallback<LocationSettingsResult> mLocationSettingsCallback
            = new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(LocationSettingsResult locationSettingsResult) {
            final Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Log.i(LOG_TAG, "All location settings are satisfied.");
                    startLocationUpdates();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Log.i(LOG_TAG, "Location settings are not satisfied. Show the user a dialog to" +
                            "upgrade location settings ");
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
                        status.startResolutionForResult(G3UserSearchRestaurant.this,
                                GoogleAPIConstants.REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        Log.i(LOG_TAG, "PendingIntent unable to execute request.");
                        showUnableToObtainLocation();
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.i(LOG_TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                            "not created.");
                    showUnableToObtainLocation();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case GoogleAPIConstants.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(LOG_TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(LOG_TAG, "User chose not to make required location settings changes.");
                        showUnableToObtainLocation();
                        break;
                }
                break;
            case FILTER_RECEIVED:
                if ( resultCode == RESULT_OK && data != null )
                    mRestaurantFilters = data.getParcelableExtra(FILTERS_KEY);
                break;
        }
    }

    @SuppressWarnings("ResourceType")
    // Requests location updates from the FusedLocationApi.
    private void startLocationUpdates() {

        // Requesting position to Google API
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);

        // Start time-out for retrieving position
        mTimeoutHandler.postDelayed(mExpiredRunnable, GoogleAPIConstants.UPDATE_INTERVAL_IN_MILLISECONDS * 2);
    }

    // Removes location updates from the FusedLocationApi.
    private void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    // Callback that fires when the device's location changes.
    @Override
    public void onLocationChanged(Location location) {
        stopLocationUpdates();
        mCurrentLocation = location;
        fetchAddressFromLatLng();
    }

    // Retrieve a street address from coordinates obtained from device localization
    private void fetchAddressFromLatLng() {
        Intent intent = new Intent(this, GeocodeAddressIntentService.class);
        intent.putExtra(GoogleAPIConstants.RECEIVER, mResultReceiver);
        intent.putExtra(GoogleAPIConstants.LOCATION_LATITUDE_DATA_EXTRA, mCurrentLocation.getLatitude());
        intent.putExtra(GoogleAPIConstants.LOCATION_LONGITUDE_DATA_EXTRA, mCurrentLocation.getLongitude());

        Log.i(LOG_TAG, "Starting Geocoder Service");
        startService(intent);
    }

    @SuppressLint("ParcelCreator")
    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            // Remove time-out handler
            mTimeoutHandler.removeCallbacks(mExpiredRunnable);

            if (resultCode == GoogleAPIConstants.SUCCESS_RESULT) {
                // No more location updates until a new user request
                mRequestingLocationUpdates = false;

                // Save the found address
                lastValidAddress = resultData.getString(GoogleAPIConstants.RESULT_DATA_KEY);

                // Update UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update location with the result
                        updateLocation();

                        // Start filtering restaurant's based on their distance from the device's location
                        doSearch();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showUnableToObtainLocation();
                    }
                });
            }
        }
    }

    // Executed when the time for device localization is exceeded
    private final Runnable mExpiredRunnable = new Runnable() {
        @Override
        public void run() {
            showUnableToObtainLocation();
        }
    };

    private void showUnableToObtainLocation() {
            // Dismiss Progress Dialog and stop "my location" icon animation
            progress.dismiss();
            setLoadingLocation(false);

            // No more location updates needed until a new request
            mRequestingLocationUpdates = false;

            // Show Not found location error
            notFoundText.setVisibility(View.VISIBLE);
            noRestaurantImage.setVisibility(View.VISIBLE);
    }

    private void setLoadingLocation(boolean isLoading) {

        if (isLoading) {
            mRecyclerView.setVisibility(View.GONE);
            notFoundText.setVisibility(View.GONE);
            noRestaurantText.setVisibility(View.GONE);
            noRestaurantImage.setVisibility(View.GONE);

            // Setting up and start blinking animation for "my location" icon
            final Animation animation = new AlphaAnimation(1, 0); // From fully visible to invisible
            animation.setDuration(500); // Duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // Do not alter animation rate
            animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end
            myLocationIcon.startAnimation(animation);
        } else {
            // Stop blinking animation
            myLocationIcon.clearAnimation();
        }
    }

    private void enableSearchBar(boolean enabled) {

        if (enabled) {
            // Enable search bar
            myLocationIcon.setClickable(true);
            edtSearch.setFocusableInTouchMode(true);
        } else {
            // Disable the search bar
            myLocationIcon.setClickable(false);
            edtSearch.setFocusable(false);
        }

    }

    // Update current location state variables and UI with the found device's location
    private void updateLocation() {
        if (mCurrentLocation != null) {
            edtSearch.setText(lastValidAddress);
            currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            lastValidLatLng = currentLatLng;
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
            currentLatLng = place.getLatLng();
            lastValidLatLng = currentLatLng;
            lastValidAddress = place.getAddress().toString();
        }
    };

    private void loadView() {
        edtSearch = (AutoCompleteTextView) findViewById(R.id.edtSearch);
        myLocationIcon = (ImageView) findViewById(R.id.mylocation);
        noRestaurantText = (TextView) findViewById(R.id.noRestaurantText);
        noRestaurantImage = (ImageView) findViewById(R.id.norestaurantimage);
        notFoundText = (TextView) findViewById(R.id.notFoundText);
        noNetworkText = (TextView) findViewById(R.id.noNetworkText);
        orderby = (LinearLayout) findViewById(R.id.layout_order_by);
        filter = (LinearLayout) findViewById(R.id.filter);
        radiuslabel = (TextView) findViewById(R.id.distance_value);
        floatingButton = (Button) findViewById(R.id.floting_button);
        retryButton = (Button) findViewById(R.id.retryButton);
        bottomToolbar = (CoordinatorLayout) findViewById(R.id.bottom_toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerRestaurant);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        orderLabel = (TextView) findViewById(R.id.critera_order);

        progress = TransitionHelper.getProgress(this);
    }

    private void loadData(Bundle savedInstanceState) {

        Drawable top = getResources().getDrawable(R.drawable.floating_map);
        floatingButton.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);

        mGeoFireSet = new ArrayList<>();
        mFilteredSet = new ArrayList<>();

        if (savedInstanceState != null) {

            if (savedInstanceState.keySet().contains(FILTERS_KEY)) {
                mRestaurantFilters = savedInstanceState.getParcelable(FILTERS_KEY);
            }

            mRequestingLocationUpdates = savedInstanceState.getBoolean(
                    REQUESTING_LOCATION_UPDATES_KEY, false);

            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            if (savedInstanceState.keySet().contains(LAST_LATLNG_KEY)) {
                lastValidLatLng = savedInstanceState.getParcelable(LAST_LATLNG_KEY);
            }

            if (savedInstanceState.keySet().contains(CURRENT_LATLNG_KEY)) {
                currentLatLng = savedInstanceState.getParcelable(CURRENT_LATLNG_KEY);
            }

            lastValidAddress = savedInstanceState.getString(LAST_ADDRESS_KEY, "");
        } else {
            mRequestingLocationUpdates = true;
            mRestaurantFilters = new RestaurantFilters();
        }

        DecimalFormat df2 = new DecimalFormat(".##");
        int dist = mRestaurantFilters.getRadius();
        if (dist >= 1000) {
            radiuslabel.setText(String.valueOf(df2.format(dist / 1000)) + " Km");
        } else {
            radiuslabel.setText(String.valueOf(dist) + " m");
        }

    }

    private void setRecyclerView() {

        mAdapter = new G3CardReastaurantAdapter(mFilteredSet);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnCardRestaurantListener(new G3CardReastaurantAdapter.onCardRestaurantClick() {
            @Override
            public void onCardClick(int position, ImageView image) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intent = new Intent(getBaseContext(), G3UserRestaurantActivity.class);
                    intent.putExtra(RESTAUANTOBJ, mFilteredSet.get(position));
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(mActivity, (View) image, "cardImageTransition");
                    startActivity(intent, options.toBundle());
                }else{
                    Intent intent = new Intent(getBaseContext(), G3UserRestaurantActivity.class);
                    intent.putExtra(RESTAUANTOBJ, mFilteredSet.get(position));
                    startActivity(intent);
                }

            }
        });

    }

    private void setSearchBar() {

        mResultReceiver = new AddressResultReceiver(null);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build();

        mPlaceArrayAdapter = new G3PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                null, typeFilter);

        edtSearch.setAdapter(mPlaceArrayAdapter);

        edtSearch.setThreshold(3);

    }

    private void setListeners() {

        // Button to reload data from Firebase if a network or a server error has occurred
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRestaurantsFromFirebase();
            }
        });

        // Listener that manage clicks on suggestions' list
        edtSearch.setOnItemClickListener(mAutocompleteClickListener);

        // Listener that show keyboard when search bar is focused
        edtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isSearchOpened = true;
                    edtSearch.setText("");
                    //open the keyboard focused in the edtSearch
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edtSearch, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        // Invalidate current location coordinates when typing something on search bar
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().isEmpty()) {
                    currentLatLng = null;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Listener to do a search when the user clicks on search button on smart keyboard
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && isCurrentLocationValid()) {
                    //hides the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
                    edtSearch.clearFocus();
                    isSearchOpened = false;

                    notFoundText.setVisibility(View.GONE);
                    noRestaurantText.setVisibility(View.GONE);
                    noRestaurantImage.setVisibility(View.GONE);

                    doSearch();

                    return true;
                }
                return false;
            }
        });

        // Listener to locate device's position clicking on "my location" icon
        myLocationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                setLoadingLocation(true);
                edtSearch.setText("");
                if (isSearchOpened) {
                    //hides the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
                    edtSearch.clearFocus();
                    isSearchOpened = false;
                }
                if (mGoogleApiClient.isConnected()) {
                    mRequestingLocationUpdates = true;
                    // startLocationUpdates();
                    checkLocationSettingsAndStartUpdates();
                } else {
                    showUnableToObtainLocation();
                }
            }
        });

        orderby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G3OrderByDialogFragment mDialogFrag;
                mDialogFrag = G3OrderByDialogFragment.newInstance();
                mDialogFrag.setCancelable(true);
                mDialogFrag.show(getSupportFragmentManager(), "dialogorder");
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G3RadiusDialogFragment mDialogFrag;
                mDialogFrag = G3RadiusDialogFragment.newInstance();
                mDialogFrag.setCancelable(true);
                mDialogFrag.show(getSupportFragmentManager(), "dialogfilterRadius");
            }
        });

        // Button to switch to map search mode
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, G3MapSearchActivity.class);
                intent.putExtra(FILTERS_KEY, mRestaurantFilters);
                intent.putParcelableArrayListExtra(RESTAURANTS_KEY, mFakeSet);
                startActivityForResult(intent, FILTER_RECEIVED);
            }
        });

    }

    @Override
    public void onCompleteRadius() {
        DecimalFormat df2 = new DecimalFormat(".##");
        int dist = mRestaurantFilters.getRadius();
        if (dist >= 1000) {
            radiuslabel.setText(String.valueOf(df2.format(dist / 1000)) + " Km");
        } else {
            radiuslabel.setText(String.valueOf(dist) + " m");
        }

        notFoundText.setVisibility(View.GONE);
        noRestaurantText.setVisibility(View.GONE);
        noRestaurantImage.setVisibility(View.GONE);
        doSearch();
    }

    @Override
    public void onCompleteFilters() {
        notFoundText.setVisibility(View.GONE);
        noRestaurantText.setVisibility(View.GONE);
        noRestaurantImage.setVisibility(View.GONE);

        mFilteredSet.clear();
        mFilteredSet.addAll(mRestaurantFilters.applyFiltersToList(mGeoFireSet));

        if(mFilteredSet.size() == 0 && mFakeSet.size() != 0) {
            noRestaurantText.setVisibility(View.VISIBLE);
            noRestaurantImage.setVisibility(View.VISIBLE);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCompleteOrdering(int orderby) {

        currentOrder = orderby;
        if (mFilteredSet != null) {
            switch (orderby) {
                case 0:
                    orderLabel.setText(R.string.radio_distance);
                    // Your code when first option seletced
                    Collections.sort(mFilteredSet, new RestaurantComparator(
                            new RestaurantComparator.DistanceComparator(currentLatLng),
                            new RestaurantComparator.NameComparator()
                    ));
                    mAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    orderLabel.setText(R.string.radio_price);
                    // Your code when 2nd  option seletced
                    Collections.sort(mFilteredSet, new RestaurantComparator(
                            new RestaurantComparator.PriceComparator(),
                            new RestaurantComparator.NameComparator()
                    ));
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    orderLabel.setText(R.string.radio_ranking);
                    // Your code when 3rd option seletced
                    Collections.sort(mFilteredSet, new RestaurantComparator(
                            new RestaurantComparator.RankingComparator(),
                            new RestaurantComparator.NameComparator()
                    ));
                    mAdapter.notifyDataSetChanged();
                    break;
                case 3:
                    orderLabel.setText(R.string.radio_alphabeticcally);
                    // Your code when 4th  option seletced
                    Collections.sort(mFilteredSet, new RestaurantComparator.NameComparator());
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void sortRestaurants(ArrayList<G3RestaurantObj> rSet) {

        String ordering = orderLabel.getText().toString();

        if (ordering.equals(getResources().getString(R.string.radio_distance))) {
            Collections.sort(rSet, new RestaurantComparator(
                    new RestaurantComparator.DistanceComparator(currentLatLng),
                    new RestaurantComparator.NameComparator()
            ));
        } else if (ordering.equals(getResources().getString(R.string.radio_price))) {
            Collections.sort(rSet, new RestaurantComparator(
                    new RestaurantComparator.PriceComparator(),
                    new RestaurantComparator.NameComparator()
            ));
        } else if (ordering.equals(getResources().getString(R.string.radio_ranking))) {
            Collections.sort(rSet, new RestaurantComparator(
                    new RestaurantComparator.RankingComparator(),
                    new RestaurantComparator.NameComparator()
            ));
        } else if (ordering.equals(getResources().getString(R.string.radio_alphabeticcally))) {
            Collections.sort(rSet, new RestaurantComparator.NameComparator());
        }

    }

    private void doSearch() {

        if (isCurrentLocationValid()) {

            if (refreshDataNeeded) {

                progress.show();

                try {
                    G3Application.fManager.getRestaurants(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Load into array restaurant's data received from Firebase
                            mFakeSet.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                G3RestaurantObj r = data.getValue(G3RestaurantObj.class);
                                mFakeSet.add(r);
                            }
                            refreshDataNeeded = false;
                            doSearch();
                            progress.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // We keep old data to query on. We will try to update data again
                            // on next 60 seconds.
                            refreshDataNeeded = false;
                            doSearch();
                            progress.dismiss();
                        }
                    });

                } catch (NetworkDownException e) {
                    // We keep old data to query on. We will try to update data again
                    // on next 60 seconds.
                    refreshDataNeeded = false;
                    doSearch();
                    progress.dismiss();
                }

            } else {

                mGeoFireSet = mRestaurantFilters.applyRadiusFilterToList(mFakeSet, currentLatLng);
                mFilteredSet.clear();
                mFilteredSet.addAll(mRestaurantFilters.applyFiltersToList(mGeoFireSet));

                if (mFilteredSet.size() != 0) {
                    sortRestaurants(mFilteredSet);
                } else {
                    noRestaurantText.setVisibility(View.VISIBLE);
                    noRestaurantImage.setVisibility(View.VISIBLE);
                }

                // Hide loading progress dialog and stop "my location" icon blinking animation
                progress.dismiss();
                setLoadingLocation(false);

                mAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.VISIBLE);
            }

        } else {
            showUnableToObtainLocation();
        }

    }

    private boolean isCurrentLocationValid() {
        if (currentLatLng != null)
            return true;

        return false;
    }

    public static LatLng getCurrentPos() {
        return currentLatLng;
    }

    public static RestaurantFilters getRestaurantFilters() {
        return mRestaurantFilters;
    }

    public static void setmRestaurantFilters(RestaurantFilters filter) { mRestaurantFilters = filter; }

    public static int getCurrentOrder() {
        return currentOrder;
    }

}
