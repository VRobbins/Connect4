package VincentAI;

import CFGame.CFGame;
import CFPlayer.CFPlayer;
import Node.Node;
import Grid.Grid;
import java.util.ArrayList;

public class VincentAI implements CFPlayer {
    boolean[][] board;
    boolean[][] mask;
    char color;
    boolean whose_turn;
    int[][] entry_vals;
    int rows = 6;
    int cols = 7;
    int difficulty = 5;

    public VincentAI(int input_rows, int input_cols, int input_difficulty) {
        rows = input_rows;
        cols = input_cols;
        difficulty = input_difficulty;
        Grid myGrid = new Grid();
        entry_vals = new int[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                entry_vals[i][j] = myGrid.number_wins_with_element(entry_vals, i, j);
            }
        }
    }

    public VincentAI(int input_difficulty) {
        difficulty = input_difficulty;
        Grid myGrid = new Grid();
        entry_vals = new int[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                entry_vals[i][j] = myGrid.number_wins_with_element(entry_vals, i, j);
            }
        }
    }

    public int get_rows() {
        return rows;
    }

    public int get_columns() {
        return cols;
    }

    public String getName() {
        return "Vincent's AI";
    }

    public void setBoard(boolean[][] input_board) {
        board = input_board;
    }

    public void setMask(boolean[][] input_mask) {
        mask = input_mask;
    }

    public void setColor(char inputColor) {
        color = inputColor;
    }

    public int nextMove(CFGame g) {
        setBoard(copyMatrix(g.get_grid())); // set board for current move
        setMask(copyMatrix(g.get_mask()));
        setColor(g.get_whose_turn() ? 'R' : 'Y'); // find out what color you are
        Node<boolean[][]> position = new Node<>(copyMatrix(board), copyMatrix(mask), null);
        minimax(position, difficulty, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        Node<boolean[][]> best_move = position;
        int max = Integer.MIN_VALUE;
        for (Node<boolean[][]> child : position.getNext()) {
            if (child.getVal() > max) {
                best_move = child;
                max = child.getVal();
            }
        }
        if (max == Integer.MIN_VALUE + 1) {
            minimax(position, 2, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
            Node<boolean[][]> best_move2 = position;
            int max2 = Integer.MIN_VALUE;
            for (Node<boolean[][]> child : position.getNext()) {
                if (child.getVal() > max2) {
                    best_move2 = child;
                    max2 = child.getVal();
                }
            }
            return best_move2.get_last_col();
        }
        return best_move.get_last_col();
    }

    public ArrayList<Node<boolean[][]>> generateChildren(Node<boolean[][]> input_parent, boolean nextColor) // true ==
                                                                                                            // RED
    {
        ArrayList<Node<boolean[][]>> ret = new ArrayList<>();
        for (int j = 0; j < board[0].length; ++j) { // find legal moves
            if (!input_parent.getMask()[input_parent.getMask().length - 1][j]) {
                ret.add(play(input_parent.getData(), input_parent.getMask(), j, nextColor));
            }
        }
        input_parent.setNext(ret);
        return ret;
    }

    public Node<boolean[][]> play(boolean[][] input_board, boolean[][] input_mask, int col, boolean nextColor) // TRUE==RED
    {
        boolean[][] output_board = copyMatrix(input_board);
        boolean[][] output_mask = copyMatrix(input_mask);
        Node<boolean[][]> outputNode = new Node<>();
        for (int i = 0; i < output_board.length; ++i) {
            if (!output_mask[i][col]) { // go up rows until there is a space
                {
                    output_board[i][col] = nextColor;
                    output_mask[i][col] = true;
                    outputNode.setData(output_board);
                    outputNode.setMask(output_mask);
                    outputNode.setNext(null);
                    outputNode.set_last(i, col);
                    return outputNode;
                }
            }
        }
        System.out.println("failed to play a column");
        return outputNode;
    }

    public int boardValue(Node<boolean[][]> position) { // return 100 (-100) if you win (resp. lose) in this position
        Grid myGrid = new Grid();
        char winchar = Win(position);
        if (winchar == 'R') {
            return color == 'R' ? Integer.MAX_VALUE - 1 : Integer.MIN_VALUE + 1;
        }
        if (winchar == 'Y') {
            return color == 'Y' ? Integer.MAX_VALUE - 1 : Integer.MIN_VALUE + 1;
        }
        boolean[][] board = position.getData();
        boolean[][] mask = position.getMask();
        int sum_r = 0;
        int sum_b = 0;
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                if (board[i][j] && mask[i][j])
                    sum_r += entry_vals[i][j];
                else if ((!board[i][j]) && mask[i][j])
                    sum_b += entry_vals[i][j];
            }
        }
        return color == 'R' ? sum_r - sum_b : sum_b - sum_r;
    }

    public char Win(Node<boolean[][]> pos) { // returns true if red wins
        Grid myGrid = new Grid();
        char[] contains = new char[4];
        contains[0] = myGrid.contains_horizontal(pos.getData(), pos.getMask(), pos.get_last_row());
        contains[1] = myGrid.contains_vertical(pos.getData(), pos.getMask(), pos.get_last_col());
        contains[2] = myGrid.contains_forward_slash(pos.getData(), pos.getMask(), pos.get_last_row(),
                pos.get_last_col());
        contains[3] = myGrid.contains_backslash(pos.getData(), pos.getMask(), pos.get_last_row(), pos.get_last_col());
        for (int i = 0; i < 4; ++i) {
            if (contains[i] != 'O') {
                return contains[i];
            }
        }
        return 'O';
    }

    public int minimax(Node<boolean[][]> position, int depth, boolean isMaxing, int alpha, int beta) // max when at your
                                                                                                     // color
    {
        boolean amIRed = color == 'R';
        char winchar = Win(position);
        if (depth == 0) {
            position.setVal(boardValue(position));
            return position.getVal();
        } else if (winchar == 'R') {
            position.setVal(color == 'R' ? Integer.MAX_VALUE - 1 : Integer.MIN_VALUE + 1);
            return color == 'R' ? Integer.MAX_VALUE - 1 : Integer.MIN_VALUE + 1;
        } else if (winchar == 'Y') {
            position.setVal(color == 'Y' ? Integer.MAX_VALUE - 1 : Integer.MIN_VALUE + 1);
            return color == 'Y' ? Integer.MAX_VALUE - 1 : Integer.MIN_VALUE + 1;
        }
        int eval = 0;
        ArrayList<Node<boolean[][]>> children;
        if (isMaxing) {
            children = generateChildren(position, amIRed);
            int max = Integer.MIN_VALUE;
            for (Node<boolean[][]> child : children) {
                eval = minimax(child, depth - 1, false, alpha, beta);
                max = eval > max ? eval : max;
                alpha = eval > alpha ? eval : alpha;
                if (beta <= alpha) {
                    break;
                }
            }
            position.setVal(max);
            return max;
        }
        children = generateChildren(position, !amIRed);
        int min = Integer.MAX_VALUE;
        for (Node<boolean[][]> child : children) {
            eval = minimax(child, depth - 1, true, alpha, beta);
            min = eval < min ? eval : min;
            beta = eval < beta ? eval : beta;
            if (beta <= alpha) {
                break;
            }
        }
        position.setVal(min);
        return min;
    }

    public boolean[][] copyMatrix(boolean[][] input) { // copy matrix and return new one
        boolean[][] result = new boolean[input.length][input[0].length];
        for (int i = 0; i < input.length; ++i) {
            result[i] = input[i].clone();
        }
        return result;
    }
}