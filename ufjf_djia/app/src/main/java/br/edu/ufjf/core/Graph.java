package br.edu.ufjf.core;

import android.graphics.Point;

public class Graph {
	
	public GraphNode nodes[][];
	public GraphNode goal;
	
	public Graph(){
        rebuildGraph();
	}

    public void rebuildGraph(){

        nodes = new GraphNode[Board.GRID_X_SIZE][Board.GRID_Y_SIZE];

        for(int i = 0; i < Board.GRID_X_SIZE; i++){
            for(int j = 0; j< Board.GRID_Y_SIZE; j++){
                nodes[i][j]=new GraphNode(new Point(i,j) , Board.grid[i][j]);
                if(Board.grid[i][j]==Board.GOAL)
                    goal = nodes[i][j];
            }
        }

        for(int i = 0; i < Board.GRID_X_SIZE; i++){
            for(int j = 0; j< Board.GRID_Y_SIZE; j++){

                if(nodes[i][j].type!=Board.BARRIER) {
                    if (i < Board.GRID_X_SIZE - 1) {
                        if (Board.grid[i + 1][j] != Board.BARRIER) {
                            nodes[i][j].addAdjacency(nodes[i + 1][j]);
                        }
                    }
                    if (j < Board.GRID_Y_SIZE - 1) {
                        if (Board.grid[i][j + 1] != Board.BARRIER) {
                            nodes[i][j].addAdjacency(nodes[i][j + 1]);
                        }
                    }
                    if (i > 0) {
                        if (Board.grid[i - 1][j] != Board.BARRIER) {
                            nodes[i][j].addAdjacency(nodes[i - 1][j]);
                        }
                    }
                    if (j > 0) {
                        if (Board.grid[i][j - 1] != Board.BARRIER) {
                            nodes[i][j].addAdjacency(nodes[i][j - 1]);
                        }
                    }
                }
            }
        }
    }

	public GraphNode getNode(Point p){
		return nodes[p.x][p.y];
	}

}
