package decks;

import game.card.DestinationCard;
import game.deck.DestinationCardDeck;
import game.map.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Destination Card Deck test file
 *
 * @author Hew Jia Jing
 */
public class DestinationCardDeckTest {

    private DestinationCardDeck deck;
    private City cityA;
    private City cityB;

    @BeforeEach
    public void setUp() {
        deck = new DestinationCardDeck();
        cityA = new City("Penang", 1, 2);
        cityB = new City("Ipoh", 3, 4);
    }

    @Test
    public void testConstructor_createsEmptyDeck() {
        Assertions.assertTrue(deck.isInDeckEmpty());
        Assertions.assertEquals(0, deck.getInDeckPile().size());
    }

    @Test
    public void testAddCard_addsOneCardToDeck() {
        DestinationCard card = new DestinationCard(1, cityA, cityB, 5);

        deck.addCard(card);

        Assertions.assertFalse(deck.isInDeckEmpty());
        Assertions.assertEquals(1, deck.getInDeckPile().size());
        Assertions.assertEquals(card, deck.getInDeckPile().get(0));
    }

    @Test
    public void testDraw_whenDeckEmpty_returnsNull() {
        DestinationCard drawn = deck.draw();

        Assertions.assertNull(drawn);
    }

    @Test
    public void testDraw_removesAndReturnsTopCard() {
        DestinationCard card1 = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard card2 = new DestinationCard(2, cityA, cityB, 8);

        deck.addCard(card1);
        deck.addCard(card2);

        DestinationCard drawn = deck.draw();

        Assertions.assertEquals(card1, drawn);
        Assertions.assertEquals(1, deck.getInDeckPile().size());
        Assertions.assertEquals(card2, deck.getInDeckPile().get(0));
    }

    @Test
    public void testDrawThree_whenDeckEmpty_returnsEmptyList() {
        List<DestinationCard> cards = deck.drawThree();

        Assertions.assertNotNull(cards);
        Assertions.assertEquals(0, cards.size());
    }

    @Test
    public void testDrawThree_whenDeckHasLessThanThreeCards_returnsAvailableCards() {
        DestinationCard card1 = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard card2 = new DestinationCard(2, cityA, cityB, 8);

        deck.addCard(card1);
        deck.addCard(card2);

        List<DestinationCard> drawnCards = deck.drawThree();

        Assertions.assertEquals(2, drawnCards.size());
        Assertions.assertEquals(card1, drawnCards.get(0));
        Assertions.assertEquals(card2, drawnCards.get(1));
        Assertions.assertTrue(deck.isInDeckEmpty());
    }

    @Test
    public void testDrawThree_whenDeckHasThreeCards_returnsThreeCards() {
        DestinationCard card1 = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard card2 = new DestinationCard(2, cityA, cityB, 8);
        DestinationCard card3 = new DestinationCard(3, cityA, cityB, 10);

        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);

        List<DestinationCard> drawnCards = deck.drawThree();

        Assertions.assertEquals(3, drawnCards.size());
        Assertions.assertEquals(card1, drawnCards.get(0));
        Assertions.assertEquals(card2, drawnCards.get(1));
        Assertions.assertEquals(card3, drawnCards.get(2));
        Assertions.assertTrue(deck.isInDeckEmpty());
    }

    @Test
    public void testDrawThree_whenDeckHasMoreThanThreeCards_drawsOnlyThree() {
        DestinationCard card1 = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard card2 = new DestinationCard(2, cityA, cityB, 8);
        DestinationCard card3 = new DestinationCard(3, cityA, cityB, 10);
        DestinationCard card4 = new DestinationCard(4, cityA, cityB, 12);

        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        deck.addCard(card4);

        List<DestinationCard> drawnCards = deck.drawThree();

        Assertions.assertEquals(3, drawnCards.size());
        Assertions.assertEquals(1, deck.getInDeckPile().size());
        Assertions.assertEquals(card4, deck.getInDeckPile().get(0));
    }

    @Test
    public void testReturnToBottom_addsCardsToBottomOfDeck() {
        DestinationCard existingCard = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard returnedCard1 = new DestinationCard(2, cityA, cityB, 8);
        DestinationCard returnedCard2 = new DestinationCard(3, cityA, cityB, 10);

        deck.addCard(existingCard);
        deck.returnToBottom(List.of(returnedCard1, returnedCard2));

        Assertions.assertEquals(3, deck.getInDeckPile().size());
        Assertions.assertEquals(existingCard, deck.getInDeckPile().get(0));
        Assertions.assertEquals(returnedCard1, deck.getInDeckPile().get(1));
        Assertions.assertEquals(returnedCard2, deck.getInDeckPile().get(2));
    }

    @Test
    public void testReturnToBottom_withEmptyList_doesNothing() {
        deck.returnToBottom(List.of());

        Assertions.assertTrue(deck.isInDeckEmpty());
        Assertions.assertEquals(0, deck.getInDeckPile().size());
    }

    @Test
    public void testShuffle_keepsSameNumberOfCards() {
        DestinationCard card1 = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard card2 = new DestinationCard(2, cityA, cityB, 8);
        DestinationCard card3 = new DestinationCard(3, cityA, cityB, 10);

        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);

        deck.shuffle();

        Assertions.assertEquals(3, deck.getInDeckPile().size());
        Assertions.assertTrue(deck.getInDeckPile().contains(card1));
        Assertions.assertTrue(deck.getInDeckPile().contains(card2));
        Assertions.assertTrue(deck.getInDeckPile().contains(card3));
    }

    @Test
    public void testPeek_whenDeckHasMoreThan3Cards() {
        DestinationCard card1 = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard card2 = new DestinationCard(2, cityA, cityB, 8);
        DestinationCard card3 = new DestinationCard(3, cityA, cityB, 10);

        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);

        Assertions.assertEquals(3, deck.peekThree().size());
        Assertions.assertTrue(deck.getInDeckPile().contains(card1));
        Assertions.assertTrue(deck.getInDeckPile().contains(card2));
        Assertions.assertTrue(deck.getInDeckPile().contains(card3));
    }

    @Test
    public void testPeek_whenDeckHasLessThan3Cards() {
        DestinationCard card1 = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard card2 = new DestinationCard(2, cityA, cityB, 8);

        deck.addCard(card1);
        deck.addCard(card2);

        Assertions.assertEquals(2, deck.peekThree().size());
        Assertions.assertTrue(deck.getInDeckPile().contains(card1));
        Assertions.assertTrue(deck.getInDeckPile().contains(card2));
    }

    @Test
    public void testPeek_whenDeckHasNoCards() {
        Assertions.assertEquals(0, deck.peekThree().size());
    }
}
