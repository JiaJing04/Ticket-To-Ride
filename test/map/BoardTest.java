package map;

import game.character.Player;
import game.character.PlayerColour;
import game.map.Board;
import game.map.City;
import game.map.Route;
import game.map.RouteColour;
import game.strategy.routeclaiming.StandardRouteClaimStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Board test file
 *
 * @author Hew Jia Jing
 */
public class BoardTest {

    private Board board;
    private City cityA;
    private City cityB;
    private City cityC;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        board = new Board();

        cityA = new City("Penang", 1, 2);
        cityB = new City("Ipoh", 3, 4);
        cityC = new City("KL", 5, 6);

        player1 = new Player("Miranda", PlayerColour.RED, 10);
        player2 = new Player("Andy", PlayerColour.BLUE, 10);
    }

    private Route createRoute(String id, City from, City to) {
        return new Route(
                id,
                from,
                to,
                2,
                RouteColour.PINK,
                new StandardRouteClaimStrategy()
        );
    }

    @Test
    public void testAddCity_addsCityToBoard() {
        board.addCity(cityA);

        Assertions.assertEquals(1, board.getCities().size());
        Assertions.assertTrue(board.getCities().contains(cityA));
    }

    @Test
    public void testAddRoute_addsRouteToBoard() {
        Route route = createRoute("r1", cityA, cityB);

        board.addRoute(route);

        Assertions.assertEquals(1, board.getRoutes().size());
        Assertions.assertTrue(board.getRoutes().contains(route));
    }

    @Test
    public void testGetRouteById_whenExists_returnsRoute() {
        Route route = createRoute("r1", cityA, cityB);
        board.addRoute(route);

        Assertions.assertEquals(route, board.getRouteById("r1"));
    }

    @Test
    public void testGetRouteById_whenNotExists_returnsNull() {
        Assertions.assertNull(board.getRouteById("missing"));
    }

    @Test
    public void testGetCityByName_whenExists_returnsCityIgnoringCase() {
        board.addCity(cityA);

        Assertions.assertEquals(cityA, board.getCityByName("penang"));
        Assertions.assertEquals(cityA, board.getCityByName("PENANG"));
    }

    @Test
    public void testGetCityByName_whenNotExists_returnsNull() {
        board.addCity(cityA);

        Assertions.assertNull(board.getCityByName("London"));
    }

    @Test
    public void testGetAvailableRoutes_returnsOnlyUnclaimedRoutes() {
        Route route1 = createRoute("r1", cityA, cityB);
        Route route2 = createRoute("r2", cityB, cityC);

        route1.setOwner(player1);

        board.addRoute(route1);
        board.addRoute(route2);

        Assertions.assertEquals(1, board.getAvailableRoutes().size());
        Assertions.assertTrue(board.getAvailableRoutes().contains(route2));
        Assertions.assertFalse(board.getAvailableRoutes().contains(route1));
    }

    @Test
    public void testGetRoutesClaimedBy_returnsOnlyRoutesOwnedByPlayer() {
        Route route1 = createRoute("r1", cityA, cityB);
        Route route2 = createRoute("r2", cityB, cityC);
        Route route3 = createRoute("r3", cityA, cityC);

        route1.setOwner(player1);
        route2.setOwner(player2);

        board.addRoute(route1);
        board.addRoute(route2);
        board.addRoute(route3);

        Assertions.assertEquals(1, board.getRoutesClaimedBy(player1).size());
        Assertions.assertTrue(board.getRoutesClaimedBy(player1).contains(route1));
    }

    @Test
    public void testIsPathConnected_directClaimedRoute_returnsTrue() {
        Route route = createRoute("r1", cityA, cityB);
        route.setOwner(player1);
        board.addRoute(route);

        Assertions.assertTrue(board.isPathConnected(player1, cityA, cityB));
    }

    @Test
    public void testIsPathConnected_indirectClaimedPath_returnsTrue() {
        Route route1 = createRoute("r1", cityA, cityB);
        Route route2 = createRoute("r2", cityB, cityC);

        route1.setOwner(player1);
        route2.setOwner(player1);

        board.addRoute(route1);
        board.addRoute(route2);

        Assertions.assertTrue(board.isPathConnected(player1, cityA, cityC));
    }

    @Test
    public void testIsPathConnected_routeOwnedByDifferentPlayer_returnsFalse() {
        Route route1 = createRoute("r1", cityA, cityB);
        Route route2 = createRoute("r2", cityB, cityC);

        route1.setOwner(player1);
        route2.setOwner(player2);

        board.addRoute(route1);
        board.addRoute(route2);

        Assertions.assertFalse(board.isPathConnected(player1, cityA, cityC));
    }

    @Test
    public void testIsPathConnected_withNullInputs_returnsFalse() {
        Assertions.assertFalse(board.isPathConnected(null, cityA, cityB));
        Assertions.assertFalse(board.isPathConnected(player1, null, cityB));
        Assertions.assertFalse(board.isPathConnected(player1, cityA, null));
    }
}
