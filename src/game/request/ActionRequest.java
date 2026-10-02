package game.request;

import game.main.Game;
import game.command.ActionCommand;

/**
 * Represents a user request that can be converted into an action command.
 *
 * @author Hew Jia Jing
 */
public interface ActionRequest {

    /**
     * Builds the command represented by this request.
     *
     * @param game the current game instance
     * @return the action command
     */
    ActionCommand buildCommand(Game game);
}
