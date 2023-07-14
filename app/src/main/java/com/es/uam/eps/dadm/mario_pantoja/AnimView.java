package com.es.uam.eps.dadm.mario_pantoja;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AnimView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private AnimThread animThread;

    public AnimView(Context context,AttributeSet attributes) {
        super(context,attributes);
        
		setFocusable(true);
		setFocusableInTouchMode(true);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        animThread = new AnimThread(holder);
        animThread.setRunning(true);
        animThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        animThread.setRunning(false);
        while (retry) {
            try {
                animThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}
