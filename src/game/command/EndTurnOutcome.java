package game.command;

/**
 * Represents the outcome of ending a turn.
 *
 * @author Ng Zi Wei
 */
public enum EndTurnOutcome {

    NEXT_PLAYER_TURN,
    LAST_ROUND_TRIGGERED,
    LAST_ROUND_CONTINUES,
    GAME_OVER
}
