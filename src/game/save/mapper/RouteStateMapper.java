package game.save.mapper;

import game.character.Player;
import game.main.Game;
import game.map.Route;
import game.save.memento.GameMemento;
import game.save.memento.RouteMemento;

/**
 * Maps route ownership state between live game objects and their memento
 * representations.
 *
 * @author Hew Jia Jing
 */
public class RouteStateMapper {

    /**
     * Mapper used to resolve players during restoration.
     */
    private final PlayerStateMapper playerMapper;

    /**
     * Creates a route state mapper.
     *
     * @param playerMapper mapper used to locate players by name
     */
    public RouteStateMapper(PlayerStateMapper playerMapper) {
        this.playerMapper = playerMapper;
    }

    /**
     * Saves all claimed-route state into the given memento.
     *
     * @param game the game whose route state should be saved
     * @param memento the destination memento
     */
    public void save(Game game, GameMemento memento) {
        for (Route route : game.getBoard().getRoutes()) {
            if (route.isClaimed()) {
                RouteMemento snapshot = new RouteMemento();

                snapshot.setRouteId(route.getId());
                snapshot.setOwnerName(route.getOwner().getName());

                memento.getClaimedRoutes().add(snapshot);
            }
        }
    }

    /**
     * Restores claimed-route state from the given memento.
     *
     * @param game the game to restore
     * @param memento the saved game state
     * @throws IllegalArgumentException if a saved route cannot be found
     *     in the restored game
     */
    public void restore(Game game, GameMemento memento) {
        for (RouteMemento snapshot : memento.getClaimedRoutes()) {
            Route route =
                    game.getBoard().getRouteById(snapshot.getRouteId());

            if (route == null) {
                throw new IllegalArgumentException(
                        "Route not found: " + snapshot.getRouteId()
                );
            }

            Player owner =
                    playerMapper.findPlayerByName(
                            game,
                            snapshot.getOwnerName()
                    );

            route.setOwner(owner);
        }
    }
}
