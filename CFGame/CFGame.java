package CFGame;
import Grid.Grid;

public class CFGame {
    public boolean whose_turn = true; // true = red
    int count = 0;
    int rows = 6;
    int columns = 7;
    int winner = 0;
    public boolean[][] grid = new boolean[rows][columns];
    public boolean[][] mask = new boolean[rows][columns];
    public int[] last_coord = new int[2];

    public boolean[][] get_mask() {
        return mask;
    }

    public boolean get_whose_turn() {
        return whose_turn;
    }

    public int get_rows() {
        return rows;
    }

    public int get_cols() {
        return columns;
    }

    public CFGame() {
        for (int i = 0; i < rows; i++) { // initialize 'O' in every entry as empty
            for (int j = 0; j < columns; j++) {
                grid[i][j] = false; // true is RED
                mask[i][j] = false;
            }
        }
    }

    public CFGame(int input_rows, int input_columns) {
        rows = input_rows == -1 ? 6 : input_rows;
        columns = input_columns == -1 ? 7 : input_columns;
        grid = new boolean[rows][columns];
        mask = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) { // initialize empty mask and false grid
            for (int j = 0; j < columns; j++) {
                grid[i][j] = false;
                mask[i][j] = false;
            }
        }
    }

    public boolean[][] get_grid() {
        return grid;
    }

    public void print() { // Print board in console
        for (int i = rows - 1; i > -1; i--) {
            for (int j = 0; j < columns; j++) { 
                if (grid[i][j] && mask[i][j]) { // red
                    System.out.print("| RED   ");
                } else if (!grid[i][j] && mask[i][j]) {
                    System.out.print("| YELLOW ");
                } else {
                    System.out.print("|       ");
                }
            }
            System.out.print("|");
            System.out.println();
        }
    }

    public boolean play(int c) { // plays a column from 1-7
        for (int i = 0; i < rows; i++) {
            if (!mask[i][c]) { // go up rows until there is a space
                if (whose_turn) {
                    grid[i][c] = true;
                    mask[i][c] = true;
                    whose_turn = false;
                } else {
                    grid[i][c] = false;
                    mask[i][c] = true;
                    whose_turn = true;
                }
                last_coord[0] = i;
                last_coord[1] = c;
                ++count;
                if (count == rows * columns) { // If board is full
                    winner = 0;
                }
                return true;
            }
        }
        return false;
    }

    public boolean isGameOver() { // True if someone won, changes winner appropriately
        Grid myGrid = new Grid();
        char[] contains = new char[4];
        contains[0] = myGrid.contains_horizontal(grid, mask, last_coord[0]);
        contains[1] = myGrid.contains_vertical(grid, mask, last_coord[1]);
        contains[2] = myGrid.contains_forward_slash(grid, mask, last_coord[0], last_coord[1]);
        contains[3] = myGrid.contains_backslash(grid, mask, last_coord[0], last_coord[1]);
        for (int i = 0; i < 4; ++i) {
            if (contains[i] != 'O') {
                winner = contains[i] == 'R' ? 1 : -1;
                return true;
            }
        }
        winner = 0;
        return count == rows * columns;
    }

    public int winner() { // return winner
        return winner;
    }

}