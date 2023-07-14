package com.es.uam.eps.dadm.mario_pantoja;

import android.app.Activity;
import android.os.Bundle;

public class Animation extends Activity {
    private AnimView view;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //view = new AnimView(this);
        setContentView(view);
    }
}