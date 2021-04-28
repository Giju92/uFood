package com.example.ufoodrestaurant.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.ufoodlibrary.*;
import com.example.ufoodlibrary.utilities.BitmapUtils;
import com.example.ufoodlibrary.utilities.G3Application;

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
                        Intent intent = new Intent();
                        intent.setClass(G3SplashScreen.this, G3TabsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }


            });

        } catch (Exception e) {
            // TODO: handle exception
        }


    }

}
