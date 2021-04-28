package com.polito.group3.ufoodfusion.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.RestaurantFilters;
import com.polito.group3.ufoodfusion.dialogfragments.G3FilterDialogFragment;


/**
 * Created by Alfonso on 28/04/2016.
 */
public class G3CusinesFilterFragment extends Fragment {

    private Context mCtx;
    private View v;
    private RestaurantFilters mFilter;
    private CheckBox mVeganCheck;
    private CheckBox mVegetarianCheck;
    private CheckBox mGlutenfreeCheck;
    private CheckBox mItalianCheck;
    private CheckBox mJapaneseCheck;
    private CheckBox mKebabCheck;
    private CheckBox mPiadineriaCheck;
    private CheckBox mChineseCheck;
    private CheckBox mHamburgerCheck;
    private CheckBox mVegetarianShpCheck;
    private CheckBox mGreekCheck;
    private CheckBox mSudAmericanCheck;
    private CheckBox mFastFoodCheck;
    private CheckBox mBarCheck;
    private CheckBox mPizzeriaCheck;
    private CheckBox mAsianCheck;
    private CheckBox mAfricanCheck;
    private CheckBox mMeatCheck;
    private CheckBox mSeaCheck;
    private CheckBox mFusionCheck;
    private CheckBox mIndianCheck;
    private CheckBox mMediterraneanCheck;
    private CheckBox mOrientalCheck;
    private CheckBox mSandwichCheck;
    private CheckBox mPiedmontCheck;
    private CheckBox mFryCheck;
    private CheckBox mRegionalCheck;


    private LinearLayout mLayVegetarian;
    private LinearLayout mLayVegan;
    private LinearLayout mLayGluten;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCtx = this.getContext();
        v = inflater.inflate(R.layout.fragment_filter_cusines, container, false);
        mFilter = G3FilterDialogFragment.getFilter();
        initView();
        setListeners();

