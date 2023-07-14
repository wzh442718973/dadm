package com.es.uam.eps.dadm.mario_pantoja;



import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RecentGames extends Activity {
	private DatabaseAdapter db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table);

		
		db = new DatabaseAdapter(this);
		db.open();
		Cursor mCursor = db.gamesRecent();
		
		
		

		TableLayout tl = (TableLayout) findViewById(R.id.TableLayoutGeneric);

	
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
