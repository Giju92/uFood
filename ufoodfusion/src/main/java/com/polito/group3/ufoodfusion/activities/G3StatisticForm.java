package com.polito.group3.ufoodfusion.activities;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3OrderItem;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.example.ufoodlibrary.utilities.TransitionHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.ufoodfusion.R;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeMap;

public class G3StatisticForm extends G3BaseActivity {

    private String type;
    private TextView date;
    private TextView income;
    private TextView order;
    private Button numorder;
    private Button bestseller;
    private Button overall;
    private BarChart barchart;
    private HorizontalBarChart horbarchart;
    private PieChart piechart;
    private LinearLayout no_order;
    private long longday = 0;
    private boolean daily = false;
    private boolean week = false;
    private boolean month = false;
    private int totincome = 0;
    private int futureincome = 0;
    private int pending = 0;
    private int done = 0;
    private int cancelled = 0;
    private int[] donehours = new int[24];
    private int[] donehourstable = new int[24];
    private int[] weekorder = new int[7];
    private int[] weekordertable = new int[7];
    private int[] monthorder = new int[31];
    private int[] monthordertable = new int[31];

    private Date mDay;
    private ArrayList<G3OrderObj> mOrder;
    private ArrayList<G3OrderObj> filterOrder;
    private ProgressDialog progressD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ufoodlibrary.R.layout.activity_statistics_form);


        //recover the information
        Bundle selected = getIntent().getExtras();
        type = selected.getString("type");
        longday = selected.getLong("date");

        Toolbar toolbar = (Toolbar) findViewById(com.example.ufoodlibrary.R.id.toolbar_without_logo);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setTitle(type);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        loadview();

        //inizialize the two array
        mOrder = new ArrayList<>();
        filterOrder = new ArrayList<>();

        //load date from firebase
        loadData();

    }

    void dailyStatistics(Date d) {
        //get the day and set title
        String day;
        //create a datatime
        DateTime dt = new DateTime(d);
        //switch for title
        switch (dt.getDayOfWeek()) {
            case 1:
                day = getResources().getString(R.string.monday);
                break;
            case 2:
                day = getResources().getString(R.string.tuesday);
                break;
            case 3:
                day = getResources().getString(R.string.wednesday);
                break;
            case 4:
                day = getResources().getString(R.string.thursday);
                break;
            case 5:
                day = getResources().getString(R.string.friday);
                break;
            case 6:
                day = getResources().getString(R.string.saturday);
                break;
            case 7:
                day = getResources().getString(R.string.sunday);
                break;
            default:
                day = "";
        }
        date.setText(day + " " + dt.getDayOfMonth());
        if (longday != 0) {
            setTitle(day + " " + dt.getDayOfMonth());
        }

        // filter order
        filterOrder = filterByDate(d);

        //count order
        for (int i = 0; i < filterOrder.size(); i++) {

            G3OrderObj temp = filterOrder.get(i);
            //find hour and fill the array
            DateTime dtemp = new DateTime(temp.getDate());
            Date.class.isInstance(temp.getDate());

            int h = dtemp.getHourOfDay();
            switch (temp.getOrderState()) {

                //Pending
                case 0:
                    pending++;
                    futureincome += temp.getTotalOrderPrice();
                    //check if is a table booking
                    break;

                //Done
                case 1:
                    done++;
                    totincome += temp.getTotalOrderPrice();
                    //check if is a table booking
                    if (temp.getSeats() != 0) {
                        donehourstable[h] += 1;
                    } else {
                        donehours[h] += 1;
                    }
                    break;

                //Cancelled
                case 2:
                    cancelled++;
                    break;

                default:
                    break;
            }
        }

        setListner();
        //check if there are orders
        if((new DateTime(d).toLocalDate()).equals(new LocalDate())) {
            if (done == 0 && pending == 0 && cancelled == 0) {
                order.setText("0");
                income.setText("0 €");
                no_order.setVisibility(View.VISIBLE);

            } else {
                order.setText("" + done + " (" + pending + ")");
                income.setText("" + totincome + "€ + (" + futureincome + "€)");
                if (done != 0) {
                    barchart();
                }
            }
        } else {
            if (done == 0) {
                order.setText("0");
                income.setText("0 €");
                no_order.setVisibility(View.VISIBLE);
            } else {
                order.setText("" + done);
                income.setText("" + totincome + "€");
                barchart();
            }
        }

    }

    void week(Date d) {
        //set title
        date.setText(type);
        //filter order
        filterOrder = filterlastWeek(d);
        //count orders
        for (int i = 0; i < filterOrder.size(); i++) {

            G3OrderObj temp = filterOrder.get(i);
            switch (temp.getOrderState()) {

                //Pending
                case 0:
                    pending++;
                    futureincome += temp.getTotalOrderPrice();
                    break;

                //Done
                case 1:
                    done++;
                    totincome += temp.getTotalOrderPrice();
                    break;

                //Cancelled
                case 2:
                    cancelled++;
                    break;

                default:
                    break;
            }
        }
        ArrayList<G3OrderObj> filterorderevade = filterOnlyEvaded(filterOrder);
        // count for each day
        for (int i = 1; i < 8; i++) {
            //int for count order in each day

            int tempcount = 0;
            int tempcounttable = 0;
            ArrayList<G3OrderObj> dayorders = filterByDayOfTheWeek(filterorderevade, i);
            for (int j = 0; j < dayorders.size(); j++) {

                if (dayorders.get(j).getSeats() != 0) {
                    tempcounttable++;
                } else {
                    tempcount++;
                }
            }

            weekorder[i - 1] = tempcount;
            weekordertable[i - 1] = tempcounttable;
        }

        setListner();
        //check if there are orders
        if (done == 0) {
            order.setText("0");
            income.setText("0 €");
            no_order.setVisibility(View.VISIBLE);
        } else {
            order.setText("" + done);
            income.setText("" + totincome + "€");
            barchart();
        }

    }

    private void month(Date d) {
        //set title
        date.setText(type);
        //filter order
        filterOrder = filterlastMonth(d);
        //count orders
        for (int i = 0; i < filterOrder.size(); i++) {

            G3OrderObj temp = filterOrder.get(i);
            switch (temp.getOrderState()) {

                //Pending
                case 0:
                    pending++;
                    futureincome += temp.getTotalOrderPrice();
                    break;

                //Done
                case 1:
                    done++;
                    totincome += temp.getTotalOrderPrice();
                    break;

                //Cancelled
                case 2:
                    cancelled++;
                    break;

                default:
                    break;
            }
        }

        DateTime ff = new DateTime(d);
        ff = ff.minusMonths(1);

        ArrayList<G3OrderObj> filterorderevade = filterOnlyEvaded(filterOrder);
        // count for each day
        for (int i = 1; i <= ff.dayOfMonth().getMaximumValue(); i++) {
            //int for count order in each day
            int tempcount = 0;
            int tempcounttable = 0;
            ArrayList<G3OrderObj> dayorders = filterByDayOfTheMonth(filterorderevade, i);
            for (int j = 0; j < dayorders.size(); j++) {

                if (dayorders.get(j).getSeats() != 0) {
                    tempcounttable++;
                } else {
                    tempcount++;
                }
            }
            monthorder[i - 1] = tempcount;
            monthordertable[i - 1] = tempcounttable;
        }

        setListner();
        //check if there are orders
        if (done == 0) {
            order.setText("0");
            income.setText("0 €");
            no_order.setVisibility(View.VISIBLE);
        } else {
            order.setText("" + done);
            income.setText("" + totincome + "€");
            barchart();
        }

    }

    void piechart() {

        //setvisibility
        horbarchart.setVisibility(View.GONE);
        barchart.setVisibility(View.GONE);
        no_order.setVisibility(View.GONE);
        piechart.setVisibility(View.VISIBLE);

        //set title of pie chart
        piechart.setDescription("");

        //set label information
        //TODO dalete label

        //set Data
        ArrayList<Entry> entries = new ArrayList<Entry>();
        //labels
        ArrayList<String> labels = new ArrayList<String>();
        //add color
        ArrayList<Integer> colors = new ArrayList<Integer>();

        if (done != 0) {
            entries.add(new Entry(done, 0));
            labels.add(getResources().getString(R.string.done_status));
            //for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(ColorTemplate.VORDIPLOM_COLORS[0]);
        }

        if (pending != 0) {
            entries.add(new Entry(pending, 1));
            labels.add(getResources().getString(R.string.pending_status));
            colors.add(ColorTemplate.VORDIPLOM_COLORS[1]);
        }

        if (cancelled != 0) {
            entries.add(new Entry(cancelled, 2));
            labels.add(getResources().getString(R.string.rejected_status));
            colors.add(ColorTemplate.VORDIPLOM_COLORS[2]);
        }

        PieDataSet dataset = new PieDataSet(entries, "");

        dataset.setColors(colors);

        // pie data set
        PieData data = new PieData(labels, dataset);

        // customize legends
        Legend l = piechart.getLegend();
        l.setPosition(Legend.LegendPosition.PIECHART_CENTER);
        l.setTextSize(17);

        //set text dimension
        data.setValueTextSize(17);
        data.setDrawValues(true);
        data.setValueFormatter(new MyValueFormatter());

        piechart.setData(data);
        piechart.animateY(2000);

        //make it visible
        piechart.invalidate();
    }

    void barchart() {

        //set visibility
        horbarchart.setVisibility(View.GONE);
        piechart.setVisibility(View.GONE);
        no_order.setVisibility(View.GONE);
        barchart.setVisibility(View.VISIBLE);

        //some set
        barchart.setDrawGridBackground(false);
        barchart.setDrawBarShadow(false);
        barchart.setDrawValueAboveBar(false);

//        YAxis yAxis = barchart.getAxisLeft();
//        yAxis.setDrawGridLines(false);
//        yAxis = barchart.getAxisRight();
//        yAxis.setDrawGridLines(false);

        // disable Y-Axis Zoom and highlighting of bars
        barchart.setScaleYEnabled(false);
        barchart.setHighlightPerTapEnabled(false);
        barchart.setHighlightPerDragEnabled(false);

        YAxis left = barchart.getAxisLeft();
        left.setDrawLabels(false); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(false); // no grid lines
        left.setDrawZeroLine(true); // draw a zero line
        barchart.getAxisRight().setEnabled(false); // no right axis

        XAxis xaxis = barchart.getXAxis();
        xaxis.setDrawAxisLine(false);
        xaxis.setDrawGridLines(false);
        xaxis.setAvoidFirstLastClipping(true);

        if (daily) {
            //evoid to print all hours
            int i, start = 0;
            while (start < donehours.length && donehours[start] == 0 && donehourstable[start] == 0) {
                start++;
            }
            i = start;
            //set data
            ArrayList<BarEntry> entries = new ArrayList<>();
            //add entries
            int index = 0;
            while (i < 24) {
                entries.add(new BarEntry(new float[]{donehours[i], donehourstable[i]}, index));
                i++;
                index++;
            }

            BarDataSet dataset = new BarDataSet(entries, "");
            dataset.setColors(new int[]{ColorTemplate.VORDIPLOM_COLORS[3], ColorTemplate.JOYFUL_COLORS[1]});
            dataset.setStackLabels(new String[]{getString(R.string.takeaway_legend), getString(R.string.tb_legend)});

            // creating labels
            ArrayList<String> labels = new ArrayList<String>();
            while (start < 24) {
                labels.add("" + start + ":00");
                start++;
            }

            BarData data = new BarData(labels, dataset);
            data.setValueFormatter(new MyValueFormatter());
            barchart.setData(data);
            barchart.animateY(1500);
            barchart.setDescription(getString(R.string.num_orders_legend));
            barchart.invalidate();
        }
        if (week) {

            //set data
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                //add entries
                entries.add(new BarEntry(new float[]{weekorder[i], weekordertable[i]}, i));

            }

            BarDataSet dataset = new BarDataSet(entries, "");
            dataset.setColors(new int[]{ColorTemplate.VORDIPLOM_COLORS[3], ColorTemplate.JOYFUL_COLORS[1]});
            dataset.setStackLabels(new String[]{getString(R.string.takeaway_legend), getString(R.string.tb_legend)});

            DateTime dt = new DateTime(mDay);
            dt = dt.minusWeeks(1);
            // creating labels
            ArrayList<String> labels = new ArrayList<String>();
            for (int i = 1; i < 8; i++) {
                String toInsert = new SimpleDateFormat("d EEE").format(dt.withDayOfWeek(i).toDate());
                labels.add(toInsert);
            }


            BarData data = new BarData(labels, dataset);
            data.setValueFormatter(new MyValueFormatter());
            barchart.setData(data);
            barchart.animateY(1500);
            barchart.setDescription(getString(R.string.num_orders_legend));
            barchart.invalidate();

        }
        if (month) {

            DateTime ff = new DateTime(mDay);
            ff = ff.minusMonths(1);
            //set data
            ArrayList<BarEntry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<String>();

            for (int i = 0; i < ff.dayOfMonth().getMaximumValue(); i++) {
                //add entries
                entries.add(new BarEntry(new float[]{monthorder[i], monthordertable[i]}, i));
                labels.add("" + (i + 1));

            }

            BarDataSet dataset = new BarDataSet(entries, "");
            dataset.setColors(new int[]{ColorTemplate.VORDIPLOM_COLORS[3], ColorTemplate.JOYFUL_COLORS[1]});
            dataset.setStackLabels(new String[]{getString(R.string.takeaway_legend), getString(R.string.tb_legend)});

            BarData data = new BarData(labels, dataset);
            data.setValueFormatter(new MyValueFormatter());
            barchart.setData(data);
            barchart.animateY(1500);
            barchart.setDescription(getString(R.string.num_orders_legend));
            barchart.invalidate();

        }
    }

    void ranking() {

        //set visibility
        piechart.setVisibility(View.GONE);
        barchart.setVisibility(View.GONE);
        no_order.setVisibility(View.GONE);
        horbarchart.setVisibility(View.VISIBLE);

        //some set
        horbarchart.getLegend().setEnabled(false);
        horbarchart.setPinchZoom(false);
        horbarchart.setDrawGridBackground(false);
        horbarchart.setDrawBarShadow(false);
        horbarchart.setDrawValueAboveBar(false);

        // disable Axis Zoom and highlighting of bars
        horbarchart.setScaleXEnabled(false);
        horbarchart.setScaleYEnabled(false);
        horbarchart.setHighlightPerTapEnabled(false);
        horbarchart.setHighlightPerDragEnabled(false);

        YAxis left = horbarchart.getAxisLeft();
        left.setDrawLabels(false); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(false); // no grid lines
//        left.setDrawZeroLine(true); // draw a zero line
        horbarchart.getAxisRight().setEnabled(false); // no right axis

        XAxis xaxis = horbarchart.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xaxis.setDrawAxisLine(false);
        xaxis.setDrawGridLines(false);
//        xaxis.setAvoidFirstLastClipping(true);

        //set data and
        ArrayList<BarEntry> entries = new ArrayList<>();
        // creating labels
        ArrayList<String> labels = new ArrayList<String>();

        ArrayList<G3OrderObj> tmp = filterOnlyEvaded(filterOrder);

        LinkedHashMap<String, Integer> topseller = getTopSeller(tmp);

        if (topseller != null) {
            if (!topseller.isEmpty()) {
                int count = 0;
                for (String s : topseller.keySet()) {
                    Integer j = topseller.get(s);
                    entries.add(new BarEntry(j, count));
                    labels.add(s);

                    if (count < topseller.size())
                        count++;
                    else
                        break;
                }
            }

            BarDataSet dataset = new BarDataSet(entries, "");

            BarData data = new BarData(labels, dataset);
            data.setValueFormatter(new MyValueFormatter());
            horbarchart.setData(data);
            horbarchart.animateY(1500);
            horbarchart.setDescription("");
            horbarchart.invalidate();

        }
    }

    public class MyValueFormatter implements ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            if (value > 0) {
                return Math.round(value) + "";
            } else {
                return "";
            }
        }
    }

    private ArrayList<G3OrderObj> filterByDate(Date d) {

        ArrayList<G3OrderObj> bRet = new ArrayList<>();
        DateTime dt = new DateTime(d);

        if (mOrder != null && d != null) {
            if (!mOrder.isEmpty()) {
                for (G3OrderObj temp : mOrder) {
                    DateTime toCmp = new DateTime(temp.getDate());
                    if (toCmp.toLocalDate().isEqual(dt.toLocalDate())) {
                        bRet.add(temp);
                    }
                }

            }
        }

        return bRet;
    }

    private void loadData() {

        progressD = TransitionHelper.getProgress(this);
        progressD.show();

        try {
            G3Application.fManager.getRestaurantOrders(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        G3OrderObj order = data.getValue(G3OrderObj.class);

                        mOrder.add(order);
                    }
                    progressD.dismiss();

                    //chose the right type of statistics
                    if (type != null) {
                        if (type.equals(getResources().getString(R.string.today))) {
                            daily = true;
                            dailyStatistics(mDay = new Date());
                        } else if (type.equals(getResources().getString(R.string.lastweek))) {
                            week = true;
                            week(mDay = new Date());
                        } else if (type.equals(getResources().getString(R.string.lastmonth))) {
                            month = true;
                            month(mDay = new Date());
                        }
                    } else {//daily statistic for specific day
                        if (longday != 0) {
                            daily = true;
                            dailyStatistics(mDay = new Date(longday));
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (progressD.isShowing())
                        progressD.dismiss();
                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            if (progressD.isShowing())
                progressD.dismiss();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void loadview() {
        date = (TextView) findViewById(R.id.date);
        income = (TextView) findViewById(R.id.income);
        order = (TextView) findViewById(R.id.order);
        numorder = (Button) findViewById(R.id.numorder);
        bestseller = (Button) findViewById(R.id.bestseller);
        overall = (Button) findViewById(R.id.overall);
        piechart = (PieChart) findViewById(R.id.piechart);
        barchart = (BarChart) findViewById(R.id.barchart);
        horbarchart = (HorizontalBarChart) findViewById(R.id.horbarchart);
        no_order = (LinearLayout) findViewById(R.id.ranklayout);
    }

    void setListner() {

        if (done == 0) {

            numorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set visibility
                    no_order.setVisibility(View.GONE);
                    piechart.setVisibility(View.GONE);
                    barchart.setVisibility(View.GONE);
                    no_order.setVisibility(View.VISIBLE);
                }
            });

            bestseller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //set visibility
                    no_order.setVisibility(View.GONE);
                    piechart.setVisibility(View.GONE);
                    barchart.setVisibility(View.GONE);
                    no_order.setVisibility(View.VISIBLE);

                }
            });

            overall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancelled == 0 && pending == 0) {
                        //set visibility
                        no_order.setVisibility(View.GONE);
                        piechart.setVisibility(View.GONE);
                        barchart.setVisibility(View.GONE);
                        no_order.setVisibility(View.VISIBLE);
                    } else {
                        piechart();
                    }

                }
            });
        } else {
            numorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    barchart();
                }
            });

            bestseller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ranking();
                }
            });

            overall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    piechart();
                }
            });
        }
    }

    private ArrayList<G3OrderObj> filterOnlyEvaded(ArrayList<G3OrderObj> orders) {
        ArrayList<G3OrderObj> bRet = new ArrayList<>();
        if (orders != null) {
            if (!orders.isEmpty()) {
                for (G3OrderObj order : orders) {

                    if (order.getOrderState() == 1) {
                        bRet.add(order);
                    }
                }
            }
        }

        return bRet;
    }

    private ArrayList<G3OrderObj> filterByDayOfTheWeek(ArrayList<G3OrderObj> orders, int day) {
        ArrayList<G3OrderObj> bRet = new ArrayList<>();

        if (orders != null) {
            if (!orders.isEmpty()) {
                for (G3OrderObj order : orders) {
                    DateTime dt = new DateTime(order.getDate());
                    if (dt.getDayOfWeek() == day) {
                        bRet.add(order);
                    }
                }
            }
        }

        return bRet;
    }

    private ArrayList<G3OrderObj> filterByDayOfTheMonth(ArrayList<G3OrderObj> orders, int day) {
        ArrayList<G3OrderObj> bRet = new ArrayList<>();

        if (orders != null) {
            if (!orders.isEmpty()) {
                for (G3OrderObj order : orders) {
                    DateTime dt = new DateTime(order.getDate());
                    if (dt.getDayOfMonth() == day) {
                        bRet.add(order);
                    }
                }
            }
        }

        return bRet;
    }

    private ArrayList<G3OrderObj> filterlastWeek(Date d) {

        ArrayList<G3OrderObj> bRet = new ArrayList<>();
        if (d == null) {
            return bRet;
        }

        DateTime thisday = new DateTime(d);
        thisday = thisday.minusWeeks(1);

        DateTime lunedi = thisday.withDayOfWeek(1).withTimeAtStartOfDay();
        DateTime domenica = thisday.withDayOfWeek(7).withTimeAtStartOfDay().plusDays(1);

        Interval interval = new Interval(lunedi, domenica);
        if (mOrder != null) {
            if (!mOrder.isEmpty()) {
                for (G3OrderObj temp : mOrder) {
                    DateTime dt = new DateTime(temp.getDate());
                    if (interval.contains(dt)) {
                        bRet.add(temp);
                    }
                }

            }
        }

        return bRet;

    }

    private ArrayList<G3OrderObj> filterlastMonth(Date d) {

        ArrayList<G3OrderObj> bRet = new ArrayList<>();
        if (d == null) {
            return bRet;
        }

        DateTime thisday = new DateTime(d);
        thisday = thisday.minusMonths(1);


        DateTime primo = thisday.withDayOfMonth(thisday.dayOfMonth().getMinimumValue()).withTimeAtStartOfDay();
        DateTime ultimo = thisday.withDayOfMonth(thisday.dayOfMonth().getMaximumValue()).withTimeAtStartOfDay().plusDays(1);

        Interval interval = new Interval(primo, ultimo);
        if (mOrder != null) {
            if (!mOrder.isEmpty()) {
                for (G3OrderObj temp : mOrder) {
                    DateTime dt = new DateTime(temp.getDate());
                    if (interval.contains(dt)) {
                        bRet.add(temp);
                    }
                }

            }
        }

        return bRet;

    }

    private ArrayList<G3OrderObj> filterByWeeksBeforeDate(Date d, int weeks) {

        ArrayList<G3OrderObj> bRet = new ArrayList<>();
        if (d == null) {
            return bRet;
        }

        DateTime dtUP = new DateTime(d);
        DateTime dtDOWN = new DateTime(d);
        dtDOWN = dtDOWN.minusWeeks(weeks);
        Interval interval = new Interval(dtDOWN, dtUP);

        if (mOrder != null) {
            if (!mOrder.isEmpty()) {
                for (G3OrderObj temp : mOrder) {
                    DateTime dt = new DateTime(temp.getDate());
                    if (interval.contains(dt)) {
                        bRet.add(temp);
                    }
                }

            }
        }

        return bRet;
    }

    private LinkedHashMap<String, Integer> getTopSeller(ArrayList<G3OrderObj> orders) {
        HashMap<String, Integer> myMap = new HashMap<>();
        LinkedHashMap<String, Integer> sortedMap = null;

        if (orders != null) {
            if (!orders.isEmpty()) {
                for (G3OrderObj tempOrder : orders) {
                    List<G3OrderItem> items = tempOrder.getOrderItems();
                    for (G3OrderItem item : items) {
                        String key = item.getMenuItem().getName();
                        int qty = item.getQuantity();
                        Integer toInsert;
                        if (myMap.containsKey(key)) {
                            toInsert = myMap.get(key);
                            toInsert += qty;
                        } else {
                            toInsert = qty;
                        }
                        myMap.put(key, toInsert);
                    }

                }
            }


            sortedMap = sortHashMapByValues(myMap);

        }

        NavigableMap<String, Integer> map = new TreeMap<>(sortedMap);
        LinkedHashMap<String, Integer> reverseMap = new LinkedHashMap<>();

        //revert
        NavigableSet<String> keySet = map.navigableKeySet();
        Iterator<String> iterator = keySet.descendingIterator();
        String i;
        while (iterator.hasNext()) {
            i = iterator.next();
            reverseMap.put(i, map.get(i));
        }


        return reverseMap;

    }

    public LinkedHashMap<String, Integer> sortHashMapByValues(
            HashMap<String, Integer> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Integer> sortedMap =
                new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();

        while (valueIt.hasNext()) {
            Integer val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = passedMap.get(key);
                Integer comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
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

}
