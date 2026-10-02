package map;

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

import java.util.List;

/**
 * Route test file
 *
 * @author Hew Jia Jing
 */
public class RouteTest {

    private City cityA;
    private City cityB;
    private Route route;
    private Player player;

    @BeforeEach
    public void setUp() {
        cityA = new City("KL", 1, 2);
        cityB = new City("Penang", 3, 4);

        route = new Route(
                "r1",
                cityA,
                cityB,
                2,
                RouteColour.PINK,
                new StandardRouteClaimStrategy()
        );

        player = new Player("Miranda", PlayerColour.RED, 10);
    }

    @Test
    public void testConstructor_initialisesRouteCorrectly() {
        Assertions.assertEquals("r1", route.getId());
        Assertions.assertEquals(cityA, route.getCityA());
        Assertions.assertEquals(cityB, route.getCityB());
        Assertions.assertEquals(2, route.getLength());
        Assertions.assertEquals(RouteColour.PINK, route.getColour());
        Assertions.assertFalse(route.isClaimed());
        Assertions.assertNull(route.getOwner());
    }

    @Test
    public void testSetOwner_marksRouteAsClaimed() {
        route.setOwner(player);

        Assertions.assertEquals(player, route.getOwner());
        Assertions.assertTrue(route.isClaimed());
    }

    @Test
    public void testConnects_sameDirection_returnsTrue() {
        Assertions.assertTrue(route.connects(cityA, cityB));
    }

    @Test
    public void testConnects_reverseDirection_returnsTrue() {
        Assertions.assertTrue(route.connects(cityB, cityA));
    }

    @Test
    public void testConnects_unrelatedCity_returnsFalse() {
        City cityC = new City("Ipoh", 5, 6);

        Assertions.assertFalse(route.connects(cityA, cityC));
    }

    @Test
    public void testCanClaim_whenValid_returnsTrue() {
        TrainCard card1 = new TrainCard(1, CardColour.PINK);
        TrainCard card2 = new TrainCard(2, CardColour.PINK);

        player.getHand().addTrainCards(List.of(card1, card2));

        Assertions.assertTrue(route.canClaim(player, List.of(card1, card2)));
    }

    @Test
    public void testCanClaim_whenAlreadyClaimed_returnsFalse() {
        TrainCard card1 = new TrainCard(1, CardColour.PINK);
        TrainCard card2 = new TrainCard(2, CardColour.PINK);

        route.setOwner(player);

        Assertions.assertFalse(route.canClaim(player, List.of(card1, card2)));
    }

    @Test
    public void testCanClaim_whenNotEnoughTrainPieces_returnsFalse() {
        Player poorPlayer = new Player("Andy", PlayerColour.BLUE, 1);

        TrainCard card1 = new TrainCard(1, CardColour.PINK);
        TrainCard card2 = new TrainCard(2, CardColour.PINK);

        poorPlayer.getHand().addTrainCards(List.of(card1, card2));

        Assertions.assertFalse(route.canClaim(poorPlayer, List.of(card1, card2)));
    }

    @Test
    public void testCanClaim_whenInvalidCardColour_returnsFalse() {
        TrainCard card1 = new TrainCard(1, CardColour.BLUE);
        TrainCard card2 = new TrainCard(2, CardColour.BLUE);

        player.getHand().addTrainCards(List.of(card1, card2));

        Assertions.assertFalse(route.canClaim(player, List.of(card1, card2)));
    }
}
