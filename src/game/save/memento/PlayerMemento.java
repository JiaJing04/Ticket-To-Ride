package game.save.memento;

import game.character.PlayerColour;

import java.util.ArrayList;
import java.util.List;

/**
 * Memento object that stores the saved state of a player.
 *
 * @author Hew Jia Jing
 */
public class PlayerMemento {

    /**
     * Player name.
     */
    private String name;

    /**
     * Player colour.
     */
    private PlayerColour colour;

    /**
     * Player score.
     */
    private int score;

    /**
     * Number of train pieces remaining.
     */
    private int remainingTrainPieces;

    /**
     * Name of the player's special ability.
     */
    private String abilityName;

    /**
     * Whether the player currently has a double turn.
     */
    private boolean hasDoubleTurn;

    /**
     * IDs of train cards held by the player.
     */
    private final List<Integer> trainCardIds =
            new ArrayList<>();

    /**
     * IDs of destination cards held by the player.
     */
    private final List<Integer> destinationCardIds =
            new ArrayList<>();

    /**
     * Returns the player's name.
     *
     * @return the player name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player's name.
     *
     * @param name the player name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the player's colour.
     *
     * @return the player colour
     */
    public PlayerColour getColour() {
        return colour;
    }

    /**
     * Sets the player's colour.
     *
     * @param colour the player colour
     */
    public void setColour(PlayerColour colour) {
        this.colour = colour;
    }

    /**
     * Returns the player's score.
     *
     * @return the player score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the player's score.
     *
     * @param score the player score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the number of train pieces remaining.
     *
     * @return the remaining train pieces
     */
    public int getRemainingTrainPieces() {
        return remainingTrainPieces;
    }

    /**
     * Sets the number of train pieces remaining.
     *
     * @param remainingTrainPieces the remaining train pieces
     */
    public void setRemainingTrainPieces(int remainingTrainPieces) {
        this.remainingTrainPieces = remainingTrainPieces;
    }

    /**
     * Returns the player's ability name.
     *
     * @return the ability name
     */
    public String getAbilityName() {
        return abilityName;
    }

    /**
     * Sets the player's ability name.
     *
     * @param abilityName the ability name
     */
    public void setAbilityName(String abilityName) {
        this.abilityName = abilityName;
    }

    /**
     * Returns whether the player has a double turn.
     *
     * @return {@code true} if the player has a double turn;
     *     {@code false} otherwise
     */
    public boolean hasDoubleTurn() {
        return hasDoubleTurn;
    }

    /**
     * Sets whether the player has a double turn.
     *
     * @param hasDoubleTurn the double-turn state
     */
    public void setHasDoubleTurn(boolean hasDoubleTurn) {
        this.hasDoubleTurn = hasDoubleTurn;
    }

    /**
     * Returns the IDs of train cards held by the player.
     *
     * @return the train card IDs
     */
    public List<Integer> getTrainCardIds() {
        return trainCardIds;
    }

    /**
     * Returns the IDs of destination cards held by the player.
     *
     * @return the destination card IDs
     */
    public List<Integer> getDestinationCardIds() {
        return destinationCardIds;
    }
}
