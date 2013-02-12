package com.es.uam.eps.dadm.mario_pantoja;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;


public class MainActivity extends Activity implements OnClickListener {

	private void log (String text){
		Log.d ("LifeCycleTest", text);
	}
	private int currentPlayer =0;
	private String[] symbol = new String[]{"x","o"};
	private int ids[] ={
			R.id.pos1_3,
			R.id.pos1_4,
			R.id.pos1_5,
			R.id.pos2_3,
			R.id.pos2_4,
			R.id.pos2_5,
			R.id.pos3_1,
			R.id.pos3_2,
			R.id.pos3_3,
			R.id.pos3_4,
			R.id.pos3_5,
			R.id.pos3_6,
			R.id.pos3_7,
			R.id.pos4_1,
			R.id.pos4_2,
			R.id.pos4_3,
			R.id.pos4_4,
			R.id.pos4_5,
			R.id.pos4_6,
			R.id.pos4_7,
			R.id.pos5_1,
			R.id.pos5_2,
			R.id.pos5_3,
			R.id.pos5_4,
			R.id.pos5_5,
			R.id.pos5_6,
			R.id.pos5_7,
			R.id.pos6_3,
			R.id.pos6_4,
			R.id.pos6_5,
			R.id.pos7_3,
			R.id.pos7_4,
			R.id.pos7_5};
	
	private int columna1[] ={
			R.id.pos1_3,
			R.id.pos1_4,
			R.id.pos1_5,
			};
	private int columna2[] ={

			R.id.pos2_3,
			R.id.pos2_4,
			R.id.pos2_5
			};
	private int columna3[] ={

			R.id.pos3_1,
			R.id.pos3_2,
			R.id.pos3_3,
			R.id.pos3_4,
			R.id.pos3_5,
			R.id.pos3_6,
			R.id.pos3_7};
	
	private int columna4[] ={

			R.id.pos4_1,
			R.id.pos4_2,
			R.id.pos4_3,
			R.id.pos4_4,
			R.id.pos4_5,
			R.id.pos4_6,
			R.id.pos4_7};
	
	private int estado[]= new int[]{1,2,3};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		registerListeners();
		setStartFigure();
		
		log("created");
	}
	
	public void onSaveInstanceState(Bundle outState){
		outState.putIntArray("ESTADO", estado);
		super.onSaveInstanceState(outState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		estado=savedInstanceState.getIntArray("ESTADO");
		Button button;
		
		for (int i = 0; i < ids.length; i++) {
			button= (Button) findViewById(ids[i]);
			button.setText(R.string.on);
		} 
	}
	public void onStart() {
		super.onStart();
		log("started");
		
	}
	protected void onResume() {
		super.onResume();
		log("RESUMED");
		Music.play(this, R.raw.moog);
	}
	protected void onPause() {
		super.onPause();
		log("PAUSE");
		Music.stop(this);

		
	}
	protected void onStop() {
		super.onStop();
		log(" STOP");
		
	}
	protected void onRestart() {
		super.onRestart();
		log("Restarted");
		
	}
	
	public void selectPosition(View v) {
		//Toast.makeText(this, "On Click", Toast.LENGTH_LONG).show();
		
	}
	
	private void setStartFigure() {
		Button button;
		
		for (int i = 0; i < ids.length; i++) {
			button= (Button) findViewById(ids[i]);
			button.setText(R.string.on);
		}
		button= (Button) findViewById(R.id.pos4_4);
		button.setText(R.string.off);
}
	public void onClick (View v){
		Button button = (Button) v;
		
		button.setText(R.string.off);
		button.setClickable(false);
		currentPlayer= currentPlayer == 0 ? 1:0;
		
		
		/* Aqui deberia ir un metodo que capture los 4 vecinos! */
		 if(button.getId() == R.id.pos4_6)
		 { 
			Button button_aux=(Button) findViewById(R.id.pos4_4);
			button_aux.setText(R.string.on);
			button_aux=(Button) findViewById(R.id.pos4_5);
			button_aux.setText(R.string.off);
			estado[0] =1;
			estado[1] =1;
			estado[2] =1;

		 }

			Toast.makeText(this, v.getId(), Toast.LENGTH_SHORT).show();

			// lo suyo seria definir:
			// clase posicion
			// clase ficha
			// clase jugador
			// clase accion 
				// clase salto?
				
			
			
	}
	
	private void registerListeners(){

		 
		Button button;
		
		for (int i = 0; i < ids.length; i++) {
			button= (Button) findViewById(ids[i]);
			button.setOnClickListener(this);
		}


	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}


