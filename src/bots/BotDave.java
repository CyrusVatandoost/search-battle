/*package bots;

import java.util.ArrayList;
import game.Block;
import game.Character;
import game.Treasure;
import game.Wall;

public class BotDave extends Character {
	protected ArrayList<int[]> priority = new ArrayList<int[]>(); 
	protected int Goal_check = 0;
	protected int Treasure_check = 0;	
	protected ArrayList<int[]> OpenSet = new ArrayList<int[]>();
	protected ArrayList<int[]> ClosedSet = new ArrayList<int[]>();
	protected ArrayList<int[]> path = new ArrayList<int[]>();
	protected int thinking = 1;
	protected int starting = 0;
	protected int[] position = {this.x , this.y}; 
	protected Grid[][] grid = new Grid[blocks.length][blocks[0].length] ;
	
	public BotDave(Block[][] blocks, int x, int y) {
		super(blocks, x, y);
	}
	
	public void startup() {  // Function for specifying every node in the map
	
		for(int j = 0; j < blocks.length; j++) {
	        for(int i = 0; i < blocks[0].length; i++){	 
	        	  
	        	grid[j][i] = new Grid();
	        	grid[j][i].setX(j);
	        	grid[j][i].setY(i);
	        	
	        	//Assign each block to the side that is within the bounds of the map as neighbor
	        	if(j != 0   )
	        		grid[j][i].add_neighbor(j - 1, i);
	        	if(j != blocks.length - 1  )
	        		grid[j][i].add_neighbor(j + 1, i);
	        	if(i != 0  )
	        		grid[j][i].add_neighbor(j, i - 1);		
	        	if(i != blocks[0].length - 1 )
	        		grid[j][i].add_neighbor(j, i + 1);
	        	
	        	if(blocks[j][i] instanceof Wall)
	        		grid[j][i].SetWall(true);
	        }
		}
	}
	
	private void TresureCheck() {//checks changes in treasure counts
		
		if(Treasure_check == 0) { // first check at the start
			for(int j = 0; j < blocks.length; j++) {
				for(int i = 0; i < blocks[0].length; i++)
					if(blocks[j][i] instanceof Treasure){	
						int[] ColRowCost = { j , i , Math.abs(j - this. x) + Math.abs( i - position[1]) };
		        	 	priority.add(ColRowCost);
					}
			}
			Treasure_check = 1;
		}
		else if (Treasure_check == 1){ // proceeding checks stores values temporarily 
			ArrayList<int[]> temp_prio = new ArrayList<int[]>();
			
			for(int j = 0; j < blocks.length; j++) {
				for(int i = 0; i < blocks[0].length; i++)
					if(blocks[j][i] instanceof Treasure){	 
						int[] ColRowCost = { j , i , Math.abs(j - this. x) + Math.abs( i - position[1]) };
				        temp_prio.add(ColRowCost);
			          }
			}
			if (temp_prio.size() < priority.size()){ // if treasure decreased, Update treasure list
				int a = temp_prio.size();
				//int b = priority.size();
				int c = 0;
				
				while( c != priority.size())
					priority.remove(0);
				while(a != c){
					priority.add(temp_prio.get(c));
					c++;
				}
				thinking = 1;
			}
			else if(temp_prio.size() == priority.size()){ // treasure is still the same, Recompute cost
				for(int x = 0; x < priority.size(); x++){
					int col = priority.get(x)[0];
					int row = priority.get(x)[1];
					int cost = Math.abs( col - position[0]) + Math.abs(row - position[1]);
					int[] ColRowCost = {col , row , cost};
					priority.set(x, ColRowCost);
				}
			}
		}
	}
	
	private void Goal_Priority() { // sets the priority for each treasure
		int k = priority.size();
		int tempCost = 0;
		int tempY = 0;
		int tempX = 0;
		while( k != 0) {
			for( int i = 0; i < k; i++){ 
				if ( i != k - 1)
					if (priority.get(i)[2] > priority.get(i + 1)[2] ) {// if Treasure is farther move down the list
						tempCost = priority.get(i + 1)[2];
						priority.get(i + 1)[2] = priority.get(i)[2];
						priority.get(i)[2] = tempCost;
						
						tempY = priority.get(i + 1)[1];
						priority.get(i + 1)[1] = priority.get(i)[1];
						priority.get(i)[1] = tempY;
						
						tempX = priority.get(i + 1)[0];
						priority.get(i + 1)[0] = priority.get(i)[0];
						priority.get(i)[0] = tempX;
					}
			}
			k--;
		}
		
		if(path.isEmpty() == false && priority.isEmpty() == false) // if priority changes , change pathing
			if(priority.get(0) != path.get(path.size() - 1))
				thinking = 1;
		while(path.isEmpty() == false)
			path.remove(0);
	}
	
	

private void PathFinding() { // finds the optimal path to the treasure with top priority
	OpenSet.add(position);	
	if(priority.isEmpty() == false)	
		while(OpenSet.isEmpty() == false){
			int lowestIndex = 0;
			for (int i = 0; i < OpenSet.size(); i++){ // gets neighbor node with lowest cost
				if( grid[OpenSet.get(i)[0]][OpenSet.get(i)[1]].getF() 
						< grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].getF()){
					lowestIndex = i;
				}
			}
			
			if(OpenSet.get(lowestIndex)[0] == priority.get(0)[0] 
					&& OpenSet.get(lowestIndex)[1] == priority.get(0)[1]) { // if already at treasure
				starting = 0; 
				int g = OpenSet.get(lowestIndex)[0];
				int h = OpenSet.get(lowestIndex)[1];
				while(grid[g][h].getParent() == 1) {
					int[] temp = {g ,h };
					path.add(0,temp);
					g = grid[temp[0]][temp[1]].parent[0];
					h = grid[temp[0]][temp[1]].parent[1];
				}
				while( OpenSet.isEmpty() == false )
					OpenSet.remove(0);
				while( ClosedSet.isEmpty() == false )
					ClosedSet.remove(0);
			}
			else { // not in treasure yet , add neighbors, that are not walls, to the set of options
				ClosedSet.add(OpenSet.get(lowestIndex));
				for ( int x = 0 ; 
						x < grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].Neigbors.size(); 
						x++){   
					int neighbor = 0;
					int[] temp = {grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].Neigbors.get(x)[0], 
							grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].Neigbors.get(x)[1]};
					grid[temp[0]][temp[1]].setG(grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].getG() + 1);
					grid[temp[0]][temp[1]].setH(Math.abs(grid[temp[0]][temp[1]].getX()-priority.get(0)[0]) + Math.abs(grid[temp[0]][temp[1]].getY()-priority.get(0)[1]));
					grid[temp[0]][temp[1]].setF(grid[temp[0]][temp[1]].getH() + grid[temp[0]][temp[1]].getG());
					if(ClosedSet.isEmpty() == false) {
						for(int w = 0; w < ClosedSet.size(); w++) {
							if(ClosedSet.get(w)[0] == temp[0] && ClosedSet.get(w)[1] == temp[1] || grid[temp[0]][temp[1]].GetWall() != false)
								neighbor = 1;
						}
						if (neighbor == 0){
							grid[temp[0]][temp[1]].add_parent(OpenSet.get(lowestIndex)[0], OpenSet.get(lowestIndex)[1]);
							OpenSet.add(temp);
						}
					}
				}
					OpenSet.remove(lowestIndex);
			}
		}
	}
	
	public void pathe() { // moves 
		 
		if( path.isEmpty() == false){  
			//System.out.print("GUMALAW AKO PUTANG INA\n");
			//System.out.println(position[0]);
			//System.out.println(position[1]);
			if(path.get(0)[0] == position[0]){
				if(path.get(0)[1] > position[1])
					moveList.add(DOWN);
				else if(path.get(0)[1] < position[1])
					moveList.add(UP);
				else if(path.get(0)[1] == position[1])
					moveList.add(STAY);
			}
			else if(path.get(0)[0] > position[0])
		    	moveList.add(RIGHT);
			else if(path.get(0)[0] < position[0])
				moveList.add(LEFT);
		    
			System.out.println("\n\n==========================="
					+ "\nCURRENT POSTION:" + position[0] + " , "+ position[1]);
			
			System.out.println("\nPrint Priority:");
			for(int y = 0; y < priority.size(); y++)
				System.out.print(priority.get(y)[0] +" , "+priority.get(y)[1] +" , "+ priority.get(y)[2]+"\n");
			
			System.out.println("\nPath List:");
			for(int y = 0; y < path.size(); y++)
				System.out.print(path.get(path.size() - 1 -y)[0] +" , "+path.get(path.size() - 1 - y)[1] +"\n");
			
			position[0] = path.get(0)[0];
			position[1] = path.get(0)[1];
			path.remove(0);
		}
		starting = 0;
	}
	
	@Override
	public void think() {
		
		if (starting == 0) {
			starting = 1;
			startup();
		}
		TresureCheck(); 
		Goal_Priority();
		if(thinking == 1) {
			thinking = 0;
            PathFinding();
		}
		pathe();
	}
}



class Grid {
	
	protected int x;
	protected int y;
	protected int f_score = 0;//total travel cost
	protected int g_score = 0;//cost from start to specific node
	protected int h_score = 0;//cost from specific node to target node
	public ArrayList<int[]> Neigbors = new ArrayList<int[]>();
	public int[] parent = new int[2];
	public int has_parent = 0;
	protected boolean isWall = false;
	
	public void add_parent( int x , int y)
	{
		parent[0] = x;
		parent[1] = y;
		has_parent = 1;
		
	}
	
	public boolean GetWall() {
		return isWall;
	}
	
	public void SetWall(boolean c) {
		isWall = c;
	}
	
	public int getParent() {
		return has_parent;
	}
		
	public void add_neighbor(int x, int y)
	{
		int[] coordinates = {x , y};
		
		Neigbors.add(coordinates);
	}
		
		
	public void setX ( int input) {
		x = input;
	}
	
	public void setY ( int input) {
		y = input;
	}
	
	public int getY() {
		return y;
	}
	
	public int getX() {
		return x;
	}

	//Pertaining to f(n) = g(n) + h(n)
	public void setF ( int input) { //total travel cost
		f_score = input;
	}
	
	public int getF() {
		return f_score;
	}
	
	public void setG ( int input) { //cost from start to specific node
		g_score = input;
	}
	
	public int getG() {
		return g_score;
	}
	
	public void setH ( int input) { //cost from specific node to target node
		h_score = input;
	}
	
	public int getH() {
		return h_score;
	}
}*/

