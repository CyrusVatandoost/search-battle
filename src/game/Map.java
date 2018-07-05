package game;

import java.awt.Color;
import java.util.ArrayList;

import bots.BotAndy;
import bots.BotBrad;
import bots.BotChip;
import bots.BotDave;

public class Map {

	private static final int UP = 0;
	private static final int DOWN = 1;
	private static final int LEFT = 2;
	private static final int RIGHT = 3;
	private static final int STAY = 4;
	private static final int NONE = 4;
	
	private int height;
	private int width;
	private Boolean pause = false;
	private Boolean stop = false;
	private Block[][] blocks;
	private ArrayList<Character> characters;
	private Thread thread;
	
	public Map(int height, int width) {
		this.setup(height, width);
	}
	
	/**
	 * This function sets up the game.
	 * @param height
	 * @param width
	 */
	public void setup(int height, int width) {
		this.height = height;
		this.width = width;
		this.blocks = new Block[this.width][this.height];
		this.characters = new ArrayList<Character>();
	}
	
	
	/**
	 * This function toggles pause/play of the game.
	 */
	public void togglePause() {
		pause = !pause;
	}
	
	/**
	 * This function starts a thread that will run continuously updating the game.
	 */
	public void startGame() {
		for(Character c : characters) {
			c.start();
		}
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Boolean running = true;
				while(running) {
	                while(!pause) {
		                if(stop) {
		                	pause = true;
		                	running = false;
		                	for(Character c : characters) {
		                		c.stop();
		                	}
		                }
	                	update();
	                    try {Thread.sleep(100);} catch (Exception ex) {}
	                }
				}
			}
		});
		thread.start();
	}
	
	public void endGame() {
		stop = true;
	}
	
	public void addWall(int x, int y) {
		this.blocks[x][y] = new Wall();
		this.blocks[x][y].setLocation(x, y);
	}
	
	public void addSpace(int x, int y) {
		this.blocks[x][y] = new Block();
		this.blocks[x][y].setLocation(x, y);
	}
	
	public void addTreasure(int x, int y) {
		this.blocks[x][y] = new Treasure();
		this.blocks[x][y].setLocation(x, y);
	}
	
	public void addCharacter(int x, int y) {
		characters.add(new Character(this.blocks, x, y));
		characters.get(characters.size() - 1).setColor(Color.CYAN);
		blocks[x][y] = characters.get(characters.size() - 1);
	}
	
	public void addCharacter(int x, int y, Color color) {
		characters.add(new Character(this.blocks, x, y));
		characters.get(characters.size() - 1).setColor(color);
		blocks[x][y] = characters.get(characters.size() - 1);
	}
	
	public void addEnemy(int x, int y) {
		characters.add(new Enemy(blocks, x, y));
		blocks[x][y] = characters.get(characters.size() - 1);
	}
	
	public void addBotAndy(int x, int y) {
		characters.add(new BotAndy(blocks, x, y));
		characters.get(characters.size() - 1).setColor(Color.GREEN);
		blocks[x][y] = characters.get(characters.size() - 1);
	}
	
	public void addBotBrad(int x, int y) {
		characters.add(new BotBrad(blocks, x, y));
		characters.get(characters.size() - 1).setColor(Color.PINK);
		blocks[x][y] = characters.get(characters.size() - 1);
	}
	
	public void addBotChip(int x, int y) {
		characters.add(new BotChip(blocks, x, y));
		characters.get(characters.size() - 1).setColor(Color.ORANGE);
		blocks[x][y] = characters.get(characters.size() - 1);
	}
	
	public void addBotDave(int x, int y) {
		characters.add(new BotDave(blocks, x, y));
		characters.get(characters.size() - 1).setColor(Color.MAGENTA);
		blocks[x][y] = characters.get(characters.size() - 1);
	}
	
	/**
	 * This function checks if the block at x and y is an instance of Treasure.
	 * @param x
	 * @param y
	 * @return
	 */
	public Boolean isTreasure(int x, int y) {
		return this.blocks[x][y] instanceof Treasure;
	}
	
	public void displayScores() {
		for(Character c : characters) {
			System.out.println(c.getPoints());
		}
	}
	
	/**
	 * This function checks all the Characters and get their requestion movement direction and applies it.
	 */
	private void update() {
		int direction = STAY;
		for(Character boi : characters) {
			if(!boi.moveList.isEmpty()) {
				direction = boi.moveList.get(0);
				boi.moveList.remove(0);
			}
			else if(boi.move != NONE) {
				direction = boi.getMove();
			}
			else {
				direction = boi.getDirection();
			}
			
			if(direction == UP) {
				this.moveUp(boi);
			}
			else if(direction == DOWN) {
				this.moveDown(boi);
			}
			else if(direction == LEFT) {
				this.moveLeft(boi);
			}
			else if(direction == RIGHT) {
				this.moveRight(boi);
			}
			else {
				this.stay(boi);
			}
			
		}
		printScores(); // this can be commented out to not show the scores
	}
	
	/**
	 * This function moves a block one block up.
	 * @param block This is the block to be moved.
	 */
	private void moveUp(Character character) {
		int x = character.getX();
		int y = character.getY();
		if(y > 0 && !(this.getBlocks()[x][y-1] instanceof Wall)) {
			if(isTreasure(x, y - 1)) {
				character.addPoints(1);
			}
			this.blocks[x][y] = new Block();
			this.blocks[x][y - 1] = character;
			character.setLocation(x, y - 1);
		}
	}

	/**
	 * This function moves a block one block down.
	 * @param block This is the block to be moved.
	 */
	private void moveDown(Character character) {
		int x = character.getX();
		int y = character.getY();
		if(y < this.height - 1 && !(this.getBlocks()[x][y+1] instanceof Wall)) {
			if(isTreasure(x, y + 1)) {
				character.addPoints(1);
			}
			this.blocks[x][y] = new Block();
			this.blocks[x][y + 1] = character;
			character.setLocation(x, y + 1);
		}
	}

	/**
	 * This function moves a block one block left.
	 * @param block This is the block to be moved.
	 */
	private void moveLeft(Character character) {
		int x = character.getX();
		int y = character.getY();
		if(x > 0 && !(this.getBlocks()[x-1][y] instanceof Wall)) {
			if(isTreasure(x - 1, y)) {
				character.addPoints(1);
			}
			this.blocks[x][y] = new Block();
			this.blocks[x - 1][y] = character;
			character.setLocation(x - 1, y);
		}
	}

	/**
	 * This function moves a block one block right.
	 * @param block This is the block to be moved.
	 */
	private void moveRight(Character character) {
		int x = character.getX();
		int y = character.getY();
		if(x < this.width - 1 && !(this.getBlocks()[x+1][y] instanceof Wall)) {
			if(isTreasure(x + 1, y)) {
				character.addPoints(1);
			}
			this.blocks[x][y] = new Block();
			this.blocks[x + 1][y] = character;
			character.setLocation(x + 1, y);
		}
	}
	
	private void stay(Character character) {
		int x = character.getX();
		int y = character.getY();
		this.blocks[x][y] = character;
		character.setLocation(x, y);
	}
	
	/**
	 * This function prints the scores of all the Characters in the Game.
	 */
	public void printScores() {
		System.out.println(characters.size());
		for(Character c : characters) {
			System.out.print(c.getClass() + ": " + c.getPoints());
			System.out.print(" (" + c.getX() + ", " + c.getY() + ")");
			System.out.println();
		}
		System.out.println();
	}
	
	public Block[][] getBlocks() {
		return blocks;
	}
	
	public ArrayList<Character> getCharacters() {
		return characters;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}

}
