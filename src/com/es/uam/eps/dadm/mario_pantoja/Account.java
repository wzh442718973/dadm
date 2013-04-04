package com.es.uam.eps.dadm.mario_pantoja;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Account extends Activity implements OnClickListener{
	
	
	private EditText editTextUsername;
	private EditText editTextPassword;
	private EditText editTextPasswordAgain;
	
	
	private DatabaseAdapter db;
	
	public void  onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.account);
		
		editTextUsername= (EditText) findViewById(R.id.newUserUsernameText);
		editTextPassword= (EditText) findViewById(R.id.newUserPasswordText);
		editTextPasswordAgain= (EditText) findViewById(R.id.newUserPasswordAgainText);
		
		Button acceptButton = (Button) findViewById(R.id.acceptNewUserButton);
		acceptButton.setOnClickListener(this); 
		
		Button cancelButton = (Button) findViewById(R.id.cancelNewUserButton);
		cancelButton.setOnClickListener(this); 
	
	}

	
	private void newAccount(){
		String name= editTextUsername.getText().toString();
		String password1= editTextPassword.getText().toString();
		String password2= editTextPasswordAgain.getText().toString();
		
		if (!password1.equals("") && !password2.equals("") || password1.equals(password2))  {
			db = new DatabaseAdapter(this);
			db.open();
			db.insertUser(name, password1);
			db.close();
			
			Toast.makeText(Account.this,
					"New user added to the database",Toast.LENGTH_SHORT).show();
			finish();
		}
		else if (password1.equals("") || password2.equals("") || name.equals("")) {
			
			Toast.makeText(Account.this,
					"Missing data ",Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(Account.this,
					"Incorrect Password ",Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acceptNewUserButton:
			newAccount();
			finish();
			break;
		case R.id.cancelNewUserButton:
			finish();
			break;

		}
	}

}
