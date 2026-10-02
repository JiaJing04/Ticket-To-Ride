package game.card;

import game.character.Player;
import game.map.Board;
import game.map.City;

/**
 * Represents a destination ticket card
 *
 * @author Ng Zi Wei
 */
public class DestinationCard extends Card {

    /**
     * The starting city of this destination card.
     */
    private final City from;

    /**
     * The destination city of this destination card.
     */
    private final City to;

    /**
     * The points awarded for completing this destination card.
     */
    private final int points;

    /**
     * Constructs a new DestinationCard
     *
     * @param id     the unique identifier for the Destination Card
     * @param from   the starting City
     * @param to     the destination City
     * @param points the point value of the ticket
     */
    public DestinationCard(int id, City from, City to, int points) {
        super(id);
        this.from = from;
        this.to = to;
        this.points = points;
    }

    /**
     * Returns the starting city for this destination card.
     *
     * @return the starting city for this destination card.
     */
    public City getFrom() {
        return from;
    }

    /**
     * Returns the destination city for this destination card.
     *
     * @return the destination city for this destination card.
     */
    public City getTo() {
        return to;
    }

    /**
     * Returns the points for this destination card.
     *
     * @return the points for this destination card.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Checks whether the given player has connected a path between
     * the source and destination cities on the board.
     *
     * @param ownerPlayer the player who owns the routes
     * @param board       the game board used to evaluate connectivity
     * @return true if the player has a connected path between the cities
     */
    public boolean isCompleted(Player ownerPlayer, Board board) {
        return board.isPathConnected(ownerPlayer, from, to);
    }
}
