package com.polito.group3.ufoodfusion.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.GoogleAPIConstants;
import com.example.ufoodlibrary.utilities.RestaurantFilters;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.polito.group3.ufoodfusion.R;
import com.polito.group3.ufoodfusion.adapters.G3CardReastaurantAdapter;
import com.polito.group3.ufoodfusion.adapters.G3MapInfoWindowAdapter;
import com.polito.group3.ufoodfusion.adapters.G3PlaceArrayAdapter;
import com.polito.group3.ufoodfusion.dialogfragments.G3FilterDialogFragment;


import java.util.ArrayList;

/**
 * Created by Luigi on 27/05/2016.
 */
public class G3MapSearchActivity extends G3UserBaseActivityWithDrawer implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener, G3FilterDialogFragment.OnCompleteListener, OnMapReadyCallback, GoogleMap.OnCameraChangeListener {

    private AutoCompleteTextView edtSearch;
    private ImageView myLocationIcon;
    private Button floatingButton;

    private boolean isSearchOpened = false; // "true" when search bar is focused and user's typing
                                            // an address
    private boolean mRequestingLocationUpdates = false; // "true" when a device's location query
                                                        // is to be performed (i.e. at first start
                                                        // of the activity.)
    private static Location mCurrentLocation; // Location retrieved by GPS or network
    private static LatLng currentLatLng; // Location actually used to filtering restaurants
    private LatLng lastValidLatLng; // Last valid location searched to be restored when the user
                                    // abort the search
    private static RestaurantFilters mRestaurantFilters;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private G3PlaceArrayAdapter mPlaceArrayAdapter;

    // Keys for storing activity state in the Bundle.
    private final static String RESTAURANTS_KEY = "restaurants-key";
    private final static String LOCATION_KEY = "location-key";
    private final static String LAST_LATLNG_KEY = "last-latlng-key";
    private final static String CURRENT_LATLNG_KEY = "current-latlng-key";
    private final static String FILTERS_KEY = "filters-key";
    private final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    public final static String RESTAUANTOBJ = "restaurantObj";

    private static final String LOG_TAG = "Map Search Act";
    private static final int MY_LOCATION_PERMISSION = 1;

    private G3CardReastaurantAdapter mAdapter;
    private ArrayList<G3RestaurantObj> mFakeSet;
    private ArrayList<G3RestaurantObj> mGeoFireSet;
    private ArrayList<G3RestaurantObj> mFilteredSet;

    private ProgressDialog progress;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private Circle searchCircle;
    private ClusterManager<G3RestaurantObj> mClusterManager;
    private static G3RestaurantObj clickedClusterItem;
    private static boolean firstTimeShowingInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ufoodlibrary.R.layout.activity_map_search);

        //Setting Toolbar as Action Bar
        Toolbar myToolbar = (Toolbar) findViewById(com.example.ufoodlibrary.R.id.toolbar);
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
            buildGoogleApiClient();
        }

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        setSearchBar();

        setListeners();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putParcelable(LAST_LATLNG_KEY, lastValidLatLng);
        savedInstanceState.putParcelable(CURRENT_LATLNG_KEY, currentLatLng);
        savedInstanceState.putParcelable(FILTERS_KEY, mRestaurantFilters);
        savedInstanceState.putParcelableArrayList(RESTAURANTS_KEY, mFakeSet);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.example.ufoodlibrary.R.menu.menu_user_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case com.example.ufoodlibrary.R.id.filter_action:
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
            edtSearch.setText("");
            edtSearch.clearFocus();
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
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_PERMISSION);

            }
        } else {
            if (mGoogleApiClient == null) {
                buildGoogleApiClient();
            }
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
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

                    checkPemission();
                    // permission was granted, yay! Do the contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPemission();
        } else {
            mMap.setMyLocationEnabled(true);
        }

        mClusterManager = new ClusterManager<G3RestaurantObj>(this, mMap);

        // Set custom InfoViewAdapter
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new G3MapInfoWindowAdapter(getLayoutInflater()));
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<G3RestaurantObj>() {
            @Override
            public boolean onClusterItemClick(G3RestaurantObj restaurant) {
                if (!restaurant.equals(clickedClusterItem)) {
                    clickedClusterItem = restaurant;
                    firstTimeShowingInfoWindow = true;
                }
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getBaseContext(), G3UserRestaurantActivity.class);
                intent.putExtra(RESTAUANTOBJ, clickedClusterItem);
                startActivity(intent);
            }
        });

        LatLng latLngCenter;
        if (currentLatLng != null)
            latLngCenter = currentLatLng;
        else
            latLngCenter = new LatLng(45.0625527, 7.6602097); // Politecnico

        mRestaurantFilters.setRadius(1000);
        searchCircle = mMap.addCircle(new CircleOptions().center(latLngCenter).radius(1000));
        searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
        searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, 14));
        mMap.setOnCameraChangeListener(this);

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

        if (mRequestingLocationUpdates) {
            checkLocationSettingsAndStartUpdates();
        }

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
    // checkLocationSettingsAndStartUpdates method, with the results provided through a {@code PendingResult}.
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
                        status.startResolutionForResult(G3MapSearchActivity.this,
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
        }
    }

    @SuppressWarnings("ResourceType")
    // Requests location updates from the FusedLocationApi.
    private void startLocationUpdates() {

//        setLoadingUI(true);

        // Requesting position to Google API
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);

        // Start time-out for retrieving position
