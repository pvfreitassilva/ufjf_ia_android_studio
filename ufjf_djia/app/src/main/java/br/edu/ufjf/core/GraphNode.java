package br.edu.ufjf.core;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

public class GraphNode{

    public Point point;
	public int type;
	public List<GraphNode> adjacency;

	public GraphNode (Point point, int type){

		this.point = point;
		this.type = type;
		adjacency = new ArrayList<GraphNode>();
	}

	public void addAdjacency(GraphNode n){
		adjacency.add(n);
	}

	//todo remove
	@Override
	public String toString(){
		return "x: "+point.x+" , y: "+point.y;
	}
}
