package game;

import java.awt.Color;
import java.util.ArrayList;

public class Map {

	private static final int UP = 0;
	private static final int DOWN = 1;
	private static final int LEFT = 2;
	private static final int RIGHT = 3;
	private static final int STAY = 4;
	
	private int height;
	private int width;
	private Block[][] blocks;

	private ArrayList<Character> characters;
	
	public Map(int height, int width) {
		this.setup(height, width);
//		this.startGame();
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
//		this.setMap();
	}
	
	/**
	 * This function sets up the game map and it's entities.
	 */
	public void setMap() {
		// add walls
		int x, y;
		for(y = 0; y < this.height; y++) {
			for(x = 0; x < this.width; x++) {
				if(y == 0 || x == 0 || y == this.height - 1 || x == this.width - 1) {
					this.addWall(x, y);
				}
			}
		}

		this.addCharacter(2, 2);
		this.addCharacter(2, 2);
		this.addCharacter(2, 2);
		this.addCharacter(2, 2);
		characters.add(new Character(this.blocks, 10, 10));
		characters.get(characters.size() - 1).setColor(Color.GREEN);
	}
	
	/**
	 * This function starts a thread that will run continuously updating the game.
	 */
	public void startGame() {
		Thread game = new Thread(new Runnable() {
			@Override
			public void run() {
                while (true) {
                	update();
                    try {Thread.sleep(100);} catch (Exception ex) {}
                }
			}
		});
		game.start();
	}
	
	public void addWall(int x, int y) {
		this.blocks[x][y] = new Wall();
	}
	
	public void addCharacter(int x, int y) {
		characters.add(new Character(this.blocks, x, y));
		characters.get(characters.size() - 1).setColor(Color.CYAN);
	}
	
	public void addCharacter(int x, int y, Color color) {
		characters.add(new Character(this.blocks, x, y));
		characters.get(characters.size() - 1).setColor(color);
	}
	
	public void addBotAndy(int x, int y) {
		characters.add(new BotAndy(blocks, x, y));
	}
	
	public void addTreasure(int x, int y) {
		this.blocks[x][y] = new Treasure();
	}
	
	public void addEnemy(int x, int y) {
		characters.add(new Enemy(blocks, x, y));
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
		for(Character boi : characters) {
			if(boi.direction() == UP) {
				this.moveUp(boi);
			}
			else if(boi.direction() == DOWN) {
				this.moveDown(boi);
			}
			else if(boi.direction() == LEFT) {
				this.moveLeft(boi);
			}
			else if(boi.direction() == RIGHT) {
				this.moveRight(boi);
			}
		}
		this.displayScores();
	}
	
	/**
	 * This function moves a block one block up.
	 * @param block This is the block to be moved.
	 */
	private void moveUp(Block block) {
		int x = block.getX();
		int y = block.getY();
		if(y > 0 && !(this.getBlocks()[x][y-1] instanceof Wall)) {
			this.blocks[x][y] = new Block();
			this.blocks[x][y - 1] = block;
			block.setLocation(x, y - 1);
		}
	}

	/**
	 * This function moves a block one block down.
	 * @param block This is the block to be moved.
	 */
	private void moveDown(Block block) {
		int x = block.getX();
		int y = block.getY();
		if(y < this.height - 1 && !(this.getBlocks()[x][y+1] instanceof Wall)) {
			this.blocks[x][y] = new Block();
			this.blocks[x][y + 1] = block;
			block.setLocation(x, y + 1);
		}
	}

	/**
	 * This function moves a block one block left.
	 * @param block This is the block to be moved.
	 */
	private void moveLeft(Block block) {
		int x = block.getX();
		int y = block.getY();
		if(x > 0 && !(this.getBlocks()[x-1][y] instanceof Wall)) {
			this.blocks[x][y] = new Block();
			this.blocks[x - 1][y] = block;
			block.setLocation(x - 1, y);
		}
	}

	/**
	 * This function moves a block one block right.
	 * @param block This is the block to be moved.
	 */
	private void moveRight(Block block) {
		int x = block.getX();
		int y = block.getY();
		if(x < this.width - 1 && !(this.getBlocks()[x+1][y] instanceof Wall)) {
			this.blocks[x][y] = new Block();
			this.blocks[x + 1][y] = block;
			block.setLocation(x + 1, y);
		}
	}
	
	
	public Block[][] getBlocks() {
		return blocks;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}

}
