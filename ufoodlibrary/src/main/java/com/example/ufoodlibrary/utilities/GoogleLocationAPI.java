package com.example.ufoodlibrary.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.places.Places;

/**
 * Created by Alfonso-LAPTOP on 25/05/2016.
 */
public class GoogleLocationAPI {


    private static GoogleLocationAPI instance;
    private String LOG_TAG = "GoogleLocationAPI";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private FragmentActivity mActivity;
    private GoogleApiClient.OnConnectionFailedListener connfailedlistener;
    private GoogleApiClient.ConnectionCallbacks connectionCallbacks;
    private LocationListener locationListener;


    private GoogleLocationAPI() {

    }

    public static GoogleLocationAPI newInstance(FragmentActivity context) {
        if (instance == null) {
            instance = new GoogleLocationAPI();
            instance.mActivity = context;
            instance.connfailedlistener = (GoogleApiClient.OnConnectionFailedListener) context;
            instance.connectionCallbacks = (GoogleApiClient.ConnectionCallbacks) context;
            instance.locationListener = (LocationListener) context;
        }
        return instance;
    }


    // Builds a GoogleApiClient
    private synchronized void buildGoogleApiClient() {
        Log.i(LOG_TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .enableAutoManage(mActivity, GoogleAPIConstants.GOOGLE_API_CLIENT_ID, connfailedlistener) //TODO listener
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connfailedlistener)
                .addApi(Places.GEO_DATA_API) // Address suggestions
                .addApi(LocationServices.API) // Device localization
                .build();

        createLocationRequest();
        buildLocationSettingsRequest();
    }

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

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void checkLocationSettingsAndStartUpdates() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback((ResultCallback<? super LocationSettingsResult>) mActivity);
    }

    public void startLocationUpdates() {

        // Requesting position to Google API
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, locationListener);

    }

    public void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener); //TODO this è un listener

    }

    public GoogleApiClient getApiClient(){
        return mGoogleApiClient;
    }
}
