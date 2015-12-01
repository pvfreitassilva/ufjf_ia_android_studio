package br.edu.granbery.core;

public class BoardStack {
	private BoardNode top;
	
	public BoardStack(){
		top=null;
	}
	
	public BoardNode getTop(){
		return top;
	}
	
	public void add(BoardNode bn){
		bn.next=top;
		top=bn;
	}
	
	public void removeTop(){
		BoardNode temp=top.next;
		
		top.next=null;
		top=null;
		
		top=temp;
	}
	
	public Boolean isEmpty(){
		if(top==null)
			return true;
		return false;					
	}
	
	
}
