package game.deck;

import game.card.TrainCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents the deck of train cards, managing the hidden deck,
 * the 5 face-up cards, and the discarded pile.
 *
 * @author Ng Zi Wei
 */
public class TrainCardDeck extends Deck<TrainCard> {

    /**
     * Cards discarded by players.
     */
    private final List<TrainCard> discardedPile;

    /**
     * Face-up cards available for selection.
     */
    private final List<TrainCard> faceUpPile;

    /**
     * Number of face-up cards maintained.
     */
    private static final int FACE_UP_SIZE = 5;

    /**
     * Maximum number of locomotives allowed in the face-up pile.
     */
    private static final int MAX_LOCOMOTIVES_ALLOWED_FACE_UP = 2;

    /**
     * Slot value representing drawing from the deck.
     */
    private static final int DECK_SLOT = -1;

    /**
     * Constructs a TrainCardDeck with 3 empty piles.
     */
    public TrainCardDeck() {
        super();

        this.discardedPile = new ArrayList<>();
        this.faceUpPile = new ArrayList<>(Collections.nCopies(FACE_UP_SIZE,
                null));
    }

    /**
     * Replaces the current face-up pile with the specified cards.
     *
     * @param cards cards that will become the new face-up pile
     */
    public void setFaceUpPile(List<TrainCard> cards) {
        faceUpPile.clear();
        faceUpPile.addAll(cards);
    }

    /**
     * Replaces the current discard pile with the specified cards.
     *
     * @param cards cards that will become the new discard pile
     */
    public void setDiscardedPile(List<TrainCard> cards) {
        discardedPile.clear();
        discardedPile.addAll(cards);
    }

    /**
     * Constructs a TrainCardDeck with in deck cards and face up cards.
     *
     * @param inDeckPile the face down cards managed by this deck
     * @param faceUpPile the face up cards managed by this deck
     */
    public TrainCardDeck(List<TrainCard> inDeckPile,
                         List<TrainCard> faceUpPile) {
        super(inDeckPile);

        this.discardedPile = new ArrayList<>();
        this.faceUpPile = new ArrayList<>(Collections.nCopies(FACE_UP_SIZE,
                null));

        // Ensures only 5 face-up cards
        if (faceUpPile != null) {
            for (int i = 0; i < Math.min(faceUpPile.size(), FACE_UP_SIZE); i++) {
                this.faceUpPile.set(i, faceUpPile.get(i));
            }
        }
    }

    /**
     * Returns the total number of cards in this train card deck system.
     *
     * @return total number of cards in this train card deck system
     */
    @Override
    public int getTotalCardsInSystem() {
        int total = super.getInDeckPile().size() + discardedPile.size();

        for (TrainCard card : faceUpPile) {
            if (card != null) {
                total++;
            }
        }

        return total;
    }

    /**
     * Returns the slot value representing the draw deck.
     *
     * @return slot index used for drawing from the deck
     */
    public int getDeckSlot() {
        return DECK_SLOT;
    }

    /**
     * Draws a card from the specified slot.
     *
     * @param slot slot index for face-up, or -1 for deck
     * @return drawn card, or null if unavailable
     */
    public TrainCard drawBySlot(int slot) {
        if (slot == DECK_SLOT) {
            return drawInDeck();
        }
        return drawFaceUp(slot);
    }

    /**
     * Draws the top train card in deck
     *
     * @return the top train card in deck.
     */
    private TrainCard drawInDeck() {
        // Try refilling in deck pile if empty
        tryRefillInDeckPile();

        return draw();
    }

    /**
     * Draws the face up card at the specific slot
     *
     * @param slot The slot of the target drawn face up card.
     * @return The face up train card at the specific slot is available, null
     * otherwise.
     */
    private TrainCard drawFaceUp(int slot) {
        if (!canDrawFaceUp(slot)) {
            return null;
        }

        TrainCard drawnCard = faceUpPile.get(slot);

        // Replace the drawn face up card
        replaceFaceUp(slot);

        return drawnCard;
    }

    /**
     * Returns whether a card can be drawn from the given slot.
     *
     * @param slot slot index for face-up, or -1 for deck
     * @return true if drawable
     */
    public boolean canDraw(int slot) {
        if (slot != DECK_SLOT) {
            return canDrawFaceUp(slot);
        }
        return true;
    }

    /**
     * Checks if the train card at the specific slot is available
     *
     * @param slot The target face up slot card to check
     * @return true if the train card at the specific slot is available
     */
    private boolean canDrawFaceUp(int slot) {
        return slot >= 0
                && slot < FACE_UP_SIZE
                && faceUpPile.get(slot) != null;
    }

