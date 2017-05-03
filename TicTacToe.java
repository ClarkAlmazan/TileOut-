
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Point;


public class TicTacToe{
	//constants
	private static final int GRID_SIZE = 3;

	//essential items for game rendering
	private JFrame gameWindow;
	private Container gameContainer;
	private JPanel gameGrid;
	
	//essential for reading the state file
	private BufferedReader fileReader;
	
	//essential for tracking game state
	private Tile[][] tileArray;
	private String[][] tileSettings;

	//player variables
	private int player1; //X
	private int player2; //O
	private int currentPlayer;
	//Game constructor
	public TicTacToe(){

		this.gameGrid = new JPanel(new GridLayout(GRID_SIZE,GRID_SIZE));
		this.gameWindow = new JFrame("Tile Out!");
		this.gameContainer = gameWindow.getContentPane();
		this.gameContainer = gameGrid;
		this.tileSettings = new String[GRID_SIZE][GRID_SIZE];

		this.player1 = 1;
		this.player2 = 0;	//CPU by default
		this.currentPlayer = 1;
		

		//after reading the file, initializes the Light tiles
		initializeLights();

		//add an ActionListener on every Light tile then add them to the JPanel
		for (Tile[] x: tileArray){
			for (Tile y: x){

				y.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						toggleTiles(currentPlayer,y);
					}
				});

