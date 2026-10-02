package game.command;

import game.character.Player;

/**
 * Defines an action that can be performed by a player.
 */
public interface ActionCommand {

    /**
     * Executes the action.
     *
     * @param currentPlayer player performing the action
     * @return result of the action execution
     */
    ActionResult execute(Player currentPlayer);

    /**
     * Returns the action cost.
     *
     * @return the action cost
     */
    ActionCost getCost();
}
