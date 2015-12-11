package br.edu.granbery.gomap;

import java.io.Serializable;
import br.edu.granbery.core.Board;

public class GameInstance implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5572839049422398275L;
	public Board board;
	public int mode;
	public int boardSize;
	
	public GameInstance(Board board, int mode, int boardSize){
		this.board = board.clone();
		this.mode = mode;
		this.boardSize = boardSize;
	}
	
}