    /**
     * Discards cards into the discard pile.
     *
     * @param cards train cards to discard
     */
    public void discard(List<TrainCard> cards) {

        if (cards == null) return;

        for (TrainCard card : cards) {
            if (card != null) {
                discardedPile.add(card);
            }
        }

        // Try refill empty deck or face-up pile
        if (isInDeckEmpty() || hasFaceUpSlotEmpty()){
            tryRefillFaceUpPile();
        }
    }

    /**
     * Tries to refill the face up pile from in deck pile
     */
    private void tryRefillFaceUpPile() {

        tryRefillInDeckPile();
        refillEmptyFaceUpSlots();

        // Ensure no have more than 3 face up locomotive cards
        checkAndReplaceLocomotive();
    }

    /**
     * Tries to refill the in deck pile from discarded pile
     */
    private void tryRefillInDeckPile() {
        if (isInDeckEmpty() && !discardedPile.isEmpty()) {
            shuffleDiscardedIntoDeck();
        }
    }

    /**
     * Replaces the drawn face up card at specific slot with the
     * train card from in deck
     *
     * @param slot The slot of the face up card need to be replaced.
     */
    private void replaceFaceUp(int slot) {
        // Refill in deck pile
        tryRefillInDeckPile();

        if (!isInDeckEmpty()) {
            faceUpPile.set(slot, draw());
            checkAndReplaceLocomotive();
        } else {
            faceUpPile.set(slot, null); // slot positions preserved
        }
    }

    /**
     * Handles the rule of preventing 3 locomotive face-up cards
     */
    private void checkAndReplaceLocomotive() {

        while (true) {
            int locomotiveCount = 0;

            for (TrainCard card : faceUpPile) {
                if (card != null && card.isLocomotive()) {
                    locomotiveCount++;
                }
            }

            // Condition 1: Valid state (2 or fewer locomotives) -> We are good!
            if (locomotiveCount <= MAX_LOCOMOTIVES_ALLOWED_FACE_UP) {
                return;
            }

            // Condition 2: No have additional cards to replace locomotives
            if (getTotalCardsInSystem() <= FACE_UP_SIZE) {
                return;
            }

            // Condition 3: Rule violation, discard and try again
            discard(new ArrayList<>(faceUpPile));
            Collections.fill(faceUpPile, null);

            tryRefillInDeckPile();
            refillEmptyFaceUpSlots();
        }
    }

    /**
     * Add the all cards in discarded pile to in deck pile and reshuffle.
     */
    private void shuffleDiscardedIntoDeck() {
        addCardsToBottom(discardedPile);
        discardedPile.clear();
        shuffle();
    }

    /**
     * Returns whether the face-up pile is empty.
     *
     * @return true if empty
     */
    public boolean isFaceUpEmpty() {
        return faceUpPile.stream().allMatch(Objects::isNull);
    }

    /**
     * Returns whether there is at least one empty slot in the face-up pile.
     *
     * @return true if at least one slot is empty
     */
    public boolean hasFaceUpSlotEmpty() {
        return faceUpPile.stream().anyMatch(Objects::isNull);
    }

    /**
     * Returns the face-up cards.
     *
     * @return the train cards in the face up pile
     */
    public List<TrainCard> getFaceUpPile() {
        return Collections.unmodifiableList(faceUpPile);
    }

    /**
     * Returns the discarded cards.
     *
     * @return the train cards in the discarded pile
     */
    public List<TrainCard> getDiscardedPile() {
        return Collections.unmodifiableList(discardedPile);
    }

    /**
     * Peeks the face up card at the specific slot
     *
     * @param slot The slot of the target peeked face up card.
     * @return The face up train card at the specific slot is available, null
     * otherwise
     */
    public TrainCard peekFaceUp(int slot) {
        if (!canDrawFaceUp(slot)) {
            return null;
        }

        return faceUpPile.get(slot);
    }

    /**
     * Initialize the face up pile
     */
    public void initialiseFaceUpPile() {
        tryRefillFaceUpPile();
    }

    /**
     * Fills all empty face-up slots using cards from the draw pile.
     */
    private void refillEmptyFaceUpSlots() {
        for (int i = 0; i < FACE_UP_SIZE; i++) {
            if (faceUpPile.get(i) == null && !isInDeckEmpty()) {
                faceUpPile.set(i, draw());
            }
        }
    }
}
