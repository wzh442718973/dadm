package com.es.uam.eps.dadm.mario_pantoja;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
		
		/* Assign listeners */
		Button button = (Button) findViewById(R.id.newgamebtn);
		button.setOnClickListener( this);
		button = (Button) findViewById(R.id.about);
		button.setOnClickListener( this);
		button = (Button) findViewById(R.id.loginbtn);
		button.setOnClickListener( this);
		button = (Button) findViewById(R.id.sitebtn);
		button.setOnClickListener( this);

		
		
		/* Profile Pic */
		ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
		String str = "android.resource://com.es.uam.eps.dadm.mario_pantoja/drawable/pic";
		Uri uri = Uri.parse(str);
		imageButton.setImageURI(uri);
		setLongListener();
	

	}
	


	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.newgamebtn) {
		
			
			Intent myIntent = new Intent(this, Session.class);
			myIntent.putExtra("type",Game.EUROPEAN);

			/* retrieve type of board */
			RadioGroup g = (RadioGroup) findViewById(R.id.radioGroup1);
			
			SharedPreferences sharedPreferences = getSharedPreferences("type", MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			 
			switch (g.getCheckedRadioButtonId()) {
				case R.id.radio0:
					editor.putInt("type", Game.EUROPEAN);
					break;
		
				case R.id.radio1:
					editor.putInt("type", Game.ENGLISH);
					break;
			}
			
			 editor.commit();

			startActivity(myIntent);
			//startActivityForResult(myIntent, Session.REQUEST_CODE);
			//startActivity(new Intent("com.es.uam.eps.dadm.mario_pantoja.SESSION"));
		}
		else if (v.getId()==R.id.sitebtn) {
			Intent intent= new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://www.marioandrei.com"));
			startActivity(intent);
			//startActivityForResult(intent, Session.REQUEST_CODE);
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
	}
	

	

	//TODO captura la informacion devuelta por otras actividades
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode==Session.REQUEST_CODE) {
			if (resultCode==RESULT_OK) {
				String type= intent.getData().toString();	
				Toast.makeText(this, type, Toast.LENGTH_SHORT).show();
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
														// e.printStackTrace();
					} catch (IOException e) {
						// catch block e.printStackTrace();
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
	
	/**
	 * ImageBUtton listener (profile pic)
	 * 
	 * */
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
	}

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
}
