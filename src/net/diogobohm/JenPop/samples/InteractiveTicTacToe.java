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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.diogobohm.JenPop.samples.TicTacToe.TicTacToeState;

/**
 * Provides interactive Tic Tac Toe gameplay against a JenPop powered player.
 */
public class InteractiveTicTacToe extends JFrame {

    private TicTacToe game;
    private JButton[][] buttons = new JButton[3][3];
    private static TicTacToeState playerMarker = TicTacToeState.O;

    public InteractiveTicTacToe() {
        TicTacToeState[][] initialBoard = {
            {TicTacToeState.BLANK, TicTacToeState.BLANK, TicTacToeState.BLANK},
            {TicTacToeState.BLANK, TicTacToeState.BLANK, TicTacToeState.BLANK},
            {TicTacToeState.BLANK, TicTacToeState.BLANK, TicTacToeState.BLANK}
        };
        this.setTitle("InteractiveTicTacToe");
        this.setSize(300, 300);
        game = new TicTacToe(initialBoard, playerMarker.getOpposite());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout(3, 3));
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++) {
                final int l = x, c = y;

                buttons[x][y] = new JButton();
                buttons[x][y].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (game.getBoard()[l][c] == TicTacToeState.BLANK) {
                            //Player plays
                            game.getBoard()[l][c] = playerMarker;
                            setButtons();
                            if (game.hasWinningCondition()) {
                                JOptionPane.showMessageDialog(rootPane, "You won!");
                                System.exit(0);
                            }
                            //Computer plays
                            game = (TicTacToe) game.getBestChild(3);
                            setButtons();
                            if (game.hasWinningCondition()) {
                                JOptionPane.showMessageDialog(rootPane, "You lost!");
                                System.exit(0);
                            }
                        }
                    }
                });

                this.getContentPane().add(buttons[x][y]);
            }
        }

        this.setVisible(true);
    }

    private void setButtons() {
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[0].length; y++) {
                if (game.getBoard()[x][y] != TicTacToeState.BLANK) {
                    buttons[x][y].setText(game.getBoard()[x][y].getString());
                }
            }
        }
        this.repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InteractiveTicTacToe instance = new InteractiveTicTacToe();
    }
}
