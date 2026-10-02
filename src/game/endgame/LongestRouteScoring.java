package game.endgame;

import game.character.Player;
import game.map.Board;
import game.map.City;
import game.map.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * End game scoring strategy to assign bonus point to Player with longest route
 * connected
 *
 * @author Jia Ming
 */
public class LongestRouteScoring implements EndGameScoringStrategy, TieBreakerStrategy {

    /**
     * Bonus point of the longest route, 10
     */
    private static final int BONUS = 10;

    /**
     * Priority order of this tie-breaker strategy. A value of -1 disables this
     * strategy for tie-breaking
     */
    private final int tieBreakerOrder;

    /**
     * Players who currently have the longest route.
     */
    private List<Player> playersWithLongestRoute = new ArrayList<>();

    /**
     * Creates a longest route scoring strategy.
     *
     * @param tieBreakerOrder the priority order of this tiebreaker strategy
     */
    public LongestRouteScoring(int tieBreakerOrder) {
        this.tieBreakerOrder = tieBreakerOrder;
    }

    /**
     * Applies end-game scoring by awarding bonus points to players
     * with the longest continuous route.
     *
     * @param playerList list of all Players in the Game
     * @param board      Board of the game
     */
    @Override
    public void apply(List<Player> playerList, Board board) {
        List<Player> longestRouteCandidates = new ArrayList<>();
        int maxLength = 0;

        for (Player player : playerList) {
            int playerLongestRoute = computeLongestPath(player, board);

            if (playerLongestRoute > maxLength) {
                maxLength = playerLongestRoute;
                longestRouteCandidates.clear();
                longestRouteCandidates.add(player);
            } else if (playerLongestRoute == maxLength) {
                longestRouteCandidates.add(player);
            }
        }

        playersWithLongestRoute = longestRouteCandidates;

        for (Player player : playersWithLongestRoute) {
            player.addScore(BONUS);
        }
    }
    
    /**
     * Returns players who currently hold the longest route.
     *
     * @param allPlayers all players in the game
     * @param board      game board
     * @return players with the longest route
     */
    @Override
    public List<Player> getTieBreaker(List<Player> allPlayers, Board board) {
        return playersWithLongestRoute;
    }

    /**
     * Returns the priority order of this tiebreaker strategy.
     *
     * @return the tiebreaker order value
     */
    @Override
    public int getTieBreakerOrder() {
        return tieBreakerOrder;
    }

    /**
     * Computes the longest possible route of a Player
     *
     * @param player player to compute the longest route
     * @param board  current game board
     * @return maximum length of Player claimed route
     */
    private int computeLongestPath(Player player, Board board) {
        Map<City, List<Route>> adjacencyMap = new HashMap<>();

        for (Route route : board.getRoutes()) {
            if (!player.equals(route.getOwner())) {
                continue;
            }

            adjacencyMap
                    .computeIfAbsent(route.getCityA(), k -> new ArrayList<>())
                    .add(route);
            adjacencyMap
                    .computeIfAbsent(route.getCityB(), k -> new ArrayList<>())
                    .add(route);
        }

        int maxLength = 0;

        // DFS starting from every city
        for (City start : adjacencyMap.keySet()) {
            maxLength = Math.max(
                    maxLength,
                    dfs(start, adjacencyMap, new HashSet<>()));
        }

        return maxLength;
    }

    /**
     * Depth-First Search algorithm
     *
     * @param current      current City / source City
     * @param adjacencyMap adjacent vertices
     * @param used         Route visited before
     * @return longest path length from current city
     */
    private int dfs(City current, Map<City,
            List<Route>> adjacencyMap, Set<Route> used) {
        int max = 0;

        for (Route route : adjacencyMap.getOrDefault(current, List.of())) {
            if (used.contains(route)) {
                continue;
            }

            used.add(route);

            City next = route.getCityA().equals(current)
                    ? route.getCityB()
                    : route.getCityA();

            int length = route.getLength() + dfs(next, adjacencyMap, used);

            max = Math.max(max, length);

            used.remove(route);
        }

        return max;
    }
}
