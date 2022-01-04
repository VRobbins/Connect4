package GUICF;

import javax.swing.*;

import CFGame.CFGame;
import CFPlayer.CFPlayer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GUICF extends CFGame {
    GUICF g = this;
    JButton PlayButton;
    CFPlayer p1;
    CFPlayer p2;
    private GameBoard thisboard;

    JLabel[][] labels = new JLabel[6][7];
    public GUICF(CFPlayer ai) { // ai vs user
        p1 = ai;
        thisboard = new GameBoard();
        JPanel full_board = new JPanel();
        full_board.setLayout(new GridLayout(0, 7));
        for (int i = 0; i < 7; i++) { // add first row of buttons
            PlayButton = new JButton(String.valueOf(i+1));
            Ai_versus_Human listener = new Ai_versus_Human();
            PlayButton.addActionListener(listener);
            listener.set_col(i);
            full_board.add(PlayButton);
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                full_board.add(labels[i][j]); // add relevent label for every entry in grid
            }
        }
        JFrame frame = new JFrame(" Connect Four ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(full_board);
        frame.setSize(1000, 800);
        Random rand = new Random();
        if(rand.nextBoolean()==true)
        {
            playGUI(p1.nextMove(g));
        }
        frame.setVisible(true);

    }

    private class Ai_versus_Human implements ActionListener {
        int col; // column associated with button

        public void set_col(int input_col) {
            col = input_col;
        }

        public void actionPerformed(ActionEvent event) {
            if (isGameOver() == false && get_grid()[5][col] == 'O') {
                playGUI(col);
                if (isGameOver() == false) { // play the AI's next move if you didn't just win
                    playGUI(p1.nextMove(g));
                }
            }
        }
    }

    private class Ai_versus_Ai implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            boolean turn = whose_turn;
            if (isGameOver() == false) { // if game isn't over, proceed
                while (whose_turn && turn) { // while it's red's turn, play (whose_turn only changes if the play was
                                             // successful)
                    playGUI(p1.nextMove(g));
                }
                while (!whose_turn && !turn) { // while it's black's turn, do the same.
                    playGUI(p2.nextMove(g));
                }
            }
        }
    }

    public GUICF(CFPlayer ai1, CFPlayer ai2) {
        Random rand = new Random();
        if(rand.nextBoolean()==true)
        {
            p1 = ai1;
        p2 = ai2;
        }
        else{
            p2 = ai1;
            p1 = ai2;
        }
        /*
        p1=ai1;
        p2=ai2;
        */
        
        thisboard = new GameBoard();
        JPanel full_board = new JPanel();
        full_board.setLayout(new GridLayout(0, 7));
        for (int i = 0; i < 6; i++) { // add JButtons to first row
            full_board.add(new JLabel());
            if (i == 2) { // button in center is a play button
                PlayButton = new JButton("Play");
                PlayButton.addActionListener(new Ai_versus_Ai());
                full_board.add(PlayButton);
            }
        }
        for (int i = 0; i < 6; i++) { // add the rest of the JLabels
            for (int j = 0; j < 7; j++) {
                full_board.add(labels[i][j]);
            }
        }
        JFrame frame = new JFrame(" Connect Four ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(full_board);
        frame.setSize(1000, 800);
        frame.setVisible(true);
    }

    private boolean playGUI(int c) { // play respective column and paint
        boolean val = play(c);
        thisboard.paint(last_coord[0], last_coord[1], whose_turn ? 1 : 0);
        return val;
    }

    private class GameBoard extends javax.swing.JPanel {
        private GameBoard() { 
            GridLayout my_layout = new GridLayout(6, 7);
            setLayout(my_layout);
            for (int i = 0; i < 6; i++) { // initialize empty board with grid layout
                for (int j = 0; j < 7; j++) {
                    labels[i][j] = new JLabel();
                    labels[i][j].setOpaque(true);
                    labels[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    add(labels[i][j]);
                }
            }
        }
        private void paint(int x, int y, int color) {// paint appropriate square (must horizontally flip current grid)
            if (color == 1) { 
                labels[6 - 1 - x][y].setBackground(Color.BLACK);

            } else {
                labels[6 - 1 - x][y].setBackground(Color.RED);
            }
        }
    }
    public GameBoard get_board() { // return board
        return thisboard;
    }

}