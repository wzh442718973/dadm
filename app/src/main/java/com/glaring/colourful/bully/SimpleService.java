package com.glaring.colourful.bully;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;



public class SimpleService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		//task("first");
		//new BackgroundTask().execute("first","second","third");
		//Toast.makeText(this, "task done", Toast.LENGTH_SHORT).show();
		Music.play(this, R.raw.moog);

		return START_STICKY;
	}
	
	/** 
	 * 
	 * @param string
	 * @return
	 */
	private int task(String string) {
		try {
			Thread.sleep(string.length()*1000);
		} catch (InterruptedException e) {
		e.printStackTrace();
		}		
		return string.length();
	}

	public void onDestroy() {
		super.onDestroy();
		Music.stop(this);

		//Toast.makeText(this, "service stopped", Toast.LENGTH_SHORT).show();
	}
	
	public class BackgroundTask extends AsyncTask<String, Integer, Long>{

		@Override
		protected Long doInBackground(String... strings) {
			int count = strings.length;
			long total=0;
			for (int i = 0; i < count; i++) {
				total+=task(strings[i]);
				publishProgress((int) (((i+1)/(float) count) *100));
			}
			return total;
		}
		
		protected void onPreExecute() {
			
		}
		protected void onProgressUpdate(Integer... progress) {
			Toast.makeText(getBaseContext(), String.valueOf(progress[0])+" % done", Toast.LENGTH_LONG).show();

		}
		protected void onPostExecute(Long result) {
			Toast.makeText(getBaseContext(), "done "+result+" characters", Toast.LENGTH_LONG).show();
			stopSelf();

		}
		protected void onCancelled() {
			
		}
	}


	
}
