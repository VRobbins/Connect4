package CFPlayer;

import CFGame.CFGame;

public interface CFPlayer {
    int nextMove(CFGame g);
    String getName();
}