package com.polito.group3.ufoodfusion.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.ufoodlibrary.R;
import com.example.ufoodlibrary.utilities.BitmapUtils;

/**
 * Created by Alfonso on 01-Apr-16.
 */
public class G3UserSplashScreen extends Activity {

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
                    Intent intent = new Intent();
                    intent.setClass(G3UserSplashScreen.this, G3UserSearchRestaurant.class);
                    startActivity(intent);
                    finish();
                }


            });

        } catch (Exception e) {
            // TODO: handle exception
        }


    }

}
