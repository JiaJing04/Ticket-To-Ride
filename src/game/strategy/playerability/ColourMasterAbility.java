package game.strategy.playerability;

import game.card.TrainCard;
import game.character.Player;
import game.map.Route;
import game.map.RouteColour;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Player ability that rewards claiming routes of unique colours.
 * <p>
 * The player gains bonus points whenever they claim a route of a colour
 * they have not previously claimed. After claiming routes of four unique
 * colours, the player receives a one-time double turn.
 *
 * @author Hew Jia Jing
 */
public class ColourMasterAbility implements PlayerAbility {

    /**
     * Bonus points awarded for claiming a route of a different colour.
     */
    private static final int BONUS_POINTS = 2;

    /**
     * Number of unique route colours required to unlock the double turn.
     */
    private static final int UNIQUE_COLOURS_THRESHOLD = 4;

    /**
     * Whether the double-turn reward has already been granted.
     */
    private boolean doubleTurnActivated = false;

    /**
     * Colours of routes previously claimed by the player.
     */
    private final Set<RouteColour> claimedColours = new HashSet<>();

    /**
     * Applies the colour master ability.
     *
     * @param player    the player activating the ability
     * @param route     the route that was claimed
     * @param usedCards the train cards used to claim the route
     */
    @Override
    public String apply(Player player, Route route, List<TrainCard> usedCards) {
        RouteColour newColour = route.getColour();
        StringBuilder message = new StringBuilder();

        // Give +2 points each time a route of a new/different colour is claimed
        if (!claimedColours.contains(newColour) && !claimedColours.isEmpty()) {
            player.addScore(BONUS_POINTS);
            message.append("Colour Master: +").append(BONUS_POINTS)
                    .append(" bonus points for claiming a new colour!");
        }

        claimedColours.add(newColour);

        // Once 4 unique colours collected, grant a one-time double turn
        if (!doubleTurnActivated && claimedColours.size() >= UNIQUE_COLOURS_THRESHOLD) {
            player.grantDoubleTurn();
            doubleTurnActivated = true;
            if (!message.isEmpty()) message.append("\n");
            message.append("Colour Master: 4 unique colours claimed — you get a double turn!");
        }

        return message.isEmpty() ? null : message.toString();
    }

    /**
     * Returns the display name of this ability.
     *
     * @return the ability name
     */
    @Override
    public String getName() {
        return "Colour Master";
    }
}
