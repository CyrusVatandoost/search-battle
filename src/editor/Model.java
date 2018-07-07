package editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import game.Block;
import game.Display;
import game.Map;
import game.Reader;
import game.Wall;

public class Model {

	private static final int SPACE = 0;
	private static final int WALL = 1;
	private static final int TREASURE = 2;
	private static final int BOT = 3;
	private static final int ENEMY = 4;
	private static final int ANDY = 5;
	private static final int BRAD = 6;
	private static final int CHIP = 7;
	private static final int DAVE = 8;
	
	private Editor editor;
	private Display display;
	private Map map;
	private int tool = SPACE;
	private String fileName;
	
	public Model() {
		fileName = "map.txt";
		this.setMap(fileName);
	}
	
	public void setMap(String fileName) {
		map = Reader.getMap(fileName);
		editor = new Editor(new Controller(this));
		editor.setVisible(true);
		display = new Display(map);
		editor.setContentPane(display);
		editor.setExtendedState(JFrame.MAXIMIZED_BOTH);
		editor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		display.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				int[] coordinates = display.getCoordinates(me.getX(), me.getY());
				System.out.println(coordinates[0] + ", " + coordinates[1]);
				add(coordinates[0], coordinates[1]);
			}
		});
		
		display.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent me) {
				int[] coordinates = display.getCoordinates(me.getX(), me.getY());
				System.out.println(coordinates[0] + ", " + coordinates[1]);
				add(coordinates[0], coordinates[1]);
			}
		});
	}
	
	public void newMap(int width, int height) {
		Map map = new Map(height, width);
		for(int y = 0; y < map.getHeight(); y++) {
			for(int x = 0; x < map.getWidth(); x++) {
				if(x == 0 || y == 0 || x == width -1 || y == height - 1) {
					map.getBlocks()[x][y] = new Wall();
				}
				else {
					map.getBlocks()[x][y] = new Block();
				}
			}
		}
		this.map = map;
		display.setMap(map);
	}
	
	public void openMap(String fileName) {
		this.fileName = fileName;
		map = Reader.getMap(fileName);
		display.setMap(map);
	}
	
	public void add(int x, int y) {
		switch(tool) {
			case SPACE:
				this.addSpace(x, y);
				break;
			case WALL:
				this.addWall(x, y);
				break;
			case TREASURE:
				this.addTreasure(x, y);
				break;
			case BOT:
				this.addBot(x, y);
				break;
			case ENEMY:
				this.addEnemy(x, y);
				break;
			case ANDY:
				this.addBotAndy(x, y);
				break;
			case BRAD:
				this.addBotBrad(x, y);
				break;
			case CHIP:
				this.addBotChip(x, y);
				break;
			case DAVE:
				this.addBotDave(x, y);
				break;
		}
	}
	
	public void addSpace(int x, int y) {
		map.addSpace(x, y);
	}
	
	public void addWall(int x, int y) {
		map.addWall(x, y);
	}
	
	public void addTreasure(int x, int y) {
		map.addTreasure(x, y);
	}
	
	public void addBot(int x, int y) {
		map.addBotAndy(x, y);
	}
	
	public void addEnemy(int x, int y) {
		map.addEnemy(x, y);
	}
	
	public void addBotAndy(int x, int y) {
		map.addBotAndy(x, y);
	}
	
	public void addBotBrad(int x, int y) {
		map.addBotBrad(x, y);
	}
	
	public void addBotChip(int x, int y) {
		map.addBotChip(x, y);
	}
	
	public void addBotDave(int x, int y) {
		map.addBotDave(x, y);
	}
	
	public void setTool(int tool) {
		this.tool = tool;
	}
	
	public void saveMap() {
		new Printer(map, fileName);
	}

}
