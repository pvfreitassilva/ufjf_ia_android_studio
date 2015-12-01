package br.edu.ufjf;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

public class Board {
	public static final int GRID_X_SIZE = 20;
	public static final int GRID_Y_SIZE = 12;
	public static final int BARRIER = 1;
	public static final int EMPTY = 0;
	public static final int GOAL = 2;
	
	public static final int PLAYERWIN = 0;
	public static final int GAMEOVER = 1;
	public static final int RESUME = 2;
	
	public static int grid[][] ={
			/*{0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0},
			{0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0},
			{0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0},
			{0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0},
			{1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0},
			{0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 0},
			{1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0},
			{0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0},
			{1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1},
			{1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1},
			{1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1},
			{1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 2, 1}};*/
			
			{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},
			{0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0},
			{0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
			{0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0},
			{0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0},
			{1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1},
			{0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0},
			{0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
			{0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1},
			{0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
			{0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0},
			{0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0},
			{0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0},
			{0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1},
			{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
			{1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0},
			{1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
			{1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0},
			{1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0},
			{1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0},
			{1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0},
			{1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0}};
	
	public Point player;
	public List <Enemy> enemies;
	//public Point goal;
	public Graph graph;
	
	public Board(){
		
		player = new Point(0,0);

		graph = new Graph(this);
		
		enemies = new ArrayList<Enemy>();		
		
		enemies.add(new Enemy(Board.GRID_X_SIZE-1, Board.GRID_Y_SIZE-1, Search.GULOSA));
		enemies.add(new Enemy(0, Board.GRID_Y_SIZE-1, Search.GULOSA));
		//Random r = new Random();
		//grid = new int[GRID_SIZE*2][GRID_SIZE];		
		
		/*for (int i=0;i<GRID_X_SIZE ;i++){
            for (int j=0;j<GRID_Y_SIZE ;j++) {
            	if(r.nextInt(4)==3)
                //grid[i][j] =  r.nextInt(2);      
            		grid[i][j] = 1;
            	else
            		grid[i][j] = 0;
            }
        }*/
        
	}
	
	public void makeEnemiesMove(Graph graph){
		
		for(Enemy enemy : enemies){
			enemy.makeMove(graph, player);
		}
	}
	
	public void makePlayerMove(int x, int y, Graph graph){
		
		boolean makeMove=false;
		
		if( x >= player.x - 1 &&
			x <= player.x + 1 &&
			y >= player.y - 1 &&
			y <= player.y + 1){
			
			if( x == player.x +1 					&&
				player.x < Board.GRID_X_SIZE - 1	&&
				Board.grid[player.x+1][player.y] != BARRIER){
				player.x++;
				makeMove=true;
			}
			else
			if( x == player.x -1 	&&
				player.x > 0		&&
				Board.grid[player.x-1][player.y] != BARRIER){
				player.x--;
				makeMove=true;
			}
				
			if( y == player.y +1					&&
				player.y < Board.GRID_Y_SIZE - 1	&&
				Board.grid[player.x][player.y +1] != BARRIER){
				player.y++;
				makeMove=true;
			}
			else
			if( y == player.y -1 	&&
				player.y > 0		&&
				Board.grid[player.x][player.y -1] != BARRIER){
				player.y--;
				makeMove=true;
			}
		}
		
		if(makeMove){
			makeEnemiesMove(graph);
		}
		
	}
	
	public int getGameState(){
		
		if( grid[player.x][player.y] == GOAL )
			return PLAYERWIN;
		else{
			
			for(Enemy e : enemies){
				if(	(e.x == player.x   && e.y == player.y  ) ||
					(e.x == player.x+1 && e.y == player.y  ) ||
					(e.x == player.x   && e.y == player.y+1) ||
					(e.x == player.x-1 && e.y == player.y  ) ||
					(e.x == player.x   && e.y == player.y-1)){
					return GAMEOVER;
				}
			}
		}
		
		return RESUME;
	}
}
