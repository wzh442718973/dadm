package com.es.uam.eps.dadm.mario_pantoja;


import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class AllGames extends ListActivity {
	DatabaseAdapter db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		
		db = new DatabaseAdapter(this);
		db.open();
		Cursor mCursor = db.getAllGames();
		
		
		ArrayList<String> games = new ArrayList<String>();

		String gameString="";

		for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor
				.moveToNext()) {
			// The Cursor is now set to the right position
			gameString="";
            for(int i=0; i<mCursor.getColumnCount(); i++)
            {                   
              gameString+=" "+mCursor.getColumnName(i)+": "+mCursor.getString(i)+"\n";
            }
            games.add(gameString);
            
			//board_type_list.add(mCursor.getString(mCursor.getColumnIndex(DatabaseAdapter.BOARD)));
			//id_list.add(mCursor.getInt(mCursor.getColumnIndex(DatabaseAdapter.ID)));
			//seconds.add(Integer.toString(mCursor.getInt(mCursor.getColumnIndex(DatabaseAdapter.SECONDS))));

		}
		db.close();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  games);
		setListAdapter(adapter);
	}

}
