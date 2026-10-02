package game.main;

import game.character.Player;
import game.command.ActionResult;
import game.command.EndTurnOutcome;
import game.main.dto.GameResultDTO;
import game.main.dto.TicketDecisionCardDTO;
import game.pendingdecision.PendingDecision;
import game.pendingdecision.TicketDecision;
import game.request.ActionRequest;
import game.save.GameSerializer;
import game.save.SaveLoadService;
import game.setup.GameConfiguration;
import game.setup.GameSetupDirector;
import game.ui.GameUI;

import java.util.List;

/**
 * Coordinates game setup and gameplay requests.
 *
 * @author Hew Jia Jing
 */
public class GameController implements PendingDecisionVisitor {

    /**
     * Current game instance managed by this controller.
     */
    private Game game;

    /**
     * UI used to display game state and player prompts.
     */
    private GameUI gameUI;

    /**
     * Service responsible for saving and loading game state.
     */
    private final SaveLoadService saveLoadService;

    /**
     * Creates a controller before the game is started.
     */
    public GameController() {
        this.saveLoadService =
                new SaveLoadService(new GameSerializer());
    }

    /**
     * Sets the UI used by this controller.
     *
     * @param gameUI UI used to display game state and prompts
     */
    public void setGameUI(GameUI gameUI) {
        this.gameUI = gameUI;
    }

    /**
     * Starts a new game using the given configuration.
     *
     * @param config the game setup configuration
     */
    public void onStartGame(GameConfiguration config) {
        GameSetupDirector director = new GameSetupDirector();
        game = director.constructGame(config);
    }

    /**
     * Starts the setup phase for all players.
     */
    public void beginSetupPhase() {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            game.triggerSetupForCurrentPlayer();
            notifyPendingDecision(game.getPendingDecision());
        }
        gameUI.showMessage("Setup complete! Let the game begin.");
    }

    /**
     * Handles an action request from the UI.
     *
     * @param req action request
     */
    public void handleRequest(ActionRequest req) {
        ActionResult result = game.play(req);

        handleActionResult(result);
    }

    /**
     * Handles an action result.
     *
     * @param result action result
     */
    private void handleActionResult(ActionResult result) {
        switch (result.getStatus()) {
            case SUCCESS -> {
                gameUI.showMessage(result.getMessage());
                gameUI.refresh();
            }

            case PENDING -> notifyPendingDecision(result.getPendingDecision());

            case FAILED -> notifyFail(result);

            case PENDING_FAILED -> {
                gameUI.showMessage(result.getMessage());
                notifyPendingDecision(result.getPendingDecision());
            }
        }

        if (result.getEndTurnOutcome() != null &&
                result.getEndTurnOutcome() == EndTurnOutcome.LAST_ROUND_TRIGGERED) {

            gameUI.showLastRoundWarning();
        }

        if (result.getEndTurnOutcome() != null &&
                result.getEndTurnOutcome() == EndTurnOutcome.GAME_OVER) {

            List<Player> leaderboard = game.playerLeaderboard();
            Player winner = game.getWinner();

            gameUI.showEndGameResult(GameResultDTO.getLeaderboardDTO(winner, leaderboard));
        }
    }

    /**
     * Returns the current game.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Notifies the UI of a failed action.
     *
     * @param result action result containing the failure message
     */
    private void notifyFail(ActionResult result) {
        gameUI.showError(result.getMessage());
    }

    /**
     * Notifies the UI of a pending decision.
     *
     * @param decision pending decision to display
     */
    private void notifyPendingDecision(PendingDecision decision) {

        decision.accept(this);
    }

    /**
     * Notifies the UI to shows the ticket selection prompt.
     *
     * @param decision ticket decision to display
     */
    @Override
    public void visitTicketDecision(TicketDecision decision) {
        gameUI.promptTicketSelection(TicketDecisionCardDTO.convertModels2DTOs(decision.getDrawnCards()), decision.getMinKeep());
    }

    /**
     * Saves the current game state to a file.
     *
     * @param filePath path of the save file
     */
    public void saveGame(String filePath) {

        try {
            saveLoadService.saveGame(game, filePath);

            gameUI.showMessage("Game saved successfully.");

        } catch (Exception e) {

            gameUI.showError("Failed to save game: " + e.getMessage());
        }
    }

    /**
     * Loads a saved game from file.
     *
     * @param filePath path of the save file
     * @throws Exception if loading fails
     */
    public void loadGame(String filePath) throws Exception {
        this.game = saveLoadService.loadGame(filePath);
    }
}
