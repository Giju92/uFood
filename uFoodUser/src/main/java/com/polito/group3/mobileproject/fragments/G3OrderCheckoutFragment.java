package com.polito.group3.mobileproject.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3OpeningHoursObj;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.objects.G3RestaurantObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.polito.group3.mobileproject.activities.G3BasketActivity;
import com.polito.group3.mobileproject.activities.G3LoginActivity;
import com.polito.group3.mobileproject.activities.G3UserRestaurantActivity;
import com.polito.group3.mobileproject.activities.G3UserSearchRestaurant;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mattia on 12/05/2016.
 */
public class G3OrderCheckoutFragment extends android.support.v4.app.Fragment  {

    private Context mCtx;
    private View v;
    private G3OrderObj mOrder;
    private boolean mAllowsTableBooking = false;
    private int quantity = 1;
    private int mServiceTime;
    private G3OpeningHoursObj mLunchTime;
    private G3OpeningHoursObj mDinnerTime;
    private ArrayList<String> mSpinnerDataSet;

    private TextView mAwayOrTableLabel;
    private TextView mNumberOfPeopleLabel;
    private ImageButton mPlusButton;
    private ImageButton mMinusButton;
    private EditText mNumOfPeople;
//    private Spinner mHoursSpinner;
    private WheelView wheelView;
    private Button mCheckoutButton;
    private RadioGroup mAwayOrTableRadioGroup;
    private RadioButton mTakeAwayRadioButton;
    private RadioButton mTableRadioButton;
    private G3RestaurantObj mRestaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mCtx = this.getContext();

        v = inflater.inflate(R.layout.fragment_order_checkout, container, false);

        loadView();

        loadData();

        setListeners();

        return v;
    }

    private void loadView() {
        mAwayOrTableLabel = (TextView) v.findViewById(R.id.awayortablelabel);
        mNumberOfPeopleLabel = (TextView) v.findViewById(R.id.numberofpeoplelable);
        mPlusButton = (ImageButton) v.findViewById(R.id.plusbutton);
        mMinusButton = (ImageButton) v.findViewById(R.id.minusbutton);
        mNumOfPeople = (EditText) v.findViewById(R.id.nofpeople);
        mCheckoutButton = (Button) v.findViewById(R.id.buttoncheckout);
        mAwayOrTableRadioGroup = (RadioGroup) v.findViewById(R.id.awayortableradiogroup);
        mTakeAwayRadioButton = (RadioButton) v.findViewById(R.id.take_away);
        mTableRadioButton = (RadioButton) v.findViewById(R.id.eat_here);
//        mHoursSpinner = (Spinner) v.findViewById(R.id.spinner2);
        wheelView = (WheelView) v.findViewById(R.id.wheelview);

    }

    private void loadData() {
        mOrder = G3UserRestaurantActivity.getOrder();
        mRestaurant = G3UserRestaurantActivity.getRestautant();
        mAllowsTableBooking = ((G3BasketActivity) getActivity()).getAllowsTableBooking();
        mServiceTime = ((G3BasketActivity) getActivity()).getServiceTime();
        mLunchTime = ((G3BasketActivity) getActivity()).getLunchTime();
        mDinnerTime = ((G3BasketActivity) getActivity()).getDinnerTime();

        if(!mAllowsTableBooking){
            mAwayOrTableLabel.setVisibility(View.GONE);
            mAwayOrTableRadioGroup.setVisibility(View.GONE);
            mNumberOfPeopleLabel.setVisibility(View.GONE);
            mPlusButton.setVisibility(View.GONE);
            mMinusButton.setVisibility(View.GONE);
            mNumOfPeople.setVisibility(View.GONE);
        }

        mSpinnerDataSet = fillSpinnerData();

        if (mSpinnerDataSet.isEmpty()) {
            mOrder = null;

            AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
            builder.setMessage(mCtx.getString(R.string.error_too_late)).setTitle(mCtx.getString(R.string.error_label));
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            getActivity().finish();
                            dialog.dismiss();

                        }
                    });


            AlertDialog dialog = builder.create();
            dialog.show();
        }

