package game.command;

import game.card.DestinationCard;
import game.character.Player;
import game.deck.DestinationCardDeck;

import java.util.ArrayList;
import java.util.List;

/**
 * Resolves the pending decision of keeping or discarding destination cards.
 *
 * @author Ng Zi Wei
 */
public class KeepDestCommand implements ActionCommand {

    /**
     * Destination cards the player chooses to keep.
     */
    private final List<Integer> keepDestCardIds;

    /**
     * Destination cards the player chooses to discard.
     */
    private final List<Integer> discardDestCardIds;

    /**
     * Deck used to return discarded destination cards.
     */
    private final DestinationCardDeck ticketDeck;

    /**
     * Minimum number of destination cards the player must keep.
     */
    private final boolean isSetupPhase;

    /**
     * Constructs a keep destination card command.
     *
     * @param keepDestCardIds    destination cards id the player chooses to keep
     * @param discardDestCardIds destination cards id the player chooses to discard
     * @param ticketDeck         destination card deck
     * @param isSetupPhase       if the current game is still in setup phase
     */
    public KeepDestCommand(List<Integer> keepDestCardIds,
                           List<Integer> discardDestCardIds,
                           DestinationCardDeck ticketDeck,
                           boolean isSetupPhase) {
        this.keepDestCardIds = keepDestCardIds;
        this.discardDestCardIds = discardDestCardIds;
        this.ticketDeck = ticketDeck;
        this.isSetupPhase = isSetupPhase;
    }

    /**
     * Executes the keep destination card action.
     *
     * @param currentPlayer player performing the action
     * @return result of the action execution
     */
    @Override
    public ActionResult execute(Player currentPlayer) {
        int minKeep = isSetupPhase ? ticketDeck.getSetupMinKeep() : ticketDeck.getInGameMinKeep();

        // Validates player should keep at least 1 destination card.
        if (keepDestCardIds.size() < minKeep) {
            return ActionResult.pendingFailed("You must keep at least " + minKeep
                    + " destination card(s).");
        }

        List<DestinationCard> destinationCardsToDraw = ticketDeck.drawThree();

        List<DestinationCard> keepDestCards = new ArrayList<>();
        List<DestinationCard> returnDestCards = new ArrayList<>();

        for (DestinationCard card : destinationCardsToDraw) {
            if (keepDestCardIds.contains(card.getId())) {
                keepDestCards.add(card);
            } else if (discardDestCardIds.contains(card.getId())) {
                returnDestCards.add(card);
            }
        }

        // Add kept cards to the player
        currentPlayer.drawDestCard(keepDestCards);

        // Return discarded cards to the bottom of the deck
        ticketDeck.returnToBottom(returnDestCards);

        return ActionResult.success("Successfully draw " + keepDestCards.size()
                + " destination cards.");
    }

    /**
     * Returns the action cost for resolving destination card selection.
     *
     * @return the action cost
     */
    @Override
    public ActionCost getCost() {
        return ActionCost.FREE;
    }
}
