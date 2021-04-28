package com.polito.group3.ufoodfusion.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3DailyMenu;
import com.example.ufoodlibrary.objects.G3MenuItem;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.ItemDecoration;
import com.example.ufoodlibrary.utilities.TransitionHelper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.polito.group3.ufoodfusion.adapters.G3MenuRestourantCategoryAdapter;
import com.polito.group3.ufoodfusion.dialogfragments.G3AddItemMenuDialogFragment;
import com.polito.group3.ufoodfusion.fragments.G3MenuFragment;

import java.util.ArrayList;


/**
 * Created by Alfonso on 06/04/2016.
 */
public class G3MenuRestourantActivity extends G3BaseActivity implements G3AddItemMenuDialogFragment.onSaveItemListener {

    private Toolbar toolbar;
    private TextView mTutorial;
    private Intent mIntent;
    private String mCat;
    private RecyclerView mRecycler;
    private G3AddItemMenuDialogFragment mDialogFrag;
    private LinearLayoutManager mLayoutManager;
    private G3MenuRestourantCategoryAdapter mAdapter;
    private G3RestaurantObj mRestaurant;
    private ArrayList<G3MenuItem> mDataSet;
    private ArrayList<G3MenuItem> mCompleteDataSet;
    private G3DailyMenu menu;
    private ProgressDialog progressD;
    private boolean dailymenu = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_restourant_category_select);

        mIntent = getIntent();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mCat = mIntent.getStringExtra(G3MenuFragment.CATEGORIE);
        mRestaurant = mIntent.getParcelableExtra(G3MenuFragment.RESTAURANT);
        mDataSet = new ArrayList<>();
        mCompleteDataSet = new ArrayList<>();
        setSupportActionBar(toolbar);
        if(toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mCat);
        }
        if(mCat.equals(getResources().getString(R.string.daily_menu))){
            dailymenu=true;
        }

        loadData();
        mTutorial = (TextView) findViewById(R.id.tutorialtext);
        mRecycler = (RecyclerView) findViewById(R.id.menucatresrecycler);
        setRecycler();

        mTutorial.setText(R.string.tutorial_add_item);


    }

    private void loadData() {

        if(mCat.equals(getResources().getString(R.string.daily_menu))){
            menu = mRestaurant.getmDailymenu();
            if(menu != null){
                for (G3MenuItem itemMenu : menu.getMenuitems()) {
                    mDataSet.add(itemMenu);
                }
                if(mRestaurant.getMenu().getMenuItems() != null) {
                    for (G3MenuItem itemMenu : mRestaurant.getMenu().getMenuItems()) {

                        mCompleteDataSet.add(itemMenu);
                    }
                }
            }
            return;
        }

        if(mRestaurant.getMenu().getMenuItems() != null) {
            mDataSet = new ArrayList<>();
            mCompleteDataSet = new ArrayList<>();
            for (G3MenuItem itemMenu : mRestaurant.getMenu().getMenuItems()) {
                if(itemMenu.getCategory().equalsIgnoreCase(mCat)) {
                    mDataSet.add(itemMenu);
                }
                else{
                    mCompleteDataSet.add(itemMenu);
                }
            }
        }

    }

    private void setRecycler() {

        mRecycler.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayoutManager);

        ItemDecoration idec = new ItemDecoration(this);
        mRecycler.addItemDecoration(idec);

        mAdapter = new G3MenuRestourantCategoryAdapter(mDataSet);
        mRecycler.setAdapter(mAdapter);

        if(mDataSet.isEmpty()){
            mTutorial.setVisibility(View.VISIBLE);
            mRecycler.setVisibility(View.GONE);
            mTutorial.setText(getResources().getString(R.string.tutorial_add_item));
        }

        mAdapter.setOnItemClickListener(new G3MenuRestourantCategoryAdapter.onItemCategoryClickListener() {

            @Override
            public void onDeleteItemClick(int position) {


                saveRestaurant();

            }

            @Override
            public void onEditItemClick(int position) {
                mDialogFrag = G3AddItemMenuDialogFragment.newInstance(mCat, mDataSet.get(position), position);
                mDialogFrag.setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
                mDialogFrag.show(getSupportFragmentManager(), "dialogedititem");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_restourant_fragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.addCategory:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {

        mDialogFrag = G3AddItemMenuDialogFragment.newInstance(mCat, null,-1);
        mDialogFrag.setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
        mDialogFrag.show(getSupportFragmentManager(), "dialogadditem");

    }

    @Override
    public void onSaveItemClick(G3MenuItem mItemMenu,String mode, int position) {


        if(mode.equals("add")){
            mDataSet.add(mItemMenu);
            mTutorial.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
            saveRestaurant();
        }
        if(mode.equals("edit")){
            mDataSet.remove(position);
            mDataSet.add(position,mItemMenu);
            mAdapter.notifyDataSetChanged();
            saveRestaurant();
        }
        if(mode.equals("addmenu")){
            mDataSet.add(mItemMenu);
            mTutorial.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
            try {
                G3Application.fManager.addNotifyDaily(mRestaurant.getName());
            } catch (NetworkDownException e) {

            }
            saveRestaurant();
        }
        if(mode.equals("editmenu")){
            mDataSet.remove(position);
            mDataSet.add(position,mItemMenu);
            mAdapter.notifyDataSetChanged();
            saveRestaurant();
        }


    }

    private void saveRestaurant() {
        ArrayList<G3MenuItem> toInsert = new ArrayList<>();
        if(!dailymenu){
            for(G3MenuItem it: mDataSet){
                toInsert.add(it);
            }
            for(G3MenuItem it: mCompleteDataSet){
                toInsert.add(it);
            }
            mRestaurant.getMenu().setMenuItems(toInsert);
        }
        else{
            G3DailyMenu menu = new G3DailyMenu(mRestaurant.getName());
            for(G3MenuItem it: mDataSet){
                menu.addItemToMenu(it);
            }
            mRestaurant.setmDailymenu(menu);
        }

        progressD = TransitionHelper.getProgress(this);
        progressD.show();
        try {
            G3Application.fManager.saveRestaurantProfile(mRestaurant, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.successprogress), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorprogress), Toast.LENGTH_SHORT).show();

                    }
                    if (progressD.isShowing())
                        progressD.dismiss();
                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            if (progressD.isShowing())
                progressD.dismiss();
        }


    }


}
