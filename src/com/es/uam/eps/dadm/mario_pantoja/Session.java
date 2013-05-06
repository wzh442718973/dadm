package com.es.uam.eps.dadm.mario_pantoja;

import java.io.IOException;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
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
	final static String SERVER_NAME = "http://ptha.ii.uam.es/chachacha/"; 
	final static String FIGURES_PAGE = SERVER_NAME + "figures.php" ;
	final static String NEW_SCORE_PAGE = SERVER_NAME + "addscore.php" ;
	
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

		Intent uploadService=new Intent(getApplicationContext(), UploaderService.class);
		startService(uploadService);
		
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
	
	
	public static class UploaderService extends Service {
		private UploadTask uploader;
		
		@Override
		public IBinder onBind(Intent arg0) {
			return null;
		}
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {

			uploader = new UploadTask();
			String id= Preferences.getUUID(getApplicationContext());
			uploader.execute(id);
			Log.d("Debug", "inside on start command: score upload requested");
			return START_REDELIVER_INTENT;
			
		}
		private class UploadTask extends AsyncTask<String, String, Boolean>{

			@Override
			protected Boolean doInBackground(String... params) {

				boolean result=postScoresToServer(params[0]);
				Log.d("Debug", "post scores returned: "+result);

				return result;
			}
			private boolean postScoresToServer(String userId){
				boolean state = false;
				

				Vector<NameValuePair>vars = new Vector<NameValuePair>();
				vars.add(new BasicNameValuePair("playerid",Preferences.getUUID(getApplicationContext())));
				vars.add(new BasicNameValuePair("duration",Preferences.getDuration(getApplicationContext())));
				vars.add(new BasicNameValuePair("numberoftiles",Preferences.getNumberoftiles(getApplicationContext())));
				vars.add(new BasicNameValuePair("date",Preferences.getDate(getApplicationContext())));
				vars.add(new BasicNameValuePair("board",Preferences.getFigureName(getApplicationContext())));

				
				String url = NEW_SCORE_PAGE+ "?"+ URLEncodedUtils.format(vars,null);
				HttpGet request = new HttpGet(url);
				Log.d("Debug", "httpget server: "+url);

				
				
				try {
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					HttpClient client = new DefaultHttpClient();
					String responseBody= client.execute(request,responseHandler);
					
					if (responseBody != null && responseBody.length()>0) {
						if(!responseBody.equals("-1")){
							Log.d("DEBUG", "Score uploaded to "+url);
						}
						else
							Log.d("DEBUG", "that user doesnt exists.");

					}						
					else
						Log.d("DEBUG", "Score upload failed.");
					
					state=true;
					
				} catch (ClientProtocolException e) {
					Log.e("DEBUG","Failed to upload score (protocol):", e);
							}
				catch (IOException e) {
					 Log.e("DEBUG",	"Failed to upload score (IO): ",e);
				}
				
				
				return state;
			}
			
		}
		
		
	}


}
