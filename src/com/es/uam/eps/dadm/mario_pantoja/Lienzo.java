package com.es.uam.eps.dadm.mario_pantoja;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Lienzo extends View {

	Paint pline = new Paint();
	ArrayList<PointF> points = new ArrayList<PointF>();
	ArrayList<Float> presiones = new ArrayList<Float>();
	PointF last = new PointF(0,0);
	
	public Lienzo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Lienzo(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Lienzo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		PointF current = new PointF(event.getX(), event.getY());
		if (!current.equals(last)) {
			points.add(current);
			presiones.add(event.getPressure());
			invalidate((int)Math.min(current.x, last.x)-7, (int)Math.min(current.y, last.y)-7,
					(int)Math.max(current.x, last.x)+7, (int)Math.max(current.y, last.y)+7);
			last = current;
		}
		if (event.getAction()==MotionEvent.ACTION_UP) {
			points.add(new PointF(-1,-1));
			presiones.add(-1.0f);
			invalidate();
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		PointF p0 = null;
		for(int i = 0; i < points.size(); i++) {
			PointF p = points.get(i);
			if (p0==null) p0 = p;
			else if (p.x < 0) p0 = null;
			else {
				pline.setStrokeWidth(presiones.get(i).floatValue()*30);
				canvas.drawLine(p0.x,p0.y,p.x,p.y,pline);
				p0 = p;
			}
		}
		
	}
	
}

