package com.polito.group3.ufoodfusion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.polito.group3.ufoodfusion.R;
import com.polito.group3.ufoodfusion.dialogfragments.G3SelectDateDialogFragment;


public class G3StatisticsMenu extends G3BaseActivity {

    private Button mTodayButton;
    private Button mLastWButton;
    private Button mLastMButton;
    private Button mDailyMButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.ufoodlibrary.R.layout.activity_statistics_menu);

        Toolbar toolbar = (Toolbar) findViewById(com.example.ufoodlibrary.R.id.toolbar_without_logo);
        setSupportActionBar(toolbar);
        setTitle(com.example.ufoodlibrary.R.string.statistic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadView();
        setListeners();
    }

    private void loadView() {

        mTodayButton = (Button) findViewById(com.example.ufoodlibrary.R.id.today);
        mLastWButton = (Button) findViewById(com.example.ufoodlibrary.R.id.lastweek);
        mLastMButton = (Button) findViewById(com.example.ufoodlibrary.R.id.lastmonth);
        mDailyMButton = (Button) findViewById(R.id.dailystatistic);
    }



    void setListeners(){

        mTodayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), G3StatisticForm.class);
                intent.putExtra("type",getResources().getString(R.string.today));
                startActivity(intent);

            }
        });
        mLastWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), G3StatisticForm.class);
                intent.putExtra("type",getResources().getString(R.string.lastweek));
                startActivity(intent);

            }
        });
        mLastMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), G3StatisticForm.class);
                intent.putExtra("type",getResources().getString(R.string.lastmonth));
                startActivity(intent);

            }
        });

        mDailyMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                G3SelectDateDialogFragment mDialogFrag;
                mDialogFrag = G3SelectDateDialogFragment.newInstance(getApplicationContext());
                mDialogFrag.setCancelable(true);
                mDialogFrag.show(getSupportFragmentManager(), "dialogSelectDate");
            }
        });

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
