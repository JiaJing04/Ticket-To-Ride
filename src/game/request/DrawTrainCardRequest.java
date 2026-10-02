package game.request;

import game.command.ActionCommand;
import game.command.DrawTrainCardCommand;
import game.main.Game;

/**
 * Represents a request for drawing a train card.
 *
 * @author Hew Jia Jing
 */
public class DrawTrainCardRequest implements ActionRequest {

    /**
     * Slot index indicating where to draw from.
     * A special value represents drawing from the deck instead of face-up cards.
     */
    private final int slot;

    /**
     * Creates a draw train card request.
     *
     * @param slot the selected face-up card slot, or a special value for deck draw
     */
    public DrawTrainCardRequest(int slot) {
        this.slot = slot;
    }

    /**
     * Builds a command to draw a train card.
     *
     * @param game current game instance
     * @return command representing the draw train card action
     */
    @Override
    public ActionCommand buildCommand(Game game) {
        return new DrawTrainCardCommand(slot, game.getTrainCardDeck());
    }
}
