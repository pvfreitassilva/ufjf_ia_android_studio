package br.edu.ufjf.core;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Point;

public class Board {
	public static int GRID_X_SIZE;
	public static int GRID_Y_SIZE;
	public static final int BARRIER = 1;
	public static final int EMPTY = 0;
	public static final int GOAL = 2;
	
	public static final int PLAYERWIN = 0;
	public static final int GAMEOVER = 1;
	public static final int RESUME = 2;
    public static final int BUILDING_BOARD = 3;
    public static final int BUILDING_MAP = 4;
	public static final int CHANGING_PLAYER_POSITION = 5;
	public static final int CHANGING_GOAL_POSITION = 6;
	public static final int ADDING_NEW_ENEMY = 7;
	
	public static Integer grid[][];
	
	public Player player;
	public List <Enemy> enemies;
	public Graph graph;

    private int movement = 0;
	
	public Board(){

        GRID_X_SIZE = 20;
        GRID_Y_SIZE = 30;

		grid = new Integer[GRID_X_SIZE][GRID_Y_SIZE];

		for(int i = 0; i<GRID_X_SIZE; i++)
			for(int j = 0; j<GRID_Y_SIZE; j++)
				grid[i][j]=0;
		
		player = new Player(new Point(0,0));
		graph = new Graph();
		enemies = new ArrayList<Enemy>();
	}

	public Board(Integer grid[][], Point playerPoint, int grid_x, int grid_y){
        GRID_X_SIZE = grid_x;
        GRID_Y_SIZE = grid_y;
		this.grid=grid;
		this.player= new Player(playerPoint);
		graph = new Graph();
		enemies = new ArrayList<Enemy>();
	}

    public boolean changeGrid(int x, int y){
        if(x<GRID_X_SIZE && y <GRID_Y_SIZE) {
			if (grid[x][y] != GOAL && !player.point.equals(x, y) && !enemies.contains(new Enemy(new Point(x, y), null, null, -1, -1))) {
				grid[x][y] = (grid[x][y] == BARRIER) ? EMPTY : BARRIER;
			}
			else return false;
		}
        return true;
    }
	
	public void makeEnemiesMove(){
		for(Enemy enemy : enemies){
			enemy.makeMove();
		}
	}
	
	public void makePlayerMove(int x, int y){
		
		boolean makeMove=false;
		
		if( x >= player.point.x - 1 &&
			x <= player.point.x + 1 &&
			y >= player.point.y - 1 &&
			y <= player.point.y + 1){
			
			if( x >= player.point.x +1 					&&
				player.point.x < Board.GRID_X_SIZE - 1	&&
				grid[player.point.x+1][player.point.y] != BARRIER){
				player.point.x++;
				makeMove=true;
			}
			else
			if( x <= player.point.x -1 	&&
				player.point.x > 0		&&
				grid[player.point.x-1][player.point.y] != BARRIER){
				player.point.x--;
				makeMove=true;
			}
				
			if( y >= player.point.y +1					&&
				player.point.y < Board.GRID_Y_SIZE - 1	&&
				grid[player.point.x][player.point.y +1] != BARRIER){
				player.point.y++;
				makeMove=true;
			}
			else
			if( y <= player.point.y -1 	&&
				player.point.y > 0		&&
				grid[player.point.x][player.point.y -1] != BARRIER){
				player.point.y--;
				makeMove=true;
			}
		}
		
		if(makeMove){
            movement++;
            System.out.println("---> Movimento "+movement);
            makeEnemiesMove();
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

	public void addEnemy(int x, int y, int searchType, int color){
        if(isValidPosition(x,y))
		    enemies.add(new Enemy(new Point(x,y), player.point, graph, searchType, color) );
	}

	public boolean isValidPosition(int eventX, int eventY) {
		if(eventX<GRID_X_SIZE && eventY <GRID_Y_SIZE)
			if(graph.nodes[eventX][eventY].type!=Board.BARRIER)
				return true;
		return false;
	}

    public void changeGoal(int x, int y){

        if(isValidPosition(x,y)) {

            for (int i = 0; i < GRID_X_SIZE; i++)
                for (int j = 0; j < GRID_Y_SIZE; j++)
                    if (grid[i][j] == GOAL)
                        grid[i][j] = EMPTY;

            grid[x][y] = GOAL;

            graph.rebuildGraph();
        }
    }

	public static Integer[][] getGrid() {
		return grid;
	}

	public void removeEnemis() {
		enemies.clear();
	}
}
