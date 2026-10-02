package game.strategy.routeclaiming;

import game.card.TrainCard;
import game.character.Player;
import game.map.Route;

import java.util.List;

/**
 * Interface for different route claiming rule
 *
 * @author Jia Ming
 */
public interface RouteClaimStrategy {

    /**
     * Check if a Route can be claim
     *
     * @param route          Route to be claimed by the Player
     * @param player         Player to claim the route
     * @param trainCardToUse trainCard of Player to be used for claiming routes
     * @return if the claiming is valid
     */
    boolean validate(Route route, Player player, List<TrainCard> trainCardToUse);
}
