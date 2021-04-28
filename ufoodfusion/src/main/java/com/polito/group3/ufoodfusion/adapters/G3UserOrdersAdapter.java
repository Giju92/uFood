package com.polito.group3.ufoodfusion.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Luigi on 05/05/2016.
 */
public class G3UserOrdersAdapter extends RecyclerView.Adapter<G3UserOrdersAdapter.ViewHolder> {

    private onOrderClick mListener;
    private ArrayList<G3OrderObj> mDataSet;
    private ArrayList<G3RestaurantObj> restaurants;

    public G3UserOrdersAdapter(ArrayList<G3OrderObj> dataSet,
                               ArrayList<G3RestaurantObj> restaurants){

        this.mDataSet = dataSet;
        this.restaurants = restaurants;
    }

    public interface onOrderClick{

        void onCardClick(int position);

        void onDetailsClick(int adapterPosition);

    }

    public void setOnOrderClickListener(final onOrderClick listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        public ImageView mRestaurantImage;
        public TextView mNameRestaurant;
        public TextView mTotalOrder;
        public TextView mOrderStatus;
        public TextView mOrderDate;
        public TextView mOrderRatedLabel;
        public Button mRateOrderButton;
        public Button mDetailsButton;

        public ViewHolder(View v) {

            super(v);
            mView = v;

            mRestaurantImage = (ImageView) v.findViewById(R.id.logo);
            mNameRestaurant = (TextView) v.findViewById(R.id.restaurant_name);
            mTotalOrder = (TextView) v.findViewById(R.id.order_total);
            mOrderStatus = (TextView) v.findViewById(R.id.order_status);
            mOrderDate = (TextView) v.findViewById(R.id.order_date);
            mOrderRatedLabel = (TextView) v.findViewById(R.id.order_rated_label);
            mRateOrderButton = (Button) v.findViewById(R.id.rate_order_button);
            mDetailsButton = (Button) v.findViewById(R.id.details_button);

            mRateOrderButton.setOnClickListener(this);
            mDetailsButton.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            if(mListener != null){
                if(v.getId() == mRateOrderButton.getId())
                    mListener.onCardClick(getAdapterPosition());
                if(v.getId() == mDetailsButton.getId())
                    mListener.onDetailsClick(getAdapterPosition());
            }

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_order_details_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        G3OrderObj temp = mDataSet.get(position);
        G3RestaurantObj temRest = null;

        for(G3RestaurantObj rest: restaurants) {
            if (rest.getId().equals(temp.getRestaurantId())){
                temRest = rest;
                break;
            }
        }

        if(temRest != null){
            String url= temRest.getPhotoPath();
            if(url != null && url.length()>0)
            {
                Picasso.with(G3Application.getAppContext()).load(url).placeholder(R.drawable.default_restaurant)
                        .into(holder.mRestaurantImage);
            }else {
                Picasso.with(G3Application.getAppContext()).load(R.drawable.default_restaurant)
                        .into(holder.mRestaurantImage);
            }
            holder.mNameRestaurant.setText(temRest.getName());
        }




        String orderStatus;
        switch (temp.getOrderState()) {
            case 0:
                orderStatus = G3Application.getAppContext().getResources().getString(R.string.pending_status);
                holder.mOrderStatus.setTextColor(G3Application.getAppContext().getResources().getColor(R.color.orange));
                break;
            case 1:
                orderStatus = G3Application.getAppContext().getResources().getString(R.string.done_status);
                holder.mOrderStatus.setTextColor(G3Application.getAppContext().getResources().getColor(R.color.green));
                break;
            case 2:
                orderStatus = G3Application.getAppContext().getResources().getString(R.string.rejected_status);
                holder.mOrderStatus.setTextColor(G3Application.getAppContext().getResources().getColor(R.color.red));
                break;
            default:
                orderStatus = "";
                break;
        }
        holder.mOrderStatus.setText(orderStatus);

        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat sdf2 = new SimpleDateFormat("HH:mm");
        holder.mOrderDate.setText(sdf.format(temp.getDate()) + "\n"
                + G3Application.getAppContext().getResources().getString(R.string.at_label)
                + sdf2.format(temp.getDate()));

        DecimalFormat df = new DecimalFormat(".00");
        String totalOrderText = G3Application.getAppContext().getResources().getString(R.string.total_order_label)+" "
                + df.format(temp.getTotalOrderPrice()) + " (" + temp.getOrderItems().size() + " "
                + G3Application.getAppContext().getResources().getString(R.string.products) + ")";
        holder.mTotalOrder.setText(totalOrderText);

        if(temp.getOrderState() == 1 && temp.getReviewId() == null) {
            holder.mOrderRatedLabel.setVisibility(View.GONE);
            holder.mRateOrderButton.setClickable(true);
            holder.mRateOrderButton.setTextColor(G3Application.getAppContext().getResources().getColor(R.color.secondary_text));
        } else if(temp.getOrderState() == 1 && temp.getReviewId() != null) {
            holder.mOrderRatedLabel.setVisibility(View.VISIBLE);
            holder.mRateOrderButton.setClickable(false);
            holder.mRateOrderButton.setTextColor(G3Application.getAppContext().getResources().getColor(R.color.light_grey));
        } else {
            holder.mOrderRatedLabel.setVisibility(View.GONE);
            holder.mRateOrderButton.setClickable(false);
            holder.mRateOrderButton.setTextColor(G3Application.getAppContext().getResources().getColor(R.color.light_grey));
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}