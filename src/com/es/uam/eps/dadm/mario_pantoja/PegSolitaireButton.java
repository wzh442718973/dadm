/**
 * 
 */
package com.es.uam.eps.dadm.mario_pantoja;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * @author marioandrei
 * VIEW
 */
public class PegSolitaireButton extends Button	{

	Paint paint=new Paint();
	
	int idSelected=R.drawable.selected;
	int idOff=R.drawable.off;
	int idOn=R.drawable.on;
	int currentId=idOn;
	
	public PegSolitaireButton(Context context, AttributeSet attrs) {
	 super(context,attrs);
	 //reset();
	}

	public void reset(){
	       //setText(" ");
	       //setBackgroundColor(Color.DKGRAY);
	}
		
	public void drawOn(){
       //this.setBackgroundResource(R.drawable.on);
		currentId=idOn;
   
	}
	public void drawOff(){
        //this.setBackgroundResource(R.drawable.off);
		currentId=idOff;

	}
	public void drawSelected(){
        //this.setBackgroundResource(R.drawable.selected);
		currentId=idSelected;
	}
	
	//TODO
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	    
		float width = getWidth();
	    float height = getHeight();
	    
	    float padding = 10;
	         
//		int color=getResources().getColor(R.color.background_color);
//		paint.setColor(color);
//		paint.setStrokeWidth(3);
//		canvas.drawRect(30, 10,90,100, paint);
//		paint.setStrokeWidth(0);
//		paint.setColor(color);
//		canvas.drawCircle(10, 10,30, paint);
		
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), currentId);
        canvas.drawBitmap(bitmap, 0, 0, paint);
		
//		Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		backgroundPaint.setColor(color);
//		canvas.drawRect(padding, padding, width-padding, height-padding,
//		                         backgroundPaint);
//	    float x = 0.5f * width;
//	    float y = 0.5f * height;
//	    //Paint paint = new Paint();
//	    paint.setColor(getResources().getColor(R.color.textColor));
//	    paint.setStrokeWidth(2);
//	    paint.setTextAlign(Align.CENTER);
//	    paint.setTextSize(20);
//	    canvas.drawText("* ", x, y, paint);
		
	}
}
