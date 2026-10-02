package routeClaimStrategy;

import game.card.CardColour;
import game.card.TrainCard;
import game.character.Player;
import game.character.PlayerColour;
import game.map.City;
import game.map.Route;
import game.map.RouteColour;
import game.strategy.routeclaiming.GrayRouteClaimStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class to test gray route claiming rule
 */
public class GrayRouteClaimingStrategyTest {
    private final GrayRouteClaimStrategy grayRouteClaimStrategy = new GrayRouteClaimStrategy();

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
        route = new Route("route", cityA, cityB, 3, RouteColour.GREY, null);

        player = new Player("Player", PlayerColour.RED, 10);
    }

    @Test
    public void testClaim_successWithOneColourOnly() {
        Assertions.assertTrue(grayRouteClaimStrategy.validate(route, player, trainCardToUse));
    }

    @Test
    public void testClaim_successMixingWildColour() {
        // to ensure the list length is aligned with route length
        trainCardToUse.removeFirst();

        TrainCard trainCard = new TrainCard(3, CardColour.WILD);
        trainCardToUse.add(trainCard);

        Assertions.assertTrue(grayRouteClaimStrategy.validate(route, player, trainCardToUse));
    }

    @Test
    public void testClaim_successWithWildOnly() {
        TrainCard trainCard = new TrainCard(1, CardColour.WILD);
        TrainCard trainCard1 = new TrainCard(2, CardColour.WILD);
        TrainCard trainCard2 = new TrainCard(3, CardColour.WILD);

        trainCardToUse = List.of(trainCard, trainCard1, trainCard2);

        Assertions.assertTrue(grayRouteClaimStrategy.validate(route, player, trainCardToUse));
    }

    @Test
    public void testClaim_failWithMultipleColour() {
        TrainCard trainCard = new TrainCard(1, CardColour.PINK);
        TrainCard trainCard1 = new TrainCard(2, CardColour.WILD);
        TrainCard trainCard2 = new TrainCard(3, CardColour.BLUE);

        trainCardToUse = List.of(trainCard, trainCard1, trainCard2);

        Assertions.assertFalse(grayRouteClaimStrategy.validate(route, player, trainCardToUse));
    }

    @Test
    public void testClaim_failWithInsufficientCards() {
        TrainCard trainCard = new TrainCard(1, CardColour.PINK);
        TrainCard trainCard1 = new TrainCard(2, CardColour.WILD);

        trainCardToUse = List.of(trainCard, trainCard1);

        Assertions.assertFalse(grayRouteClaimStrategy.validate(route, player, trainCardToUse));
    }

    @Test
    public void testClaim_failWithTooManyCards() {
        TrainCard trainCard = new TrainCard(1, CardColour.BLACK);
        trainCardToUse.add(trainCard);

        Assertions.assertFalse(grayRouteClaimStrategy.validate(route, player, trainCardToUse));
    }
}
