package br.edu.granbery.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Point;

public class GraphBuilder {
    
    private int gridSize;
    private int boardSize;
    private int[][] controlGrid;
    private Random rand;
    
    public GraphBuilder(int gridSize, int boardSize) {
        this.gridSize = gridSize;
        this.boardSize = boardSize;
        rand = new Random();
    }
    
    public Graph build() {
        int maxNodes = getMaxNodes();
        Graph graph = new Graph(maxNodes);
        
        initControlGrid();
        
        int minSize = boardSize - (boardSize / 2);
        if (minSize < 2) minSize = 2;
        //int maxSize = boardSize + (boardSize / 2);
                
        for (int i=0;i<maxNodes;i++) {
            graph.nodes[i] = generatePiece(minSize, i);
        }
        //int k = 0;
        
        /*
         * Minha ideia: percorrer a grid procurando por espaços vazios,
         * e pegar aleatoriamente um espaço ao redor, que se não for vazio,
         * terá um ID de uma piece, então é só atribuir o espaço vazio
         * a piece adjacente
         */
        
        while (hasEmptySquare()) {
            int i = rand.nextInt(maxNodes);
            //if (graph.nodes[i].size() < maxSize) {
                List<Point> adjacency = getPieceAdjacency(graph.nodes[i]);
                if (!adjacency.isEmpty()) {
                    int random = rand.nextInt(adjacency.size());
                    Point point = adjacency.get(random);
                    graph.nodes[i].add(point);
                    controlGrid[point.x][point.y] = i;
                }                
            //}
            //if (k++ > 10000) break;
        }
        
        GraphBuilder gp = new GraphBuilder(this.gridSize, this.boardSize);
		gp.initControlGrid();
		
        for (Piece p : graph.nodes) {
			List<Point> adj = gp.getPieceAdjacency(p);
			for (Point point : adj) {
				Piece npiece = graph.getPiece(point);
				if (p != null && p != npiece) {
					p.addAdjacency(npiece);
				}
			}
        }
        //TODO
        //remove
        //print();
        
        graph.setControlGrid(controlGrid);
        
        return graph;
    }
    
    private Piece generatePiece(int minSize, int id) {
        Piece p = new Piece(id);
        
        do {
            setStartPoint(p);
            
            for (int i = 0; i < minSize-1 ; i++ ){
                List<Point> adjacency = getPieceAdjacency(p);
                if (!adjacency.isEmpty()) {
                    int random = rand.nextInt(adjacency.size());
                    Point point = adjacency.get(random);
                    p.add(point);
                    controlGrid[point.x][point.y] = id;
                }
            }
            
            if (p.size() != minSize) {
                free(p);
            }
        } while (p.size() == 0);
        
        return p;
    }
    
    private void setStartPoint(Piece p) {
        int x;
        int y;
        do {
            x = rand.nextInt(gridSize);
            y = rand.nextInt(gridSize);
        } while (controlGrid[x][y] != -1);
        p.add(x, y);
        controlGrid[x][y] = p.getId();
    }
    
    public List<Point> getPieceAdjacency(Piece piece) {
        List<Point> adjacency = new ArrayList<Point>();
        for (Point p : piece.coordinates) {
            for (int i = p.x - 1 ; i < p.x + 2 ; i ++ ) {
                for (int j = p.y - 1 ; j < p.y + 2 ; j ++) {
                    if (p.x == i || p.y == j) { // Avoid diagonal
                        if (i >= 0 && i < gridSize && j >=0 && j < gridSize) { // Valid position on matrix
                            if (controlGrid[i][j] == -1) { // Position is free
                                Point adjacentPoint = new Point(i, j);
                                if (!adjacency.contains(adjacentPoint)) {
                                    adjacency.add(adjacentPoint);
                                }
                            }
                        }
                    }
                }
            }
        }
        return adjacency;
    }
    
    private void free(Piece piece) {
        for (Point p : piece.coordinates) {
            controlGrid[p.x][p.y] = -1;
        }
        piece.clear();
    }
    
    private int getMaxNodes() {
        int squareNumber = gridSize * gridSize;
        int nodes = squareNumber / boardSize;
        return nodes;
    }    
    
    public void initControlGrid() {
        controlGrid = new int[gridSize][gridSize];
        for (int i=0;i<gridSize;i++) {
            for (int j=0;j<gridSize;j++) {
                controlGrid[i][j] = -1;
            }
        }
    }
    
    /*
    private void print() {
        for (int i=0;i<gridSize;i++){
            for (int j=0;j<gridSize;j++) {
                String str;
                str = String.valueOf(controlGrid[i][j]);
                if (str.equals("-1")) str = "   ";                
                if (str.length() == 1) str = "00" + str;
                if (str.length() == 2) str = "0" + str;
                System.out.print("[" + str + "]");
            }
            System.out.println();
        }        
    }
    */
    
    private boolean hasEmptySquare() {
        for (int i=0;i<gridSize;i++) {
            for (int j=0;j<gridSize;j++) {
                if (controlGrid[i][j] == -1) {
                    return true;
                }
            }
        }        
        return false;
    }

}

