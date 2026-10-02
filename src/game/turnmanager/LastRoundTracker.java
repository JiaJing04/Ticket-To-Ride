package game.turnmanager;

import game.character.Player;
import game.command.EndTurnOutcome;

/**
 * Tracks and manages the final round state of the game.
 *
 * @author Ng Zi Wei
 */
public class LastRoundTracker {

    /**
     * Remaining train pieces required to trigger the final round.
     */
    private static final int TRAIN_PIECE_THRESHOLD = 2;

    /**
     * Whether the final round has been triggered.
     */
    private boolean triggered;

    /**
     * Number of turns remaining before the game ends.
     */
    private int turnsRemaining;

    /**
     * Player who triggered the final round.
     */
    private Player triggeringPlayer;

    /**
     * Constructs a new turn tracker.
     */
    public LastRoundTracker() {
        this.triggered = false;
        this.turnsRemaining = 0;
        this.triggeringPlayer = null;
    }

    /**
     * Ends the current player's turn and determines the game state.
     * And trigger the final round, decrement remaining turns, or declare
     * that the game has ended.
     *
     * @param currentPlayer the player who just completed their turn
     * @param totalPlayers  the total number of players in the game
     * @return the outcome of ending the turn
     */
    public EndTurnOutcome endTurn(Player currentPlayer, int totalPlayers) {
        boolean justTriggered = checkAndTrigger(currentPlayer, totalPlayers);

        if (isTriggered() && !justTriggered) {
            decrementRemainingTurn();
        }

        if (isOver()) {
            return EndTurnOutcome.GAME_OVER;
        }

        if (justTriggered) {
            return EndTurnOutcome.LAST_ROUND_TRIGGERED;
        } else if (isTriggered()) {
            return EndTurnOutcome.LAST_ROUND_CONTINUES;
        } else {
            return EndTurnOutcome.NEXT_PLAYER_TURN;
        }
    }

    /**
     * Checks and triggers the final round based on the current player state.
     *
     * @param currentPlayer the player who just finished their turn
     * @param totalPlayers  the total number of players in the game
     * @return true if the final round was just triggered
     */
    private boolean checkAndTrigger(Player currentPlayer, int totalPlayers) {
        if (triggered) {
            return false;
        }

        // Checks if the condition of triggering the last round is achieved
        if (currentPlayer.getRemainingTrainPieces() <= TRAIN_PIECE_THRESHOLD) {
            this.triggered = true;
            this.triggeringPlayer = currentPlayer;
            this.turnsRemaining = totalPlayers;
            return true;
        }

        return false;
    }

    /**
     * Returns whether the final round has been triggered.
     *
     * @return true if the final round is triggered
     */
    public boolean isTriggered() {
        return triggered;
    }

    /**
     * Returns the player who triggered the final round.
     *
     * @return the triggering player
     */
    public Player getTriggeringPlayer() {
        return triggeringPlayer;
    }

    /**
     * Decrements the remaining turns in the final round.
     */
    private void decrementRemainingTurn() {
        if (turnsRemaining > 0) {
            turnsRemaining--;
        }
    }

    /**
     * Returns whether the final round has ended.
     *
     * @return true if the final round is over
     */
    public boolean isOver() {
        return triggered && turnsRemaining <= 0;
    }

    /**
     * Returns the number of turns remaining in the last round.
     *
     * @return the number of turns remaining
     */
    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    /**
     * Restores the state of the last-round tracker.
     *
     * @param triggered whether the last round has been triggered
     * @param turnsRemaining the number of turns remaining in the last round
     * @param triggeringPlayer the player who triggered the last round,
     *     or null if no player has triggered it
     */
    public void restore(boolean triggered,
                        int turnsRemaining,
                        Player triggeringPlayer) {
        this.triggered = triggered;
        this.turnsRemaining = turnsRemaining;
        this.triggeringPlayer = triggeringPlayer;
    }
}
