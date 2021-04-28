package com.polito.group3.ufoodfusion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3ReviewObj;
import com.example.ufoodlibrary.objects.G3UserObj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Mattia on 09/05/2016.
 */
public class G3UserReviewAdapter  extends RecyclerView.Adapter<G3UserReviewAdapter.ViewHolder> {

    private ArrayList<G3ReviewObj> mDataSet;
    private ArrayList<G3OrderObj> mOrders;
    private ArrayList<G3UserObj> mUsers;
    private Context mCtx;

    public G3UserReviewAdapter(ArrayList<G3ReviewObj> mSet, ArrayList<G3UserObj> mUsers) {
        this.mDataSet = mSet;
        this.mUsers = mUsers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView mUsernameText;
        public TextView mReviewText;
        public RatingBar mReviewRatingbar;
        public TextView mReviewDateText;
        public TextView mReplyText;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mUsernameText = (TextView) v.findViewById(R.id.usernametext);
            mReviewText = (TextView) v.findViewById(R.id.reviewtext);
            mReviewRatingbar = (RatingBar) v.findViewById(R.id.reviewratingbar);
            mReviewDateText = (TextView) v.findViewById(R.id.reviewdatetext);
            mReplyText = (TextView) v.findViewById(R.id.replytext);
        }


    }

    @Override
    public G3UserReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_review_adapter, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(G3UserReviewAdapter.ViewHolder holder, int position) {
        G3UserObj temp = null;
        for(G3UserObj user: mUsers){
            if(user.getId().equals(mDataSet.get(position).getUsername())){
                temp = user;
                break;
            }
        }
        if(temp != null){
            holder.mUsernameText.setText(temp.getFirstName());
        }

        holder.mReviewRatingbar.setRating((float)mDataSet.get(position).getOverallRating());
        holder.mReviewText.setText(mDataSet.get(position).getReviewText());

        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        holder.mReviewDateText.setText(sdf.format(mDataSet.get(position).getDate()));

        if(mDataSet.get(position).getReply() != null) {
            holder.mReplyText.setText(mDataSet.get(position).getReply());
            holder.mReplyText.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
