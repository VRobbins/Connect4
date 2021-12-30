package RandomAI;

import java.util.*;

import CFGame.CFGame;
import CFPlayer.CFPlayer;

public class RandomAI implements CFPlayer {
    public int nextMove(CFGame g) { // returns random next legal move
        char[][] grid = g.get_grid();
        boolean[] legal = new boolean[grid[0].length];
        for (int j = 0; j < grid[0].length; j++) { // check if there is an empty space to play
            if (grid[grid.length - 1][j] == 'O') {
                legal[j] = true;
            } else {
                legal[j] = false;
            }
        }
        Random rand = new Random();
        int x = rand.nextInt(legal.length);
        while (!legal[x]) { // so long as random choice is illegal, choose a new column
            x = rand.nextInt(legal.length);
        }
        return x;
    }
    
    public String getName() { // return name of random player
        return "Random Player";
    }
}