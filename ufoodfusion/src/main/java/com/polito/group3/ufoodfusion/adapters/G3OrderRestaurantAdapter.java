package com.polito.group3.ufoodfusion.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3UserObj;
import com.example.ufoodlibrary.utilities.G3Application;

import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * Created by Mattia on 12/04/2016.
 */
public class G3OrderRestaurantAdapter extends RecyclerView.Adapter<G3OrderRestaurantAdapter.ViewHolder>{

    public LinkedList<G3OrderObj> mDataset;
    public LinkedList<G3UserObj> mUserSet;
    onItemClickListener mListener;
    Context mcontext;

    public G3OrderRestaurantAdapter(LinkedList<G3OrderObj> mdataSet, LinkedList<G3UserObj> users, Context context) {
        mDataset = mdataSet;
        mUserSet = users;
        mcontext = context;
    }

    public interface onItemClickListener{
        void onItemClick(View v, G3OrderObj order, G3UserObj user, int position);
        void onCancelButtonClick(int position);
        void onEvadeButtonClick(int position);
    }


    public void setOnItemClickListener(final onItemClickListener listener){
        this.mListener = listener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        public TextView id, type, time, status, price, name;
        public Button evadeorderbutton, cancelorderbutton;
        public RelativeLayout relative;
        public CardView card;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            time = (TextView) view.findViewById(R.id.order_time);
            name = (TextView) view.findViewById(R.id.account_name);
            type = (TextView) view.findViewById(R.id.order_type);
            status = (TextView) view.findViewById(R.id.order_status);
            evadeorderbutton = (Button) view.findViewById(R.id.button_done);
            cancelorderbutton = (Button) view.findViewById(R.id.button_reject);
            relative = (RelativeLayout) view.findViewById(R.id.card_layout);
            card = (CardView) view.findViewById(R.id.card_order);


            mView.setOnClickListener(this);
            evadeorderbutton.setOnClickListener(this);
            cancelorderbutton.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if(mListener != null){
                if(v.getId() == R.id.button_done){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);

                    builder.setMessage(R.string.are_you_sure_evade_order).setTitle(R.string.evade);

                    builder.setNegativeButton(R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog

                                }
                            });
                    builder.setPositiveButton(R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mListener.onEvadeButtonClick(getAdapterPosition());
                                }
                            });

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                else{
                    if(v.getId() == R.id.button_reject){


                        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);

                        builder.setMessage(R.string.are_you_sure_cancel_order).setTitle(R.string.cancel);

                        builder.setNegativeButton(R.string.no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                        builder.setPositiveButton(R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mListener.onCancelButtonClick(getAdapterPosition());
                                    }
                                });

                        // 3. Get the AlertDialog from create()
                        AlertDialog dialog = builder.create();
                        dialog.show();


                    } else {
                        G3OrderObj temporder = mDataset.get(getAdapterPosition());
                        G3UserObj tempuser = null;
                        String userdid = temporder.getUserid();
                        for(G3UserObj user: mUserSet){
                            if(user.getId().equals(userdid)){
                                tempuser = user;
                                break;
                            }
                        }
                        mListener.onItemClick(v,temporder,tempuser, getAdapterPosition());
                        card.setCardBackgroundColor(G3Application.getAppContext().getResources().getColor(R.color.white));
                    }

                }
            }
        }
    }

    @Override
    public G3OrderRestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_restaurant_order, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        G3OrderObj temporder = mDataset.get(position);
        G3UserObj tempuser = null;
        String userdid = temporder.getUserid();
        for(G3UserObj user: mUserSet){
            if(user.getId().equals(userdid)){
                tempuser = user;
                break;
            }
        }

        if(tempuser != null){

            if(!mDataset.get(position).isVisualized())
                holder.card.setCardBackgroundColor(G3Application.getAppContext().getResources().getColor(R.color.pallid_yellow));

            holder.time.setText(mDataset.get(position).getDate().getHours()+":"+getformatted(mDataset.get(position).getDate().getMinutes()));
            if(mDataset.get(position).getSeats()==0)
                holder.type.setText(G3Application.getAppContext().getResources().getString(R.string.takeaway).toUpperCase());
            else {
                holder.type.setText(" " + mDataset.get(position).getSeats());
                holder.type.setCompoundDrawablesWithIntrinsicBounds(G3Application.getAppContext().getResources().getDrawable(R.drawable.number_seats_red),null,null,null);
            }
            holder.name.setText(" " + tempuser.getLastName().substring(0, 1).toUpperCase() + tempuser.getLastName().substring(1) + " " + tempuser.getFirstName().substring(0, 1).toUpperCase()+tempuser.getFirstName().substring(1));
            //operation to get number and set the listener
            final String uri;
            if(!tempuser.getMobileNumber().isEmpty())
                uri = "tel:" + tempuser.getMobileNumber();
            else
                uri = "tel:" + tempuser.getPhoneNumber();
            holder.name.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                        mcontext.startActivity(dialIntent);
                    } catch (Exception e) {
                        Log.e("call", "dial not started");
                    }
                }
            });

            holder.status.setText(mDataset.get(position).getOrderasString());

            switch (mDataset.get(position).getOrderState()) {
                case 0:
                    holder.status.setTextColor(G3Application.getAppContext().getResources().getColor(R.color.orange));
                    break;
                case 1:
                    holder.status.setTextColor(G3Application.getAppContext().getResources().getColor(R.color.green));
                    holder.cancelorderbutton.setVisibility(View.GONE);
                    holder.evadeorderbutton.setVisibility(View.GONE);
                    break;
                case 2:
                    holder.status.setTextColor(G3Application.getAppContext().getResources().getColor(R.color.red));
                    holder.cancelorderbutton.setVisibility(View.GONE);
                    holder.evadeorderbutton.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }

        }

    }




    private String getformatted(int minutes) {
        DecimalFormat df = new DecimalFormat("00");
        return(df.format(minutes));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }



    
}
