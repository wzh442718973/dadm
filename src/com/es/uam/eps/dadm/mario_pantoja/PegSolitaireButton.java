/**
 * 
 */
package com.es.uam.eps.dadm.mario_pantoja;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * @author marioandrei
 *
 */
public class PegSolitaireButton extends Button	{

	
	
	/**
	 * 
	 */
	public PegSolitaireButton(Context context, AttributeSet attrs) {
	 super(context,attrs);
	 reset();
	}

	public void reset(){
	       //setText(" ");
	       //setBackgroundColor(Color.DKGRAY);
	}
	
	
	public void drawOn(){
        this.setBackgroundResource(R.drawable.on);
   
	}
	public void drawOff(){
        this.setBackgroundResource(R.drawable.off);
	}
	public void drawSelected(){
        this.setBackgroundResource(R.drawable.selected);
	}
}
