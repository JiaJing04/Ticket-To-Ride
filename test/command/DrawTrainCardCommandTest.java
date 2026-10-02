package command;

import game.card.CardColour;
import game.card.TrainCard;
import game.character.Player;
import game.character.PlayerColour;
import game.command.ActionResult;
import game.command.DrawTrainCardCommand;
import game.deck.TrainCardDeck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DrawTrainCardCommandTest {
    private DrawTrainCardCommand drawTrainCardCommand;

    private TrainCardDeck trainCardDeck;

    private Player player;

    @BeforeEach
    public void setUp() {
        trainCardDeck = new TrainCardDeck();

        TrainCard trainCard1 = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard2 = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard3 = new TrainCard(1, CardColour.WILD);
        TrainCard trainCard4 = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard5 = new TrainCard(1, CardColour.BLACK);
        TrainCard trainCard6 = new TrainCard(1, CardColour.BLACK);

        trainCardDeck.addCard(trainCard1);
        trainCardDeck.addCard(trainCard2);
        trainCardDeck.addCard(trainCard3);
        trainCardDeck.addCard(trainCard4);
        trainCardDeck.addCard(trainCard5);
        trainCardDeck.addCard(trainCard6);

        trainCardDeck.initialiseFaceUpPile();

        player = new Player("Test", PlayerColour.BLACK, 10);
    }

    @Test
    public void testExecute_successDrawInDeck() {
        drawTrainCardCommand = new DrawTrainCardCommand(trainCardDeck.getDeckSlot(), trainCardDeck);
        ActionResult result = drawTrainCardCommand.execute(player);

        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals("Drew a train card.", result.getMessage());
        Assertions.assertEquals(1, player.getHand().getTrainCards().get(CardColour.BLACK).size());
    }

    @Test
    public void testExecute_successDrawFaceUp() {
        drawTrainCardCommand = new DrawTrainCardCommand(1, trainCardDeck);
        ActionResult result = drawTrainCardCommand.execute(player);

        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals("Drew a train card.", result.getMessage());
        Assertions.assertEquals(1, player.getHand().getTrainCards().get(CardColour.BLACK).size());
    }

    @Test
    public void testExecute_successDrawFaceUpLocomotive() {
        drawTrainCardCommand = new DrawTrainCardCommand(2, trainCardDeck);
        ActionResult result = drawTrainCardCommand.execute(player);

        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals("Drew a face-up locomotive card. No more actions can be done.", result.getMessage());
        Assertions.assertEquals(1, player.getHand().getTrainCards().get(CardColour.WILD).size());
    }

    @Test
    public void testExecute_failDrawFaceUp_slotInvalid() {
        drawTrainCardCommand = new DrawTrainCardCommand(10, trainCardDeck);
        ActionResult result = drawTrainCardCommand.execute(player);

        Assertions.assertFalse(result.isSuccess());
        Assertions.assertEquals("Invalid face-up card slot.", result.getMessage());
        Assertions.assertEquals(0, player.getHand().getTrainCards().get(CardColour.BLACK).size());
    }

    @Test
    public void testExecute_failDrawFaceUp_slotPileEmpty() {
        // draw slot 1 two times to let the Deck do not have enough cards to refill the slot
        drawTrainCardCommand = new DrawTrainCardCommand(1, trainCardDeck);
        drawTrainCardCommand.execute(player);

        drawTrainCardCommand = new DrawTrainCardCommand(1, trainCardDeck);
        drawTrainCardCommand.execute(player);

        // slot emptied due to insufficient cards in deck
        drawTrainCardCommand = new DrawTrainCardCommand(1, trainCardDeck);
        ActionResult result = drawTrainCardCommand.execute(player);

        Assertions.assertFalse(result.isSuccess());
        Assertions.assertEquals("Invalid face-up card slot.", result.getMessage());
        Assertions.assertEquals(2, player.getHand().getTrainCards().get(CardColour.BLACK).size());
    }

    @Test
    public void testExecute_failDrawInDeck_deckEmpty() {
        trainCardDeck = new TrainCardDeck();
        drawTrainCardCommand = new DrawTrainCardCommand(trainCardDeck.getDeckSlot(), trainCardDeck);
        ActionResult result = drawTrainCardCommand.execute(player);

        Assertions.assertFalse(result.isSuccess());
        Assertions.assertEquals("No train card is available to draw.", result.getMessage());
        Assertions.assertEquals(0, player.getHand().getTrainCards().get(CardColour.BLACK).size());
    }
}
