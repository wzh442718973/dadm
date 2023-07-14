/**
 * 
 */
package com.es.uam.eps.dadm.mario_pantoja;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
/**
 * @author marioandrei
 *
 */
import android.view.SurfaceHolder;

public class AnimThread extends Thread {

    private SurfaceHolder holder;
    private boolean running = true;
    int i = 0;

    public AnimThread(SurfaceHolder holder) {
        this.holder = holder;
    }

    @Override
    public void run() {
        while(running ) {
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                 synchronized (holder) {
                    // draw
                    canvas.drawColor(Color.CYAN);
                    Paint paint = new Paint();
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(i++, 100, 50, paint);
                }
            }
            finally {
                    if (canvas != null) {
                            holder.unlockCanvasAndPost(canvas);
                        }
            }
        }
    }

    public void setRunning(boolean b) {
        running = b;
    }
}