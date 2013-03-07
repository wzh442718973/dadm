package com.es.uam.eps.dadm.mario_pantoja;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
/**
 * @author marioandrei
 * CONTROLLER
 */
public class MainActivity extends Activity implements OnClickListener {

	Game game;
	private int[] currentBoardState=new int[49];
	
	private void log(String text) {
		Log.d("LifeCycleTest", text);
	}

	private int currentPlayer = 0;
	private int ids[] = { 
			0, 			 0,  		  R.id.pos1_3, R.id.pos1_4, R.id.pos1_5, 0, 		  0, 
			0,			 R.id.pos2_2, R.id.pos2_3, R.id.pos2_4, R.id.pos2_5, R.id.pos2_6, 0,
			R.id.pos3_1, R.id.pos3_2, R.id.pos3_3, R.id.pos3_4, R.id.pos3_5, R.id.pos3_6, R.id.pos3_7, 
			R.id.pos4_1, R.id.pos4_2, R.id.pos4_3, R.id.pos4_4, R.id.pos4_5, R.id.pos4_6, R.id.pos4_7, 
			R.id.pos5_1, R.id.pos5_2, R.id.pos5_3, R.id.pos5_4, R.id.pos5_5, R.id.pos5_6, R.id.pos5_7, 
			0, 			 R.id.pos6_2, R.id.pos6_3, R.id.pos6_4, R.id.pos6_5, R.id.pos6_6, 0,
			0, 			 0, 		  R.id.pos7_3, R.id.pos7_4, R.id.pos7_5, 0, 		  0 };

	private int column1[] = { 0, 0, R.id.pos1_3, R.id.pos1_4, R.id.pos1_5, 0, 0 };
	private int column2[] = { 0, R.id.pos2_2, R.id.pos2_3, R.id.pos2_4, R.id.pos2_5, R.id.pos2_6, 0, };
	private int column3[] = {R.id.pos3_1, R.id.pos3_2, R.id.pos3_3, R.id.pos3_4, R.id.pos3_5, R.id.pos3_6, R.id.pos3_7 };
	private int column4[] = {R.id.pos4_1, R.id.pos4_2, R.id.pos4_3, R.id.pos4_4, R.id.pos4_5, R.id.pos4_6, R.id.pos4_7 };
	private int column5[] = {R.id.pos5_1, R.id.pos5_2, R.id.pos5_3, R.id.pos5_4, R.id.pos5_5, R.id.pos5_6, R.id.pos5_7 };
	private int column6[] = { 0, R.id.pos6_2, R.id.pos6_3, R.id.pos6_4,	R.id.pos6_5, R.id.pos6_6, 0 };
	private int column7[] = { 0, 0, R.id.pos7_3, R.id.pos7_4, R.id.pos7_5, 0, 0 };


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*  
		 * 	cargar un menu donde elegir:
		 * 		tipo de tablero
		 * 		nombre jugador
		 * 		abrir partida guardada.
		 * */
		
		
		setContentView(R.layout.activity_main);
		game = new Game(Game.FRANCES);

		/*Link Buttons to listener*/
		registerListeners();
		
		/*Create buttons from int[][] matrix values*/
		setStartFigure();
		
