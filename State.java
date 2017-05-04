import java.util.ArrayList;
import java.awt.Point;

public class State{
	private static final int GRID_SIZE = 3;
	private int score;
	private int currentPlayer;
	private String[][] config = new String[3][3];
	private Point action;

	public State(String[][] array, int player){

		for (int i=0; i<GRID_SIZE;i++){
			for (int j=0; j<GRID_SIZE; j++){
				this.config[i][j] = array[i][j];
			}
		}
		this.currentPlayer = player;
		// System.out.println("Player "+ this.currentPlayer);
	}

	public State(String[][] array, int player, Point action){

		for (int i=0; i<GRID_SIZE;i++){
			for (int j=0; j<GRID_SIZE; j++){
				this.config[i][j] = array[i][j];
			}
		}
		setCurrentPlayer(player);
		this.action = action;
	}	

	public String getConfig(int x, int y){
		return this.config[x][y];
	}

	public void setConfig(int x, int y, String value){
		this.config[x][y] = value;
	}

	public String[][] getConfigState(){
		return this.config;
	}

	public void setAction(Point a){
		this.action = a;
	}

	public Point getAction(){
		return this.action;
	}

	public void setCurrentPlayer(int i){
		this.currentPlayer = i;
	}	

	public int getCurrentPlayer(){
		return this.currentPlayer;
	}

	private boolean checkGameEndHor(){
		boolean end=true;
		for(int i=0; i< GRID_SIZE; i++){
			end = true;
			for (int j=1; j<GRID_SIZE; j++){
				if (config[i][j].compareTo(config[i][j-1])!=0 || (config[i][j].isEmpty() || config[i][j-1].isEmpty())){ //if not equal or still empty
					end=false;
					break;
				}
			}
			if (end==true){
				break;
			}
		}

		
		return end;
	}

	private boolean checkGameEndVer(){
		boolean end = true;
		for(int j=0; j<GRID_SIZE; j++){
			end = true;
			for (int i=1; i< GRID_SIZE; i++){
				if (config[i][j].compareTo(config[i-1][j])!=0 || (config[i][j].isEmpty() || config[i-1][j].isEmpty())){
					end = false;
					break;
				}
			}
			if(end == true) break;
		}

		
		return end;
	}

	private boolean checkGameEndDia(){
		boolean end=true;
		int j = 1;
		for(int i=1; i< GRID_SIZE; i++){
			end = true;
			if (config[i][j].compareTo(config[i-1][j-1])!=0 || (config[i][j].isEmpty() || config[i-1][j-1].isEmpty())){ //if not equal or still empty
				end=false;
				break;
			}
			j++;
		}
		if (end) return end;


		j=1;
		for(int i=1; i< GRID_SIZE; i++){
			end = true;
			if (config[i][j].compareTo(config[i-1][j+1])!=0 || (config[i][j].isEmpty() || config[i-1][j+1].isEmpty())){ //if not equal or still empty
				end=false;
				break;
			}
			j--;
		}
		
		return end;
	}

	private boolean gameWin(){
		if (checkGameEndHor() || checkGameEndVer() || checkGameEndDia()){
			return true;
		}
		else return false;
	}

	//function for checking if board is already full
	public boolean terminalCheck(){
		if (gameWin() || noBlanks())	return true;
		return false;
	}

	//check if board is full
	private boolean noBlanks(){
		for(int i = 0 ; i<GRID_SIZE; i++){
			for (int j=0; j<GRID_SIZE; j++){
				if(config[i][j].length()==0) {
					// System.out.println("not yet full");
					return false;	
				}
			}
		}
		return true;
	}	


	public int utility(){
		if (gameWin() && (this.currentPlayer == 0) ){
			return 10;	
		}
		else if (gameWin() && (this.currentPlayer == 1) ){
			// this.score = -10;
			return -10;	
		}
		else return 0; //Value returned if game is a draw			
	}

	public void printBoard(){
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if (!config[i][j].isEmpty()) 
					System.out.print(config[i][j]+ " ");
				else if(config[i][j].length()==0)
					System.out.print("- ");
			}
			System.out.println("");
		}

		System.out.println("");
	}
	
} 