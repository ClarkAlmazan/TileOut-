
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
	private JOptionPane playerturn;
	
	//essential for tracking game state
	private Tile[][] tileArray;
	// private String[][] tileSettings = {{"X","O","O"},{"X","O","X"},{"","",""}};
	private String[][] tileSettings;
	private State currentState;

	private int count = 0; //REMOVE

	//player variables
	private int human = 1; //X
	private int cpu = 0; //O
	private int currentPlayer;
	//Game constructor
	public TicTacToe(){

		this.gameGrid = new JPanel(new GridLayout(GRID_SIZE,GRID_SIZE,2,2));
		this.gameWindow = new JFrame("Tile Out!");
		this.gameContainer = gameWindow.getContentPane();
		this.gameContainer = gameGrid;
		this.tileSettings = new String[GRID_SIZE][GRID_SIZE];

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
		
		//start with cpu turn
		Object[] order = {"Player first","CPU first"};

		int choice = playerturn.showOptionDialog(null,"Pick the order of Players","", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,order,order[0]);

		if(choice == 0){

			JOptionPane playerstart = new JOptionPane();
			playerstart.showMessageDialog(null, "Player will move first.", "PLAYER START",JOptionPane.PLAIN_MESSAGE);

			
		}	
		else if(choice == 1){

			JOptionPane playerstart = new JOptionPane();
			playerstart.showMessageDialog(null, "CPU will move first.", "CPU START",JOptionPane.PLAIN_MESSAGE);
			this.currentPlayer = cpu;
			cpuTurn();
		}
		else{
			JOptionPane playerstart = new JOptionPane();
			playerstart.showMessageDialog(null, "Player did not choose.\n"+"CPU will go first.", "",JOptionPane.PLAIN_MESSAGE);
			this.currentPlayer = cpu;
			cpuTurn();		
		}


	}

	//function that initializes Tile array
	//gives every tile tile their respective status, and coordinates
	private void initializeTiles(){
		this.tileArray = new Tile[GRID_SIZE][GRID_SIZE];
		for (int i=0; i<GRID_SIZE;i++){
			for (int j=0; j<GRID_SIZE; j++){
				this.tileSettings[i][j] = "";
				// if (!tileSettings[i][j].equals("")) 
				// 	this.tileArray[i][j] = new Tile(i, j, tileSettings[i][j]);
				// else 
					this.tileArray[i][j] = new Tile(i, j);
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
		tileSettings[xCoor][yCoor] = "X";
		currentState.setConfig(xCoor, yCoor, "X");	
		
		
		//prints the coordinates of the clicked tile as well as the game state
		// printGameState();
		
		

		//after every move, check if the game has reached the end state
		// System.out.println("Horizontal:"+checkGameEndHor());
		// System.out.println("Vertical:"+checkGameEndVer());
		// System.out.println("Diagonal:"+checkGameEndDia());
		
		if (checkGameEndHor() || checkGameEndVer() || checkGameEndDia()){
			JOptionPane endgame = new JOptionPane();
			// System.out.println(utility());
			if(this.currentPlayer == 1){
				endgame.showMessageDialog(null, "Congratulations, you Win!", "GAME OVER",JOptionPane.ERROR_MESSAGE);
			}
			else{
				endgame.showMessageDialog(null, "You Lose. CPU wins!", "GAME OVER",JOptionPane.ERROR_MESSAGE);
			}
			System.exit(0);
		}

		if (noBlanks() && (checkGameEndHor() || checkGameEndDia() || checkGameEndVer())==false){
			JOptionPane endgame = new JOptionPane();
			// System.out.println(utility());
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
			System.out.println(utility());
			if(this.currentPlayer == 1){
				endgame.showMessageDialog(null, "You Win!", "GAME OVER",JOptionPane.ERROR_MESSAGE);
			}
			else{
				endgame.showMessageDialog(null, "You Lose. CPU wins!", "GAME OVER",JOptionPane.ERROR_MESSAGE);
			}
			System.exit(0);
		}

		if (noBlanks() && (checkGameEndHor() || checkGameEndDia() || checkGameEndVer())==false){
			JOptionPane endgame = new JOptionPane();
			System.out.println(utility());
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
		this.count = 0; //REMOVE
		State gameState = new State(tileSettings,human);
		ArrayList<State> successorStates = successors(gameState);
		int count = 0;
		int v = 0;
		int m = -999;
		
		for(State s: successorStates){
			// System.out.println(s.getCurrentPlayer());
			// s.printBoard();
			v = value(s,-999,999);

			// if(count==0){
			// 	gameState = s;
			// 	currentValue = m;
			// }
			if (v>m){
				gameState = s;
				m = v;
			}

			// count++;

		}
		
		toggleTileCPU(gameState.getAction());

		System.out.println("Number of states: " + this.count);//REMOVE

		swapPlayers();
		System.out.println("cpu turn done");

	}

	private int utility(){
		if (gameWin() && (this.currentPlayer == cpu) ){		//Maximize Computer
			return 10;	
		}
		else if (gameWin() && (this.currentPlayer == human) ){
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
		String[][] config = board_state.getConfigState();
		Point nextToggle = new Point(0,0);
		for (int y=0; y<GRID_SIZE; y++){
			for(int x=0; x<GRID_SIZE; x++){			
				//if tile is blank, it is an available action
				if (config[x][y].isEmpty()){
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
		State result = new State(in.getConfigState(), player, a);
		if (player==cpu)	
			result.setConfig(a.x, a.y, "O");
		else
			result.setConfig(a.x, a.y, "X");

		
		// result.printBoard();
		// System.out.println("result set");
		return result;
	}

	//function for getting the successor states
	public ArrayList<State> successors(State in){
		ArrayList<State> nextStates = new ArrayList<State>();
		int player = human;
		ArrayList<Point> availableActions = Actions(in);
		//player of successor state should be human if preceding player was cpu, vice versa
		if(in.getCurrentPlayer() == human) player = cpu;
		else if(in.getCurrentPlayer() == cpu) player = human;

		// System.out.println(availableActions.size());
		for (Point a: availableActions){
				nextStates.add(Result(in, a, player));

		}
		// System.out.println("Number of successors: "+ nextStates.size());
		return nextStates;
	}

	private int value(State board_state, int alpha, int beta ){

		//if board in terminal state
		if(board_state.terminalCheck())
			return board_state.utility();
		

		//if max node
		else if(board_state.getCurrentPlayer() == human)
			return max_value(board_state,alpha,beta);
		

		//if min node
		// else if(board_state.getCurrentPlayer() == human)
			return min_value(board_state,alpha,beta);


	}

	public int max_value(State s, int alpha, int beta){
		int m = -999;
		
		ArrayList<State> successorStates = successors(s);
		for(State a: successorStates){	
			int v = value(a, alpha, beta);
			m = maxValue(m, v);
			alpha = maxValue(alpha, v);
			if(beta <= alpha){
				break;
			}
			this.count++; //REMOVE
		}

		return m;

	}

	public int min_value(State s , int alpha, int beta){
		int m = 999;

		ArrayList<State> successorStates = successors(s);
		for(State a: successorStates){
			int v = value(a, alpha, beta);
			m = minValue(m, v);
			beta = minValue(beta, v);
			if(beta <= alpha){
				break;
			}
			this.count++; //REMVOE
		}

		return m;

	}
}
//Here be dragons