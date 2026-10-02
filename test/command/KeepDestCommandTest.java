package command;

import game.card.DestinationCard;
import game.character.Player;
import game.character.PlayerColour;
import game.command.*;
import game.deck.DestinationCardDeck;
import game.map.City;
import org.junit.jupiter.api.*;

import java.util.List;

public class KeepDestCommandTest {

    private DestinationCardDeck destinationCardDeck;
    private Player player;

    private DestinationCard card1;
    private DestinationCard card2;
    private DestinationCard card3;

    @BeforeEach
    public void setUp() {
        destinationCardDeck = new DestinationCardDeck();

        City cityA = new City("Ipoh", 0, 0);
        City cityB = new City("Penang", 1, 1);
        City cityC = new City("KL", 2, 2);

        card1 = new DestinationCard(1, cityA, cityB, 5);
        card2 = new DestinationCard(2, cityB, cityC, 7);
        card3 = new DestinationCard(3, cityA, cityC, 10);

        destinationCardDeck.addCard(card1);
        destinationCardDeck.addCard(card2);
        destinationCardDeck.addCard(card3);

        player = new Player("Miranda", PlayerColour.BLACK, 10);
    }

    @Test
    public void testExecute_successKeepDestinationCards() {
        KeepDestCommand command = new KeepDestCommand(
                List.of(1, 2),
                List.of(3),
                destinationCardDeck,
                false
        );

        ActionResult result = command.execute(player);

        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals(ActionStatus.SUCCESS, result.getStatus());
        Assertions.assertEquals("Successfully draw 2 destination cards.", result.getMessage());
    }

    @Test
    public void testExecute_failKeepLessThanMinimum() {
        KeepDestCommand command = new KeepDestCommand(
                List.of(),
                List.of(1, 2, 3),
                destinationCardDeck,
                false
        );

        ActionResult result = command.execute(player);

        Assertions.assertFalse(result.isSuccess());
        Assertions.assertEquals(ActionStatus.PENDING_FAILED, result.getStatus());
        Assertions.assertEquals("You must keep at least 1 destination card(s).", result.getMessage());
    }

    @Test
    public void testGetCost_returnsFreeActionCost() {
        KeepDestCommand command = new KeepDestCommand(
                List.of(1),
                List.of(2, 3),
                destinationCardDeck,
                false
        );

        Assertions.assertEquals(ActionCost.FREE, command.getCost());
    }
}
