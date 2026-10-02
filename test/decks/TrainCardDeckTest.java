package decks;

import game.card.CardColour;
import game.card.TrainCard;
import game.deck.TrainCardDeck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Train Card Deck test file
 *
 * @author Hew Jia Jing
 */
public class TrainCardDeckTest {

    @Test
    public void testConstructor_emptyDeck_faceUpPileHasFiveNullSlots() {
        TrainCardDeck deck = new TrainCardDeck();

        Assertions.assertTrue(deck.isInDeckEmpty());
        Assertions.assertTrue(deck.isFaceUpEmpty());
        Assertions.assertEquals(5, deck.getFaceUpPile().size());
    }

    @Test
    public void testConstructor_withCards_initialisesDeckAndFaceUpPile() {
        TrainCard red = new TrainCard(1, CardColour.PINK);
        TrainCard blue = new TrainCard(2, CardColour.BLUE);
        TrainCard green = new TrainCard(3, CardColour.GREEN);

        TrainCardDeck deck = new TrainCardDeck(
                List.of(red),
                List.of(blue, green)
        );

        Assertions.assertEquals(1, deck.getInDeckPile().size());
        Assertions.assertEquals(blue, deck.getFaceUpPile().get(0));
        Assertions.assertEquals(green, deck.getFaceUpPile().get(1));
        Assertions.assertNull(deck.getFaceUpPile().get(2));
        Assertions.assertEquals(5, deck.getFaceUpPile().size());
    }

    @Test
    public void testGetDeckSlot_returnsNegativeOne() {
        TrainCardDeck deck = new TrainCardDeck();

        Assertions.assertEquals(-1, deck.getDeckSlot());
    }

    @Test
    public void testCanDraw_fromDeckSlot_returnsTrueEvenWhenDeckEmpty() {
        TrainCardDeck deck = new TrainCardDeck();

        Assertions.assertTrue(deck.canDraw(deck.getDeckSlot()));
    }

    @Test
    public void testDrawBySlot_fromEmptyDeck_returnsNull() {
        TrainCardDeck deck = new TrainCardDeck();

        TrainCard drawn = deck.drawBySlot(deck.getDeckSlot());

        Assertions.assertNull(drawn);
    }

    @Test
    public void testDrawBySlot_fromDeck_returnsTopCard() {
        TrainCard red = new TrainCard(1, CardColour.PINK);
        TrainCard blue = new TrainCard(2, CardColour.BLUE);

        TrainCardDeck deck = new TrainCardDeck(
                List.of(red, blue),
                List.of()
        );

        TrainCard drawn = deck.drawBySlot(deck.getDeckSlot());

        Assertions.assertEquals(red, drawn);
        Assertions.assertEquals(1, deck.getInDeckPile().size());
        Assertions.assertEquals(blue, deck.getInDeckPile().get(0));
    }

    @Test
    public void testCanDraw_invalidFaceUpSlot_returnsFalse() {
        TrainCardDeck deck = new TrainCardDeck();

        Assertions.assertFalse(deck.canDraw(-2));
        Assertions.assertFalse(deck.canDraw(5));
        Assertions.assertFalse(deck.canDraw(99));
    }

    @Test
    public void testPeekFaceUp_invalidSlot_returnsNull() {
        TrainCardDeck deck = new TrainCardDeck();

        Assertions.assertNull(deck.peekFaceUp(-1));
        Assertions.assertNull(deck.peekFaceUp(5));
        Assertions.assertNull(deck.peekFaceUp(99));
    }

    @Test
    public void testPeekFaceUp_validSlotWithCard_returnsCardWithoutRemoving() {
        TrainCard red = new TrainCard(1, CardColour.PINK);

        TrainCardDeck deck = new TrainCardDeck(
                List.of(),
                List.of(red)
        );

        TrainCard peeked = deck.peekFaceUp(0);

        Assertions.assertEquals(red, peeked);
        Assertions.assertEquals(red, deck.getFaceUpPile().get(0));
    }

    @Test
    public void testDrawBySlot_validFaceUpSlot_returnsCardAndReplacesWithNullWhenDeckEmpty() {
        TrainCard red = new TrainCard(1, CardColour.PINK);

        TrainCardDeck deck = new TrainCardDeck(
                List.of(),
                List.of(red)
        );

        TrainCard drawn = deck.drawBySlot(0);

        Assertions.assertEquals(red, drawn);
        Assertions.assertNull(deck.getFaceUpPile().get(0));
    }

