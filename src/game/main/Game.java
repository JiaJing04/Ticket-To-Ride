package game.main;

import game.character.Player;
import game.command.ActionCommand;
import game.command.ActionResult;
import game.command.DrawDestCommand;
import game.command.EndTurnOutcome;
import game.deck.DestinationCardDeck;
import game.deck.TrainCardDeck;
import game.endgame.EndGameService;
import game.map.Board;
import game.pendingdecision.PendingDecision;
import game.request.ActionRequest;
import game.setup.MapType;
import game.strategy.routescoring.RouteScoringStrategy;
import game.turnmanager.LastRoundTracker;
import game.turnmanager.TurnMediator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents the main game state and coordinates gameplay.
 *
 * @author Hew Jia Jing
 */
public class Game {

    /**
     * Players in turn order.
     */
    private final List<Player> players;

    /**
     * Game board.
     */
    private final Board board;

    /**
     * Train card deck.
     */
    private final TrainCardDeck trainCardDeck;

    /**
     * Destination card deck.
     */
    private final DestinationCardDeck destinationCardDeck;

    /**
     * Manages per-turn action state.
     */
    private final TurnMediator turnMediator;

    /**
     * Tracks end-game triggering and remaining turns.
     */
    private final LastRoundTracker lastRoundTracker;

    /**
     * Handles end-game scoring logic.
     */
    private final EndGameService endGameService;

    /**
     * Strategy for scoring claimed routes.
     */
    private final RouteScoringStrategy routeScoringStrategy;

    /**
     * Index of the current player.
     */
    private int currentPlayerIndex;

    /**
     * Winner of the game after completion.
     */
    private Player winner;

    /**
     * Whether the game is currently in setup phase.
     */
    private boolean isSetupPhase = true;

    /**
     * Type of map being used for the game.
     */
    private final MapType mapType;

    /**
     * Creates a game instance.
     *
     * @param players              players in turn order
     * @param board                game board
     * @param trainCardDeck        train card deck
     * @param destinationCardDeck  destination card deck
     * @param turnMediator         manages turn state
     * @param lastRoundTracker     tracks end-game triggering and remaining turns
     * @param endGameService       end-game scoring service
     * @param routeScoringStrategy route scoring strategy
     * @param mapType              map type used
     */
    public Game(
            List<Player> players,
            Board board,
            TrainCardDeck trainCardDeck,
            DestinationCardDeck destinationCardDeck,
            TurnMediator turnMediator,
            LastRoundTracker lastRoundTracker,
            EndGameService endGameService,
            RouteScoringStrategy routeScoringStrategy,
            MapType mapType) {
        this.players = List.copyOf(players);
        this.turnMediator = turnMediator;
        this.lastRoundTracker = lastRoundTracker;
        this.currentPlayerIndex = 0;
        this.board = board;
        this.trainCardDeck = trainCardDeck;
        this.destinationCardDeck = destinationCardDeck;
        this.endGameService = endGameService;
        this.routeScoringStrategy = routeScoringStrategy;
        this.mapType = mapType;
    }

    /**
     * Triggers the setup step for the current player.
     */
    public void triggerSetupForCurrentPlayer() {
        // Setup rule: keep minimum 2
        ActionCommand setupAction = new DrawDestCommand(destinationCardDeck, destinationCardDeck.getSetupMinKeep());
        turnMediator.processAction(setupAction, getCurrentPlayer());
    }

    /**
     * Executes an action request and returns the result.
     *
     * @param request request used to build the action command
     * @return result of the action execution
     */
    public ActionResult play(ActionRequest request) {
        Player currentPlayer = getCurrentPlayer();
        ActionCommand action = request.buildCommand(this);
        ActionResult actionResult = turnMediator.processAction(action, currentPlayer);

        if (isSetupPhase) {
            handleSetupResult(actionResult);
        } else {
            handleInGameResult(actionResult);
        }

        return actionResult;
    }

