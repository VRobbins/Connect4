package GUICF;

import javax.swing.*;
import java.awt.geom.*;

import CFGame.CFGame;
import CFPlayer.CFPlayer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GUICF extends CFGame {
    GUICF g = this;
    JButton PlayButton;
    CFPlayer p1 = null;
    CFPlayer p2 = null;
    private GameBoard thisboard;
    JLabel[][] labels;

    class Ellipse implements Icon {
        int width;
        int height;
        Color color;

        public Ellipse(int input_width, int input_height, Color input_color) {
            width = input_width;
            height = input_height;
            color = input_color;
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillOval(x, y, width, height);
        }
    }

    public GUICF(CFPlayer ai) { // ai vs user
        super(ai.get_rows(), ai.get_columns());
        p1 = ai;
        Random rand = new Random();
        boolean does_ai_start = rand.nextBoolean();
        labels = new JLabel[get_rows()][get_cols()];
        thisboard = new GameBoard();
        JPanel full_board = new JPanel();
        full_board.setLayout(new GridLayout(0, get_cols()));
        for (int i = 0; i < get_cols(); ++i) { // add first row of buttons
            PlayButton = new JButton(String.valueOf(i + 1));
            Ai_versus_Human listener = new Ai_versus_Human();
            PlayButton.addActionListener(listener);
            listener.set_col(i);
            full_board.add(PlayButton);
        }
        for (int i = 0; i < get_rows(); ++i) {
            for (int j = 0; j < get_cols(); ++j) {
                full_board.add(labels[i][j]); // add relevent label for every entry in grid
            }
        }
        JFrame frame = new JFrame(" Connect Four ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(full_board);
        frame.setSize(800, 800); // 1000,800
        // frame.setResizable(false);
        frame.setVisible(true);
        for (int i = 0; i < get_rows(); ++i) {
            for (int j = 0; j < get_cols(); ++j) {
                Ellipse Piece = new Ellipse(3 * labels[i][j].getWidth() / 4, 3 * labels[i][j].getHeight() / 4,
                        Color.WHITE);
                labels[i][j].setIcon(Piece);
            }
        }
        if (does_ai_start) {
            playGUI(p1.nextMove(g));
        }
    }

    private class Ai_versus_Human implements ActionListener {
        int col; // column associated with button

        public void set_col(int input_col) {
            col = input_col;
        }

        public void actionPerformed(ActionEvent event) {
            if (isGameOver() == false && get_mask()[get_rows() - 1][col] == false) {
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
        super(ai1.get_rows(), ai1.get_columns());
        labels = new JLabel[get_rows()][get_cols()];
        Random rand = new Random();
        p1 = rand.nextBoolean() ? ai1 : ai2;
        p2 = p1 == ai1 ? ai2 : ai1;
        thisboard = new GameBoard();
        JPanel full_board = new JPanel();
        full_board.setLayout(new GridLayout(0, get_cols()));
        for (int i = 0; i < get_cols(); ++i) { // add JButtons to first row
            if (i == get_cols() / 2) { // button in center is a play button
                PlayButton = new JButton("Play");
                PlayButton.addActionListener(new Ai_versus_Ai());
                PlayButton.setOpaque(true);
                PlayButton.setBackground(Color.BLUE);
                full_board.add(PlayButton);
            } else {
                JLabel next = new JLabel();
                next.setBackground(Color.BLUE);
                next.setOpaque(true);
                full_board.add(next);
            }
        }
        for (int i = 0; i < get_rows(); ++i) { // add the rest of the JLabels
            for (int j = 0; j < get_cols(); ++j) {
                full_board.add(labels[i][j]);
            }
        }
        JFrame frame = new JFrame(" Connect Four ");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(full_board);
        frame.setSize(800, 800);
        frame.setVisible(true);
        for (int i = 0; i < get_rows(); ++i) {
            for (int j = 0; j < get_cols(); ++j) {
                Ellipse Piece = new Ellipse(3 * labels[i][j].getWidth() / 4, 3 * labels[i][j].getHeight() / 4,
                        Color.WHITE);
                labels[i][j].setIcon(Piece);
                labels[i][j].setHorizontalAlignment(JLabel.CENTER);
                labels[i][j].setVerticalAlignment(JLabel.CENTER);
            }
        }
    }

    private boolean playGUI(int c) { // play respective column and paint
        boolean val = play(c);
        thisboard.paint(last_coord[0], last_coord[1], whose_turn ? 1 : 0);
        return val;
    }

    private class GameBoard extends javax.swing.JPanel {
        private GameBoard() {
            GridLayout my_layout = new GridLayout(get_rows(), get_cols());
            setLayout(my_layout);
            for (int i = 0; i < get_rows(); ++i) { // initialize empty board with grid layout
                for (int j = 0; j < get_cols(); ++j) {
                    labels[i][j] = new JLabel();
                    labels[i][j].setOpaque(true);
                    labels[i][j].setBorder(BorderFactory.createLoweredBevelBorder());
                    labels[i][j].setBackground(Color.BLUE);
                    add(labels[i][j]);
                }
            }
        }

        private void paint(int x, int y, int color) {// paint appropriate square (must horizontally flip current grid)
            // labels[get_rows() - 1 - x][y].setBackground(color==1? Color.BLACK :
            // Color.RED);
            Ellipse Piece = new Ellipse(3 * labels[get_rows() - 1 - x][y].getWidth() / 4,
                    3 * labels[get_rows() - 1 - x][y].getHeight() / 4, color == 1 ? Color.YELLOW : Color.RED);
            labels[get_rows() - 1 - x][y].setIcon(Piece);
        }
    }

    public GameBoard get_board() { // return board
        return thisboard;
    }
}