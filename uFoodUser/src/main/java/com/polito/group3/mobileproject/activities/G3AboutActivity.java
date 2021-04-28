package com.polito.group3.mobileproject.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.ufoodlibrary.R;

/**
 * Created by Alfonso on 14/04/2016.
 */
public class G3AboutActivity extends G3BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_without_logo);
        setSupportActionBar(toolbar);
        setTitle(R.string.about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
