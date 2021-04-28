package com.polito.group3.mobileproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.RestaurantFilters;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.polito.group3.mobileproject.R;
import com.polito.group3.mobileproject.activities.G3MapSearchActivity;
import com.polito.group3.mobileproject.activities.G3UserSearchRestaurant;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * Created by Luigi on 03/06/2016.
 */
public class G3MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater mInflater;

    public G3MapInfoWindowAdapter(LayoutInflater i) {
        mInflater = i;
    }

    // This defines the contents within the info window based on the marker
    @Override
    public View getInfoContents(Marker marker) {

        ImageView mRestaurantImage;
        TextView mNameRestaurant;
        TextView mTypeKitchen;
        TextView mDistance;
        RatingBar mRewiRating;
        TextView mNumRew;
        TextView mPriceRating;

        // Getting view from the layout file
        View v = mInflater.inflate(R.layout.info_window_maps, null);

        mRestaurantImage = (ImageView) v.findViewById(com.example.ufoodlibrary.R.id.restaurantImage);
        mNameRestaurant = (TextView) v.findViewById(com.example.ufoodlibrary.R.id.reastaurantName);
        mTypeKitchen = (TextView) v.findViewById(com.example.ufoodlibrary.R.id.typekitchen);
        mDistance = (TextView) v.findViewById(com.example.ufoodlibrary.R.id.distance);
        mRewiRating = (RatingBar) v.findViewById(com.example.ufoodlibrary.R.id.starratingbar);
        mNumRew = (TextView) v.findViewById(com.example.ufoodlibrary.R.id.numrew);
        mPriceRating = (TextView) v.findViewById(com.example.ufoodlibrary.R.id.price_range);

        // Populate fields
        G3RestaurantObj clickedClusterItem = G3MapSearchActivity.getClickedClusterItem();

        mRewiRating.setNumStars(5);
        float number = (float) clickedClusterItem.getReviewsAvgScore();
        mRewiRating.setRating((float)clickedClusterItem.getReviewsAvgScore());
        mNameRestaurant.setText(clickedClusterItem.getName());
        mTypeKitchen.setText(clickedClusterItem.getCategoryAsEnum().getString());

        if(G3MapSearchActivity.getCurrentLocation() != null) {
            DecimalFormat df2 = new DecimalFormat(".##");
            double dist = RestaurantFilters.calculateDistance(G3MapSearchActivity.getCurrentLocation(),
                    new LatLng(clickedClusterItem.getAddressObj().getLatitude(), clickedClusterItem.getAddressObj().getLongitude()));
            if (dist >= 1) {
                mDistance.setText(df2.format(dist) + " Km");
            } else {
                mDistance.setText(Math.round(dist * 1000) + " m");
            }
        } else {
            mDistance.setVisibility(View.GONE);
        }

        String url  = clickedClusterItem.getPhotoPath();
        if(url != null && url.length()>0) {

            if (!G3MapSearchActivity.isFirstTimeShowingInfoWindow()) {
                Picasso.with(G3Application.getAppContext()).load(url).placeholder(com.example.ufoodlibrary.R.drawable.default_restaurant)
                        .into(mRestaurantImage);
            } else {
                G3MapSearchActivity.setFirstTimeShowingInfoWindow(false);
                Picasso.with(G3Application.getAppContext()).load(url).placeholder(com.example.ufoodlibrary.R.drawable.default_restaurant)
                        .into(mRestaurantImage, new Callback() {

                            private Marker marker;
                            private G3RestaurantObj restaurant;

                            public Callback init(Marker marker, G3RestaurantObj restaurant) {
                                this.marker = marker;
                                this.restaurant = restaurant;
                                return this;
                            }

                            @Override
                            public void onSuccess() {
                                if (restaurant.equals(G3MapSearchActivity.getClickedClusterItem())) {
                                    marker.showInfoWindow();
                                }
                            }

                            @Override
                            public void onError() {

                            }

                        }.init(marker, clickedClusterItem));
            }

        }
//        } else {
//            Picasso.with(G3Application.getAppContext()).load(com.example.ufoodlibrary.R.drawable.default_restaurant)
//                    .into(mRestaurantImage);
//        }

        mNumRew.setText("(" + clickedClusterItem.getNumOfReviews() + ")");

        mPriceRating.setText(clickedClusterItem.getPriceRatingEuro());

        // Return info window contents
        return v;
    }

    // This changes the frame of the info window; returning null uses the default frame.
    // This is just the border and arrow surrounding the contents specified above
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}
