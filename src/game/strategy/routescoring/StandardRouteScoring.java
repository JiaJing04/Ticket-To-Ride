package game.strategy.routescoring;

import game.map.Route;

import java.util.Map;

/**
 * Route scoring strategy for standard score table
 *
 * @author Jia Ming
 */
public class StandardRouteScoring implements RouteScoringStrategy {

    /**
     * Score table map (key, value) = (length of route, score of route)
     */
    private static final Map<Integer, Integer> ROUTE_SCORE_MAP = Map.of(
            1, 1,
            2, 2,
            3, 4,
            4, 7,
            5, 10,
            6, 15
    );

    /**
     * Maps route lengths to their corresponding score values.
     *
     * @param route the claimed route
     * @return the score based on the route length
     */
    @Override
    public int score(Route route) {
        return ROUTE_SCORE_MAP.get(route.getLength());
    }
}
