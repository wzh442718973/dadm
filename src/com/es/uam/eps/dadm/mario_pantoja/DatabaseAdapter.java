package com.es.uam.eps.dadm.mario_pantoja;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseAdapter {
	
	public static final String ID = "_id";
	
	/* USERS */
	public static final String NAME = "username";
	public static final String PASSWORD = "password";
	
	/* GAMES */

	public static final String BOARD = "board";
	public static final String SECONDS = "seconds";
	public static final String PEGCOUNT = "pegcount";
	public static final String DATE = "date";
	
	/* BOARDS */

	public static final String BOARDNAME = "boardname";
	public static final String NUMBEROFPEGS = "pegcount";
	
	

	private static final String DATABASE_NAME = "pegsolitaire.db";
	
	private static final String TABLE_NAME_USERS = "users";
	private static final String TABLE_NAME_GAMES = "games";
	private static final String TABLE_NAME_BOARDS = "boards";

	private static final int DATABASE_VERSION = 1;
	
	private final Context context;
	private DatabaseHelper helper;
	private SQLiteDatabase db;
	
	public DatabaseAdapter(Context context){
		this.context=context;
		helper= new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	
		@Override
		public void onCreate(SQLiteDatabase database){
            Log.v("INFO1","creating db");
			createTable(database);
		}



		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("DatabaseAdapter","Upgrading database from version "+oldVersion+" to version"+newVersion);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_USERS);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_GAMES);
			db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_BOARDS);


			createTable(db);
		}
		
		
		
		private void createTable(SQLiteDatabase db) {
			String str = "CREATE TABLE "+TABLE_NAME_USERS+" ( "+ID+
					" INTEGER PRIMARY KEY AUTOINCREMENT, "+ NAME +" TEXT NOT NULL, "+ PASSWORD + " TEXT NOT NULL);";
			
			
            Log.v("DBLOG"," execSQL :"+str);

			try {
				db.execSQL(str);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			String str2 = "CREATE TABLE "+TABLE_NAME_GAMES+" ( "+
					ID		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
					NAME	+ " TEXT NOT NULL, "+
					SECONDS	+ " INTEGER NOT NULL, "+
					DATE	+ " TEXT NOT NULL, "+
					PEGCOUNT+ " INTEGER NOT NULL, "+
					BOARD	+ " TEXT NOT NULL);";
			
			
            Log.v("DBLOG"," exexSQL :"+str2);

			try {
				db.execSQL(str2);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			String str3 = "CREATE TABLE "+TABLE_NAME_BOARDS+" ( "+
					ID		+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
					BOARDNAME+ " TEXT NOT NULL, "+
					BOARD	+ " TEXT NOT NULL);";
			
			
            Log.v("DBLOG"," exexSQL :"+str3);

			try {
				db.execSQL(str3);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}	
	public DatabaseAdapter open() throws SQLException{
		db = helper.getWritableDatabase();
		return this;
		
	}
	
	public void close() {
		helper.close();
	}
	
	public long insertUser(String username, String password) {
		ContentValues values = new ContentValues();
		values.put(NAME, username);
		values.put(PASSWORD, password);
		return db.insert(TABLE_NAME_USERS, null, values);
	}
	
	public boolean deleteUser(long id) {
		return db.delete(TABLE_NAME_USERS, ID+"="+id, null) >0 ;
	}
	public Cursor getAllUsers(){
		return db.query(TABLE_NAME_USERS, new String []{ID,NAME,PASSWORD},null, null, null,null, null);
	}

	public boolean isRegistered(String username, String password) {
		boolean in=false;
		Cursor cursor = db.query(TABLE_NAME_USERS, new String [] {NAME,PASSWORD}, 
								NAME + " = '"+ username + "' AND "+ PASSWORD+ "= '"+ password+ "'",
								null, null, null, NAME+ " DESC");
		if (cursor.moveToFirst()) {
			in=true;
		}
		if (!cursor.isClosed()) {
			cursor.close();
		}
		return in;
	}

	
		public long insertGame(Game game, String username) {
			ContentValues values = new ContentValues();
			values.put(NAME, username);
			if (game.getType()==Game.ENGLISH) {
				values.put(BOARD, "English Board");

			}else
				values.put(BOARD, "European Board");

			//values.put(BOARD, game.getBoard());
			values.put(SECONDS, game.getSeconds());
			values.put(PEGCOUNT, game.getPegCount());
			values.put(DATE, game.getDate());
			return db.insert(TABLE_NAME_GAMES, null, values);
		}
		
		public boolean deleteGame(long id) {
			return db.delete(TABLE_NAME_GAMES, ID+"="+id, null) >0 ;
		}
		public Cursor getAllGames(){
			return db.query(TABLE_NAME_GAMES, new String []{ID,NAME,BOARD,SECONDS,PEGCOUNT,DATE},null, null, null,null, null);
		}

		public boolean gamesByUser(String username) {
			boolean in=false;
			Cursor cursor = db.query(TABLE_NAME_GAMES, new String [] {NAME,BOARD,SECONDS,PEGCOUNT,DATE}, 
									NAME + " = '"+ username+ "' ",
									null, null, null, SECONDS+ " DESC");
			if (cursor.moveToFirst()) {
				in=true;
			}
			if (!cursor.isClosed()) {
				cursor.close();
			}
			return in;
		}
		public Cursor gamesRecent() {

			return db.query(TABLE_NAME_GAMES, new String [] {ID,NAME,BOARD,SECONDS,PEGCOUNT,DATE}, 
									null,
									null, null, null, ID+ " ASC","3");
			}
		
		public Cursor gamesTopTen() {

			return db.query(TABLE_NAME_GAMES, new String [] {NAME,BOARD,SECONDS,DATE}, 
									PEGCOUNT + " = '1' ",
									null, null, null, SECONDS+ " ASC ","10");
			}
	public Context getContext() {
		return context;
	}
	
	
	private static final String board_english_basic =		"-1,-1,1,1,1,-1,-1,"+
			"-1,-1,1,1,1,-1,-1,"+
			"1,1,1,1,1,1,1,"+
			"1,1,1,0,1,1,1,"+
			"1,1,1,1,1,1,1,"+
			"-1,-1,1,1,1,-1,-1,"+
			"-1,-1,1,1,1,-1,-1";

	private static final String board_european_basic =		"-1,-1,1,1,1,-1,-1,"+
			"-1,1,1,1,1,1,-1,"+
			"1,1,1,1,1,1,1,"+
			"1,1,1,0,1,1,1,"+
			"1,1,1,1,1,1,1,"+
			"-1,1,1,1,1,1,-1,"+
			"-1,-1,1,1,1,-1,-1";

	public long insertBoard(String name, String board) {
		ContentValues values = new ContentValues();
		values.put(BOARDNAME, name);
		values.put(BOARD, board);
		return db.insert(TABLE_NAME_BOARDS, null, values);
	}
	
	void insertBoards(){
		insertBoard("English", board_english_basic);
		insertBoard("European", board_european_basic);
	}

}
