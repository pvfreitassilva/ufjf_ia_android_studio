package br.edu.ufjf;

import java.util.List;

public class Graph {
	
	public Node nodes[][];
	public Node goal;
	
	public Graph(Board board){
			
		nodes = new Node[Board.GRID_X_SIZE][Board.GRID_Y_SIZE];
		
		for(int i = 0; i < Board.GRID_X_SIZE; i++){
			for(int j = 0; j< Board.GRID_Y_SIZE; j++){
				nodes[i][j]=new Node(i,j, Board.grid[i][j]);
				if(Board.grid[i][j]==Board.GOAL)
					goal = nodes[i][j];
			}
		}
			
		for(int i = 0; i < Board.GRID_X_SIZE; i++){
			for(int j = 0; j< Board.GRID_Y_SIZE; j++){

				if(nodes[i][j].type!=Board.BARRIER) {

					if (i < Board.GRID_X_SIZE - 1) {
						if (Board.grid[i + 1][j] != Board.BARRIER) {
							nodes[i][j].addAdjacency(nodes[i + 1][j], Search.DIREITA);
						}
					}
					if (j < Board.GRID_Y_SIZE - 1) {
						if (Board.grid[i][j + 1] != Board.BARRIER) {
							nodes[i][j].addAdjacency(nodes[i][j + 1], Search.BAIXO);
						}
					}
					if (i > 0) {
						if (Board.grid[i - 1][j] != Board.BARRIER) {
							nodes[i][j].addAdjacency(nodes[i - 1][j], Search.ESQUERDA);
						}
					}
					if (j > 0) {
						if (Board.grid[i][j - 1] != Board.BARRIER) {
							nodes[i][j].addAdjacency(nodes[i][j - 1], Search.CIMA);
						}
					}
				}
			}
		}
	}
	
	List<Node> getAdjacency(int x, int y){
		return nodes[x][y].adjacency;
	}
}
