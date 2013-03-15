package com.es.uam.eps.dadm.mario_pantoja;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovementSQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_MOVES = "moves";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_X0 = "x0";
  public static final String COLUMN_Y0= "y0";
  public static final String COLUMN_X = "x";
  public static final String COLUMN_Y= "y";  

  private static final String DATABASE_NAME = "moves.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_MOVES + "(" + COLUMN_ID
      + " integer primary key autoincrement, "
      + COLUMN_X0+ " integer not null,"
      + COLUMN_Y0+ " integer not null,"
      + COLUMN_X+ " integer not null,"
      + COLUMN_Y+ " integer not null);";

  public MovementSQLiteHelper(Context context) {
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
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVES);
    onCreate(db);
  }

  
}