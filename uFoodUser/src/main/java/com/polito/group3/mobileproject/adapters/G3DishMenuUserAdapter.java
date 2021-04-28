package com.polito.group3.mobileproject.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3MenuItem;

import java.util.ArrayList;

/**
 * Created by Mattia on 04/05/2016.
 */
public class G3DishMenuUserAdapter extends RecyclerView.Adapter<G3DishMenuUserAdapter.ViewHolder> {


    private ArrayList<G3MenuItem> mDataSet;

    private Context mCtx;
    private onAddItemToCart mListener;
    private boolean dailymenu= false;

    public G3DishMenuUserAdapter(ArrayList<G3MenuItem> dataSet, Context mCtx, boolean menu) {
        this.mCtx = mCtx;
        this.mDataSet = dataSet;
        this.dailymenu = menu;
    }


    public interface onAddItemToCart{
        void addItem(G3MenuItem item);
    }

    public void setOnAddItemToCartListener(onAddItemToCart listener){
        mListener = listener;

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public View mView;
        public TextView mDishNameText;
        public TextView mDishIngredientsText;
        public TextView mDishPriceText;
        public TextView mDishOffertText;
        public ImageButton mPurchaseButton;
        public LinearLayout mDishNameLayout;
        public LinearLayout mDishPriceLayout;
        public ImageView mVegIcon;
        public ImageView mGlutenfreeIcon;
        public TextView mDishDiscountedPriceText;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mDishNameText = (TextView) v.findViewById(R.id.dishnametext);
            mDishIngredientsText = (TextView) v.findViewById(R.id.dishingredientstext);
            mDishPriceText = (TextView) v.findViewById(R.id.dishpricetext);
            mDishOffertText = (TextView) v.findViewById(R.id.dishofferttext);
            mPurchaseButton = (ImageButton) v.findViewById(R.id.purchasebutton);
            mDishNameLayout = (LinearLayout) v.findViewById(R.id.dishnamelayout);
            mDishPriceLayout = (LinearLayout) v.findViewById(R.id.dishpricelayout);
            mVegIcon = (ImageView) v.findViewById(R.id.veg_icon);
            mGlutenfreeIcon = (ImageView) v.findViewById(R.id.glutenfreeicon);
            mDishDiscountedPriceText = (TextView) v.findViewById(R.id.dishdiscountedpricetext);
            mDishNameLayout.setOnClickListener(this);
            mDishPriceLayout.setOnClickListener(this);
            mPurchaseButton.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            if(mListener != null){
                mListener.addItem(mDataSet.get(getAdapterPosition()));
            }
        }
    }

    @Override
    public G3DishMenuUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dish_menu_user_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(G3DishMenuUserAdapter.ViewHolder holder, int position) {

        DisplayMetrics displayMetrics = mCtx.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mDishNameLayout.getLayoutParams();
        params.width = (int)dpWidth/3;
        holder.mDishNameLayout.setLayoutParams(params);

        holder.mDishNameText.setText(mDataSet.get(position).getName());

        if( mDataSet.get(position).getIngredients() != null){
            if(!mDataSet.get(position).getIngredients().equals("") ){
                holder.mDishIngredientsText.setText(mDataSet.get(position).getIngredients());
            }
            else {
                holder.mDishIngredientsText.setVisibility(View.GONE);
            }
        } else {
            holder.mDishIngredientsText.setVisibility(View.GONE);
        }

        if(!dailymenu) {
            if (mDataSet.get(position).getSalePercentage() > 0) {
                holder.mDishOffertText.setText(Integer.toString(mDataSet.get(position).getSalePercentage()) + "%");
                holder.mDishPriceText.setText(String.format("%.2f", mDataSet.get(position).getPrice()) + " €");
                holder.mDishPriceText.setPaintFlags(holder.mDishPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.mDishPriceText.setTextColor(mCtx.getResources().getColor(R.color.primary));
                holder.mDishDiscountedPriceText.setVisibility(View.VISIBLE);
                holder.mDishDiscountedPriceText.setText(String.format("%.2f", mDataSet.get(position).getPrice() - (mDataSet.get(position).getPrice() * mDataSet.get(position).getSalePercentage() / 100)) + " €");
            } else {
                holder.mDishOffertText.setVisibility(View.INVISIBLE);
                holder.mDishPriceText.setText(String.format("%.2f", mDataSet.get(position).getPrice()) + " €");
                holder.mDishDiscountedPriceText.setVisibility(View.GONE);

            }
        }


        if(mDataSet.get(position).isVegan()){
            holder.mVegIcon.setImageResource(R.drawable.vegan);
        }
        else{
            if(mDataSet.get(position).isVegetarian()){
                holder.mVegIcon.setImageResource(R.drawable.vegetarian);
            }else{
                holder.mVegIcon.setVisibility(View.GONE);
            }
        }

        if(!mDataSet.get(position).isGlutenFree()){
            holder.mGlutenfreeIcon.setVisibility(View.GONE);
        }

        if(dailymenu){
            holder.mPurchaseButton.setVisibility(View.INVISIBLE);
            holder.mDishOffertText.setVisibility(View.INVISIBLE);
            holder.mDishDiscountedPriceText.setVisibility(View.INVISIBLE);
            holder.mDishPriceLayout.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}

