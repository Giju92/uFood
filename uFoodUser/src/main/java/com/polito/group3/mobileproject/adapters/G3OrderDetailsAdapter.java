package com.polito.group3.mobileproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3OrderItem;

import java.util.List;

/**
 * Created by Mattia on 13/04/2016.
 */
public class G3OrderDetailsAdapter extends RecyclerView.Adapter<G3OrderDetailsAdapter.ViewHolder>{

        private List<G3OrderItem> morderitems;

    public G3OrderDetailsAdapter(List<G3OrderItem> morderitems) {
        this.morderitems = morderitems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView menutitemnametext, menuitemquantitytext;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            menutitemnametext = (TextView) v.findViewById(R.id.menuitemnametext);
            menuitemquantitytext = (TextView) v.findViewById(R.id.menuitemquantitytext);
        }


    }

    @Override
    public G3OrderDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_details_orders, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.menutitemnametext.setText(morderitems.get(position).getMenuItem().getName());
        holder.menuitemquantitytext.setText(String.valueOf(morderitems.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return morderitems.size();
    }

}
