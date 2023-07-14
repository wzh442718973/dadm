package com.glaring.colourful.bully;


public class Position {
	
	private final int ON = 1;	
	private final int OFF = 0;
	
	private int x;
	private int y;
	
	private int estado;

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public Position() {
		this.setEstado(OFF);
	}
	
	public void setON() {
		this.setEstado(ON);
	}
}


