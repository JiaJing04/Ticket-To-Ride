package game.setup;

import game.character.PlayerColour;
import game.strategy.playerability.PlayerAbility;

/**
 * Stores configuration data for a player during game setup.
 *
 * @author Hew Jia Jing
 */
public class PlayerConfiguration {

    /**
     * Player name.
     */
    private final String name;

    /**
     * Player colour.
     */
    private final PlayerColour colour;

    /**
     * Player ability.
     */
    private final PlayerAbility ability;

    /**
     * Creates a player configuration.
     *
     * @param name   the player name
     * @param colour the player colour
     * @param ability the player ability
     */
    public PlayerConfiguration(String name, PlayerColour colour, PlayerAbility ability) {
        this.name = name;
        this.colour = colour;
        this.ability = ability;
    }

    /**
     * Returns the player name.
     *
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player colour.
     *
     * @return player colour
     */
    public PlayerColour getColour() {
        return colour;
    }

    /**
     * Returns the player ability.
     *
     * @return player ability
     */
    public PlayerAbility getAbility() {
        return ability;
    }
}