//        ArrayAdapter<String> mAdapter= new ArrayAdapter<>(mCtx, android.R.layout.simple_spinner_item, mSpinnerDataSet);
//        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mHoursSpinner.setAdapter(mAdapter);
        wheelView.setWheelAdapter(new ArrayWheelAdapter(mCtx));
        wheelView.setSkin(WheelView.Skin.Holo);
        WheelView.WheelViewStyle wheelViewStyle = new WheelView.WheelViewStyle();
        wheelViewStyle.backgroundColor = Color.TRANSPARENT;
        wheelViewStyle.holoBorderColor = getResources().getColor(R.color.primary);
        wheelViewStyle.textSize = 14;
        wheelViewStyle.selectedTextZoom = 1.1f;
        wheelView.setStyle(wheelViewStyle);
        wheelView.setWheelData(mSpinnerDataSet);

    }

    private ArrayList<String> fillSpinnerData(){

        ArrayList<String> timeSlots = new ArrayList<>();

        if (mLunchTime != null) {

            timeSlots.addAll(mLunchTime.getOrderingTimeSlots(mServiceTime));

        }

        if (mDinnerTime != null) {

            timeSlots.addAll(mDinnerTime.getOrderingTimeSlots(mServiceTime));

        }

        return timeSlots;

    }

    private void setListeners() {

        mNumOfPeople.setKeyListener(null);

        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                mNumOfPeople.setText(String.valueOf(quantity));
            }
        });

        mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity>1) {
                    quantity--;
                    mNumOfPeople.setText(String.valueOf(quantity));
                }
            }
        });

        mTakeAwayRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumberOfPeopleLabel.setVisibility(View.GONE);
                mMinusButton.setVisibility(View.GONE);
                mPlusButton.setVisibility(View.GONE);
                mNumOfPeople.setVisibility(View.GONE);
            }
        });

        mTableRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumberOfPeopleLabel.setVisibility(View.VISIBLE);
                mMinusButton.setVisibility(View.VISIBLE);
                mPlusButton.setVisibility(View.VISIBLE);
                mNumOfPeople.setVisibility(View.VISIBLE);
            }
        });


        mCheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);



                if (mAwayOrTableRadioGroup.getCheckedRadioButtonId() == -1 && mAllowsTableBooking) {
                    builder.setMessage(R.string.choosesomething).setTitle("Ops!");
                    builder.setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();

                                }
                            });


                    AlertDialog dialog2 = builder.create();
                    dialog2.show();
                } else {


                    builder.setMessage(mCtx.getString(R.string.confirm_order)).setTitle(mCtx.getString(R.string.confirm_order_title));
                    builder.setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    int selectedId = mAwayOrTableRadioGroup.getCheckedRadioButtonId();

                                    if (selectedId == R.id.eat_here) {

                                        mOrder.setSeats(Integer.parseInt(mNumOfPeople.getText().toString()));
                                    }

                                    String orderTime = wheelView.getSelectionItem().toString();
                                    mOrder.setDate(new DateTime()
                                            .withHourOfDay(Integer.parseInt(orderTime.substring(0, 2))).
                                                    withMinuteOfHour(Integer.parseInt(orderTime.substring(3, 5))).toDate());

                                    mOrder.setTimestamp(System.currentTimeMillis());
                                    if(mOrder.getOrderItems() == null){
                                        Toast.makeText(getContext(),getResources().getString(R.string.no_orders), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    final ProgressDialog progressD = TransitionHelper.getProgress(getActivity());
                                    progressD.show();
                                    try {
                                        G3Application.fManager.addOrder(mOrder, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(Task<Void> task) {
                                                progressD.dismiss();
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getContext(),getResources().getString(R.string.successprogress),Toast.LENGTH_SHORT).show();
                                                    Calendar calendar = Calendar.getInstance();
                                                    mOrder = new G3OrderObj(mRestaurant.getId(),null,null,calendar.getTime(),System.currentTimeMillis());

                                                    G3UserRestaurantActivity.setmOrder(mOrder);
                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(mCtx);
                                                    builder2.setCancelable(false);
                                                    builder2.setMessage(R.string.ordine_Send).setTitle(R.string.success);
                                                    builder2.setPositiveButton(R.string.ok,
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    getActivity().finish();
                                                                    dialog.dismiss();
                                                                    Intent i=new Intent (getActivity(), G3UserSearchRestaurant.class);
                                                                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                    startActivity(i);
                                                                }
                                                            });


                                                    AlertDialog dialog2 = builder2.create();
                                                    dialog2.show();
                                                }
                                            }
                                        });
                                    } catch (NotAuthenticatedException | NetworkDownException e) {
                                        progressD.dismiss();
                                        Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), G3LoginActivity.class);
                                        intent.putExtra("checkout",true);
                                        startActivity(intent);
                                    }

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
            }

        });

    }

}
