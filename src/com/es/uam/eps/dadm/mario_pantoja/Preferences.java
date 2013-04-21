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

	private final static String PLAYER_KEY = "username";
	private final static String PLAYER_DEFAULT = "First player";
	
	private final static String FIGURE_KEY = "figure_pref";
	private final static String FIGURE_DEFAULT = "basic";
	
	private final static String WIFI_KEY = "wifi";
	private final static boolean WIFI_DEFAULT = false;
	
	private final static String LOGGED_KEY = "logged";
	private final static boolean LOGGED_DEFAULT = false;

	private final static String SOUND_KEY = "sound";
	private final static boolean SOUND_DEFAULT = false;
	
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
	
	public static int getType(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getInt(TYPE_KEY, TYPE_DEFAULT);
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


}