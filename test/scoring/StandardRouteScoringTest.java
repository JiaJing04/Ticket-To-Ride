package scoring;

import game.map.City;
import game.map.Route;
import game.map.RouteColour;
import game.strategy.routescoring.StandardRouteScoring;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StandardRouteScoringTest {
    private final StandardRouteScoring standardRouteScoring = new StandardRouteScoring();

    /**
     * Test score of a route based on route length
     */
    @Test
    public void testScore_routeLength() {
        City cityA = new City("A", 1, 2);
        City cityB = new City("B", 2, 3);

        Route routeLength1 = new Route("test-route", cityA, cityB, 1, RouteColour.BLACK, null);
        Assertions.assertEquals(1, standardRouteScoring.score(routeLength1));

        Route routeLength2 = new Route("test-route", cityA, cityB, 2, RouteColour.BLACK, null);
        Assertions.assertEquals(2, standardRouteScoring.score(routeLength2));

        Route routeLength3 = new Route("test-route", cityA, cityB, 3, RouteColour.BLACK, null);
        Assertions.assertEquals(4, standardRouteScoring.score(routeLength3));

        Route routeLength4 = new Route("test-route", cityA, cityB, 4, RouteColour.BLACK, null);
        Assertions.assertEquals(7, standardRouteScoring.score(routeLength4));

        Route routeLength5 = new Route("test-route", cityA, cityB, 5, RouteColour.BLACK, null);
        Assertions.assertEquals(10, standardRouteScoring.score(routeLength5));

        Route routeLength6 = new Route("test-route", cityA, cityB, 6, RouteColour.BLACK, null);
        Assertions.assertEquals(15, standardRouteScoring.score(routeLength6));
    }
}
