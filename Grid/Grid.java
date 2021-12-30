package Grid;

public class Grid {
    public char contains_horizontal(char[][] input, int last_row) { // is there a horizontal line of four?
        int countR = 0;
        int countB = 0;
        for (int j = 0; j < input[0].length; j++) {
            if (input[last_row][j] == 'O') {
                countR = 0;
                countB = 0;
            } else if (input[last_row][j] == 'R') {
                countR += 1;
                countB = 0;
            } else if (input[last_row][j] == 'B') {
                countB += 1;
                countR = 0;
            }
            if (countR == 4) {
                return 'R';
            }
            if (countB == 4) {
                return 'B';
            }
        }
        return 'O';
    }
    public char contains_vertical(char[][] input, int last_col) { // is there a vertical line of four?
        int countR = 0;
        int countB = 0;
        for (int i = 0; i < input.length; i++) {
            if (input[i][last_col] == 'O') {
                countR = 0;
                countB = 0;
            } else if (input[i][last_col] == 'R') {
                countR += 1;
                countB = 0;
            } else if (input[i][last_col] == 'B') {
                countB += 1;
                countR = 0;
            }
            if (countR == 4) {
                return 'R';
            }
            if (countB == 4) {
                return 'B';
            }
        }
        return 'O';
    }

    public char contains_forward_slash(char[][] input, int last_row, int last_col) { // is there a forward slash?
        int countR = 0;
        int countB = 0;
        int i = last_row <= last_col ? 0 : last_row - last_col;
        int j = last_row <= last_col ? last_col - last_row : 0;
        for(int index = 0; i+index<input.length && j+index<input[0].length;++index)
        {
            if (input[i+index][j+index] == 'O') {
                countR = 0;
                countB = 0;
            } else if (input[i+index][j+index] == 'R') {
                countR += 1;
                countB = 0;
            } else if (input[i+index][j+index] == 'B') {
                countB += 1;
                countR = 0;
            }
            if (countR == 4) {
                return 'R';
            }
            if (countB == 4) {
                return 'B';
            }
        }
        return 'O';
    }
    public char contains_backslash(char[][] input, int last_row, int last_col) {
        char[][] reflect = new char[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                reflect[i][j] = input[i][(input[0].length - 1) - j ]; // flip matrix about vertical axis and check if there is a
                                                        // forward slash
            }
        }
        return contains_forward_slash(reflect, last_row, (input[0].length - 1) - last_col);
    }
}
