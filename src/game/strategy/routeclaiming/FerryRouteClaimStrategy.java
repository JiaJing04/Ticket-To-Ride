package game.strategy.routeclaiming;

import game.card.CardColour;
import game.card.TrainCard;
import game.character.Player;
import game.map.Route;
import game.map.RouteColour;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Strategy for validating ferry route claims.
 *
 * @author Hew Jia Jing
 */
public class FerryRouteClaimStrategy implements RouteClaimStrategy {

    /**
     * Number of locomotive cards normally required to claim the ferry route.
     */
    private final int requiredWildCards;

    /**
     * Creates a ferry route claim strategy.
     *
     * @param requiredWildCards the number of locomotive cards required
     */
    public FerryRouteClaimStrategy(int requiredWildCards) {
        this.requiredWildCards = requiredWildCards;
    }

    /**
     * Returns the number of locomotive cards required for the ferry route.
     *
     * @return the required number of locomotive cards
     */
    public int getRequiredWildCards() {
        return requiredWildCards;
    }

    /**
     * Validates whether the player can claim the ferry route using the given cards.
     *
     * @param route the route being claimed
     * @param player the player attempting to claim the route
     * @param trainCardToUse the train cards used for the claim
     * @return true if the claim is valid; false otherwise
     */
    @Override
    public boolean validate(Route route, Player player, List<TrainCard> trainCardToUse) {
        int routeLength = route.getLength();

        for (int missingWilds = 0; missingWilds <= requiredWildCards; missingWilds++) {
            int actualWildsNeeded = requiredWildCards - missingWilds;
            int substituteCardsNeeded = missingWilds * 3;

            int totalCardsNeeded = routeLength + (missingWilds * 2);

            if (trainCardToUse.size() != totalCardsNeeded) {
                continue;
            }

            long wildCount = trainCardToUse.stream()
                    .filter(c -> CardColour.WILD.equals(c.getColour()))
                    .count();

            if (wildCount < actualWildsNeeded) {
                continue;
            }

            int remainingRouteSpaces = routeLength - requiredWildCards;

            if (canCoverRemainingSpaces(
                    route,
                    trainCardToUse,
                    actualWildsNeeded,
                    substituteCardsNeeded,
                    remainingRouteSpaces
            )) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether the remaining cards can cover the non-ferry spaces of the route.
     *
     * @param route the route being claimed
     * @param cards the cards used for the claim
     * @param wildsUsedForFerry the number of wild cards used to satisfy ferry requirements
     * @param substituteCardsUsed the number of extra cards used to replace missing wild cards
     * @param remainingRouteSpaces the number of route spaces still needing coverage
     * @return true if the remaining cards can cover the route spaces;
     *     false otherwise
     */
    private boolean canCoverRemainingSpaces(Route route,
                                            List<TrainCard> cards,
                                            int wildsUsedForFerry,
                                            int substituteCardsUsed,
                                            int remainingRouteSpaces) {

        List<CardColour> remainingColours = cards.stream()
                .map(TrainCard::getColour)
                .collect(Collectors.toList());

        for (int i = 0; i < wildsUsedForFerry; i++) {
            remainingColours.remove(CardColour.WILD);
        }

        for (int i = 0; i < substituteCardsUsed && !remainingColours.isEmpty(); i++) {
            remainingColours.remove(0);
        }

        if (RouteColour.GREY.equals(route.getColour())) {
            Set<CardColour> nonWildColours = remainingColours.stream()
                    .filter(c -> !CardColour.WILD.equals(c))
                    .collect(Collectors.toSet());

            return remainingColours.size() == remainingRouteSpaces
                    && nonWildColours.size() <= 1;
        }

        return remainingColours.size() == remainingRouteSpaces
                && remainingColours.stream()
                .allMatch(c ->
                        c.name().equals(route.getColour().name())
                                || CardColour.WILD.equals(c)
                );
    }
}
