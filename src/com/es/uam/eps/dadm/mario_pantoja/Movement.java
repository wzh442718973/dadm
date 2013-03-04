package com.es.uam.eps.dadm.mario_pantoja;

import java.util.Date;

public class Movement {
	private int origin[][]=new int[1][1];
	private int destination[][]= new int[1][1];;
	private int player;
	/**
	 * @return the origin
	 */
	public int[][] getOrigin() {
		return origin;
	}
	/**
	 * @return the destination
	 */
	public int[][] getDestination() {
		return destination;
	}
	/**
	 * @return the player
	 */
	public int getPlayer() {
		return player;
	}
	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(int[][] origin) {
		this.origin = origin;
	}
	/**
	 * @param destination the destination to set
	 */
	public void setDestination(int[][] destination) {
		this.destination = destination;
	}
	/**
	 * @param player the player to set
	 */
	public void setPlayer(int player) {
		this.player = player;
	}
	/**
	 * @param origin
	 * @param destination
	 * @param player
	 */
	public Movement(int[][] origin, int[][] destination, int player) {
		this.origin = origin;
		this.destination = destination;
		this.player = player;
	}
	
}
