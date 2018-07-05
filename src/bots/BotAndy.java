package bots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import game.Block;
import game.Character;
import game.Treasure;
import game.Wall;

public class BotAndy extends Character {

	private static final int TARGET = -1;
	private static final int SPACE = -2;
	private static final int EMPTY = -3;
	private static final int WALL = -4;
	private static final int BOT = -5;
	private ArrayList<Point> treasures;
	private Boolean thinking;
  
	public BotAndy(Block[][] blocks, int x, int y) {
		super(blocks, x, y);
		treasures = new ArrayList<Point>();
		thinking = false;
	}
  
  	@Override
  	public void think() {
  		if(!thinking) {
  			thinking = true;
  			if(treasures.isEmpty()) {
  				// search for all Treasures and put them into a list
  				// treasures.addAll(basicAlgorithm());
  				treasures.addAll(this.searchNearestTreasures());
  				// treasures.addAll(this.searchAStarTreasures());
  			}
  			else {
  				int targetX = treasures.get(0).getX();
  				int targetY = treasures.get(0).getY();
				// treasures.addAll(basicAlgorithm());
	  			// treasures.addAll(this.searchNearestTreasures());
  				
  				if(treasures.size() != this.getTreasures().size()) {
					treasures.clear();
	  				treasures.addAll(this.searchAStarTreasures());
  				}
  				
  				// if Bot has reached the target, remove the target from the list and search again
				if(isBotHere(targetX, targetY)) {
					// treasures.remove(0);
					treasures.clear();
	  				treasures.addAll(this.searchAStarTreasures());
		  			// treasures.addAll(this.searchNearestTreasures());
	  				if(treasures.size() > 0) {
	  					targetX = treasures.get(0).getX();
						targetY = treasures.get(0).getY();
	  				}
  				}
  				
  				aStarSearch(targetX, targetY);
  				// goTo(targetX, targetY);
  			}
  			thinking = false;
  		}
  	}
  	
	private void goToSearch(int x, int y) {
		if(this.x < x) {
			move = RIGHT;
		}
		else if(this.x > x) {
			move = LEFT;
		}
		else if(this.y < y) {
			move = DOWN;
		}
		else if(this.y > y) {
			move = UP;
		}
	}
  
	private void aStarSearch(int x, int y) {
		// System.out.println("init");
		// System.out.println(x + "," + y);
		
		// initialize variables
		int width = blocks.length;
		int height = blocks[0].length;
	  
		// initialize 2D array
		int[][] values = new int[width][height];
		for(int a = 0; a < width; a++) {
			for(int b = 0; b < height; b++) {
				if(blocks[a][b] instanceof Wall) {
					values[a][b] = WALL;
				}
				else {
					values[a][b] = EMPTY;
				}
			}
		}
	  
		// assign value to Treasure
		values[x][y] = TARGET;
	  
		// assign value to Bot
		values[this.getX()][this.getY()] = BOT;
	  
		// System.out.println("search");
		for(int i = TARGET; i < width * height * height; i++) {
			for(int a = 0; a < width; a++) {
				for(int b = 0; b < height; b++) {
					if(values[a][b] == i) {
						// System.out.println(a + "," + b);
						values = incrementAround(values, a, b, i);
					}
				}
			}
		}
	  
		int a = this.getX();
		int b = this.getY();
		int min = height * width;
		int tempMove = STAY;

		if(values[a][b - 1] < min && values[a][b - 1] > SPACE) {
			min = values[a][b - 1];
			tempMove = UP;
			// System.out.println("UP");
		}
		if(values[a][b + 1] < min && values[a][b + 1] > SPACE) {
			min = values[a][b + 1];
			tempMove = DOWN;
			// System.out.println("DOWN");
		}
		if(values[a - 1][b] < min && values[a - 1][b] > SPACE) {
			min = values[a - 1][b];
			tempMove = LEFT;
			// System.out.println("LEFT");
		}
		if(values[a + 1][b] < min && values[a + 1][b] > SPACE) {
			min = values[a + 1][b];
			tempMove = RIGHT;
			// System.out.println("RIGHT");
		}
	  
		move = tempMove;
		// System.out.println("move: " + move);
		// System.out.println("end");
	}
  
	private int[][] incrementAround(int[][] values, int x, int y, int i) {
		int width = values.length;
		int height = values[0].length;
		i++;
		// up
		if(y > 1) {
			if(values[x][y - 1] == EMPTY) {
				values[x][y - 1] = i;
			}
		}
		// down
		if(y < height - 2) {
			if(values[x][y + 1] == EMPTY) {
				values[x][y + 1] = i;
			}
		}
		// left
		if(x < width - 2) {
			if(values[x + 1][y] == EMPTY) {
				values[x + 1][y] = i;
			}
		}
		// right
		if(x > 1) {
			if(values[x - 1][y] == EMPTY) {
				values[x - 1][y] = i;
			}	
		}
		return values;
	}
  
