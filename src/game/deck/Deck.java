package game.deck;

import game.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class representing a generic deck of cards.
 *
 * @author Ng Zi Wei
 */
public abstract class Deck<T extends Card> {

    /**
     * Cards currently in the draw pile.
     */
    private final List<T> inDeckPile;

    /**
     * Constructs a new empty Deck
     */
    public Deck() {
        this.inDeckPile = new ArrayList<>();
    }

    /**
     * Constructs a deck with initial cards.
     *
     * @param inDeckPile initial face down cards in the deck
     */
    public Deck(List<T> inDeckPile) {
        this.inDeckPile = new ArrayList<>(inDeckPile);
    }

    /**
     * Draws the top card from the deck.
     *
     * @return top card, or null if the deck is empty
     */
    public T draw() {
        if (isInDeckEmpty()) {
            return null;
        }

        // Removes and returns the top card
        return inDeckPile.remove(0);
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(inDeckPile);
    }

    /**
     * Returns the cards in the deck.
     *
     * @return unmodifiable list of cards
     */
    public List<T> getInDeckPile() {
        return Collections.unmodifiableList(inDeckPile);
    }

    /**
     * Returns whether the deck is empty.
     *
     * @return true if empty
     */
    public boolean isInDeckEmpty() {
        return inDeckPile.isEmpty();
    }

    /**
     * Adds a card to the deck.
     *
     * @param card card to add
     */
    public void addCard(T card) {
        inDeckPile.add(card);
    }

    /**
     * Adds cards to the bottom of the deck.
     *
     * @param cards cards to add
     */
    protected void addCardsToBottom(List<T> cards) {
        inDeckPile.addAll(cards);
    }

    /**
     * Returns the total number of cards in the system.
     *
     * @return total card count
     */
    public abstract int getTotalCardsInSystem();

    /**
     * Replaces the current draw pile with the specified cards.
     *
     * @param cards cards to replace draw pile
     */
    public void setInDeckPile(List<T> cards) {
        this.inDeckPile.clear();
        this.inDeckPile.addAll(cards);
    }
}
