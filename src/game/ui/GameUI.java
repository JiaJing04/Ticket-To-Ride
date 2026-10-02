package game.ui;

import game.main.dto.GameResultDTO;
import game.main.dto.TicketDecisionCardDTO;

import java.util.List;

/**
 * Defines the user interface layer for game interactions.
 *
 * @author Hew Jia Jing
 */
public interface GameUI {

    /**
     * Handles a player drawing a train card.
     *
     * @param slot index of the clicked slot, or a special value for the deck
     */
    void onTrainCardClicked(int slot);

    /**
     * Handles a player claiming a route.
     *
     * @param routeId              unique identifier of the selected route
     * @param selectedTrainCardIds IDs of the train cards chosen to claim the route
     */
    void onClaimRouteClicked(String routeId, List<Integer> selectedTrainCardIds);

    /**
     * Handles a player drawing destination tickets.
     */
    void onDrawDestinationCardsClicked();

    /**
     * Handles confirmation of destination tickets to keep or discard.
     *
     * @param keepIds    IDs of destination tickets to keep
     * @param discardIds IDs of destination tickets to discard
     */
    void onTicketsSelectionConfirmed(List<Integer> keepIds, List<Integer> discardIds);

    /**
     * Refreshes the UI to reflect the current game state.
     */
    void refresh();

    /**
     * Shows a general message to the player.
     *
     * @param message message to display
     */
    void showMessage(String message);

    /**
     * Shows an error message to the player.
     *
     * @param error error message to display
     */
    void showError(String error);

    /**
     * Prompts the player to select destination tickets to keep.
     *
     * @param cards destination cards drawn for the player
     */
    void promptTicketSelection(List<TicketDecisionCardDTO> cards, int minKeep);

    /**
     * Shows the final scores and winner.
     *
     * @param gameResult DTO containing leaderboard, winner, and draw status
     */
    void showEndGameResult(GameResultDTO gameResult);

    /**
     * Sets the callback executed when returning to the main menu.
     *
     * @param callback callback used to switch screens or reset the application
     */
    void setOnReturnToMenu(Runnable callback);

    /**
     * Shows the final-round warning message.
     */
    void showLastRoundWarning();
}
