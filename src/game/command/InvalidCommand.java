package game.command;

import game.character.Player;

/**
 * Represents an invalid action command.
 *
 * @author Ng Zi Wei
 */
public class InvalidCommand implements ActionCommand {

    /**
     * Message describing why the action is invalid.
     */
    private final String errorMessage;

    /**
     * Indicates whether the failure occurs during a pending decision phase.
     */
    private final boolean isPending;

    /**
     * Constructs an invalid command.
     *
     * @param errorMessage error message describing the failure
     * @param isPending    whether the failure occurs during a pending phase
     */
    public InvalidCommand(String errorMessage, boolean isPending) {
        this.errorMessage = errorMessage;
        this.isPending = isPending;
    }

    /**
     * Executes the invalid action.
     *
     * @param currentPlayer player attempting the action
     * @return failure result, or pending failure if applicable
     */
    @Override
    public ActionResult execute(Player currentPlayer) {
        if (isPending) {
            return ActionResult.pendingFailed(errorMessage);
        }

        return ActionResult.fail(errorMessage);
    }

    /**
     * Returns the action cost of an invalid command.
     *
     * @return the action cost
     */
    @Override
    public ActionCost getCost() {
        return ActionCost.FREE;
    }
}
