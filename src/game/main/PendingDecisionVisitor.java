package game.main;

import game.pendingdecision.TicketDecision;

/**
 * The visitor interface for the Double Dispatch pattern, allowing
 * the controller/UI layer to react to specific types of pending decisions.
 *
 * @author Ng Zi Wei
 */
public interface PendingDecisionVisitor {

    /**
     * Handles the UI display of TicketDecision
     *
     * @param decision The TicketDecision need to be displayed
     */
    void visitTicketDecision(TicketDecision decision);
}
