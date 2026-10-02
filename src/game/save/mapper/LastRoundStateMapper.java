package game.save.mapper;

import game.character.Player;
import game.main.Game;
import game.save.memento.GameMemento;

/**
 * Maps last-round state between a live game and its memento representation.
 *
 * @author Hew Jia Jing
 */
public class LastRoundStateMapper {

    /**
     * Mapper used to resolve players during restoration.
     */
    private final PlayerStateMapper playerMapper;

    /**
     * Creates a last-round state mapper.
     *
     * @param playerMapper mapper used to locate players by name
     */
    public LastRoundStateMapper(PlayerStateMapper playerMapper) {
        this.playerMapper = playerMapper;
    }

    /**
     * Saves last-round state into the given memento.
     *
     * @param game the game whose last-round state should be saved
     * @param memento the destination memento
     */
    public void save(Game game, GameMemento memento) {
        memento.setLastRoundTriggered(
                game.getLastRoundTracker().isTriggered()
        );

        memento.setLastRoundTurnsRemaining(
                game.getLastRoundTracker().getTurnsRemaining()
        );

        if (game.getLastRoundTracker().getTriggeringPlayer() != null) {
            memento.setLastRoundTriggeringPlayer(
                    game.getLastRoundTracker()
                            .getTriggeringPlayer()
                            .getName()
            );
        }
    }

    /**
     * Restores last-round state from the given memento.
     *
     * @param game the game to restore
     * @param memento the saved game state
     */
    public void restore(Game game, GameMemento memento) {
        Player triggeringPlayer = null;

        if (memento.getLastRoundTriggeringPlayer() != null) {
            triggeringPlayer =
                    playerMapper.findPlayerByName(
                            game,
                            memento.getLastRoundTriggeringPlayer()
                    );
        }

        game.getLastRoundTracker().restore(
                memento.isLastRoundTriggered(),
                memento.getLastRoundTurnsRemaining(),
                triggeringPlayer
        );
    }
}
