package com.glaring.colourful.bully;

import java.io.IOException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TopScoresByFigure extends Activity implements OnClickListener{
	final static String SERVER_NAME = "http://ptha.ii.uam.es/chachacha/"; 
	final static String FIGURES_PAGE = SERVER_NAME + "figures.php" ;

	FiguresDownloaderTask figuresDownloaderTask;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//request progress bar window
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
		//inflate
		setContentView(R.layout.table);
		
		//get TableLayout from XML
		TableLayout tableLayoutFigures = (TableLayout)findViewById(R.id.TableLayoutGeneric);
		//add header
		headerRow(tableLayoutFigures);
		
		//new service or task if you will, first create object then get from topten.php the scores
		//and put them on tableLayout Top Scores.
		figuresDownloaderTask = new FiguresDownloaderTask();
		figuresDownloaderTask.execute(FIGURES_PAGE, tableLayoutFigures);
		


	}

   

    protected void onPause(){
    	if(figuresDownloaderTask!=null && figuresDownloaderTask.getStatus()!=AsyncTask.Status.FINISHED){
    		figuresDownloaderTask.cancel(true);
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
	    
	    tableRow.setOnClickListener(this);
	    
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
	    textView.setText("Figure Name"); 
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
	private class FiguresDownloaderTask extends AsyncTask<Object, String, Boolean> {
		private static final String DEBUG_TAG = "TopScoreDLTask";
		TableLayout table;

		@Override
		protected void onCancelled() {
			Log.i(DEBUG_TAG, "onCancelled() inside TopScoreDownloader");
			TopScoresByFigure.this.setProgressBarIndeterminateVisibility(false);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.i(DEBUG_TAG, "onPostExecute() inside TopScoreDownloader");
			TopScoresByFigure.this.setProgressBarIndeterminateVisibility(false);

		}

		@Override
		protected void onPreExecute() {
			TopScoresByFigure.this.setProgressBarIndeterminateVisibility(true);

		}

		@Override
		protected void onProgressUpdate(String... values) {
			if (values.length == 2) {
				String nombre = values[0];
				insertRow(table, nombre);
				//then when the user selects one from the table, save that one as a string and pass it to game to grid
				//convert the 0s to -1, the 1 to 1 and put the middle one to 0.
			} else {
				Log.e(DEBUG_TAG,
						"onProgressUpdate() inside TopScoreDownloader: Error downloading data.");
				//Toast.makeText(getBaseContext(),"Error downloading data.",Toast.LENGTH_SHORT).show();	
			}
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			boolean result = false;
			String path = (String) params[0];
			table = (TableLayout) params[1];
			

				publishProgress();
			
			
			
			XmlPullParser figuresFromXml;
			try {
				URL xmlUrl = new URL(path);
				figuresFromXml = XmlPullParserFactory.newInstance().newPullParser();
				figuresFromXml.setInput(xmlUrl.openStream(), null);
			} catch (XmlPullParserException e) {
				figuresFromXml = null;
			} catch (IOException e) {
				figuresFromXml = null;
			}
			if (figuresFromXml != null) {
				try {
					processFigures(figuresFromXml);
				} catch (XmlPullParserException e) {
					Log.e(DEBUG_TAG,
							"doInBackgraound() inside FiguresDownloader: Pull Parser failure",
							e);
				} catch (IOException e) {
					Log.e(DEBUG_TAG,
							"doInBackgraound() inside FiguresDownloades: IO Exception parsing XML",
							e);
				}
			}
			return result;
		}

		private void insertRow(final TableLayout table, String nombre) {
			final TableRow row = new TableRow(TopScoresByFigure.this);
			int textColor = getResources().getColor(R.color.top_scores_color);
			float textSize = getResources().getDimension(R.dimen.figures_name_text_size);
			
			addTextView(row, nombre, textColor, textSize);
			textSize = getResources().getDimension(R.dimen.figures_values_text_size);

			table.addView(row);
		}

		private void processFigures(XmlPullParser figures) throws XmlPullParserException, IOException {
			int eventType = -1;
			boolean flag = false, read = false;
			String str = null, nombre = null, valor = null;
			
			//detect the beginning of the xml element
			// with 
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_TAG)
					str = figures.getName();
				else if (eventType == XmlResourceParser.TEXT) {
					if (str.equals("nombre"))
						nombre = figures.getText();
					else if (str.equals("valor")){
						valor = figures.getText();
						read = true;
					}
				}
				if (read) {
					publishProgress(nombre, valor);
					read = false;
					flag = true;
				}
				eventType = figures.next();
			}
			if (flag == false) {
				publishProgress();
			}
		}
	}

	@Override
	public void onClick(View v) {
		TableRow row= (TableRow) v;
		TextView et=(TextView ) row.getChildAt(0);
				
		String text=et.getText().toString();

		
		
		//pasar text a db adaptaer
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor editor= settings.edit();
		editor.putString("TOPSCOREFIGUREQUERY",text);
		editor.commit();
		
	    Intent myIntent = new Intent(this, TopScores.class);
		/* retrieve type of board */
		startActivity(myIntent);
		

		
		finish();
	}
}