    /**
     * Handles action results during the setup phase.
     *
     * @param result the result of the executed action
     */
    private void handleSetupResult(ActionResult result) {
        if (result.isSuccess() && turnMediator.isTurnComplete()) {
            turnMediator.reset();
            advancePlayer();

            // If index wraps back to 0, everyone had exactly one setup turn.
            if (currentPlayerIndex == 0) {
                isSetupPhase = false;
            }
        }
    }

    /**
     * Handles action results during normal gameplay.
     *
     * @param result the result of the executed action
     */
    private void handleInGameResult(ActionResult result) {
        if (result.isSuccess() && turnMediator.isTurnComplete()) {
            EndTurnOutcome endTurnOutcome = lastRoundTracker.endTurn(getCurrentPlayer(), getPlayers().size());
            result.setEndTurnOutcome(endTurnOutcome);

            if (endTurnOutcome == EndTurnOutcome.GAME_OVER) {
                winner = resolveGameOver();
            }
            advanceTurn();
        }
    }

    /**
     * Advances to the next turn and resets turn state.
     */
    private void advanceTurn() {
        // Reset the turn
        turnMediator.reset();
        advancePlayer();
    }

    /**
     * Returns the current player.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Returns the turn mediator responsible for managing turn state
     * and action execution.
     *
     * @return the turn mediator
     */
    public TurnMediator getTurnMediator() {
        return turnMediator;
    }

    /**
     * Returns the current player index.
     *
     * @return the current player index
     */
    public Integer getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Resolves final scoring and returns players ordered by final result.
     *
     * @return players ordered by final score
     */
    public Player resolveGameOver() {
        endGameService.applyEndGameScoring(players, board);
        return endGameService.determineWinner(players, board);
    }

    /**
     * Returns the train card deck.
     *
     * @return the train card deck
     */
    public TrainCardDeck getTrainCardDeck() {
        return trainCardDeck;
    }

    /**
     * Returns the destination card deck.
     *
     * @return the destination card deck
     */
    public DestinationCardDeck getDestinationCardDeck() {
        return destinationCardDeck;
    }

    /**
     * Returns the board.
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns whether the game is currently in the setup phase.
     *
     * @return true if the game is in the setup phase; false otherwise
     */
    public boolean isSetupPhase() {
        return isSetupPhase;
    }

    /**
     * Returns the route scoring strategy.
     *
     * @return the route scoring strategy
     */
    public RouteScoringStrategy getRouteScoringStrategy() {
        return routeScoringStrategy;
    }

    /**
     * Returns all players in turn order.
     *
     * @return the players in the game
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Move to the next player's turn.
     */
    public void advancePlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Returns the current pending decision.
     *
     * @return The current pending decision.
     */
    public PendingDecision getPendingDecision() {
        return turnMediator.getPendingDecision();
    }

    /**
     * Returns the winner of the game.
     *
     * @return the winner, or null if the game is not finished
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Returns players ordered by leaderboard ranking.
     *
     * @return a list of players ordered by ranking
     */
    public List<Player> playerLeaderboard() {
        List<Player> leaderboard = new ArrayList<>(players);

        leaderboard.sort(Comparator.comparingInt(Player::getScore).reversed());

        if (winner != null) {
            leaderboard.remove(winner);
            leaderboard.add(0, winner);
        }

        return leaderboard;
    }

    /**
     * Restores game state information.
     *
     * @param currentPlayerIndex the index of the current player
     * @param setupPhase whether the game is in the setup phase
     */
    public void restoreGameState(int currentPlayerIndex, boolean setupPhase) {
        this.currentPlayerIndex = currentPlayerIndex;
        this.isSetupPhase = setupPhase;
    }

    /**
     * Returns the tracker responsible for managing last-round state.
     *
     * @return the last-round tracker
     */
    public LastRoundTracker getLastRoundTracker() {
        return lastRoundTracker;
    }

    /**
     * Returns the type of map being used in this game.
     *
     * @return the map type
     */
    public MapType getMapType() {
        return mapType;
    }
}
