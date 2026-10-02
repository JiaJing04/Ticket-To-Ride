package game.request;

import game.command.ActionCommand;
import game.command.DrawDestCommand;
import game.deck.DestinationCardDeck;
import game.main.Game;

/**
 * Represents a request for drawing destination cards.
 *
 * @author Ng Zi Wei
 */
public class DrawDestCardRequest implements ActionRequest {

    /**
     * Builds a command to draw the destination cards.
     *
     * @param game current game instance
     * @return command representing the draw destination card action
     */
    @Override
    public ActionCommand buildCommand(Game game) {
        DestinationCardDeck deck = game.getDestinationCardDeck();
        int minKeep = game.isSetupPhase() ? deck.getSetupMinKeep() : deck.getInGameMinKeep();

        return new DrawDestCommand(game.getDestinationCardDeck(), minKeep);
    }
}
