package com.es.uam.eps.dadm.mario_pantoja;


import java.util.ArrayList;

import org.apache.http.client.CircularRedirectException;

import android.view.HapticFeedbackConstants;
import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.view.animation.Animation;

/**
 * @author marioandrei
 * VIEW
 * 
 */
public class Board extends View {
    private Game game;
    
    private final int SIZE = 7;
    private float height_of_position;
    private float width_of_position;
    private Bitmap bitmapON,bitmapOFF,bitmapSEL;

    Context context;
    /**
     * Constructor
     * @param context
     * @param attributes
     */
	public Board(Context context, AttributeSet attributes) {
		super(context, attributes);
		setFocusable(true);
		setFocusableInTouchMode(true);

		/* you can set the type of board here, ENGLISH OR EUROPEAN*/
		game = new Game(context, Game.EUROPEAN);
		this.context=context;

	}
	/**
	 * updates width and height of the board positions
	 */
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight){
		
		if (width<height)
			height=width;
		else
			width=height;
		/* once picked the shortest size we define the size of the cell
		 * in this case is called, position*/
		
		this.width_of_position=width/7f;
		this.height_of_position=height/7f;
		
		bitmapON = BitmapFactory.decodeResource(getResources(), R.drawable.on);
		bitmapSEL = BitmapFactory.decodeResource(getResources(), R.drawable.selected);
		bitmapOFF = BitmapFactory.decodeResource(getResources(), R.drawable.off);
		
		super.onSizeChanged(width, height, oldWidth, oldHeight);
		
	}
	
	//TODO optimize
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		/* get current size of the board */
		float width_of_board=getWidth();
		float height_of_board=getHeight();
		canvas.save();
		
		if (width_of_board < height_of_board)
	        height_of_board = width_of_board;
		else
			width_of_board=height_of_board;
	

        
		/* DRAW the BOARD */
		drawPegs(canvas);
		
		/* DRAW the CURRENT PEG NUMBER */
		drawPegCount(canvas);
		
		
		if (game.getSelectionModeOn()==true) {
			//Toast.makeText(this.getContext(), "cadena "+game.posibilities(game.posibleDestinations(game.getPivotX(), game.getPivotY())) , Toast.LENGTH_LONG).show();

			paintPossibleDestinations(game.posibleDestinations(game.getPivotX(), game.getPivotY()), canvas);
		}
	}
	
	//TODO optimize even more
	private void drawPegs (Canvas canvas){
		
	    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLACK);


	    /* all the bitmaps are preloaded onSizeChanged, maybe It could also go in Constructor */
		
		/* PAINT BOARD FROM GAME MATRIX */
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++) {
				if (game.getGrid()[i][j]==1) {
					/* paint ON */
			        canvas.drawBitmap(bitmapON, i*width_of_position, j*width_of_position, paint);

				} else if (game.getGrid()[i][j]==0) {
			        canvas.drawBitmap(bitmapOFF, i*width_of_position, j*width_of_position, paint);
					/* paint OFF*/

				} else if (game.getGrid()[i][j]==2) {
			        canvas.drawBitmap(bitmapSEL, i*width_of_position, j*width_of_position, paint);
					/* paint SELECTED*/

				}
			}
			
		/*else
			{ 	
				//get invalidate RECT. just redraw the dirty are from the grid
		        Rect rect = canvas.getClipBounds();
				int left = (int) (rect.left/width_of_position)+1;
				int top = (int) (rect.top/height_of_position)+1;
				//paint.setColor(Color.GREEN);
				//canvas.drawRect(rect, paint);

			}*/

	}
	private void paintPossibleDestinations(ArrayList<int[]> destinations, Canvas canvas){

		for (int[] pos : destinations) {
			int x=(int) (pos[0] * width_of_position)+2;
			int y=(int) (pos[1] * width_of_position)+3;
			int x1=(int) ((pos[0] +1)* width_of_position);
			int y1=(int) ((pos[1] +1)* width_of_position);

	        /* paint a cyan circle inside */
		    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.CYAN);
			paint.setAlpha(125);
			// Rect rect = new Rect(x,y,x1,y1);
			//canvas.drawRect(rect, paint);
            
			canvas.drawCircle(x+width_of_position/2, y+width_of_position/2, (float) (width_of_position*0.4), paint);

			
			/*PAINT A RED DASHED CIRCLE*/
			 paint.setColor(Color.RED);
             DashPathEffect dashPath = new DashPathEffect(new float[]{5,5}, (float)1.0);

             paint.setPathEffect(dashPath);
             paint.setStrokeWidth((float) (width_of_position*0.06));
             paint.setStyle(Style.STROKE);
             canvas.drawCircle(x+width_of_position/2, y+width_of_position/2, (float) (width_of_position*0.4), paint);
		}
	}
	private void drawPegCount (Canvas canvas){
		
		/* PAINT */
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		paint.setColor(getResources().getColor(R.color.textColor));
		paint.setStrokeWidth(2);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(20);
		canvas.drawText("Pegs: "+Integer.toString(game.getPegCount()), (float) ((6.1)*width_of_position), (float) ((6.3)*width_of_position), paint);
		canvas.drawText("Moves: "+Integer.toString(game.getMoveCount()), (float) (6.1*width_of_position), (float) ((6.6)*width_of_position), paint);


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
				
				xIndex=(int) (x/width_of_position);
				yIndex=(int) (y/height_of_position);
				if (xIndex>6 || yIndex>6) {
					return super.onTouchEvent(event);

				}
				
				//if tries to select an empty position and no previous peg selected
				if (game.getGrid()[xIndex][yIndex]==0 && game.getSelectionModeOn()==false) {
					Toast.makeText(this.getContext(), "EMPTY" , Toast.LENGTH_LONG).show();
				// if tries to click on the invisible positions of the grid (-1)
				}else if (game.getGrid()[xIndex][yIndex]==-1) {
					return super.onTouchEvent(event);
				}// if we select a peg and is no other is selected
				else if (game.getSelectionModeOn()==false) {
					/* set that position to SELECTED on grid, mark that selectedPosition is != -1, set the pivot to x, y */
					game.select(xIndex, yIndex);
					game.setSelectionModeOn(true);
					game.setPivot(xIndex,yIndex);
						
					if (game.getGrid()[xIndex][yIndex]==-1)
						return super.onTouchEvent(event);
					//Toast.makeText(this.getContext(), "x,y="+Integer.toString(game.getPivotX())+" "+Integer.toString(game.getPivotY()) , Toast.LENGTH_LONG).show();

					//invalidatePosition(xIndex, yIndex);
					invalidatePosition(game.getPivotX(), game.getPivotY());
					
					//TODO redraw the 5x5 grid around the selection
					Rect position = new Rect(0, 0, (int) (width_of_position*SIZE), (int) (width_of_position*SIZE));
					invalidate(position);


				}// if we select a peg and another is already selected
				else if (game.getSelectionModeOn()!=false){
					
					//select the same peg resets
					if (game.getGrid()[xIndex][yIndex]==2){
						game.setGameValue(xIndex, yIndex, 1);
						game.getGrid()[game.getPivotX()][game.getPivotY()]=1; 
						invalidatePosition(game.getPivotX(), game.getPivotY());
						game.setSelectionModeOn(false);
						//TODO redraw the 5x5 grid around the selection
						Rect position = new Rect(0, 0, (int) (width_of_position*SIZE), (int) (width_of_position*SIZE));
						invalidate(position);
						
						return super.onTouchEvent(event);

					}
					else if (game.getGrid()[xIndex][yIndex]==-1){
						game.setSelectionModeOn(false);
						game.getGrid()[game.getPivotX()][game.getPivotY()]=1; 
						invalidatePosition(game.getPivotX(), game.getPivotY());
						return super.onTouchEvent(event);
					}

					//game.setPivot(-1,-1);

					else if (game.validMove(game.getPivotX(),game.getPivotY(),xIndex ,yIndex)) {

						game.play(game.getPivotX(),game.getPivotY(),xIndex ,yIndex);
						//int[] destination=new int[2];
						//destination[0]=xIndex;
						//destination[1]=yIndex;
						//game.setDestination(destination);
						//game.getGrid()[game.getPivotX()][game.getPivotY()]=0;
						
						
						/*invalidate original position of the peg, the new position and the number of pegs*/
						invalidatePosition(game.getPivotX(), game.getPivotY());
						invalidatePosition(xIndex, yIndex);
						invalidatePegCount();
						
						game.setSelectionModeOn(false);
						game.setPivot(-1, -1);


					}
					else{
						//Toast.makeText(this.getContext(), "NOT VALID" , Toast.LENGTH_LONG).show();
						
						Animation animation=AnimationUtils.loadAnimation(context,R.anim.shake);
						this.startAnimation(animation);
						invalidatePosition(game.getPivotX(), game.getPivotY());
						
						Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
						// Vibrate for 300 milliseconds
						v.vibrate(300);


						game.getGrid()[game.getPivotX()][game.getPivotY()]=1; 
						game.setSelectionModeOn(false);




					}
					game.setSelectionModeOn(false);
					
					if (game.isWon())
						Toast.makeText(this.getContext(),
								"Player " + game.getCurrentPlayer() + " is the winner",
								Toast.LENGTH_LONG).show();
					else if (game.isDrawn())
						Toast.makeText(this.getContext(), "Game on draw", Toast.LENGTH_LONG)
								.show();
					//game.switchCurrentPlayer();
					
				}
				break;
		}//end case
		return super.onTouchEvent(event);
		
	}
	
	
	private void invalidatePosition(int xIndex, int yIndex) {
		int left = (int) (xIndex*width_of_position);
		int top = (int) (yIndex*height_of_position);
		int right = (int) (xIndex*width_of_position+width_of_position ); 
		int bottom = (int) (yIndex*height_of_position+height_of_position );

		Rect position = new Rect(left, top, right, bottom);
		invalidate(position);
	}
	
	private void invalidatePegCount() {
		int left = (int) (5*width_of_position);
		int top = (int) (6*height_of_position);
		int right = (int) (6* width_of_position+width_of_position); 
		int bottom = (int) (6 *height_of_position +width_of_position);

		Rect position = new Rect(left, top, right, bottom);
		invalidate(position);
		}

	
}
