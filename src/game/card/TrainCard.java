package game.card;

/**
 * Represents a train card which players draw and use to claim routes.
 *
 * @author Ng Zi Wei
 */
public class TrainCard extends Card {

    /**
     * The colour of this train card.
     */
    private final CardColour colour;

    /**
     * Constructs a new TrainCard.
     *
     * @param id     the unique identifier for this train card.
     * @param colour the colour for this train card.
     */
    public TrainCard(int id, CardColour colour) {
        super(id);

        this.colour = colour;
    }

    /**
     * Returns the colour for this train card.
     *
     * @return the colour for this train card.
     */
    public CardColour getColour() {
        return colour;
    }

    /**
     * Checks if this train card is wild locomotive card.
     *
     * @return true if the train card is locomotive, false otherwise.
     */
    public boolean isLocomotive() {
        return colour == CardColour.WILD;
    }
}
