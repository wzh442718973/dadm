package com.es.uam.eps.dadm.mario_pantoja;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener; 
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity implements OnClickListener {
	public static final int REQUEST_CODE = 1;

	
	private DatabaseAdapter db;
	private EditText usernameEditText;
	private EditText passwordEditText;
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		
		
		usernameEditText= (EditText) findViewById(R.id.usernameText);
		passwordEditText= (EditText) findViewById(R.id.passwordText);
		
		
		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);
		
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(this);
		
		Button newUserButton = (Button) findViewById(R.id.newUserButton);
		newUserButton.setOnClickListener(this);
		
		/*loginButton.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				TextView usernameText = (TextView) findViewById(R.id.usernameText);
				Intent intent = getIntent();
				intent.setData(Uri.parse(usernameText.getText().toString()));
				setResult(RESULT_OK, intent);
				finish();
			}
		});*/
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginButton:
			poblateBoards();
			check();			
			break;
			
		case R.id.cancelButton:
			finish();
			break;

		case R.id.newUserButton:
			startActivity(new Intent(this, Account.class));
			break;
		}
	}

	private void poblateBoards(){
		db= new DatabaseAdapter(this);
		db.open();
		db.insertBoards();
		db.close();
	}

	private void check() {
		String username = usernameEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		
		db=new DatabaseAdapter(this);
		db.open();
		boolean in = db.isRegistered(username, password);
		db.close();
		
		if (in==true){
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor= settings.edit();
			editor.putString("username",username);
			editor.commit();
			Preferences.setPlayerName(this, username);
			Preferences.setIfUserIsLogged(getBaseContext(),true);
			startActivity(new Intent(this, Session.class));
			finish();
		}
		else{
			new AlertDialog.Builder(this)
			.setTitle("Error")
			.setMessage("Login failed")
			.setNeutralButton("Try Again",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			}).show();
		}
	}


}