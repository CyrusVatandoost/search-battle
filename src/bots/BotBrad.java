package bots;

import java.util.*;

import game.Block;
import game.Character;
import game.Wall;
import game.Treasure;
 
public class BotBrad extends Character {

	public static Block[][] blocks2;
	public static int flag;
	public static int flag2;
	public static int flagmap;
	public static int flagdis;
	public static int recursemap;
	public static int treasurecount;
	public static int treasurecounted;
	public static int treasureindex;
	public static int flagthink;
	public static int treasurefound;
	public static int xtemp;
	public static int ytemp;
	public static int reset;
	List<Integer> Treasures = new ArrayList<Integer>();
	
	public BotBrad(Block[][] blocks, int x, int y) {
		super(blocks, x, y);
		
		flag=0;
		flag2=0;
		flagmap=0;
		recursemap=0;
		reset=0;
		flagdis=0;
		treasureindex=0;
		treasurefound=0;
		flagthink=0;
		xtemp=0;
		ytemp=0;
	}
	
	public int[][] getMap()
	{
		int row=blocks.length;
		int col=blocks[0].length;
		
		int[][] Map  = new int[row][col];		

		if(flagmap==0)
		{
			//read map into 2D array
			for(int i=0;i<col;i++)
			{
				for(int j=0;j<row;j++)
				{
					//wall = 1
					if(blocks[j][i] instanceof Wall)
						Map[j][i] = 1;
										
					//treasure = 3
					else if(blocks[j][i] instanceof Treasure)
						Map[j][i] = 2;
					
					//"unvisited" node
					else
						Map[j][i] = 4;
				}
			}
			flagmap++;
		}
		return Map;
	}
	
