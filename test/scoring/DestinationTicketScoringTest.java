package scoring;

import game.card.DestinationCard;
import game.character.Player;
import game.character.PlayerColour;
import game.map.Board;
import game.map.City;
import game.map.Route;
import game.endgame.DestinationTicketScoring;
import game.map.RouteColour;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Unit Test for DestinationTickerScoring scoring related
 *
 * @author Jia Ming
 */
public class DestinationTicketScoringTest {
    private DestinationTicketScoring destinationTicketScoring;

    private Board board;

    private Route routeA;

    private Route routeB;

    private Player playerA;

    private Player playerB;

    private DestinationCard destCardA;

    private DestinationCard destCardB;

    private final int DEST_CARD_A_POINTS = 10;

    private final int DEST_CARD_B_POINTS = 8;

    private final int DEST_SCORE_TIE_BREAKER_ORDER = 1;

    @BeforeEach
    public void setUp() {
        destinationTicketScoring = new DestinationTicketScoring(DEST_SCORE_TIE_BREAKER_ORDER);
        board = new Board();

        City cityA = new City("A", 1, 2);
        City cityB = new City("B", 3, 4);
        destCardA = new DestinationCard(1, cityA, cityB, DEST_CARD_A_POINTS);
        routeA = new Route("route", cityA, cityB, 5, null, null);

        City cityC = new City("C", 5, 6);
        City cityD = new City("D", 7, 8);
        destCardB = new DestinationCard(2, cityC, cityD, DEST_CARD_B_POINTS);
        routeB =  new Route("routeB", cityC, cityD, 8, null, null);

        playerA = new Player("A", PlayerColour.BLACK, 10);
        playerB = new Player("B", PlayerColour.BLACK, 10);

        board.addRoute(routeA);
        board.addRoute(routeB);
    }

    @Test
    public void testApply_success() {
        routeA.setOwner(playerA);
        routeB.setOwner(playerB);
        playerA.drawDestCard(List.of(destCardA));
        playerB.drawDestCard(List.of(destCardB));

        destinationTicketScoring.apply(List.of(playerA, playerB), board);

        Assertions.assertEquals(DEST_CARD_A_POINTS, playerA.getScore());
        Assertions.assertEquals(DEST_CARD_B_POINTS, playerB.getScore());
    }

    @Test
    public void testApply_successWithPlayerHasInitialScore() {
        routeA.setOwner(playerA);
        routeB.setOwner(playerB);
        playerA.drawDestCard(List.of(destCardA));
        playerB.drawDestCard(List.of(destCardB));

        playerA.addScore(10);
        playerB.addScore(10);

        destinationTicketScoring.apply(List.of(playerA, playerB), board);

        Assertions.assertEquals(DEST_CARD_A_POINTS + 10, playerA.getScore());
        Assertions.assertEquals(DEST_CARD_B_POINTS + 10, playerB.getScore());
    }

    @Test
    public void testApply_withPlayerHasNotCompletedDest() {
        playerA.drawDestCard(List.of(destCardA));
        playerB.drawDestCard(List.of(destCardB));

        playerA.addScore(10);
        playerB.addScore(10);

        destinationTicketScoring.apply(List.of(playerA, playerB), board);

        Assertions.assertEquals( 10 - DEST_CARD_A_POINTS, playerA.getScore());
        Assertions.assertEquals( 10 - DEST_CARD_B_POINTS, playerB.getScore());
    }

    @Test
    public void testApply_withPlayerHasMultipleAllCompletedDest() {
        routeA.setOwner(playerA);
        routeB.setOwner(playerA);

        playerA.drawDestCard(List.of(destCardA, destCardB));

        // set initial score
        playerA.addScore(10);
        playerB.addScore(10);

        destinationTicketScoring.apply(List.of(playerA, playerB), board);

        Assertions.assertEquals( 10 + DEST_CARD_A_POINTS + DEST_CARD_B_POINTS, playerA.getScore());
        Assertions.assertEquals( 10, playerB.getScore());
    }

    @Test
    public void testApply_playerWithCompletedAndIncompletedDest() {
        routeA.setOwner(playerA);

        playerA.drawDestCard(List.of(destCardA, destCardB));

        // set initial score
        playerA.addScore(10);
        playerB.addScore(10);

        destinationTicketScoring.apply(List.of(playerA, playerB), board);

        Assertions.assertEquals( 10 + DEST_CARD_A_POINTS - DEST_CARD_B_POINTS, playerA.getScore());
        Assertions.assertEquals( 10, playerB.getScore());
    }

    @Test
    public void testApply_playerWithClaimedRouteButIncompletedDest() {
        City city1 = new City("city-1", 1, 2);
        City city2 = new City("city-2", 3, 4);
        Route route = new Route("route", city1, city2, 5, RouteColour.BLACK, null);

        City city3 = new City("city-3", 5, 6);
        int destCardPoint = 5;
        DestinationCard destinationCard = new DestinationCard(1, city1, city3, destCardPoint);

        route.setOwner(playerA);
        playerA.drawDestCard(List.of(destinationCard));

        // set initial score
        playerA.addScore(10);
        playerB.addScore(10);

        destinationTicketScoring.apply(List.of(playerA, playerB), board);

        Assertions.assertEquals( 10 - destCardPoint, playerA.getScore());
        Assertions.assertEquals( 10, playerB.getScore());
    }
}