        return v;
    }

    private void initView() {

        mGlutenfreeCheck = (CheckBox) v.findViewById(R.id.cbglutenfree);
        mVeganCheck = (CheckBox) v.findViewById(R.id.cbvegan);
        mVegetarianCheck = (CheckBox) v.findViewById(R.id.cbvegetarian);
        mItalianCheck = (CheckBox) v.findViewById(R.id.cbitalian);
        mJapaneseCheck = (CheckBox) v.findViewById(R.id.cbjapanese);
        mKebabCheck = (CheckBox) v.findViewById(R.id.cbkebab);
        mVegetarianShpCheck = (CheckBox) v.findViewById(R.id.cbvegetarianshop);
        mGreekCheck = (CheckBox) v.findViewById(R.id.cbgreek);
        mHamburgerCheck = (CheckBox) v.findViewById(R.id.cbhamburger);
        mPiadineriaCheck = (CheckBox) v.findViewById(R.id.cbpiadineria);
        mChineseCheck = (CheckBox) v.findViewById(R.id.cbchinese);
        mBarCheck = (CheckBox) v.findViewById(R.id.cbbar);
        mAsianCheck = (CheckBox) v.findViewById(R.id.cbasian);
        mPizzeriaCheck = (CheckBox) v.findViewById(R.id.cbpizzeria);
        mSudAmericanCheck = (CheckBox) v.findViewById(R.id.cbsudamerican);
        mFastFoodCheck = (CheckBox) v.findViewById(R.id.cbfast);
        mAfricanCheck = (CheckBox) v.findViewById(R.id.cbafrican);
        mMeatCheck = (CheckBox) v.findViewById(R.id.cbmeat);
        mSeaCheck  = (CheckBox) v.findViewById(R.id.cbsea);
        mFusionCheck  = (CheckBox) v.findViewById(R.id.cbfusion);
        mIndianCheck = (CheckBox) v.findViewById(R.id.cbindian);
        mMediterraneanCheck = (CheckBox) v.findViewById(R.id.cbmediterranean);
        mOrientalCheck = (CheckBox) v.findViewById(R.id.cboriental);
        mSandwichCheck = (CheckBox) v.findViewById(R.id.cbsandwich);
        mPiedmontCheck = (CheckBox) v.findViewById(R.id.cbpiedmont);
        mFryCheck = (CheckBox) v.findViewById(R.id.cbfry);
        mRegionalCheck  = (CheckBox) v.findViewById(R.id.cbregional);


        mLayVegetarian = (LinearLayout) v.findViewById(R.id.layvegetarian);
        mLayVegan = (LinearLayout) v.findViewById(R.id.layvegan);
        mLayGluten = (LinearLayout) v.findViewById(R.id.laygluten);


        if(mFilter.getVeganFilter()){
            mVeganCheck.setChecked(true);
            ((LinearLayout) mVeganCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
        }
        else{
            mVeganCheck.setChecked(false);
        }
        if(mFilter.getVegetarianFilter()){
            mVegetarianCheck.setChecked(true);
            ((LinearLayout) mVegetarianCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
        }
        else{
            mVegetarianCheck.setChecked(false);
        }
        if(mFilter.getGlutenFreeFilter()){
            mGlutenfreeCheck.setChecked(true);
            ((LinearLayout) mGlutenfreeCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
        }
        else{
            mGlutenfreeCheck.setChecked(false);
        }

        for (Integer i:mFilter.getRestaurantCatFilter()){
            G3RestaurantObj.restaurantCategory temp = G3RestaurantObj.restaurantCategory.getRestaurantCategoryFromInt(i.intValue());
            switch(temp){
                case ASIAN:
                    mAsianCheck.setChecked(true);
                    ((LinearLayout) mAsianCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case AFRICAN:
                    mAfricanCheck.setChecked(true);
                    ((LinearLayout) mAfricanCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case BAR:
                    mBarCheck.setChecked(true);
                    ((LinearLayout) mBarCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case CHINESE:
                    mChineseCheck.setChecked(true);
                    ((LinearLayout) mChineseCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case MEAT_RESTAURANT:
                    mMeatCheck.setChecked(true);
                    ((LinearLayout) mMeatCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case SEA_FOOD:
                    mSeaCheck.setChecked(true);
                    ((LinearLayout) mSeaCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case FAST_FOOD:
                    mFastFoodCheck.setChecked(true);
                    ((LinearLayout) mFastFoodCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case FUSION:
                    mFusionCheck.setChecked(true);
                    ((LinearLayout) mFusionCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case JAPANESE:
                    mJapaneseCheck.setChecked(true);
                    ((LinearLayout) mJapaneseCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case GREEK:
                    mGreekCheck.setChecked(true);
                    ((LinearLayout) mGreekCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case HAMBURGER:
                    mHamburgerCheck.setChecked(true);
                    ((LinearLayout) mHamburgerCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case INDIAN:
                    mIndianCheck.setChecked(true);
                    ((LinearLayout) mIndianCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case ITALIAN:
                    mItalianCheck.setChecked(true);
                    ((LinearLayout) mItalianCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case KEBAB:
                    mKebabCheck.setChecked(true);
                    ((LinearLayout) mKebabCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case MEDITERRANEAN:
                    mMediterraneanCheck.setChecked(true);
                    ((LinearLayout) mMediterraneanCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case ORIENTAL:
                    mOrientalCheck.setChecked(true);
                    ((LinearLayout) mOrientalCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case SANDWICH:
                    mSandwichCheck.setChecked(true);
                    ((LinearLayout) mSandwichCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case PIADINERIA:
                    mPiadineriaCheck.setChecked(true);
                    ((LinearLayout) mPiadineriaCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case PIZZERIA:
                    mPizzeriaCheck.setChecked(true);
                    ((LinearLayout) mPizzeriaCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case PIEDMONT:
                    mPiedmontCheck.setChecked(true);
                    ((LinearLayout) mPiedmontCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case FRY_SHOP:
                    mFryCheck.setChecked(true);
                    ((LinearLayout) mFryCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case SUD_AMERICAN:
                    mSudAmericanCheck.setChecked(true);
                    ((LinearLayout) mSudAmericanCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case VEGETARIAN:
                    mVegetarianShpCheck.setChecked(true);
                    ((LinearLayout) mVegetarianShpCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
                case REGIONAL_KITCHEN:
                    mRegionalCheck.setChecked(true);
                    ((LinearLayout) mRegionalCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                    break;
            }
        }

    }

    private void setListeners() {

        mLayVegan.setOnClickListener(myClickListener);
        mLayVegetarian.setOnClickListener(myClickListener);
        mLayGluten.setOnClickListener(myClickListener);
        mItalianCheck.setOnCheckedChangeListener(myCheckedList);
        mPiadineriaCheck.setOnCheckedChangeListener(myCheckedList);
        mChineseCheck.setOnCheckedChangeListener(myCheckedList);
        mJapaneseCheck.setOnCheckedChangeListener(myCheckedList);
        mKebabCheck.setOnCheckedChangeListener(myCheckedList);
        mBarCheck.setOnCheckedChangeListener(myCheckedList);
        mFastFoodCheck.setOnCheckedChangeListener(myCheckedList);
        mSudAmericanCheck.setOnCheckedChangeListener(myCheckedList);
        mHamburgerCheck.setOnCheckedChangeListener(myCheckedList);
        mVegetarianShpCheck.setOnCheckedChangeListener(myCheckedList);
        mGreekCheck.setOnCheckedChangeListener(myCheckedList);
        mPizzeriaCheck.setOnCheckedChangeListener(myCheckedList);
        mAsianCheck.setOnCheckedChangeListener(myCheckedList);
        mAfricanCheck.setOnCheckedChangeListener(myCheckedList);
        mMeatCheck.setOnCheckedChangeListener(myCheckedList);
        mSeaCheck.setOnCheckedChangeListener(myCheckedList);
        mFusionCheck.setOnCheckedChangeListener(myCheckedList);
        mFryCheck.setOnCheckedChangeListener(myCheckedList);
        mPiedmontCheck.setOnCheckedChangeListener(myCheckedList);
        mIndianCheck.setOnCheckedChangeListener(myCheckedList);
        mMediterraneanCheck.setOnCheckedChangeListener(myCheckedList);
        mOrientalCheck.setOnCheckedChangeListener(myCheckedList);
        mSandwichCheck.setOnCheckedChangeListener(myCheckedList);
        mRegionalCheck.setOnCheckedChangeListener(myCheckedList);





        mGlutenfreeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilter.setGlutenFreeFilter(isChecked);
                if(isChecked)
                    ((LinearLayout) mGlutenfreeCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    ((LinearLayout) mGlutenfreeCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        mVeganCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilter.setVeganFilter(isChecked);
                if(isChecked)
                    ((LinearLayout) mVeganCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    ((LinearLayout) mVeganCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        mVegetarianCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilter.setVegetarianFilter(isChecked);
                if(isChecked)
                    ((LinearLayout) mVegetarianCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                else
                    ((LinearLayout) mVegetarianCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

    }

    CompoundButton.OnCheckedChangeListener myCheckedList = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();

            switch (id){

                case R.id.cbitalian:
                    if(isChecked) {
                        ((LinearLayout) mItalianCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.ITALIAN.getNumber());
                    }else {
                        ((LinearLayout) mItalianCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.ITALIAN.getNumber()));
                    }
                    break;
                case R.id.cbkebab:
                    if(isChecked) {
                        ((LinearLayout) mKebabCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.KEBAB.getNumber());
                    }else {
                        ((LinearLayout) mKebabCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.KEBAB.getNumber()));
                    }
                    break;
                case R.id.cbpiadineria:
                    if(isChecked) {
                        ((LinearLayout) mPiadineriaCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.PIADINERIA.getNumber());
                    }else {
                        ((LinearLayout) mPiadineriaCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.PIADINERIA.getNumber()));
                    }
                    break;
                case R.id.cbjapanese:
                    if(isChecked) {
                        ((LinearLayout) mJapaneseCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.JAPANESE.getNumber());
                    }else {
                        ((LinearLayout) mJapaneseCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.JAPANESE.getNumber()));
                    }
                    break;
                case R.id.cbchinese:
                    if(isChecked) {
                        ((LinearLayout) mChineseCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.CHINESE.getNumber());
                    }else {
                        ((LinearLayout) mChineseCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.CHINESE.getNumber()));
                    }
                    break;
                case R.id.cbbar:
                    if(isChecked) {
                        ((LinearLayout) mBarCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.BAR.getNumber());
                    }else {
                        ((LinearLayout) mBarCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.BAR.getNumber()));
                    }
                    break;
                case R.id.cbhamburger:
                    if(isChecked) {
                        ((LinearLayout) mHamburgerCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.HAMBURGER.getNumber());
                    }else {
                        ((LinearLayout) mHamburgerCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.HAMBURGER.getNumber()));
                    }
                    break;
                case R.id.cbvegetarianshop:
                    if(isChecked) {
                        ((LinearLayout) mVegetarianShpCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.VEGETARIAN.getNumber());
                    }else {
                        ((LinearLayout) mVegetarianShpCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.VEGETARIAN.getNumber()));
                    }
                    break;
                case R.id.cbgreek:
                    if(isChecked) {
                        ((LinearLayout) mGreekCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.GREEK.getNumber());
                    }else {
                        ((LinearLayout) mGreekCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.GREEK.getNumber()));
                    }
                    break;
                case R.id.cbsudamerican:
                    if(isChecked) {
                        ((LinearLayout) mSudAmericanCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.SUD_AMERICAN.getNumber());
                    }else {
                        ((LinearLayout) mSudAmericanCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.SUD_AMERICAN.getNumber()));
                    }
                    break;
                case R.id.cbfast:
                    if(isChecked) {
                        ((LinearLayout) mFastFoodCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.FAST_FOOD.getNumber());
                    }else {
                        ((LinearLayout) mFastFoodCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.FAST_FOOD.getNumber()));
                    }
                    break;
                case R.id.cbasian:
                    if(isChecked) {
                        ((LinearLayout) mAsianCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.ASIAN.getNumber());
                    }else {
                        ((LinearLayout) mAsianCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.ASIAN.getNumber()));
                    }
                    break;
                case R.id.cbpizzeria:
                    if(isChecked) {
                        ((LinearLayout) mPizzeriaCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.PIZZERIA.getNumber());
                    }else {
                        ((LinearLayout) mPizzeriaCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.PIZZERIA.getNumber()));
                    }
                    break;
                case R.id.cbmeat:
                    if(isChecked) {
                        ((LinearLayout) mMeatCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.MEAT_RESTAURANT.getNumber());
                    }else {
                        ((LinearLayout) mMeatCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.MEAT_RESTAURANT.getNumber()));
                    }
                    break;
                case R.id.cbsea:
                    if(isChecked) {
                        ((LinearLayout) mSeaCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.SEA_FOOD.getNumber());
                    }else {
                        ((LinearLayout) mSeaCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.SEA_FOOD.getNumber()));
                    }
                    break;
                case R.id.cbafrican:
                    if(isChecked) {
                        ((LinearLayout) mAfricanCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.AFRICAN.getNumber());
                    }else {
                        ((LinearLayout) mAfricanCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.AFRICAN.getNumber()));
                    }
                    break;
                case R.id.cbfusion:
                    if(isChecked) {
                        ((LinearLayout) mFusionCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.FUSION.getNumber());
                    }else {
                        ((LinearLayout) mFusionCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.FUSION.getNumber()));
                    }
                    break;
                case R.id.cbmediterranean:
                    if(isChecked) {
                        ((LinearLayout) mMediterraneanCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.MEDITERRANEAN.getNumber());
                    }else {
                        ((LinearLayout) mMediterraneanCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.MEDITERRANEAN.getNumber()));
                    }
                    break;
                case R.id.cbindian:
                    if(isChecked) {
                        ((LinearLayout) mIndianCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.INDIAN.getNumber());
                    }else {
                        ((LinearLayout) mMediterraneanCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.INDIAN.getNumber()));
                    }
                    break;
                case R.id.cboriental:
                    if(isChecked) {
                        ((LinearLayout) mOrientalCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.ORIENTAL.getNumber());
                    }else {
                        ((LinearLayout) mOrientalCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.ORIENTAL.getNumber()));
                    }
                    break;
                case R.id.cbsandwich:
                    if(isChecked) {
                        ((LinearLayout) mSandwichCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.SANDWICH.getNumber());
                    }else {
                        ((LinearLayout) mSandwichCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.SANDWICH.getNumber()));
                    }
                    break;
                case R.id.cbpiedmont:
                    if(isChecked) {
                        ((LinearLayout) mPiedmontCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.PIEDMONT.getNumber());
                    }else {
                        ((LinearLayout) mPiedmontCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.PIEDMONT.getNumber()));
                    }
                    break;
                case R.id.cbfry:
                    if(isChecked) {
                        ((LinearLayout) mFryCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.FRY_SHOP.getNumber());
                    }else {
                        ((LinearLayout) mFryCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.FRY_SHOP.getNumber()));
                    }
                    break;
                case R.id.cbregional:
                    if(isChecked) {
                        ((LinearLayout) mRegionalCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.primary_light));
                        mFilter.getRestaurantCatFilter().add(G3RestaurantObj.restaurantCategory.REGIONAL_KITCHEN.getNumber());
                    }else {
                        ((LinearLayout) mRegionalCheck.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
                        mFilter.getRestaurantCatFilter().remove(Integer.valueOf(G3RestaurantObj.restaurantCategory.REGIONAL_KITCHEN.getNumber()));
                    }
                    break;
            }
        }
    };

    View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int id = v.getId();

            switch(id){

                case R.id.layvegetarian:
                    mVegetarianCheck.setChecked(!mVegetarianCheck.isChecked());
                    break;
                case R.id.laygluten:
                    mGlutenfreeCheck.setChecked(!mGlutenfreeCheck.isChecked());
                    break;
                case R.id.layvegan:
                    mVeganCheck.setChecked(!mVeganCheck.isChecked());
                    break;

            }

        }
    };
}
