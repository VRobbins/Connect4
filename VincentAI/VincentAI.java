package VincentAI;

import CFGame.CFGame;
import CFPlayer.CFPlayer;
import Node.Node;
import Grid.Grid;
import java.util.ArrayList;

public class VincentAI implements CFPlayer {
    char[][] board;
    char color;
    boolean whose_turn;

    public String getName() {
        return "Vincent's AI";
    }

    public void setBoard(char[][] input_board) {
        board = input_board;
    }

    public void setColor(char inputColor) {
        color = inputColor;
    }

    public int nextMove(CFGame g) {
        setBoard(copyMatrix(g.get_grid())); // set board for current move
        setColor(g.get_whose_turn() ? 'R' : 'B'); // find out what color you are
        Node<char[][]> position = new Node<>(copyMatrix(board), null);
        minimax(position, 7, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        Node<char[][]> best_move = position;
        int max = Integer.MIN_VALUE;
        for (Node<char[][]> child : position.getNext()) {
            if (child.getVal() > max) {
                best_move = child;
                max = child.getVal();
            }
        }
        //System.out.println(max);
        if(max==Integer.MIN_VALUE)
        {
            minimax(position, 2, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
            Node<char[][]> best_move2 = position;
            int max2 = Integer.MIN_VALUE;
            for (Node<char[][]> child : position.getNext()) {
                if (child.getVal() > max2) {
                    best_move2 = child;
                    max2 = child.getVal();
                }
            }
            return best_move2.get_last_col();
        }
        return best_move.get_last_col(); 
    }

    public ArrayList<Node<char[][]>> generateChildren(Node<char[][]> input_parent, boolean nextColor) // true == RED
    {
        ArrayList<Node<char[][]>> ret = new ArrayList<>();
        for (int j = 0; j < board[0].length; j++) { // find legal moves
            if (input_parent.getData()[input_parent.getData().length - 1][j] == 'O') {
                ret.add(play(input_parent.getData(), j, nextColor));
                // ret.add( new Node<>( play(input_parent.getData(), j,nextColor) , null));
            }
        }
        input_parent.setNext(ret);
        return ret;
    }

    public Node<char[][]> play(char[][] input_board, int col, boolean nextColor) // TRUE==RED
    {
        char[][] output_board = copyMatrix(input_board);
        Node<char[][]> outputNode = new Node<>();
        for (int i = 0; i < output_board.length; i++) {
            if (output_board[i][col] == 'O') { // go up rows until there is a space
                {
                    output_board[i][col] = nextColor ? 'R' : 'B';
                    outputNode.setData(output_board);
                    outputNode.setNext(null);
                    outputNode.set_last(i, col);
                    return outputNode;
                }
            }
        }
        System.out.println("failed to play a column");
        return outputNode;
    }

    public int boardValue(Node<char[][]> position) { // return 100 (-100) if you win (resp. lose) in this position 
        if (redWin(position)) {
            return color == 'R' ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if (blackWin(position)) {
            return color == 'B' ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        return 0;
    }

    public boolean redWin(Node<char[][]> pos) { //returns true if red wins
        Grid myGrid = new Grid();
        return myGrid.contains_horizontal(pos.getData(), pos.get_last_row()) == 'R'
                || myGrid.contains_vertical(pos.getData(), pos.get_last_col()) == 'R'
                || myGrid.contains_forward_slash(pos.getData(), pos.get_last_row(), pos.get_last_col()) == 'R'
                || myGrid.contains_backslash(pos.getData(), pos.get_last_row(), pos.get_last_col()) == 'R';
    }

    public boolean blackWin(Node<char[][]> pos) { // returns true if black wins
        Grid myGrid = new Grid();
        return myGrid.contains_horizontal(pos.getData(), pos.get_last_row()) == 'B'
                || myGrid.contains_vertical(pos.getData(), pos.get_last_col()) == 'B'
                || myGrid.contains_forward_slash(pos.getData(), pos.get_last_row(), pos.get_last_col()) == 'B'
                || myGrid.contains_backslash(pos.getData(), pos.get_last_row(), pos.get_last_col()) == 'B';
    }

    public int minimax(Node<char[][]> position, int depth, boolean isMaxing, int alpha, int beta) // max when at your color
    {
        boolean amIRed = color == 'R';
        if (depth == 0) {
            position.setVal(boardValue(position));
            return position.getVal();
        }
        if (redWin(position)) {
            position.setVal(color == 'R' ? Integer.MAX_VALUE : Integer.MIN_VALUE);
            return color == 'R' ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if (blackWin(position)) {
            position.setVal(color == 'B' ? Integer.MAX_VALUE : Integer.MIN_VALUE);
            return color == 'B' ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        ArrayList<Node<char[][]>> children;
        if (isMaxing) {
            children = generateChildren(position, amIRed);
            int max = Integer.MIN_VALUE;
            int val1 = 0;
            for (Node<char[][]> child : children) {
                val1 = minimax(child, depth - 1, false, alpha, beta);
                max = val1 > max ? val1 : max;
                alpha = val1 > alpha ? val1 : alpha;
                if (beta <= alpha) {
                    break;
                }
            }
            position.setVal(max);
            return max;
        }
        children = generateChildren(position, !amIRed);
        int min = Integer.MAX_VALUE;
        int val2 = 0;
        for (Node<char[][]> child : children) {
            val2 = minimax(child, depth - 1, true, alpha, beta);
            min = val2 < min ? val2 : min;
            beta = val2 < beta ? val2 : beta;
            if (beta <= alpha) {
                break;
            }
        }
        position.setVal(min);
        return min;
    }

    public char[][] copyMatrix(char[][] input) { // copy matrix and return new one
        char[][] result = new char[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            result[i] = input[i].clone();
        }
        return result;
    }
}