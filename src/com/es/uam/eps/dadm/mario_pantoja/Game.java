package com.es.uam.eps.dadm.mario_pantoja;





/**
 * @author marioandrei
 * MODEL
 */
public class Game {
	

	public static final int EUROPEAN = 33;
	public static final int ENGLISH = 37;

	public static final int ON = 1;
	public static final int OFF = 0;
	public static final int INVISIBLE = -1;
	public static final int SELECTED = 2;


	private enum STATE {Inactive, Active, Won, Drawn};

	private STATE gameState=STATE.Inactive;
	private int type=ENGLISH;
	
	private boolean selectionModeOn=false;
	private int []pivot = new int[]{-1,-1};

	//	private Position[][] grid; 
	private int[][] grid; 

	private int currentPlayer;
	private int pegCount;
	private int moveCount;

	/**
	 * @return the moveCount
	 */
	public int getMoveCount() {
		return moveCount;
	}
	/**
	 * @param moveCount the moveCount to set
	 */
	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}
	public int [] getPivot() {
		return pivot;
	}
	public int getPivotX() {
		return pivot[0];
	}
	public int getPivotY() {
		return pivot[1];
	}
	public void setPivot(int [] pivot) {
		this.pivot = pivot;
	}
	public void setPivot(int x, int y) {
		this.pivot[0] = x;
		this.pivot[1]=y;
	}
	/**
	 * @return the selectedPosition
	 */
	public boolean getSelectionModeOn() {
		return selectionModeOn;
	}
	/**
	 * @param selectedPosition the selectedPosition to set
	 */
	public void setSelectionModeOn(boolean selectionModeOn) {
		this.selectionModeOn = selectionModeOn;
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
	 * @return the pegCount
	 */
	public int getPegCount() {
		return pegCount;
	}
	/**
	 * @param pegCount the pegCount to set
	 */
	public void setPegCount(int peg_count) {
		this.pegCount = peg_count;
	}
	/**
	 * @param gameState
	 * @param tablero
	 * @param currentPlayer
	 * @param pegCount
	 * 
	 * If no type on the arguments, EUROPEAN board
	 */
	public Game() {
		//initialize grid 7x7 by default FRENCH
		this.setGame(new int[7][7]);
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				grid[i][j]=1;
			}
		}
		getGrid()[0][0]=-1;
		getGrid()[0][1]=-1;
		getGrid()[1][0]=-1;
		
		getGrid()[0][5]=-1;
		getGrid()[1][6]=-1;
		getGrid()[0][6]=-1;
		
		getGrid()[5][0]=-1;
		getGrid()[6][0]=-1;
		getGrid()[6][1]=-1;
		
		getGrid()[5][6]=-1;
		getGrid()[6][5]=-1;
		getGrid()[6][6]=-1;
		
		getGrid()[3][3]=0;
		
		
		this.gameState=STATE.Active;
		this.pegCount=EUROPEAN;
		this.currentPlayer=0;
	}
	
	public Game(int tipo){
		if (tipo==EUROPEAN){
			this.type=EUROPEAN;
			this.setGame(new int[7][7]);
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					getGrid()[i][j]=1;
				}
			}
			
			getGrid()[0][0]=-1;
			getGrid()[0][1]=-1;
			getGrid()[1][0]=-1;
			
			getGrid()[0][5]=-1;
			getGrid()[1][6]=-1;
			getGrid()[0][6]=-1;
			
			getGrid()[5][0]=-1;
			getGrid()[6][0]=-1;
			getGrid()[6][1]=-1;
			
			getGrid()[5][6]=-1;
			getGrid()[6][5]=-1;
			getGrid()[6][6]=-1;
			
			
			getGrid()[3][3]=0;

		}
		else if(tipo==ENGLISH){
			this.type=ENGLISH;

			this.setGame(new int[7][7]);
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					getGrid()[i][j]=1;
				}
			}
			
			getGrid()[0][0]=-1;
			getGrid()[0][1]=-1;
			getGrid()[1][0]=-1;
			
			getGrid()[0][5]=-1;
			getGrid()[1][6]=-1;
			getGrid()[0][6]=-1;
			
			getGrid()[5][0]=-1;
			getGrid()[6][0]=-1;
			getGrid()[6][1]=-1;
			
			getGrid()[5][6]=-1;
			getGrid()[6][5]=-1;
			getGrid()[6][6]=-1;
			
			
			getGrid()[1][5]=-1;
			getGrid()[5][1]=-1;
			getGrid()[1][1]=-1;
			getGrid()[5][5]=-1;

			getGrid()[3][3]=0;

		}
		
		this.updatePegCount();

		this.gameState=STATE.Active;
		this.currentPlayer=0;
	}
	
	
	public void updatePegCount(){
		int pegs=0;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (grid[i][j]==1)
					pegs++;
			}
		}
		this.setPegCount(pegs);
	}
	 
	public void play(int x0, int y0, int x, int y) {
		/* play occurs only if is a valid Move */
		
		// mark the old postion as OFF
		grid[x0][y0]=OFF;
		//mark the new position as ON
		grid[x][y]=ON;
		//depending on the direction of move, mark the position in between as OFF
		if (x0==x) {
			if (y0<y) {
				grid[x][y-1]=OFF;
			}
			else
				grid[x][y+1]=OFF;
		}else
		{
			if (x0<x) {
				grid[x-1][y]=OFF;
			}
			else
				grid[x+1][y]=OFF;
		}
		//check if the number of pegs is 1
		pegCount=pegCount-2;
		updatePegCount();
		checkGameState();
	}
	
	public void select(int x, int y) {
		//set the position as selected
        grid[x][y]=SELECTED;
        checkGameState();
	}
	
	private void checkGameState (){
		if (pegCount==1)
			gameState = STATE.Won;
		//gameState = STATE.Drawn;
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
	public int[][] getGrid() {
		return grid;
	}
	public void setGame(int[][] game) {
		this.grid = game;
	}
	public void setGameValue(int x,int y,int value){
		this.grid[x][y]=value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public boolean validMove(int x0, int y0, int x, int y) {
		/*
		 *  0 * *   
		 *  * * *
		 *  * * 0
		 * 
		 * */
		if(x0==x){
			if (y0<y) {
				/*destination must be 1 position further*/
				if ((y==y0+1) || (y!=y0+2) || (grid[x][y0+1]==0)) {
					return false;
				}
				return true;
			}else{
				if ((y0==y+1)|| (y0!=y+2) || (grid[x][y+1]==0)) {
					return false;
				}
				
				return true;
			}
	
			
		}else if(y0==y){
			if (x0<x) {
				/*destination must be 1 position further*/
				if ((x==x0+1) || (x!=x0+2) || (grid[x0+1][y]==0)) {
					return false;
				}
				return true;
			}else{
				if ((x0==x+1)|| (x0!=x+2) || (grid[x+1][y]==0)) {
					return false;
				}
				
				return true;
			}
			
		}
		else
			return false;
	} // end validMove
	

	
}