//        mTimeoutHandler.postDelayed(mExpiredRunnable, GoogleAPIConstants.UPDATE_INTERVAL_IN_MILLISECONDS * 2);
    }

    // Removes location updates from the FusedLocationApi.
    private void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    // Callback that fires when the device's location changes.
    @Override
    public void onLocationChanged(Location location) {

        // Remove time-out handler
//        mTimeoutHandler.removeCallbacks(mExpiredRunnable);

        // No more location updates until a new user request
        mRequestingLocationUpdates = false;
        stopLocationUpdates();

        mCurrentLocation = location;

        if (currentLatLng != null) {

            updateLocation();
            doSearch();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));

        } else {
            updateLocation();
        }

    }

    private void showUnableToObtainLocation() {
        mRequestingLocationUpdates = false;
        Toast.makeText(getApplicationContext(), R.string.location_not_found, Toast.LENGTH_SHORT).show();
    }

    private void setLoadingUI(boolean isLoading) {

        if (isLoading) {
            // During device's location query disable the search bar
//            myLocationIcon.setClickable(false);
//            edtSearch.setFocusableInTouchMode(false);

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

    // Update current location state variables and UI with the found device's location
    private void updateLocation() {
        if (mCurrentLocation != null) {
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
        }
    };

    private void loadView() {
        edtSearch = (AutoCompleteTextView) findViewById(com.example.ufoodlibrary.R.id.edtSearch);
        myLocationIcon = (ImageView) findViewById(com.example.ufoodlibrary.R.id.mylocation);
        floatingButton = (Button) findViewById(R.id.floting_button);

        myLocationIcon.setVisibility(View.GONE);
    }

    private void loadData(Bundle savedInstanceState) {

        Drawable top = getResources().getDrawable(com.example.ufoodlibrary.R.drawable.floating_list);
        floatingButton.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);

        mGeoFireSet = new ArrayList<>();
        mFilteredSet = new ArrayList<>();
        currentLatLng = null;
        clickedClusterItem = null;
        firstTimeShowingInfoWindow = true;

        if (savedInstanceState != null) {

            mRequestingLocationUpdates = savedInstanceState.getBoolean(
                    REQUESTING_LOCATION_UPDATES_KEY, false);

            if (savedInstanceState.keySet().contains(FILTERS_KEY)) {
                mRestaurantFilters = savedInstanceState.getParcelable(FILTERS_KEY);
            }

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

            if (savedInstanceState.keySet().contains(RESTAURANTS_KEY)) {
                mFakeSet = savedInstanceState.getParcelableArrayList(RESTAURANTS_KEY);
            }

        } else {
            mRequestingLocationUpdates = true;
            mRestaurantFilters = getIntent().getExtras().getParcelable(FILTERS_KEY);
            mFakeSet = getIntent().getExtras().getParcelableArrayList(RESTAURANTS_KEY);
        }

    }

    private void setSearchBar() {

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_GEOCODE)
                .build();

        mPlaceArrayAdapter = new G3PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                null, typeFilter);

        edtSearch.setAdapter(mPlaceArrayAdapter);

        edtSearch.setThreshold(3);

    }

    private void setListeners() {

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
//                if (!s.toString().isEmpty()) {
                    currentLatLng = null;
//                }
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

                    doSearch();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));

                    return true;
                }
                return false;
            }
        });

//        // Listener to locate device's position clicking on "my location" icon
//        myLocationIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isSearchOpened) {
//                    //hides the keyboard
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
//                    edtSearch.clearFocus();
//                    edtSearch.setText("");
//                    isSearchOpened = false;
//                }
////                doSearch();
//
//            }
//        });

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRestaurantFilters.setRadius(G3UserSearchRestaurant.getRestaurantFilters().getRadius());

                Intent intent = new Intent();
                intent.putExtra(FILTERS_KEY, mRestaurantFilters);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void onCompleteFilters() {

        mFilteredSet.clear();
        mFilteredSet.addAll(mRestaurantFilters.applyFiltersToList(mGeoFireSet));

        notifyDataChange();

    }

    private void doSearch() {

        if (isCurrentLocationValid()) {

            mGeoFireSet = mRestaurantFilters.applyRadiusFilterToList(mFakeSet, currentLatLng);
            mFilteredSet.clear();
            mFilteredSet.addAll(mRestaurantFilters.applyFiltersToList(mGeoFireSet));

            notifyDataChange();

        }

    }

    private void notifyDataChange () {

        mClusterManager.clearItems();
        mClusterManager.addItems(mFilteredSet);
        mClusterManager.cluster();

    }

    private boolean isCurrentLocationValid() {
        if (currentLatLng != null)
            return true;

        return false;
    }

    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000/Math.pow(2, zoomLevel);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

        if (cameraPosition.zoom > 17) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        } else if (cameraPosition.zoom < 11) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        } else {

            // Update the search criteria for this geoQuery and the circle on the map
            LatLng center = cameraPosition.target;
            double radius = zoomLevelToRadius(cameraPosition.zoom);
            this.searchCircle.setCenter(center);
            this.searchCircle.setRadius(radius);
            currentLatLng = new LatLng(center.latitude, center.longitude);
            mRestaurantFilters.setRadius((int)radius);

            doSearch();

//            mClusterManager.onCameraChange(cameraPosition);
        }
    }

    public static LatLng getCurrentPos() {
        return currentLatLng;
    }

    public static LatLng getCurrentLocation() {
        if (mCurrentLocation != null) {
            return new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
        return null;
    }

    public static RestaurantFilters getRestaurantFilters() {
        return mRestaurantFilters;
    }

    public static void setmRestaurantFilters(RestaurantFilters filter) { mRestaurantFilters = filter; }

    public static G3RestaurantObj getClickedClusterItem() { return clickedClusterItem; }

    public static boolean isFirstTimeShowingInfoWindow() { return firstTimeShowingInfoWindow; }

    public static void setFirstTimeShowingInfoWindow(boolean value) { firstTimeShowingInfoWindow = value; }

}

