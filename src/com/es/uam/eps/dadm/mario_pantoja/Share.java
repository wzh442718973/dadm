package com.es.uam.eps.dadm.mario_pantoja;

import java.io.IOException;
import java.net.URL;
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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.es.uam.eps.dadm.mario_pantoja.Session.UploaderService;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author marioandrei
 * Figures gets the figures from the online server and lets the user choose one to play
 */
public class Share extends Activity implements OnClickListener{
	/**
	 * @return the uuid
	 */
	public static String getUuid() {
		return uuid;
	}



	/**
	 * @return the username
	 */
	public static String getUsername() {
		return username;
	}



	/**
	 * @return the msg
	 */
	public static String getMsg() {
		return msg;
	}



	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		Share.uuid = uuid;
	}



	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		Share.username = username;
	}



	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		Share.msg = msg;
	}


	final static String SERVER_NAME = "http://ptha.ii.uam.es/chachacha/"; 
	
	final static String SENDMSG_PAGE = SERVER_NAME + "sendmsg.php" ;
	final static String GETMSG_PAGE = SERVER_NAME + "getmsgs.php" ;
	private DatabaseAdapter db;
	MessagesDownloaderTask messagesDownloaderTask;
	
	private int numberOfLocalFigures=0;
	
	
	private static String uuid;
	private static String username;
	private static String msg;


	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//request progress bar window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
		//inflate
		setContentView(R.layout.table);
		
		
		
		//TableLayout tableLayoutFigures = (TableLayout)findViewById(R.id.TableLayoutGeneric);
		
		TableLayout tl = (TableLayout) findViewById(R.id.TableLayoutGeneric);

		//add header
		headerRow(tl);
		
		
		//TODO
		//add figuras that can be sent!
		
		
		
		/* connect to the db, make query, print table, close db.*/ 
		db = new DatabaseAdapter(this);
		db.open();
		Cursor mCursor = db.getAllFigures();
			

		
		for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor
				.moveToNext()) {
			// The Cursor is now set to the right position
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
		
			
			//name of the figure
    			TextView text = new TextView(this);
               	text.setTextAppearance(this, android.R.style.TextAppearance_Large);
               	text.setTextSize(getResources().getDimension(R.dimen.figures_name_text_size));
               	

               	text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
               	String aux_string=mCursor.getString(0);
               	text.setText(aux_string);
    			tr.addView(text);
    			
    			
    		/*
    			 text = new TextView(this);
               	text.setTextAppearance(this, android.R.style.TextAppearance_Large);
           		text.setTextSize(getResources().getDimension(R.dimen.figures_values_text_size));
               	

               	text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
               	 aux_string=mCursor.getString(1);
               	if (aux_string.length()>8) {
				}
               	
               	text.setText(aux_string);
    			tr.addView(text);
    			*/
    			
    			
    			
            
    	    tr.setOnClickListener(this);

			tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

		}
		db.close();
	

		
		
		
		inboxHeaderRow(tl);		
		//from the message get the name and the figure array.

		//http://ptha.ii.uam.es/chachacha/getmsgs.php?playerid=b1f056ce-5cfd-4b1f-b64a-aebed21b71f5
		messagesDownloaderTask = new MessagesDownloaderTask();
		messagesDownloaderTask.execute(GETMSG_PAGE, tl);
	}

   

    protected void onPause(){
    	if(messagesDownloaderTask!=null && messagesDownloaderTask.getStatus()!=AsyncTask.Status.FINISHED){
    		messagesDownloaderTask.cancel(true);
    	}
    	super.onPause();
    }

   
    /**
     * simplifies the addTextView 
     * add to the TableRow a new created TextView with the string text.
     * @param tableRow
     * @param text
     * @param textColor
     * @param textSize
     */
    private void addTextView(TableRow tableRow, String text, int textColor, float textSize)
    {
	    TextView textView = new TextView(this);
	    textView.setTextSize(textSize);
	    textView.setTextColor(textColor);
	    textView.setText(text); 
	    tableRow.addView(textView);
	    
	    tableRow.setId(numberOfLocalFigures);
	    numberOfLocalFigures++;
	    
	    tableRow.setOnClickListener(this);
	    
    }
    
    private void inboxHeaderRow(TableLayout scoreTable) {
    	
    	
    	//add title
    	TableRow subheader = new TableRow(this);
    	int textColor = getResources().getColor(R.color.header_top_scores_color); 
    	float textSize = getResources().getDimension(R.dimen.header_top_scores_size);
	    TextView textView = new TextView(this);
	    textView.setTextSize(textSize);
	    textView.setTextColor(textColor);
	    //textView.setText(Preferences.getUUID(getBaseContext())); 
	    textView.setText("Inbox:");

	    subheader.addView(textView);
    	scoreTable.addView(subheader); 
    	
    	
    	//add labels
    	subheader = new TableRow(this);
    	textColor = getResources().getColor(R.color.header_top_scores_color); 
    	textSize = getResources().getDimension(R.dimen.header_top_scores_size);

    	textView = new TextView(this);
	    textView.setTextSize(textSize);
	    textView.setTextColor(textColor);
	    textView.setText("From:");
	    subheader.addView(textView);
	    
    	textView = new TextView(this);
	    textView.setTextSize(textSize);
	    textView.setTextColor(textColor);
	    textView.setText("Figure:");
	    subheader.addView(textView);
	    
    	scoreTable.addView(subheader); 

	    
    }
    
    /**
     * add the first ROW to the table layout
     * @param scoreTable
     */
    private void headerRow(TableLayout scoreTable) {
    	
    	TableRow header = new TableRow(this);
    	int textColor = getResources().getColor(R.color.header_top_scores_color); 
    	float textSize = getResources().getDimension(R.dimen.header_top_scores_size);
    	
    	
	    TextView textView = new TextView(this);
	    textView.setTextSize(textSize);
	    textView.setTextColor(textColor);
	    //textView.setText(Preferences.getUUID(getBaseContext())); 
	    textView.setText("Name");

	    header.addView(textView);
	    
	    
	    textView = new TextView(this);
	    textView.setTextSize(textSize);
	    textView.setTextColor(textColor);
	    textView.setText(" "); 
	    header.addView(textView);

	    
    	//addTextView(header, "Figure Name", textColor, textSize); 
    	//addTextView(header, "Value", textColor, textSize); 
    	scoreTable.addView(header); 
    	

    }

    /**
     * class that creates that defines the asynchronous service
     * @author marioandrei
     *
     */
	private class MessagesDownloaderTask extends AsyncTask<Object, String, Boolean> {
		private static final String DEBUG_TAG = "Message Download DLTask";
		TableLayout table;

		@Override
		protected void onCancelled() {
			Log.i(DEBUG_TAG, "onCancelled() inside Message Download ");
			Share.this.setProgressBarIndeterminateVisibility(false);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.i(DEBUG_TAG, "onPostExecute() inside Message Download Task ");
			Share.this.setProgressBarIndeterminateVisibility(false);
		}

		@Override
		protected void onPreExecute() {
			Share.this.setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			if (values.length == 2) {
				String nombre = values[0];
				String valor = values[1];
				insertRow(table, nombre, valor);
				//then when the user selects one from the table, save that one as a string and pass it to game to grid
				//convert the 0s to -1, the 1 to 1 and put the middle one to 0.
			} else {
				//Log.e(DEBUG_TAG,"onProgressUpdate() inside message Downloader: Error downloading data.");
				//Toast.makeText(getBaseContext(),"Error downloading data.",Toast.LENGTH_SHORT).show();	
			}
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			boolean result = false;
			String url = (String) params[0];
			//http://ptha.ii.uam.es/chachacha/getmsgs.php?playerid=b1f056ce-5cfd-4b1f-b64a-aebed21b71f5
			String path=url+"?playerid="+Preferences.getUUID(getBaseContext());
			table = (TableLayout) params[1];
			Log.d(DEBUG_TAG,"player UUID: "+Preferences.getUUID(getBaseContext()));

				publishProgress();
			
			//	5cf4ea4c-2008-4f05-84a7-929fd0cb0f59

			
			XmlPullParser messagesFromXml;
			try {
				URL xmlUrl = new URL(path);
				messagesFromXml = XmlPullParserFactory.newInstance().newPullParser();
				messagesFromXml.setInput(xmlUrl.openStream(), null);
			} catch (XmlPullParserException e) {
				messagesFromXml = null;
			} catch (IOException e) {
				messagesFromXml = null;
			}
			if (messagesFromXml != null) {
				try {
					processFigures(messagesFromXml);
				} catch (XmlPullParserException e) {
					Log.e(DEBUG_TAG,
							"doInBackgraound() inside Message Downloader Task : Pull Parser failure",
							e);
				} catch (IOException e) {
					Log.e(DEBUG_TAG,
							"doInBackgraound() inside Message Downloader Task: IO Exception parsing XML",
							e);
				}
			}
			return result;
		}

		/**
		 * @param table
		 * @param name
		 * @param value
		 * inserts row on a table, with the 
		 */
		private void insertRow(final TableLayout table, String name,
				String value) {
			final TableRow row = new TableRow(Share.this);
			int textColor = getResources().getColor(R.color.top_scores_color);
			float textSize = getResources().getDimension(R.dimen.figures_name_text_size);
			
			addTextView(row, name, textColor, textSize);
			textSize = getResources().getDimension(R.dimen.figures_values_text_size);

			String figure=value.substring(0, 49);
			String figurename=value.substring(49);
			addTextView(row, figurename, textColor, textSize);
			
			addTextView(row, figure, textColor, textSize);

			table.addView(row);
			
			
			
		}

		/**
		 * @param messages
		 * @throws XmlPullParserException
		 * @throws IOException
		 * 
		 * update the table from a Pull of a XML through onProgressUpdate (calling it with publish progress)
		 */
		private void processFigures(XmlPullParser messages) throws XmlPullParserException, IOException {
			int eventType = -1;
			boolean flag = false, read = false;
			String str = null, name = null, value = null;
			
			//detect the beginning of the xml element
			// with 
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_TAG)
					str = messages.getName();
				else if (eventType == XmlResourceParser.TEXT) {
					if (str.equals("remitente"))
						name = messages.getText();
					else if (str.equals("cuerpo")){
						value = messages.getText();
						read = true;
					}
				}
				if (read) {
					publishProgress(name, value);
					read = false;
					flag = true;
				}
				eventType = messages.next();
			}
			if (flag == false) {
				publishProgress();
			}
		}
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
			uploader.execute(Share.getUuid(),Share.getUsername(), Share.getMsg());
			Log.d("Debug", "inside on start command: send message requested");
			return START_REDELIVER_INTENT;
			
		}
		private class UploadTask extends AsyncTask<String, String, Boolean>{

			@Override
			protected Boolean doInBackground(String... params) {

				boolean result=sendMessageToUser(params[0],params[1],params[2]);
				Log.d("Debug", "Message Sent to"+params[0]+" delivered "+result);

				return result;
			}
			private boolean sendMessageToUser(String id,String username, String msg){
				boolean state = false;
				

				Vector<NameValuePair>vars = new Vector<NameValuePair>();
				vars.add(new BasicNameValuePair("playerid",id));
				vars.add(new BasicNameValuePair("to",username));
				vars.add(new BasicNameValuePair("msg",msg));


				
				String url = SENDMSG_PAGE+ "?"+ URLEncodedUtils.format(vars,null);
				HttpGet request = new HttpGet(url);
				Log.d("Debug", "httpget server: "+url);

				
				
				try {
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					HttpClient client = new DefaultHttpClient();
					String responseBody= client.execute(request,responseHandler);
					
					if (responseBody != null && responseBody.length()>0) {
						if(!responseBody.equals("-1")){
							Log.d("DEBUG", "Message sent. "+url);
						}
						else
							Log.d("DEBUG", "that user doesnt exists.");

					}						
					else
						Log.d("DEBUG", "message upload failed.");
					
					state=true;
					
				} catch (ClientProtocolException e) {
					Log.e("DEBUG","Failed to send message (protocol):", e);
							}
				catch (IOException e) {
					 Log.e("DEBUG",	"Failed to send message (IO): ",e);
				}
				
				
				return state;
			}
			
		}
		
		
	}


	@Override
	public void onClick(View v) {
		TableRow row= (TableRow) v;
		int n=(Integer) row.getId();
        
		
		

		 
		 if (n==-1) {
			 
				TextView et=(TextView ) row.getChildAt(0);
				//Integer n = row.getVirtualChildCount();//getChildAt(0);
				final String text=et.getText().toString();

				//ask the user for a username
				
				final EditText input = new EditText(this);

				/*
				 * ask the user for a name for the figure, and add it to the db
				 */
				new AlertDialog.Builder(Share.this)
			    .setTitle("Sharing Your Figure")
			    .setMessage("Username ")
			    .setView(input)
			    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            Editable value = input.getText(); 
			            setUsername(value.toString());
			            setUuid(Preferences.getUUID(getBaseContext()));
			            
			            
			            
			    		db = new DatabaseAdapter(Share.this);
			    		db.open();
			    		Cursor mCursor = db.getFigureByName(text);
			    		for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor
			    				.moveToNext()) {
			            setMsg(mCursor.getString(0)+text);
			    		}
			            db.close();
			            
			    		Intent uploadService=new Intent(getApplicationContext(), UploaderService.class);
			    		startService(uploadService);
			    		
			    		finish();

			        }
			    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            // Do nothing.		

			        }
			    }).show();

				
			    Toast.makeText(getBaseContext(),"sending the figure named:\n  "+text,Toast.LENGTH_SHORT).show();	

		}
		else{
			TextView et=(TextView ) row.getChildAt(1);
			//Integer n = row.getVirtualChildCount();//getChildAt(0);
			String text=et.getText().toString();

			 et=(TextView ) row.getChildAt(1);
			 text=et.getText().toString();
			 et=(TextView ) row.getChildAt(2);
			 String figure=et.getText().toString();
			 Toast.makeText(getBaseContext(),"Save figure named:\n  "+text+"tag: "+n+"figure "+figure,Toast.LENGTH_SHORT).show();	
		
	    		db = new DatabaseAdapter(Share.this);
	    		db.open();
	    		text = text.replaceAll("\\s+", "");
			db.insertFigure(text, figure);
			db.close();

		}
        //Preferences.setFigureName(getBaseContext(),text );

		
		//finish();
	}
}
