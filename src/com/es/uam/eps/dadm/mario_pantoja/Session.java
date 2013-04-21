package com.es.uam.eps.dadm.mario_pantoja;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.Toast;
import android.widget.Chronometer.OnChronometerTickListener;

/**
 * 
 * @author marioandrei
 * CONTROLLER
 * 
 */
public class Session extends Activity {
	
	public static final int REQUEST_CODE = 1;
	
	private int type;
	
	private String figure;
	
	private Board board;
	
	private Game game=null;
	
	private String playerName="Unknown Player";
	
	private boolean musicOn=false;
	
	long startTime;
    long countUp;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//TODO    Debes anadir iconos a la barra de acci—n y ligarlos a las acciones del menu inicial.

		//SharedPreferences sharedPreferences = getSharedPreferences("figure_pref", MODE_PRIVATE);
		//Toast.makeText(this,"SESSION FIGURE number="+sharedPreferences.getString("figure_pref","DESDE SESSION"),Toast.LENGTH_SHORT).show();				    
		//Toast.makeText(this,"FIGURE ="+Preferences.getFigure(this),Toast.LENGTH_SHORT).show();		
		
	
		getFigureFromPreferences();
		getTypeFromPreferences();
		musicOn();
		//getFigureFromPreferences();
		
		play();	
		
		
	//	ActionBar actionbar=getActionBar(); SDK 11 Needed!
		

        Chronometer stopWatch = (Chronometer) findViewById(R.id.chrono);
        startTime = SystemClock.elapsedRealtime();

        //textGoesHere = (TextView) findViewById(R.id.textGoesHere);
        stopWatch.setOnChronometerTickListener(new OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
                //String asText = (countUp / 60) + ":" + (countUp % 60); 
               // textGoesHere.setText(asText);
                game.setSeconds((int)countUp);
                setPlayerNameFromsetUPreferences();
                game.setCurrentPlayer(playerName);
            }
        });
        
        stopWatch.start();

        

		

	}
	


	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	
	/**
	 * @return the figure
	 */
	public String getFigure() {
		return figure;
	}



	/**
	 * @param figure the figure to set
	 */
	public void setFigure(String figure) {
		this.figure = figure;
	}



	private void getTypeFromPreferences() {
			if	(Preferences.getType(this)==Game.EUROPEAN){
				setType(Game.EUROPEAN);
			}
			else
				setType(Game.ENGLISH);
		
	}

	private void play(){ 
		
		setContentView(R.layout.session);
		
		board = (Board) findViewById(R.id.board); 
		
		//retrieve the game from the created board
		setGame(board.getGame());

	}
	
	
	private void setPlayerNameFromsetUPreferences(){
		
		
		setPlayerName(Preferences.getPlayerName(this)); 
	}
	
	
	private void musicOn(){
		
		
		musicOn=Preferences.playMusic(this); 
	}
	
	private void getFigureFromPreferences(){
		
		
		setFigure(Preferences.getFigure(this)); 
	}
	
	public void quitGame(){
		new AlertDialog.Builder(this)
		.setTitle("Exit")
		.setMessage("Leave this game?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){
	            game.setSeconds((int) countUp);
	            game.newGameEntry();
				finish();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){}
		})
		.show();
        
	}
	
	
	public void restartGame(){
		new AlertDialog.Builder(this)
		.setTitle("You won")
		.setMessage("New game?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){
	            //update the amount of seconds it took to finish the game
	            game.setSeconds((int) countUp);
	            //add to db
	            game.newGameEntry();
	            //send RESULT OK to initial in order to launch a new session
				setResult(RESULT_OK);
				finish();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){
				
	            game.setSeconds((int) countUp);
	            game.newGameEntry();
				finish();
				
				

			}
		})
		.show();
	}
	
	public void restartGameLoser(){
		new AlertDialog.Builder(this)
		.setTitle("You Lost")
		.setMessage("New game?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){
	            game.setSeconds((int) countUp);
	            game.newGameEntry();
				setResult(RESULT_OK);
				finish();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){
				
	            game.setSeconds((int) countUp);
	            game.newGameEntry();
				finish();
				
				

			}
		})
		.show();
	}
	

	
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Closing Current Game")
	        .setMessage("Are you sure you want to leave this match?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {


	        	finish();  
	            
	        }

	    })
	    .setNegativeButton("No", null)
	    .show();

	}
	
	public void onStart() {
		super.onStart();

	}

	protected void onResume() {
		super.onResume();
		

		if (musicOn==true) {
			startService(new Intent(getBaseContext(), SimpleService.class));

		}
	}

	protected void onPause() {
		super.onPause();
		

	}

	protected void onStop() {
		super.onStop();

	}

	protected void onRestart() {
		super.onRestart();

	}

	protected void onDestroy() {
		super.onDestroy();
		//stops music
		stopService(new Intent(getBaseContext(), SimpleService.class));


	}




}
