package com.es.uam.eps.dadm.mario_pantoja;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends Activity implements OnClickListener {

	Board board;
	
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

	int[][] tablero = new int[7][7];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		board = new Board();

		/*Link Buttons to listener*/
		registerListeners();
		
		/*Create buttons from int[][] matrix values*/
		setStartFigure();
		
		/*Redundant*/
		setCurrentBoard();
		log("CREATED");
	}

	public void onSaveInstanceState(Bundle outState) {
		//outState.putIntArray("ESTADO", estado);
		super.onSaveInstanceState(outState);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		//estado = savedInstanceState.getIntArray("ESTADO");
		Button button;

		for (int i = 0; i < ids.length; i++) {
			button = (Button) findViewById(ids[i]);
			if (button != null)
				button.setText(R.string.on);
		}
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
					button.setText(Integer.toString(board.getBoard()[x][y]));
					button.drawOn();
			}


		}
		if (board.getType()==Board.INGLES) {
			button = (PegSolitaireButton) findViewById(R.id.pos2_6);
			button.setVisibility(Button.INVISIBLE);
			button = (PegSolitaireButton) findViewById(R.id.pos2_2);
			button.setVisibility(Button.INVISIBLE);
			button = (PegSolitaireButton) findViewById(R.id.pos6_2);
			button.setVisibility(Button.INVISIBLE);
			button = (PegSolitaireButton) findViewById(R.id.pos6_6);
			button.setVisibility(Button.INVISIBLE);

		}

	}
	private void setCurrentBoard() {
		PegSolitaireButton button;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				button = (PegSolitaireButton) findViewById(fromPositionToId(i, j));
				if ((button != null)){
					button.setText(Integer.toString(board.getBoard()[i][j]));
					if(board.getBoard()[i][j]==0){
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
		
		if (!board.isActive()) {
			return;
		}
		
		if(board.getSelectionMode()==-1){
			// get the x and y from the ID of the button
			x=fromIdtoPositionX(v.getId());
			y=fromIdtoPositionY(v.getId());
			
			// if the possition is different from blank==0 state
			if(board.getBoard()[x][y]!=0){
			//v.setBackgroundColor(Color.RED);
			button.drawSelected();
				Toast.makeText(this, "Origen: x , y = "+Integer.toString(x)+" , "+Integer.toString(y), Toast.LENGTH_SHORT).show();
				//modo seleccion=idPosicion
				board.setSelectionMode(v.getId());
			}
			else
				Toast.makeText(this,"Vacia", Toast.LENGTH_SHORT).show();

			
			//estaria bien generar un algoritmo que marque las posibles movidas
			//board.showPossiblePositions();
		}
		else
		{	// A button is selected!
			// if the new click is on the same button, leave as it was
			if(v.getId()==board.getSelectionMode()){
				v.setBackgroundColor(Color.LTGRAY);
				board.setSelectionMode(-1);
				button.drawOn();

			}
			else{	
				// the click is on a different button
				// get the x and y from the previous button and the destination button
				int origenX=fromIdtoPositionX(board.getSelectionMode());
				int origenY=fromIdtoPositionY(board.getSelectionMode());
				int destinoX=fromIdtoPositionX(v.getId());
				int destinoY=fromIdtoPositionY(v.getId());
				
				// is the position free?
				if(board.getBoard()[destinoX][destinoY]==0){

					PegSolitaireButton btn;

				
					Toast.makeText(this, "Destino: x , y = "+Integer.toString(destinoX)+" , "+Integer.toString(destinoY), Toast.LENGTH_SHORT).show();

				
					if (origenX==destinoX){
						//mark the board[x][y]= 1
						//button OFF

						//button.drawOff();
						board.setSelectionMode(-1);
						//board.setBoardValue(destinoX, destinoY, 1);
		
						if (origenY<destinoY) {// el destino esta debajo
							for (int i = origenY; i <= destinoY; i++) {
								btn = (PegSolitaireButton) findViewById(fromPositionToId(origenX, i));
								
								board.setBoardValue(origenX,i, 0);
							}
							btn = (PegSolitaireButton) findViewById(fromPositionToId(origenX, origenY));
							board.setBoardValue(origenX,origenY, 0);
							board.setBoardValue(destinoX,destinoY, 1);
						}else // el destino esta arriba
						{
							for (int i = destinoY; i <= origenY; i++) {
								btn = (PegSolitaireButton) findViewById(fromPositionToId(origenX, i));
								
								board.setBoardValue(origenX,i, 0);
							}
							btn = (PegSolitaireButton) findViewById(fromPositionToId(origenX, origenY));
							board.setBoardValue(origenX,origenY, 0);
							board.setBoardValue(destinoX,destinoY, 1);	
						}


					}
					else if (origenX!=destinoX && origenY==destinoY){
						//mark the board[x][y]= 1
						board.setSelectionMode(-1);
						//board.setBoardValue(destinoX, destinoY, 1);
						if (origenX<destinoX) {// el destino esta debajo
							for (int i = origenX; i <= destinoX; i++) {
								btn = (PegSolitaireButton) findViewById(fromPositionToId(i, origenY));
								board.setBoardValue(i,origenY, 0);	
	
							}
							btn = (PegSolitaireButton) findViewById(fromPositionToId(origenX, origenY));
							//btn.drawOn();
							board.setBoardValue(origenX,origenY, 0);
							board.setBoardValue(destinoX,destinoY, 1);
						}
						else{
							for (int i = destinoX; i <= origenX; i++) {
								btn = (PegSolitaireButton) findViewById(fromPositionToId(i, origenY));
								board.setBoardValue(i,origenY, 0);	
	
							}
							btn = (PegSolitaireButton) findViewById(fromPositionToId(origenX, origenY));
							//btn.drawOn();
							board.setBoardValue(origenX,origenY, 0);
							board.setBoardValue(destinoX,destinoY, 1);
						}

					}
					else{

							Toast.makeText(this,"En diagonal no se puede", Toast.LENGTH_SHORT).show();

							
							
					}

				}
				else Toast.makeText(this,"Vacia", Toast.LENGTH_SHORT).show();

					
		
			}
			setCurrentBoard();

		}// end else selectionMode
	
	
		
		/* si hay una sola pieza restante, ganamos. */
		
		
		//button.setClickable(false);
		currentPlayer = currentPlayer == 0 ? 1 : 0;
		/*
		 * Seleccionar posicion y entrar modo salto
		 * 
		 * 
		 * calcular las posibles direcciones de salto
		 * 
		 * posiciones libres del tablero y ver si estan en la misma columna o
		 * fila del id de la posicion seleccionada
		 * 
		 * 
		 * si se presiona de nuevo, se deselecciona
		 * 
		 * si se presiona una posicion valida saltar eliminar
		 */
		
		
//		if (button.getId() == R.id.pos4_6) {
//			Button button_aux = (Button) findViewById(R.id.pos4_4);
//			button_aux.setText(R.string.on);
//			button_aux = (Button) findViewById(R.id.pos4_5);
//			button_aux.setText(R.string.off);
//	
//
//		}


		// lo suyo seria definir:
		// clase posicion
		// clase ficha
		// clase jugador
		// clase accion

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
	//TODO
	public Boolean validMove(int x0, int y0, int x, int y) {
		if(x0==x){
			if (y0<y) {
				boolean flag = true;
				int first = 1;
				for(int i = x0; i < x && flag; i++)
				{
				  if (board.getBoard()[x][i] != first) flag = false;
				}
				return flag;
			}else{
				boolean flag = true;
				int first = 1;
				for(int i = x; i < x0 && flag; i++)
				{
				  if (board.getBoard()[x][i] != first) flag = false;
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
