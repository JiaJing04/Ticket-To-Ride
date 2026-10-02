package game.strategy.playerability;

import game.card.TrainCard;
import game.character.Player;
import game.map.Route;

import java.util.List;

/**
 * Defines a special ability that can be applied when a player claims a route.
 *
 * @author Hew Jia Jing
 */
public interface PlayerAbility {

    /**
     * Applies the ability effect after a route has been successfully claimed.
     *
     * @param player the player activating the ability
     * @param route the route that was claimed
     * @param usedCards the train cards used to claim the route
     */
    String apply(Player player,
               Route route,
               List<TrainCard> usedCards);

    /**
     * Returns the display name of this ability.
     *
     * @return the ability name
     */
    String getName();
}
