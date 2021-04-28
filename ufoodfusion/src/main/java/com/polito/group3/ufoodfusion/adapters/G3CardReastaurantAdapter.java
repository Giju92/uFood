package com.polito.group3.ufoodfusion.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3OpeningHoursObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.JsonKey;
import com.example.ufoodlibrary.utilities.RestaurantFilters;
import com.google.android.gms.maps.model.LatLng;
import com.polito.group3.ufoodfusion.activities.G3UserSearchRestaurant;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Alfonso on 20/04/2016.
 */
public class G3CardReastaurantAdapter extends RecyclerView.Adapter<G3CardReastaurantAdapter.ViewHolder> {

    private onCardRestaurantClick mListener;
    private ArrayList<G3RestaurantObj> mDataSet;

    public G3CardReastaurantAdapter(ArrayList<G3RestaurantObj> dataSet){

        this.mDataSet = dataSet;
    }

    public interface onCardRestaurantClick{

        public void onCardClick(int position, ImageView image);

    }

    public void setOnCardRestaurantListener(final onCardRestaurantClick listener){
        this.mListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        public ImageView mRestaurantImage;
        public TextView mNameRestaurant;
        public TextView mTypeKitchen;
        public TextView mDistance;
        public RatingBar mRewiRating;
        public TextView mNumRew;
        public TextView mPriceRating;
        public TextView mClosedLabel;

        public ViewHolder(View v) {
            super(v);
            mView = v;

            mRestaurantImage = (ImageView) v.findViewById(R.id.restaurantImage);

            mNameRestaurant = (TextView) v.findViewById(R.id.reastaurantName);

            mTypeKitchen = (TextView) v.findViewById(R.id.typekitchen);

            mDistance = (TextView) v.findViewById(R.id.distance);

            mRewiRating = (RatingBar) v.findViewById(R.id.starratingbar);

            mNumRew = (TextView) v.findViewById(R.id.numrew);

            mPriceRating = (TextView) v.findViewById(R.id.price_range);

            mClosedLabel = (TextView) v.findViewById(R.id.todayclosedlabel) ;

            mView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            if(mListener != null){
                ImageView image = (ImageView) v.findViewById(R.id.restaurantImage);
                mListener.onCardClick(getAdapterPosition(), image );
            }

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_search_restaurant_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        G3RestaurantObj temp = mDataSet.get(position);

        holder.mRewiRating.setNumStars(5);
        float number = (float) temp.getReviewsAvgScore();
        holder.mRewiRating.setRating((float)temp.getReviewsAvgScore());
        holder.mNameRestaurant.setText(temp.getName());
        holder.mTypeKitchen.setText(temp.getCategoryAsEnum().getString());
        DecimalFormat df2 = new DecimalFormat(".##");
        if( G3UserSearchRestaurant.getCurrentPos() != null) {
            double dist = RestaurantFilters.calculateDistance(G3UserSearchRestaurant.getCurrentPos(), new LatLng(temp.getAddressObj().getLatitude(), temp.getAddressObj().getLongitude()));

            if (dist >= 1) {
                holder.mDistance.setText(df2.format(dist) + " Km");
            } else {
                holder.mDistance.setText(Math.round(dist * 1000) + " m");
            }
        } else {
            holder.mDistance.setVisibility(View.INVISIBLE);
        }


        boolean isOpen = false;

        DateTime now = new DateTime();
        String dayOfTheWeek = G3OpeningHoursObj.Day.getDayFromInt(
                now.getDayOfWeek() - 1 ).getString();

        // Check if the restaurant is open
        if (temp.getOpeningHours().containsKey(dayOfTheWeek + JsonKey.LUNCHTAG)) {

            int from = temp.getOpeningHours().get(dayOfTheWeek + JsonKey.LUNCHTAG).getFrom();
            int to = temp.getOpeningHours().get(dayOfTheWeek + JsonKey.LUNCHTAG).getTo();

            String f = String.format("%04d", from);
            String t = String.format("%04d", to);

            Integer fh = Integer.valueOf(f.substring(0, 2));
            Integer fm = Integer.valueOf(f.substring(2));

            Integer th = Integer.valueOf(t.substring(0, 2));
            Integer tm = Integer.valueOf(t.substring(2));

            DateTime open = now.withHourOfDay(fh).withMinuteOfHour(fm);
            DateTime close = now.withHourOfDay(th).withMinuteOfHour(tm);

            if (now.isBefore(close)) isOpen = true;

        }

        if (temp.getOpeningHours().containsKey(dayOfTheWeek + JsonKey.DINNERTAG)) {

            int from = temp.getOpeningHours().get(dayOfTheWeek + JsonKey.DINNERTAG).getFrom();
            int to = temp.getOpeningHours().get(dayOfTheWeek + JsonKey.DINNERTAG).getTo();

            String f = String.format("%04d", from);
            String t = String.format("%04d", to);

            Integer fh = Integer.valueOf(f.substring(0, 2));
            Integer fm = Integer.valueOf(f.substring(2));

            Integer th = Integer.valueOf(t.substring(0, 2));
            Integer tm = Integer.valueOf(t.substring(2));

            DateTime open = now.withHourOfDay(fh).withMinuteOfHour(fm);
            DateTime close = now.withHourOfDay(th).withMinuteOfHour(tm);

            if (now.isBefore(close)) isOpen = true;

        }

        if(!isOpen){
            holder.mClosedLabel.setVisibility(View.VISIBLE);
            holder.mView.setClickable(false);
        }

        String url  = temp.getPhotoPath();
        if(url != null && url.length()>0)
        {
            Picasso.with(G3Application.getAppContext()).load(url).placeholder(R.drawable.default_restaurant)
                    .into(holder.mRestaurantImage);
        }else {
            Picasso.with(G3Application.getAppContext()).load(R.drawable.default_restaurant)
                    .into(holder.mRestaurantImage);
        }

        holder.mNumRew.setText("(" + temp.getNumOfReviews() + ")");

        holder.mPriceRating.setText(temp.getPriceRatingEuro());

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
