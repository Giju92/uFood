package com.polito.group3.ufoodfusion.dialogfragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3UserObj;
import com.polito.group3.ufoodfusion.adapters.G3OrderDetailsAdapter;

/**
 * Created by Mattia on 12/04/2016.
 */
public class G3OrderDetailsDialogFragment extends android.support.v4.app.DialogFragment{

    private static G3OrderObj morder;
    private static G3UserObj mUserobj;
    private static int mposition;
    private static String mTag;
    private LinearLayout mlinearlay;

    private TextView mtotalpricelabel;
    private RecyclerView mrecycleview;
    private TextView mtotalpricetext;
    private TextView madditionalnotestext;
    private G3OrderDetailsAdapter madapter;
    private RecyclerView.LayoutManager mlayoutmanager;
    private static Context mcontext;
    private Button done_button;
    private Button reject_button;

    public static G3OrderDetailsDialogFragment newInstance(G3OrderObj g3OrderObj, G3UserObj g3UserObj, int position, Context context) {
        G3OrderDetailsDialogFragment fragment = new G3OrderDetailsDialogFragment();
        morder = g3OrderObj;
        mposition = position;
        mUserobj = g3UserObj;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.orderdetails_dialogfragment, container, false);
        getDialog().setTitle(getResources().getString(R.string.add));
        mTag = getTag();

        mlinearlay = (LinearLayout) v.findViewById(R.id.linearlayout);

        mtotalpricelabel = (TextView) v.findViewById(R.id.totalprice);
        mrecycleview = (RecyclerView) v.findViewById(R.id.recycleview);
        mtotalpricetext = (TextView) v.findViewById(R.id.totalpricetext);

        //madditionalnotestext = (TextView) v.findViewById(R.id.additionalnotestext);


        if(morder != null){
            getDialog().setTitle(getResources().getString(R.string.details));
            loadData();
        }
        return v;
    }

    private void loadData() {


        mrecycleview.setHasFixedSize(true);
        mlayoutmanager = new LinearLayoutManager(getActivity());
        mrecycleview.setLayoutManager(mlayoutmanager);

        madapter = new G3OrderDetailsAdapter(morder.getOrderItems());
        mrecycleview.setAdapter(madapter);

        mtotalpricetext.setText(String.valueOf(morder.getTotalOrderPrice())+" €");
/*
        if(morder.getAdditionalNotes() != null) {
            madditionalnotestext.setText(morder.getAdditionalNotes());
        } else{
            madditionalnotestext.setVisibility(View.GONE);
        }*/
    }

}
