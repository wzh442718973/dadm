package com.es.uam.eps.dadm.mario_pantoja;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GameSQLiteHelper extends SQLiteOpenHelper {

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    
	  public static final String TABLE_GAMES = "games";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_GRID = "grid";
	  public static final String COLUMN_CURRENTPLAYER= "player";
	  public static final String COLUMN_STATE = "state";
	  public static final String COLUMN_PEGCOUNT= "pegs";  

	  private static final String DATABASE_NAME = "games.db";

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_GAMES + "(" + COLUMN_ID
	      + " integer primary key autoincrement, "
	      + COLUMN_GRID+ " text not null,"
	      + COLUMN_CURRENTPLAYER + " integer not null,"
	      + COLUMN_STATE+ " integer not null,"
	      + COLUMN_PEGCOUNT+ " integer not null);";

	  public GameSQLiteHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(MovementSQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
	    onCreate(db);
	  }
	  
	  /**
	   * TODO
	   */

	  // Adding new Game
	  void addGame(String game) {
	      SQLiteDatabase db = this.getWritableDatabase();

	      ContentValues values = new ContentValues();
	      values.put(COLUMN_GRID, game); // Contact Name
	      //values.put(COLUMN_PEGCOUNT, game.getPegCount()); // Contact Phone

	      // Inserting Row
	      db.insert(TABLE_GAMES, null, values);
	      db.close(); // Closing database connection
	  }
	// Getting single contact
	  public Game getGame(int id) {
		  return null;
	  }
	   
	  // Getting All Games
	  public List<Game> getAllContacts() {
		  return null;
	  }
	   
	  // Getting contacts Count
	  public int getGameCount() {
		  return 0;
	  }
	  // Updating single contact
	  public int updateGame(Game game) {
		  return 1;
	  }
	   
	  // Deleting single contact
	  public void deleteGame(Game game) {}


	} 