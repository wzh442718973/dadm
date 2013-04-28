package com.es.uam.eps.dadm.mario_pantoja;

import java.io.IOException;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.client.utils.URLEncodedUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.View.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

/**
 * @author marioandrei
 *
 */
public class Account extends Activity implements OnClickListener{
	final static String SERVER_NAME = "http://ptha.ii.uam.es/chachacha/"; 
	final static String FIGURES_PAGE = SERVER_NAME + "figures.php" ;
	final static String NEW_ACCOUNT_PAGE = SERVER_NAME + "account.php" ;

	
	private EditText editTextUsername;
	private EditText editTextPassword;
	private EditText editTextPasswordAgain;
	
	NewAccountTask newAccountTask ;
	ProgressDialog waitDialog;
	
	private DatabaseAdapter db;

	@Override
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
	
		if (Preferences.isWifiConnected(this)) {
			Toast.makeText(Account.this,
					"Connected ",Toast.LENGTH_SHORT).show();
		}
		
		
		 waitDialog = new ProgressDialog(this);

	}
	@Override
	public void onStop() {
		super.onStop();
	    waitDialog = null;

		
	}

	
	/**
	 * creates a new account (user,pass) on the local db
	 */
	private void newAccount(){
		
		//get From EditText fields the strings
		String name= editTextUsername.getText().toString();
		String password1= editTextPassword.getText().toString();
		String password2= editTextPasswordAgain.getText().toString();
		
		if (!password1.equals("") && !password2.equals("") || password1.equals(password2))  {
			db = new DatabaseAdapter(this);
			db.open();
			int id= (int) db.insertUser(name, password1);
			db.close();
			
			Preferences.setId(this, Integer.toString(id));
			Preferences.setPlayerName(this, name);
			Toast.makeText(Account.this,
					"New user added to the database.",Toast.LENGTH_SHORT).show();
			//finish();
		}
		else if (password1.equals("") || password2.equals("") || name.equals("")) {
			
			Toast.makeText(Account.this,
					"Missing data ",Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(Account.this,
					"Incorrect Password ",Toast.LENGTH_SHORT).show();

	}

	
	/**
	 * @author marioandrei
	 *
	 */
	private class NewAccountTask extends AsyncTask<String, String, Boolean> {
		private static final String DEBUG_TAG = "NewAccountTask";

		@Override
		protected void onCancelled() {
			Log.i(DEBUG_TAG, "onCancelled() inside NewAccountTask");
			Account.this.setProgressBarIndeterminateVisibility(false);
		}



		@Override
		protected void onPreExecute() {
			
			waitDialog=ProgressDialog.show(Account.this,"Please, wait","Checking if username is avaible",true, true);
			waitDialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface arg0) {
					Log.d("DEBUG ","onCancel inisde onPreExecute()");
					NewAccountTask.this.cancel(true);
				}
			});
		}

		@Override
		protected void onProgressUpdate(String... values) {

		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = false;		

			//if (Preferences.isWifiConnected(Account.this)){

				result = postSettingsToServer(params[0], params[1]);
				
			//}
			return result;
		}
		/**
		 * @param username String that is going to be uploaded to the web server
		 * @param password
		 * @return
		 */
		private boolean postSettingsToServer(String username, String password){
			

			boolean state = false;
			Vector<NameValuePair>vars = new Vector<NameValuePair>();
			vars.add(new BasicNameValuePair("playername",username));
			vars.add(new BasicNameValuePair("playerpassword",password));

			
			String url = NEW_ACCOUNT_PAGE+ "?"+ URLEncodedUtils.format(vars,null);
			HttpGet request = new HttpGet(url);
			
			
			try {
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				HttpClient client = new DefaultHttpClient();
				String responseBody= client.execute(request,responseHandler);
				
				if (responseBody != null && responseBody.length()>0) {
					Log.d(DEBUG_TAG,
							"response received from "+url);
					//responseBody is a UUID = defbe56c-b4cd-44a0-9607-8be00be66736  

					Preferences.setId(Account.this, responseBody);
					Preferences.setUUID(Account.this, responseBody);

				}
				state=true;
				
			} catch (ClientProtocolException e) {
				Log.e(DEBUG_TAG,
						"Failed to get playerId (protocol):", e);
						}
			catch (IOException e) {
				 Log.e(DEBUG_TAG,
						"Failed to get player ID (IO): ",e);
			}
			return state;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			

			if (!isCancelled()) {

				Log.d("DEBUG","new account task complete");
				if (result) {

					if(! Preferences.getId(Account.this).equals("-1")){
						// a new user is created locally
						newAccount();
					}
					else
						Toast.makeText(Account.this,"username already used",Toast.LENGTH_LONG).show();	
				}
			}
			else
				Toast.makeText(Account.this,"inside onpostexecute.",Toast.LENGTH_LONG).show();	

			   if (waitDialog != null) { 
			        waitDialog.dismiss();
			   }		
			
			Account.this.finish();
			
		}




		
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acceptNewUserButton:
			//if (Preferences.isWifiConnected(Account.this)) {
			
				/*create a new Task in order to get the UUID from the online db
				 * get the username and password TODO: the password should be salted */
				String name= editTextUsername.getText().toString();
				String password1= editTextPassword.getText().toString();
				/* the new account checks if the user already exists */
				newAccountTask = new NewAccountTask();
				newAccountTask.execute(name,password1);
			//}
			//	newAccount();


			finish();
			break;
		case R.id.cancelNewUserButton:
			finish();
			break;

		}
	}

}
