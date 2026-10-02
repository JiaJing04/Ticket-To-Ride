package turnManager;

import game.character.Player;
import game.character.PlayerColour;
import game.command.EndTurnOutcome;
import game.turnmanager.LastRoundTracker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LastRoundTrackerTest {

    private LastRoundTracker tracker;
    private Player player;

    // this test will assume a two player game
    private static final int TOTAL_PLAYER = 2;

    @BeforeEach
    public void setUp() {
        tracker = new LastRoundTracker();
        player = new Player("Miranda", PlayerColour.BLUE, 10);
    }

    @Test
    public void testConstructor_initialState() {
        Assertions.assertFalse(tracker.isTriggered());
        Assertions.assertFalse(tracker.isOver());
        Assertions.assertNull(tracker.getTriggeringPlayer());
    }

    @Test
    public void testCheckAndTrigger_whenPlayerHasMoreThanTwoTrainPieces_returnsNextPlayerTurn() {
        EndTurnOutcome outcome = tracker.endTurn(player, TOTAL_PLAYER);

        Assertions.assertEquals(EndTurnOutcome.NEXT_PLAYER_TURN, outcome);
        Assertions.assertFalse(tracker.isTriggered());
        Assertions.assertNull(tracker.getTriggeringPlayer());
    }

    @Test
    public void testCheckAndTrigger_whenPlayerHasTwoTrainPieces_triggersLastRound() {
        player.reduceTrainPieces(8);

        EndTurnOutcome outcome = tracker.endTurn(player, TOTAL_PLAYER);

        Assertions.assertEquals(EndTurnOutcome.LAST_ROUND_TRIGGERED, outcome);
        Assertions.assertTrue(tracker.isTriggered());
        Assertions.assertEquals(player, tracker.getTriggeringPlayer());
        Assertions.assertFalse(tracker.isOver());
    }

    @Test
    public void testCheckAndTrigger_whenPlayerHasLessThanTwoTrainPieces_triggersLastRound() {
        player.reduceTrainPieces(9);

        EndTurnOutcome outcome = tracker.endTurn(player, TOTAL_PLAYER);

        Assertions.assertEquals(EndTurnOutcome.LAST_ROUND_TRIGGERED, outcome);
        Assertions.assertTrue(tracker.isTriggered());
        Assertions.assertEquals(player, tracker.getTriggeringPlayer());
    }

    @Test
    public void testCheckAndTrigger_whenAlreadyTriggered_returnsFalseAndKeepsOriginalPlayer() {
        Player secondPlayer = new Player("Emily", PlayerColour.BLUE, 1);

        player.reduceTrainPieces(8);
        EndTurnOutcome outcomeFirst = tracker.endTurn(player, TOTAL_PLAYER);

        EndTurnOutcome outcomeSecond = tracker.endTurn(secondPlayer, TOTAL_PLAYER);

        Assertions.assertEquals(EndTurnOutcome.LAST_ROUND_TRIGGERED, outcomeFirst);
        Assertions.assertEquals(EndTurnOutcome.LAST_ROUND_CONTINUES, outcomeSecond);
        Assertions.assertEquals(player, tracker.getTriggeringPlayer());
    }

    @Test
    public void testEndTurn_beforeTriggered_doesNotMakeGameOver() {
        tracker.endTurn(player, TOTAL_PLAYER);

        Assertions.assertFalse(tracker.isOver());
    }

    @Test
    public void testIsOver_afterCountdownReachesZero_returnsTrue() {
        player.reduceTrainPieces(8);
        // first player trigger end game
        tracker.endTurn(player, TOTAL_PLAYER);

        // second player take a turn
        tracker.endTurn(new Player("Player2", PlayerColour.BLACK, 10), TOTAL_PLAYER);

        Assertions.assertFalse(tracker.isOver());

        // triggering player take the last turn
        tracker.endTurn(player, TOTAL_PLAYER);

        Assertions.assertTrue(tracker.isOver());
    }

    @Test
    public void testDecrementRemainingTurn_doesNotGoBelowZeroEffectively() {
        player.reduceTrainPieces(8);
        tracker.endTurn(player, TOTAL_PLAYER);

        tracker.endTurn(player, TOTAL_PLAYER);
        tracker.endTurn(player, TOTAL_PLAYER);

        Assertions.assertTrue(tracker.isOver());
    }
}
