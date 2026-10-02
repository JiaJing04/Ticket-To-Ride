package game.map;

import game.character.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of parallel routes connecting the same two cities.
 *
 * @author Ng Zi Wei
 */
public class RouteGroup {

    /**
     * The list of parallel routes in this route group.
     */
    private final List<Route> routeInParallel;

    /**
     * Number of players in the current game
     */
    private final int playerCount;

    /**
     * Constructs a new RouteGroup
     *
     * @param playerCount the number of players in game
     */
    public RouteGroup(int playerCount) {
        this.routeInParallel = new ArrayList<>();
        this.playerCount = playerCount;
    }

    /**
     * Adds a route to this group and links it back to this RouteGroup.
     *
     * @param route the Route to be added
     */
    public void addRoute(Route route) {
        this.routeInParallel.add(route);
        route.setRouteGroup(this);
    }

    /**
     * Checks whether a player can claim a route in this group, based on
     * parallel route and player-count restrictions.
     *
     * @param player      the player attempting to claim the route
     * @param targetRoute the route being claimed
     * @return true if the route can be legally claimed
     */
    public boolean canClaim(Player player, Route targetRoute) {

        for (Route sibling : routeInParallel) {

            // Skip checking the route against itself
            if (sibling == targetRoute) {
                continue;
            }

            // A player cannot own both parallel routes
            if (sibling.isClaimed() && sibling.getOwner() == player) {
                return false;
            }

            // In games with 3 or fewer players, parallel routes are locked
            if (this.playerCount <= 3 && sibling.isClaimed()) {
                return false;
            }
        }

        return true;
    }
}
