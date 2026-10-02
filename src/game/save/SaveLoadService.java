package game.save;

import game.main.Game;
import game.save.mapper.GameStateMapper;
import game.save.memento.GameMemento;
import game.turnmanager.TurnMediator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles saving and loading game state to and from persistent storage.
 *
 * @author Hew Jia Jing
 */
public class SaveLoadService {

    private static final Path SAVE_DIRECTORY = Path.of("saves");

    /**
     * Serializer used to convert game state to and from text.
     */
    private final GameSerializer serializer;

    /**
     * Mapper used to convert between Game instances and mementos.
     */
    private final GameStateMapper mapper;

    /**
     * Creates a save/load service.
     *
     * @param serializer serializer used for persistence
     */
    public SaveLoadService(GameSerializer serializer) {
        this.serializer = serializer;
        this.mapper = new GameStateMapper();
    }

    /**
     * Saves the current game state to a file.
     *
     * @param game the game to save
     * @param fileName the file name to be saved as
     * @throws IOException if an error occurs while writing the save file
     * @throws IllegalStateException if saving is not currently allowed
     */
    public void saveGame(Game game, String fileName) throws IOException {
        validateSaveAllowed(game);

        Files.createDirectories(SAVE_DIRECTORY);

        GameMemento memento = mapper.createMemento(game);
        String data = serializer.serialize(memento);

        Path savePath = SAVE_DIRECTORY.resolve(fileName);

        Files.writeString(savePath, data);
    }

    /**
     * Loads a game state from a save file.
     *
     * @param fileName the name of the save file
     * @return the restored game
     * @throws IOException if an error occurs while reading the save file
     */
    public Game loadGame(String fileName) throws IOException {

        Path savePath = SAVE_DIRECTORY.resolve(fileName);

        String data = Files.readString(savePath);

        GameMemento memento = serializer.deserialize(data);

        return mapper.restore(memento);
    }

    /**
     * Validates whether the game can be saved at its current state.
     *
     * @param game the game to validate
     * @throws IllegalStateException if saving is not permitted
     */
    private void validateSaveAllowed(Game game) {
        TurnMediator mediator = game.getTurnMediator();

        boolean noActionTaken = mediator.hasFullActionCost();
        boolean noPendingDecision = mediator.getPendingDecision() == null;

        if (!(noActionTaken && noPendingDecision)) {
            throw new IllegalStateException(
                    "Game can only be saved before the current player acts."
            );
        }
    }
}
