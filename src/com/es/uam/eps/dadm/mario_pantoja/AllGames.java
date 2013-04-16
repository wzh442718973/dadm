package com.es.uam.eps.dadm.mario_pantoja;



import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class AllGames extends Activity {
	private DatabaseAdapter db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_games);

		
		//TODO tablelayout not working
		db = new DatabaseAdapter(this);
		db.open();
		Cursor mCursor = db.getAllGames();
		
		
		
		

		TableLayout tl = (TableLayout) findViewById(R.id.table_allgames);

	
		TableRow tr1 = new TableRow(this);
		tr1.setLayoutParams(new LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
	

        for(int i=0; i<mCursor.getColumnCount(); i++){      
			TextView text = new TextView(this);
           	text.setTextAppearance(this, android.R.style.TextAppearance_Medium);

           	text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));
           	text.setText(mCursor.getColumnName(i)+" ");
			tr1.addView(text);
			
        }
		tl.addView(tr1, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

		
		
		for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor
				.moveToNext()) {
			// The Cursor is now set to the right position
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
		
            for(int i=0; i<mCursor.getColumnCount(); i++){      
    			TextView text = new TextView(this);
               	text.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
               	text.setText(mCursor.getString(i)+" ");
    			tr.addView(text);
            }

			tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		}
		db.close();
		

	}

}
