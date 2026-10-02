package game.command;

import game.card.TrainCard;
import game.character.Player;
import game.deck.TrainCardDeck;
import game.map.Route;
import game.strategy.routescoring.RouteScoringStrategy;

import java.util.List;

/**
 * Executes a route-claiming action for a player.
 *
 * @author Jia Ming
 */
public class ClaimRouteCommand implements ActionCommand {

    /**
     * Route being claimed.
     */
    private final Route route;

    /**
     * Train cards used to claim the route.
     */
    private final List<TrainCard> trainCardsToUse;

    /**
     * Deck used to discard spent train cards.
     */
    private final TrainCardDeck trainCardDeck;

    /**
     * Strategy used to calculate route score.
     */
    private final RouteScoringStrategy routeScoringStrategy;

    /**
     * Constructs a claim route command.
     *
     * @param route                route to be claimed
     * @param trainCardsToUse      train cards used to claim the route
     * @param trainCardDeck        deck used for discarding cards
     * @param routeScoringStrategy scoring strategy applied to the route
     */
    public ClaimRouteCommand(Route route, List<TrainCard> trainCardsToUse, TrainCardDeck trainCardDeck, RouteScoringStrategy routeScoringStrategy) {
        this.route = route;
        this.trainCardsToUse = trainCardsToUse;
        this.trainCardDeck = trainCardDeck;
        this.routeScoringStrategy = routeScoringStrategy;
    }

    /**
     * Executes the route-claiming action.
     * Validates the claim, updates the player's state, and discards used cards.
     *
     * @param currentPlayer current player attempting the action
     * @return result of the action execution
     */
    @Override
    public ActionResult execute(Player currentPlayer) {

        if (!route.canClaim(currentPlayer, trainCardsToUse)) {
            return ActionResult.fail("Invalid Claim");
        }

        trainCardDeck.discard(trainCardsToUse);

        String abilityMessage = currentPlayer.claimRoute(route, trainCardsToUse, routeScoringStrategy);

        String message = "Claim Route Successful";
        if (abilityMessage != null) {
            message += "\n" + abilityMessage;
        }

        return ActionResult.success(message);
    }

    /**
     * Returns the action cost for claiming a route.
     *
     * @return the action cost
     */
    @Override
    public ActionCost getCost() {
        return ActionCost.FULL;
    }
}
