package com.example.ufoodrestaurant.activities;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;

import com.example.ufoodlibrary.R;

public class G3BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    protected void setupWindowAnimations() {
//        // Re-enter transition is executed when returning to this activity
//        Slide slideTransition = new Slide();
//        slideTransition.setSlideEdge(Gravity.LEFT);
//        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
//        getWindow().setReenterTransition(slideTransition);
//        getWindow().setExitTransition(slideTransition);
//    }
//
//    @SuppressWarnings("unchecked") void transitionTo(Intent i) {
//        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
//        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
//        startActivity(i, transitionActivityOptions.toBundle());
//    }

}
