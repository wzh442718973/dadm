package com.glaring.colourful.bully;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Statistics extends ListActivity{
		DatabaseAdapter db;

	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    String[] values = new String[] { "Top 10", "All Players", "All Games", "Recent Games","Top Scores (online)" };
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	  }


	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = (String) getListAdapter().getItem(position);
		
	    if (item.equals("All Players")) {
		    Intent myIntent = new Intent(this, AllUsers.class);
			/* retrieve type of board */
			startActivity(myIntent);
			
		}else 	    if (item.equals("All Games")) {
		    Intent myIntent = new Intent(this, AllGames.class);
			/* retrieve type of board */
			startActivity(myIntent);
			
		}else 	    if (item.equals("Top 10")) {
		    Intent myIntent = new Intent(this, TopTenByFigure.class);
			/* retrieve type of board */
			startActivity(myIntent);
			
		}else 	    if (item.equals("Recent Games")) {
		    Intent myIntent = new Intent(this, RecentGames.class);
			/* retrieve type of board */
			startActivity(myIntent);
			
		}else 	    if (item.equals("Top Scores (online)")) {
			
		
			    Intent myIntent = new Intent(this, TopScoresByFigure.class);
				/* retrieve type of board */
				startActivity(myIntent);
	

			
		}
	    
	    

	   // Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	  }
	
	

}