/*package bots;

import java.util.ArrayList;
import game.Block;
import game.Character;
import game.Treasure;
import game.Wall;

public class BotDave extends Character {
	protected ArrayList<int[]> priority = new ArrayList<int[]>(); 
	protected int Goal_check = 0;
	protected int Treasure_check = 0;	
	protected ArrayList<int[]> OpenSet = new ArrayList<int[]>();
	protected ArrayList<int[]> ClosedSet = new ArrayList<int[]>();
	protected ArrayList<int[]> path = new ArrayList<int[]>();
	protected int thinking = 1;
	protected int starting = 0;
	protected int[] position = {this.x , this.y}; 
	protected Grid[][] grid = new Grid[blocks.length][blocks[0].length] ;
	
	public BotDave(Block[][] blocks, int x, int y) {
		super(blocks, x, y);
	}
	
	public void startup() {  // Function for specifying every node in the map
	
		for(int j = 0; j < blocks.length; j++) {
	        for(int i = 0; i < blocks[0].length; i++){	 
	        	  
	        	grid[j][i] = new Grid();
	        	grid[j][i].setX(j);
	        	grid[j][i].setY(i);
	        	
	        	//Assign each block to the side that is within the bounds of the map as neighbor
	        	if(j != 0   )
	        		grid[j][i].add_neighbor(j - 1, i);
	        	if(j != blocks.length - 1  )
	        		grid[j][i].add_neighbor(j + 1, i);
	        	if(i != 0  )
	        		grid[j][i].add_neighbor(j, i - 1);		
	        	if(i != blocks[0].length - 1 )
	        		grid[j][i].add_neighbor(j, i + 1);
	        	
	        	if(blocks[j][i] instanceof Wall)
	        		grid[j][i].SetWall(true);
	        }
		}
	}
	
	private void TresureCheck() {//checks changes in treasure counts

		if(Treasure_check == 0) { // first check at the start
			for(int j = 0; j < blocks.length; j++) {
				for(int i = 0; i < blocks[0].length; i++)
					if(blocks[j][i] instanceof Treasure){	
				    	int cluster = 0;
						int minSubMapX = j-blocks.length/4;
						int maxSubMapX = j+blocks.length/4;
						if(minSubMapX <= 0) {
							minSubMapX = 1;
						}
						if(maxSubMapX >= blocks.length) {
							maxSubMapX = blocks.length-1;
						}
						int minSubMapY = i-blocks[0].length/2;
						int maxSubMapY = i+blocks[0].length/2;
						if(minSubMapY <= 0) {
							minSubMapY = 1;
						}
						if(maxSubMapY >= blocks[0].length) {
							maxSubMapY = blocks[0].length-1;
						}
						
						while(minSubMapX < maxSubMapX) {
							minSubMapY = i-blocks[0].length/2;
							if(minSubMapY <= 0) {
								minSubMapY = 1;
							}
					        while(minSubMapY < maxSubMapY){
					        	if(blocks[minSubMapX][minSubMapY] instanceof Treasure)
					        		cluster++;
					        	minSubMapY++;
					        }
					        minSubMapX++;
					    } 
						int[] ColRowCost = { j , i , Math.abs(j - this. x) + Math.abs( i - position[1]), cluster };
		        	 	priority.add(ColRowCost);
					}
			}
			Treasure_check = 1;
		}
		else if (Treasure_check == 1){ // proceeding checks stores values temporarily 
			ArrayList<int[]> temp_prio = new ArrayList<int[]>();
			
			for(int j = 0; j < blocks.length; j++) {
				for(int i = 0; i < blocks[0].length; i++)
					if(blocks[j][i] instanceof Treasure){	
						int cluster = 0;
						int minSubMapX = j-blocks.length/4;
						int maxSubMapX = j+blocks.length/4;
						if(minSubMapX <= 0) {
							minSubMapX = 1;
						}
						if(maxSubMapX >= blocks.length) {
							maxSubMapX = blocks.length-1;
						}
						int minSubMapY = i-blocks[0].length/2;
						int maxSubMapY = i+blocks[0].length/2;
						if(minSubMapY <= 0) {
							minSubMapY = 1;
						}
						if(maxSubMapY >= blocks[0].length) {
							maxSubMapY = blocks[0].length-1;
						}
						
						while(minSubMapX < maxSubMapX) {
							minSubMapY = i-blocks[0].length/2;
							if(minSubMapY <= 0) {
								minSubMapY = 1;
							}
					        while(minSubMapY < maxSubMapY){
					        	if(blocks[minSubMapX][minSubMapY] instanceof Treasure)
					        		cluster++;
					        	minSubMapY++;
					        }
					        minSubMapX++;
					    } 
						int[] ColRowCost = { j , i , Math.abs(j - this. x) + Math.abs( i - position[1]), cluster };
				        temp_prio.add(ColRowCost);
			          }
			}
			if (temp_prio.size() < priority.size()){ // if treasure decreased, Update treasure list
				int a = temp_prio.size();
				//int b = priority.size();
				int c = 0;
				
				while( c != priority.size())
					priority.remove(0);
				while(a != c){
					priority.add(temp_prio.get(c));
					c++;
				}
				thinking = 1;
			}
		}
	}
	
	private void Goal_Priority() { // sets the priority for each treasure
		int k = priority.size();
		int tempCluster = 0;
		int tempCost = 0;
		int tempY = 0;
		int tempX = 0;
		while( k != 0) {
			for( int i = 0; i < k; i++){ 
				if ( i != k - 1) {
					if (priority.get(i)[2] > priority.get(i + 1)[2] ) {// if Treasure is farther move down the list
						
						tempCluster = priority.get(i + 1)[3];
						priority.get(i + 1)[3] = priority.get(i)[3];
						priority.get(i)[3] = tempCluster;
						
						tempCost = priority.get(i + 1)[2];
						priority.get(i + 1)[2] = priority.get(i)[2];
						priority.get(i)[2] = tempCost;
						
						tempY = priority.get(i + 1)[1];
						priority.get(i + 1)[1] = priority.get(i)[1];
						priority.get(i)[1] = tempY;
						
						tempX = priority.get(i + 1)[0];
						priority.get(i + 1)[0] = priority.get(i)[0];
						priority.get(i)[0] = tempX;
					}

					if (priority.get(i)[3] < priority.get(i+1)[3]) {// if Treasure is farther move down the list
						
						tempCluster = priority.get(i + 1)[3];
						priority.get(i + 1)[3] = priority.get(i)[3];
						priority.get(i)[3] = tempCluster;
						
						tempCost = priority.get(i + 1)[2];
						priority.get(i + 1)[2] = priority.get(i)[2];
						priority.get(i)[2] = tempCost;
						
						tempY = priority.get(i + 1)[1];
						priority.get(i + 1)[1] = priority.get(i)[1];
						priority.get(i)[1] = tempY;
						
						tempX = priority.get(i + 1)[0];
						priority.get(i + 1)[0] = priority.get(i)[0];
						priority.get(i)[0] = tempX;
					}
				}
			}
			k--;
		}
		
		if(path.isEmpty() == false && priority.isEmpty() == false) // if priority changes , change pathing
			if(priority.get(0) != path.get(path.size() - 1))
				thinking = 1;
		while(path.isEmpty() == false)
			path.remove(0);
	}
	
	

private void PathFinding() { // finds the optimal path to the treasure with top priority
	OpenSet.add(position);	
	if(priority.isEmpty() == false)	
		while(OpenSet.isEmpty() == false){
			int lowestIndex = 0;
			for (int i = 0; i < OpenSet.size(); i++){ // gets neighbor node with lowest cost
				if( grid[OpenSet.get(i)[0]][OpenSet.get(i)[1]].getF() 
						< grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].getF()){
					lowestIndex = i;
				}
			}
			
			if(OpenSet.get(lowestIndex)[0] == priority.get(0)[0] 
					&& OpenSet.get(lowestIndex)[1] == priority.get(0)[1]) { // if already at treasure
				starting = 0; 
				int g = OpenSet.get(lowestIndex)[0];
				int h = OpenSet.get(lowestIndex)[1];
				while(grid[g][h].getParent() == 1) {
					int[] temp = {g ,h };
					path.add(0,temp);
					g = grid[temp[0]][temp[1]].parent[0];
					h = grid[temp[0]][temp[1]].parent[1];
				}
				while( OpenSet.isEmpty() == false )
					OpenSet.remove(0);
				while( ClosedSet.isEmpty() == false )
					ClosedSet.remove(0);
			}
			else { // not in treasure yet , add neighbors, that are not walls, to the set of options
				ClosedSet.add(OpenSet.get(lowestIndex));
				for ( int x = 0 ; 
						x < grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].Neigbors.size(); 
						x++){   
					int neighbor = 0;
					int[] temp = {grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].Neigbors.get(x)[0], 
							grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].Neigbors.get(x)[1]};
					grid[temp[0]][temp[1]].setG(grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].getG() + 1);
					grid[temp[0]][temp[1]].setH(Math.abs(grid[temp[0]][temp[1]].getX()-priority.get(0)[0]) + Math.abs(grid[temp[0]][temp[1]].getY()-priority.get(0)[1]));
					grid[temp[0]][temp[1]].setF(grid[temp[0]][temp[1]].getH() + grid[temp[0]][temp[1]].getG());
					if(ClosedSet.isEmpty() == false) {
						for(int w = 0; w < ClosedSet.size(); w++) {
							if(ClosedSet.get(w)[0] == temp[0] && ClosedSet.get(w)[1] == temp[1] || grid[temp[0]][temp[1]].GetWall() != false)
								neighbor = 1;
						}
						if (neighbor == 0){
							grid[temp[0]][temp[1]].add_parent(OpenSet.get(lowestIndex)[0], OpenSet.get(lowestIndex)[1]);
							OpenSet.add(temp);
						}
					}
				}
					OpenSet.remove(lowestIndex);
			}
		}
	}
	
	public void pathe() { // moves 
		 
		if( path.isEmpty() == false){  
			//System.out.print("GUMALAW AKO PUTANG INA\n");
			//System.out.println(position[0]);
			//System.out.println(position[1]);
			if(path.get(0)[0] == position[0]){
				if(path.get(0)[1] > position[1])
					moveList.add(DOWN);
				else if(path.get(0)[1] < position[1])
					moveList.add(UP);
				else if(path.get(0)[1] == position[1])
					moveList.add(STAY);
			}
			else if(path.get(0)[0] > position[0])
		    	moveList.add(RIGHT);
			else if(path.get(0)[0] < position[0])
				moveList.add(LEFT);
		    
			System.out.println("\n\n==========================="
					+ "\nCURRENT POSTION:" + position[0] + " , "+ position[1]);
			
			System.out.println("\nPrint Priority:");
			for(int y = 0; y < priority.size(); y++)
				System.out.print(priority.get(y)[0] +" , "+priority.get(y)[1] +" , "+ priority.get(y)[2]+" , "+ priority.get(y)[3]+"\n");
			
			System.out.println("\nPath List:");
			for(int y = 0; y < path.size(); y++)
				System.out.print(path.get(path.size() - 1 -y)[0] +" , "+path.get(path.size() - 1 - y)[1] +"\n");
			
			position[0] = path.get(0)[0];
			position[1] = path.get(0)[1];
			path.remove(0);
		}
		starting = 0;
	}
	
	@Override
	public void think() {
		
		if (starting == 0) {
			starting = 1;
			startup();
		}
		TresureCheck(); 
		if(thinking == 1) {
			thinking = 0;
			Goal_Priority();
            PathFinding();
		}
		pathe();
	}
}



class Grid {
	
	protected int x;
	protected int y;
	protected int f_score = 0;//total travel cost
	protected int g_score = 0;//cost from start to specific node
	protected int h_score = 0;//cost from specific node to target node
	public ArrayList<int[]> Neigbors = new ArrayList<int[]>();
	public int[] parent = new int[2];
	public int has_parent = 0;
	protected boolean isWall = false;
	
	public void add_parent( int x , int y)
	{
		parent[0] = x;
		parent[1] = y;
		has_parent = 1;
		
	}
	
	public boolean GetWall() {
		return isWall;
	}
	
	public void SetWall(boolean c) {
		isWall = c;
	}
	
	public int getParent() {
		return has_parent;
	}
		
	public void add_neighbor(int x, int y)
	{
		int[] coordinates = {x , y};
		
		Neigbors.add(coordinates);
	}
		
		
	public void setX ( int input) {
		x = input;
	}
	
	public void setY ( int input) {
		y = input;
	}
	
	public int getY() {
		return y;
	}
	
	public int getX() {
		return x;
	}

	//Pertaining to f(n) = g(n) + h(n)
	public void setF ( int input) { //total travel cost
		f_score = input;
	}
	
	public int getF() {
		return f_score;
	}
	
	public void setG ( int input) { //cost from start to specific node
		g_score = input;
	}
	
	public int getG() {
		return g_score;
	}
	
	public void setH ( int input) { //cost from specific node to target node
		h_score = input;
	}
	
	public int getH() {
		return h_score;
	}
}*/

