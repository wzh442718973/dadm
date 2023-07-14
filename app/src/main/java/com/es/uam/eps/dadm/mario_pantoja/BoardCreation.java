package com.es.uam.eps.dadm.mario_pantoja;





import android.view.View;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import android.graphics.Rect;

import android.util.AttributeSet;
import android.view.MotionEvent;



/**
 * @author marioandrei
 * VIEW
 * the board used in creation mode
 */
public class BoardCreation extends View {
    private Game game;
    
    private final int SIZE = 7;
    private float height_of_position;
    private float width_of_position;
    private Bitmap bitmapON,bitmapOFF,bitmapSEL;
	private SessionCreation session;
	private int type = Game.ENGLISH;

    int SOUNDON=1;
    int SOUNDOFF=2;
    int SOUNDMOVE=3;
	Sound sound;

    /**
     * Constructor
     * @param context
     * @param attributes
     */
	public BoardCreation(Context context, AttributeSet attributes) {
		super(context, attributes);
		setFocusable(true);
		setFocusableInTouchMode(true);
		sound=new Sound(context);
		
		
		/*
		 * sound init
		 * 
		 * */

		/* you can set the type of board here, ENGLISH OR EUROPEAN*/		
		SharedPreferences sharedPreferences = context.getSharedPreferences("type", Context.MODE_PRIVATE);
		int type = Preferences.getType(context);
		
		setSession((SessionCreation) context);
		
		//set the desired figure from preferences
				

		game = new Game(context,type);
		
		
		sharedPreferences = context.getSharedPreferences("username", Context.MODE_PRIVATE);
		String username = sharedPreferences.getString("username", " desde Board");
		game.setCurrentPlayer(username);
		
		
		bitmapON = BitmapFactory.decodeResource(getResources(), R.drawable.on);
		bitmapSEL = BitmapFactory.decodeResource(getResources(), R.drawable.selected);
		bitmapOFF = BitmapFactory.decodeResource(getResources(), R.drawable.off);

	}
	
	
	/*sets and gets*/
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the session
	 */
	public SessionCreation getSession() {
		return session;
	}
	/**
	 * @param session the session to set
	 */
	public void setSession(SessionCreation session) {
		this.session = session;
	}
	
	/**
	 * @return
	 */
	public Game getGame() {
		return this.game;
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
		
	
		super.onSizeChanged(width, height, oldWidth, oldHeight);
		
	}


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
		
		game.updatePegCount();


		

	}
	
	/**
	 * paints the pegs on the board
	 * @param canvas
	 */
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


	}
	



	
	public boolean onTouchEvent(MotionEvent event) {

		int xIndex = 0;
	    int yIndex = 0;
	    int action = event.getAction();
		
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				float x = event.getX();
				float y = event.getY();
				
				xIndex=(int) (x/width_of_position);
				yIndex=(int) (y/height_of_position);
				if (xIndex>(SIZE-1) || yIndex>6) {
					return super.onTouchEvent(event);

				}
				
				if (game.getGrid()[xIndex][yIndex]==Game.OFF) {
					game.getGrid()[xIndex][yIndex]=Game.ON; 
					invalidatePosition(xIndex, yIndex);
					sound.playSound(SOUNDON, 1.0f);

					return super.onTouchEvent(event);
				}else if (game.getGrid()[xIndex][yIndex]==Game.INVISIBLE) {
					return super.onTouchEvent(event);
				}else if (game.getGrid()[xIndex][yIndex]==Game.ON) {
					game.getGrid()[xIndex][yIndex]=Game.OFF; 
					invalidatePosition(xIndex, yIndex);
					sound.playSound(SOUNDON, 1.0f);
					return super.onTouchEvent(event);
				}				
				break;
		}//end case
		
		game.updatePegCount();

		return super.onTouchEvent(event);
		
	}
	
	
	
	/**
	 * invalidates the area of a peg on the board
	 * @param xIndex
	 * @param yIndex
	 */
	private void invalidatePosition(int xIndex, int yIndex) {
		int left = (int) (xIndex*width_of_position);
		int top = (int) (yIndex*height_of_position);
		int right = (int) (xIndex*width_of_position+width_of_position ); 
		int bottom = (int) (yIndex*height_of_position+height_of_position );

		Rect position = new Rect(left, top, right, bottom);
		invalidate(position);
	}
	


	
}
