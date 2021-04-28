package com.polito.group3.ufoodfusion.adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3ReviewObj;
import com.example.ufoodlibrary.objects.G3UserObj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Mattia on 09/05/2016.
 */
public class G3ReviewAdapter extends RecyclerView.Adapter<G3ReviewAdapter.ViewHolder> {

    private ArrayList<G3ReviewObj> mDataSet;
    private ArrayList<G3OrderObj> mOrderSet;
    private ArrayList<G3UserObj> mUserData;

    private OnSaveReplyClickListener mListener;
    private Context mCtx;

    public G3ReviewAdapter(ArrayList<G3ReviewObj> mSet, ArrayList<G3OrderObj> mOrder, ArrayList<G3UserObj> mUsers, Context mCtx) {
        this.mCtx = mCtx;
        this.mDataSet = mSet;
        this.mOrderSet = mOrder;
        this.mUserData = mUsers;
    }

    public interface OnSaveReplyClickListener{
        void onSaveReplyClick(String reviewId, String reply, int position);
    }


    public void setOnSaveReplyClickListener(final OnSaveReplyClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        public TextView mUsernameText;
        public TextView mReviewText;
        public RatingBar mOverallRatingbar;
        public RatingBar mFoodRatingbar;
        public RatingBar mPunctualityRatingbar;
        public RatingBar mServiceRatingbar;
        public TextView mReviewDateText;
        public Button mSaveButton;
        public Button mReplyButton;
        public Button mCancelButton;
        public TextView mReplyText;
        public EditText mReplyEditText;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mUsernameText = (TextView) v.findViewById(com.example.ufoodlibrary.R.id.usernametext);
            mReviewText = (TextView) v.findViewById(com.example.ufoodlibrary.R.id.reviewtext);
            mOverallRatingbar = (RatingBar) v.findViewById(com.example.ufoodlibrary.R.id.reviewratingbar);
            mFoodRatingbar = (RatingBar) v.findViewById(com.example.ufoodlibrary.R.id.foodratingBar);
            mPunctualityRatingbar = (RatingBar) v.findViewById(com.example.ufoodlibrary.R.id.punctualityratingBar);
            mServiceRatingbar = (RatingBar) v.findViewById(com.example.ufoodlibrary.R.id.serviceratingBar);
            mReviewDateText = (TextView) v.findViewById(com.example.ufoodlibrary.R.id.reviewdatetext);
            mReplyButton = (Button) v.findViewById(com.example.ufoodlibrary.R.id.reply_button);
            mSaveButton = (Button) v.findViewById(com.example.ufoodlibrary.R.id.save_button);
            mCancelButton = (Button) v.findViewById(com.example.ufoodlibrary.R.id.cancel_button);
            mReplyText = (TextView) v.findViewById(com.example.ufoodlibrary.R.id.replytext);
            mReplyEditText = (EditText) v.findViewById(com.example.ufoodlibrary.R.id.replyEditText);
            mReplyButton.setOnClickListener(this);
            mSaveButton.setOnClickListener(this);
            mCancelButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v.getId() == mReplyButton.getId()) {
                mReplyButton.setVisibility(View.GONE);
                mReplyEditText.setVisibility(View.VISIBLE);
                mSaveButton.setVisibility(View.VISIBLE);
                mCancelButton.setVisibility(View.VISIBLE);

                mReplyEditText.requestFocus();
            }

            if(v.getId() == mCancelButton.getId()) {
                InputMethodManager imm = (InputMethodManager) mCtx.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mReplyEditText.getWindowToken(), 0);
                mReplyEditText.clearFocus();

                mReplyEditText.setVisibility(View.GONE);
                mSaveButton.setVisibility(View.GONE);
                mCancelButton.setVisibility(View.GONE);
                mReplyButton.setVisibility(View.VISIBLE);
            }

            if(v.getId() == mSaveButton.getId()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setMessage(com.example.ufoodlibrary.R.string.are_you_sure).setTitle(com.example.ufoodlibrary.R.string.save);
                builder.setPositiveButton(com.example.ufoodlibrary.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                InputMethodManager imm = (InputMethodManager) mCtx.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mReplyEditText.getWindowToken(), 0);
                                mReplyEditText.clearFocus();

                                mListener.onSaveReplyClick(mDataSet.get(getAdapterPosition()).getId(),
                                        mReplyEditText.getText().toString(),
                                        getAdapterPosition());
                            }
                        });
                builder.setNegativeButton(com.example.ufoodlibrary.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            }

        }

    }

    @Override
    public G3ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(com.example.ufoodlibrary.R.layout.review_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(G3ReviewAdapter.ViewHolder holder, int position) {

        G3ReviewObj tempRew = mDataSet.get(position);
        G3UserObj tempUsr = null;
        G3OrderObj tempOrd = null;
        String userid = tempRew.getUsername();
        String orderid = tempRew.getOrderId();
        for(G3UserObj u: mUserData){
            if(u.getId().equals(userid)){
                tempUsr = u;
                break;
            }
        }
        for(G3OrderObj o : mOrderSet){
            if(o.getOrderid().equals(orderid)){
                tempOrd = o;
                break;
            }
        }


        if(tempRew != null && tempOrd != null && tempUsr != null){
            holder.mUsernameText.setText(tempUsr.getFirstName());
            holder.mOverallRatingbar.setRating((float) tempRew.getOverallRating());
            holder.mFoodRatingbar.setRating((float) tempRew.getFoodRating());
            holder.mPunctualityRatingbar.setRating((float) tempRew.getPunctualityRating());
            holder.mServiceRatingbar.setRating((float) tempRew.getServiceRating());
            holder.mReviewText.setText(tempRew.getReviewText());

            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            holder.mReviewDateText.setText(sdf.format(tempOrd.getDate()));

            if (tempRew.getReply() != null) {
                holder.mReplyText.setText(tempRew.getReply());
                holder.mReplyText.setVisibility(View.VISIBLE);
                holder.mReplyButton.setVisibility(View.GONE);
            } else {
                holder.mReplyText.setVisibility(View.GONE);
                holder.mReplyButton.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
