package br.edu.granbery.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

public class Graph implements Cloneable, Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 665405980830082090L;
	public Piece[] nodes;
    public int[] value;
    private int[][] controlGrid;
    private int maxNodes;
    
    public Graph(int maxNodes) {
        nodes = new Piece[maxNodes];
        value = new int[maxNodes];
        this.maxNodes=maxNodes;
        for (int i=0;i<maxNodes;i++) 
        	value[i] = -1;
    }
    
    public void setControlGrid(int[][] controlGrid) {
    	this.controlGrid = controlGrid;
    }
    
    public int[][] getControlGrid() {
    	return controlGrid;
    }
    
    public Piece getPiece(int x, int y) {
    	Point point = new Point(x, y);
    	return getPiece(point);
    }
        
    public Piece getPiece(Point point) {
    	for (Piece piece : nodes) {
    		if (piece.coordinates.contains(point)) {
    			return piece;
    		}
    	}    	
    	return null;
    }
    
    public List<Piece> getPossibleMoves() {
    	List<Piece> possibleMoves = new ArrayList<Piece>();
    	for (Piece p : nodes) {
    		if (value[p.getId()] == -1) possibleMoves.add(p);
    	}
    	return possibleMoves;
    }
    
    @Override
    public Graph clone() {
    	try {
			Graph clone = (Graph) super.clone();
    		
    		int newValue[] = new int[value.length];
    		for (int i=0;i<nodes.length;i++){
    			newValue[i] = value[i];
    		}
    		
    		clone.value = newValue;
    		
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
    }

	public void prepareSerialization() {
		for(int i = 0; i < maxNodes; i++)
			nodes[i].saveAdjacencyIds();
	}
	
	public void restoreGraph() {
		for(int i = 0; i < maxNodes; i++)
			nodes[i].restoreAdjacency(nodes, maxNodes);;
	}
}
