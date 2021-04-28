package com.polito.group3.mobileproject.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3OrderItem;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.utilities.G3Application;

import java.util.ArrayList;

/**
 * Created by Alfonso on 10/05/2016.
 */
public class G3BasketAdapter extends RecyclerView.Adapter<G3BasketAdapter.ViewHolder> {

    private ArrayList<G3OrderItem> mDataSet;
    private G3OrderObj mOrder;
    private Context mCtx;
    private boolean change = false;

    public G3BasketAdapter(ArrayList < G3OrderItem > dataSet, G3OrderObj mOrder, Context mCtx){

        this.mDataSet = dataSet;
        this.mOrder = mOrder;
        this.mCtx = mCtx;
    }

    public void setChange(){
        this.change = !this.change;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        public TextView mNameItem;
        public TextView mPricexQnt;
        public TextView mTotalPrice;
        public TextView mDiscountedPrice;
        public TextView mQntitem;
        public ImageButton mMenoitem;
        public ImageButton mPlusitem;
        public LinearLayout mChangeLayout;

        public ViewHolder(View v) {
            super(v);

            mView = v;
            mNameItem = (TextView) v.findViewById(R.id.nameitemtext);
            mPricexQnt = (TextView) v.findViewById(R.id.quantityitemtext);
            mTotalPrice = (TextView) v.findViewById(R.id.totalpriceitem);
            mDiscountedPrice = (TextView) v.findViewById(R.id.discountedpriceitem);
            mQntitem = (TextView) v.findViewById(R.id.qntitem);
            mMenoitem = (ImageButton) v.findViewById(R.id.itemmeno);
            mPlusitem = (ImageButton) v.findViewById(R.id.itemplus);
            mChangeLayout = (LinearLayout) v.findViewById(R.id.changeitemlay);
            mPlusitem.setOnClickListener(this);
            mMenoitem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            G3OrderItem temp = mDataSet.get(getAdapterPosition());
            int qnt= temp.getQuantity();
            if(v.getId() == mPlusitem.getId()){

                temp.setQuantity(++qnt);
                mQntitem.setText(String.valueOf(qnt));
                mOrder.refreshOrder();
                notifyItemChanged(getAdapterPosition());

            }
            if(v.getId() == mMenoitem.getId()){

                if(qnt>0){
                    temp.setQuantity(--qnt);
                    mQntitem.setText(String.valueOf(qnt));
                    mOrder.refreshOrder();
                    notifyItemChanged(getAdapterPosition());
                }
                if(temp.getQuantity() <=0){
                    mDataSet.remove(getAdapterPosition());
                    mOrder.removeOrderItem(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            }

        }

    }

    @Override
    public G3BasketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.basket_adapter_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(G3BasketAdapter.ViewHolder holder, int position) {

        G3OrderItem temp = mDataSet.get(position);

        if(temp != null) {
            holder.mNameItem.setText(temp.getMenuItem().getName());
            holder.mPricexQnt.setText(temp.getQuantity() + " x " + String.format("%.2f", temp.getMenuItem().getPrice()-temp.getMenuItem().getPrice()*temp.getMenuItem().getSalePercentage()/100) + " "+
                    G3Application.getAppContext().getResources().getString(R.string.euro_symbol));

            if(temp.getMenuItem().getSalePercentage()>0){
                holder.mTotalPrice.setText(String.format("%.2f", temp.getTotalPrice()+temp.getMenuItem().getSalePercentage()*temp.getQuantity()*temp.getMenuItem().getPrice()/100) + " " + G3Application.getAppContext().getResources().getString(R.string.euro_symbol));
                holder.mTotalPrice.setPaintFlags(holder.mTotalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.mTotalPrice.setTextColor(mCtx.getResources().getColor(R.color.primary));
                holder.mDiscountedPrice.setVisibility(View.VISIBLE);
                holder.mDiscountedPrice.setText(String.format( "%.2f", temp.getTotalPrice()) + " €" );


            } else {
                holder.mDiscountedPrice.setVisibility(View.GONE);
                holder.mTotalPrice.setText(String.format("%.2f", temp.getTotalPrice()) + " " +
                        G3Application.getAppContext().getResources().getString(R.string.euro_symbol));
            }

            holder.mQntitem.setText(String.valueOf(temp.getQuantity()));

            if (this.change) {
                holder.mTotalPrice.setVisibility(View.GONE);
                holder.mChangeLayout.setVisibility(View.VISIBLE);
            } else {
                holder.mTotalPrice.setVisibility(View.VISIBLE);
                holder.mChangeLayout.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
