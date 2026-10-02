package game.command;

import game.pendingdecision.PendingDecision;

/**
 * Represents the result of executing an action.
 *
 * @author Ng Zi Wei
 */
public class ActionResult {

    /**
     * Status of the action (success, failure, pending, or pending failed).
     */
    private final ActionStatus status;

    /**
     * Message describing the result of the action.
     */
    private final String message;

    /**
     * End-of-turn outcome associated with this action, if applicable.
     */
    private EndTurnOutcome endTurnOutcome;

    /**
     * Pending decision that requires further player input, if applicable.
     */
    private PendingDecision pendingDecision;

    /**
     * Constructs a new ActionResult
     *
     * @param status          The status of the ActionResult
     * @param message         The messages left for the ActionResult
     * @param pendingDecision The pending decision resulted from the Action
     */
    private ActionResult(ActionStatus status, String message,
                         PendingDecision pendingDecision) {
        this.status = status;
        this.message = message;
        this.pendingDecision = pendingDecision;
    }

    /**
     * Returns a successful action result.
     *
     * @param message success message
     * @return a successful action result
     */
    public static ActionResult success(String message) {
        return new ActionResult(ActionStatus.SUCCESS, message,
                null);
    }

    /**
     * Returns a failed action result.
     *
     * @param message failure message
     * @return a failed action result
     */
    public static ActionResult fail(String message) {
        return new ActionResult(ActionStatus.FAILED, message,
                null);
    }

    /**
     * Returns a pending action result.
     *
     * @param message         context for the pending state
     * @param pendingDecision decision to be resolved
     * @return a pending action result
     */
    public static ActionResult pending(String message,
                                       PendingDecision pendingDecision) {
        return new ActionResult(ActionStatus.PENDING, message,
                pendingDecision);
    }

    /**
     * Returns a failed pending action result.
     *
     * @param message context for the failure
     * @return a failed pending action result
     */
    public static ActionResult pendingFailed(String message) {
        return new ActionResult(ActionStatus.PENDING_FAILED, message,
                null);
    }

    /**
     * Returns whether the action succeeded.
     *
     * @return true if successful
     */
    public boolean isSuccess() {
        return this.status == ActionStatus.SUCCESS;
    }

    /**
     * Returns whether a pending decision exists.
     *
     * @return true if a pending decision exists
     */
    public boolean hasPendingDecision() {
        return this.pendingDecision != null;
    }

    /**
     * Returns the pending decision.
     *
     * @return pending decision, or null if none exists
     */
    public PendingDecision getPendingDecision() {
        return pendingDecision;
    }

    /**
     * Sets the pending decision.
     *
     * @param decision pending decision
     */
    public void setPendingDecision(PendingDecision decision) {
        this.pendingDecision = decision;
    }

    /**
     * Returns the end-of-turn outcome.
     *
     * @return end turn outcome, or null if not set
     */
    public EndTurnOutcome getEndTurnOutcome() {
        return endTurnOutcome;
    }

    /**
     * Sets the end-of-turn outcome.
     *
     * @param endTurnOutcome end turn outcome
     */
    public void setEndTurnOutcome(EndTurnOutcome endTurnOutcome) {
        this.endTurnOutcome = endTurnOutcome;
    }

    /**
     * Returns the result message.
     *
     * @return result message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the action status.
     *
     * @return action status
     */
    public ActionStatus getStatus() {
        return status;
    }

}
