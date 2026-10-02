package game.card;

/**
 * Abstract base class representing a generic card in the game.
 *
 * @author Ng Zi Wei
 */
public abstract class Card {

    /**
     * Unique identifier for the Card
     */
    private final int id;

    /**
     * Constructs a new Card.
     *
     * @param id the unique identifier for this card.
     */
    public Card(int id) {
        this.id = id;
    }

    /**
     * Returns the unique identifier for this card.
     *
     * @return the unique identifier for this card.
     */
    public int getId() {
        return id;
    }

}
