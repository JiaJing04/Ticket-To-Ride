package game.strategy.playerability;

import game.card.CardColour;
import game.card.TrainCard;
import game.character.Player;
import game.map.Route;

import java.util.List;

/**
 * Player ability that grants limited locomotive refunds when claiming
 * ferry routes.
 * Each time the player claims a ferry route using at least one
 * locomotive card, one locomotive card is returned to the player's hand.
 * This effect may be used a maximum of two times per game.
 *
 * @author Hew Jia Jing
 */
public class FerryMasterAbility implements PlayerAbility {

    /**
     * Maximum number of free ferry refunds available.
     */
    private static final int MAX_FREE_CLAIMS = 2;

    /**
     * Number of ferry refunds remaining.
     */
    private int freeClaimsRemaining = MAX_FREE_CLAIMS;

    /**
     * Applies the ferry master ability.
     *
     * @param player the player activating the ability
     * @param route the route that was claimed
     * @param usedCards the train cards used to claim the route
     */
    @Override
    public String apply(Player player, Route route, List<TrainCard> usedCards) {
        if (!route.isFerry() || freeClaimsRemaining <= 0) return null;

        long locomotivesUsed = usedCards.stream()
                .filter(c -> CardColour.WILD.equals(c.getColour()))
                .count();

        if (locomotivesUsed > 0) {
            TrainCard locomotiveToRefund = usedCards.stream()
                    .filter(c -> CardColour.WILD.equals(c.getColour()))
                    .findFirst()
                    .get();

            player.drawTrainCard(locomotiveToRefund);
            freeClaimsRemaining--;

            return "Ferry Master: A locomotive card was refunded! ("
                    + freeClaimsRemaining + " free claims remaining)";
        }

        return null;
    }

    /**
     * Returns the display name of this ability.
     *
     * @return the ability name
     */
    @Override
    public String getName() {
        return "Ferry Master";
    }
}
