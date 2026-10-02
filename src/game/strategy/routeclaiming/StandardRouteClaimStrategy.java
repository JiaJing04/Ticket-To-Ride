package game.strategy.routeclaiming;

import game.card.CardColour;
import game.card.TrainCard;
import game.character.Player;
import game.map.Route;

import java.util.List;

/**
 * Strategy to represent standard route claiming rule
 *
 * @author Jia Ming
 */
public class StandardRouteClaimStrategy implements RouteClaimStrategy {

    /**
     * Validates whether the given train cards can be used to claim
     * a standard coloured route.
     *
     * @param route          Route to be claimed by the Player
     * @param player         Player to claim the route
     * @param trainCardToUse trainCard of Player to be used for claiming routes
     * @return if the claiming is valid
     */
    @Override
    public boolean validate(Route route, Player player, List<TrainCard> trainCardToUse) {
        if (route.getLength() != trainCardToUse.size()) {
            return false;
        }

        return trainCardToUse.stream()
                .allMatch(t ->
                        t.getColour().name().equals(route.getColour().name()) ||
                                CardColour.WILD.equals(t.getColour())
                );
    }
}
