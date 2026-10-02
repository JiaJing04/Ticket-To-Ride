package game.endgame;

import game.card.DestinationCard;
import game.character.Player;
import game.map.Board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * End game scoring strategy based on number of destination ticket completed
 * by the Player
 *
 * @author Jia Ming
 */
public class DestinationTicketScoring implements EndGameScoringStrategy, TieBreakerStrategy {

    /**
     * Map to store the number of destination ticket completed by each Player
     */
    private Map<Player, Integer> playerDestCompletedMap = new HashMap<>();

    /**
     * Priority order of this tie-breaker strategy. A value of -1 disables this
     * strategy for tie-breaking
     */
    private final int tieBreakerOrder;

    /**
     * Creates a destination ticket scoring strategy.
     *
     * @param tieBreakerOrder the priority order of this tiebreaker strategy
     */
    public DestinationTicketScoring(int tieBreakerOrder) {
        this.tieBreakerOrder = tieBreakerOrder;
    }

    /**
     * Applies end game scoring to each of the player based on the completion
     * of their destination ticket on hand.
     * completed: add score of the ticket
     * not completed: deduct score of the ticket
     *
     * @param playerList list of all Players in the Game
     * @param board      Board of the game
     */
    @Override
    public void apply(List<Player> playerList, Board board) {
        for (Player player : playerList) {

            int completedCount = 0;

            for (DestinationCard destCard : player.getHand().getDestinationCards()) {
                if (destCard.isCompleted(player, board)) {
                    player.addScore(destCard.getPoints());

                    completedCount++;

                } else {
                    player.deductScore(destCard.getPoints());
                }
            }

            playerDestCompletedMap.put(player, completedCount);
        }
    }

    /**
     * Resolves tie-breaker based on number of completed destination tickets.
     * Player with the highest number of destination ticket completion will be
     * selected. If multiple players tie, all are returned.
     *
     * @param allPlayers all Player in the current game
     * @param board      current Board of the game
     * @return list of Players with the highest number of destination ticket completion
     */
    @Override
    public List<Player> getTieBreaker(List<Player> allPlayers, Board board) {
        int maxCompleted = 0;

        for (int count : playerDestCompletedMap.values()) {
            maxCompleted = Math.max(maxCompleted, count);
        }

        List<Player> result = new ArrayList<>();

        for (Player player : allPlayers) {
            int count = playerDestCompletedMap.getOrDefault(player, 0);

            if (count == maxCompleted) {
                result.add(player);
            }
        }

        return result;
    }
    
    /**
     * Returns the order of tiebreaker in the current game.
     *
     * @return the tiebreaker order priority (lower = higher priority)
     */
    @Override
    public int getTieBreakerOrder() {
        return tieBreakerOrder;
    }
}
