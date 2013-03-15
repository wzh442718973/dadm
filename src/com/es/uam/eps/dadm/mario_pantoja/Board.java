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
public class Board extends View {
    private Game game;
    
    private final int SIZE = 7;
    private float height_of_position;
    private float width_of_position;
    
    
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
		if (game.getDestination()[0]==-1) {
			drawPegs(canvas);

		}
		else{
			drawPeg(canvas);
			drawInvalidate(canvas);

		}

		/* DRAW the CURRENT PEG NUMBER */
		drawPegCount(canvas);
	}
	
	private void drawInvalidate(Canvas canvas) {
        Rect r = canvas.getClipBounds();
	    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    paint.setARGB(61, 255, 255, 5);
//	    paint.setColor(Color.RED);
	    canvas.drawRect(r, paint);
	}
	//TODO optimize
	private void drawPegs (Canvas canvas){
		
	    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    /* PAINT 

	    paint.setStrokeWidth(10);
		paint.setTextSize(height_of_position * 0.70f);
		paint.setTextScaleX(getWidth() / getHeight());
		paint.setTextAlign(Paint.Align.CENTER);*/
        //Rect r = canvas.getClipBounds();
			/* PAINT BOARD FROM GAME MATRIX */
			for (int i = 0; i < SIZE; i++)
				for (int j = 0; j < SIZE; j++) {
					if (game.getGrid()[i][j]==1) {
						/* paint ON */
						Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.on);
						paint.setColor(Color.BLACK);
				        canvas.drawBitmap(bitmap, i*width_of_position, j*width_of_position, paint);

					} else if (game.getGrid()[i][j]==0) {
						Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.off);
						paint.setColor(Color.BLACK);
				        canvas.drawBitmap(bitmap, i*width_of_position, j*width_of_position, paint);
						/* paint OFF*/

					} else if (game.getGrid()[i][j]==2) {
						Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.selected);
						paint.setColor(Color.BLACK);
				        canvas.drawBitmap(bitmap, i*width_of_position, j*width_of_position, paint);
						/* paint SELECTED*/

					}
				}

	}
	private void drawPegCount (Canvas canvas){
		
		/* PAINT */
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

//		paint.setStrokeWidth(10);
	//	paint.setTextSize(height_of_position * 0.70f);
		//paint.setTextScaleX(getWidth() / getHeight());
	//	paint.setTextAlign(Paint.Align.CENTER);


//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.selected);
//		paint.setColor(Color.BLACK);
//		canvas.drawBitmap(bitmap, 1*width_of_position, 5*width_of_position, paint);

		paint.setColor(getResources().getColor(R.color.textColor));
		paint.setStrokeWidth(2);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(20);
		canvas.drawText("Pegs: "+Integer.toString(game.getPegCount()), (float) ((6.1)*width_of_position), (float) ((6.3)*width_of_position), paint);
		canvas.drawText("Moves: "+Integer.toString(game.getMoveCount()), (float) (6.1*width_of_position), (float) ((6.6)*width_of_position), paint);


	}

private void drawPeg (Canvas canvas){
		
	    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	


				if (game.getGrid()[game.getDestination()[0]][game.getDestination()[1]]==1) {
					/* paint ON */
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.on);
					paint.setColor(Color.BLACK);
			        canvas.drawBitmap(bitmap, game.getDestination()[0]*width_of_position, game.getDestination()[1]*width_of_position, paint);

				} else if (game.getGrid()[game.getDestination()[0]][game.getDestination()[1]]==0) {
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.off);
					paint.setColor(Color.BLACK);
			        canvas.drawBitmap(bitmap, game.getDestination()[0]*width_of_position, game.getDestination()[1]*width_of_position, paint);
					/* paint OFF*/

				} else if (game.getGrid()[game.getDestination()[0]][game.getDestination()[1]]==2) {
					Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.selected);
					paint.setColor(Color.BLACK);
			        canvas.drawBitmap(bitmap, game.getDestination()[0]*width_of_position, game.getDestination()[1]*height_of_position, paint);
					/* paint SELECTED*/
			        }
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

				/*if (game.getType()==Game.ENGLISH) {
					if ((xIndex<2 && yIndex<2) || (xIndex>4 && yIndex>4) || (xIndex>4 && yIndex<2) || (xIndex<2 && yIndex>4)) {
						return super.onTouchEvent(event);
					}

				}*/
				//if tries to select an empty position and no previous peg selected
				if (game.getGrid()[xIndex][yIndex]==0 && game.getSelectionModeOn()==false) {
					Toast.makeText(this.getContext(), "EMPTY" , Toast.LENGTH_LONG).show();
				// if tries to click on the invisible positions of the grid (-1)
				}else if (game.getGrid()[xIndex][yIndex]==-1) {
					return super.onTouchEvent(event);
				}// if we select a peg and is no other is selected
				else if (game.getSelectionModeOn()==false) {
					/* set that position to SELECTED on grid, mark that selectedPosition is != -1, set the pibot to x, y */
					game.select(xIndex, yIndex);
					game.setSelectionModeOn(true);
					game.setPivot(xIndex,yIndex);
					//game.setSelectedPosition(1);
					
	
					if (game.getGrid()[xIndex][yIndex]==-1)
						return super.onTouchEvent(event);
					//Toast.makeText(this.getContext(), "x,y="+Integer.toString(xIndex)+" "+Integer.toString(yIndex) , Toast.LENGTH_LONG).show();

					//invalidatePosition(xIndex, yIndex);

				}// if we select a peg and another is already selected
				else if (game.getSelectionModeOn()!=false){
					
					if (game.getGrid()[xIndex][yIndex]==2)
						game.setGameValue(xIndex, yIndex, 1);
					
					if (game.getGrid()[xIndex][yIndex]==-1)
						return super.onTouchEvent(event);
					

					//game.setPivot(-1,-1);

					if (game.validMove(game.getPivotX(),game.getPivotY(),xIndex ,yIndex)) {

						game.play(game.getPivotX(),game.getPivotY(),xIndex ,yIndex);
						int[] destination=new int[2];
						destination[0]=xIndex;
						destination[1]=yIndex;

						game.setDestination(destination);
						game.getGrid()[game.getPivotX()][game.getPivotY()]=0;

					}
					else{
						//Toast.makeText(this.getContext(), "NOT VALID" , Toast.LENGTH_LONG).show();
						
						
						game.getGrid()[game.getPivotX()][game.getPivotY()]=1; 

					}
					/*invalidate original position of the peg, the new position and the number of pegs*/
					invalidatePosition(game.getPivotX(), game.getPivotY());
					invalidatePosition(xIndex, yIndex);
					invalidatePegCount();

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
