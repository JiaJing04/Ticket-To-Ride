package game.pendingdecision;

import game.card.DestinationCard;
import game.main.PendingDecisionVisitor;

import java.util.List;

/**
 * Represents a pending decision after a player draws destination cards.
 *
 * @author Ng Zi Wei
 */
public class TicketDecision implements PendingDecision {

    /**
     * Destination cards drawn for this pending decision.
     */
    private final List<DestinationCard> drawnCards;

    /**
     * Minimum number of destination cards the player must keep.
     */
    private final int minKeep;

    /**
     * Constructs a new TicketDecision.
     *
     * @param drawnCards The list of cards the player drew
     */
    public TicketDecision(List<DestinationCard> drawnCards, int minKeep) {
        this.drawnCards = drawnCards;
        this.minKeep = minKeep;
    }

    /**
     * Returns the cards drawn by the player.
     *
     * @return drawn destination cards
     */
    public List<DestinationCard> getDrawnCards() {
        return drawnCards;
    }

    /**
     * Returns the minimum number of destination cards the player must keep.
     *
     * @return minimum number of cards to retain
     */
    public int getMinKeep() {
        return minKeep;
    }

    /**
     * Accepts a visitor to handle this ticket decision (Double Dispatch).
     *
     * @param visitor visitor handling UI or routing logic
     */
    @Override
    public void accept(PendingDecisionVisitor visitor) {
        visitor.visitTicketDecision(this);
    }
}
