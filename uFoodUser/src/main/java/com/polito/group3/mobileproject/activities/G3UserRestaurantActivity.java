package com.polito.group3.mobileproject.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3OpeningHoursObj;
import com.example.ufoodlibrary.objects.G3OrderItem;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.BitmapUtils;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.JsonKey;
import com.example.ufoodlibrary.utilities.RestaurantFilters;
import com.google.android.gms.maps.model.LatLng;
import com.example.ufoodlibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.mobileproject.dialogfragments.G3PurchaseDialogFragment;
import com.polito.group3.mobileproject.fragments.G3MenuUserRestaurantFragment;
import com.polito.group3.mobileproject.fragments.G3UserRestaurantInfoFragment;
import com.polito.group3.mobileproject.fragments.G3UserReviewListFragment;
import com.squareup.picasso.Picasso;


import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Mattia on 20/04/2016.
 */
public class G3UserRestaurantActivity extends G3BaseActivity implements G3PurchaseDialogFragment.onAddItemToOrder {

    private Context mCtx;

    private TextView mRestaurantNameText;
    private TextView mRestaurantCategoryText;

    private TextView mAddress;
    private TextView mStarRatinglabel;
    private TextView mPriceRatingLabel;
    private TextView mTimelunch;
    private TextView mTimedinner;
    private TextView mDistance;
    private TextView mWaitingtime;
    private TextView mCallLabel;
    private LinearLayout mInfoButton;
    private LinearLayout mReviewsButton;
    private LinearLayout mMenuButton;
    private LinearLayout mCategoryBar;
    private CoordinatorLayout mChartToolbar;
    private TextView mNOfElementsLabel;
    private TextView mPriceLabel;
    private TextView mGoToChartLabel;
//    private ImageView mFavouriteStar;
    private FloatingActionButton mFavouriteStar;
    private ImageView mToolbarPic;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private TextView mNestedFragmentLabel;
    private TextView mMenuTextView;
    private TextView mReviewTextView;
    private TextView mInfoTextView;

    private Menu menu;
    AppBarLayout appBarLayout;
    private ImageView mAppLogo;

    private static G3RestaurantObj mRestaurantObj;
    private static G3OrderObj mOrder;
    private boolean favourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mCtx = getApplicationContext();

        setContentView(R.layout.activity_user);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.collapse_toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadView();
        loadData();
        setListeners();
    }

    private void setListeners() {

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange() - 40) {
                    mAppLogo.setVisibility(View.VISIBLE);
                    mFavouriteStar.setVisibility(View.GONE);
                } else {
                    // Not collapsed
                    mAppLogo.setVisibility(View.INVISIBLE);
                    mFavouriteStar.setVisibility(View.VISIBLE);
                }
            }
        });

        mInfoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mFragment = new G3UserRestaurantInfoFragment();
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.nestedfragment, mFragment, "USER_RESAURANT_INFO");
                mFragmentTransaction.commit();
            }
        });

        mReviewsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                if(mRestaurantObj.getNumOfReviews()>0) {
                mFragment = new G3UserReviewListFragment();
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.nestedfragment, mFragment, "REVIEWS");
                mFragmentTransaction.commit();
