package com.polito.group3.ufoodfusion.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ufoodlibrary.R;

import java.util.ArrayList;

/**
 * Created by Alfonso on 05-Apr-16.
 */
public class G3MenuRestourantAdapter extends RecyclerView.Adapter<G3MenuRestourantAdapter.ViewHolder> {

    private ArrayList<String> mDataset;
    onItemClickListener mListener;


    public G3MenuRestourantAdapter(ArrayList<String> myDataset) {

        mDataset = myDataset;
    }



    public interface onItemClickListener{
        public void onItemClick(View v, int position);
        public void onDeleteItem(View v, int position);
    }

    public void setOnItemClickListener(final onItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        public TextView text;
        public ImageButton mDeleteButton;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            text = (TextView) v.findViewById(R.id.textitemcategorymenu);
            mDeleteButton = (ImageButton) v.findViewById(R.id.deleteItem);
            mDeleteButton.setOnClickListener(this);
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                if(v.getId() == R.id.deleteItem){
                    int poss = getAdapterPosition();
                    removeAt(getAdapterPosition());
                    mListener.onDeleteItem(v, poss);
                }
                else{
                    mListener.onItemClick((TextView) v.findViewById(R.id.textitemcategorymenu), getAdapterPosition());
                }

            }
        }


    }

    private void removeAt(int adapterPosition) {
        mDataset.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(adapterPosition, mDataset.size());
    }


    @Override
    public G3MenuRestourantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.menu_restourant_itemcategory, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(G3MenuRestourantAdapter.ViewHolder holder, int position) {
        holder.text.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
