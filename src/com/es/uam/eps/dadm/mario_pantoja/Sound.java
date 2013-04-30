package com.es.uam.eps.dadm.mario_pantoja;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Sound  {

	private Context context;


    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundsMap;

    int SOUNDON=1;
    int SOUNDOFF=2;
    int SOUNDMOVE=3;
    
	public Sound(Context context){
		this.setContext(context);
		// assign to the pool the
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundsMap = new HashMap<Integer, Integer>();
		soundsMap.put(SOUNDON, soundPool.load(context, R.raw.on, 1));
		soundsMap.put(SOUNDOFF, soundPool.load(context, R.raw.off, 1));
		soundsMap.put(SOUNDMOVE, soundPool.load(context, R.raw.move, 1));

		
	}
		public void playSound(int sound, float fSpeed){
			AudioManager mgr = (AudioManager)context.getSystemService(android.content.Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = streamVolumeCurrent / streamVolumeMax;
			/* 1st arg of play() is the selection of a sound, which in this case we get from a HashMap.get sound sound being an int.
			 *   2nd and 3rd args are left and right volumes
			 *   4th is priority, 1 the highest, 0  the lowest
			 *   5th is the looping value which we’ve set to 0, meaning “don’t loop”. 
			 *   6th is the playback speed */
			boolean soundPref = Preferences.playSound(context);
			if (soundPref) {
				soundPool.play(soundsMap.get(sound), volume, volume, 1, 0, fSpeed);
			}
		}
		/**
		 * @return the context
		 */
		public Context getContext() {
			return context;
		}
		/**
		 * @param context the context to set
		 */
		public void setContext(Context context) {
			this.context = context;
		}
			
}