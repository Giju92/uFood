package com.polito.group3.mobileproject.dialogfragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3MenuItem;

/**
 * Created by Alfonso on 09-Apr-16.
 */
public class G3AddItemMenuDialogFragment extends android.support.v4.app.DialogFragment {

    private EditText mNameEdit;
    private EditText mPriceEdit;
    private EditText mIngredientsEdit;
    private RadioGroup mSelectType;
    private RadioButton mSelectButton;
    private RelativeLayout mFourrelative;
    private RelativeLayout mFiveRrelative;
    private RelativeLayout mSixRrelative;
    private CheckBox mSaleCheck;
    private CheckBox mVeganCheck;
    private CheckBox mVegetarianCheck;
    private CheckBox mGlutenfreeCheck;
    private CheckBox mAvailableCheck;
    private EditText mSaleEdit;
    private ImageButton mSaveButton;
    private ImageButton mCancButton;
    private TextView mSelectTypeText;
    private View mFocusView;
    private onSaveItemListener mListener;
    private static String mCat;
    private String mName;
    private String mIngredients = "";
    private String mPrice;
    private int mPercentage = 0;
    private boolean mVegan = false;
    private boolean mVegetarian = false;
    private boolean mGlutenfree = false;
    private boolean mAvailable = true;
    private static String mTag;
    private int mType = -1; // 0 food, 1 drink, -1 not set
    private static G3MenuItem mItem;
    private static int mPosition;




    public interface onSaveItemListener{
         void onSaveItemClick(G3MenuItem mItemMenu,String mode, int position);
    }


