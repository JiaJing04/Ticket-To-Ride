package game.command;

import game.card.DestinationCard;
import game.character.Player;
import game.deck.DestinationCardDeck;
import game.pendingdecision.PendingDecision;
import game.pendingdecision.TicketDecision;

import java.util.List;

/**
 * Executes the action of drawing destination tickets from the deck.
 *
 * @author Ng Zi Wei
 */
public class DrawDestCommand implements ActionCommand {

    /**
     * Deck used to draw destination cards.
     */
    private final DestinationCardDeck ticketDeck;

    /**
     * Minimum number of drawn destination tickets the player must keep.
     */
    private final int minKeep;

    /**
     * Constructs a draw destination card command.
     *
     * @param ticketDeck destination card deck
     */
    public DrawDestCommand(DestinationCardDeck ticketDeck, int minKeep) {
        this.ticketDeck = ticketDeck;
        this.minKeep = minKeep;
    }

    /**
     * Executes the draw destination ticket action.
     *
     * @param currentPlayer player performing the action
     * @return result of the action execution
     */
    @Override
    public ActionResult execute(Player currentPlayer) {

        // Draw 3 destination cards
        List<DestinationCard> drawnCards = ticketDeck.peekThree();

        if (drawnCards.isEmpty()) {
            return ActionResult.fail("The destination ticket deck is empty!");
        }

        // Create the decision for the player to resolve
        PendingDecision decision = new TicketDecision(drawnCards, minKeep);
        return ActionResult.pending(
                "Drew tickets. Please select which to keep", decision);

    }

    /**
     * Returns the action cost for drawing destination tickets.
     *
     * @return the action cost
     */
    @Override
    public ActionCost getCost() {
        return ActionCost.FULL;
    }
}
