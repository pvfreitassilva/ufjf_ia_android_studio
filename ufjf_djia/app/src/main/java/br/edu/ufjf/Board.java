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
	
	public Player player;
	public List <Enemy> enemies;
	//public Point goal;
	public Graph graph;
	
	public Board(){
		
		player = new Player(new Point(0,0));

		graph = new Graph(this);
		
		enemies = new ArrayList<Enemy>();
		
		enemies.add(new Enemy( new Point(Board.GRID_X_SIZE-1, Board.GRID_Y_SIZE-1), player.point, graph, Search.AESTRELA));
		enemies.add(new Enemy( new Point(0, Board.GRID_Y_SIZE-1), player.point, graph, Search.GULOSA));
	}
	
	public void makeEnemiesMove(Graph graph){
		
		for(Enemy enemy : enemies){
			enemy.makeMove();
		}
	}
	
	public void makePlayerMove(int x, int y, Graph graph){
		
		boolean makeMove=false;
		
		if( x >= player.point.x - 1 &&
			x <= player.point.x + 1 &&
			y >= player.point.y - 1 &&
			y <= player.point.y + 1){
			
			if( x == player.point.x +1 					&&
				player.point.x < Board.GRID_X_SIZE - 1	&&
				Board.grid[player.point.x+1][player.point.y] != BARRIER){
				player.point.x++;
				makeMove=true;
			}
			else
			if( x == player.point.x -1 	&&
				player.point.x > 0		&&
				Board.grid[player.point.x-1][player.point.y] != BARRIER){
				player.point.x--;
				makeMove=true;
			}
				
			if( y == player.point.y +1					&&
				player.point.y < Board.GRID_Y_SIZE - 1	&&
				Board.grid[player.point.x][player.point.y +1] != BARRIER){
				player.point.y++;
				makeMove=true;
			}
			else
			if( y == player.point.y -1 	&&
				player.point.y > 0		&&
				Board.grid[player.point.x][player.point.y -1] != BARRIER){
				player.point.y--;
				makeMove=true;
			}
		}
		
		if(makeMove){
			makeEnemiesMove(graph);
		}
		
	}
	
	public int getGameState(){
		
		if( grid[player.point.x][player.point.y] == GOAL )
			return PLAYERWIN;
		else{
			
			for(Enemy e : enemies){
				if(	(e.point.x == player.point.x   && e.point.y == player.point.y  ) ||
					(e.point.x == player.point.x+1 && e.point.y == player.point.y  ) ||
					(e.point.x == player.point.x   && e.point.y == player.point.y+1) ||
					(e.point.x == player.point.x-1 && e.point.y == player.point.y  ) ||
					(e.point.x == player.point.x   && e.point.y == player.point.y-1)){
					return GAMEOVER;
				}
			}
		}
		
		return RESUME;
	}
}
