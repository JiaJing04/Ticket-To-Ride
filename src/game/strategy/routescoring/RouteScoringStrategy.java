package game.strategy.routescoring;

import game.map.Route;

/**
 * Interface for route scoring strategy
 *
 * @author Jia Ming
 */
public interface RouteScoringStrategy {

    /**
     * Returns the score awarded for claiming the given route.
     *
     * @param route the claimed route
     * @return the score for the Route
     */
    int score(Route route);
}
