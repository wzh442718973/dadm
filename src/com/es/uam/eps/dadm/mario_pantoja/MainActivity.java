package com.es.uam.eps.dadm.mario_pantoja;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.util.Log;
/**
 * @author marioandrei
 * CONTROLLER
 */
public class MainActivity extends Activity {


	private void log(String text) {
		Log.d("LifeCycleTest", text);
	}


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
		
		/* Initialize board with values from game*/
		log("CREATED");
	}

	public void onSaveInstanceState(Bundle outState) {
		//outState.putIntArray("ESTADO", game);
		//outState.putIntArray("CURRENT_STATE", currentBoardState);
		super.onSaveInstanceState(outState);
	}
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		//currentBoardState = savedInstanceState.getIntArray("CURRENT_STATE");
		//arrayToMatrix(currentBoardState);
		//Button button;

		//TODO save number of pegs left.
		
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

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


}
