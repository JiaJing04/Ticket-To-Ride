package game.endgame;

import game.character.Player;
import game.map.Board;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for end game calculation and determine winner
 *
 * @author Jia Ming
 */
public class EndGameService {

    /**
     * List to store all end game scoring strategy in the current game
     */
    private List<EndGameScoringStrategy> endGameScoringStrategies = new ArrayList<>();

    /**
     * List to store all tiebreaker strategy in the current game
     */
    private List<TieBreakerStrategy> tieBreakerStrategies = new ArrayList<>();

    /**
     * Map to store each Player final score
     */
    private Map<Player, Integer> playerFinalScoreMap = new HashMap<>();

    /**
     * Adds an end game strategy to the service for the current game
     *
     * @param scoringStrategy EndGameScoringStrategy to be added
     */
    public void addEndGameScoringStrategy(EndGameScoringStrategy scoringStrategy) {
        endGameScoringStrategies.add(scoringStrategy);
    }

    /**
     * Adds tie-breaker strategy to the service for the current game
     *
     * @param tieBreakerStrategy TieBreakerStrategy to be added
     */
    public void addTieBreakerStrategy(TieBreakerStrategy tieBreakerStrategy) {
        tieBreakerStrategies.add(tieBreakerStrategy);
    }

    /**
     * Applies all end-game scoring strategies to all players and stores final
     * scores.
     *
     * @param playerList list of all Players in the game
     * @param board      Board of the game
     */
    public void applyEndGameScoring(List<Player> playerList, Board board) {

        // Apply all end game scoring
        for (EndGameScoringStrategy endGameScoringStrategy : endGameScoringStrategies) {
            endGameScoringStrategy.apply(playerList, board);
        }

        // Save each Player final score
        for (Player player : playerList) {
            playerFinalScoreMap.put(player, player.getScore());
        }
    }

    /**
     * Determines the winner of the game using score and tie-breaker rules.
     *
     * @param playerList the players in the game
     * @param board the game board
     * @return the winning player, or {@code null} if the game is a draw
     */
    public Player determineWinner(List<Player> playerList, Board board) {
        int highestScore = getHighestScore(playerList);
        List<Player> candidates = getPlayersWithScore(
                playerList,
                highestScore
        );

        if (candidates.size() == 1) {
            return candidates.get(0);
        }

        return applyTieBreakers(candidates, board);
    }

    /**
     * Returns the highest score among all players.
     *
     * @param playerList the players to check
     * @return the highest score, or {@code 0} if the list is empty
     */
    private int getHighestScore(List<Player> playerList) {
        return playerList.stream()
                .mapToInt(Player::getScore)
                .max()
                .orElse(0);
    }

    /**
     * Returns all players with the specified score.
     *
     * @param playerList the players to filter
     * @param score the score to match
     * @return the players with the specified score
     */
    private List<Player> getPlayersWithScore(
            List<Player> playerList,
            int score) {

        return playerList.stream()
                .filter(player -> player.getScore() == score)
                .toList();
    }

    /**
     * Applies tie-breaker strategies to the current winner candidates.
     *
     * @param candidates players currently tied for the win
     * @param board the game board
     * @return the winning player, or {@code null} if the game remains a draw
     */
    private Player applyTieBreakers(List<Player> candidates, Board board) {
        sortTieBreakers();

        List<Player> currentCandidates = new ArrayList<>(candidates);

        for (TieBreakerStrategy tieBreakerStrategy : tieBreakerStrategies) {
            currentCandidates = tieBreakerStrategy.getTieBreaker(
                    currentCandidates,
                    board
            );

            if (currentCandidates.size() == 1) {
                return currentCandidates.get(0);
            }
        }

        return null;
    }

    /**
     * Sorts tie-breaker strategies by their configured order.
     */
    private void sortTieBreakers() {
        tieBreakerStrategies.sort(
                Comparator.comparingInt(TieBreakerStrategy::getTieBreakerOrder)
        );
    }
}
