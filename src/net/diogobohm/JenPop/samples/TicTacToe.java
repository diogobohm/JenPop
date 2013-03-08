/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.diogobohm.JenPop.samples;

import java.util.ArrayList;
import net.diogobohm.JenPop.GeneticNode;

/**
 * TODO: This class has no description yet!
 * @author diogo
 */
public class TicTacToe extends GeneticNode{
	public enum TicTacToeState {
		BLANK, O, X;
		
		public String getString() {
			return this == BLANK ? " " : this == X ? "X" : "O";
		}
		
		public TicTacToeState getOpposite() {
			return this == X? O : this == O? X : BLANK;
		}
	}
	
	private TicTacToeState[][] board;
	private TicTacToeState myMarker;
	private boolean myTurn;
	
	public TicTacToe(TicTacToeState[][] board, boolean amIX) {
		this.board = new TicTacToeState[3][3];
		if (board.length == 3 && board[0].length == 3) {
			copyBoard(board, this.board);
			this.myMarker = amIX ? TicTacToeState.X : TicTacToeState.O;
			this.myTurn = this.myMarker == getCurrentTurn();
		}
	}
	
	public TicTacToeState[][] getBoard() {
		return board;
	}
	
	public void copyBoard(TicTacToeState[][] srcBoard, TicTacToeState[][] dstBoard) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				dstBoard[x][y] = srcBoard[x][y];
			}
		}
	}
	
	public TicTacToeState getCurrentTurn() {
		int count = 0;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (board[x][y] == TicTacToeState.BLANK) {
					count++;
				}
			}
		}
		return (count % 2) == 0 ? TicTacToeState.X : TicTacToeState.O;
	}
	
	public boolean isMyTurn() {
		return myTurn;
	}
	
	@Override
	public long getScore() {
		long score = 0;
		
		//Get lines' score
		score += getLineScore(board[0][0], board[0][1], board[0][2]);
		score += getLineScore(board[1][0], board[1][1], board[1][2]);
		score += getLineScore(board[2][0], board[2][1], board[2][2]);
		//Get columns' score
		score += getLineScore(board[0][0], board[1][0], board[2][0]);
		score += getLineScore(board[0][1], board[1][1], board[2][1]);
		score += getLineScore(board[0][2], board[1][2], board[2][2]);
		//Get diagonals' score
		score += getLineScore(board[0][0], board[1][1], board[2][2]);
		score += getLineScore(board[0][2], board[1][1], board[2][0]);
		
		return score;
	}
	
	public boolean hasWinningCondition() {
		
		return  //Get lines' score
				(board[0][0] == board[0][1] && board[0][1] == board[0][2] && board[0][1] != TicTacToeState.BLANK) ||
				(board[1][0] == board[1][1] && board[1][1] == board[1][2] && board[1][1] != TicTacToeState.BLANK) ||
				(board[2][0] == board[2][1] && board[2][1] == board[2][2] && board[2][1] != TicTacToeState.BLANK) ||
				//Get columns' score
				(board[0][0] == board[1][0] && board[1][0] == board[2][0] && board[1][0] != TicTacToeState.BLANK) ||
				(board[0][1] == board[1][1] && board[1][1] == board[2][1] && board[1][1] != TicTacToeState.BLANK) ||
				(board[0][2] == board[1][2] && board[1][2] == board[2][2] && board[1][2] != TicTacToeState.BLANK) ||
				//Get diagonals' score
				(board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[1][1] != TicTacToeState.BLANK) ||
				(board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[1][1] != TicTacToeState.BLANK) ||
				//Get whether everybody's filled:
				(
					board[0][0] != TicTacToeState.BLANK && board[0][1] != TicTacToeState.BLANK && board[0][2] != TicTacToeState.BLANK &&
					board[1][0] != TicTacToeState.BLANK && board[1][1] != TicTacToeState.BLANK && board[1][2] != TicTacToeState.BLANK &&
					board[2][0] != TicTacToeState.BLANK && board[2][1] != TicTacToeState.BLANK && board[2][2] != TicTacToeState.BLANK
				);
	}
	
	private long getLineScore(TicTacToeState a, TicTacToeState b, TicTacToeState c) {
		long lineScore = 0;
		
		//Check winning candidate
		if ((a == myMarker && b == myMarker && c == TicTacToeState.BLANK) ||
				(a == myMarker && c == myMarker && b == TicTacToeState.BLANK) ||
				(b == myMarker && c == myMarker && a == TicTacToeState.BLANK)) {
			lineScore += 100;
		//Check losing condition
		} else if ((a == myMarker.getOpposite() && b == myMarker.getOpposite() && c == TicTacToeState.BLANK) ||
				(a == myMarker.getOpposite() && c == myMarker.getOpposite() && b == TicTacToeState.BLANK) ||
				(b == myMarker.getOpposite() && c == myMarker.getOpposite() && a == TicTacToeState.BLANK)){
			lineScore -= 200;
		}
		
		lineScore += a == myMarker? 5 : a == TicTacToeState.BLANK ? 0 : -5;
		lineScore += b == myMarker? 5 : b == TicTacToeState.BLANK ? 0 : -5;
		lineScore += c == myMarker? 5 : c == TicTacToeState.BLANK ? 0 : -5;
		
		return lineScore;
	}

	@Override
	public ArrayList<GeneticNode> createGeneration() {
		TicTacToeState marker = getCurrentTurn();
		ArrayList<GeneticNode> children = new ArrayList<GeneticNode>();
		
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				if (board[x][y] == TicTacToeState.BLANK) {
					TicTacToeState[][] newBoard = new TicTacToeState[3][3];
					copyBoard(board, newBoard);
					newBoard[x][y] = marker;
					children.add(new TicTacToe(newBoard, marker == TicTacToeState.X));
				}
			}
		}
		
		return children;
	}

	@Override
	public GeneticNode createChild() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public void printBoard(){
		for (int x = 0; x < 3; x++) {
			System.out.println(
					board[x][0].getString() + "|" +
					board[x][1].getString() + "|" +
					board[x][2].getString());
		}
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		TicTacToeState[][] initialBoard = {
			{ TicTacToeState.BLANK, TicTacToeState.BLANK, TicTacToeState.BLANK },
			{ TicTacToeState.BLANK, TicTacToeState.BLANK, TicTacToeState.BLANK },
			{ TicTacToeState.BLANK, TicTacToeState.BLANK, TicTacToeState.BLANK }
		};

		TicTacToe game = new TicTacToe(initialBoard, false);
		
		int turn = 0;
		while (!game.hasWinningCondition()) {
			
			System.out.println("Turn "+String.valueOf(turn++));
			game = (TicTacToe) game.getBestChild();
			game.printBoard();
		}
	}

}
