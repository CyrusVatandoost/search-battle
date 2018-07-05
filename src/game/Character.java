package game;

import java.util.ArrayList;

public class Character extends Block implements Runnable {

	protected static final int UP = 0;
	protected static final int DOWN = 1;
	protected static final int LEFT = 2;
	protected static final int RIGHT = 3;
	protected static final int STAY = 4;
	protected static final int NONE = 4;
	protected int direction = STAY;
	protected int move = NONE;
	protected ArrayList<Integer> moveList;
	protected Block[][] blocks;
	protected int points;
	protected Boolean play = true;
	
	protected Boolean play = true;
	
	/**
	 * This function is the contructor of Character.
	 * @param blocks is the 2D array of the map containing what is inside the map.
	 * @param x is where the Character's x axis will spawn and where is it as the moment.
	 * @param y is where the Character's y axis will spawn and where is it at the moment.
	 */
	public Character(Block[][] blocks, int x, int y) {
		this.blocks = blocks;
		super.setLocation(x, y);
		this.points = 0;
		this.direction = STAY;
		this.moveList = new ArrayList<Integer> ();
	}
	
	/**
	 * This function starts the thread of Character that does that thinking.
	 */
	public void start() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * This function is what the thread will contain.
	 */
	@Override
	public void run() {
		while(play) {
            try {Thread.sleep(10);} catch (Exception ex) {}
            this.think();
		}
	}
	
	/**
	 * This function adds points to the character.
	 * @param points
	 */
	public void addPoints(int points) {
		this.points += points;
	}
	
	/**
	 * This function should contain the search methods which the character will
	 * make a decision from and choose a direction to go to.
	 */
	protected void think() {
		direction = (int)(Math.random() * 4);
	}
	
	protected void stop() {
		this.play = false;
	}
	
	/**
	 * This function returns the direction of where the Character wants to go.
	 * @return
	 */
	public int getDirection() {
		return this.direction;
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public int getMove() {
		int move = this.move;
		this.move = NONE;
		return move;
	}
	
	public ArrayList<Integer> getMoveList() {
		return this.moveList;
	}

}
