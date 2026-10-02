package game.save.mapper;

import game.card.DestinationCard;
import game.card.TrainCard;
import game.main.Game;
import game.save.memento.GameMemento;
import game.setup.GameConfiguration;
import game.setup.GameSetupDirector;

import java.util.Map;

/**
 * Maps between live game objects and their memento representations.
 *
 * @author Hew Jia Jing
 */
public class GameStateMapper {

    /**
     * Handles player state persistence.
     */
    private final PlayerStateMapper playerMapper;

    /**
     * Handles route state persistence.
     */
    private final RouteStateMapper routeMapper;

    /**
     * Handles deck state persistence.
     */
    private final DeckStateMapper deckMapper;

    /**
     * Handles last-round state persistence.
     */
    private final LastRoundStateMapper lastRoundMapper;

    /**
     * Creates a game state mapper and its supporting mappers.
     */
    public GameStateMapper() {
        this.playerMapper = new PlayerStateMapper();
        this.routeMapper = new RouteStateMapper(playerMapper);
        this.deckMapper = new DeckStateMapper();
        this.lastRoundMapper = new LastRoundStateMapper(playerMapper);
    }

    /**
     * Creates a memento containing the current game state.
     *
     * @param game the game whose state should be saved
     * @return a memento representing the current game state
     */
    public GameMemento createMemento(Game game) {
        GameMemento memento = new GameMemento();

        memento.setMapType(game.getMapType());
        memento.setCurrentPlayerIndex(game.getCurrentPlayerIndex());
        memento.setRemainingActionCost(
                game.getTurnMediator().getRemainingActionCost()
        );
        memento.setSetupPhase(game.isSetupPhase());

        lastRoundMapper.save(game, memento);
        playerMapper.save(game, memento);
        routeMapper.save(game, memento);
        deckMapper.save(game, memento);

        return memento;
    }

    /**
     * Restores a game instance from the given memento.
     *
     * @param memento the saved game state
     * @return the restored game
     */
    public Game restore(GameMemento memento) {
        Game game = buildBaseGame(memento);

        Map<Integer, TrainCard> trainCardsById =
                deckMapper.indexTrainCards(game);

        Map<Integer, DestinationCard> destinationCardsById =
                deckMapper.indexDestinationCards(game);

        playerMapper.restore(
                game,
                memento,
                trainCardsById,
                destinationCardsById
        );

        routeMapper.restore(game, memento);

        deckMapper.restore(
                game,
                memento,
                trainCardsById,
                destinationCardsById
        );

        lastRoundMapper.restore(game, memento);

        game.restoreGameState(
                memento.getCurrentPlayerIndex(),
                memento.isSetupPhase()
        );

        game.getTurnMediator().setRemainingActionCost(
                memento.getRemainingActionCost()
        );

        return game;
    }

    /**
     * Constructs a base game instance from the saved configuration.
     *
     * @param memento the saved game state
     * @return a newly constructed game ready for state restoration
     */
    private Game buildBaseGame(GameMemento memento) {
        GameConfiguration config =
                new GameConfiguration(
                        playerMapper.toPlayerConfigs(memento),
                        memento.getMapType()
                );

        GameSetupDirector director = new GameSetupDirector();

        return director.constructGameForLoad(config);
    }
}
