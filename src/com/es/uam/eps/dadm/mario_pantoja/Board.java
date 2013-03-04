package com.es.uam.eps.dadm.mario_pantoja;


public class Board {
	
	private static final int ON = 1;
	private static final int OFF = 0;
	public static final int FRANCES = 0;
	public static final int INGLES = 1;

	private final int TIPO_1 = 33;
	private final int TIPO_2 = 37;
	
	final char peg = 'x';
	final char hole = ' ';
	
	private enum STATE {Inactive, Active, Won, Drawn};

	private STATE gameState=STATE.Inactive;
	private int type=INGLES;
	
	private int selectionMode=-1;
		//	private Position[][] board; 
	private int[][] board; 

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
	public Board() {
		//initialize board 7x7
		this.setBoard(new int[7][7]);
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				board[i][j]=1;
			}
		}
		getBoard()[3][3]=0;
		
		
		this.gameState=STATE.Active;
		this.peg_count=TIPO_1-1;
		this.currentPlayer=0;
	}
	
	public Board(int tipo){
		if (tipo==FRANCES){
			this.setBoard(new int[7][7]);
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					getBoard()[i][j]=1;
				}
			}
			getBoard()[3][3]=0;
		}
		else if(tipo==INGLES){
			this.setBoard(new int[7][7]);
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					getBoard()[i][j]=1;
				}
			}
			getBoard()[1][5]=0;
			getBoard()[5][1]=0;
			getBoard()[1][1]=0;
			getBoard()[5][5]=0;

			getBoard()[3][3]=0;
		}
	}
	
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
	public int[][] getBoard() {
		return board;
	}
	public void setBoard(int[][] board) {
		this.board = board;
	}
	public void setBoardValue(int x,int y,int value){
		this.board[x][y]=value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
