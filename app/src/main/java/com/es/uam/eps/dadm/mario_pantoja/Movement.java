package com.es.uam.eps.dadm.mario_pantoja;

import java.util.Calendar;


public class Movement {
	private int origin[]=new int[2];
	private int destination[]= new int[2];
	private int player;
	final Calendar c ;
	

	public Movement( int x0, int y0, int x, int y, int player) {
		this.origin[0]=x0;
		this.origin[1]=y0;
		
		this.destination[0]=x0;
		this.destination[1]=y0;
		setPlayer(player);
        c= Calendar.getInstance();
}


	/**
	 * @return the player
	 */
	public int getPlayer() {
		return player;
	}


	/**
	 * @param player the player to set
	 */
	public void setPlayer(int player) {
		this.player = player;
	}	
}