		/*initializate board with values from game*/
		setCurrentBoard();
		log("CREATED");
	}

	public void onSaveInstanceState(Bundle outState) {
		//outState.putIntArray("ESTADO", game);
		this.matrixToArray(game.getGame());
		outState.putIntArray("CURRENT_STATE", currentBoardState);
		super.onSaveInstanceState(outState);
	}
	public  void arrayToMatrix(int[] array) {
	    for (int i = 0; i < array.length; i++) 
	    	//this.game.getGame()[i/7][i%7] = array[i];
	    	this.game.setGameValue(i/7, i%7, array[i]);
	}
	
	public  void matrixToArray(int[][] matrix) {
	    int k=0;
		for (int i = 0; i < 7; i++)
	    	for (int j = 0; j < 7; j++) {
	    		currentBoardState[k]=matrix[i][j];
				k++;
			}

	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentBoardState = savedInstanceState.getIntArray("CURRENT_STATE");
		arrayToMatrix(currentBoardState);
		//Button button;

		//TODO save number of pegs left.
		
		setCurrentBoard();
	}

	public void onStart() {
		super.onStart();
		log("STARTED");

	}

	protected void onResume() {
		super.onResume();
		log("RESUMED");
		//Music.play(this, R.raw.moog);
	}

	protected void onPause() {
		super.onPause();
		log("PAUSE");
		//Music.stop(this);

	}

	protected void onStop() {
		super.onStop();
		log(" STOP");

	}

	protected void onRestart() {
		super.onRestart();
		log("Restarted");

	}

	protected void onDestroy() {
		super.onDestroy();
		log("Destroy");

	}

	public void selectPosition(View v) {
		// Toast.makeText(this, "On Click", Toast.LENGTH_LONG).show();

	}

	private void setStartFigure() {
		PegSolitaireButton button;
		int x=-1, y=-1;
		for (int i = 0; i < ids.length; i++) {
			x=fromIdtoPositionX(ids[i]);
			y=fromIdtoPositionY(ids[i]);
			
			button = (PegSolitaireButton) findViewById(ids[i]);
			if ((button != null)){
					//button.setText(Integer.toString(game.getGame()[x][y]));
					button.drawOn();
					//TODO
					Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha); 
					button.startAnimation(animation);
			}


		}
		if (game.getType()==Game.INGLES) {
			button = (PegSolitaireButton) findViewById(R.id.pos2_6);
			button.setVisibility(Button.INVISIBLE);
			button = (PegSolitaireButton) findViewById(R.id.pos2_2);
			button.setVisibility(Button.INVISIBLE);
			button = (PegSolitaireButton) findViewById(R.id.pos6_2);
			button.setVisibility(Button.INVISIBLE);
			button = (PegSolitaireButton) findViewById(R.id.pos6_6);
			button.setVisibility(Button.INVISIBLE);

		}
		TextView number_pegs=(TextView) findViewById(R.id.text_view_num_pegs);
		number_pegs.setText(Integer.toString(game.getPeg_count()));
	}
	private void setCurrentBoard() {
		PegSolitaireButton button;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				button = (PegSolitaireButton) findViewById(fromPositionToId(i, j));
				if ((button != null)){
					//button.setText(Integer.toString(game.getGame()[i][j]));
					if(game.getGame()[i][j]==0){
						button.drawOff();
					}
					else
						button.drawOn();
				}
			}
		}
	}

	public void onClick(View v) {
		PegSolitaireButton button = (PegSolitaireButton) v;
		int x,y;
		
		setCurrentBoard();
		if (!game.isActive()) {
			return;
		}
		/* IF NO BUTTON SELECTED */
		if(game.getSelectionMode()==-1){
			// get the x and y from the ID of the button
			x=fromIdtoPositionX(v.getId());
			y=fromIdtoPositionY(v.getId());
			
			// if the possition is different from blank==0 state
			if(game.getGame()[x][y]!=0){
			
				button.drawSelected();
				//Toast.makeText(this, "Origen: x , y = "+Integer.toString(x)+" , "+Integer.toString(y), Toast.LENGTH_SHORT).show();
				//Selection Mode off==-1 on==idPosicion
				game.setSelectionMode(v.getId());

			}
			else
				Toast.makeText(this,"Vacia", Toast.LENGTH_SHORT).show();
			//estaria bien generar un algoritmo que marque las posibles movidas
			//game.showPossiblePositions();
		}
		/* IF BUTTON SELECTED */
		else
		{	// if the new click is on the same button, leave as it was
			if(v.getId()==game.getSelectionMode()){
				game.setSelectionMode(-1);
				button.drawOn();
				//Toast.makeText(this,"UNSELECTED", Toast.LENGTH_SHORT).show();
			}
			else{	
				// the click is on a different button
				// get the x and y from the previous button and the destination button
				int origenX=fromIdtoPositionX(game.getSelectionMode());
				int origenY=fromIdtoPositionY(game.getSelectionMode());
				
				int destinoX=fromIdtoPositionX(v.getId());
				int destinoY=fromIdtoPositionY(v.getId());
				
				button.drawOn();
				
				
				// the position is free and it's a valid move
				if(game.getGame()[destinoX][destinoY]==0 && validMove(origenX, origenY, destinoX, destinoY)){
					PegSolitaireButton btn;
					if (origenX==destinoX){ // same column
						//if (validMove(origenX, origenY, destinoX, destinoY)) {
						//	Toast.makeText(this, "Destino: x , y = "+Integer.toString(destinoX)+" , "+Integer.toString(destinoY)+" VALIDO", Toast.LENGTH_SHORT).show();

						//}
						game.setSelectionMode(-1);
						//game.setGameValue(destinoX, destinoY, 1);
		
						if (origenY<destinoY) {// el destino esta arriba
							for (int i = origenY; i < destinoY; i++) {
								//TODO
								//btn = (PegSolitaireButton) findViewById(fromPositionToId(destinoX, i));
								game.setGameValue(origenX,i, 0);
								Toast.makeText(this, "Destino: x , y = "+Integer.toString(destinoX)+" , "+Integer.toString(i)+" es "+Integer.toString(game.getGame()[origenX][i]), Toast.LENGTH_SHORT).show();
								//btn.drawOff();
							}
							btn = (PegSolitaireButton) findViewById(fromPositionToId(origenX, origenY));
							//btn.drawOff();
							game.setGameValue(origenX,origenY, 0);
							game.setGameValue(destinoX,destinoY, 1);
							
						}else if (origenY>destinoY)// el destino esta abajo
						{
							for (int i = origenY; i > destinoY; i--) {
								//btn = (PegSolitaireButton) findViewById(fromPositionToId(destinoX, i));
								game.setGameValue(origenX,i, 0);
							//	btn.drawOff();
								//Toast.makeText(this, "Destino: x , y = "+Integer.toString(destinoX)+" , "+Integer.toString(i)+" a 0", Toast.LENGTH_SHORT).show();

							}
							btn = (PegSolitaireButton) findViewById(fromPositionToId(origenX, origenY));
							//btn.drawOff();
							game.setGameValue(origenX,origenY, 0);
							game.setGameValue(destinoX,destinoY, 1);

						}
					} // different column same row
					else if (origenX!=destinoX && origenY==destinoY){
						game.setSelectionMode(-1);
						button.drawOn();

						if (origenX<destinoX) { // destino esta a la derecha
							for (int i = origenX; i <= destinoX; i++) {
								btn = (PegSolitaireButton) findViewById(fromPositionToId(i, origenY));
								game.setGameValue(i,origenY, 0);
								//btn.drawOff();
	
							}
							btn = (PegSolitaireButton) findViewById(fromPositionToId(origenX, origenY));
							//btn.drawOff();
							game.setGameValue(origenX,origenY, 0);
							game.setGameValue(destinoX,destinoY, 1);

						}
						else if (origenX>destinoX){ // destino  esta a la izquierda
							for (int i = origenX; i >= destinoX; i--) {
								btn = (PegSolitaireButton) findViewById(fromPositionToId(i, origenY));
								game.setGameValue(i,origenY, 0);
							}
							btn = (PegSolitaireButton) findViewById(fromPositionToId(origenX, origenY));
							//btn.drawOff();
							game.setGameValue(origenX,origenY, 0);
							game.setGameValue(destinoX,destinoY, 1);
						}
					}//end if same row
					else{
							Toast.makeText(this,"En diagonal no se puede", Toast.LENGTH_SHORT).show();
							game.setSelectionMode(-1);					
					}

				}
				else {
					Toast.makeText(this,"Movimiento No Valido", Toast.LENGTH_SHORT).show();
					game.setSelectionMode(-1);
					button.drawOn();
				}
				game.setSelectionMode(-1);					
			}// END IF - DESTIONATION CLICKED
			
			/*UPDATE BOARD FROM GAME MATRIX */
			game.updatePegCount();
			setCurrentBoard();
			
			/*UPDATE PEG COUNT */
			TextView number_pegs=(TextView) findViewById(R.id.text_view_num_pegs);
			number_pegs.setText(Integer.toString(game.getPeg_count()));

		}// end else selectionMode
	
	
		
		/* si hay una sola pieza restante, ganamos. */
		
		
		//button.setClickable(false);
		currentPlayer = currentPlayer == 0 ? 1 : 0;


	}

	private void registerListeners() {

		PegSolitaireButton button;

		for (int i = 0; i < ids.length; i++) {

			button = (PegSolitaireButton) findViewById(ids[i]);
			if (button != null) {
				button.setOnClickListener(this);
			}
		}
	}

	private int fromPositionToId(int x, int y) {
		switch (x) {
		case 0:
			return column1[y ];
		case 1:
			return column2[y ];
		case 2:
			return column3[y ];
		case 3:
			return column4[y ];
		case 4:
			return column5[y ];
		case 5:
			return column6[y];
		case 6:
			return column7[y];
		default:
			break;
		}
		return -1;
	}
	
	private int fromIdtoPositionX(int id) {
		for (int i = 0; i < ids.length; i++) {
			if (id==ids[i]) {
				return i / 7;
			}
		}
		return -1;
	}
	private int fromIdtoPositionY(int id) {
		int j=0;
		for (int i = 0; i < ids.length; i++) {
		
				j=i % 7;
			if (id==ids[i]) {
				return j;
			}
			
		}
		return -1;
	}

	public Boolean validMove(int x0, int y0, int x, int y) {
		if(x0==x){
			if (y0<y) {
				boolean flag = true;
				/*destination must be 1 position further*/
				if (y==y0+1) {
					return false;
				}
				for(int i = y0; i < y && flag; i++)
				{
				  if (game.getGame()[x][i] != 1) 
					  flag = false;
				}

				return flag;
			}else{
				boolean flag = true;
				if (y0==y+1) {
					return false;
				}
				for(int i = y0; i > y && flag; i--)
				{
				  if (game.getGame()[x][i] != 1) 
					  flag = false;
				}
				return flag;
			}
	
			
		}else if(y0==y){
			if (x0<x) {
				boolean flag = true;
				if (x==x0+1) {
					return false;
				}
				int first = 1;
				for(int i = x0; i < x && flag; i++)
				{
				  if (game.getGame()[i][y] != first) flag = false;
				}
				return flag;
			}else{
				boolean flag = true;
				if (x0==x+1) {
					return false;
				}
				for(int i = x0; i > x && flag; i--)
				{
				  if (game.getGame()[i][y] != 1) flag = false;
				}

				return flag;
			}
			
		}
		else
			return false;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
