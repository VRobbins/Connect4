package ConsoleCF;

import java.util.*;
import CFGame.CFGame;
import CFPlayer.CFPlayer;
import VincentAI.VincentAI;
import RandomAI.RandomAI;

public class ConsoleCF extends CFGame {
    CFPlayer first = null;
    CFPlayer second = null;
    boolean is_ai_first = false;
    boolean human_player_involved = false;
    CFGame game = new CFGame();

    private class HumanPlayer implements CFPlayer {
        public int nextMove(CFGame g) {
            char[][] grid = g.get_grid();
            boolean[] legal = new boolean[grid[0].length];
            for (int j = 0; j < grid[0].length; j++) { // check if there is an empty space to play
                if (grid[grid.length - 1][j] == 'O') {
                    legal[j] = true;
                } else {
                    legal[j] = false;
                }
            }
            boolean b = false;
            int num = 0;
            while (!b) {
                game.print();
                System.out.print("CHOOSE COLUMN (1-7): ");
                Scanner in = new Scanner(System.in);
                num = in.nextInt();
                if (num < 1 || num > 7) {
                    b = false;
                    System.out.println("Out of bounds, please try again.");
                } else {
                    b = legal[num - 1];
                    if (!b) {
                        System.out.println("Illegal Entry, please try again");
                    }
                }
                in.close();

            }
            return num - 1;
        }

        public String getName() {
            return "Human Player";
        }
    }

    public ConsoleCF(CFPlayer ai) {
        Random rand = new Random();
        HumanPlayer human = new HumanPlayer();
        if (rand.nextBoolean()) { // decides randomly who goes first
            is_ai_first = true;
            first = ai;
            second = human;
        } else {
            is_ai_first = false;
            first = human;
            second = ai;
        }
        human_player_involved = true;
    }

    public ConsoleCF(CFPlayer ai1, CFPlayer ai2) {
        Random rand = new Random();
        if (rand.nextBoolean()) { // decides randomly who goes first
            first = ai1;
            second = ai2;
        } else {
            first = ai2;
            second = ai1;
        }
    }

    public void playOut() {

        while (!game.isGameOver()) {
            game.play(first.nextMove(game));
            //game.print();
            //System.out.println();
            if (!game.isGameOver()) {
                game.play(second.nextMove(game));
                //game.print();
                //System.out.println();
            }
        }
        if (game.isGameOver()) {
            System.out.println(getWinner() + " Wins!");
        }

    }

    public String getWinner() {
        if (game.winner() == 1) {
            return first.getName();
        } else if (game.winner() == -1) {
            return second.getName();
        }
        return "No one";
    }

    public static void main(String[] args) {

        CFPlayer ai1 = new VincentAI();
        CFPlayer ai2 = new RandomAI();
        int n = 1000;
        int winCount = 0;
        int winCount2 = 0;
        for (int i = 0; i < n; i++) {
            ConsoleCF game = new ConsoleCF(ai1, ai2);
            game.playOut();
            if (game.getWinner() == ai1.getName()) {
                winCount++;
            }
            if (game.getWinner() == ai2.getName()) {
                winCount2++;
            }
        }
        System.out.print(ai1.getName() + " wins ");
        System.out.print(((double) winCount) / ((double) n) * 100 + "%");
        System.out.print(" of the time.");
        System.out.println();
        System.out.print(ai2.getName() + " wins ");
        System.out.print(((double) winCount2) / ((double) n) * 100 + "%");
        System.out.print(" of the time.");

        /*
         * CFPlayer ai1 = new VincentAI();
         * CFPlayer ai2 = new RandomAI();
         * ConsoleCF game= new ConsoleCF(ai1);
         * game.playOut();
         */
        /*
         * int x = 0;
         * while (x == 0) {
         * CFPlayer ai1 = new VincentAI();
         * CFPlayer ai2 = new RandomAI();
         * ConsoleCF game = new ConsoleCF(ai1, ai2);
         * game.playOut();
         * if (game.getWinner() == ai2.getName()) {
         * x=1;
         * }
         * }
         */

    }
}