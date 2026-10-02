package game.setup;

import game.main.Game;

/**
 * Coordinates construction of a game from a game configuration.
 *
 * @author Hew Jia Jing
 */
public class GameSetupDirector {

    /**
     * Constructs a complete game using the selected setup configuration.
     *
     * @param config the game setup configuration
     * @return the constructed game
     */
    public Game constructGame(GameConfiguration config) {
        return construct(config, true);
    }

    /**
     * Constructs a game for loading a saved state.
     *
     * @param config the game setup configuration
     * @return the constructed game
     */
    public Game constructGameForLoad(GameConfiguration config) {
        return construct(config, false);
    }

    /**
     * Performs the common game-construction workflow.
     *
     * @param config the game setup configuration
     * @param dealStartingHands whether starting cards should be dealt
     * @return the constructed game
     */
    private Game construct(GameConfiguration config, boolean dealStartingHands) {
        GameBuilder builder = createBuilder(config.getMapType());

        builder.buildBoard(config.getPlayerConfigs().size());
        builder.buildDecks();
        builder.buildPlayers(config.getPlayerConfigs());
        builder.buildServices();

        if (dealStartingHands) {
            builder.dealStartingHands();
        }

        return builder.getResult(config.getMapType());
    }

    /**
     * Creates a game builder for the specified map type.
     *
     * @param mapType the map type to build
     * @return a builder capable of constructing that map type
     */
    private GameBuilder createBuilder(MapType mapType) {
        return switch (mapType) {
            case LONDON -> new LondonGameBuilder();
        };
    }
}
