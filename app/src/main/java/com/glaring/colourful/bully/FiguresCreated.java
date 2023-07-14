package com.glaring.colourful.bully;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FiguresCreated extends Activity implements OnClickListener{
	final static String SERVER_NAME = "http://ptha.ii.uam.es/chachacha/"; 
	final static String FIGURES_PAGE = SERVER_NAME + "figures.php" ;
	private DatabaseAdapter db;
	

	
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
		

		/* connect to the db, make query, print table, close db.*/ 
		db = new DatabaseAdapter(this);
		db.open();
		Cursor mCursor = db.getAllFigures();
			
		TableLayout tl = (TableLayout) findViewById(R.id.TableLayoutGeneric);

		
		for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor
				.moveToNext()) {
			// The Cursor is now set to the right position
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
		
            for(int i=0; i<mCursor.getColumnCount(); i++){      
    			TextView text = new TextView(this);
               	text.setTextAppearance(this, android.R.style.TextAppearance_Large);
               	//text.setTextSize(getResources().getDimension(R.dimen.figures_values_text_size));

               	text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
               	text.setText(mCursor.getString(i));
    			tr.addView(text);
            }
    	    tr.setOnClickListener(this);


			tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			
			
			
		
		}
		db.close();
	}


    /**
     * add the first ROW to the table layout
     * @param scoreTable
     */


    protected void onPause(){

    	super.onPause();
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
	    
	    
	    textView = new TextView(this);
	    textView.setTextSize(textSize);
	    textView.setTextColor(textColor);
	    textView.setText("Value"); 
	    header.addView(textView);
	    

    	scoreTable.addView(header); 
    }



	@Override
	public void onClick(View v) {
		TableRow row= (TableRow) v;
		TextView et=(TextView ) row.getChildAt(1);
				
		String text=et.getText().toString();

		Preferences.setOnlineFigure(this.getBaseContext(),text);
		Preferences.setFigure(this.getBaseContext(),text);

		//save to preferences the name if the figure
		 et=(TextView ) row.getChildAt(0);
		 text=et.getText().toString();

        Preferences.setFigureName(getBaseContext(),text );
		startActivity(new Intent(this, Session.class));

		
		finish();
	}
}
