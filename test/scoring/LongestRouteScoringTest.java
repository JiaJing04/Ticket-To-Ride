package scoring;

import game.character.Player;
import game.character.PlayerColour;
import game.map.Board;
import game.map.City;
import game.map.Route;
import game.endgame.LongestRouteScoring;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Class to test LongestRouteScoring
 *
 * @author Jia Ming
 */
public class LongestRouteScoringTest {
    private LongestRouteScoring longestRouteScoring;

    private Player playerA;

    private Player playerB;

    private Route routeA;

    private Route routeB;

    private final int LONGEST_TIE_BREAKER_ORDER = 2;

    @BeforeEach
    public void setUp() {
        longestRouteScoring = new LongestRouteScoring(LONGEST_TIE_BREAKER_ORDER);
        playerA = new Player("A", PlayerColour.BLACK, 10);
        playerB = new Player("B", PlayerColour.RED, 10);

        City cityA = new City("A", 1, 2);
        City cityB = new City("B", 3, 4);
        routeA = new Route("route", cityA, cityB, 5, null, null);

        City cityC = new City("C", 1, 2);
        City cityD = new City("D", 3, 4);
        routeB = new Route("route", cityC, cityD, 4, null, null);
    }

    @Test
    public void testApply_bonusAppliedSuccessful() {
        Board board = new Board();
        routeA.setOwner(playerA);
        routeB.setOwner(playerB);

        board.addRoute(routeA);
        board.addRoute(routeB);

        longestRouteScoring.apply(List.of(playerA, playerB), board);

        // routeA has length 5
        // routeB has length 4
        Assertions.assertEquals(10 , playerA.getScore());
        Assertions.assertEquals(0 , playerB.getScore());
    }

    @Test
    public void testApply_bonusAppliedSuccessful_withPlayerHasInitialScore() {
        Board board = new Board();
        routeA.setOwner(playerA);
        routeB.setOwner(playerB);

        board.addRoute(routeA);
        board.addRoute(routeB);

        playerA.addScore(10);
        playerB.addScore(10);

        longestRouteScoring.apply(List.of(playerA, playerB), board);

        Assertions.assertEquals(20 , playerA.getScore());
        Assertions.assertEquals(10 , playerB.getScore());
    }

    @Test
    public void testApply_bonusAppliedSuccessfulForAllLongestRoute() {
        Board board = new Board();
        City cityA = new City("A", 1, 2);
        City cityB = new City("B", 3, 4);
        Route route = new Route("route", cityA, cityB, 5, null, null);
        route.setOwner(playerA);
        routeA.setOwner(playerB);

        board.addRoute(route);
        board.addRoute(routeA);

        longestRouteScoring.apply(List.of(playerA, playerB), board);

        // route has length 5
        // routeA has length 5
        Assertions.assertEquals(10 , playerA.getScore());
        Assertions.assertEquals(10 , playerB.getScore());
    }

    @Test
    public void testApply_MultiplePathGetLongest() {
        Board board = new Board();
        City cityA = new City("A", 1, 2);
        City cityB = new City("B", 3, 4);
        City cityC = new City("C", 5, 6);
        City cityD = new City("D", 7, 8);
        City cityE = new City("E", 9, 10);
        City cityF = new City("F", 11, 12);

        Route routeAB = new Route("route", cityA, cityB, 5, null, null);
        Route routeBC = new Route("route", cityB, cityC, 4, null, null);
        Route routeCD = new Route("route", cityC, cityD, 6, null, null);
        Route routeDA = new Route("route", cityD, cityA, 1, null, null);
        Route routeDE = new Route("route", cityD, cityE, 5, null, null);
        Route routeAF = new Route("route", cityA, cityF, 7, null, null);

        // two possible paths
        // 1. AB -> BC -> CD -> DA -> AF (length 23)
        // 2. AB -> BC -> CD -> DE (length 20)
        routeAB.setOwner(playerA);
        routeBC.setOwner(playerA);
        routeCD.setOwner(playerA);
        routeDA.setOwner(playerA);
        routeDE.setOwner(playerA);
        routeAF.setOwner(playerA);

        City cityMock = new City("M", 1, 2);
        Route routeMock = new Route("route", cityA, cityMock, 20, null, null);
        routeMock.setOwner(playerB);

        board.addRoute(routeAB);
        board.addRoute(routeBC);
        board.addRoute(routeCD);
        board.addRoute(routeDA);
        board.addRoute(routeDE);
        board.addRoute(routeAF);
        board.addRoute(routeMock);

        longestRouteScoring.apply(List.of(playerA, playerB), board);

        // playerA has longest possible path = 23
        // playerB has longest possible path = 20 (same as playerA second longest possible path)
        Assertions.assertEquals(10 , playerA.getScore());
        Assertions.assertEquals(0 , playerB.getScore());
    }

    @Test
    public void testTieBreaker_success() throws NoSuchFieldException, IllegalAccessException {
        Field field2 = LongestRouteScoring.class.getDeclaredField("playersWithLongestRoute");
        field2.setAccessible(true);
        field2.set(longestRouteScoring, List.of(playerA));

        List<Player> winnerList = longestRouteScoring.getTieBreaker(List.of(playerA, playerB), new Board());
        Assertions.assertEquals(playerA, winnerList.get(0));
        Assertions.assertEquals(1, winnerList.size());
    }
}
