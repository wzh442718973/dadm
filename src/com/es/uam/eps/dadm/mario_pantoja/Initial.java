package com.es.uam.eps.dadm.mario_pantoja;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class Initial extends Activity implements OnClickListener{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.initial);
		setContentView(R.layout.main);
		Button button = (Button) findViewById(R.id.newgamebtn);
		button.setOnClickListener( this);
		button = (Button) findViewById(R.id.about);
		button.setOnClickListener( this);
	}
	
	/*public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction()== MotionEvent.ACTION_DOWN) {
			startActivity(new Intent("com.es.uam.eps.dadm.mario_pantoja.SESSION"));
		}
		return true;
	}*/

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.newgamebtn) {

			startActivity(new Intent("com.es.uam.eps.dadm.mario_pantoja.SESSION"));

		}
		else if (v.getId()==R.id.about) {
			Intent intent= new Intent(android.content.Intent.ACTION_VIEW,Uri.parse("http://www.marioandrei.com"));
			startActivity(intent);
		}
	}
	

}
