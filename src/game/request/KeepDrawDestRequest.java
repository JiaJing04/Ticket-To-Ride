package game.request;

import game.command.ActionCommand;
import game.command.InvalidCommand;
import game.command.KeepDestCommand;
import game.main.Game;
import game.pendingdecision.PendingDecision;

import java.util.List;

/**
 * Represents a request to confirming the kept and discarded destination cards selection.
 *
 * @author Ng Zi Wei
 */
public class KeepDrawDestRequest implements ActionRequest {

    /**
     * IDs of destination cards selected to keep.
     */
    private final List<Integer> keepDestCardsIds;

    /**
     * IDs of destination cards selected to discard.
     */
    private final List<Integer> discardDestCardsIds;

    /**
     * Creates a request to resolve destination ticket selection.
     *
     * @param keepDestCardsIds    IDs of destination cards to keep
     * @param discardDestCardsIds IDs of destination cards to discard
     */
    public KeepDrawDestRequest(List<Integer> keepDestCardsIds, List<Integer> discardDestCardsIds) {
        this.keepDestCardsIds = keepDestCardsIds;
        this.discardDestCardsIds = discardDestCardsIds;
    }

    /**
     * Builds a command to resolve the destination ticket pending decision.
     *
     * @param game current game instance
     * @return command resolving the destination ticket selection
     */
    @Override
    public ActionCommand buildCommand(Game game) {

        PendingDecision currentPending = game.getPendingDecision();

        if (currentPending == null) {
            return new InvalidCommand("No pending decision found.",
                    false);
        }

        return new KeepDestCommand(keepDestCardsIds, discardDestCardsIds, game.getDestinationCardDeck(), game.isSetupPhase());

    }
}
