package bots;

import java.awt.Color;
import java.util.ArrayList;

import game.Block;
import game.Character;
import game.Treasure;
import game.Wall;

public class BotAndy extends Character {
	
	private ArrayList<Point> treasures;
	private ArrayList<Point> gaps;
	private int height, width;

	public BotAndy(Block[][] blocks, int x, int y) {
		super(blocks, x, y);
		
		treasures = new ArrayList<Point>();
		gaps = new ArrayList<Point>();
		width = blocks.length;
		height = blocks[0].length;
		
	}
	
	@Override
	public void think() {
		addTreasures();
		checkTreasures();
		
		if(treasures.isEmpty()) {
			direction = STAY;
		}
		else if(!(gaps.isEmpty())) {
			movetoGoal(x, y, gaps.get(0).getX(), gaps.get(0).getY());
			if(x == gaps.get(0).getX() && y == gaps.get(0).getY()) {
				getGaps();
			}
		}
		else {
			movetoGoal(x, y, treasures.get(0).getX(), treasures.get(0).getY());
			if(x == treasures.get(0).getX() && y == treasures.get(0).getY()) {
				getTreasures();
			}
		}
		
		
//		System.out.println(width + " " + height);
//		System.out.println(x + " " + y);
//		System.out.println(treasures.get(0).getX() + "  " + treasures.get(0).getY());
//		System.out.println(treasures.get(1).getX() + "  " + treasures.get(1).getY());
//		setValues();
		
		
	}
	
	public void movetoGoal(int startX, int startY, int goalX, int goalY) {
		if(compareGreatest(absoluteDistance(startX, goalX), absoluteDistance(startY, goalY)) == absoluteDistance(startY, goalY)) {
			if(realDistance(startY, goalY) > 0) {
				direction = UP;
				if(blocks[x][y-1] instanceof Wall) {
					System.out.println("hit wall up");
					checkWallX(y);
				}
			}
			else if(realDistance(startY, goalY) < 0) {
				direction = DOWN;
				if(blocks[x][y+1] instanceof Wall) {
					System.out.println("hit wall down");
					checkWallX(y);
				}
			}
			else {
				;
			}
		}
		else if(compareGreatest(absoluteDistance(startX, goalX), absoluteDistance(startY, goalY)) == absoluteDistance(startX, goalX)) {
			if(realDistance(startX, goalX) > 0) {
				direction = LEFT;
				if(blocks[x-1][y] instanceof Wall) {
					System.out.println("hit wall left");
					checkWallY(x);
				}
			}
			else if(realDistance(startX, goalX) < 0) {
				direction = RIGHT;
				if(blocks[x+1][y] instanceof Wall) {
					System.out.println("hit wall right");
					checkWallY(x);
				}
			}
			else {
				;
			}
		}
	}
	
//	public void movetoGoalX(int startX, int startY, int goalX, int goalY) {
//		if(realDistance(startX, goalX) > 0) {
//			direction = LEFT;
//			if(blocks[x-1][y] instanceof Wall) {
//				System.out.println("hit wall left");
//				checkWallY(x);
//			}
//		}
//		else if(realDistance(startX, goalX) < 0) {
//			direction = RIGHT;
//			if(blocks[x+1][y] instanceof Wall) {
//				System.out.println("hit wall right");
//				checkWallY(x);
//			}
//		}
//		else {
//			;
//		}
//	}
//	
//	public void movetoGoalY(int startX, int startY, int goalX, int goalY) {
//		if(realDistance(startY, goalY) > 0) {
//			direction = UP;
//			if(blocks[x][y-1] instanceof Wall) {
//				System.out.println("hit wall up");
//				checkWallX(y);
//			}
//		}
//		else if(realDistance(startY, goalY) < 0) {
//			direction = DOWN;
//			if(blocks[x][y+1] instanceof Wall) {
//				System.out.println("hit wall down");
//				checkWallX(y);
//			}
//		}
//		else {
//			;
//		}
//	}
	
	
	public void checkWallX(int y) {
		for(int row = this.x; row < width; row++) {
			if(!(blocks[row][y] instanceof Wall)) {
				addGaps(row, y);
			}
			else {
				break;
			}
		}
		for(int row = this.x; row > 0; row--) {
			if(!(blocks[row][y] instanceof Wall)) {
				addGaps(row, y);
			}
			else {
				break;
			}
		}
	}
	
	public void checkWallY(int x) {
		for(int col = this.y; col < height; col++) {
			if(!(blocks[x][col] instanceof Wall)) {
				addGaps(x, col);
			}
			else {
				break;
			}
		}
		for(int col = this.y; col > 0; col--) {
			if(!(blocks[x][col] instanceof Wall)) {
				addGaps(x, col);
			}
			else {
				break;
			}
		}
	}
	
	public void checkTreasures() {
		if(treasures.isEmpty()) {
			addTreasures();
		}
		else if(!(blocks[treasures.get(0).getX()][treasures.get(0).getY()] instanceof Treasure)) {
			getTreasures();
		}
		else {
			direction = STAY;
		}
	}
	
	public void getTreasures() {
		treasures.remove(0);
	}
	
	public void getGaps() {
		gaps.remove(0);
	}
	
	public void addTreasures() {
		if(treasures.isEmpty()) {
			for(int row = 0; row < width; row++) {
				for(int col = 0; col < height; col++) {
					if(blocks[row][col] instanceof Treasure) {
						treasures.add(new Point(row, col));
					}
				}
			}
		}
	}
	
	public void addGaps(int gx, int gy) {
		gaps.add(new Point(gx, gy));
	}
	
	public int compareLeast(int c1, int c2) {
		if(c1 < c2) {
			return c1;
		}
		else if(c2 < c1) {
			return c2;
		}
		else {
			int rand = (int)(Math.random() * 2);
			if(rand == 1) {
				return c1;
			}
			else {
				return c2;
			}
		}
	}
	
	public int compareGreatest(int c1, int c2) {
		if(c1 < c2) {
			return c2;
		}
		else if(c2 < c1) {
			return c1;
		}
		else {
			int rand = (int)(Math.random() * 2);
			if(rand == 1) {
				return c1;
			}
			else {
				return c2;
			}
		}
	}
	
	public int absoluteDistance(int ad1, int ad2) {
		if(ad1 > ad2) {
			return ad1 - ad2;
		}
		else if(ad2 > ad1) {
			return ad2 - ad1;
		}
		else {
			return 0;
		}
	}
	
	public int realDistance(int pl, int tr) {
		return pl - tr;
	}

}

class Point {
	
	public int tx;
	public int ty;
	
	public Point(int tx, int ty) {
		this.tx = tx;
		this.ty = ty;
	}
	
	public int getX() {
		return this.tx;
	}
	
	public int getY() {
		return this.ty;
	}
}