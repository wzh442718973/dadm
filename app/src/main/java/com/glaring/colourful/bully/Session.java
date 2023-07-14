package com.glaring.colourful.bully;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
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
	
	private Chronometer stopWatch;
	
	String oldfigure;
	
	long startTime;
    long countUp;
	
	/**
	 * @return the figure
	 */
	public String getFigure() {
		return figure;
	}
	


	private void getFigureFromPreferences(){
		
		if(Preferences.getOnlineFigure(this).equals("none"))
			setFigure(Preferences.getFigure(this)); 
		else
			setFigure(Preferences.getOnlineFigure(this)); 

	}

	public Game getGame() {
		return game;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getType() {
		return type;
	}

	private void getTypeFromPreferences() {
			if	(Preferences.getType(this)==Game.EUROPEAN){
				setType(Game.EUROPEAN);
			}
			else
				setType(Game.ENGLISH);
		
	}

	private void musicOn(){
		
		
		musicOn=Preferences.playMusic(this); 
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



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
	
		getFigureFromPreferences();
		getTypeFromPreferences();
		musicOn();		
		play();	
		oldfigure=Preferences.getFigureName(this);

		
		
		//ActionBar actionbar=getActionBar(); //SDK 11 Needed!
		

         setStopWatch((Chronometer) findViewById(R.id.chrono));
        startTime = SystemClock.elapsedRealtime();

        //textGoesHere = (TextView) findViewById(R.id.textGoesHere);
        getStopWatch().setOnChronometerTickListener(new OnChronometerTickListener(){
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
        
        getStopWatch().start();
        

		

	}



	protected void onDestroy() {
		super.onDestroy();
		//stops music
		stopService(new Intent(getBaseContext(), SimpleService.class));


	}

	protected void onPause() {
		super.onPause();
		stopService(new Intent(getBaseContext(), SimpleService.class));

		

	}
	
	
	protected void onRestart() {
		super.onRestart();

	}
	
	
	protected void onResume() {
		super.onResume();
		

		if (musicOn==true) {
			startService(new Intent(getBaseContext(), SimpleService.class));

		}
		
		if (!oldfigure.equals(Preferences.getFigureName(this))) {
            //send RESULT OK to initial in order to launch a new session
			setResult(RESULT_OK);
			finish();
		}
	}
	
	public void onStart() {
		super.onStart();

	}
	
	protected void onStop() {
		super.onStop();

	}
	
	
	private void play(){ 
		
		setContentView(R.layout.session);
		
		board = (Board) findViewById(R.id.board); 
		
		//retrieve the game from the created board
		setGame(board.getGame());

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
	            report();
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
	            report();

	            //send RESULT OK to initial in order to launch a new session
				setResult(RESULT_OK);
				finish();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){
				
	            game.setSeconds((int) countUp);
	            game.newGameEntry();
	            report();

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
	            report();

				setResult(RESULT_OK);
				finish();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){
				
	            game.setSeconds((int) countUp);
	            game.newGameEntry();
	            report();

				finish();
				
				

			}
		})
		.show();
	}

	/**
	 * @param figure the figure to set
	 */
	public void setFigure(String figure) {
		this.figure = figure;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	private void setPlayerNameFromsetUPreferences(){
		
		
		setPlayerName(Preferences.getPlayerName(this)); 
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	public void report() {
		
		
        Preferences.setDuration(getApplicationContext(), Integer.toString((int)countUp));
        Preferences.setNumberoftiles(getApplicationContext(), Integer.toString(game.getPegCount()));
        Preferences.setDate(getApplicationContext(), game.getDate());
        
		//Toast.makeText(this," "+game.getDate(),Toast.LENGTH_SHORT).show();				    

	}

	/**
	 * @return the stopWatch
	 */
	public Chronometer getStopWatch() {
		return stopWatch;
	}



	/**
	 * @param stopWatch the stopWatch to set
	 */
	public void setStopWatch(Chronometer stopWatch) {
		this.stopWatch = stopWatch;
	}

	
	public void stopWatch() {
		this.stopWatch.stop();
	}
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_session, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_preferences:
			oldfigure=Preferences.getFigureName(getApplicationContext());
			
			startActivity(new Intent(this, Preferences.class));

			return true;
		case R.id.menu_about:
			startActivity(new Intent(this, About.class));
			return true;
		case R.id.menu_exit:
			quitGame();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
