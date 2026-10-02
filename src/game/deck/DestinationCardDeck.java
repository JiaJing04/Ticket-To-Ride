package game.deck;

import game.card.DestinationCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the deck of Destination Card in the game.
 *
 * @author Ng Zi Wei
 */
public class DestinationCardDeck extends Deck<DestinationCard> {

    /**
     * Number of destination cards drawn per draw action.
     */
    private static final int DRAW_AMOUNT = 3;

    /**
     * Minimum number of destination cards a player must keep during normal gameplay.
     */
    private static final int IN_GAME_MIN_KEEP = 1;

    /**
     * Minimum number of destination cards a player must keep during game setup.
     */
    private static final int SETUP_MIN_KEEP = 2;

    /**
     * Constructs a DestinationCardDeck
     */
    public DestinationCardDeck() {
        super();
    }

    /**
     * Returns the minimum number of destination cards a player must keep
     * during normal gameplay.
     *
     * @return minimum number of cards to keep in-game
     */
    public int getInGameMinKeep() {
        return IN_GAME_MIN_KEEP;
    }

    /**
     * Returns the minimum number of destination cards a player must keep
     * during initial game setup.
     *
     * @return minimum number of cards to keep during setup
     */
    public int getSetupMinKeep() {
        return SETUP_MIN_KEEP;
    }

    /**
     * Returns the total number of cards in this destination card deck system.
     *
     * @return total number of cards in this destination card deck system
     */
    @Override
    public int getTotalCardsInSystem() {
        return super.getInDeckPile().size();
    }

    /**
     * Draws up to three destination cards from the deck.
     *
     * @return list of drawn destination cards
     */
    public List<DestinationCard> drawThree() {
        List<DestinationCard> drawnCards = new ArrayList<>();

        for (int i = 0; i < DRAW_AMOUNT; i++) {
            if (!isInDeckEmpty()) {

                drawnCards.add(draw());
            }
        }

        return drawnCards;
    }

    /**
     * Returns an unmodifiable view of up to {@value #DRAW_AMOUNT} destination cards
     * from the top of the deck without removing them.
     *
     * @return unmodifiable list of destination cards available to draw
     */
    public List<DestinationCard> peekThree() {
        int end = Math.min(DRAW_AMOUNT, getInDeckPile().size());

        return Collections.unmodifiableList(getInDeckPile().subList(0, end));
    }

    /**
     * Returns discarded destination cards to the bottom of the deck
     *
     * @param cards The list of destination cards being discarded.
     */
    public void returnToBottom(List<DestinationCard> cards) {

        super.addCardsToBottom(cards);
    }
}
