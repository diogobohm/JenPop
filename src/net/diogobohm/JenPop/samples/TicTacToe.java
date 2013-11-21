/**
 * JenPop - Java Genetic Population Framework Copyright (C) 2013 Diogo Luiz Böhm
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.diogobohm.JenPop.samples;

import java.util.ArrayList;
import net.diogobohm.JenPop.GeneticNode;

/**
 * Plays an automatic Tic Tac Toe match between JenPop players.
 */
public class TicTacToe extends GeneticNode {

    public enum TicTacToeState {
        BLANK, O, X;

        public String getString() {
            return this == BLANK ? " " : this == X ? "X" : "O";
        }

        public TicTacToeState getOpposite() {
            return this == X ? O : this == O ? X : BLANK;
        }
    }
    private TicTacToeState[][] board;
    private TicTacToeState cpuMarker;
    private TicTacToeState nextPlayer;
    private long score;

    public TicTacToe(TicTacToeState[][] board, TicTacToeState cpuMarker) {
        this.board = new TicTacToeState[3][3];
        copyBoard(board, this.board);
        this.nextPlayer = getNextTurn();
        this.cpuMarker = cpuMarker;
        this.score = calculateScore();
    }

    public TicTacToeState[][] getBoard() {
        return board;
    }

    public final void copyBoard(TicTacToeState[][] srcBoard, TicTacToeState[][] dstBoard) {
        for (int x = 0; x < 3; x++) {
            System.arraycopy(srcBoard[x], 0, dstBoard[x], 0, 3);
        }
    }

    public final TicTacToeState getNextTurn() {
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
        return this.cpuMarker == getNextTurn();
    }
    
    /*
    static int[][] Three_in_a_Row = {
        { 0, 1, 2 },
        { 3, 4, 5 },
        { 6, 7, 8 },
        { 0, 3, 6 },
        { 1, 4, 7 },
        { 2, 5, 8 },
        { 0, 4, 8 },
        { 2, 4, 6 }
    };
    static int[][] Heuristic_Array = {
        {     0,   -10,  -100, -1000 },
        {    10,     0,     0,     0 },
        {   100,     0,     0,     0 },
        {  1000,     0,     0,     0 }
    };

    @Override
    public long getScore(){
        TicTacToeState piece = null, opponent = myMarker.getOpposite();
        int index = 0;
        int players, others, t = 0, i, j;
        for (i = 0; i < 8; i++) {
            players = others = 0;
            for (j = 0; j < 3; j++) {
                index = Three_in_a_Row[i][j];
                piece = board[index%3][index/3];
                if (piece == myMarker) {
                    players++;
                } else if (piece == opponent) {
                    others++;
                }
            }
            t += Heuristic_Array[players][others];
        }
        return t;
    }*/
    
    public long calculateScore() {
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
        
        //printBoard();
        //System.out.println("Score - "+score);

        return score;
    }
    
    @Override
    public long getScore() {
        return score;
    }
    
    @Override
    public long promoteScore(int grade) {
        return getScore()+(grade*10);
    }

    @Override
    public long punishScore(int grade) {
        return getScore()-(grade*10);
    }

    public boolean hasWinningCondition() {

        return //Get lines' score
                (board[0][0] == board[0][1] && board[0][1] == board[0][2] && board[0][1] != TicTacToeState.BLANK)
                || (board[1][0] == board[1][1] && board[1][1] == board[1][2] && board[1][1] != TicTacToeState.BLANK)
                || (board[2][0] == board[2][1] && board[2][1] == board[2][2] && board[2][1] != TicTacToeState.BLANK)
                || //Get columns' score
                (board[0][0] == board[1][0] && board[1][0] == board[2][0] && board[1][0] != TicTacToeState.BLANK)
                || (board[0][1] == board[1][1] && board[1][1] == board[2][1] && board[1][1] != TicTacToeState.BLANK)
                || (board[0][2] == board[1][2] && board[1][2] == board[2][2] && board[1][2] != TicTacToeState.BLANK)
                || //Get diagonals' score
                (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[1][1] != TicTacToeState.BLANK)
                || (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[1][1] != TicTacToeState.BLANK)
                || //Get whether everybody's filled:
                (board[0][0] != TicTacToeState.BLANK && board[0][1] != TicTacToeState.BLANK && board[0][2] != TicTacToeState.BLANK
                && board[1][0] != TicTacToeState.BLANK && board[1][1] != TicTacToeState.BLANK && board[1][2] != TicTacToeState.BLANK
                && board[2][0] != TicTacToeState.BLANK && board[2][1] != TicTacToeState.BLANK && board[2][2] != TicTacToeState.BLANK);
    }

