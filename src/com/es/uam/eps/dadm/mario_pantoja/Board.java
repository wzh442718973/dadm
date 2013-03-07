package com.es.uam.eps.dadm.mario_pantoja;

import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

public class Board extends View {
    private Game game;
    //TODO
	public Board(Context context, AttributeSet attributes) {
		
	      super(context, attributes);
	      
	      setFocusable(true);
	      
	      setFocusableInTouchMode(true);
	      
	      game = new Game();
	        
	}

}
