package com.polito.group3.mobileproject.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3MenuItem;
import com.example.ufoodlibrary.utilities.BitmapUtils;
import com.example.ufoodlibrary.utilities.G3Application;

import java.util.LinkedList;

/**
 * Created by Alfonso on 10-Apr-16.
 */
public class G3MenuRestourantCategoryAdapter extends RecyclerView.Adapter<G3MenuRestourantCategoryAdapter.ViewHolder> {

    private onItemCategoryClickListener mListener;
    private LinkedList<G3MenuItem> mDataSet;

    public G3MenuRestourantCategoryAdapter (LinkedList<G3MenuItem> dataSet){

        this.mDataSet = dataSet;

    }

    public interface onItemCategoryClickListener{

        void onDeleteItemClick(int position);
        void onEditItemClick(int position);

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        public ImageView mItemImage;
        public TextView mDiscountText;
        public ImageButton mDeleteButton;
        public TextView mNameItem;
        public TextView mPriceItem;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mItemImage = (ImageView) mView.findViewById(R.id.imageitem);
            mDiscountText = (TextView) mView.findViewById(R.id.offertItem);
            mDeleteButton = (ImageButton) mView.findViewById(R.id.deleteItem);
            mNameItem = (TextView) mView.findViewById(R.id.nameitem);
            mPriceItem = (TextView) mView.findViewById(R.id.priceitem);

            mView.setOnClickListener(this);
            mDeleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                if(v.getId() == R.id.deleteItem){
                    removeAt(getAdapterPosition());
                    mListener.onDeleteItemClick(getAdapterPosition());
                }
                else{
                    mListener.onEditItemClick(getAdapterPosition());
                    edit(getAdapterPosition());
                }

            }
        }
    }

    private void edit(int adapterPosition) {
        notifyItemChanged(adapterPosition);
    }

    public void setOnItemClickListener(final onItemCategoryClickListener listener){
        this.mListener = listener;
    }

    @Override
    public G3MenuRestourantCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_restourant_cat_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        G3MenuItem temp = mDataSet.get(position);
        if(!temp.isAvailable()){
            holder.mView.setBackgroundColor(Color.parseColor("#FF5252"));
        }
        else{
            holder.mView.setBackgroundColor(Color.WHITE);
        }

        if(temp.getTypeofItem() == 1){
            BitmapUtils.loadImage(R.drawable.plate_red, holder.mItemImage);
        }
        if(temp.getTypeofItem() == 0){
            BitmapUtils.loadImage(R.drawable.drink_white,holder.mItemImage);
        }
        if(temp.getSalePercentage()>0){
            holder.mDiscountText.setVisibility(View.VISIBLE);
            holder.mDiscountText.setText(temp.getSalePercentage() + G3Application.getAppContext().getString(R.string.percentage_symbol));
        }
        else{
            holder.mDiscountText.setVisibility(View.INVISIBLE);
        }

        holder.mNameItem.setText(temp.getName());
        holder.mPriceItem.setText(String.valueOf(temp.getPrice())+" "+ G3Application.getAppContext().getString(R.string.euro_symbol));

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void removeAt(int position) {
        mDataSet.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataSet.size());
    }


}
