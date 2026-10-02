package command;

import game.card.CardColour;
import game.card.TrainCard;
import game.character.Player;
import game.character.PlayerColour;
import game.command.ActionResult;
import game.command.ClaimRouteCommand;
import game.deck.TrainCardDeck;
import game.map.City;
import game.map.Route;
import game.map.RouteColour;
import game.strategy.routeclaiming.GrayRouteClaimStrategy;
import game.strategy.routeclaiming.StandardRouteClaimStrategy;
import game.strategy.routescoring.RouteScoringStrategy;
import game.strategy.routescoring.StandardRouteScoring;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * ClaimRouteCommand test file
 *
 * @author Jia Ming
 */
public class ClaimRouteCommandTest {

    private Route route;

    private Player player;

    private TrainCardDeck deck;

    private RouteScoringStrategy scoringStrategy;

    private List<TrainCard> trainCardToUse;

    @BeforeEach
    public void setUp() {
        City cityA = new City("A", 1, 2);
        City cityB = new City("B", 3, 4);
        route = new Route("route", cityA, cityB, 5, RouteColour.BLACK, new StandardRouteClaimStrategy());

        player = new Player("player", PlayerColour.RED, 10);
        deck = new TrainCardDeck();
        scoringStrategy = new StandardRouteScoring();

        TrainCard trainCard1 = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard2 = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard3 = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard4 = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard5 = new TrainCard(1, CardColour.BLACK);
        trainCardToUse = List.of(trainCard1, trainCard2, trainCard3, trainCard4, trainCard5);
        player.getHand().addTrainCards(trainCardToUse);
    }

    @Test
    public void testExecute_success() {
        ClaimRouteCommand command = new ClaimRouteCommand(route, trainCardToUse, deck, scoringStrategy);
        ActionResult result = command.execute(player);

        // result
        Assertions.assertTrue(result.isSuccess());

        // player state changes
        Assertions.assertEquals(10, player.getScore());
        Assertions.assertEquals(5, player.getRemainingTrainPieces());
        Assertions.assertEquals(0, player.getHand().getTrainCards().get(CardColour.BLACK).size());

        // route state changes
        Assertions.assertEquals(player, route.getOwner());
        Assertions.assertTrue(route.isClaimed());

        testDiscardedByReshuffling();
    }

    @Test
    public void testExecute_successGrayRoute() {
        City cityA = new City("A", 1, 2);
        City cityB = new City("B", 3, 4);
        route = new Route("route", cityA, cityB, 5, RouteColour.GREY, new GrayRouteClaimStrategy());

        ClaimRouteCommand command = new ClaimRouteCommand(route, trainCardToUse, deck, scoringStrategy);
        ActionResult result = command.execute(player);

        // result
        Assertions.assertTrue(result.isSuccess());

        // player state changes
        Assertions.assertEquals(10, player.getScore());
        Assertions.assertEquals(5, player.getRemainingTrainPieces());
        Assertions.assertEquals(0, player.getHand().getTrainCards().get(CardColour.BLACK).size());

        // route state changes
        Assertions.assertEquals(player, route.getOwner());
        Assertions.assertTrue(route.isClaimed());

        testDiscardedByReshuffling();
    }

    @Test
    public void testExecute_invalidTrainCardColour() {
        TrainCard trainCard1 = new TrainCard(1, CardColour.BLUE);
        TrainCard trainCard2 = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard3 = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard4 = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard5 = new TrainCard(1, CardColour.BLACK);
        trainCardToUse = List.of(trainCard1, trainCard2, trainCard3, trainCard4, trainCard5);

        ClaimRouteCommand command = new ClaimRouteCommand(route, trainCardToUse, deck, scoringStrategy);
        ActionResult result = command.execute(player);

        // result
        Assertions.assertFalse(result.isSuccess());

        // player state changes
        Assertions.assertEquals(0, player.getScore());
        Assertions.assertEquals(10, player.getRemainingTrainPieces());
        Assertions.assertEquals(5, player.getHand().getTrainCards().get(CardColour.BLACK).size());

        // route state changes
        Assertions.assertNull(route.getOwner());
        Assertions.assertFalse(route.isClaimed());

        testDiscardedEmpty();
    }

    @Test
    public void testExecute_invalidTrainPieces() {
        // remaining train piece to be 2
        // invalid claim as route length = 5
        player.reduceTrainPieces(8);

        ClaimRouteCommand command = new ClaimRouteCommand(route, trainCardToUse, deck, scoringStrategy);
        ActionResult result = command.execute(player);

        // result
        Assertions.assertFalse(result.isSuccess());

        // player state changes
        Assertions.assertEquals(0, player.getScore());
        Assertions.assertEquals(2, player.getRemainingTrainPieces());
        Assertions.assertEquals(5, player.getHand().getTrainCards().get(CardColour.BLACK).size());

        // route state changes
        Assertions.assertNull(route.getOwner());
        Assertions.assertFalse(route.isClaimed());

        testDiscardedEmpty();
    }

    @Test
    public void testExecute_invalid_routeIsClaimed() {
        route.setOwner(new Player("test", PlayerColour.BLACK, 1));

        ClaimRouteCommand command = new ClaimRouteCommand(route, trainCardToUse, deck, scoringStrategy);
        ActionResult result = command.execute(player);

        // result
        Assertions.assertFalse(result.isSuccess());

        // player state changes
        Assertions.assertEquals(0, player.getScore());
        Assertions.assertEquals(10, player.getRemainingTrainPieces());
        Assertions.assertEquals(5, player.getHand().getTrainCards().get(CardColour.BLACK).size());

        testDiscardedEmpty();
    }

    /**
     * test for discarded pile having the train cards when claimed routes
     */
    private void testDiscardedByReshuffling() {
        TrainCard card = deck.drawBySlot(-1);

        Assertions.assertEquals(4, deck.getInDeckPile().size());
    }

    /**
     * test for discarded pile does not contain any card when claim route is invalid
     */
    private void testDiscardedEmpty() {
        TrainCard card = deck.drawBySlot(-1);

        Assertions.assertEquals(0, deck.getInDeckPile().size());
    }
}
