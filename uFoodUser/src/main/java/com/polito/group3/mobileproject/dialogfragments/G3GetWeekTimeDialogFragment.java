package com.polito.group3.mobileproject.dialogfragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import com.example.ufoodlibrary.R;


/**
 * Created by Alfonso on 13/04/2016.
 */
public class G3GetWeekTimeDialogFragment extends android.support.v4.app.DialogFragment  {

    private onSaveItemListener mListener;
    private static String mWeekDay;
    private EditText mLunchFrom;
    private EditText mLunchTo;
    private EditText mDinnerFrom;
    private EditText mDinnerTo;
    private ImageButton mSaveButton;
    private ImageButton mCancelButton;
    private int lunchfromH = -1;
    private int lunchfromM = -1;
    private int lunchtoH = -1;
    private int lunchtoM = -1;
    private int dinnerfromH = -1;
    private int dinnerfromM = -1;
    private int dinnertoH = -1;
    private int dinnertoM = -1;
    private Context mCtx;

    public interface onSaveItemListener{
        void onSaveItemClick(String weekDay,int lfh,int lfm,int lth,int ltm,int dfh,int dfm,int dth,int dtm);
    }


    public static G3GetWeekTimeDialogFragment newInstance(String s){

        G3GetWeekTimeDialogFragment fragment = new G3GetWeekTimeDialogFragment();
        mWeekDay = s;

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

        View v = inflater.inflate(R.layout.timetable_dialog, container, false);
        getDialog().setTitle(getResources().getString(R.string.select_time) + ": " + mWeekDay);
        setCancelable(true);

        mCtx = getContext();

        mSaveButton = (ImageButton) v.findViewById(R.id.addimbutt);
        mCancelButton = (ImageButton) v.findViewById(R.id.cancimbutt);

        mLunchFrom = (EditText) v.findViewById(R.id.fromlaunch);
        mLunchTo = (EditText) v.findViewById(R.id.tolaunch);
        mDinnerFrom = (EditText) v.findViewById(R.id.fromdinner);
        mDinnerTo = (EditText) v.findViewById(R.id.todinner);




        setListeners();

        return v;
    }

    private void setListeners() {

        mLunchTo.setOnClickListener(this.mClickListner);
        mDinnerFrom.setOnClickListener(this.mClickListner);
        mDinnerTo.setOnClickListener(this.mClickListner);
        mLunchFrom.setOnClickListener(this.mClickListner);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSaveItemClick(mWeekDay,lunchfromH,lunchfromM,lunchtoH,lunchtoM,dinnerfromH,dinnerfromM,dinnertoH,dinnertoM);
                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private View.OnClickListener mClickListner = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            final EditText mView = (EditText) v;

            TimePickerDialog mTimeDialog = new TimePickerDialog(mCtx,R.style.AppDialogTheme, new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if(mView.getId() == R.id.fromlaunch){

                        lunchfromH = hourOfDay;
                        lunchfromM = minute;
                        mView.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
                    }
                    if(mView.getId() == R.id.tolaunch){

                        lunchtoH = hourOfDay;
                        lunchtoM = minute;
                        mView.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));

                    }
                    if(mView.getId() == R.id.fromdinner){

                        dinnerfromH = hourOfDay;
                        dinnerfromM = minute;
                        mView.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));

                    }
                    if(mView.getId() == R.id.todinner){

                        dinnertoH = hourOfDay;
                        dinnertoM = minute;
                        mView.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));

                    }
                }
            }, 0, 0, true);

            mTimeDialog.setTitle(mView.getText());
            mTimeDialog.show();
        }
    };



}
