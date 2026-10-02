package game.endgame;

import game.character.Player;
import game.map.Board;

import java.util.List;

/**
 * Strategy interface for resolving tied game results.
 *
 * @author Jia Ming
 */
public interface TieBreakerStrategy {
    
    /**
     * Returns the players who satisfy this tie-breaker condition.
     *
     * @param allPlayer all Players in the current Game
     * @param board     current Board in the game
     * @return all Player who meet the tiebreaker condition
     */
    List<Player> getTieBreaker(List<Player> allPlayer, Board board);

    /**
     * Returns the priority order of this tie-breaker strategy. Lower Values
     * indicate higher priority.
     *
     * @return the priority order of the strategy
     */
    int getTieBreakerOrder();
}