	//visited node = 3
	//not visited = 4
	public boolean depthFirst(int[][] map, int x, int y, List<Integer> path)	
	{
		//if target(treasure) has been found
		if(map[x][y]==2)
		{
			path.add(x);
			path.add(y);
			return true;
		}
		
		if(map[x][y]==4)
		{
			//mark current position as visited
			map[x][y]=3;
			
			/*
				the if statements here will visit the neighbor positions recursively to find a path
				
				if a path to the next treasure was found, the "path" list will be filled 
				with the coordinates of all positions of the path
			*/
			 int tempx=-1;
			 int tempy=0;
			 
			//check left
			if(depthFirst(map, x+tempx,y+tempy,path))
			{
				path.add(x);
				path.add(y);
				return true;
			}
			
			//check right
			tempx=1;
			tempy=0;
			if(depthFirst(map, x+tempx,y+tempy,path))
			{
				path.add(x);
				path.add(y);
				return true;
			}
			
			//check up
			tempx=0;
			tempy=-1;
			if(depthFirst(map, x+tempx,y+tempy,path))
			{
				path.add(x);
				path.add(y);
				return true;
			}
			
			//check down
			tempx=0;
			tempy=1;
			if(depthFirst(map, x+tempx,y+tempy,path))
			{
				path.add(x);
				path.add(y);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void think() 
	{
		int row=blocks.length;
		int col=blocks[0].length;
		
		int[][] Map = new int[row][col];
		
		int rise, run;
		int distance;
		
		// array of treasure coordinates with their distances (x,y,distance)
		ArrayList<Integer> TreasDis = new ArrayList<Integer>();		
		// array of sorted distances of each treasure based on TreasDis
		ArrayList<Integer> DistanceSorted = new ArrayList<Integer>();
		// array of sorted treasures based on DistanceSorted
		ArrayList<Integer> TreasureSorted = new ArrayList<Integer>();
		
		for(int i = 0;i<Treasures.size();i+=2)
		{
			if(x==Treasures.get(i+1) && y==Treasures.get(i))
				treasurefound=1;
		}
		
		if(treasurecounted==0)
		{
			for(int j=0;j<row;j++)
			{
				for(int i=0;i<col;i++)
				{
					if(blocks[j][i] instanceof Treasure)
					{
						//add x and y coordinates of treasure
						Treasures.add(i);
						Treasures.add(j);
						treasurecount++;
						run=j-x;
						rise=i-y;
						
						if(run<0)
							run*=-1;
						
						if(rise<0) 
							rise*=-1;
						
						//add distance
						distance=rise+run;
						TreasDis.add(j);
						TreasDis.add(i);
						TreasDis.add(distance);
					}
				}
			}
			treasurecounted=1;
		}
		if(flagdis==0)
		{
			//add distances into DistanceSorted for sorting
			for(int a=2;a<=TreasDis.size();a+=3)
				DistanceSorted.add(TreasDis.get(a));
			
			Collections.sort(DistanceSorted);
						
			int b=0;
			int a=2;
						
			while(TreasureSorted.size()!=2*DistanceSorted.size()) 
			{	
				//add treasures to TreasureSorted for sorting	
				if(TreasDis.get(a)==DistanceSorted.get(b)) 
				{
					TreasureSorted.add(TreasDis.get(a-2));
					TreasureSorted.add(TreasDis.get(a-1));
					TreasDis.remove(a);
					TreasDis.remove(a-1);
					TreasDis.remove(a-2);
					
					a=2;
					b++;
				}
				a+=3;
				
				if(TreasDis.size()==3)
				{
					TreasureSorted.add(TreasDis.get(0));
					TreasureSorted.add(TreasDis.get(1));
				}
				
				if(a>TreasDis.size())
					a=2;
			}
			
			flagdis=1;
		}
		
		if(treasureindex+2 < treasurecount*2 ) 
		{
			if((x == Treasures.get(treasureindex+1) && y == Treasures.get(treasureindex)) || treasurefound==1)
			{
				if(x == Treasures.get(treasureindex+1) && y == Treasures.get(treasureindex))
					treasureindex+=2;
				
				Map[x][y]=0;
				flagmap=0;
				treasurefound=0;
				flag=0;
				flag2=0;
				flagthink=0;
			}
		}
		
		while(treasureindex*2 != treasurecount && flagthink==0)
		{
			if(flag==0)
			{
				//re-initialize map array
				for(int j=0;j<row;j++)
				{
					for(int i=0;i<col;i++)
					{
						if(blocks[j][i] instanceof Wall)
							Map[j][i] = 1;
						
						else if(blocks[j][i] instanceof Treasure)
							Map[j][i] = 2;
								
						else
							Map[j][i] = 0;
					}
				}
				flag=1;
			}
			
			if(flag2==0)
			{
				ArrayList<Integer> path = new ArrayList<Integer>();
				flag2=1;
				
				if(xtemp==0)
					depthFirst(getMap(), x, y, path);
				
				else
					depthFirst(getMap(), xtemp, ytemp, path);
				
				Collections.reverse(path);
				
				int x1=x;
				int y1=y;
			
				for(int i=0;i<path.size();i+=2)
				{
					
					//UNCOMMENT TO TRACE PATH
					//System.out.println(path.get(i+1)+", "+path.get(i));
					
					//the if statements here will traverse the path array and add moves
					  
					//if next coordinate is to the right of current position, add right to move list
					if(x1+1==(path.get(i+1)))
					{
						x1++;
						//System.out.println("right\n");
						moveList.add(RIGHT);
					}
					
					//if next coordinate is to the left of current position, add left to move list
					else if(x1-1==(path.get(i+1)))
					{
						x1--;
						//System.out.println("left\n");
						moveList.add(LEFT);
					}
					
					//if next coordinate is below current position, add down to move list
					else if(y1+1==(path.get(i)))
					{
						y1++;
						//System.out.println("down\n");
						moveList.add(DOWN);
					}
					
					//if next coordinate is above current position, add up to move list
					else if(y1-1==(path.get(i)))
					{
						y1--;
						//System.out.println("up\n");
						moveList.add(UP);
					}
					
						xtemp=x1;
						ytemp=y1;
				}
				
				flagthink=1;

			}
		}
	}
}