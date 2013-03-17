package com.es.uam.eps.dadm.mario_pantoja;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;





/**
 * @author marioandrei
 * MODEL
 */
public class Game {
	
private Context context;
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
	private int []destination = new int[]{-1,-1};

	//	private Position[][] grid; 
	private int[][] grid; 
	private int[] currentBoardStateArray=new int[49];


	private int currentPlayer;
	private int pegCount;
	private int moveCount;
	
	
	//DB
	GameSQLiteHelper db;

	

	public int [] getDestination() {
		return destination;
	}
	public void setDestination(int [] destination) {
		this.destination = destination;
	}
	/**
	 * @return the currentBoardStateArray
	 */
	public int[] getCurrentBoardStateArray() {
		return currentBoardStateArray;
	}
	/**
	 * @param currentBoardStateArray the currentBoardStateArray to set
	 */
	public void setCurrentBoardStateArray(int[] currentBoardState) {
		this.currentBoardStateArray = currentBoardState;
	}
	
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
	public Game( Context context) {
		//initialize grid 7x7 by default EUROPEAN
		this.setGame(new int[7][7]);
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				grid[i][j]=ON;
			}
		}
		getGrid()[0][0]=INVISIBLE;
		getGrid()[0][1]=INVISIBLE;
		getGrid()[1][0]=INVISIBLE;
		
		getGrid()[0][5]=INVISIBLE;
		getGrid()[1][6]=INVISIBLE;
		getGrid()[0][6]=INVISIBLE;
		
		getGrid()[5][0]=INVISIBLE;
		getGrid()[6][0]=INVISIBLE;
		getGrid()[6][1]=INVISIBLE;
		
		getGrid()[5][6]=INVISIBLE;
		getGrid()[6][5]=INVISIBLE;
		getGrid()[6][6]=INVISIBLE;
		
		getGrid()[3][3]=OFF;
		
		
		this.gameState=STATE.Active;
		this.pegCount=EUROPEAN;
		this.currentPlayer=0;

		
		//GameSQLiteHelper db = new GameSQLiteHelper(context);
	}
	
	public Game(Context context, int tipo){
		if (tipo==EUROPEAN){
			this.type=EUROPEAN;
			this.context=context;
			this.setGame(new int[7][7]);
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					getGrid()[i][j]=ON;
				}
			}
			getGrid()[0][0]=INVISIBLE;
			getGrid()[0][1]=INVISIBLE;
			getGrid()[1][0]=INVISIBLE;
			
			getGrid()[0][5]=INVISIBLE;
			getGrid()[1][6]=INVISIBLE;
			getGrid()[0][6]=INVISIBLE;
			
			getGrid()[5][0]=INVISIBLE;
			getGrid()[6][0]=INVISIBLE;
			getGrid()[6][1]=INVISIBLE;
			
			getGrid()[5][6]=INVISIBLE;
			getGrid()[6][5]=INVISIBLE;
			getGrid()[6][6]=INVISIBLE;
			
			
			getGrid()[3][3]=OFF;

		}
		else if(tipo==ENGLISH){
			this.type=ENGLISH;

			this.setGame(new int[7][7]);
			for (int i = 0; i < 7; i++) {
				for (int j = 0; j < 7; j++) {
					getGrid()[i][j]=ON;
				}
			}
			
			getGrid()[0][0]=INVISIBLE;
			getGrid()[0][1]=INVISIBLE;
			getGrid()[1][0]=INVISIBLE;
			
			getGrid()[0][5]=INVISIBLE;
			getGrid()[1][6]=INVISIBLE;
			getGrid()[0][6]=INVISIBLE;
			
			getGrid()[5][0]=INVISIBLE;
			getGrid()[6][0]=INVISIBLE;
			getGrid()[6][1]=INVISIBLE;
			
			getGrid()[5][6]=INVISIBLE;
			getGrid()[6][5]=INVISIBLE;
			getGrid()[6][6]=INVISIBLE;
			
			
			getGrid()[1][5]=INVISIBLE;
			getGrid()[5][1]=INVISIBLE;
			getGrid()[1][1]=INVISIBLE;
			getGrid()[5][5]=INVISIBLE;

			getGrid()[3][3]=OFF;

		}
		
		this.updatePegCount();

		this.gameState=STATE.Active;
		this.currentPlayer=0;
	}
	/**
	 * TODO
	 * 
	 */
	public void saveGrid(){
		
	}
	public String posibilities(ArrayList<int[]> posibleDestinations) {
		String string= "x,y = ";
		for (int[] is : posibleDestinations) {
			string=string+" "+Integer.toString(is[0])+","+Integer.toString(is[1]);
		}
		return string;
	}
	public ArrayList<int[]> posibleDestinations(int x, int y){
		int northX, southX, westX, eastX,nwX,neX,seX,swX;
		int northY, southY, westY, eastY,nwY,neY,seY,swY;
		northX=x;
		northY=y-1;
		southX=x;
		southY=y+1;
		westX=x+1;
		westY=y;
		eastX=x-1;
		eastY=y;
		ArrayList<int[]> destinations=new ArrayList<int[]>();
		/* from grid 2,2 to grid 4 4*/
		if ( (x>1) && (y>1)&&(x<5)&&(y<5) ) {
			
			if((grid[northX][northY]==ON) && (grid[northX][northY-1]==OFF)){
				int[] aux=new int[2];

					aux[0]=northX;
					aux[1]=northY-1;
					destinations.add(aux);
			}
			if((grid[southX][southY]==ON) && (grid[southX][southY+1]==OFF)){
				int[] aux=new int[2];

				aux[0]=southX;
				aux[1]=southY+1;
				destinations.add(aux);
			}
			if((grid[westX][westY]==ON) && (grid[westX+1][westY]==OFF)){
				int[] aux=new int[2];

				aux[0]=westX+1;
				aux[1]=westY;
				destinations.add(aux);
			}
			if((grid[eastX][eastY]==ON) && (grid[eastX-1][eastY]==OFF)){
				int[] aux=new int[2];

				aux[0]=eastX-1;
				aux[1]=eastY;
				destinations.add(aux);
			}
		/* for the  row 1 */
		}else if (y==1 && x<=5 && x>=1) {
			
			if (x==1) {
				if((grid[southX][southY]==ON) && (grid[southX][southY+1]==OFF)){
					int[] aux=new int[2];

					aux[0]=southX;
					aux[1]=southY+1;
					destinations.add(aux);
				}
				if((grid[westX][westY]==ON) && (grid[westX+1][westY]==OFF)){
					int[] aux=new int[2];

					aux[0]=westX+1;
					aux[1]=westY;
					destinations.add(aux);
				}
			}
			else if (x==5) {
				if((grid[southX][southY]==ON) && (grid[southX][southY+1]==OFF)){
					int[] aux=new int[2];

					aux[0]=southX;
					aux[1]=southY+1;
					destinations.add(aux);
				}
				if((grid[eastX][eastY]==ON) && (grid[eastX-1][eastY]==OFF)){
					int[] aux=new int[2];

					aux[0]=eastX-1;
					aux[1]=eastY;
					destinations.add(aux);
				}
			}
			else if((grid[southX][southY]==ON) && (grid[southX][southY+1]==OFF)){
				int[] aux=new int[2];

				aux[0]=southX;
				aux[1]=southY+1;
				destinations.add(aux);
			}
			if((grid[westX][westY]==ON) && (grid[westX+1][westY]==OFF)){
				int[] aux=new int[2];

				aux[0]=westX+1;
				aux[1]=westY;
				destinations.add(aux);
			}
			if((grid[eastX][eastY]==ON) && (grid[eastX-1][eastY]==OFF)){
				int[] aux=new int[2];

				aux[0]=eastX-1;
				aux[1]=eastY;
				destinations.add(aux);
			}
		/*for the row 5 */	
		}else if (y==5 && x<=5 && x>=1) {
			
			if (x==1) {
				if((grid[northX][northY]==ON) && (grid[northX][northY-1]==OFF)){
					int[] aux=new int[2];

					aux[0]=northX;
					aux[1]=northY-1;
					destinations.add(aux);
				}
				if((grid[westX][westY]==ON) && (grid[westX+1][westY]==OFF)){
					int[] aux=new int[2];

					aux[0]=westX+1;
					aux[1]=westY;
					destinations.add(aux);
				}
			}
			else if (x==5) {
				if((grid[northX][northY]==ON) && (grid[northX][northY-1]==OFF)){
					int[] aux=new int[2];

					aux[0]=northX;
					aux[1]=northY-1;
					destinations.add(aux);
				}
				if((grid[eastX][eastY]==ON) && (grid[eastX-1][eastY]==OFF)){
					int[] aux=new int[2];

					aux[0]=eastX-1;
					aux[1]=eastY;
					destinations.add(aux);
				}
			}
			else if((grid[northX][northY]==ON) && (grid[northX][northY-1]==OFF)){
				int[] aux=new int[2];

				aux[0]=northX;
				aux[1]=northY-1;
				destinations.add(aux);
			}
			if((grid[westX][westY]==ON) && (grid[westX+1][westY]==OFF)){
				int[] aux=new int[2];

				aux[0]=westX+1;
				aux[1]=westY;
				destinations.add(aux);
			}
			if((grid[eastX][eastY]==ON) && (grid[eastX-1][eastY]==OFF)){
				int[] aux=new int[2];

				aux[0]=eastX-1;
				aux[1]=eastY;
				destinations.add(aux);
			}
		/* column 1 */	
		}else if (x==1 && y<5 && y>1) {
			if((grid[northX][northY]==ON) && (grid[northX][northY-1]==OFF)){
				int[] aux=new int[2];

				aux[0]=northX;
				aux[1]=northY-1;
				destinations.add(aux);
			}
			if((grid[southX][southY]==ON) && (grid[southX][southY+1]==OFF)){
				int[] aux=new int[2];

				aux[0]=southX;
				aux[1]=southY+1;
				destinations.add(aux);
			}
			if((grid[westX][westY]==ON) && (grid[westX+1][westY]==OFF)){
				int[] aux=new int[2];

				aux[0]=westX+1;
				aux[1]=westY;
				destinations.add(aux);
			}
		/* column 5*/	
		}else if (x==5 && y<5 && y>1) {
			if((grid[northX][northY]==ON) && (grid[northX][northY-1]==OFF)){
				int[] aux=new int[2];

				aux[0]=northX;
				aux[1]=northY-1;
				destinations.add(aux);
			}
			if((grid[southX][southY]==ON) && (grid[southX][southY+1]==OFF)){
				int[] aux=new int[2];

				aux[0]=southX;
				aux[1]=southY+1;
				destinations.add(aux);
			}
			if((grid[eastX][eastY]==ON) && (grid[eastX-1][eastY]==OFF)){
				int[] aux=new int[2];

				aux[0]=eastX-1;
				aux[1]=eastY;
				destinations.add(aux);
			}
			
		}
		
		else if (x==0) {
			if((grid[westX][westY]==ON) && (grid[westX+1][westY]==OFF)){
				int[] aux=new int[2];

				aux[0]=westX+1;
				aux[1]=westY;
				destinations.add(aux);
			}
			if((grid[northX][northY]==ON) && (grid[northX][northY-1]==OFF)){
				int[] aux=new int[2];

				aux[0]=northX;
				aux[1]=northY-1;
				destinations.add(aux);
			}
			if((grid[southX][southY]==ON) && (grid[southX][southY+1]==OFF)){
				int[] aux=new int[2];

				aux[0]=southX;
				aux[1]=southY+1;
				destinations.add(aux);
			}
		}else if (x==6) {
			if((grid[eastX][eastY]==ON) && (grid[eastX-1][eastY]==OFF)){
				int[] aux=new int[2];

				aux[0]=eastX-1;
				aux[1]=eastY;
				destinations.add(aux);
			}
			if((grid[northX][northY]==ON) && (grid[northX][northY-1]==OFF)){
				int[] aux=new int[2];

				aux[0]=northX;
				aux[1]=northY-1;
				destinations.add(aux);
			}
			if((grid[southX][southY]==ON) && (grid[southX][southY+1]==OFF)){
				int[] aux=new int[2];

				aux[0]=southX;
				aux[1]=southY+1;
				destinations.add(aux);
			}
		}
		else if (y==0) {
			if((grid[westX][westY]==ON) && (grid[westX+1][westY]==OFF)){
				int[] aux=new int[2];

				aux[0]=westX+1;
				aux[1]=westY;
				destinations.add(aux);
			}
			if((grid[eastX][eastY]==ON) && (grid[eastX-1][eastY]==OFF)){
				int[] aux=new int[2];

				aux[0]=eastX-1;
				aux[1]=eastY;
				destinations.add(aux);
			}
			if((grid[southX][southY]==ON) && (grid[southX][southY+1]==OFF)){
				int[] aux=new int[2];

				aux[0]=southX;
				aux[1]=southY+1;
				destinations.add(aux);
			}
		}else if (y==6) {
			if((grid[eastX][eastY]==ON) && (grid[eastX-1][eastY]==OFF)){
				int[] aux=new int[2];

				aux[0]=eastX-1;
				aux[1]=eastY;
				destinations.add(aux);
			}
			if((grid[northX][northY]==ON) && (grid[northX][northY-1]==OFF)){
				int[] aux=new int[2];

				aux[0]=northX;
				aux[1]=northY-1;
				destinations.add(aux);
			}
			if((grid[eastX][eastY]==ON) && (grid[eastX-1][eastY]==OFF)){
				int[] aux=new int[2];

				aux[0]=eastX-1;
				aux[1]=eastY;
				destinations.add(aux);
			}
		}
		
		
		return destinations;
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
		
		// mark the old position as OFF
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
		
		//substract pegCount
		pegCount--;
		
		//check if the number of pegs is 1
		checkGameState();
		
		saveGridOnArray();
	  //  db.addGame(arrayToString(getCurrentBoardStateArray()));
		
		/* update number of moves */
		moveCount++;
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
	public ArrayList<Position> PossibleDestinations(int x, int y) {
		
		return null;
		
	}
	//TODO
	public boolean validMove(int x0, int y0, int x, int y) {
		/*
		 *  0 * *   
		 *  * * *
		 *  * * 0
		 * 
		 * */
		if (grid[x][y]==ON) {
			return false;
		}
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
			
		}//TODO diagonal
		else
			return false;
	} // end validMove
	


	
	public  void updateGridFromCurrentBoardStateArray() {
	    for (int i = 0; i < currentBoardStateArray.length; i++) 
	    	//this.game.getGame()[i/7][i%7] = array[i];
	    	this.setGameValue(i/7, i%7, currentBoardStateArray[i]);
	}
	
	/**
	 * update the currentBoardStateArray
	 */
	public  void saveGridOnArray() {
	    int k=0;
		for (int i = 0; i < 7; i++)
	    	for (int j = 0; j < 7; j++) {
	    		currentBoardStateArray[k]=grid[i][j];
				k++;
			}
	}
	/* from Int[] to String */
	public String arrayToString(int[] array) {
		String res = Arrays.toString(array);
		return res;
	}
	
}
