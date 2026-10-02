package character;

import game.card.CardColour;
import game.card.DestinationCard;
import game.card.TrainCard;
import game.character.PlayerHand;
import game.map.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * Player Hand test file
 *
 * @author Hew Jia Jing
 */
public class PlayerHandTest {

    private PlayerHand hand;
    private City cityA;
    private City cityB;

    @BeforeEach
    public void setUp() {
        hand = new PlayerHand();
        cityA = new City("A", 1, 2);
        cityB = new City("B", 3, 4);
    }

    @Test
    public void testConstructor_initialisesEmptyHandForAllColours() {
        for (CardColour colour : CardColour.values()) {
            Assertions.assertEquals(0,
                    hand.getTrainCardCountByColour(colour));
        }

        Assertions.assertEquals(0, hand.getDestinationCards().size());
    }

    @Test
    public void testAddTrainCards_addsCardsByColour() {
        TrainCard red1 = new TrainCard(1, CardColour.PINK);
        TrainCard red2 = new TrainCard(2, CardColour.PINK);
        TrainCard blue = new TrainCard(3, CardColour.BLUE);

        hand.addTrainCards(List.of(red1, red2, blue));

        Assertions.assertEquals(2,
                hand.getTrainCardCountByColour(CardColour.PINK));
        Assertions.assertEquals(1,
                hand.getTrainCardCountByColour(CardColour.BLUE));
    }

    @Test
    public void testRemoveTrainCards_removesCardsFromHand() {
        TrainCard red1 = new TrainCard(1, CardColour.PINK);
        TrainCard red2 = new TrainCard(2, CardColour.PINK);

        hand.addTrainCards(List.of(red1, red2));
        hand.removeTrainCards(List.of(red1));

        Assertions.assertEquals(1,
                hand.getTrainCardCountByColour(CardColour.PINK));
        Assertions.assertTrue(
                hand.getTrainCards().get(CardColour.PINK).contains(red2));
    }

    @Test
    public void testAddDestinationCards_addsCards() {
        DestinationCard card1 = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard card2 = new DestinationCard(2, cityA, cityB, 8);

        hand.addDestinationCards(List.of(card1, card2));

        Assertions.assertEquals(2, hand.getDestinationCards().size());
        Assertions.assertTrue(hand.getDestinationCards().contains(card1));
        Assertions.assertTrue(hand.getDestinationCards().contains(card2));
    }

    @Test
    public void testRemoveDestinationCards_removesCards() {
        DestinationCard card1 = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard card2 = new DestinationCard(2, cityA, cityB, 8);

        hand.addDestinationCards(List.of(card1, card2));
        hand.removeDestinationCards(List.of(card1));

        Assertions.assertEquals(1, hand.getDestinationCards().size());
        Assertions.assertFalse(hand.getDestinationCards().contains(card1));
        Assertions.assertTrue(hand.getDestinationCards().contains(card2));
    }

    @Test
    public void testGetLocomotiveCount_returnsWildCardCount() {
        TrainCard wild1 = new TrainCard(1, CardColour.WILD);
        TrainCard wild2 = new TrainCard(2, CardColour.WILD);

        hand.addTrainCards(List.of(wild1, wild2));

        Assertions.assertEquals(2, hand.getLocomotiveCount());
    }

    @Test
    public void testHasEnoughOfAnyColour_withoutWildCards_returnsTrue() {
        TrainCard red1 = new TrainCard(1, CardColour.PINK);
        TrainCard red2 = new TrainCard(2, CardColour.PINK);
        TrainCard red3 = new TrainCard(3, CardColour.PINK);

        hand.addTrainCards(List.of(red1, red2, red3));

        Assertions.assertTrue(hand.hasEnoughOfAnyColour(3));
    }

    @Test
    public void testHasEnoughOfAnyColour_withWildCards_returnsTrue() {
        TrainCard red = new TrainCard(1, CardColour.PINK);
        TrainCard wild1 = new TrainCard(2, CardColour.WILD);
        TrainCard wild2 = new TrainCard(3, CardColour.WILD);

        hand.addTrainCards(List.of(red, wild1, wild2));

        Assertions.assertTrue(hand.hasEnoughOfAnyColour(3));
    }

    @Test
    public void testHasEnoughOfAnyColour_whenNotEnough_returnsFalse() {
        TrainCard red = new TrainCard(1, CardColour.PINK);
        TrainCard wild = new TrainCard(2, CardColour.WILD);

        hand.addTrainCards(List.of(red, wild));

        Assertions.assertFalse(hand.hasEnoughOfAnyColour(3));
    }

    @Test
    public void testGetTrainCardByIds_returnsMatchingCards() {
        TrainCard red = new TrainCard(1, CardColour.PINK);
        TrainCard blue = new TrainCard(2, CardColour.BLUE);
        TrainCard green = new TrainCard(3, CardColour.GREEN);

        hand.addTrainCards(List.of(red, blue, green));

        List<TrainCard> result = hand.getTrainCardByIds(List.of(1, 3));

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(red));
        Assertions.assertTrue(result.contains(green));
        Assertions.assertFalse(result.contains(blue));
    }

    @Test
    public void testGetDestinationCardByIds_returnsMatchingCards() {
        DestinationCard card1 = new DestinationCard(1, cityA, cityB, 5);
        DestinationCard card2 = new DestinationCard(2, cityA, cityB, 8);
        DestinationCard card3 = new DestinationCard(3, cityA, cityB, 10);

        hand.addDestinationCards(List.of(card1, card2, card3));

        List<DestinationCard> result =
                hand.getDestinationCardByIds(List.of(1, 3));

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(card1));
        Assertions.assertTrue(result.contains(card3));
        Assertions.assertFalse(result.contains(card2));
    }

    @Test
    public void testGetTrainCards_returnsDefensiveCopy() {
        TrainCard red = new TrainCard(1, CardColour.PINK);

        hand.addTrainCards(List.of(red));

        Map<CardColour, List<TrainCard>> cards = hand.getTrainCards();

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> cards.get(CardColour.PINK).add(
                        new TrainCard(2, CardColour.PINK)
                )
        );
    }

    @Test
    public void testGetDestinationCards_returnsDefensiveCopy() {
        DestinationCard card = new DestinationCard(1, cityA, cityB, 5);

        hand.addDestinationCards(List.of(card));

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> hand.getDestinationCards().add(
                        new DestinationCard(2, cityA, cityB, 8)
                )
        );
    }
}
