package game.turnmanager;

import game.character.Player;
import game.command.ActionCommand;
import game.command.ActionResult;
import game.pendingdecision.PendingDecision;

/**
 * Coordinates the flow of a player's turn
 *
 * @author Ng Zi Wei
 */
public class TurnMediator {

    /**
     * Maximum action cost available per turn.
     */
    private static final int MAX_ACTION_COST = 2;

    /**
     * Remaining action cost for the current turn.
     */
    private int remainingActionCost;

    /**
     * Pending decision that must be resolved before continuing the turn.
     */
    private PendingDecision pendingDecision;

    /**
     * Constructs a turn mediator.
     */
    public TurnMediator() {
        reset();
    }

    /**
     * Returns the current pending decision.
     *
     * @return the current pending decision, or null if none exists
     */
    public PendingDecision getPendingDecision() {
        return pendingDecision;
    }

    /**
     * Clears the current pending decision.
     */
    private void clearPendingDecision() {
        this.pendingDecision = null;
    }

    /**
     * Returns whether the turn is complete.
     *
     * @return true if the turn is complete
     */
    public boolean isTurnComplete() {
        return remainingActionCost <= 0 && pendingDecision == null;
    }

    /**
     * Resets the mediator for a new turn.
     */
    public void reset() {
        this.remainingActionCost = MAX_ACTION_COST;
        this.pendingDecision = null;
    }

    /**
     * Processes a game action.
     *
     * @param action        command to execute
     * @param currentPlayer player performing the action
     * @return result of the action execution
     */
    public ActionResult processAction(ActionCommand action,
                                      Player currentPlayer) {

        // Checks if the action is allowed currently
        int actionCost = action.getCost().getCost();
        if (actionCost > remainingActionCost) {
            return ActionResult.fail("This action is currently not allowed.");
        }

        // Resolve pending decision
        if (pendingDecision != null) {
            return handleDecisionResolution(action, currentPlayer);
        }

        return handleStandardAction(action, currentPlayer, actionCost);
    }

    /**
     * Handles a standard action.
     *
     * @param action        command to execute
     * @param currentPlayer player performing the action
     * @param actionCost    cost of the action
     * @return result of the action execution
     */
    private ActionResult handleStandardAction(ActionCommand action,
                                              Player currentPlayer,
                                              int actionCost) {

        // Execute the action
        ActionResult result = action.execute(currentPlayer);

        // Update state only if successful or pending
        if (result.isSuccess() || result.hasPendingDecision()) {
            remainingActionCost -= actionCost;

            this.pendingDecision = result.getPendingDecision();

            // TurnMediator owns turn flow — handle bonus turn here
            if (currentPlayer.hasDoubleTurn()) {
                currentPlayer.consumeDoubleTurn();
                grantBonusTurn();
            }
        }

        return result;
    }

    /**
     * Handles an action that resolves a pending decision.
     *
     * @param action        command to execute
     * @param currentPlayer player performing the action
     * @return result of the action execution
     */
    private ActionResult handleDecisionResolution(ActionCommand action,
                                                  Player currentPlayer) {

        ActionResult result = action.execute(currentPlayer);

        // Clear pending decision if pending decision command is successful
        if (result.isSuccess()) {
            clearPendingDecision();
        } else {
            result.setPendingDecision(this.pendingDecision);
        }

        return result;
    }

    /**
     * Returns the remaining action cost for the current turn.
     *
     * @return the remaining action cost
     */
    public int getRemainingActionCost() {
        return remainingActionCost;
    }

    /**
     * Checks whether the current turn still has the full action cost available.
     *
     * @return true if no action cost has been spent; false otherwise
     */
    public boolean hasFullActionCost() {
        return remainingActionCost == 2;
    }

    /**
     * Restores the remaining action cost.
     *
     * @param remainingActionCost the remaining action cost to restore
     */
    public void setRemainingActionCost(int remainingActionCost) {
        this.remainingActionCost = remainingActionCost;
    }

    /**
     * Grants an additional turn's worth of action cost.
     */
    public void grantBonusTurn() {
        this.remainingActionCost += MAX_ACTION_COST;
    }
}
