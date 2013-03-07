package com.es.uam.eps.dadm.mario_pantoja;


/**
 * @author marioandrei
 * MODEL
 */
public class Game {
	

	public static final int FRANCES = 33;
	public static final int INGLES = 37;


	
	final char peg = 'x';
	final char hole = ' ';
	
	private enum STATE {Inactive, Active, Won, Drawn};

	private STATE gameState=STATE.Inactive;
	private int type=INGLES;
	
	private int selectionMode=-1;
		//	private Position[][] game; 
	private int[][] game; 

	private int currentPlayer;
	private int peg_count;
/**
	 * @return the selected
	 */
	public int getSelectionMode() {
		return selectionMode;
	}
	/**
	 * @param selected the selected to set
	 */
	public void setSelectionMode(int selected) {
		this.selectionMode = selected;
	}

	/**
	 * @return the gameState
	 */
	public STATE getGameState() {
		return gameState;
	}
	/**
	 * @param gameState the gameState to set
	 */
	public void setGameState(STATE gameState) {
		this.gameState = gameState;
	}
	/**
	 * @return the tablero
	 */

	/**
	 * @return the currentPlayer
	 */
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	/**
	 * @param currentPlayer the currentPlayer to set
	 */
	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	/**
	 * @return the peg_count
	 */
	public int getPeg_count() {
		return peg_count;
	}
	/**
	 * @param peg_count the peg_count to set
	 */
	public void setPeg_count(int peg_count) {
		this.peg_count = peg_count;
	}
	/**
	 * @param gameState
	 * @param tablero
	 * @param currentPlayer
	 * @param peg_count
	 */
	public Game() {
		//initialize game 7x7 by default ENGLISH
		this.setGame(new int[7][7]);
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				game[i][j]=1;
			}
		}
		getGame()[0][0]=0;
		getGame()[0][1]=0;
		getGame()[1][0]=0;
		
		getGame()[0][5]=0;
		getGame()[1][6]=0;
		getGame()[0][6]=0;
		
		getGame()[5][0]=0;
		getGame()[6][0]=0;
		getGame()[6][1]=0;
		
		getGame()[5][6]=0;
		getGame()[6][5]=0;
		getGame()[6][6]=0;
		
		getGame()[3][3]=0;
		
		
		this.gameState=STATE.Active;
		this.peg_count=INGLES;
		this.currentPlayer=0;
	}
	
	public Game(int tipo){
		if (tipo==FRANCES){
			this.type=FRANCES;
			this.setGame(new int[7][7]);
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					getGame()[i][j]=1;
				}
			}
			
			getGame()[0][0]=0;
			getGame()[0][1]=0;
			getGame()[1][0]=0;
			
			getGame()[0][5]=0;
			getGame()[1][6]=0;
			getGame()[0][6]=0;
			
			getGame()[5][0]=0;
			getGame()[6][0]=0;
			getGame()[6][1]=0;
			
			getGame()[5][6]=0;
			getGame()[6][5]=0;
			getGame()[6][6]=0;
			
			
			getGame()[3][3]=0;

		}
		else if(tipo==INGLES){
			this.type=INGLES;

			this.setGame(new int[7][7]);
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					getGame()[i][j]=1;
				}
			}
			
			getGame()[0][0]=0;
			getGame()[0][1]=0;
			getGame()[1][0]=0;
			
			getGame()[0][5]=0;
			getGame()[1][6]=0;
			getGame()[0][6]=0;
			
			getGame()[5][0]=0;
			getGame()[6][0]=0;
			getGame()[6][1]=0;
			
			getGame()[5][6]=0;
			getGame()[6][5]=0;
			getGame()[6][6]=0;
			
			
			getGame()[1][5]=0;
			getGame()[5][1]=0;
			getGame()[1][1]=0;
			getGame()[5][5]=0;

			getGame()[3][3]=0;

		}
		
		this.updatePegCount();

		this.gameState=STATE.Active;
		this.currentPlayer=0;
	}
	
	
	public void updatePegCount(){
		int pegs=0;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (game[i][j]==1)
					pegs++;
			}
		}
		this.setPeg_count(pegs);
	}
	
	//TODO fromIdToPosition is exclusive of MainActivity
	public void play() {
		
	}
	
	public Boolean isActive() {
		return this.getGameState()==STATE.Active;
	}
	
	public Boolean isDrawn() {
		return gameState==STATE.Drawn;
	}
	
	public Boolean isWon() {
		return this.getGameState()==STATE.Won;
	}
	public int[][] getGame() {
		return game;
	}
	public void setGame(int[][] game) {
		this.game = game;
	}
	public void setGameValue(int x,int y,int value){
		this.game[x][y]=value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