    @Test
    public void testDrawBySlot_validFaceUpSlot_replacesCardFromDeck() {
        TrainCard faceUp = new TrainCard(1, CardColour.PINK);
        TrainCard replacement = new TrainCard(2, CardColour.BLUE);

        TrainCardDeck deck = new TrainCardDeck(
                List.of(replacement),
                List.of(faceUp)
        );

        TrainCard drawn = deck.drawBySlot(0);

        Assertions.assertEquals(faceUp, drawn);
        Assertions.assertEquals(replacement, deck.getFaceUpPile().get(0));
        Assertions.assertTrue(deck.isInDeckEmpty());
    }

    @Test
    public void testDiscard_ignoresNullCards() {
        TrainCard red = new TrainCard(1, CardColour.PINK);

        TrainCardDeck deck = new TrainCardDeck();

        List<TrainCard> cards = new java.util.ArrayList<>();
        cards.add(red);
        cards.add(null);

        deck.discard(cards);

        TrainCard drawn = deck.drawBySlot(deck.getDeckSlot());

        Assertions.assertEquals(red, drawn);
        Assertions.assertTrue(deck.isInDeckEmpty());
    }

    @Test
    public void testDiscard_refillsDeckWhenDrawingFromEmptyDeck() {
        TrainCard red = new TrainCard(1, CardColour.PINK);
        TrainCard blue = new TrainCard(2, CardColour.BLUE);

        TrainCardDeck deck = new TrainCardDeck();

        deck.discard(List.of(red, blue));

        TrainCard drawn = deck.drawBySlot(deck.getDeckSlot());

        Assertions.assertNotNull(drawn);
        Assertions.assertEquals(1, deck.getInDeckPile().size());
    }

    @Test
    public void testInitialiseFaceUpPile_fillsFaceUpPileFromDeck() {
        TrainCard card1 = new TrainCard(1, CardColour.PINK);
        TrainCard card2 = new TrainCard(2, CardColour.BLUE);
        TrainCard card3 = new TrainCard(3, CardColour.GREEN);
        TrainCard card4 = new TrainCard(4, CardColour.YELLOW);
        TrainCard card5 = new TrainCard(5, CardColour.BLACK);

        TrainCardDeck deck = new TrainCardDeck(
                List.of(card1, card2, card3, card4, card5),
                List.of()
        );

        deck.initialiseFaceUpPile();

        Assertions.assertFalse(deck.isFaceUpEmpty());
        Assertions.assertEquals(card1, deck.getFaceUpPile().get(0));
        Assertions.assertEquals(card2, deck.getFaceUpPile().get(1));
        Assertions.assertEquals(card3, deck.getFaceUpPile().get(2));
        Assertions.assertEquals(card4, deck.getFaceUpPile().get(3));
        Assertions.assertEquals(card5, deck.getFaceUpPile().get(4));
        Assertions.assertTrue(deck.isInDeckEmpty());
    }

    @Test
    public void testGetTotalCardsInSystem_returnsCorrectTotal() {
        TrainCard pink = new TrainCard(1, CardColour.PINK);
        TrainCard blue = new TrainCard(2, CardColour.BLUE);
        TrainCard wild = new TrainCard(3, CardColour.WILD);

        TrainCardDeck deck = new TrainCardDeck(
                List.of(pink),
                List.of(blue)
        );

        deck.discard(List.of(wild));

        Assertions.assertEquals(3, deck.getTotalCardsInSystem());
    }

    @Test
    public void testInitialiseFaceUpPile_withFiveOrFewerCards_allowsThreeLocomotives() {

        TrainCard loco1 = new TrainCard(1, CardColour.WILD);
        TrainCard loco2 = new TrainCard(2, CardColour.WILD);
        TrainCard loco3 = new TrainCard(3, CardColour.WILD);
        TrainCard pink1 = new TrainCard(4, CardColour.PINK);
        TrainCard pink2 = new TrainCard(5, CardColour.PINK);

        TrainCardDeck deck = new TrainCardDeck(
                List.of(loco1, loco2, loco3, pink1, pink2),
                List.of()
        );

        deck.initialiseFaceUpPile();

        int locomotiveCount = 0;
        for (TrainCard card : deck.getFaceUpPile()) {
            if (card != null && card.isLocomotive()) {
                locomotiveCount++;
            }
        }

        Assertions.assertEquals(5, deck.getTotalCardsInSystem());
        Assertions.assertEquals(3, locomotiveCount, "Should allow 3 locomotives because total cards <= 5");
    }
}
