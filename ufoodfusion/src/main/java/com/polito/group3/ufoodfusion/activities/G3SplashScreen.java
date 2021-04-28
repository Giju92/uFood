package com.polito.group3.ufoodfusion.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.utilities.BitmapUtils;
import com.example.ufoodlibrary.utilities.G3Application;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.ufoodfusion.R;

/**
 * Created by Alfonso on 01-Apr-16.
 */
public class G3SplashScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        ImageView bgImageView = (ImageView) findViewById(R.id.imageViewBG);

        BitmapUtils.loadImage(R.drawable.new_logo, bgImageView);

        try {
            Animation ani = new AlphaAnimation(0, 1);
            ani.setDuration(2000);
            bgImageView.startAnimation(ani);

            ani.setAnimationListener(new Animation.AnimationListener() {

                public void onAnimationStart(Animation animation) {


                }

                public void onAnimationRepeat(Animation animation) {


                }

                public void onAnimationEnd(Animation animation) {
                    if(!G3Application.fManager.isSignedin()) {
                        Intent intent = new Intent();
                        intent.setClass(G3SplashScreen.this, G3LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        checkisRestaurant();
                    }
                }


            });

        } catch (Exception e) {
            // TODO: handle exception
        }


    }

    private void checkisRestaurant() {

        try {
            G3Application.fManager.isRestaurant(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        Toast.makeText(getApplication(), getResources().getString(com.example.ufoodlibrary.R.string.successprogress), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), G3TabsActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplication(), getResources().getString(com.example.ufoodlibrary.R.string.successprogress), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), G3UserSearchRestaurant.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (NotAuthenticatedException | NetworkDownException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }

}