    /**
     * Calculate the line score, based on created info. We examine this board as
     * a candidate.
     * @param a
     * @param b
     * @param c
     * @return 
     */
    private long getLineScore(TicTacToeState a, TicTacToeState b, TicTacToeState c) {
        long lineScore = 0;

        //Check victory
        if (a == cpuMarker && b == cpuMarker && c == cpuMarker) {
            lineScore += 10000;
        //Check winning candidate
        } else if ((a == cpuMarker && b == cpuMarker && c == TicTacToeState.BLANK)
                || (a == cpuMarker && c == cpuMarker && b == TicTacToeState.BLANK)
                || (b == cpuMarker && c == cpuMarker && a == TicTacToeState.BLANK)) {
            lineScore += 100;
            if (cpuMarker == nextPlayer) { //The next turn is ours to win! :)
                lineScore += 500;
            }
        //Check losing condition
        } else if ((a == cpuMarker.getOpposite() && b == cpuMarker.getOpposite() && c == TicTacToeState.BLANK)
                || (a == cpuMarker.getOpposite() && c == cpuMarker.getOpposite() && b == TicTacToeState.BLANK)
                || (b == cpuMarker.getOpposite() && c == cpuMarker.getOpposite() && a == TicTacToeState.BLANK)) {
            lineScore -= 100;
            if (cpuMarker != nextPlayer) { //The next turn is not ours :(
                lineScore -= 8000;
            }
        }

        lineScore += a == cpuMarker ? 5 : a == TicTacToeState.BLANK ? 0 : -5;
        lineScore += b == cpuMarker ? 5 : b == TicTacToeState.BLANK ? 0 : -5;
        lineScore += c == cpuMarker ? 5 : c == TicTacToeState.BLANK ? 0 : -5;

        return lineScore;
    }

    @Override
    public ArrayList<GeneticNode> createGeneration() {
        TicTacToeState marker = getNextTurn();
        ArrayList<GeneticNode> children = new ArrayList<GeneticNode>();

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (board[x][y] == TicTacToeState.BLANK) {
                    TicTacToeState[][] newBoard = new TicTacToeState[3][3];
                    copyBoard(board, newBoard);
                    newBoard[x][y] = marker;
                    children.add(new TicTacToe(newBoard, cpuMarker));
                }
            }
        }

        return children;
    }

    @Override
    public GeneticNode createChild() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void printBoard() {
        for (int x = 0; x < 3; x++) {
            System.out.println(
                    board[x][0].getString() + "|"
                    + board[x][1].getString() + "|"
                    + board[x][2].getString());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TicTacToeState[][] initialBoard = {
            {TicTacToeState.BLANK, TicTacToeState.BLANK, TicTacToeState.BLANK},
            {TicTacToeState.BLANK, TicTacToeState.BLANK, TicTacToeState.BLANK},
            {TicTacToeState.BLANK, TicTacToeState.BLANK, TicTacToeState.BLANK}
        };

        TicTacToe game = new TicTacToe(initialBoard, TicTacToeState.X);

        int turn = 0;
        while (!game.hasWinningCondition()) {

            System.out.println("Turn " + String.valueOf(turn++));
            game = (TicTacToe) game.getBestChild();
            game.printBoard();
        }
    }
}
