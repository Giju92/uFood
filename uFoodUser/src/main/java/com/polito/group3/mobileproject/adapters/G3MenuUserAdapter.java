package com.polito.group3.mobileproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ufoodlibrary.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 23-Apr-16.
 */
public class G3MenuUserAdapter extends RecyclerView.Adapter<G3MenuUserAdapter.ViewHolder>  {


    private onCategoryClick mListener;
    private ArrayList<String> mDataSet;

    public G3MenuUserAdapter(ArrayList<String> dataSet) {

        this.mDataSet = dataSet;
    }

    public interface onCategoryClick {

        void onCatClick(View v, String category);

    }

    public void setOnCardRestaurantListener(final onCategoryClick listener) {
        this.mListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        public TextView mTextString;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mTextString = (TextView) v.findViewById(R.id.string);
            mView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (mListener != null) {
                mListener.onCatClick(v, mDataSet.get(getAdapterPosition()));
            }

        }
    }

    @Override
    public G3MenuUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_string, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(G3MenuUserAdapter.ViewHolder holder, int position) {

        holder.mTextString.setText(mDataSet.get(position));

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
