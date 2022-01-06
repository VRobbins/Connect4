package CFGame;
import Grid.Grid;

public class CFGame {
    public boolean whose_turn = true; // true = red
    int count = 0;
    int rows = 6;
    int columns = 7;
    int winner = 0;
    public char[][] grid = new char[rows][columns];
    public int[] last_coord = new int[2];

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
                grid[i][j] = 'O';
            }
        }
    }
    public CFGame(int input_rows, int input_columns) {
        rows = input_rows == -1? 6:input_rows;
        columns=input_columns==-1 ? 7 : input_columns;
        grid = new char[rows][columns];
        for (int i = 0; i < rows; i++) { // initialize 'O' in every entry as empty
            for (int j = 0; j < columns; j++) {
                grid[i][j] = 'O';
            }
        }
    }

    public char[][] get_grid() {
        return grid;
    }

    public void print() {
        for (int i = rows - 1; i > -1; i--) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j] == 'R') {
                    System.out.print("| RED   ");
                } else if (grid[i][j] == 'Y') {
                    System.out.print("| YELLOW ");
                } else if (grid[i][j] == 'O') {
                    System.out.print("|       ");
                }
            }
            System.out.print("|");
            System.out.println();
        }
    }

    public boolean play(int c) { // plays a column from 1-7
        for (int i = 0; i < rows; i++) {
            if (grid[i][c] == 'O') { // go up rows until there is a space
                if (whose_turn) {
                    grid[i][c] = 'R';
                    whose_turn = false;
                } else {
                    grid[i][c] = 'Y';
                    whose_turn = true;
                }
                last_coord[0] = i;
                last_coord[1] = c;
                count++;
                if (count == rows * columns) {
                    winner = 0;
                }
                return true;
            }
        }
        return false;
    }
    public boolean isGameOver() {
        Grid myGrid = new Grid();
        if(myGrid.contains_horizontal(grid, last_coord[0]) == 'R' || myGrid.contains_vertical(grid, last_coord[1])
        == 'R' || myGrid.contains_forward_slash(grid, last_coord[0], last_coord[1]) == 'R' 
        || myGrid.contains_backslash(grid, last_coord[0], last_coord[1])   == 'R' )
        {
            winner = 1;
            return true;
        }
        if(myGrid.contains_horizontal(grid, last_coord[0]) == 'Y' || myGrid.contains_vertical(grid, last_coord[1])
        == 'Y' || myGrid.contains_forward_slash(grid, last_coord[0], last_coord[1]) == 'Y' 
        || myGrid.contains_backslash(grid, last_coord[0], last_coord[1])   == 'Y' )
        {
            winner = -1;
            return true;
        }
        winner = 0;
        return count == rows*columns;
    }

    public int winner() { // return winner
        return winner;
    }

}