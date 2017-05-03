
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Component;

import java.awt.Font;


public class Tile extends JButton{ 
		//coordinates for location
		private int x_axis;
		private int y_axis;

		//accepts parameters for on/off status, and coordinates
		public Tile(int x, int y){
			super(); //calls main constructor of a JButton
			
			this.setText("");

			this.x_axis = x;
			this.y_axis = y;
			this.setFont(new Font("Arial", Font.BOLD, 30));
		}

		public Tile(int x, int y, String text){
			super(); //calls main constructor of a JButton
			
			this.setText(text);

			this.x_axis = x;
			this.y_axis = y;
			this.setFont(new Font("Arial", Font.BOLD, 30));
			this.setEnabled(false);
		}

		public String getTile(){
			return this.getText();
		}

		//getter function for coordinates
		public int getXcoordinate(){
			return this.x_axis;
		}

		public int getYcoordinate(){
			return this.y_axis;
		}
		
		//built-in light toggle function that can be accessed outside
		public void toggleTile(int player){
			if (player == 1){
				this.setText("x");
			}
			else{
				this.setText("O");
			}
			this.setEnabled(false);
		}
}

