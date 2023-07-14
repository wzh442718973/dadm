package com.es.uam.eps.dadm.mario_pantoja;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class Initial extends Activity implements OnClickListener{
	private static final String DEBUG_TAG = null;
	private static final int GALLERY_REQUEST = 0;
	private static final int CAMERA_REQUEST = 2;



	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		
		//int type_from_preferences = sharedPreferences.getInt("type", -2);
		int type_from_preferences=Preferences.getType(this);
		//Toast.makeText(this,"TYPE ="+type_from_preferences,Toast.LENGTH_SHORT).show();	
		if (type_from_preferences!=-2) {
			if (type_from_preferences==Game.ENGLISH) {
				RadioButton br = (RadioButton) findViewById(R.id.radio0);
				br.setChecked(true);
			}else{
				RadioButton br = (RadioButton) findViewById(R.id.radio1);
				br.setChecked(true);
			}
		}	
		
		boolean wifiConnected = false;
		
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiConn = networkInfo.isConnected();
		if (networkInfo != null && networkInfo.isConnected()) {
			wifiConnected = true;
		} else {
			wifiConnected = false;
		}
		
		
		SharedPreferences settings = getSharedPreferences(Preferences.WIFI_KEY, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("Preferences.WIFI_KEY", wifiConnected);
		editor.commit();
				
		networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		boolean isMobileConn = networkInfo.isConnected();
		
		settings = getSharedPreferences("isMobileConn", MODE_PRIVATE);
		editor = settings.edit();
		editor.putBoolean("isMobileConn", isMobileConn);
		
		Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
		Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);
		
		
		/* Assign listeners */
		Button button = (Button) findViewById(R.id.newgamebtn);
		button.setOnClickListener( this);
		button = (Button) findViewById(R.id.about);
		button.setOnClickListener( this);
		button = (Button) findViewById(R.id.loginbtn);
		button.setOnClickListener( this);
		button = (Button) findViewById(R.id.sitebtn);
		button.setOnClickListener( this);
		button = (Button) findViewById(R.id.statisticsbtn);
		button.setOnClickListener( this);
		button = (Button) findViewById(R.id.preferences);
		button.setOnClickListener( this);	
		button = (Button) findViewById(R.id.creationbtn);
		button.setOnClickListener( this);		
		button = (Button) findViewById(R.id.figurescreated);
		button.setOnClickListener( this);		
		
		
		if (isWifiConn || isMobileConn) {
			button = (Button) findViewById(R.id.share);
			button.setOnClickListener( this);	
			button = (Button) findViewById(R.id.figures);
			button.setOnClickListener( this);
		}
		else{
			button = (Button) findViewById(R.id.share);
			button.setBackgroundResource(R.drawable.offmenubtn);
			button = (Button) findViewById(R.id.figures);
			button.setBackgroundResource(R.drawable.offmenubtn);
		}
		
		/* Set Profile Pic long listener 
		ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
		String str = "android.resource://com.es.uam.eps.dadm.mario_pantoja/drawable/pic";
		Uri uri = Uri.parse(str);
		imageButton.setImageURI(uri);
		setLongListener();*/
	

	}
	

	@Override
	public void onClick(View v) {
			
		
		RadioGroup g = (RadioGroup) findViewById(R.id.radioGroup1);
		SharedPreferences sharedPreferences = getSharedPreferences("type",MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		/*int type_from_preferences = sharedPreferences.getInt("type", -2);
		Toast.makeText(this,"TYPE ="+type_from_preferences,Toast.LENGTH_SHORT).show();	

		//if there are preferences, get the value from them
		if (type_from_preferences!=-2) {
			if (type_from_preferences==Game.ENGLISH) {
				RadioButton br = (RadioButton) findViewById(R.id.radio0);
				br.setChecked(true);
			}else{
				RadioButton br = (RadioButton) findViewById(R.id.radio1);
				br.setChecked(true);
			}
			v.invalidate();
		}*/
		
		switch (g.getCheckedRadioButtonId()) {
		case R.id.radio0:
			editor.putInt("type", Game.EUROPEAN);
			break;
		case R.id.radio1:
			editor.putInt("type", Game.ENGLISH);
			break;
		}
		editor.commit();

		


		if (v.getId() == R.id.newgamebtn) {
			
			
			//TODO if logged put text to LOG OUT and
			if (Preferences.isUserLogged(this)==true) {
	

			Intent myIntent = new Intent(this, Session.class);
			// myIntent.putExtra("type",Game.EUROPEAN);
			//startActivity(myIntent);
			startActivityForResult(myIntent, Session.REQUEST_CODE);
			// startActivity(new
			// Intent("com.es.uam.eps.dadm.mario_pantoja.SESSION"));
			}
			else
				Toast.makeText(this,"Login First",Toast.LENGTH_SHORT).show();	

		} else if (v.getId() == R.id.sitebtn) {
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://en.wikipedia.org/wiki/Peg_solitaire"));
			startActivity(intent);
			// startActivityForResult(intent, Session.REQUEST_CODE);
		}

		else if (v.getId()==R.id.camera) {
			onLaunchCamera( v);
		}
		
		else if (v.getId()==R.id.loginbtn) {
			Intent intent = new Intent("com.es.uam.eps.dadm.mario_pantoja.LOGIN");
			startActivityForResult(intent, Login.REQUEST_CODE);
		}
		
		else if (v.getId()==R.id.about) {
			Intent intent = new Intent("com.es.uam.eps.dadm.mario_pantoja.ABOUT"); 
			startActivity(intent);
		}
		else if (v.getId()==R.id.statisticsbtn) {
			Intent intent = new Intent("com.es.uam.eps.dadm.mario_pantoja.STATISTICS"); 
			startActivity(intent);
		}
		else if (v.getId()==R.id.figures) {
			Intent intent = new Intent("com.es.uam.eps.dadm.mario_pantoja.FIGURES"); 
			startActivity(intent);
		}
		else if (v.getId()==R.id.figurescreated) {
			Intent intent = new Intent("com.es.uam.eps.dadm.mario_pantoja.FIGURESCREATED"); 
			startActivity(intent);
		}
		else if (v.getId()==R.id.preferences) {
			Intent intent = new Intent("com.es.uam.eps.dadm.mario_pantoja.PREFERENCES"); 
			startActivity(intent);
		}		
		else if (v.getId()==R.id.share) {
			Intent intent = new Intent("com.es.uam.eps.dadm.mario_pantoja.SHARE"); 
			startActivity(intent);
		}
		else if (v.getId()==R.id.creationbtn) {

			Intent myIntent = new Intent(this, SessionCreation.class);
			startActivity(myIntent);
		}
	}
	

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 * when comming back from session, if the user wants to restart a game, this launches
	 * a new sessiong
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode==Session.REQUEST_CODE) {
			if (resultCode==RESULT_OK) {
				/* RELAUNCH NEW GAME*/				
				Intent myIntent = new Intent(this, Session.class);
				startActivityForResult(myIntent, Session.REQUEST_CODE);
			}
		}
		switch (requestCode) {
		case CAMERA_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Bitmap cameraPicture = (Bitmap) intent.getExtras().get("data");
				if (cameraPicture != null)
					try {
						saveImage(cameraPicture);
					} catch (Exception e) {
						Log.e(DEBUG_TAG, "saveImage() failed");
					}
			}
			break;
		case GALLERY_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = intent.getData();
				if (uri != null) {
					try {
						Bitmap pic = Media.getBitmap(getContentResolver(), uri);
						saveImage(pic);
					} catch (FileNotFoundException e) { // block
														 e.printStackTrace();
					} catch (IOException e) {
						// catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	public void onLaunchCamera(View v) {
		String message = "Take your pic!";
		Intent pictureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(Intent.createChooser(pictureIntent, message),
				CAMERA_REQUEST);
	}
	
	/*
	 * ImageBUtton listener (profile pic)
	 * 
	 *
	private void setLongListener() {
		ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
		imageButton.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				String message = "Choose a picture";
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, message),
						GALLERY_REQUEST);
				return true;
			}
		});
	} */

	/**
	 * saves a Bitmap to the disk
	 * 
	 * */
	private void saveImage(Bitmap bitmap) {
		String filename = "portrait.jpg";
		try {
			bitmap.compress(CompressFormat.JPEG, 100,
					openFileOutput(filename, MODE_PRIVATE));
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "Image compression failed.", e);
		}
		Uri uri = Uri.fromFile(new File(Initial.this.getFilesDir(), filename));
		ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
		Uri imageUri = Uri.parse(uri.getPath());
		imageButton.setImageURI(null);
		imageButton.setImageURI(imageUri);
	}
	/**
	 * confirmation dialog 
	 */
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Closing PegSolitaire")
	        .setMessage("Are you sure you want to Exit?")
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
	
	/* menu */
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
	
	/**
	 * exit the game dialog
	 */
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
	
	protected void onResume() {
		/* get from preferences current type of board 
		
		//weird bug http://code.google.com/p/android/issues/detail?id=2096#makechanges
		//http://stackoverflow.com/questions/5227478/getting-integer-or-index-values-from-a-list-preference
		SharedPreferences sharedPreferences = getSharedPreferences("type",MODE_PRIVATE);

		int type_from_preferences = sharedPreferences.getInt("type", -2);
		Toast.makeText(this,"TYPE ="+type_from_preferences,Toast.LENGTH_SHORT).show();	

		
		if (type_from_preferences!=-2) {
			if (type_from_preferences==Game.ENGLISH) {
				RadioButton br = (RadioButton) findViewById(R.id.radio0);
				br.setChecked(true);
						br.invalidate();

			}else{
				RadioButton br = (RadioButton) findViewById(R.id.radio1);
				br.setChecked(true);
			}
		}	


		*/
		super.onResume();
	}

	protected void onStop() {
		super.onStop();
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_session, menu);
		return true;
	}
}
