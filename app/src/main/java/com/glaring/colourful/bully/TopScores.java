package com.glaring.colourful.bully;

import java.io.IOException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TopScores extends Activity {
	
	final static String SERVER_NAME = "http://ptha.ii.uam.es/chachacha/"; 
	final static String ADD_SCORE_PAGE = SERVER_NAME + "addscore.php" ;
	final static String TOP_TEN = SERVER_NAME + "topten.php" ;

	TopScoresDownloaderTask topScoresDownloaderTask;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//request progress bar window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
		//inflate
		setContentView(R.layout.table);
		
		//get TableLayout from XML
		TableLayout tableLayoutTopScores = (TableLayout)findViewById(R.id.TableLayoutGeneric);
		//add header
		headerRow(tableLayoutTopScores);
		
		//new service or task if you will, first create object then get from topten.php the scores
		//and put them on tableLayout Top Scores.
		topScoresDownloaderTask = new TopScoresDownloaderTask();
		String toptenbyfigure=TOP_TEN+"?figurename="+PreferenceManager.getDefaultSharedPreferences(getBaseContext())
				.getString("TOPSCOREFIGUREQUERY", "completo");
		Log.i("HEY", "TOPSCORE QUERY ="+toptenbyfigure);

		topScoresDownloaderTask.execute(toptenbyfigure, tableLayoutTopScores);
	}

   

    protected void onPause(){
    	if(topScoresDownloaderTask!=null && topScoresDownloaderTask.getStatus()!=AsyncTask.Status.FINISHED){
    		topScoresDownloaderTask.cancel(true);
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
	    
    }
    /**
     * add the first ROW to the table layout
     * @param scoreTable
     */
    private void headerRow(TableLayout scoreTable) {
    	TableRow header = new TableRow(this);
    	int textColor = getResources().getColor(R.color.header_top_scores_color); 
    	float textSize = getResources().getDimension(R.dimen.header_top_scores_size);
    	addTextView(header, getResources().getString(R.string.username), textColor, textSize); 
    	addTextView(header, getResources().getString(R.string.npieces), textColor, textSize); 
    	addTextView(header, getResources().getString(R.string.duration), textColor, textSize);
    	scoreTable.addView(header); 
    }

    /**
     * class that creates that defines the asynchronous service
     * @author marioandrei
     *
     */
	private class TopScoresDownloaderTask extends AsyncTask<Object, String, Boolean> {
		private static final String DEBUG_TAG = "TopScoreDLTask";
		TableLayout table;

		@Override
		protected void onCancelled() {
			Log.i(DEBUG_TAG, "onCancelled() inside TopScoreDownloader");
			TopScores.this.setProgressBarIndeterminateVisibility(false);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.i(DEBUG_TAG, "onPostExecute() inside TopScoreDownloader");
			TopScores.this.setProgressBarIndeterminateVisibility(false);
		}

		@Override
		protected void onPreExecute() {
			TopScores.this.setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			if (values.length == 3) {
				String username = values[0];
				String npieces = values[1];
				String duration = values[2];
				insertRow(table, username, npieces, duration);
			} else {
				//Log.e(DEBUG_TAG,"onProgressUpdate() inside TopScoreDownloader: Error downloading data.");
				//Toast.makeText(getBaseContext(),"Error downloading data.",Toast.LENGTH_SHORT).show();	
			}
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			boolean result = false;
			String path = (String) params[0];
			table = (TableLayout) params[1];
			

				publishProgress();
			
			
			
			XmlPullParser topScores;
			try {
				URL xmlUrl = new URL(path);
				topScores = XmlPullParserFactory.newInstance().newPullParser();
				topScores.setInput(xmlUrl.openStream(), null);
			} catch (XmlPullParserException e) {
				topScores = null;
			} catch (IOException e) {
				topScores = null;
			}
			if (topScores != null) {
				try {
					processScores(topScores);
				} catch (XmlPullParserException e) {
					Log.e(DEBUG_TAG,
							"doInBackgraound() inside TopScoresDownloader: Pull Parser failure",
							e);
				} catch (IOException e) {
					Log.e(DEBUG_TAG,
							"doInBackgraound() inside TopScoresDownloader: IO Exception parsing XML",
							e);
				}
			}
			return result;
		}

		private void insertRow(final TableLayout table, String username,
				String npieces, String duration) {
			final TableRow row = new TableRow(TopScores.this);
			int textColor = getResources().getColor(R.color.top_scores_color);
			float textSize = getResources().getDimension(R.dimen.top_scores_size);
			
			addTextView(row, username, textColor, textSize);
			addTextView(row, npieces, textColor, textSize);
			addTextView(row, duration, textColor, textSize);
			table.addView(row);
		}

		private void processScores(XmlPullParser scores) throws XmlPullParserException, IOException {
			int eventType = -1;
			boolean flag = false, read = false;
			String str = null, username = null, npieces = null, duration = null;
			
			//detect the beginning of the xml element
			// with 
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_TAG)
					str = scores.getName();
				else if (eventType == XmlResourceParser.TEXT) {
					if (str.equals("username"))
						username = scores.getText();
					else if (str.equals("npiezas"))
						npieces = scores.getText();
					else if (str.equals("duracion")) {
						read = true;
						duration = scores.getText();
					}
				}
				if (read) {
					publishProgress(username, npieces, duration);
					read = false;
					flag = true;
				}
				eventType = scores.next();
			}
			if (flag == false) {
				publishProgress();
			}
		}
	}
}
