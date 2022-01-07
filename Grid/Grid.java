package Grid;

public class Grid {

    public char contains_horizontal(boolean[][] input, boolean[][] mask, int last_row) { // is there a horizontal line of four?
        int countR = 0;
        int countB = 0;
        for (int j = 0; j < input[0].length; j++) {
            if (mask[last_row][j] == false) {
                countR = 0;
                countB = 0;
            } else if (input[last_row][j] == true) {
                countR += 1;
                countB = 0;
            } else if (input[last_row][j] == false) {
                countB += 1;
                countR = 0;
            }
            if (countR == 4) {
                return 'R';
            }
            if (countB == 4) {
                return 'Y';
            }
        }
        return 'O';
    }

    public char contains_vertical(boolean[][] input, boolean[][] mask, int last_col) { // is there a vertical line of four?
        int countR = 0;
        int countB = 0;
        for (int i = 0; i < input.length; i++) {
            if (mask[i][last_col] == false) {
                break; // pieces fall to bottom
            } else if (input[i][last_col] == true) {
                countR += 1;
                countB = 0;
            } else if (input[i][last_col] == false) {
                countB += 1;
                countR = 0;
            }
            if (countR == 4) {
                return 'R';
            }
            if (countB == 4) {
                return 'Y';
            }
        }
        return 'O';
    }

    public char contains_forward_slash(boolean[][] input, boolean[][] mask, int last_row, int last_col) { // is there a forward slash?
        int countR = 0;
        int countB = 0;
        int i = last_row <= last_col ? 0 : last_row - last_col;
        int j = last_row <= last_col ? last_col - last_row : 0;
        for (int index = 0; i + index < input.length && j + index < input[0].length; ++index) {
            if (mask[i + index][j + index] == false) {
                countR = 0;
                countB = 0;
            } else if (input[i + index][j + index] == true) {
                countR += 1;
                countB = 0;
            } else if (input[i + index][j + index] == false) {
                countB += 1;
                countR = 0;
            }
            if (countR == 4) {
                return 'R';
            }
            if (countB == 4) {
                return 'Y';
            }
        }
        return 'O';
    }

    public char contains_backslash(boolean[][] input, boolean[][] mask, int last_row, int last_col) {
        boolean[][] reflect = new boolean[input.length][input[0].length];
        boolean[][] reflect_mask = new boolean[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                reflect[i][j] = input[i][(input[0].length - 1) - j]; // flip matrix about vertical axis and check if
                                                                     // there is a
                // forward slash
                reflect_mask[i][j] = mask[i][(input[0].length - 1) - j];
            }
        }
        return contains_forward_slash(reflect, reflect_mask, last_row, (input[0].length - 1) - last_col);
    }

    public int number_wins_with_element(int[][] input,int input_row, int input_column) {
        int num_r = input_column <= input[0].length / 2 ? input_column + 1 : (input[0].length - 1) - input_column + 1;
        int num_c = input_row < input.length / 2 ? input_row + 1 : (input.length - 1) - input_row + 1;
        int diag = 0;
        int index_up = 0;
        int index_down = 0;

        while (input_row + index_up < input.length && input_column + index_up < input[0].length && index_up < 4) {
            ++index_up;
        }
        while (input_row - index_down >= 0 && input_column - index_down >= 0 && index_down < 4) {
            ++index_down;
        }
        diag = index_up + index_down - 1 < 4 ? 0 : index_up - 2 + index_down - 2;
        if (index_up >= 4)
            diag = index_down - 1;
        if (index_down >= 4)
            diag = index_up - 1;
        return num_r + num_c + diag;
    }

}
