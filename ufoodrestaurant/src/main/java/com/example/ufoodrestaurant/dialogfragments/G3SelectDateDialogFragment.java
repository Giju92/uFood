package com.example.ufoodrestaurant.dialogfragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodrestaurant.R;
import com.example.ufoodrestaurant.activities.G3StatisticForm;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Alfonso-LAPTOP on 20/07/2016.
 */
public class G3SelectDateDialogFragment extends android.support.v4.app.DialogFragment{

    private static Context mCtx;
    private Button mCancel;
    private Button mSelect;

    public static G3SelectDateDialogFragment newInstance(Context context) {
        G3SelectDateDialogFragment fragment = new G3SelectDateDialogFragment();
        mCtx = context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_ACTION_BAR);
        View view  = inflater.inflate(com.example.ufoodlibrary.R.layout.dialog_calendar_statistic, container, false);
        CalendarView calendar = (CalendarView) view.findViewById(R.id.calendar);
        mSelect = (Button) view.findViewById(R.id.btn_select);
        mCancel = (Button) view.findViewById(R.id.btn_cancel);
        getDialog().setTitle(getResources().getString(com.example.ufoodlibrary.R.string.select_date));

        final Date[] date = {null};
        date[0] = new Date();
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                switch(month){
                    case 0:
                        cal.set(Calendar.MONTH, Calendar.JANUARY);
                        break;
                    case 1:
                        cal.set(Calendar.MONTH, Calendar.FEBRUARY);
                        break;
                    case 2:
                        cal.set(Calendar.MONTH, Calendar.MARCH);
                        break;
                    case 3:
                        cal.set(Calendar.MONTH, Calendar.APRIL);
                        break;
                    case 4:
                        cal.set(Calendar.MONTH, Calendar.MAY);
                        break;
                    case 5:
                        cal.set(Calendar.MONTH, Calendar.JUNE);
                        break;
                    case 6:
                        cal.set(Calendar.MONTH, Calendar.JULY);
                        break;
                    case 7:
                        cal.set(Calendar.MONTH, Calendar.AUGUST);
                        break;
                    case 8:
                        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
                        break;
                    case 9:
                        cal.set(Calendar.MONTH, Calendar.OCTOBER);
                        break;
                    case 10:
                        cal.set(Calendar.MONTH, Calendar.NOVEMBER);
                        break;
                    case 11:
                        cal.set(Calendar.MONTH, Calendar.DECEMBER);
                        break;
                }

                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                date[0] = cal.getTime();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date[0] == null ) {
                    Toast.makeText(getContext(),getResources().getString(R.string.date_necessary),Toast.LENGTH_SHORT).show();
                }else{
                    long tosend = date[0].getTime();
                    Intent intent = new Intent(mCtx, G3StatisticForm.class);
                    intent.putExtra("date",tosend);
                    startActivity(intent);
                    getDialog().dismiss();
                }
            }
        });


        return view;
    }

}
