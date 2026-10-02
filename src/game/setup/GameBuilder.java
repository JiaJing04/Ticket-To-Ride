package game.setup;

import game.main.Game;

import java.util.List;

/**
 * Defines steps to build a game.
 *
 * @author Hew Jia Jing
 */
public interface GameBuilder {

    /**
     * Builds the board.
     *
     * @param playerCount The player count in game
     */
    void buildBoard(int playerCount);

    /**
     * Builds the decks.
     */
    void buildDecks();

    /**
     * Builds players.
     *
     * @param playerConfigs player setup information
     */
    void buildPlayers(List<PlayerConfiguration> playerConfigs);

    /**
     * Builds the services.
     */
    void buildServices();

    /**
     * Deals starting cards to players.
     */
    void dealStartingHands();

    /**
     * Returns the constructed game.
     *
     * @param mapType the map type used by the game
     * @return completed game instance
     */
    Game getResult(MapType mapType);
}