/*package bots;

import java.util.ArrayList;
import game.Block;
import game.Character;
import game.Treasure;
import game.Wall;

public class BotDave extends Character {
	protected ArrayList<int[]> priority = new ArrayList<int[]>(); 
	protected int Goal_check = 0;
	protected int Treasure_check = 0;	
	protected ArrayList<int[]> OpenSet = new ArrayList<int[]>();
	protected ArrayList<int[]> ClosedSet = new ArrayList<int[]>();
	protected ArrayList<int[]> path = new ArrayList<int[]>();
	protected int thinking = 1;
	protected int starting = 0;
	protected int[] position = {this.x , this.y}; 
	protected Grid[][] grid = new Grid[blocks.length][blocks[0].length] ;
	protected int Clu = 0;
	
	public BotDave(Block[][] blocks, int x, int y) {
		super(blocks, x, y);
	}
	
	
	private void startup() {  // Function for specifying every node in the map
	
		for(int j = 0; j < blocks.length; j++) {
	        for(int i = 0; i < blocks[0].length; i++){	 
	        	grid[j][i] = new Grid();
	        	grid[j][i].setX(j);
	        	grid[j][i].setY(i);
	        	
	        	//Assign each block to the side that is within the bounds of the map as neighbor
	        	if(j != 0   )
	        		grid[j][i].add_neighbor(j - 1, i);
	        	if(j != blocks.length - 1  )
	        		grid[j][i].add_neighbor(j + 1, i);
	        	if(i != 0  )
	        		grid[j][i].add_neighbor(j, i - 1);		
	        	if(i != blocks[0].length - 1 )
	        		grid[j][i].add_neighbor(j, i + 1);
	        	
	        	if(blocks[j][i] instanceof Wall)
	        		grid[j][i].SetWall(true);
	        }
		}
	}
	
	
	private int cluster( int j , int i) {
		
		int AOE_X = (blocks.length/8);
		int AOE_Y = (blocks[0].length/8);
		
		int MinX = j - AOE_X;
		int MaxX = j + AOE_X;
		int MinY = i - AOE_Y;
		int MaxY = i + AOE_Y;
		int cluster = 0;
		
		if(MinX < 0)
			MinX = 0;
		if(MaxX > blocks.length - 1)
			MaxX = blocks.length - 1;
		if(MinY < 0)
			MinY = 0;
		if(MaxY > blocks[0].length - 1)
			MaxY = blocks[0].length - 1;
		for( int m = MinX; m <= MaxX;m++ )
			for( int n = MinY; n <= MaxY;n++){
				if(blocks[m][n] instanceof Treasure)
					cluster++;
			}
		return cluster;
	}
	
	
	private int costing(int x , int y) {
		
		int costing = 0;
		int[] pair = {x,y};
		OpenSet.add(position);	
		while(OpenSet.isEmpty() == false){
			int lowestIndex = 0;
			for (int i = 0; i < OpenSet.size(); i++){ // gets neighbor node with lowest cost
				if( grid[OpenSet.get(i)[0]][OpenSet.get(i)[1]].getF() 
						< grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].getF()){
					lowestIndex = i;
				}
			}
			if(OpenSet.get(lowestIndex)[0] == pair[0] && OpenSet.get(lowestIndex)[1] == pair[1]) { // if already at treasure
				starting = 0; 
				int g = OpenSet.get(lowestIndex)[0];
				int h = OpenSet.get(lowestIndex)[1];
				while(grid[g][h].getParent() == 1) {
					int[] temp = {g ,h };
					costing++;
					g = grid[temp[0]][temp[1]].parent[0];
					h = grid[temp[0]][temp[1]].parent[1];
				}
				while( OpenSet.isEmpty() == false )
					OpenSet.remove(0);
				while( ClosedSet.isEmpty() == false )
					ClosedSet.remove(0);
			}
			else { // not in treasure yet , add neighbors, that are not walls, to the set of options
				int g = OpenSet.get(lowestIndex)[0];
				int h = OpenSet.get(lowestIndex)[1];
				ClosedSet.add(OpenSet.get(lowestIndex));
				for ( int c = 0 ; c < grid[g][h].Neigbors.size(); c++){   
					int neighbor = 0;
					int[] temp = {grid[g][h].Neigbors.get(c)[0], grid[g][h].Neigbors.get(c)[1]};
					Grid tempGrid = grid[temp[0]][temp[1]];
					tempGrid.setG(grid[g][h].getG() + 1);
					tempGrid.setH(Math.abs(tempGrid.getX()-pair[0]) + Math.abs(tempGrid.getY()-pair[1]));
					tempGrid.setF(tempGrid.getH() + tempGrid.getG());
					if(ClosedSet.isEmpty() == false) {
						for(int w = 0; w < ClosedSet.size(); w++) {
							if(ClosedSet.get(w)[0] == temp[0] && ClosedSet.get(w)[1] == temp[1] || tempGrid.GetWall() != false)
								neighbor = 1;
						}
						if (neighbor == 0){
							grid[temp[0]][temp[1]].add_parent(g, h);
							OpenSet.add(temp);
						}
					}
				}
				OpenSet.remove(lowestIndex);
			}
		}
		return costing;
	}
	
	
	private void tresureCheck() {//checks changes in treasure counts
		
		if(Treasure_check == 0) { // first check at the start
			for(int j = 0; j < blocks.length; j++) {
				for(int i = 0; i < blocks[0].length; i++) {
					if(blocks[j][i] instanceof Treasure){	
						int[] ColRowCost = { j , i , costing(j,i) - cluster(j , i) };
		        	 	priority.add(ColRowCost);
					}
				}
			}
			Treasure_check = 1;
			thinking = 1;
		}
		else if (Treasure_check == 1){ // proceeding checks stores values temporarily 
			ArrayList<int[]> temp_prio = new ArrayList<int[]>();
			int size = 0;
			for(int j = 0; j < blocks.length; j++) {
				for(int i = 0; i < blocks[0].length; i++) {
					if(blocks[j][i] instanceof Treasure){	 
						size++;
			        }
				}
			}
			if (size < priority.size()){ // if treasure decreased, Update treasure list
				int a = size;
				int c = 0;
				for(int j = 0; j < blocks.length; j++) 
					for(int i = 0; i < blocks[0].length; i++)
						if(blocks[j][i] instanceof Treasure){	 
							int[] ColRowCost = { j , i , costing(j,i) - cluster(j , i) };
					        temp_prio.add(ColRowCost);
				          }
				while( c != priority.size())
					priority.remove(0);
				while(a != c){
					priority.add(temp_prio.get(c));
					c++;
				}
				thinking = 1;
			}
		}
	}
	
	
	private void goalPriority() { // sets the priority for each treasure
		
		int k = priority.size();
		int tempCost = 0;
		int tempY = 0;
		int tempX = 0;
		while( k != 0) {
			for( int i = 0; i < k - 1; i++){ 
				if ( i != k - 1) {
					 if (priority.get(i)[2] > priority.get(i + 1)[2]  ) {// if Treasure is farther move down the list
						tempCost = priority.get(i + 1)[2];
						priority.get(i + 1)[2] = priority.get(i)[2];
						priority.get(i)[2] = tempCost;
						
						tempY = priority.get(i + 1)[1];
						priority.get(i + 1)[1] = priority.get(i)[1];
						priority.get(i)[1] = tempY;
						
						tempX = priority.get(i + 1)[0];
						priority.get(i + 1)[0] = priority.get(i)[0];
						priority.get(i)[0] = tempX;
					}
				}
			}
			k--;
		}
		if(path.isEmpty() == false && priority.isEmpty() == false) { // if priority changes , change pathing
			if(priority.get(0) != path.get(path.size() - 1)) {
				thinking = 1;
			}
		}
		while(path.isEmpty() == false)
			path.remove(0);
	}
	
	
	private void pathFinding() { // finds the optimal path to the treasure with top priority
		
		OpenSet.add(position);	
		if(priority.isEmpty() == false)	{
			while(OpenSet.isEmpty() == false){
				int lowestIndex = 0;
				for (int i = 0; i < OpenSet.size(); i++){ // gets neighbor node with lowest cost
					if( grid[OpenSet.get(i)[0]][OpenSet.get(i)[1]].getF() 
							< grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].getF()){
						lowestIndex = i;
					}
				}
				if(OpenSet.get(lowestIndex)[0] == priority.get(0)[0] 
						&& OpenSet.get(lowestIndex)[1] == priority.get(0)[1]) { // if already at treasure
					starting = 0; 
					int g = OpenSet.get(lowestIndex)[0];
					int h = OpenSet.get(lowestIndex)[1];
					while(grid[g][h].getParent() == 1) {
						int[] temp = {g ,h };
						path.add(0,temp);
						g = grid[temp[0]][temp[1]].parent[0];
						h = grid[temp[0]][temp[1]].parent[1];
					}
					while( OpenSet.isEmpty() == false )
						OpenSet.remove(0);
					while( ClosedSet.isEmpty() == false )
						ClosedSet.remove(0);
				}
				else { // not in treasure yet , add neighbors, that are not walls, to the set of options
					ClosedSet.add(OpenSet.get(lowestIndex));
					int g = OpenSet.get(lowestIndex)[0];
					int h = OpenSet.get(lowestIndex)[1];
					for ( int x = 0 ; x < grid[g][h].Neigbors.size(); x++){   
						int neighbor = 0;
						int[] temp = {grid[g][h].Neigbors.get(x)[0], grid[g][h].Neigbors.get(x)[1]};
						Grid tempGrid = grid[temp[0]][temp[1]];
						tempGrid.setG(grid[g][h].getG() + 1);
						tempGrid.setH(Math.abs(tempGrid.getX()-priority.get(0)[0]) + Math.abs(tempGrid.getY()-priority.get(0)[1]));
						tempGrid.setF(tempGrid.getH() + tempGrid.getG());
						if(ClosedSet.isEmpty() == false) {
							for(int w = 0; w < ClosedSet.size(); w++) {
								if(ClosedSet.get(w)[0] == temp[0] && ClosedSet.get(w)[1] == temp[1] || tempGrid.GetWall() != false)
									neighbor = 1;
							}
							if (neighbor == 0){
								tempGrid.add_parent(g, h);
								OpenSet.add(temp);
							}
						}
					}
					OpenSet.remove(lowestIndex);
				}
			}
		}
	}
	
	
	private void pathe() { // moves 

		if( path.isEmpty() == false){  
			if(path.get(0)[0] == position[0]){
				if(path.get(0)[1] > position[1])
					moveList.add(DOWN);
				else if(path.get(0)[1] < position[1])
					moveList.add(UP);
				else if(path.get(0)[1] == position[1])
					moveList.add(STAY);
			}
			else if(path.get(0)[0] > position[0])
		    	moveList.add(RIGHT);
			else if(path.get(0)[0] < position[0])
				moveList.add(LEFT);
		    
			System.out.println("\n\n==========================="
					+"\nCURRENT POSTION:" + position[0] + " , "+ position[1]
					+"\nPrint Priority:");
			for(int y = 0; y < priority.size(); y++)
				System.out.print(priority.get(y)[0] +" , "+priority.get(y)[1] +" , "+ priority.get(y)[2]
						+ " ("+costing(priority.get(y)[0],priority.get(y)[1])+"-"+cluster(priority.get(y)[0] , priority.get(y)[1])+")\n");
			position[0] = path.get(0)[0];
			position[1] = path.get(0)[1];
			path.remove(0);
		}
		starting = 0;
	}
	
	
	@Override
	public void think() {
		
		if (starting == 0) {
			starting = 1;
			startup();
		}
		tresureCheck(); 
		if(thinking == 1) {
			thinking = 0;
			goalPriority();
            pathFinding();
		}
		pathe();
	}
}



class Grid {
	
	protected int x;
	protected int y;
	protected int f_score = 0;
	protected int g_score = 0;
	protected int h_score = 0;
	public ArrayList<int[]> Neigbors = new ArrayList<int[]>();
	public int[] parent = new int[2];
	public int has_parent = 0;
	protected boolean isWall = false;
	
	public void add_parent( int x , int y)
	{
		parent[0] = x;
		parent[1] = y;
		has_parent = 1;
	}
	public boolean GetWall() {
		return isWall;
	}
	public void SetWall(boolean c) {
		isWall = c;
	}
	public int getParent() {
		return has_parent;
	}
	public void add_neighbor(int x, int y)
	{
		int[] coordinates = {x , y};
		Neigbors.add(coordinates);
	}
	public void setX ( int input) {
		x = input;
	}
	public void setY ( int input) {
		y = input;
	}
	public int getY() {
		return y;
	}
	public int getX() {
		return x;
	}
	public void setF ( int input) {
		f_score = input;
	}
	public int getF() {
		return f_score;
	}
	public void setG ( int input) {
		g_score = input;
	}
	public int getG() {
		return g_score;
	}
	public void setH ( int input) {
		h_score = input;
	}
	public int getH() {
		return h_score;
	}
}*/

