package command;

import game.card.DestinationCard;
import game.character.Player;
import game.character.PlayerColour;
import game.command.ActionCost;
import game.command.ActionResult;
import game.command.ActionStatus;
import game.command.DrawDestCommand;
import game.deck.DestinationCardDeck;
import game.map.City;
import game.pendingdecision.TicketDecision;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DrawDestCommandTest {

    private DrawDestCommand drawDestCommand;
    private DestinationCardDeck destinationCardDeck;
    private Player player;

    @BeforeEach
    public void setUp() {
        destinationCardDeck = new DestinationCardDeck();

        City cityA = new City("Ipoh", 0, 0);
        City cityB = new City("Penang", 1, 1);
        City cityC = new City("KL", 2, 2);
        City cityD = new City("Selangor", 3, 3);

        destinationCardDeck.addCard(new DestinationCard(1, cityA, cityB, 5));
        destinationCardDeck.addCard(new DestinationCard(2, cityB, cityC, 7));
        destinationCardDeck.addCard(new DestinationCard(3, cityC, cityD, 9));

        player = new Player("Emily", PlayerColour.BLACK, 10);
    }

    @Test
    public void testExecute_successDrawTickets() {
        drawDestCommand = new DrawDestCommand(destinationCardDeck, 1);

        ActionResult result = drawDestCommand.execute(player);

        Assertions.assertEquals(ActionStatus.PENDING, result.getStatus());
        Assertions.assertFalse(result.isSuccess());
        Assertions.assertEquals("Drew tickets. Please select which to keep", result.getMessage());
        Assertions.assertTrue(result.hasPendingDecision());
        Assertions.assertNotNull(result.getPendingDecision());
        Assertions.assertTrue(result.getPendingDecision() instanceof TicketDecision);
    }

    @Test
    public void testExecute_failDeckEmpty() {
        destinationCardDeck = new DestinationCardDeck();
        drawDestCommand = new DrawDestCommand(destinationCardDeck, 1);

        ActionResult result = drawDestCommand.execute(player);

        Assertions.assertFalse(result.isSuccess());
        Assertions.assertEquals("The destination ticket deck is empty!", result.getMessage());
    }

    @Test
    public void testGetCost_returnsFullActionCost() {
        drawDestCommand = new DrawDestCommand(destinationCardDeck, 1);

        Assertions.assertEquals(ActionCost.FULL, drawDestCommand.getCost());
    }

}
