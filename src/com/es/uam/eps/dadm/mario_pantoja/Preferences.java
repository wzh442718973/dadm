package com.es.uam.eps.dadm.mario_pantoja;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Preferences extends PreferenceActivity {
	private final static String TYPE_KEY = "type";
	private final static int TYPE_DEFAULT = Game.ENGLISH ;

	private final static String PLAY_MUSIC_KEY = "music";
	private final static boolean PLAY_MUSIC_DEFAULT = false;

	public final static String PLAYER_KEY = "playername";
	public final static String PLAYER_DEFAULT = "The Player";

	public final static String PASSWORD_KEY = "playerpassword";
	public final static String PASSWORS_DEFAULT = "a";
	
	private final static String FIGURE_KEY = "figure_pref";
	private final static String FIGURE_DEFAULT = "basic";
	
	private final static String ONLINE_FIGURE_KEY = "online_figure";
	private final static String ONLINE_FIGURE_DEFAULT = "none";
	
	public final static String WIFI_KEY = "wifi";
	private final static boolean WIFI_DEFAULT = false;
	
	private final static String LOGGED_KEY = "logged";
	private final static boolean LOGGED_DEFAULT = false;

	private final static String SOUND_KEY = "sound";
	private final static boolean SOUND_DEFAULT = false;
	
	public final static String PLAYER_ID_KEY = "PLAYER_ID";
	public final static String PLAYER_ID_DEFAULT = "-1";
	
	public final static String UUID_KEY="uuid";
	public final static String UUID_DEFAULT="-1";


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		/*create the preferences from */
	    Preference custom = (Preference) findPreference("figure");
	    custom.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				
	        @Override
	        public boolean onPreferenceClick(Preference preference) {
		 //   Toast.makeText(Preferences.this, "custom preference clicked", Toast.LENGTH_LONG).show();
				Intent intent = new Intent("com.es.uam.eps.dadm.mario_pantoja.FIGURES"); 
				startActivity(intent);
		    return true;
	        }
	     });

	}

	

	public static String getFigure(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(FIGURE_KEY, FIGURE_DEFAULT);
	}
	public static String getOnlineFigure(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(ONLINE_FIGURE_KEY, ONLINE_FIGURE_DEFAULT);
	}	
	public static int getType(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getInt(TYPE_KEY, TYPE_DEFAULT);
	}
	public static String getId(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("PLAYER_ID", "-1");
	}
	public static boolean playMusic(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(PLAY_MUSIC_KEY, PLAY_MUSIC_DEFAULT);
	}
	
	
	public static boolean playSound(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(SOUND_KEY, SOUND_DEFAULT);
	}

	public static String getPlayerName(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PLAYER_KEY, PLAYER_DEFAULT);
	}
	
	
	//to do, store the password hash, no the password
	public static String getPlayerPassword(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(PASSWORD_KEY, PASSWORS_DEFAULT);
	}
	
	public static boolean isWifiConnected(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(WIFI_KEY, WIFI_DEFAULT);
	}
	public static boolean isUserLogged(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(LOGGED_KEY, LOGGED_DEFAULT);
	}
	public static void setIfUserIsLogged(Context context, boolean logged){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor= settings.edit();
		editor.putBoolean("logged",logged);
		editor.commit();
	}
	public static void setId(Context context, String responseBody){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor= settings.edit();
		editor.putString("PLAYER_ID",responseBody);
		editor.commit();
	}
	
	
	public static void setOnlineFigure(Context context, String figure){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor= settings.edit();
		editor.putString(ONLINE_FIGURE_KEY,figure);
		editor.commit();
	}
	
	public static void setFigure(Context context, String figure){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor= settings.edit();
		editor.putString(FIGURE_KEY,figure);
		editor.commit();
	}

	
	public static void setPlayerName(Context context, String name){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor= settings.edit();
		editor.putString(PLAYER_KEY,name);
		editor.commit();
	}
	
	public static void setPlayerPassword(Context context, String password){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor= settings.edit();
		editor.putString(PASSWORD_KEY,password);
		editor.commit();
	}
	
	
	public static void setUUID(Context context, String name){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor= settings.edit();
		editor.putString(UUID_KEY,name);
		editor.commit();
	}

	public static String getUUID(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(UUID_KEY, UUID_DEFAULT);
	}



	public static String getDuration(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("duration", "1984");
	}



	public static String getNumberoftiles(Context applicationContext) {
		return PreferenceManager.getDefaultSharedPreferences(applicationContext)
				.getString("numeroftiles", "37");
	}



	public static String getFigureName(Context applicationContext) {
		return PreferenceManager.getDefaultSharedPreferences(applicationContext)
				.getString("figurename", "completo");
	}



	public static String getDate(Context applicationContext) {
		return PreferenceManager.getDefaultSharedPreferences(applicationContext)
				.getString("date", "1984");
	}
	
//////

	public static void setDuration(Context context, String duration) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor= settings.edit();
		editor.putString("duration",duration);
		editor.commit();
	}



	public static void setNumberoftiles(Context applicationContext, String string) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor= settings.edit();
		editor.putString("numberoftiles",string);
		editor.commit();
	}



	public static void setFigureName(Context applicationContext, String figuername) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor= settings.edit();
		editor.putString("figurename",figuername);
		editor.commit();
	}



	public static void setDate(Context applicationContext, String date) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor= settings.edit();
		editor.putString("date",date);
		editor.commit();
	}
	
	
}