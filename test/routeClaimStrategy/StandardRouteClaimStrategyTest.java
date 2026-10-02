package routeClaimStrategy;

import game.card.CardColour;
import game.card.TrainCard;
import game.character.Player;
import game.character.PlayerColour;
import game.map.City;
import game.map.Route;
import game.map.RouteColour;
import game.strategy.routeclaiming.StandardRouteClaimStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class to test standard route claiming validation
 *
 * @author Jia Ming
 */
public class StandardRouteClaimStrategyTest {
    private final StandardRouteClaimStrategy standardRouteClaimStrategy = new StandardRouteClaimStrategy();

    private List<TrainCard> trainCardToUse;

    private Route route;

    private Player player;

    @BeforeEach
    public void setUp() {
        TrainCard trainCard = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard1 = new TrainCard(2, CardColour.BLACK);
        TrainCard trainCard2 = new TrainCard(3, CardColour.BLACK);
        trainCardToUse = new ArrayList<>();
        trainCardToUse.add(trainCard);
        trainCardToUse.add(trainCard1);
        trainCardToUse.add(trainCard2);

        City cityA = new City("CityA", 1, 2);
        City cityB = new City("CityB", 3, 4);
        route = new Route("route", cityA, cityB, 3, RouteColour.BLACK, null);

        player = new Player("Player", PlayerColour.RED, 10);
    }

    @Test
    public void testClaim_successWithoutWild() {
        Assertions.assertTrue(standardRouteClaimStrategy.validate(route, player, trainCardToUse));
    }

    @Test
    public void testClaim_successWithWild() {
        trainCardToUse.removeFirst();
        trainCardToUse.add(new TrainCard(1, CardColour.WILD));

        Assertions.assertTrue(standardRouteClaimStrategy.validate(route, player, trainCardToUse));
    }

    @Test
    public void testClaim_successWithAllWild() {
        trainCardToUse = List.of(
                new TrainCard(1, CardColour.WILD),
                new TrainCard(2, CardColour.WILD),
                new TrainCard(3, CardColour.WILD)
        );

        Assertions.assertTrue(standardRouteClaimStrategy.validate(route, player, trainCardToUse));
    }

    @Test
    public void testClaim_failWithInvalidColour() {
        TrainCard trainCard = new TrainCard(1, CardColour.PINK);
        TrainCard trainCard1 = new TrainCard(2, CardColour.PINK);
        TrainCard trainCard2 = new TrainCard(3, CardColour.PINK);
        trainCardToUse = List.of(trainCard1, trainCard2, trainCard);

        Assertions.assertFalse(standardRouteClaimStrategy.validate(route, player, trainCardToUse));
    }

    @Test
    public void testClaim_failWithMultipleColour() {
        TrainCard trainCard = new TrainCard(1, CardColour.PINK);
        TrainCard trainCard1 = new TrainCard(2, CardColour.BLUE);
        TrainCard trainCard2 = new TrainCard(3, CardColour.PINK);
        trainCardToUse = List.of(trainCard1, trainCard2, trainCard);

        Assertions.assertFalse(standardRouteClaimStrategy.validate(route, player, trainCardToUse));
    }

    @Test
    public void testClaim_failWithInsufficientCards() {
        trainCardToUse.removeFirst();
        Assertions.assertFalse(standardRouteClaimStrategy.validate(route, player, trainCardToUse));
    }

    @Test
    public void testClaim_failWithTooManyCards() {
        trainCardToUse.add(new TrainCard(1, CardColour.BLACK));
        Assertions.assertFalse(standardRouteClaimStrategy.validate(route, player, trainCardToUse));
    }
}