				gameGrid.add(y);
			}
		}

		//add JPanel to JFrame and set some parameters including window size, close operation, and visibility
		gameWindow.add(gameGrid);
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.setLocationRelativeTo(null);
		gameWindow.setMinimumSize(new Dimension(300,300));
		gameWindow.pack();
		gameWindow.setVisible(true);
		
		//print initial game state
		printGameState();
	}

	//function that initializes Tile array
	//gives every Light tile their respective status, and coordinates
	private void initializeLights(){
		this.tileArray = new Tile[GRID_SIZE][GRID_SIZE];
		for (int i=0; i<GRID_SIZE;i++){
			for (int j=0; j<GRID_SIZE; j++){
				this.tileSettings[i][j] = "";
				this.tileArray[i][j] = new Tile(i, j);
			}
		}

	}
	

	
	//function handling light toggling
	private void toggleTiles(int player, Tile x){
		//get current coordinates of clicked light
		int xCoor = x.getXcoordinate();
		int yCoor = x.getYcoordinate();
		
		//toggle clicked light and adjust current state table
		tileArray[xCoor][yCoor].toggleTile(player);
		if(this.currentPlayer == player1)	tileSettings[xCoor][yCoor] = "X";	
		else tileSettings[xCoor][yCoor] = "O";
		
		
		//prints the coordinates of the clicked light as well as the game state
		printGameState();
		
		

		//after every move, check if the game has reached the end state
		System.out.println("Horizontal:"+checkGameEndHor());
		System.out.println("Vertical:"+checkGameEndVer());
		System.out.println("Diagonal:"+checkGameEndDia());
		System.out.println("currentPlayer:"+this.currentPlayer);
		if (checkGameEndHor() || checkGameEndVer() || checkGameEndDia()){
			JOptionPane endgame = new JOptionPane();
			System.out.println(value_of_Board());
			endgame.showMessageDialog(null, "Yay", "Yay",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		if (noBlanks() && (checkGameEndHor() || checkGameEndDia() || checkGameEndVer())==false){
			JOptionPane endgame = new JOptionPane();
			System.out.println(value_of_Board());
			endgame.showMessageDialog(null, "Draw", "Aww",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}


		swapPlayers();

		if(this.currentPlayer == 0){	//after swapping
			cpu_turn();
		}
	}

	//function for checking if the game has reached the end state
	//if all Tile pattern match, game end
	private boolean checkGameEndHor(){
		boolean end=true;
		for(int i=0; i< GRID_SIZE; i++){
			end = true;
			for (int j=1; j<GRID_SIZE; j++){
				if (tileSettings[i][j].compareTo(tileSettings[i][j-1])!=0 || (tileSettings[i][j].isEmpty() || tileSettings[i][j-1].isEmpty())){ //if not equal or still empty
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
				if (tileSettings[i][j].compareTo(tileSettings[i-1][j])!=0 || (tileSettings[i][j].isEmpty() || tileSettings[i-1][j].isEmpty())){
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
			if (tileSettings[i][j].compareTo(tileSettings[i-1][j-1])!=0 || (tileSettings[i][j].isEmpty() || tileSettings[i-1][j-1].isEmpty())){ //if not equal or still empty
				end=false;
				break;
			}
			j++;
		}
		if (end) return end;


		j=1;
		for(int i=1; i< GRID_SIZE; i++){
			end = true;
			if (tileSettings[i][j].compareTo(tileSettings[i-1][j+1])!=0 || (tileSettings[i][j].isEmpty() || tileSettings[i-1][j+1].isEmpty())){ //if not equal or still empty
				end=false;
				break;
			}
			j--;
		}
		
		return end;
	}

	private boolean noBlanks(){
		for(int i = 0 ; i<GRID_SIZE; i++){
			for (int j=0; j<GRID_SIZE; j++){
				if(tileSettings[i][j].isEmpty()) return false;
			}
		}
		return true;
	}


	
	//function for printing the game state in 1's and 0's
	private void printGameState(){
		for(int i=0; i<GRID_SIZE; i++){
			for(int j=0; j<GRID_SIZE; j++){
				if (!tileSettings[i][j].isEmpty()) System.out.print(tileSettings[i][j]);
				else System.out.print("-");
				
			}
			System.out.println();
		}
		
		
	}

	private void swapPlayers(){
		if (this.currentPlayer == 1) this.currentPlayer = 0;
		else this.currentPlayer = 1;
	}


	private boolean gameWin(){
		if (checkGameEndHor() || checkGameEndVer() || checkGameEndDia()){
			return true;
		}
		else return false;
	}

	private boolean terminal_check(){
		if (gameWin()==true){
			return true;
		}

		for(int i=0; i<GRID_SIZE; i++){
			for(int j=0; j<GRID_SIZE; j++){
				if(!tileSettings[i][j].isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	private void cpu_turn(){

		State gameState = new State(tileSettings,player2);

		int value = -999;
		int x = -999; 
		int y = -999;

		// for(int i=0; i<GRID_SIZE;i++){
		// 	for(int j=0; j<GRID_SIZE; j++){
		// 		if((gameState.get_Config(i,j)).isEmpty() ){
		// 			gameState.set_Config(i,j,"O"); //Adds player move to check
						
		// 			int m = minMaxAlgorithm(gameState, 1);

		// 			gameState.set_Config(i,j,""); //reset config for possible next itereations

		// 			if(m > value){	//if max value is found, the coordinates are saved for toggling
		// 				value = m;
		// 				x = i;
		// 				y = j;

		// 			}
		// 		}	
		// 	}
		// }
		int m = minMaxAlgorithm(gameState, 1);
		
		toggleTiles(player2,tileArray[x][y]);
		swapPlayers();

	}

	private int value_of_Board(){
		if (gameWin() && (this.currentPlayer == 0) ){		//Maximize Computer
			return 10;	
		}
		else if (gameWin() && (this.currentPlayer == 1) ){
			return -10;	
		}
		else return -1; //Value returned if game is a draw			
	}

	private int minMaxAlgorithm(State board_state, int playerTurn){
		//if board in terminal state
		if(terminal_check() == true){
			return board_state.get_Value();
		}

		int v;

		//if max node
		if(playerTurn == 0){
			return max_value(board_state);
		}	

		//if min node
		else if(playerTurn == 1){
			return min_value(board_state);
		}

		return 0;	
	}

	private int maxValue(int value1, int value2){
		if(value1 > value2){
			return value1;
		}

		else return value2;
	}

	private int minValue(int value1, int value2){
		if(value1 < value2){
			return value1;
		}

		else return value2;
	}	



	public ArrayList<Point> Actions(State board_state){
		ArrayList<Point> availableActions = new ArrayList<Point>();
		Point nextToggle = new Point(0,0);
		ArrayList<Point> toggledPoints = board_state.getToggledPoints();
		for (int y=0; y<GRID_SIZE; y++){
			for(int x=0; x<GRID_SIZE; x++){
				nextToggle = new Point(x,y);

				//if the current Point hasn't been toggled once, add to list of Actions
				if (tileSettings[x][y].isEmpty()){
					//System.out.println("Toggle light at "+ x + ", " + y);
					availableActions.add(nextToggle);
				}
				
			}
		}
		return availableActions;

	}

	public State Result(State in, Point a, int player){
		State result = new State(in.getConfigState(), player);
		if (player==0)	
			result.set_Config(a.x, a.y, "O");
		else
			result.set_Config(a.x, a.y, "X");
		return result;
	}

	public ArrayList<State> successors(State in, int player){
		ArrayList<State> nextStates = new ArrayList<State>();

		for (Point a:Actions(in)){
				nextStates.add(Result(in, a, player));

		}

		return nextStates;
	}

	public int max_value(State s){
		int m = -999;
		ArrayList<State> successors = successors(s, s.get_currentPlayer());
		for(State a: successors){
			int v = minMaxAlgorithm(a, a.get_currentPlayer());
			m = maxValue(m, v);
		}

		return m;

	}

	public int min_value(State s){
		int m = 999;
		ArrayList<State> successors = successors(s, s.get_currentPlayer());
		for(State a: successors){
			int v = minMaxAlgorithm(a, a.get_currentPlayer());
			m = minValue(m, v);
		}

		return m;

	}
}
//Here be dragons