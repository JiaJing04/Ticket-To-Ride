package character;

import game.card.CardColour;
import game.card.DestinationCard;
import game.card.TrainCard;
import game.character.Player;
import game.character.PlayerColour;
import game.map.City;
import game.map.Route;
import game.map.RouteColour;
import game.strategy.routeclaiming.StandardRouteClaimStrategy;
import game.strategy.routescoring.StandardRouteScoring;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Player test file
 *
 * @author Hew Jia Jing
 */
public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("Andy", PlayerColour.RED, 10);
    }

    @Test
    public void testConstructor_initialisesPlayerCorrectly() {
        Assertions.assertEquals("Andy", player.getName());
        Assertions.assertEquals(PlayerColour.RED, player.getColour());
        Assertions.assertEquals(10, player.getRemainingTrainPieces());
        Assertions.assertEquals(0, player.getScore());
        Assertions.assertNotNull(player.getHand());
    }

    @Test
    public void testAddScore_increasesScore() {
        player.addScore(5);

        Assertions.assertEquals(5, player.getScore());
    }

    @Test
    public void testDeductScore_decreasesScore() {
        player.addScore(10);
        player.deductScore(4);

        Assertions.assertEquals(6, player.getScore());
    }

    @Test
    public void testHasEnoughTrainPieces_whenEnough_returnsTrue() {
        Assertions.assertTrue(player.hasEnoughTrainPieces(5));
        Assertions.assertTrue(player.hasEnoughTrainPieces(10));
    }

    @Test
    public void testHasEnoughTrainPieces_whenNotEnough_returnsFalse() {
        Assertions.assertFalse(player.hasEnoughTrainPieces(11));
    }

    @Test
    public void testReduceTrainPieces_decreasesRemainingPieces() {
        player.reduceTrainPieces(3);

        Assertions.assertEquals(7, player.getRemainingTrainPieces());
    }

    @Test
    public void testDrawTrainCard_addsCardToHand() {
        TrainCard card = new TrainCard(1, CardColour.BLUE);

        player.drawTrainCard(card);

        Assertions.assertEquals(1,
                player.getHand().getTrainCardCountByColour(CardColour.BLUE));
    }

    @Test
    public void testDrawDestCard_addsDestinationCardsToHand() {
        City cityA = new City("A", 1, 2);
        City cityB = new City("B", 3, 4);

        DestinationCard card1 = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard card2 = new DestinationCard(2, cityA, cityB, 8);

        player.drawDestCard(List.of(card1, card2));

        Assertions.assertEquals(2,
                player.getHand().getDestinationCards().size());
        Assertions.assertTrue(
                player.getHand().getDestinationCards().contains(card1));
        Assertions.assertTrue(
                player.getHand().getDestinationCards().contains(card2));
    }

    @Test
    public void testClaimRoute_updatesPlayerRouteAndHandState() {
        City cityA = new City("A", 1, 2);
        City cityB = new City("B", 3, 4);

        Route route = new Route(
                "route1",
                cityA,
                cityB,
                3,
                RouteColour.BLUE,
                new StandardRouteClaimStrategy()
        );

        TrainCard card1 = new TrainCard(1, CardColour.BLUE);
        TrainCard card2 = new TrainCard(2, CardColour.BLUE);
        TrainCard card3 = new TrainCard(3, CardColour.BLUE);

        List<TrainCard> cardsToUse = List.of(card1, card2, card3);
        player.getHand().addTrainCards(cardsToUse);

        player.claimRoute(route, cardsToUse, new StandardRouteScoring());

        Assertions.assertEquals(player, route.getOwner());
        Assertions.assertTrue(route.isClaimed());
        Assertions.assertEquals(7, player.getRemainingTrainPieces());
        Assertions.assertEquals(0,
                player.getHand().getTrainCardCountByColour(CardColour.BLUE));

        Assertions.assertTrue(player.getScore() > 0);
    }
}
