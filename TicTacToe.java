
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Point;
import java.lang.Thread;


public class TicTacToe{
	//constants
	private static final int GRID_SIZE = 3;

	//essential items for game rendering
	private JFrame gameWindow;
	private Container gameContainer;
	private JPanel gameGrid;
	
	
	//essential for tracking game state
	private Tile[][] tileArray;
	private String[][] tileSettings = {{"X","O","O"},{"O","X","X"},{"","",""}};
	private State currentState;

	//player variables
	private int human = 1; //X
	private int cpu = 0; //O
	private int currentPlayer;
	//Game constructor
	public TicTacToe(){

		this.gameGrid = new JPanel(new GridLayout(GRID_SIZE,GRID_SIZE));
		this.gameWindow = new JFrame("Tile Out!");
		this.gameContainer = gameWindow.getContentPane();
		this.gameContainer = gameGrid;
		// this.tileSettings = new String[GRID_SIZE][GRID_SIZE];

		this.currentPlayer = human;

		this.currentState = new State(tileSettings, currentPlayer);
		

		//after reading the file, initializes the tile tiles
		initializeTiles();

		//add an ActionListener on every tile tile then add them to the JPanel
		for (Tile[] x: tileArray){
			for (Tile y: x){

				y.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						toggleTiles(y);
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
		// printGameState();
	}

	//function that initializes Tile array
	//gives every tile tile their respective status, and coordinates
	private void initializeTiles(){
		this.tileArray = new Tile[GRID_SIZE][GRID_SIZE];
		for (int i=0; i<GRID_SIZE;i++){
			for (int j=0; j<GRID_SIZE; j++){
				// this.tileSettings[i][j] = "";
				if (!tileSettings[i][j].equals("")) this.tileArray[i][j] = new Tile(i, j, tileSettings[i][j]);
				else this.tileArray[i][j] = new Tile(i, j);
			}
		}

	}
	

	
	//function handling tile toggling
	private void toggleTiles(Tile x){
		//get current coordinates of clicked tile
		int xCoor = x.getXcoordinate();
		int yCoor = x.getYcoordinate();
		
		//toggle clicked tile and adjust current state table
		tileArray[xCoor][yCoor].toggleTile(currentPlayer);
		if(this.currentPlayer == human)	{
			tileSettings[xCoor][yCoor] = "X";
			currentState.setConfig(xCoor, yCoor, "X");	
		}	
		// else {
		// 	tileSettings[xCoor][yCoor] = "O";
		// 	currentState.setConfig(xCoor, yCoor, "O");	
		// }
		
		
		//prints the coordinates of the clicked tile as well as the game state
		printGameState();
		
		

		//after every move, check if the game has reached the end state
		// System.out.println("Horizontal:"+checkGameEndHor());
		// System.out.println("Vertical:"+checkGameEndVer());
		// System.out.println("Diagonal:"+checkGameEndDia());
		
		if (checkGameEndHor() || checkGameEndVer() || checkGameEndDia()){
			JOptionPane endgame = new JOptionPane();
			System.out.println(valueOfBoard());
			endgame.showMessageDialog(null, "Yay", "Yay",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		if (noBlanks() && (checkGameEndHor() || checkGameEndDia() || checkGameEndVer())==false){
			JOptionPane endgame = new JOptionPane();
			System.out.println(valueOfBoard());
			endgame.showMessageDialog(null, "Draw", "Aww",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}


		swapPlayers();
		// System.out.println("currentPlayer:"+this.currentPlayer);
		if(this.currentPlayer == cpu){	//after swapping
			cpuTurn();
		}
	}

	private void toggleTileCPU(Point a){
		int xCoor = a.x;
		int yCoor = a.y;

		tileArray[xCoor][yCoor].toggleTile(cpu);
		tileSettings[xCoor][yCoor] = "O";
		currentState.setConfig(xCoor, yCoor, "O");

		if (checkGameEndHor() || checkGameEndVer() || checkGameEndDia()){
			JOptionPane endgame = new JOptionPane();
			System.out.println(valueOfBoard());
			endgame.showMessageDialog(null, "Yay", "Yay",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		if (noBlanks() && (checkGameEndHor() || checkGameEndDia() || checkGameEndVer())==false){
			JOptionPane endgame = new JOptionPane();
			System.out.println(valueOfBoard());
			endgame.showMessageDialog(null, "Draw", "Aww",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}


	//check for a horizontal winning pattern
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

	//check for a vertical winning pattern
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

	//check for a diagonal winning pattern
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

	//check if board is full
	private boolean noBlanks(){
		for(int i = 0 ; i<GRID_SIZE; i++){
			for (int j=0; j<GRID_SIZE; j++){
				if(tileSettings[i][j].isEmpty()) return false;
			}
		}
		return true;
	}

	//function for printing the game state
	private void printGameState(){
		for(int i=0; i<GRID_SIZE; i++){
			for(int j=0; j<GRID_SIZE; j++){
				if (!tileSettings[i][j].isEmpty()) System.out.print(tileSettings[i][j]);
				else System.out.print("-");
				
			}
			System.out.println();
		}
	}

	//function for swapping players in between turns
	private void swapPlayers(){
		if (this.currentPlayer == human) this.currentPlayer = cpu;
		else this.currentPlayer = human;
	}


	//function for checking if a player has already won
	private boolean gameWin(){
		if (checkGameEndHor() || checkGameEndVer() || checkGameEndDia()){
			return true;
		}
		else return false;
	}

	//function for checking if board is already full
	private boolean terminalCheck(){
		if (gameWin() || noBlanks()){
			return true;
		}

		return false;
	}

	//function for executing cpu turn
	private void cpuTurn(){
		State gameState = new State(tileSettings,cpu);
		int count = 0;
		int currentValue =0;
		int m = 0;
		for(State s: successors(gameState)){

			m = value(s);

			if(count==0){
				gameState = s;
				currentValue = m;
			}
			else if (m>currentValue){
				gameState = s;
				currentValue = m;
			}

			count++;
		}
		
		toggleTileCPU(gameState.getAction());

		swapPlayers();
		System.out.println("cpu turn done");

	}


	private void copyConfig(String[][] config){
		for (int i = 0; i<GRID_SIZE; i++){
			for (int j = 0; j<GRID_SIZE; j++){
				tileSettings[i][j] = config[i][j];
				if (!tileSettings[i][j].equals("")) this.tileArray[i][j] = new Tile(i, j, tileSettings[i][j]);
				else this.tileArray[i][j] = new Tile(i, j);
			}
		}

	}
	private int valueOfBoard(){
		if (gameWin() && (this.currentPlayer == 0) ){		//Maximize Computer
			return 10;	
		}
		else if (gameWin() && (this.currentPlayer == 1) ){
			return -10;	
		}
		else return 0; //Value returned if game is a draw			
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


	//function that gets the available actions for a player
	public ArrayList<Point> Actions(State board_state){
		ArrayList<Point> availableActions = new ArrayList<Point>();
		Point nextToggle = new Point(0,0);
		for (int y=0; y<GRID_SIZE; y++){
			for(int x=0; x<GRID_SIZE; x++){			
				//if tile is blank, it is an available action
				if (tileSettings[x][y].isEmpty()){
					nextToggle = new Point(x,y);
					// System.out.println("Action("+x+", "+y+")");
					availableActions.add(nextToggle);
				}
				
			}
		}
		return availableActions;

	}

	//function that gets the resulting state of an action being executed
	public State Result(State in, Point a, int player){
		State result = new State(in.getConfigState(), player);
		if (player==cpu)	
			result.setConfig(a.x, a.y, "O");
		else
			result.setConfig(a.x, a.y, "X");

		result.setAction(a);
		result.printBoard();
		// System.out.println("result set");
		return result;
	}

	//function for getting the successor states
	public ArrayList<State> successors(State in){
		ArrayList<State> nextStates = new ArrayList<State>();
		int player;
		if(in.getCurrentPlayer() == human) player = cpu;
		else player = human;

		for (Point a:Actions(in)){
				nextStates.add(Result(in, a, player));

		}
		// System.out.println("Number of successors: "+ nextStates.size());
		return nextStates;
	}

	private int value(State board_state){

		//if board in terminal state
		if(board_state.terminalCheck() == true)
			return board_state.valueOfBoard();
		

		//if max node
		if(board_state.getCurrentPlayer() == cpu)
			return max_value(board_state);
		

		//if min node
		if(board_state.getCurrentPlayer() == human)
			return min_value(board_state);

		return 0;

	}

	public int max_value(State s){
		int m = -999;
		ArrayList<State> successorStates = successors(s);
		for(State a: successorStates){
		// for(int i=0; i<3; i++){
			// State a = successorStates.get(i);	
			int v = value(a);
			m = maxValue(m, v);
		}

		return m;

	}

	public int min_value(State s){
		int m = 999;
		ArrayList<State> successorStates = successors(s);
		for(State a: successorStates){
		// for(int i=0; i<3; i++){
			// State a = successorStates.get(i);	
			int v = value(a);
			m = minValue(m, v);
		}

		return m;

	}
}
//Here be dragons