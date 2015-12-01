package br.edu.ufjf;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Node implements Comparable{
	
	public int x, y;
	public int type;
	public List<Node> adjacency;
	public Node dad;
	public int evaluation;
	public int cost;
	
	public Node (int x, int y, int type){
		
		this.x = x;
		this.y = y;
		this.type = type;
		adjacency = new ArrayList<Node>();
		//adjacency[0]=adjacency[1]=adjacency[2]=adjacency[3]=null;
		cost = 0;
		evaluation = 0;
		
	}
	
	public void addAdjacency(Node n, int relation){
		//if(relation>=0 && relation <4)
		//	adjacency[relation]=n;
		adjacency.add(n);
	}

	@Override
	public Node clone(){
		Node clone = new Node(x,y,type);
		clone.adjacency = this.adjacency;

		return clone;
	}


	@Override
	public int compareTo(Object another) {

		Node n = (Node) another;

		if(n.x == x && n.y == y)
			return 0;
		return -1;
	}

	@Override
	public String toString(){

		return "x: "+x+" , y: "+y;
	}
}
