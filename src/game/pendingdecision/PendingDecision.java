package game.pendingdecision;

import game.main.PendingDecisionVisitor;

/**
 * Represents the contract for the pending decisions
 *
 * @author Ng Zi Wei
 */
public interface PendingDecision {

    /**
     * Accepts a visitor to handle decision-specific logic.
     *
     * @param visitor visitor handling UI or routing logic
     */
    void accept(PendingDecisionVisitor visitor);
}
