package game.save.memento;

import game.setup.MapType;

import java.util.ArrayList;
import java.util.List;

/**
 * Memento object that stores all save-state information.
 *
 * @author Hew Jia Jing
 */
public class GameMemento {

    /**
     * Type of map used by the game.
     */
    private MapType mapType;

    /**
     * Index of the current player.
     */
    private int currentPlayerIndex;

    /**
     * Remaining action cost for the current turn.
     */
    private int remainingActionCost;

    /**
     * Whether the game is currently in the setup phase.
     */
    private boolean setupPhase;

    /**
     * Whether the last round has been triggered.
     */
    private boolean lastRoundTriggered;

    /**
     * Number of turns remaining in the last round.
     */
    private int lastRoundTurnsRemaining;

    /**
     * Name of the player who triggered the last round.
     */
    private String lastRoundTriggeringPlayer;

    /**
     * Saved player states.
     */
    private final List<PlayerMemento> players =
            new ArrayList<>();

    /**
     * Saved claimed-route states.
     */
    private final List<RouteMemento> claimedRoutes =
            new ArrayList<>();

    /**
     * IDs of train cards currently in the draw pile.
     */
    private final List<Integer> trainDeckCards =
            new ArrayList<>();

    /**
     * IDs of train cards currently in the discard pile.
     */
    private final List<Integer> trainDiscardCards =
            new ArrayList<>();

    /**
     * IDs of train cards currently in the face-up display.
     */
    private final List<Integer> trainFaceUpCards =
            new ArrayList<>();

    /**
     * IDs of destination cards currently in the draw pile.
     */
    private final List<Integer> destinationDeckCards =
            new ArrayList<>();

    /**
     * Returns the map type.
     *
     * @return the map type
     */
    public MapType getMapType() {
        return mapType;
    }

    /**
     * Sets the map type.
     *
     * @param mapType the map type
     */
    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    /**
     * Returns the current player index.
     *
     * @return the current player index
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Sets the current player index.
     *
     * @param currentPlayerIndex the current player index
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * Returns the remaining action cost for the current turn.
     *
     * @return the remaining action cost
     */
    public int getRemainingActionCost() {
        return remainingActionCost;
    }


    /**
     * Sets the remaining action cost.
     *
     * @param remainingActionCost the remaining action cost
     */
    public void setRemainingActionCost(int remainingActionCost) {
        this.remainingActionCost = remainingActionCost;
    }

    /**
     * Returns whether the game is in the setup phase.
     *
     * @return true if the game is in the setup phase;
     *     false otherwise
     */
    public boolean isSetupPhase() {
        return setupPhase;
    }

    /**
     * Sets whether the game is in the setup phase.
     *
     * @param setupPhase the setup-phase state
     */
    public void setSetupPhase(boolean setupPhase) {
        this.setupPhase = setupPhase;
    }

    /**
     * Returns whether the last round has been triggered.
     *
     * @return true if the last round has been triggered; false otherwise
     */
    public boolean isLastRoundTriggered() {
        return lastRoundTriggered;
    }

    /**
     * Sets whether the last round has been triggered.
     *
     * @param lastRoundTriggered the last-round state
     */
    public void setLastRoundTriggered(boolean lastRoundTriggered) {
        this.lastRoundTriggered = lastRoundTriggered;
    }

    /**
     * Returns the number of turns remaining in the last round.
     *
     * @return the remaining last-round turns
     */
    public int getLastRoundTurnsRemaining() {
        return lastRoundTurnsRemaining;
    }

    /**
     * Sets the number of turns remaining in the last round.
     *
     * @param lastRoundTurnsRemaining the remaining last-round turns
     */
    public void setLastRoundTurnsRemaining(int lastRoundTurnsRemaining) {
        this.lastRoundTurnsRemaining = lastRoundTurnsRemaining;
    }

    /**
     * Returns the name of the player who triggered the last round.
     *
     * @return the triggering player's name, or {@code null} if none
     */
    public String getLastRoundTriggeringPlayer() {
        return lastRoundTriggeringPlayer;
    }

    /**
     * Sets the name of the player who triggered the last round.
     *
     * @param lastRoundTriggeringPlayer the triggering player's name
     */
    public void setLastRoundTriggeringPlayer(String lastRoundTriggeringPlayer) {
        this.lastRoundTriggeringPlayer = lastRoundTriggeringPlayer;
    }

    /**
     * Returns the saved player states.
     *
     * @return the player mementos
     */
    public List<PlayerMemento> getPlayers() {
        return players;
    }

    /**
     * Returns the saved claimed-route states.
     *
     * @return the route mementos
     */
    public List<RouteMemento> getClaimedRoutes() {
        return claimedRoutes;
    }

    /**
     * Returns the IDs of train cards in the draw pile.
     *
     * @return the train draw-pile card IDs
     */
    public List<Integer> getTrainDeckCards() {
        return trainDeckCards;
    }

    /**
     * Returns the IDs of train cards in the discard pile.
     *
     * @return the train discard-pile card IDs
     */
    public List<Integer> getTrainDiscardCards() {
        return trainDiscardCards;
    }

    /**
     * Returns the IDs of train cards in the face-up display.
     *
     * @return the face-up train card IDs
     */
    public List<Integer> getTrainFaceUpCards() {
        return trainFaceUpCards;
    }

    /**
     * Returns the IDs of destination cards in the draw pile.
     *
     * @return the destination draw-pile card IDs
     */
    public List<Integer> getDestinationDeckCards() {
        return destinationDeckCards;
    }
}