//                }
            }
        });

        mMenuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mFragment = new G3MenuUserRestaurantFragment();
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.nestedfragment, mFragment, "MENU");
                mFragmentTransaction.commit();
            }
        });

        mCallLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String phone_no = mCallLabel.getText().toString().replaceAll("-", "");
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phone_no));
                startActivity(callIntent);
            }
        });

        mGoToChartLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, G3BasketActivity.class);
                startActivity(intent);
            }
        });

        mFavouriteStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(G3Application.fManager.isSignedin()){
                    if(favourite){
                        try {
                            G3Application.fManager.removeFavourite(mRestaurantObj.getId());
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.successprogress),Toast.LENGTH_SHORT).show();
                        } catch (NotAuthenticatedException | NetworkDownException e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        try {
                            G3Application.fManager.addFavouriteRestaurant(mRestaurantObj.getId());
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.successprogress),Toast.LENGTH_SHORT).show();
                        } catch (NotAuthenticatedException | NetworkDownException e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.authentication_rquired),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadView() {
        mRestaurantNameText = (TextView) findViewById(R.id.name_restaurant);
        mRestaurantCategoryText = (TextView) findViewById(R.id.category_restaurant);
        mChartToolbar = (CoordinatorLayout) findViewById(R.id.chart_toolbar);
//        mFavouriteStar = (ImageView) findViewById(R.id.favouriteImage);
        mFavouriteStar = (FloatingActionButton) findViewById(R.id.favourite);
        mGoToChartLabel = (TextView) findViewById(R.id.gotochartlabel);
        mNOfElementsLabel = (TextView) findViewById(R.id.nofelementslabel);
        mPriceLabel = (TextView) findViewById(R.id.pricelabel);
        mToolbarPic = (ImageView) findViewById(R.id.toolbarpic);
        mAppLogo = (ImageView) findViewById(R.id.applogo);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mAddress = (TextView) findViewById(R.id.placelabel);
        mCallLabel = (TextView) findViewById(R.id.calllabel);
        mStarRatinglabel = (TextView) findViewById(R.id.starratinglabel);
        mPriceRatingLabel = (TextView) findViewById(R.id.priceratinglabel);
        mTimelunch = (TextView) findViewById(R.id.hourslabellunch);
        mTimedinner = (TextView) findViewById(R.id.hourslabeldinner);
        mDistance = (TextView) findViewById(R.id.distancelabel);
        mWaitingtime = (TextView) findViewById(R.id.waiting_time);
        mInfoButton = (LinearLayout) findViewById(R.id.infobutton);
        mReviewsButton = (LinearLayout) findViewById(R.id.reviewsbutton);
        mMenuButton = (LinearLayout) findViewById(R.id.menubutton);
        mMenuTextView = (TextView) findViewById(R.id.menutextview);
        mReviewTextView = (TextView) findViewById(R.id.reviewtextview);
        mInfoTextView = (TextView) findViewById(R.id.infotextview);
        mCategoryBar = (LinearLayout) findViewById(R.id.categorybar);
        mNestedFragmentLabel = (TextView) findViewById(R.id.nestedfragmentlabel);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mOrder != null){
            if(mOrder.getOrderItems() != null) {
                int nofelements = 0;
                if (mOrder.getOrderItems().size() == 0) {
                    setOptionIcon(R.id.basket_action, R.drawable.emptycart);
                    mNOfElementsLabel.setText(Integer.toString(0));
                    mChartToolbar.setVisibility(View.GONE);
                } else {
                    setOptionIcon(R.id.basket_action, R.drawable.fullcart);
                    mChartToolbar.setVisibility(View.VISIBLE);
                    for(G3OrderItem item : mOrder.getOrderItems()){
                        nofelements += item.getQuantity();
                    }
                    mNOfElementsLabel.setText(Integer.toString(nofelements));
                    mPriceLabel.setText(String.format("%.2f", mOrder.getTotalOrderPrice())+ " €");
                }
            }
        }
    }



    private void loadData() {

        Intent intent = getIntent();

        mRestaurantObj = intent.getParcelableExtra(G3UserSearchRestaurant.RESTAUANTOBJ);

        Calendar calendar = Calendar.getInstance();

        mRestaurantNameText.setText(mRestaurantObj.getName());
        mRestaurantCategoryText.setText("(" + mRestaurantObj.getCategoryAsEnum().getString() + ")");

        DecimalFormat df2 = new DecimalFormat(".##");

        mAddress.setText(mRestaurantObj.getAddressObj().getShortAddress());

        if (G3UserSearchRestaurant.getCurrentPos() != null) {
            double dist = RestaurantFilters.calculateDistance(G3UserSearchRestaurant.getCurrentPos(), new LatLng(mRestaurantObj.getAddressObj().getLatitude(), mRestaurantObj.getAddressObj().getLongitude()));
            if (dist >= 1) {
                mDistance.setText("(" + df2.format(dist) + " Km)");
            } else {
                mDistance.setText("(" + Math.round(dist * 1000) + " m)");
            }
        } else {
            mDistance.setVisibility(View.INVISIBLE);
        }

        String url  = mRestaurantObj.getPhotoPath();
        if(url != null && url.length()>0)
        {
            Picasso.with(G3Application.getAppContext()).load(url).placeholder(R.drawable.default_restaurant)
                    .into(mToolbarPic);
        }else {
            Picasso.with(G3Application.getAppContext()).load(R.drawable.default_restaurant)
                    .into(mToolbarPic);
        }

        HashMap<String, G3OpeningHoursObj> openinghours = mRestaurantObj.getOpeningHours();

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:

                if (openinghours.containsKey(G3OpeningHoursObj.MONDAY_LUNCH)) {
                    mTimelunch.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.MONDAY_LUNCH).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.MONDAY_LUNCH).getTo()));

                } else {
                    mTimelunch.setText("");
                }

                if (openinghours.containsKey(G3OpeningHoursObj.MONDAY_DINNER)) {
                    mTimedinner.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.MONDAY_DINNER).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.MONDAY_DINNER).getTo()));

                } else {
                    mTimedinner.setText("");
                }
                break;

            case Calendar.TUESDAY:

                if (openinghours.containsKey(G3OpeningHoursObj.TUESDAY_LUNCH)) {
                    mTimelunch.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.TUESDAY_LUNCH).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.TUESDAY_LUNCH).getTo()));
                } else {
                    mTimelunch.setText("");
                }

                if (openinghours.containsKey(G3OpeningHoursObj.TUESDAY_DINNER)) {
                    mTimedinner.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.TUESDAY_DINNER).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.TUESDAY_DINNER).getTo()));
                } else {
                    mTimedinner.setText("");
                }
                break;

            case Calendar.WEDNESDAY:

                if (openinghours.containsKey(G3OpeningHoursObj.WEDNESDAY_LUNCH)) {
                    mTimelunch.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.WEDNESDAY_LUNCH).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.WEDNESDAY_LUNCH).getTo()));
                } else {
                    mTimelunch.setText("");
                }

                if (openinghours.containsKey(G3OpeningHoursObj.WEDNESDAY_DINNER)) {
                    mTimedinner.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.WEDNESDAY_DINNER).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.WEDNESDAY_DINNER).getTo()));
                } else {
                    mTimedinner.setText("");
                }
                break;

            case Calendar.THURSDAY:

                if (openinghours.containsKey(G3OpeningHoursObj.THURSDAY_LUNCH)) {
                    mTimelunch.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.THURSDAY_LUNCH).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.THURSDAY_LUNCH).getTo()));
                } else {
                    mTimelunch.setText("");
                }

                if (openinghours.containsKey(G3OpeningHoursObj.THURSDAY_DINNER)) {
                    mTimedinner.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.THURSDAY_DINNER).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.THURSDAY_DINNER).getTo()));
                } else {
                    mTimedinner.setText("");
                }
                break;

            case Calendar.FRIDAY:

                if (openinghours.containsKey(G3OpeningHoursObj.FRIDAY_LUNCH)) {
                    mTimelunch.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.FRIDAY_LUNCH).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.FRIDAY_LUNCH).getTo()));
                } else {
                    mTimelunch.setText("");
                }

                if (openinghours.containsKey(G3OpeningHoursObj.FRIDAY_DINNER)) {
                    mTimedinner.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.FRIDAY_DINNER).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.FRIDAY_DINNER).getTo()));
                } else {
                    mTimedinner.setText("");
                }
                break;

            case Calendar.SATURDAY:

                if (openinghours.containsKey(G3OpeningHoursObj.SATURDAY_LUNCH)) {
                    mTimelunch.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.SATURDAY_LUNCH).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.SATURDAY_LUNCH).getTo()));
                } else {
                    mTimelunch.setText("");
                }

                if (openinghours.containsKey(G3OpeningHoursObj.SATURDAY_DINNER)) {
                    mTimedinner.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.SATURDAY_DINNER).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.SATURDAY_DINNER).getTo()));
                } else {
                    mTimedinner.setText("");
                }
                break;

            case Calendar.SUNDAY:

                if (openinghours.containsKey(G3OpeningHoursObj.SUNDAY_LUNCH)) {
                    mTimelunch.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.SUNDAY_LUNCH).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.SUNDAY_LUNCH).getTo()));
                } else {
                    mTimelunch.setText("");
                }

                if (openinghours.containsKey(G3OpeningHoursObj.SUNDAY_DINNER)) {
                    mTimedinner.setText(getformattedhours(openinghours.get(G3OpeningHoursObj.SUNDAY_DINNER).getFrom()) + " - " + getformattedhours(openinghours.get(G3OpeningHoursObj.SUNDAY_DINNER).getTo()));
                } else {
                    mTimedinner.setText("");
                }
                break;

            default:
                break;

        }

        if (mRestaurantObj.getNumOfReviews() != 0) {
//            if(mRestaurantObj.getReviewsAvgScore()%1==0.5) {
                DecimalFormat f = new DecimalFormat("0.0");
                mStarRatinglabel.setText(f.format(mRestaurantObj.getReviewsAvgScore()) + "/5  (" + mRestaurantObj.getNumOfReviews() + ")");
//            } else  {
//                DecimalFormat f = new DecimalFormat("0");
//                mStarRatinglabel.setText(f.format(mRestaurantObj.getReviewsAvgScore()) + "/5  (" + mRestaurantObj.getNumOfReviews() + ")");
//            }
        } else {
            mStarRatinglabel.setVisibility(View.GONE);
        }

        if(mRestaurantObj.getMobileNumber() != null && !mRestaurantObj.getMobileNumber().equals("")){
                mCallLabel.setVisibility(View.VISIBLE);
                SpannableString content = new SpannableString(mRestaurantObj.getMobileNumber().toString());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                mCallLabel.setText(content);
        }
        else {
            if(mRestaurantObj.getPhoneNumber() != null && !mRestaurantObj.getPhoneNumber().equals("")) {
                mCallLabel.setVisibility(View.VISIBLE);
                SpannableString content = new SpannableString(mRestaurantObj.getPhoneNumber().toString());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                mCallLabel.setText(content);
            }
            else {
                mCallLabel.setVisibility(View.GONE);
            }
        }


        if (mRestaurantObj.getPriceRatingEuro() != null)
            mPriceRatingLabel.setText(mRestaurantObj.getPriceRatingEuro());
        else
            mPriceRatingLabel.setVisibility(View.GONE);

        if(mRestaurantObj.getServiceTime() != 0){
            mWaitingtime.setText(mRestaurantObj.getServiceTime()+" min");
        } else {
            mWaitingtime.setVisibility(View.GONE);
        }

        if(G3Application.fManager.isSignedin()){
            try {
                G3Application.fManager.isFavourite(mRestaurantObj.getId(), new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()==null){
                            favourite = false;
                            mFavouriteStar.setImageResource(R.drawable.star_white);
                        }
                        else{
                            favourite = true;
                            mFavouriteStar.setImageResource(R.drawable.star_yellow);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch (NotAuthenticatedException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            } catch (NetworkDownException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }

        mOrder = new G3OrderObj(mRestaurantObj.getId(),null,null,calendar.getTime(),System.currentTimeMillis());

//        if(mRestaurantObj.getNumOfReviews()==0){
//            mReviewsButton.setClickable(false);
//            mReviewTextView.setTextColor(mCtx.getResources().getColor(R.color.very_light_grey));
//        }

        mFragment = new G3MenuUserRestaurantFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.nestedfragment, mFragment, "USER_MENU").commit();

    }

    private String getformattedhours(int hour) {
        DecimalFormat df = new DecimalFormat("00");
        return (df.format(hour/100) + ":" + df.format(hour%100));
    }

    public G3RestaurantObj getmRestaurantObj() {
        return mRestaurantObj;
    }

    public static G3OrderObj getOrder() {
        if(mOrder != null){
            return mOrder;
        }
        else{
            Calendar calendar = Calendar.getInstance();
            mOrder = new G3OrderObj(mRestaurantObj.getId(), null, "", calendar.getTime(), System.currentTimeMillis());
            return mOrder;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_user_restaurant, menu);
        return true;
    }

    public void setOptionIcon(int id, int iconRes) {
        if(menu != null) {
            MenuItem item = menu.findItem(id);
            item.setIcon(iconRes);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                if (mNestedFragmentLabel.getText().equals(getResources().getString(R.string.menu))
                        || mNestedFragmentLabel.getText().equals(getResources().getString(R.string.info))
                        || mNestedFragmentLabel.getText().equals(getResources().getString(R.string.reviews))) {

                    onBackPressed();
                } else {
                    mFragment = new G3MenuUserRestaurantFragment();
                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.nestedfragment, mFragment, "USER_MENU").commit();
                }
                return true;

            case R.id.basket_action:
                Intent intent = new Intent(this, G3BasketActivity.class);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static G3RestaurantObj getRestautant(){
        return mRestaurantObj;

    }

    public static void setmOrder(G3OrderObj mOrder) {
        G3UserRestaurantActivity.mOrder = mOrder;
    }

    @Override
    public void addItemToOrder(G3OrderItem orderitem) {

        if (mOrder != null) {
            mOrder.addOrderItem(orderitem);
        }


    }

    @Override
    public void onBackPressed() {

        if (mNestedFragmentLabel.getText().equals(getResources().getString(R.string.menu))
                || mNestedFragmentLabel.getText().equals(getResources().getString(R.string.info))
                || mNestedFragmentLabel.getText().equals(getResources().getString(R.string.reviews))) {
            if (mOrder != null) {
                if (!mOrder.getOrderItems().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(G3UserRestaurantActivity.this);
                    builder.setMessage(R.string.cart_not_empty).setTitle(R.string.warning);
                    builder.setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    supportFinishAfterTransition();
                                    dialog.dismiss();

                                }
                            });
                    builder.setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    supportFinishAfterTransition();
                    super.onBackPressed();
                }
            } else {
                supportFinishAfterTransition();
                super.onBackPressed();
            }
        } else {
            mFragment = new G3MenuUserRestaurantFragment();
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.nestedfragment, mFragment, "USER_MENU").commit();
        }



    }

    public void modifyBottomToolbar(G3OrderItem item){
        int totnumberofitems;
        if(item!=null && item.getQuantity()!=0){
            mChartToolbar.setVisibility(View.VISIBLE);
        } else{
            mChartToolbar.setVisibility(View.GONE);
            return;
        }
        totnumberofitems=Integer.parseInt(mNOfElementsLabel.getText().toString())+item.getQuantity();
        mNOfElementsLabel.setText(Integer.toString(totnumberofitems));
        mPriceLabel.setText(String.format("%.2f", mOrder.getTotalOrderPrice())+ " €");


    }

}
