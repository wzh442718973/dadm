package com.es.uam.eps.dadm.mario_pantoja;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * 
 * @author marioandrei
 * CONTROLLER
 * 
 */
public class SessionCreation extends Activity implements OnClickListener{

	
	public static final int REQUEST_CODE = 1;
	
	private int type;
	
	private String figure;
	
	private BoardCreation board;
	
	private Game game=null;
	
	private String playerName="Unknown Player";
	
	
	
	
	
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Closing Creation Mode")
	        .setMessage("Are you sure you want to leave without saving?")
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


		create();	
		
	}



	protected void onDestroy() {
		super.onDestroy();



	}

	protected void onPause() {
		super.onPause();
		

	}
	
	
	protected void onRestart() {
		super.onRestart();

	}
	
	
	protected void onResume() {
		super.onResume();

	}
	
	public void onStart() {
		super.onStart();

	}
	
	protected void onStop() {
		super.onStop();

	}
	
	
	private void create(){ 
		
		setContentView(R.layout.sessioncreation);
		
		board = (BoardCreation) findViewById(R.id.boardcreation); 
		
		//retrieve the game from the created board
		setGame(board.getGame());
		//game.setCurrentBoardStateArray(game.stringToArray(zero));
		Button button = (Button) findViewById(R.id.exitcreation);
		button.setOnClickListener(this);

	}
	

	

	
	
	public void save(){
		new AlertDialog.Builder(this)
		.setTitle("Save New Figure")
		.setMessage("Save an quit?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){
	            //add to db
	            game.newGameEntry();

				finish();
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() { 
			public void onClick(DialogInterface dialog, int which){

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



	public void setType(int type) {
		this.type = type;
	}
	
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



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (game.movesLeft()==false) {
			Toast.makeText(this,"This is not a valid board ",Toast.LENGTH_SHORT).show();		
    		finish();

		}
		else
		{
			//Toast.makeText(this,"figura "+game.getPegCount(),Toast.LENGTH_SHORT).show();		

			final EditText input = new EditText(this);

			/*
			 * ask the user for a name for the figure, and add it to the db
			 */
			new AlertDialog.Builder(SessionCreation.this)
		    .setTitle("Leaving Creation Mode")
		    .setMessage("Name your figure")
		    .setView(input)
		    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            Editable value = input.getText(); 
		            String nameofthefigure=value.toString();
					game.newFigureEntry(nameofthefigure);

		    		finish();

		        }
		    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		            // Do nothing.		
		        	finish();

		        }
		    }).show();

		}

	}


}
