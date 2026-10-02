package game.main.dto;

import game.character.Player;

import java.util.List;

/**
 * DTO class to sent to frontend for displaying winner
 *
 * @author Jia Ming
 */
public class GameResultDTO {
    
    /**
     * if the game is draw
     */
    private boolean isDraw;

    /**
     * winner of the Game, null if draw
     */
    private Player winner;

    /**
     * list of all players, to display each player score
     */
    private List<Player> leaderboard;

    /**
     * Constructs a DTO for a game result.
     *
     * @param winner      winner Player, null if none
     * @param leaderboard list of all Player in the game
     */
    public GameResultDTO(Player winner, List<Player> leaderboard) {
        this.isDraw = winner == null;
        this.winner = winner;
        this.leaderboard = leaderboard;
    }

    /**
     * Convert into DTO using winner and list Player
     *
     * @param winner      winner Player, null if none
     * @param leaderboard list of all Player in the game
     * @return DTO for leaderboard
     */
    public static GameResultDTO getLeaderboardDTO(Player winner, List<Player> leaderboard) {
        return new GameResultDTO(winner, leaderboard);
    }

    /**
     * Returns if the game is draw
     *
     * @return boolean, true if draw, else false
     */
    public boolean getIsDraw() {
        return isDraw;
    }

    /**
     * Returns winner of the game, null if draw
     *
     * @return winner or null
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Returns full list of all Player in the game
     *
     * @return list of all Player in game
     */
    public List<Player> getLeaderboard() {
        return leaderboard;
    }
}
