package com.es.uam.eps.dadm.mario_pantoja;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
	public static final String COLUMN_CURRENTPLAYER = "player";
	public static final String COLUMN_STATE = "state";
	public static final String COLUMN_PEGCOUNT = "pegs";



	private static final String DATABASE_NAME = "games.db";

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_GAMES
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_GRID + " text," + COLUMN_CURRENTPLAYER + " integer,"
			+ COLUMN_STATE + " integer," + COLUMN_PEGCOUNT + " integer);";

	public GameSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("DROP TABLE IF EXISTS TABLE_GAMES");
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MovementSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		//
		onCreate(db);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
		//getAllContacts();
	}

	/**
	 * TODO
	 */

	// Adding new Game
	void addGame(Game game) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_GRID,
				game.arrayToString(game.getCurrentBoardStateArray())); // grid
																		// in
																		// string
		values.put(COLUMN_PEGCOUNT, game.getPegCount()); // number of pegs left
		Log.w(MovementSQLiteHelper.class.getName(),
				"pegs left inserted in db: (" + game.getPegCount() + ") ");
		Log.w(MovementSQLiteHelper.class.getName(),
				"grid:" + game.arrayToString(game.getCurrentBoardStateArray()));
		// Inserting Row
		db.insert(TABLE_GAMES, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	public Game getGame(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_GAMES, new String[] { COLUMN_ID, COLUMN_GRID,
				COLUMN_PEGCOUNT }, COLUMN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		Game game=new Game(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
		//Game game = new Game(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
		return game;

	}

	// Getting All Games
	public List<Game> getAllContacts() {
		
		
		   List<Game> contactList = new ArrayList<Game>();
		   
	        // Select All Query
	        String selectQuery = "SELECT  * FROM " + TABLE_GAMES;
	 
	        SQLiteDatabase db = this.getWritableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);
	 
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            do {
	        		Log.w(MovementSQLiteHelper.class.getName(),
	        				"ID:" + cursor.getString(0)+ 
	        				"grid" +cursor.getString(1)+
	        				"pegs"+cursor.getString(2));
	        		
	            } while (cursor.moveToNext());
	        }
	 
	        // return contact list
	        
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
	public void deleteGame(Game game) {
	}

}