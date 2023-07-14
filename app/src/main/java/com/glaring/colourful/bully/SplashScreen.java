package com.glaring.colourful.bully;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
 
/**
 * Splash screen activity
 *
 * @author Mario Pantoja
 */
public class SplashScreen extends Activity {
 
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 3000; // 2 seconds
   
 
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.splash_screen);
 
        View splash= (View) findViewById(R.id.splashstitle);
        
		Animation animation=AnimationUtils.loadAnimation(splash.getContext(),R.anim.alpha);
		splash.startAnimation(animation);	
		
		
	
		//Vibrator v = (Vibrator) splash.getContext().getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 300 milliseconds
		//v.vibrate(200);
		
        Handler handler = new Handler();

 
        // run a thread after 2 seconds to start the home screen
        handler.postDelayed(new Runnable() {
 
            @Override
            public void run() {
 
                // make sure we close the splash screen so the user won't come back when it presses back key
 
                finish();
                 
                if (!mIsBackButtonPressed) {
                    // start the home screen if the back button wasn't pressed already 
                    Intent intent = new Intent(SplashScreen.this, Initial.class);
                    SplashScreen.this.startActivity(intent);
               }
                 
            }
 
        }, SPLASH_DURATION); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
 
    }
 
    @Override
   public void onBackPressed() {
 
        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();
 
    }
}