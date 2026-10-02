package game.strategy.routeclaiming;

import game.card.CardColour;
import game.card.TrainCard;
import game.character.Player;
import game.map.Route;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy for validating claims on gray routes.
 *
 * @author Jia Ming
 */
public class GrayRouteClaimStrategy implements RouteClaimStrategy {

    /**
     * Validates whether the given train cards can be used to claim a
     * gray route.
     *
     * @param route          Route to be claimed by the Player
     * @param player         Player to claim the route
     * @param trainCardToUse trainCard of Player to be used for claiming routes
     * @return true if the claim satisfies the gray route rules
     */
    @Override
    public boolean validate(Route route, Player player, List<TrainCard> trainCardToUse) {
        if (route.getLength() != trainCardToUse.size()) {
            return false;
        }

        return trainCardToUse.stream()
                .map(TrainCard::getColour)
                .filter(c -> !CardColour.WILD.equals(c))
                .collect(Collectors.toSet())
                .size() <= 1;
    }
}
