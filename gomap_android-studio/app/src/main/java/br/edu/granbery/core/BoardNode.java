package br.edu.granbery.core;

import java.util.List;

public class BoardNode {

	public Board board;
	public BoardNode boardParent;
	public Piece movedPiece;
	private List<Piece> possibleMoves;
	public int indexPiece;
	public int alpha;
	public int beta;
	public BoardNode next;
	//public Boolean alpha;
	
	public BoardNode(Board board, BoardNode boardParent, List<Piece> possibleMoves, Piece movedPiece, int alpha, int beta){
		this.board=board;
		this.boardParent=boardParent;
		this.possibleMoves=possibleMoves;
		this.indexPiece=0;
		this.movedPiece=movedPiece;
		this.alpha=alpha;
		this.beta=beta;
		this.next=null;
	}
	
	public void calculateHeuristic(){
		if (board.getPlayer() == 0){
			alpha=board.getScore()[0] - board.getScore()[1];
		}
		else{
			beta=board.getScore()[1] - board.getScore()[0];
		}
	}
	
	public Piece getNextPossibleMove(){
		if(indexPiece<possibleMoves.size()){
			indexPiece++;
			return possibleMoves.get(indexPiece-1);
		}
		return null;
	}
	
	public Boolean hasNextPossibleMove(){
		if(indexPiece<possibleMoves.size())
			return true;
		return false;
	}
}
