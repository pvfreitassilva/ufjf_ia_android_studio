package br.edu.granbery.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.util.Log;

public class Piece implements Cloneable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7030399949570079651L;
	public transient List<Point> coordinates;
    private int id;
    //private int value;
    public transient List<Piece> adjacency;
    private int adjacencyIds[];
    private int coordinatesX[];
    private int coordinatesY[];
    private int coordinatesSize;
    private int adjacencySize;
    
    public Piece() {
        coordinates = new ArrayList<Point>();
        adjacency = new ArrayList<Piece>();
        adjacencyIds= null;
        coordinatesX=null;
        coordinatesY=null;
        //value = -1;
    }
    
    public Piece(int id) {
        coordinates = new ArrayList<Point>();
        adjacency = new ArrayList<Piece>();
        adjacencyIds=null;
        //value = -1;
        this.id = id;
    }
    
    public int size() {
        return coordinates.size();
    }
    
    public void clear() {
        coordinates.clear();
    }

    public void add(int x, int y) {
        coordinates.add(new Point(x, y));
    }
    
    public void add(Point p) {
        coordinates.add(p);
    }
    
    public void remove(int x, int y) {
        Point p = new Point(x, y);
        coordinates.remove(p);
    }
    
    public void remove(Point p) {
        coordinates.remove(p);
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    /*public void setValue(int value) {
        this.value = value;
    }*/
    
    /*public int getValue() {
        return value;
    }*/
    
    public void addAdjacency(Piece p) {
    	if (!this.adjacency.contains(p)) {
        	this.adjacency.add(p);
        	p.addAdjacency(this);
    	}
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o == this) return true;
    	if (o == null) return false;
    	if (o instanceof Piece) {
    		Piece castObj;
    		castObj = (Piece)o;
    		if (this.id == castObj.id) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    	else {
    		return false;
    	}
    }
    
    
    public void saveAdjacencyIds(){
    	coordinatesSize = coordinates.size();
    	adjacencySize = adjacency.size();
    	
    	adjacencyIds = new int[adjacencySize];
    	coordinatesX = new int[coordinatesSize];
    	coordinatesY = new int[coordinatesSize];
    	int i = 0;
    	for(Piece piece : adjacency){
    		adjacencyIds[i]=piece.getId();
    		i++;
    	}
    	i=0;
    	for(Point point : coordinates){
    		coordinatesX[i]=point.x;
    		coordinatesY[i]=point.y;
    		i++;
    	}
    	adjacency = null;
    	coordinates = null;
    }
    
    public void restoreAdjacency(Piece nodes[], int maxNodes){
    	adjacency = new ArrayList<Piece>();
    	coordinates = new ArrayList<Point>();    	
    	
    	for(int i = 0; i < maxNodes; i++){
    		for(int j = 0; j < adjacencySize; j++){
    		if(nodes[i].getId() == adjacencyIds[j])
    			adjacency.add(nodes[i]);
    		}
    	}
    	
    	for(int i = 0; i < coordinatesSize; i++){
    		coordinates.add(new Point(coordinatesX[i], coordinatesY[i]));
    	}
    	
    	adjacencyIds = null;
    	coordinatesX = null;
    	coordinatesY = null;
    }
	
    /*@Override
    public Piece clone() {
    	Piece clone = null;
		try {
			clone = (Piece) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return clone;
    }*/
    
}

