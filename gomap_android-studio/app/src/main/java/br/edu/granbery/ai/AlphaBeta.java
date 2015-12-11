package br.edu.granbery.ai;

import java.util.List;

import android.util.Log;
import br.edu.granbery.core.Board;
import br.edu.granbery.core.BoardNode;
import br.edu.granbery.core.BoardStack;
import br.edu.granbery.core.Piece;

public class AlphaBeta {

	private Piece bestMove;
	//private Piece bestMoveStack;
	private int depth;
	//private int cutsStack, cutsRecursion;
	//private int recursions, stacking;
	//Thread threadStack, threadRecursion;
	//Board boardInit;
	
	//private BoardStack bsAux;

	public AlphaBeta() {
		bestMove = null;
		//bestMoveStack=null;
		//cutsStack = cutsRecursion = recursions = stacking = 0;
		
		//threadStack = new Thread();
		//threadRecursion = new Thread();
		
		//bsAux=new BoardStack();
		
	}

	public AlphaBeta(int depth) {
		this.depth = depth;
		bestMove = null;
		//bestMoveStack=null;
		//cutsStack = cutsRecursion = recursions = stacking = 0;

		//threadStack = new Thread(calculateWithStack);
		//threadRecursion = new Thread(calculateWithRecursion);

		//bsAux=new BoardStack();
		
	}
	
	/*
	Runnable calculateWithStack = new Runnable() {
		public void run() {
			long timeInit = System.currentTimeMillis();
			calculateWithStack(boardInit, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
			Log.d("Testando", "Tempo pilha: "+(System.currentTimeMillis()-timeInit));
		}
	};
	*/
	
	/*Runnable calculateWithRecursion = new Runnable() {
		public void run() {
			long timeInit = System.currentTimeMillis();
			calculate(boardInit, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
			Log.d("Testando", "Tempo recurs�o: "+(System.currentTimeMillis()-timeInit));
		}
	};*/

	//AlphaBeta using stack
	/*
	private int calculateWithStack(Board board, int depth, int alpha, int beta) {
		BoardStack boardStack = new BoardStack();
		BoardNode boardNode = new BoardNode(board, null, board.getGameGraph().getPossibleMoves(), null, alpha, beta);
		boardStack.add(boardNode);
		Board boardTemp;
		Piece piece;
		Boolean unstacked = false;

		while (!boardStack.isEmpty()) {
			boardNode = boardStack.getTop();

			if (depth == 0 || boardNode.board.isGameOver()) {
				boardNode.calculateHeuristic();
				boardStack.removeTop();
				unstacked = true;
				depth++;
				
				bsAux.add(boardNode);
				
			} else if(boardNode.alpha < boardNode.beta && boardNode.hasNextPossibleMove()){
				boardTemp = boardNode.board.clone();
				piece = boardNode.getNextPossibleMove();
				boardTemp.makeMove(piece);
				boardStack.add(new BoardNode(boardTemp, boardNode,boardTemp.getGameGraph().getPossibleMoves(), piece, boardNode.alpha, boardNode.beta));
				stacking++;
				depth--;				
			} else {
				boardStack.removeTop();
				unstacked = true;
				depth++;
				if(boardNode.alpha >= boardNode.beta)
					cutsStack++;
				
				bsAux.add(boardNode);
				
			}
			
			if (boardNode.boardParent != null && unstacked) {
				unstacked = false;
				// ALPHA
				if (boardNode.boardParent.board.getPlayer() == 0) {
					if (boardNode.beta > boardNode.boardParent.alpha) {
						boardNode.boardParent.alpha = boardNode.beta;
						if (depth == this.depth) {
							bestMoveStack = boardNode.movedPiece;
						}
					}
				}
				// BETA
				else {
					if (boardNode.alpha < boardNode.boardParent.beta) {
						boardNode.boardParent.beta = boardNode.alpha;
						if (depth == this.depth) {
							bestMoveStack = boardNode.movedPiece;
						}
					}
				}
			}
		}

		return 0;
	}
	*/

	private int calculateWithRecursion(Board board, int depth, int alpha, int beta) {
		if (depth == 0 || board.isGameOver()) {
			if (board.getPlayer() == 0)
				return board.getScore()[0] - board.getScore()[1];
			else
				return board.getScore()[1] - board.getScore()[0];
		} else {
			List<Piece> possibleMoves = board.getGameGraph().getPossibleMoves();

			/* TODO VER PORQUE PROFUNDIDADE IMPAR INVERTE A SOLUCAO DO ALPHA BETA
			 */

			if (board.getPlayer() == 0) { // Alpha
				for (Piece piece : possibleMoves) {
					Board tempBoard = board.clone();
					tempBoard.makeMove(piece);
					int result = calculateWithRecursion(tempBoard, depth - 1, alpha, beta);
					//recursions++;
					if (result > alpha) {

						alpha = result;

						if (depth == this.depth) {
							bestMove = piece;
						}
					}
					if (alpha >= beta) {
						//cutsRecursion++;
						break;
					}
				}
				return alpha;
			} else { // Beta
				for (Piece piece : possibleMoves) {
					Board tempBoard = board.clone();
					tempBoard.makeMove(piece);
					int result = calculateWithRecursion(tempBoard, depth - 1, alpha, beta);
					//recursions++;
					if (result < beta) {

						beta = result;

						if (depth == this.depth) {
							bestMove = piece;
						}
					}

					if (alpha >= beta) {
						//cutsRecursion++;
						break;
					}
				}
				return beta;
			}

		}
	}

	public Piece getBestMove(Board board) {

		//boardInit = board;
		
		//threadRecursion.start();
		//threadStack.start();
		
		//long time1 = System.currentTimeMillis();

		calculateWithRecursion(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		//Log.d("Testando","Tempo pilha: "+(System.currentTimeMillis()-time1));
		
		//while(threadStack.isAlive() || threadRecursion.isAlive());
		
		//while(threadRecursion.isAlive());
		
		/*
		long timeInit = System.currentTimeMillis();

		calculate(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		Log.d("Testando","Recurs�o terminada");

		long time1 = System.currentTimeMillis();

		calculateWithStack(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		Log.d("Testando","Pilha terminada");
		
		long time2 = System.currentTimeMillis();
		
		//bsAux=null;
		*/
		
		/*
		if (bestMoveStack == null)
			Log.d("Testando", "bestMoveStack veio nulo!");
		if (bestMove == null)
			Log.d("Testando", "bestMove veio nulo!");
		if (bestMove != null && bestMove.equals(bestMoveStack)) {
			Log.d("Testando", "Calculou igual!");
		} else
			Log.d("Testando", "Calculou diferente!");

		Log.d("Testando", "Cortes pilha: " + cutsStack + " Cortes recurs�o: "
				+ cutsRecursion);
		Log.d("Testando", "Empilhamentos: " + stacking + " Recurs�es: "
				+ recursions);
				*/
		
		/*
		Log.d("Testando", "Tempo pilha: " + (time2 - time1)
				+ " Tempo recurs�o: " + (time1 - timeInit));

		*/
		
		return bestMove;

		/*
		 * Piece bestMove = null; List<Piece> pieces =
		 * board.getGameGraph().getPossibleMoves(); if (!pieces.isEmpty()) {
		 * Random rand = new Random(); bestMove =
		 * pieces.get(rand.nextInt(pieces.size())); }
		 */
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

}