package bots;

import java.util.ArrayList;
import game.Block;
import game.Character;
import game.Treasure;
import game.Wall;

public class BotDave extends Character {
	protected ArrayList<int[]> priority = new ArrayList<int[]>(); 
	protected int Goal_check = 0;
	protected int Treasure_check = 0;	
	protected ArrayList<int[]> OpenSet = new ArrayList<int[]>();
	protected ArrayList<int[]> ClosedSet = new ArrayList<int[]>();
	protected ArrayList<int[]> path = new ArrayList<int[]>();
	protected int thinking = 1;
	protected int starting = 0;
	protected int[] position = {this.x , this.y}; 
	protected Grid[][] grid = new Grid[blocks.length][blocks[0].length] ;
	protected int Clu = 0;
	
	public BotDave(Block[][] blocks, int x, int y) {
		super(blocks, x, y);
	}
	
	
	public void startup() {  // Function for specifying every node in the map
	
		for(int j = 0; j < blocks.length; j++) {
	        for(int i = 0; i < blocks[0].length; i++){	 
	        	grid[j][i] = new Grid();
	        	grid[j][i].setX(j);
	        	grid[j][i].setY(i);
	        	
	        	//Assign each block to the side that is within the bounds of the map as neighbor
	        	if(j != 0   )
	        		grid[j][i].add_neighbor(j - 1, i);
	        	if(j != blocks.length - 1  )
	        		grid[j][i].add_neighbor(j + 1, i);
	        	if(i != 0  )
	        		grid[j][i].add_neighbor(j, i - 1);		
	        	if(i != blocks[0].length - 1 )
	        		grid[j][i].add_neighbor(j, i + 1);
	        	
	        	if(blocks[j][i] instanceof Wall)
	        		grid[j][i].SetWall(true);
	        }
		}
	}
	
	
	private int cluster( int j , int i) {
		
		int AOE_X = (blocks.length/8);
		int AOE_Y = (blocks[0].length/8);
		
		int MinX = j - AOE_X;
		int MaxX = j + AOE_X;
		int MinY = i - AOE_Y;
		int MaxY = i + AOE_Y;
		int cluster = 0;
		
		if(MinX < 0)
			MinX = 0;
		if(MaxX > blocks.length - 1)
			MaxX = blocks.length - 1;
		if(MinY < 0)
			MinY = 0;
		if(MaxY > blocks[0].length - 1)
			MaxY = blocks[0].length - 1;
		for( int m = MinX; m <= MaxX;m++ )
			for( int n = MinY; n <= MaxY;n++){
				if(blocks[m][n] instanceof Treasure)
					cluster++;
			}
		return cluster;
	}
	
	
	private int costing(int x , int y) {
		
		return Math.abs(position[0] - x ) + Math.abs(position[1] - y);
	}
	
	
	private void tresureCheck() {//checks changes in treasure counts
		
		if(Treasure_check == 0) { // first check at the start
			for(int j = 0; j < blocks.length; j++) {
				for(int i = 0; i < blocks[0].length; i++) {
					if(blocks[j][i] instanceof Treasure){	
						int[] ColRowCost = { j , i , costing(j,i) - cluster(j , i) };
		        	 	priority.add(ColRowCost);
					}
				}
			}
			Treasure_check = 1;
			thinking = 1;
		}
		else if (Treasure_check == 1){ // proceeding checks stores values temporarily 
			ArrayList<int[]> temp_prio = new ArrayList<int[]>();
			int size = 0;
			for(int j = 0; j < blocks.length; j++) {
				for(int i = 0; i < blocks[0].length; i++) {
					if(blocks[j][i] instanceof Treasure){	 
						size++;
			        }
				}
			}
			if (size < priority.size()){ // if treasure decreased, Update treasure list
				int a = size;
				int c = 0;
				for(int j = 0; j < blocks.length; j++) 
					for(int i = 0; i < blocks[0].length; i++)
						if(blocks[j][i] instanceof Treasure){	 
							int[] ColRowCost = { j , i , costing(j,i) - cluster(j , i) };
					        temp_prio.add(ColRowCost);
				          }
				while( c != priority.size())
					priority.remove(0);
				while(a != c){
					priority.add(temp_prio.get(c));
					c++;
				}
				thinking = 1;
			}
		}
	}
	
	
	private void goalPriority() { // sets the priority for each treasure
		
		int k = priority.size();
		int tempCost = 0;
		int tempY = 0;
		int tempX = 0;
		while( k != 0) {
			for( int i = 0; i < k - 1; i++){ 
				if ( i != k - 1) {
					 if (priority.get(i)[2] > priority.get(i + 1)[2]  ) {// if Treasure is farther move down the list
						tempCost = priority.get(i + 1)[2];
						priority.get(i + 1)[2] = priority.get(i)[2];
						priority.get(i)[2] = tempCost;
						
						tempY = priority.get(i + 1)[1];
						priority.get(i + 1)[1] = priority.get(i)[1];
						priority.get(i)[1] = tempY;
						
						tempX = priority.get(i + 1)[0];
						priority.get(i + 1)[0] = priority.get(i)[0];
						priority.get(i)[0] = tempX;
					}
				}
			}
			k--;
		}
		if(path.isEmpty() == false && priority.isEmpty() == false) { // if priority changes , change pathing
			if(priority.get(0) != path.get(path.size() - 1)) {
				thinking = 1;
			}
		}
		while(path.isEmpty() == false)
			path.remove(0);
	}
	
	
	private void pathFinding() { // finds the optimal path to the treasure with top priority
		
		OpenSet.add(position);	
		if(priority.isEmpty() == false)	{
			while(OpenSet.isEmpty() == false){
				int lowestIndex = 0;
				for (int i = 0; i < OpenSet.size(); i++){ // gets neighbor node with lowest cost
					if( grid[OpenSet.get(i)[0]][OpenSet.get(i)[1]].getF() 
							< grid[OpenSet.get(lowestIndex)[0]][OpenSet.get(lowestIndex)[1]].getF()){
						lowestIndex = i;
					}
				}
				if(OpenSet.get(lowestIndex)[0] == priority.get(0)[0] 
						&& OpenSet.get(lowestIndex)[1] == priority.get(0)[1]) { // if already at treasure
					starting = 0; 
					int g = OpenSet.get(lowestIndex)[0];
					int h = OpenSet.get(lowestIndex)[1];
					while(grid[g][h].getParent() == 1) {
						int[] temp = {g ,h };
						path.add(0,temp);
						g = grid[temp[0]][temp[1]].parent[0];
						h = grid[temp[0]][temp[1]].parent[1];
					}
					while( OpenSet.isEmpty() == false )
						OpenSet.remove(0);
					while( ClosedSet.isEmpty() == false )
						ClosedSet.remove(0);
				}
				else { // not in treasure yet , add neighbors, that are not walls, to the set of options
					ClosedSet.add(OpenSet.get(lowestIndex));
					int g = OpenSet.get(lowestIndex)[0];
					int h = OpenSet.get(lowestIndex)[1];
					for ( int x = 0 ; x < grid[g][h].Neigbors.size(); x++){   
						int neighbor = 0;
						int[] temp = {grid[g][h].Neigbors.get(x)[0], grid[g][h].Neigbors.get(x)[1]};
						Grid tempGrid = grid[temp[0]][temp[1]];
						tempGrid.setG(grid[g][h].getG() + 1);
						tempGrid.setH(Math.abs(tempGrid.getX()-priority.get(0)[0]) + Math.abs(tempGrid.getY()-priority.get(0)[1]));
						tempGrid.setF(tempGrid.getH() + tempGrid.getG());
						if(ClosedSet.isEmpty() == false) {
							for(int w = 0; w < ClosedSet.size(); w++) {
								if(ClosedSet.get(w)[0] == temp[0] && ClosedSet.get(w)[1] == temp[1] || tempGrid.GetWall() != false)
									neighbor = 1;
							}
							if (neighbor == 0){
								tempGrid.add_parent(g, h);
								OpenSet.add(temp);
							}
						}
					}
					OpenSet.remove(lowestIndex);
				}
			}
		}
	}
	
	
	public void pathe() { // moves 

		if( path.isEmpty() == false){  
			if(path.get(0)[0] == position[0]){
				if(path.get(0)[1] > position[1])
					moveList.add(DOWN);
				else if(path.get(0)[1] < position[1])
					moveList.add(UP);
				else if(path.get(0)[1] == position[1])
					moveList.add(STAY);
			}
			else if(path.get(0)[0] > position[0])
		    	moveList.add(RIGHT);
			else if(path.get(0)[0] < position[0])
				moveList.add(LEFT);
		    
			
			position[0] = path.get(0)[0];
			position[1] = path.get(0)[1];
			path.remove(0);
		}
		starting = 0;
	}
	
	
	@Override
	public void think() {
		
		if (starting == 0) {
			starting = 1;
			startup();
		}
		tresureCheck(); 
		goalPriority();
		if(thinking == 1) {
			thinking = 0;
            pathFinding();
		}
		pathe();
	}
}



class Grid {
	
	protected int x;
	protected int y;
	protected int f_score = 0;
	protected int g_score = 0;
	protected int h_score = 0;
	public ArrayList<int[]> Neigbors = new ArrayList<int[]>();
	public int[] parent = new int[2];
	public int has_parent = 0;
	protected boolean isWall = false;
	
	public void add_parent( int x , int y)
	{
		parent[0] = x;
		parent[1] = y;
		has_parent = 1;
	}
	public boolean GetWall() {
		return isWall;
	}
	public void SetWall(boolean c) {
		isWall = c;
	}
	public int getParent() {
		return has_parent;
	}
	public void add_neighbor(int x, int y)
	{
		int[] coordinates = {x , y};
		Neigbors.add(coordinates);
	}
	public void setX ( int input) {
		x = input;
	}
	public void setY ( int input) {
		y = input;
	}
	public int getY() {
		return y;
	}
	public int getX() {
		return x;
	}
	public void setF ( int input) {
		f_score = input;
	}
	public int getF() {
		return f_score;
	}
	public void setG ( int input) {
		g_score = input;
	}
	public int getG() {
		return g_score;
	}
	public void setH ( int input) {
		h_score = input;
	}
	public int getH() {
		return h_score;
	}
}
