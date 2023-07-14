package com.glaring.colourful.bully;


import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AllUsers extends ListActivity{
	DatabaseAdapter db;

	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    
		db= new DatabaseAdapter(this);
		db.open();
		Cursor mCursor =db.getAllUsers();
		ArrayList<String> mArrayList = new ArrayList<String>();
		for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
		    // The Cursor is now set to the right position
		    mArrayList.add(mCursor.getString(mCursor.getColumnIndex(DatabaseAdapter.NAME)));
		}
		db.close();
		
		
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, mArrayList);
	    setListAdapter(adapter);
	  }

	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = (String) getListAdapter().getItem(position);
	    Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	  }
	
	

}
