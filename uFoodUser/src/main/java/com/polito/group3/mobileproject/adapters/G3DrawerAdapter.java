package com.polito.group3.mobileproject.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3DrawerObj;

import java.util.ArrayList;

/**
 * Created by Alfonso on 12/04/2016.
 */
public class G3DrawerAdapter extends RecyclerView.Adapter<G3DrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private int mProfileImage;
    private String mTitle;
    private ArrayList<G3DrawerObj> mDataSet;
    private  onItemClickListener mListener;

    public G3DrawerAdapter(int profileImage, String title, ArrayList<G3DrawerObj> dataSet) {

        mProfileImage = profileImage;
        mTitle = title;
        mDataSet = dataSet;

    }

    public interface onItemClickListener{
        public void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(final onItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int holderid;
        View mView;
        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView mTitle;

        public ViewHolder(View v,int type) {
            super(v);

            mView = v;

            if(type == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                holderid = 1;
                mView.setOnClickListener(this);
            }
            else{
                mTitle = (TextView) itemView.findViewById(R.id.drawerTitle);
                profile = (ImageView) itemView.findViewById(R.id.circleView);
                holderid = 0;
            }

        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(textView, getAdapterPosition());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item,parent,false);

            ViewHolder vhItem = new ViewHolder(v,viewType);

            return vhItem;


        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_drawer, parent, false);

            ViewHolder vhHeader = new ViewHolder(v, viewType);

            return vhHeader;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(holder.holderid ==1) {
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mDataSet.get(position-1).getmNameDrawer());
            holder.imageView.setImageResource(mDataSet.get(position-1).getmImageDrawer());
        }
        else{
            holder.profile.setImageResource(mProfileImage);
            holder.mTitle.setText(mTitle);

        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size()+1;


    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }




}
