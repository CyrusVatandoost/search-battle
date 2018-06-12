package game;

import java.awt.Color;

public class Character extends Block implements Runnable {

	protected static final int UP = 0;
	protected static final int DOWN = 1;
	protected static final int LEFT = 2;
	protected static final int RIGHT = 3;
	protected static final int STAY = 4;
	protected int direction = STAY;
	protected Block[][] blocks;
	protected int points;
	
	/**
	 * This function is the contructor of Character.
	 * @param blocks is the 2D array of the map containing what is inside the map.
	 * @param x is where the Character's x axis will spawn and where is it as the moment.
	 * @param y is where the Character's y axis will spawn and where is it at the moment.
	 */
	public Character(Block[][] blocks, int x, int y) {
		this.blocks = blocks;
		super.setLocation(x, y);
		this.start();
		this.points = 0;
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
		while(true) {
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
	
	/**
	 * This function returns the direction of where the Character wants to go.
	 * @return
	 */
	public int direction() {
		return this.direction;
	}
	
	public int getPoints() {
		return this.points;
	}

}
