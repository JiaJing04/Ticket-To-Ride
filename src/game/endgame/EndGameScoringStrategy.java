package game.endgame;

import game.character.Player;
import game.map.Board;

import java.util.List;

/**
 * Strategy interface for applying different end-game scoring rules
 *
 * @author Jia Ming
 */
public interface EndGameScoringStrategy {

    /**
     * Applies end-game scoring to all players
     *
     * @param playerList list of all Players in the Game
     * @param board      Board of the game
     */
    void apply(List<Player> playerList, Board board);
}
