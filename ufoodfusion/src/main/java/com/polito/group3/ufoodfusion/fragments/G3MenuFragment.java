package com.polito.group3.ufoodfusion.fragments;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3DailyMenu;
import com.example.ufoodlibrary.objects.G3MenuItemCategory;
import com.example.ufoodlibrary.objects.G3MenuObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.ItemDecoration;
import com.example.ufoodlibrary.utilities.TransitionHelper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.ufoodfusion.activities.G3MenuRestourantActivity;
import com.polito.group3.ufoodfusion.activities.G3TabsActivity;
import com.polito.group3.ufoodfusion.adapters.G3MenuRestourantAdapter;
import com.polito.group3.ufoodfusion.dialogfragments.G3AddCategoryDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Alfonso on 02-Apr-16.
 */
public class G3MenuFragment extends Fragment implements G3FragmentLifecycle {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private G3MenuRestourantAdapter mAdapter;
    private Context mCtx;
    private Toolbar toolbar;
    private G3AddCategoryDialogFragment newFragment;
    private ArrayList<String>  mdataSet;
    private SharedPreferences mShared;
    private View v;
    private TextView tutorial;
    private G3RestaurantObj mRestaurant;
    static final String CATEGORIES = "Categories";
    public static final String CATEGORIE = "categorie";
    public static final String POSITION = "position";
    public static final String RESTAURANT = "restaurant";
    private ProgressDialog progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mCtx = this.getContext();

        mdataSet = new ArrayList<>();
        mRestaurant = G3TabsActivity.getmRestaurant();


        v = inflater.inflate(R.layout.menu_restourant_fragment, container, false);

        tutorial = (TextView) v.findViewById(R.id.tutorialtext);
        toolbar =  (Toolbar) this.getActivity().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_restourant_fragment);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        if(toolbar != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(mRestaurant != null){
            if (mRestaurant.getMenu() != null) {
                HashMap<String,G3MenuItemCategory> mCategories = mRestaurant.getMenu().getCategories();
                ArrayList<String> tempDataSet = new ArrayList<>();
                if(mCategories != null) {

                    for(int i=0; i<mCategories.size();i++){
                        tempDataSet.add("");
                    }
                    for (G3MenuItemCategory temp : mCategories.values()) {
                        tempDataSet.add(temp.getPriority(),temp.getName());
                    }
                    if(mRestaurant.getmDailymenu() != null){
                        mdataSet.add(getResources().getString(R.string.daily_menu));
                    }
                    for(int i=0;i<tempDataSet.size();i++){
                        if(!tempDataSet.get(i).equals("")){
                            mdataSet.add(tempDataSet.get(i));
                        }
                    }
                }

            }
        }




        if (!mdataSet.isEmpty()){

            tutorial.setVisibility(View.GONE);
        }

        initData();

        return v;
    }

    private void loadRestaurant(){
        progress = TransitionHelper.getProgress(getActivity());
        progress.show();
        try {
            G3Application.fManager.getRestaurantProfile(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mRestaurant = dataSnapshot.getValue(G3RestaurantObj.class);
                    progress.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progress.dismiss();

                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }
    }

    private void initData() {

        mRecyclerView = (RecyclerView) v.findViewById(R.id.menuresrecycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ItemDecoration idec = new ItemDecoration(this.getContext());
        mRecyclerView.addItemDecoration(idec);

        mAdapter = new G3MenuRestourantAdapter(mdataSet);
        mRecyclerView.setAdapter(mAdapter);

        if(mdataSet.isEmpty()){
            tutorial.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            tutorial.setText(getResources().getString(R.string.tutorial_add_category));
        }

        mAdapter.setOnItemClickListener(new G3MenuRestourantAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent intent = new Intent(getContext(), G3MenuRestourantActivity.class);
                intent.putExtra(CATEGORIE, ((TextView) v).getText());
                intent.putExtra(POSITION, position);
                intent.putExtra(RESTAURANT, mRestaurant);
                getContext().startActivity(intent);
            }

            @Override
            public void onDeleteItem(View v, int position) {
                if (mRestaurant.getmDailymenu() != null && position == 0) {
                    mRestaurant.setmDailymenu(null);
                }
                saveRestaurant();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addCategory:
                showDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this.getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {

        newFragment = G3AddCategoryDialogFragment.newInstance(mdataSet, mRestaurant);
        newFragment.setTargetFragment(this,1);
        newFragment.setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
        newFragment.show(getFragmentManager(), "dialogaddcat");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_restourant_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public void onResume() {
        super.onResume();
        loadRestaurant();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Stuff to do, dependent on requestCode and resultCode
        if(requestCode == 1)  // 1 is an arbitrary number, can be any int
        {
            // This is the return result of your DialogFragment
            if(resultCode == 1) // 1 is an arbitrary number, can be any int
            {
                if (!mdataSet.contains(data.getStringExtra("cat"))){
                    mdataSet.add(data.getStringExtra("cat"));
                    tutorial.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                    saveRestaurant();
                }
            }
            if(resultCode == 2) // 1 is an arbitrary number, can be any int
            {
                    mRestaurant.setmDailymenu(new G3DailyMenu(mRestaurant.getName()));
                    mdataSet.add(0,getResources().getString(R.string.daily_menu));
                    tutorial.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();

                    saveRestaurant();
            }
        }
    }

    private void saveRestaurant() {
        HashMap<String,G3MenuItemCategory> categories = new HashMap<>();
        int i=0;
        for(String s: mdataSet){
            if(s!= null) {
                if(!s.equals(getResources().getString(R.string.daily_menu))){
                    G3MenuItemCategory temp = new G3MenuItemCategory(i, s);
                    i++;
                    categories.put(s, temp);
                }

            }
        }

        if(mRestaurant.getMenu() != null) {
            mRestaurant.getMenu().setCategories(categories);

        }
        else{
            mRestaurant.setMenu(new G3MenuObj(-1));
            mRestaurant.getMenu().setCategories(categories);
        }
        try {
            G3Application.fManager.saveRestaurantProfile(mRestaurant, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    Toast.makeText(getContext(),getResources().getString(R.string.successprogress),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

}
