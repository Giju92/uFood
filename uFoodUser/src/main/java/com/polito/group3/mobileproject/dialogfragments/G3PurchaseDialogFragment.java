package com.polito.group3.mobileproject.dialogfragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3MenuItem;
import com.example.ufoodlibrary.objects.G3OrderItem;
import com.polito.group3.mobileproject.activities.G3UserRestaurantActivity;


/**
 * Created by Alfonso-LAPTOP on 09/05/2016.
 */
public class G3PurchaseDialogFragment extends DialogFragment {

    private static G3MenuItem mItemMenu;
    private  View v;
    private TextView mNameDish;
    private TextView mPriceDish;
    private TextView mPriceDiscountedDish;
    private TextView mIngrDish;
    private TextView mPercentageDish;
    private Button mAddButton;
    private ImageButton mMenoQnty;
    private ImageButton mPlusQnty;
    private EditText mEditQnty;
    private int quantity = 1;
    private onAddItemToOrder mListener;

   public interface onAddItemToOrder{
       void addItemToOrder(G3OrderItem orderitem);
   }

    public static G3PurchaseDialogFragment newInstance(G3MenuItem item){
        G3PurchaseDialogFragment fragment = new G3PurchaseDialogFragment();
        mItemMenu = item;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (onAddItemToOrder)activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.dialog_fragment_purchase, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        setCancelable(true);

        initView();
        initData();
        setListeners();

        return v;

    }

    private void initView() {

        mNameDish = (TextView) v.findViewById(R.id.dishnametext);
        mPriceDish = (TextView) v.findViewById(R.id.dishpricetext);
        mPriceDiscountedDish = (TextView) v.findViewById(R.id.dishpricediscountedtext);
        mIngrDish = (TextView) v.findViewById(R.id.dishingredientstext);
        mAddButton = (Button) v.findViewById(R.id.addtocart);
        mMenoQnty = (ImageButton) v.findViewById(R.id.menoquant);
        mPlusQnty = (ImageButton) v.findViewById(R.id.plusquant);
        mEditQnty = (EditText) v.findViewById(R.id.editquantity);
        mEditQnty.setKeyListener(null);
        mPercentageDish = (TextView) v.findViewById(R.id.dishofferttext);
    }

    private void initData() {

        if(mItemMenu != null) {

            mNameDish.setText(mItemMenu.getName());
            if(mItemMenu.getIngredients()==null){
                mIngrDish.setVisibility(View.GONE);
            } else{
                mIngrDish.setVisibility(View.VISIBLE);
                mIngrDish.setText(mItemMenu.getIngredients());
            }
            mPriceDish.setText(String.format("%.2f", mItemMenu.getPrice())+" "+getResources().getString(R.string.euro_symbol));
            if(mItemMenu.getSalePercentage()>0){
                mPercentageDish.setText(String.valueOf(mItemMenu.getSalePercentage())+getResources().getString(R.string.percentage_symbol));
                mPriceDish.setPaintFlags(mPriceDish.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mPriceDish.setTextColor(getResources().getColor(R.color.primary));
                mPriceDiscountedDish.setVisibility(View.VISIBLE);
                mPriceDiscountedDish.setText(String.format( "%.2f",  mItemMenu.getPrice()-(mItemMenu.getPrice()*mItemMenu.getSalePercentage()/100 )) + " €" );

            }
            else{
                mPercentageDish.setVisibility(View.GONE);
            }
        }


    }

    private void setListeners() {

        mPlusQnty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                mEditQnty.setText(String.valueOf(quantity));
                mPriceDish.setText(String.format("%.2f", mItemMenu.getPrice()*quantity)+" "+getResources().getString(R.string.euro_symbol));
                if(mItemMenu.getSalePercentage()>0){
                    mPriceDiscountedDish.setText(String.format( "%.2f",  mItemMenu.getPrice()*quantity-(mItemMenu.getPrice()*mItemMenu.getSalePercentage()*quantity/100)) + " €");
                }
            }
        });

        mMenoQnty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity>1) {
                    quantity--;
                    mEditQnty.setText(String.valueOf(quantity));
                    mPriceDish.setText(String.format("%.2f", mItemMenu.getPrice()*quantity)+" "+getResources().getString(R.string.euro_symbol));
                    if(mItemMenu.getSalePercentage()>0){
                        mPriceDiscountedDish.setText(String.format( "%.2f",  mItemMenu.getPrice()*quantity-(mItemMenu.getPrice()*mItemMenu.getSalePercentage()*quantity/100)) + " €");
                    }
                }
            }
        });

        mEditQnty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().isEmpty()) {
                    quantity = Integer.valueOf(s.toString());
                    if (quantity < 1) {
                        quantity = 1;
                        mEditQnty.setText(String.valueOf(quantity));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                G3OrderItem item = new G3OrderItem(1,mItemMenu,quantity);
                mListener.addItemToOrder(item);
                ((G3UserRestaurantActivity)getActivity()).setOptionIcon(R.id.basket_action, R.drawable.fullcart);
                ((G3UserRestaurantActivity)getActivity()).modifyBottomToolbar(item);
                dismiss();
            }
        });

    }

}
