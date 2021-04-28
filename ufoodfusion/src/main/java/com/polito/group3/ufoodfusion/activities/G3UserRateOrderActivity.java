package com.polito.group3.ufoodfusion.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.objects.G3ReviewObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


/**
 * Created by Luigi on 09/05/2016.
 */
public class G3UserRateOrderActivity  extends G3BaseActivity {

    private TextView mRestaurantName;
    private TextView mTotalOrder;
    private TextView mOrderStatus;
    private TextView mOrderDate;
    private TextView mFoodRatingLabel;
    private TextView mPunctualityRatingLabel;
    private TextView mServiceRatingLabel;
    private TextView mReviewText;
    private ImageView mRestaurantLogo;
    private RatingBar mFoodRating;
    private RatingBar mPunctualityRating;
    private RatingBar mServiceRating;
    private Button mDoneButton;
    private View mFocusView;
    private G3OrderObj mOrderObj;
    private G3RestaurantObj mRestaurantObj;
    private ProgressDialog progressD;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rate_order);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_without_logo);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.review_title);
        }

        loadView();
        setListeners();
        getOrder();

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

    private void loadView() {

        mRestaurantName = (TextView) findViewById(R.id.restaurant_name);
        mTotalOrder = (TextView) findViewById(R.id.order_total);
        mOrderStatus = (TextView) findViewById(R.id.order_status);
        mOrderDate = (TextView) findViewById(R.id.order_date);
        mRestaurantLogo = (ImageView) findViewById(R.id.logo);
        mFoodRating = (RatingBar) findViewById(R.id.food_quality_rate);
        mFoodRatingLabel = (TextView) findViewById(R.id.food_quality_label);
        mPunctualityRating = (RatingBar) findViewById(R.id.punctuality_rate);
        mPunctualityRatingLabel = (TextView) findViewById(R.id.punctuality_label);
        mServiceRating = (RatingBar) findViewById(R.id.service_rate);
        mServiceRatingLabel = (TextView) findViewById(R.id.service_label);
        mReviewText = (TextView) findViewById(R.id.review_text);
        mDoneButton = (Button) findViewById(R.id.submit_button);

    }

    private void getOrder(){
        String orderid;
        Intent intent = getIntent();

        orderid = intent.getStringExtra(G3UserOrdersActivity.ORDEROBJ);

        progressD = TransitionHelper.getProgress(this);
        progressD.show();

        try {
            G3Application.fManager.getOrderWithKey(orderid, new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mOrderObj = dataSnapshot.getValue(G3OrderObj.class);
                    String resId = mOrderObj.getRestaurantId();
                    getRestaurant(resId);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if(progressD.isShowing())
                        progressD.dismiss();
                }
            });
        } catch (NetworkDownException e) {
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            if(progressD.isShowing())
                progressD.dismiss();
        }
    }

    private void getRestaurant(String resId) {

        try {
            G3Application.fManager.getRestaurantProfileWithKey(resId, new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mRestaurantObj = dataSnapshot.getValue(G3RestaurantObj.class);
                    if(progressD.isShowing())
                        progressD.dismiss();
                    loadData();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if(progressD.isShowing())
                        progressD.dismiss();
                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            if(progressD.isShowing())
                progressD.dismiss();
        }


    }
    private void loadData() {


        String url  = mRestaurantObj.getPhotoPath();
        if(url != null && url.length()>0)
        {
            Picasso.with(G3Application.getAppContext()).load(url).placeholder(R.drawable.default_restaurant)
                    .into(mRestaurantLogo);
        }else {
            Picasso.with(G3Application.getAppContext()).load(R.drawable.default_restaurant)
                    .into(mRestaurantLogo);
        }

        mRestaurantName.setText(mRestaurantObj.getName());

        DecimalFormat df = new DecimalFormat(".00");
        String totalOrderText = getResources().getString(R.string.total_order_label)
                + df.format(mOrderObj.getTotalOrderPrice()) + " (" + mOrderObj.getOrderItems().size()
                + " " + getResources().getString(R.string.products) + ")";
        mTotalOrder.setText(totalOrderText);

        String orderStatus;
        switch (mOrderObj.getOrderState()) {
            case 0:
                orderStatus = getResources().getString(R.string.pending_status);
                break;
            case 1:
                orderStatus = getResources().getString(R.string.done_status);
                break;
            case 2:
                orderStatus = getResources().getString(R.string.rejected_status);
                break;
            default:
                orderStatus = "";
                break;
        }
        mOrderStatus.setText(orderStatus);

        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        mOrderDate.setText(sdf.format(mOrderObj.getDate()));

    }



    private void setListeners() {

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForm();
            }
        });

        mFoodRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mFoodRatingLabel.setError(null);
            }
        });

        mPunctualityRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mPunctualityRatingLabel.setError(null);
            }
        });

        mServiceRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mServiceRatingLabel.setError(null);
            }
        });

        mReviewText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mReviewText.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mReviewText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void checkForm() {

        mFocusView = null;

        if (mFoodRating.getRating() == 0) { // Not valued
            mFoodRatingLabel.setError(getString(R.string.not_rated_error));
            mFocusView = mFoodRatingLabel;
            mFocusView.requestFocus();
            return;
        }

        if (mPunctualityRating.getRating() == 0) {
            mPunctualityRatingLabel.setError(getString(R.string.not_rated_error));
            mFocusView = mPunctualityRatingLabel;
            mFocusView.requestFocus();
            return;
        }

        if (mServiceRating.getRating() == 0) {
            mServiceRatingLabel.setError(getString(R.string.not_rated_error));
            mFocusView = mServiceRatingLabel;
            mFocusView.requestFocus();
            return;
        }

        if (mReviewText.getText().toString().isEmpty()) {
            mReviewText.setError(getString(R.string.error_no_review));
            mFocusView = mReviewText;
            mFocusView.requestFocus();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(G3UserRateOrderActivity.this);
        builder.setMessage(R.string.are_you_sure).setTitle(R.string.save);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        saveData();
                        dialog.dismiss();

                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveData(){

        // Create new Review
        G3ReviewObj rew = new G3ReviewObj(mRestaurantObj.getId(), mOrderObj.getUserid(), mOrderObj.getOrderid(),
                     mOrderObj.getDate(), mFoodRating.getRating(), mPunctualityRating.getRating(),
                     mServiceRating.getRating(), mReviewText.getText().toString());

        // Update Restaurant's statistics
        mRestaurantObj.updateReviewsStats(rew.getOverallRating());

        progressD.show();
        try {
            G3Application.fManager.addReview(rew, mOrderObj, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    saveRestaurant();
                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            if (progressD.isShowing())
                progressD.dismiss();
        }

    }

    private void saveRestaurant() {

        try {
            G3Application.fManager.updateRestaurantProfile(mRestaurantObj, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (progressD.isShowing())
                        progressD.dismiss();
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.successprogress),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        } catch (NetworkDownException e) {
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            if (progressD.isShowing())
                progressD.dismiss();
        }

    }

}
