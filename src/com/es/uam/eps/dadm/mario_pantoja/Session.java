package com.es.uam.eps.dadm.mario_pantoja;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 
 * @author marioandrei
 * CONTROLLER
 * 
 */
public class Session extends Activity {
	
	public static final int REQUEST_CODE = 0;
	
	private int type;
	
	private Board board;
	
	private Game game=null;
	
	private String playerName;
	

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//TODO    

		// SharedPreferences sharedPreferences = getSharedPreferences("type", MODE_PRIVATE);
		 //sharedPreferences.getInt("type",this.type);
		    
	/*	getPlayerNameFromPreferences();
		getTypeFromPreferences();
*/
		play();		

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
	
	
	
	private void getTypeFromPreferences() {
			if	(Preferences.getType(this).equalsIgnoreCase("eu")){
				setType(Game.EUROPEAN);
			}
			else
				setType(Game.ENGLISH);
		
	}

	private void play(){ 
		
		//pass to board the type of board
		setContentView(R.layout.session);
		
		board = (Board) findViewById(R.id.board); 
		
		setGame(board.getGame());
	}
	
	
	private void getPlayerNameFromPreferences(){
		setPlayerName(Preferences.getPlayerName(this)); 
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_preferences:
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
	
	private void quitGame(){
		new AlertDialog.Builder(this)
		.setTitle("Exit")
		.setMessage("Leave this game?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){
				finish();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){}
		})
		.show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_session, menu);
		return true;
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
	



}
