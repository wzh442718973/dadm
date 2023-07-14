package com.glaring.colourful.bully;

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