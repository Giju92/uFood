package com.polito.group3.ufoodfusion.dialogfragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3MenuItem;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.G3Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alfonso-LAPTOP on 07/04/2016.
 */
public class G3AddCategoryDialogFragment extends android.support.v4.app.DialogFragment {

    private Spinner mSpin;
    private EditText mEdit;
    private onSpinnerSelected mListener;
    private ImageButton mButton;
    private RadioButton mCategoryButt;
    private RadioButton mDailyButt;
    private String bRet;
    private static G3RestaurantObj mRest;
    private static ArrayList<String> mDataset;
    private boolean dailypresent;

    // Container Activity must implement this interface
    public interface onSpinnerSelected {
        public void onSpinnerClickItem(String bRet);
    }

    public static G3AddCategoryDialogFragment newInstance(ArrayList<String> mdataSet, G3RestaurantObj mRestaurant) {
        mDataset = mdataSet;
        mRest = mRestaurant;
        return new G3AddCategoryDialogFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.addcategory_dialogfragment, container, false);
        getDialog().setTitle(getResources().getString(R.string.addcat));
        mEdit = (EditText) v.findViewById(R.id.otherselection);
        mSpin = (Spinner) v.findViewById(R.id.spinner);
        mButton = (ImageButton) v.findViewById(R.id.addimbutt);
        mCategoryButt = (RadioButton) v.findViewById(R.id.buttonCat);
        mDailyButt = (RadioButton) v.findViewById(R.id.buttonDaily);
        List<String> list = new ArrayList<String>();
        G3MenuItem.DishCategory[] cate = G3MenuItem.DishCategory.values();
        list.add(getResources().getString(R.string.selectcat));
        for(G3MenuItem.DishCategory c: cate){
            if(!mDataset.contains(c.getString())) {
                list.add(c.getString());
            }
        }
        list.add(getResources().getString(R.string.other));

        if(mRest.getmDailymenu() != null){
            dailypresent = true;
        }
        else{
            dailypresent = false;
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(G3Application.getAppContext(),R.layout.spinner_addcategory_item,list);
        mSpin.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        mDailyButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dailypresent){
                    mSpin.setVisibility(View.VISIBLE);
                    mCategoryButt.setChecked(true);
                    mDailyButt.setChecked(false);
                    mDailyButt.invalidate();
                    mCategoryButt.invalidate();
                    Toast.makeText(getContext(),getResources().getString(R.string.dail_present),Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDailyButt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && !dailypresent) {
                    mSpin.setVisibility(View.GONE);
                }
                else if(isChecked){
                    mSpin.setVisibility(View.VISIBLE);
                    mCategoryButt.setChecked(true);
                    Toast.makeText(getContext(),getResources().getString(R.string.dail_present),Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCategoryButt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSpin.setVisibility(View.VISIBLE);
                }
            }
        });


        mSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    TextView tx = (TextView) view;
                    if (tx.getText().equals(getResources().getString(R.string.other))) {
                        mEdit.setVisibility(View.VISIBLE);
                    } else {
                        mEdit.setVisibility(View.GONE);
                        bRet = tx.getText().toString();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bRet = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDailyButt.isChecked()) {
                    getTargetFragment().onActivityResult(1, 2, getActivity().getIntent().putExtra("daily", ""));
                    dismiss();

                } else if (bRet != null && !bRet.isEmpty() && mCategoryButt.isChecked()) {

                    String cap = bRet.substring(0, 1).toUpperCase() + bRet.substring(1);
                    bRet.toUpperCase().charAt(0);
                    getTargetFragment().onActivityResult(1, 1, getActivity().getIntent().putExtra("cat", cap));
                    dismiss();
                }
            }
        });

        return v;
    }
}
