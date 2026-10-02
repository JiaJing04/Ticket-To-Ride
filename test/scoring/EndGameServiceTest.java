package scoring;

import game.character.Player;
import game.character.PlayerColour;
import game.endgame.DestinationTicketScoring;
import game.endgame.EndGameService;
import game.endgame.LongestRouteScoring;
import game.map.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class EndGameServiceTest {

    private EndGameService endGameService;

    private Board board;

    /**
     * Test if player with the highest score are the winner
     */
    @Test
    public void testDetermineWinner_highestScoreWinner() {
        Player player1 = new Player("Player1", PlayerColour.BLACK, 10);
        Player player2 = new Player("Player2", PlayerColour.BLUE, 10);

        player1.addScore(100);
        player2.addScore(90);

        endGameService = new EndGameService();

        Player winnerPlayer = endGameService.determineWinner(List.of(player1, player2), board);

        Assertions.assertEquals(player1, winnerPlayer);
    }

    /**
     * Test tiebreaker condition for DestinationTicketScoring when determine winner
     */
    @Test
    public void testDetermineWinner_destTieBreaker() throws IllegalAccessException, NoSuchFieldException {
        Player player1 = new Player("Player1", PlayerColour.BLACK, 10);
        Player player2 = new Player("Player2", PlayerColour.BLUE, 10);

        player1.addScore(100);
        player2.addScore(100);

        DestinationTicketScoring destScoring = new DestinationTicketScoring(1);

        Field field = DestinationTicketScoring.class.getDeclaredField("playerDestCompletedMap");
        field.setAccessible(true);
        field.set(destScoring, Map.of(player1, 3, player2, 1));

        endGameService = new EndGameService();
        endGameService.addEndGameScoringStrategy(destScoring);
        endGameService.addTieBreakerStrategy(destScoring);

        Player winnerPlayer = endGameService.determineWinner(List.of(player1, player2), board);

        Assertions.assertEquals(player1, winnerPlayer);
    }

    /**
     * Test tiebreaker condition for LongestRouteScoring when determine winner
     */
    @Test
    public void testDetermineWinner_longestTieBreaker() throws NoSuchFieldException, IllegalAccessException {
        Player player1 = new Player("Player1", PlayerColour.BLACK, 10);
        Player player2 = new Player("Player2", PlayerColour.BLUE, 10);

        player1.addScore(100);
        player2.addScore(100);

        DestinationTicketScoring destScoring = new DestinationTicketScoring(1);

        Field field = DestinationTicketScoring.class.getDeclaredField("playerDestCompletedMap");
        field.setAccessible(true);
        field.set(destScoring, Map.of(player1, 3, player2, 3));

        LongestRouteScoring longestRouteScoring = new LongestRouteScoring(2);
        Field field2 = LongestRouteScoring.class.getDeclaredField("playersWithLongestRoute");
        field2.setAccessible(true);
        field2.set(longestRouteScoring, List.of(player1));

        endGameService = new EndGameService();
        endGameService.addEndGameScoringStrategy(destScoring);
        endGameService.addEndGameScoringStrategy(longestRouteScoring);
        endGameService.addTieBreakerStrategy(destScoring);
        endGameService.addTieBreakerStrategy(longestRouteScoring);

        Player winnerPlayer = endGameService.determineWinner(List.of(player1, player2), board);

        Assertions.assertEquals(player1, winnerPlayer);
    }

    /**
     * Test DRAW outcome when determine winner
     * when both Player has the same score, number of destTicket completed and both awarded longest route bonus
     */
    @Test
    public void testDetermineWinner_draw() throws NoSuchFieldException, IllegalAccessException {
        Player player1 = new Player("Player1", PlayerColour.BLACK, 10);
        Player player2 = new Player("Player2", PlayerColour.BLUE, 10);

        player1.addScore(100);
        player2.addScore(100);

        DestinationTicketScoring destScoring = new DestinationTicketScoring(1);

        Field field = DestinationTicketScoring.class.getDeclaredField("playerDestCompletedMap");
        field.setAccessible(true);
        field.set(destScoring, Map.of(player1, 3, player2, 3));

        LongestRouteScoring longestRouteScoring = new LongestRouteScoring(2);
        Field field2 = LongestRouteScoring.class.getDeclaredField("playersWithLongestRoute");
        field2.setAccessible(true);
        field2.set(longestRouteScoring, List.of(player1, player2));

        endGameService = new EndGameService();
        endGameService.addEndGameScoringStrategy(destScoring);
        endGameService.addEndGameScoringStrategy(longestRouteScoring);
        endGameService.addTieBreakerStrategy(destScoring);
        endGameService.addTieBreakerStrategy(longestRouteScoring);

        Player winnerPlayer = endGameService.determineWinner(List.of(player1, player2), board);

        Assertions.assertNull(winnerPlayer);
    }

    /**
     * Test tiebreaker condition where the TieBreaker executed according to Order
     */
    @Test
    public void testDetermineWinner_tieBreakerOrder() throws IllegalAccessException, NoSuchFieldException {
        Player player1 = new Player("Player1", PlayerColour.BLACK, 10);
        Player player2 = new Player("Player2", PlayerColour.BLUE, 10);

        player1.addScore(100);
        player2.addScore(100);

        DestinationTicketScoring destScoring = new DestinationTicketScoring(1);

        Field field = DestinationTicketScoring.class.getDeclaredField("playerDestCompletedMap");
        field.setAccessible(true);
        field.set(destScoring, Map.of(player1, 3, player2, 1));

        LongestRouteScoring longestRouteScoring = new LongestRouteScoring(2);
        Field field2 = LongestRouteScoring.class.getDeclaredField("playersWithLongestRoute");
        field2.setAccessible(true);
        field2.set(longestRouteScoring, List.of(player2));

        endGameService = new EndGameService();
        // longest route being added first into the tieBreakerList
        // test if winner comes from DestScoring (player1)
        endGameService.addEndGameScoringStrategy(longestRouteScoring);
        endGameService.addTieBreakerStrategy(longestRouteScoring);
        endGameService.addEndGameScoringStrategy(destScoring);
        endGameService.addTieBreakerStrategy(destScoring);

        Player winnerPlayer = endGameService.determineWinner(List.of(player1, player2), board);

        Assertions.assertEquals(player1, winnerPlayer);
    }
}
