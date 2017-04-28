public class State{
	private static final int GRID_SIZE = 3;
	private int score;
	private int currentPlayer;
	private String[][] config = new String[3][3];

	public State(String[][] array, int player){

		for (int i=0; i<GRID_SIZE;i++){
			for (int j=0; j<GRID_SIZE; j++){
				this.config[i][j] = array[i][j];
			}
		}
		set_currentPlayer(player);
	}

	public String get_Config(int x, int y){
		return this.config[x][y];
	}

	public void set_Config(int x, int y, String player){
		this.config[x][y] = player;
	}

	public int get_Value(){
		return value_of_Board();
	}

	public void set_currentPlayer(int i){
		this.currentPlayer = i;
	}	

	public int get_currentPlayer(){
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

	private int value_of_Board(){
		if (gameWin() && (this.currentPlayer == 1) ){
			this.score = 10;
			return 10;	
		}
		else if (gameWin() && (this.currentPlayer == 2) ){
			this.score = -10;
			return -10;	
		}
		else return -1; //Value returned if game is a draw			
	}

	
} 