	private int isBotAround(int[][] values, int x, int y) {
		int width = values.length;
		int height = values[0].length;
		if(y < height - 2) {
			if(values[x][y + 1] == BOT) {
				System.out.println("UP");
				return DOWN;
			}
		}
		// down
		else if(y > 1) {
			if(values[x][y - 1] == BOT) {
				System.out.println("DOWN");
				return UP;
			}
		}
		// left
		else if(x > 1) {
			if(values[x + 1][y] == BOT) {
				System.out.println("LEFT");
				return RIGHT;
			}
		}
		// right
		else if(x < width - 2) {
			if(values[x - 1][y] == BOT) {
				System.out.println("RIGHT");
				return LEFT;
			}
		}	
		return STAY;
	}
  
	private ArrayList<Point> getTreasures() {
		int height = blocks.length;
		int width = blocks[0].length;
    
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(this.blocks[y][x] instanceof Treasure) {
					treasures.add(new Point(y, x));
				}
			}
		}
    
		return treasures;
	}
	
	private ArrayList<Point> basicAlgorithm() {
		int height = blocks.length;
		int width = blocks[0].length;
    
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(this.blocks[y][x] instanceof Treasure) {
					treasures.add(new Point(y, x));
				}
			}
		}
    
		return treasures;
	}
	
	private ArrayList<Point> searchNearestTreasures() {
		int height = blocks.length;
		int width = blocks[0].length;
		Point bot = new Point(this.getX(), this.getY());
		ArrayList<Point> treasures = new ArrayList<Point>();		
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(this.blocks[y][x] instanceof Treasure) {
					treasures.add(new Point(y, x));
				}
			}
		}
		
		for(Point point : treasures) {
			point.setValue(this.calculateBasicDistance(point, bot));
		}
		
		Collections.sort(treasures, new Comparator<Point>() {
	    	@Override
	    	public int compare(Point lhs, Point rhs) {
	    		return lhs.getValue() < rhs.getValue() ? -1 : (lhs.getValue() > rhs.getValue()) ? 1 : 0;
	    	}
		});
		
		return treasures;
	}
	
	private ArrayList<Point> searchAStarTreasures() {
		int height = blocks.length;
		int width = blocks[0].length;
		Point bot = new Point(this.getX(), this.getY());
		ArrayList<Point> treasures = new ArrayList<Point>();		
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(this.blocks[y][x] instanceof Treasure) {
					treasures.add(new Point(y, x));
				}
			}
		}
		
		for(Point point : treasures) {
			point.setValue(this.calculateAStarDistance(point, bot));
		}
		
		Collections.sort(treasures, new Comparator<Point>() {
	    	@Override
	    	public int compare(Point lhs, Point rhs) {
	    		return lhs.getValue() < rhs.getValue() ? -1 : (lhs.getValue() > rhs.getValue()) ? 1 : 0;
	    	}
		});
		
		return treasures;
	}
  
	private ArrayList<Point> kruskalsAlgorithm() {
		return null;  
	}
  
	private int calculateBasicDistance(Point a, Point b) {
		int widthDiff = Math.abs(a.getX() - b.getY());
		int heightDiff = Math.abs(a.getY() - b.getY()); 
		return widthDiff + heightDiff;
	}
  
	private int calculateAStarDistance(Point c, Point d) {
		int width = blocks.length;
		int height = blocks[0].length;
	  
		// initialize 2D array
		int[][] values = new int[width][height];
		for(int a = 0; a < width; a++) {
			for(int b = 0; b < height; b++) {
				if(blocks[a][b] instanceof Wall) {
					values[a][b] = WALL;
				}
				else {
					values[a][b] = EMPTY;
				}
			}
		}
	  
		// assign value to Treasure
		values[c.getX()][c.getY()] = TARGET;
	  
		// assign value to Bot
		values[d.getX()][d.getY()] = BOT;
		
		Boolean loop = true;
	  
		for(int i = TARGET; i < width + height && loop; i++) {
			for(int a = 0; a < width && loop; a++) {
				for(int b = 0; b < height && loop; b++) {
					if(values[a][b] == i) {
						values = incrementAround(values, a, b, i);
						
						if(d.getX() == a && d.getY() == b) {
							loop = false;
						}
						
					}
				}
			}
		}
	  
		int a = this.getX();
		int b = this.getY();
		int min = height * width;

		if(values[a][b - 1] < min && values[a][b - 1] > SPACE) {
			min = values[a][b - 1];
		}
		if(values[a][b + 1] < min && values[a][b + 1] > SPACE) {
			min = values[a][b + 1];
		}
		if(values[a - 1][b] < min && values[a - 1][b] > SPACE) {
			min = values[a - 1][b];
		}
		if(values[a + 1][b] < min && values[a + 1][b] > SPACE) {
			min = values[a + 1][b];
		}
		
		return min;
	}
	
	private Boolean isBotHere(int x, int y) {
		return this.getX() == x && this.getY() == y;
	}

	private Boolean checkTarget(Point target) {
		return (blocks[target.getY()][target.getX()] instanceof Treasure);
	}
	
}


class Point {
  
	private int x;
	private int y;
	private int value;
  
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
  
	public int getX() {
		return x;
	}
  
	public int getY() {
		return y;
	}
	
	public int getValue() {
		return value;
	}
  
}

class Edge {
  
	private Point a;
	private Point b;
	private int distance;
  
	public Edge(Point a, Point b, int distance) {
		this.a = a;
		this.b = b;
		this.distance = distance;
	}
  
	public int distance() {
		return distance;
	}
  
	public Boolean hasPoint(Point point) {
		return point == a || point == b;
	}
  
}
