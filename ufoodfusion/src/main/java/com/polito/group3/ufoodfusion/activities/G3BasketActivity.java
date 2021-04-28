package com.polito.group3.ufoodfusion.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.objects.G3OpeningHoursObj;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.JsonKey;
import com.polito.group3.ufoodfusion.fragments.G3OrderCheckoutFragment;
import com.polito.group3.ufoodfusion.fragments.G3OrderSummaryFragment;

import org.joda.time.DateTime;


/**
 * Created by Alfonso on 03/05/2016.
 */
public class G3BasketActivity extends G3BaseActivity {

    private G3OrderObj mOrder;
    private String mResName;
    private Boolean mAllowsTableBooking;
    private int mServiceTime;
    private G3OpeningHoursObj mLunchTime, mDinnerTime;
    private G3RestaurantObj mRestaurant;
    private TextView mOrderTotalItems;
    private TextView mRestaurantName;

    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private boolean change = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.basket_user_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_without_logo);
        setSupportActionBar(toolbar);

        if(toolbar != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.basket);
        }

        initView();
        loadData();

    }

    private void initView() {
        mRestaurantName = (TextView) findViewById(R.id.resname);
        mOrderTotalItems = (TextView) findViewById(R.id.ordertotalitem);
        mFragment = new G3OrderSummaryFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.replace(R.id.nestedfragment, mFragment, "USER_ORDER_SUMMARY").commit();
    }

    private String getDayOfTheWeekString() {
        DateTime now = new DateTime();
        return G3OpeningHoursObj.Day.getDayFromInt(now.getDayOfWeek() - 1 ).getString();
    }

    private void loadData() {

        mRestaurant = G3UserRestaurantActivity.getRestautant();
        mOrder = G3UserRestaurantActivity.getOrder();
        if(mRestaurant != null) {
            mResName = mRestaurant.getName();
            mAllowsTableBooking = mRestaurant.isAllowTableBooking();
            mServiceTime = mRestaurant.getServiceTime();
        }
        String dayOfTheWeek = getDayOfTheWeekString();
        if (mRestaurant.getOpeningHours().containsKey(dayOfTheWeek + JsonKey.LUNCHTAG))
            mLunchTime = mRestaurant.getOpeningHours().get(dayOfTheWeek + JsonKey.LUNCHTAG);
        if (mRestaurant.getOpeningHours().containsKey(dayOfTheWeek + JsonKey.DINNERTAG))
            mDinnerTime = mRestaurant.getOpeningHours().get(dayOfTheWeek + JsonKey.DINNERTAG);


        mOrderTotalItems.setText(getString(R.string.order_total) + ": " + mOrder.getOrderItems().size());
        mRestaurantName.setText(mResName);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(change){
            Toast.makeText(getApplicationContext(),R.string.confirmBefore,Toast.LENGTH_LONG).show();
            return;
        }
        G3OrderSummaryFragment myFragment = (G3OrderSummaryFragment) getSupportFragmentManager().findFragmentByTag("USER_ORDER_SUMMARY");
        if (myFragment != null && myFragment.isVisible()) {
//            Intent intent = new Intent();
//            intent.putExtra(JsonKey.ORDERITEMS,mOrder.toJsonObj().toString());
//            setResult(RESULT_OK, intent);
            finish();
        }
        G3OrderCheckoutFragment myFragment2 = (G3OrderCheckoutFragment) getSupportFragmentManager().findFragmentByTag("USER_CHECKOUT_SUMMARY");
        if (myFragment2 != null && myFragment2.isVisible()) {
            getSupportFragmentManager().popBackStack();
        }


    }

    public boolean getAllowsTableBooking(){
        return mAllowsTableBooking;
    }
    public G3OpeningHoursObj getLunchTime() { return mLunchTime; }
    public G3OpeningHoursObj getDinnerTime() { return mDinnerTime; }
    public int getServiceTime() { return mServiceTime; }
    public void setChange(boolean change){
        this.change=change;
    }

}