    public static G3AddItemMenuDialogFragment newInstance(String cat, G3MenuItem item, int position){
        G3AddItemMenuDialogFragment fragment = new G3AddItemMenuDialogFragment();
        mCat = cat;
        mItem = item;
        mPosition = position;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        try {
            mListener = (onSaveItemListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.additem_dialogfragment, container, false);
        getDialog().setTitle(getResources().getString(R.string.add));
        setCancelable(false);
        mTag = getTag();

        mFourrelative = (RelativeLayout) v.findViewById(R.id.fourRelative);
        mFiveRrelative = (RelativeLayout) v.findViewById(R.id.fiveRelative);
        mSixRrelative = (RelativeLayout) v.findViewById(R.id.sixRelative);
        mSelectType = (RadioGroup) v.findViewById(R.id.selecttype);
        mNameEdit = (EditText) v.findViewById(R.id.editname);
        mPriceEdit = (EditText) v.findViewById(R.id.priceedit);
        mIngredientsEdit = (EditText) v.findViewById(R.id.ingredientsedit);
        mSaveButton = (ImageButton) v.findViewById(R.id.addimbutt);
        mCancButton = (ImageButton) v.findViewById(R.id.cancimbutt);
        mSelectTypeText = (TextView) v.findViewById(R.id.textselecttype);
        mSaleCheck = (CheckBox) v.findViewById(R.id.salecheck);
        mVeganCheck = (CheckBox) v.findViewById(R.id.vegancheck);
        mAvailableCheck = (CheckBox) v.findViewById(R.id.availablecheck);
        mVegetarianCheck = (CheckBox) v.findViewById(R.id.vegetariancheck);
        mGlutenfreeCheck = (CheckBox) v.findViewById(R.id.glutenfreecheck);
        mSaleEdit = (EditText) v.findViewById(R.id.saleedit);

        mFourrelative.setVisibility(View.GONE);
        mFiveRrelative.setVisibility(View.GONE);
        mSixRrelative.setVisibility(View.GONE);
        setListeners();
        if(mItem != null){
            getDialog().setTitle(getResources().getString(R.string.edit));
            loadData();
        }

        return v;
    }

    private void loadData() {
        mNameEdit.setText(mItem.getName());
        mName = mItem.getName();
        mType = mItem.getTypeofItem();
        mPrice = String.valueOf(mItem.getPrice());
        mAvailable = mItem.isAvailable();
        mAvailableCheck.setChecked(mAvailable);
        mPriceEdit.setText(String.valueOf(mItem.getPrice()));
        if(mType == 0){
            mSelectButton = (RadioButton) mSelectType.findViewById(R.id.radiodrink);
        }
        if(mType == 1){
            mSelectButton = (RadioButton) mSelectType.findViewById(R.id.radiofood);
            if(mItem.isVegetarian()){
                mVegetarianCheck.setChecked(true);
                mVegetarian = true;
            }
            else{
                mVegetarianCheck.setChecked(false);
                mVegetarian = false;
            }
            if(mItem.isVegan()){
                mVeganCheck.setChecked(true);
                mVegan = true;
            }
            else{
                mVeganCheck.setChecked(false);
                mVegan = false;
            }
            if(mItem.isGlutenFree()){
                mGlutenfreeCheck.setChecked(true);
                mGlutenfree = true;
            }
            else{
                mGlutenfreeCheck.setChecked(false);
                mGlutenfree = false;
            }
        }
        mSelectButton.setChecked(true);

        if(mItem.getSalePercentage()>0) {
            mSaleCheck.setChecked(true);
            mSaleEdit.setVisibility(View.VISIBLE);
            mSaleEdit.setText(String.valueOf(mItem.getSalePercentage()));
            mPercentage = mItem.getSalePercentage();
        }
        else{
            mSaleCheck.setChecked(false);
            mSaleEdit.setVisibility(View.GONE);
        }
    }

    private void setListeners() {

        mSelectType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                mSelectButton = (RadioButton) group.findViewById(checkedId);
                mSelectTypeText.setError(null);
                if(checkedId == R.id.radiofood){
                    mFourrelative.setVisibility(View.VISIBLE);
                    mFiveRrelative.setVisibility(View.VISIBLE);
                    mSixRrelative.setVisibility(View.VISIBLE);
                    mType = 0;
                }
                if(checkedId == R.id.radiodrink){
                    mFourrelative.setVisibility(View.GONE);
                    mFiveRrelative.setVisibility(View.GONE);
                    mSixRrelative.setVisibility(View.VISIBLE);
                    mType = 1;
                }
            }
        });

        mSaleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSaleEdit.setVisibility(View.VISIBLE);
                } else {
                    mPercentage=0;
                    mSaleEdit.setVisibility(View.GONE);
                }
            }
        });

        mSaleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mSaleEdit.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSaleEdit.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int val = Integer.parseInt(s.toString());
                    if (val > 100) {
                        s.replace(0, s.length(), " ", 0, 1);
                        mSaleEdit.setError(getResources().getString(R.string.saleerror));
                        mSaleEdit.requestFocus();
                    } else if (val < 0) {
                        s.replace(0, s.length(), " ", 0, 1);
                        mSaleEdit.setError(getResources().getString(R.string.saleerror));
                        mSaleEdit.requestFocus();
                    }
                    mPercentage = val;
                } catch (NumberFormatException ex) {
                    s.replace(0, s.length(), " ", 0, 1);
                    mSaleEdit.setError(getResources().getString(R.string.saleerror));
                }

            }
        });

        mAvailableCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAvailable = isChecked;
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForm();
            }
        });

        mCancButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mNameEdit.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNameEdit.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mName = s.toString();
            }
        });

        mPriceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mPriceEdit.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPriceEdit.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mPrice = s.toString();
            }
        });

        mIngredientsEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mIngredients = s.toString();
            }
        });

        mVeganCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mVegan = true;
                } else {
                    mVegan = false;
                }
            }
        });

        mVegetarianCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mVegetarian = true;
                }
                else{
                    mVegetarian = false;
                }
            }
        });

        mGlutenfreeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mGlutenfree = true;
                }
                else {
                    mGlutenfree = false;
                }
            }
        });

    }


    private void checkForm() {

        mFocusView = null;
        if (mNameEdit.getText().toString().isEmpty()) { //name not correct
            mNameEdit.setError(getResources().getString(R.string.nameerror));
            mFocusView = mSelectTypeText;
            mFocusView.requestFocus();
            return;
        }

        if(mType == -1){ // no type select
            mSelectTypeText.setError(getResources().getString(R.string.typeerror));
            mFocusView = mSelectTypeText;
            mFocusView.requestFocus();
            return;
        }

        if (mPriceEdit.getText().toString().isEmpty()) { //name not correct
            mPriceEdit.setError(getResources().getString(R.string.priceerror));
            mFocusView = mSelectTypeText;
            mFocusView.requestFocus();
            return;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

        builder.setMessage(R.string.are_you_sure).setTitle(R.string.save);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(mTag.equals("dialogadditem")) {
                            if (mSelectButton.getId() == R.id.radiofood) {
                                G3MenuItem mFoodObj = new G3MenuItem(-1, mName, Double.valueOf(mPrice), mPercentage, mCat, mIngredients, mVegan, mVegetarian, mGlutenfree, 1, mAvailable);
                                mListener.onSaveItemClick(mFoodObj,"add",-1);
                            }
                            if (mSelectButton.getId() == R.id.radiodrink) {
                                G3MenuItem mDrinkObj = new G3MenuItem(-1, mName, Double.valueOf(mPrice), mPercentage, mCat, 0, mAvailable);
                                mListener.onSaveItemClick(mDrinkObj,"add",-1);
                            }
                        }
                        else{
                            if (mSelectButton.getId() == R.id.radiofood) {
                                G3MenuItem mFoodObj = new G3MenuItem(-1, mName, Double.valueOf(mPrice), mPercentage, mCat, mIngredients, mVegan, mVegetarian, mGlutenfree, 1, mAvailable);
                                mListener.onSaveItemClick(mFoodObj,"edit",mPosition);
                            }
                            if (mSelectButton.getId() == R.id.radiodrink) {
                                G3MenuItem mDrinkObj = new G3MenuItem(-1, mName, Double.valueOf(mPrice), mPercentage, mCat, 0, mAvailable);
                                mListener.onSaveItemClick(mDrinkObj,"edit",mPosition);
                            }
                        }

                        dismiss();

                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog

                    }
                });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
