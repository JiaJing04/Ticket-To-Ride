package game.request;

import game.card.TrainCard;
import game.command.ActionCommand;
import game.command.ClaimRouteCommand;
import game.main.Game;
import game.map.Route;

import java.util.List;

/**
 * Represents a request to claim a route.
 *
 * @author Ng Zi Wei
 */
public class ClaimRouteRequest implements ActionRequest {

    /**
     * Identifier of the route to claim.
     */
    private final String routeId;

    /**
     * IDs of train cards selected to claim the route.
     */
    private final List<Integer> selectedTrainCardIds;

    /**
     * Creates a claim route request.
     *
     * @param routeId              The id of Route target to claim
     * @param selectedTrainCardIds The ids of selected train cards
     */
    public ClaimRouteRequest(String routeId, List<Integer> selectedTrainCardIds) {
        this.routeId = routeId;
        this.selectedTrainCardIds = selectedTrainCardIds;
    }

    /**
     * Builds a command to claim the route.
     *
     * @param game current game instance
     * @return command representing the claim route action
     */
    @Override
    public ActionCommand buildCommand(Game game) {

        // Find target route
        Route targetRoute = game.getBoard().getRouteById(routeId);

        // Find TrainCard objects be selected
        List<TrainCard> selectedCards = game.getCurrentPlayer().getTrainCardByIds(selectedTrainCardIds);

        return new ClaimRouteCommand(targetRoute, selectedCards,
                game.getTrainCardDeck(), game.getRouteScoringStrategy());
    }
}
