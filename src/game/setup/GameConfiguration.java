package game.setup;

import java.util.List;

/**
 * Stores setup data for the game.
 *
 * @author Hew Jia Jing
 */
public class GameConfiguration {

    /**
     * Player setup configurations.
     */
    private final List<PlayerConfiguration> playerConfigs;

    /**
     * Selected map type for the game.
     */
    private final MapType mapType;

    /**
     * Creates a game configuration.
     *
     * @param playerConfigs the list of player setup configurations
     * @param mapType       the selected map type
     */
    public GameConfiguration(List<PlayerConfiguration> playerConfigs, MapType mapType) {
        this.playerConfigs = List.copyOf(playerConfigs);
        this.mapType = mapType;
    }

    /**
     * Returns the player setup configurations.
     *
     * @return list of player configurations
     */
    public List<PlayerConfiguration> getPlayerConfigs() {
        return playerConfigs;
    }

    /**
     * Returns the map type for the game.
     *
     * @return the map type
     */
    public MapType getMapType() {
        return mapType;
    }
}
