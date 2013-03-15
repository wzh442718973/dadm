package com.es.uam.eps.dadm.mario_pantoja;

import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * @author marioandrei
 * VIEW
 * 
 */
public class ScoreBoard extends View {
    private Game game;
    
    private final int SIZE = 7;
    private float height_of_position;
    private float width_of_position;
    
    
	public ScoreBoard(Context context, AttributeSet attributes) {
		super(context, attributes);
		setFocusable(true);
		setFocusableInTouchMode(true);

		/* you can set the type of boar here, ENGLISH OR EUROPEAN*/
		game = new Game(context,Game.ENGLISH);

	}
	
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight){
		
		if (width<height)
			height=width;
		else
			width=height;
		/* once picked the shortest size we define the size of the cell
		 * in this case is called, position*/
		
		this.width_of_position=width/7f;
		this.height_of_position=height/7f;
		
		super.onSizeChanged(width, height, oldWidth, oldHeight);
		
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		/* get current size of the board */
		float width_of_board=getWidth();
		float height_of_board=getHeight();
		
		
		if (width_of_board < height_of_board)
	        height_of_board = width_of_board;
		else
			width_of_board=height_of_board;
		
		

		
		/* DRAW the CURRENT PEG NUMBER */
		drawPegCount(canvas);
	}
	
	

	private void drawPegCount (Canvas canvas){
		
	    /* PAINT */
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		paint.setStrokeWidth(10);
		paint.setTextSize(height_of_position * 0.70f);
		paint.setTextScaleX(getWidth() / getHeight());
		paint.setTextAlign(Paint.Align.CENTER);


		paint.setColor(getResources().getColor(R.color.textColor));
		paint.setStrokeWidth(2);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(20);
		canvas.drawText(Integer.toString(game.getPegCount()), 2*width_of_position, (float) ((9.6)*width_of_position), paint);


	}

	
	
	public boolean onTouchEvent(MotionEvent event) {
		if(!game.isActive()){
			return false;
		}
		int xIndex = 0;
	    int yIndex = 0;
	    int action = event.getAction();
		
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				float x = event.getX();
				float y = event.getY();

				if (x < width_of_position)
					xIndex = 0;
				else if (x < 2 * width_of_position)
					xIndex = 1;
				else if (x < 3 * width_of_position)
					xIndex = 2;
				else if (x < 4 * width_of_position)
					xIndex = 3;
				else if (x < 5 * width_of_position)
					xIndex = 4;
				else if (x < 6 * width_of_position)
					xIndex = 5;
				else if (x < 7 * width_of_position)
					xIndex = 6;
		
				
				if (y < height_of_position)
					yIndex = 0;
				else if (y < 2 * height_of_position)
					yIndex = 1;
				else if (y < 3 * height_of_position)
					yIndex = 2;
				else if (y < 4 * height_of_position)
					yIndex = 3;
				else if (y < 5 * height_of_position)
					yIndex = 4;
				else if (y < 6 * height_of_position)
					yIndex = 5;
				else if (y < SIZE * height_of_position)
					yIndex = 6;
				
				if (game.getType()==Game.ENGLISH) {
					if (xIndex<2 && yIndex<2) {
						return super.onTouchEvent(event);
					}
					if (xIndex>4 && yIndex>4) {
						return super.onTouchEvent(event);
					}
					if (xIndex>4 && yIndex<2) {
						return super.onTouchEvent(event);
					}
					if (xIndex<2 && yIndex>4) {
						return super.onTouchEvent(event);
					}

				}
				invalidatePosition(xIndex, yIndex);
			
				break;
		}//end case
		return super.onTouchEvent(event);
		
	}
	private void invalidatePosition(int xIndex, int yIndex) {
		int left = (int) (xIndex*width_of_position);
		int top = (int) (yIndex*height_of_position);
		int right = (int) (xIndex*width_of_position + width_of_position); 
		int bottom = (int) (yIndex*height_of_position + height_of_position);
		
		Rect position = new Rect(left, top, right, bottom);
		invalidate(position);
		}

	
}
