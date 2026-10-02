package game.character;

import game.card.DestinationCard;
import game.card.TrainCard;
import game.map.Route;
import game.strategy.playerability.PlayerAbility;
import game.strategy.routescoring.RouteScoringStrategy;

import java.util.List;

/**
 * Represents a player in the game.
 *
 * @author Hew Jia Jing
 */
public class Player {

    /**
     * Player's display name.
     */
    private final String name;

    /**
     * Colour assigned to the player.
     */
    private final PlayerColour colour;

    /**
     * Cards currently held by the player.
     */
    private final PlayerHand hand;

    /**
     * The remaining train pieces owned by the player
     * that can be placed on the board.
     */
    private int remainingTrainPieces;

    /**
     * Total score accumulated by the player.
     */
    private int score;

    /**
     * Player's special ability.
     */
    private PlayerAbility ability;

    /**
     * Whether the player has a double Turn granted.
     */
    private boolean hasDoubleTurn = false;

    /**
     * Creates a player.
     *
     * @param name                 the player's name
     * @param colour               the player's colour
     * @param remainingTrainPieces the player's remaining train pieces
     */
    public Player(String name, PlayerColour colour, int remainingTrainPieces, PlayerAbility ability) {
        this.name = name;
        this.colour = colour;
        this.remainingTrainPieces = remainingTrainPieces;
        this.score = 0;
        this.hand = new PlayerHand();
        this.ability = ability;
    }

    /**
     * Returns the player's hand.
     */
    public PlayerHand getHand() {
        return hand;
    }

    /**
     * Returns the player's current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the remaining train pieces.
     */
    public int getRemainingTrainPieces() {
        return remainingTrainPieces;
    }

    /**
     * Returns the player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player's colour.
     */
    public PlayerColour getColour() {
        return colour;
    }

    /**
     * Adds points to the player's score.
     *
     * @param points points to add
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Deduct points to the player's score.
     *
     * @param points points to be deducted
     */
    public void deductScore(int points) {
        score -= points;
    }

    /**
     * Checks whether the player has enough train pieces.
     *
     * @param count required number of pieces
     * @return true if enough pieces are available
     */
    public boolean hasEnoughTrainPieces(int count) {
        return remainingTrainPieces >= count;
    }

    /**
     * Reduces the number of train pieces.
     *
     * @param numOfPieces number of pieces to remove
     */
    public void reduceTrainPieces(int numOfPieces) {
        remainingTrainPieces -= numOfPieces;
    }

    /**
     * Returns list of TrainCards in the PlayerHand according to input ids
     *
     * @param trainIds trainId to find
     * @return list of TrainCard according to trainId
     */
    public List<TrainCard> getTrainCardByIds(List<Integer> trainIds) {
        return getHand().getTrainCardByIds(trainIds);
    }

    /**
     * Method for encapsulating Player state changes when claiming a Route
     * 1. remove TrainCard from Player to claim the Route
     * 2. reduce the train pieces according to Route length
     * 3. set ownership of Route
     * 4. add score to Player according to the scoring strategy
     *
     * @param route                Route to be claimed by the Player
     * @param trainCardToUse       trainCard to be used to claim the Route
     * @param routeScoringStrategy scoring strategy for claiming the Route
     */
    public String claimRoute(Route route, List<TrainCard> trainCardToUse,
                             RouteScoringStrategy routeScoringStrategy) {
        getHand().removeTrainCards(trainCardToUse);
        reduceTrainPieces(route.getLength());
        route.setOwner(this);
        addScore(routeScoringStrategy.score(route));

        return ability.apply(this, route, trainCardToUse);
    }

    /**
     * Adds a drawn train card to the Player's hand
     *
     * @param trainCard trainCard drawn by Player
     */
    public void drawTrainCard(TrainCard trainCard) {
        getHand().addTrainCards(List.of(trainCard));
    }

    /**
     * Add destinationCards drawn to the Player's hand
     *
     * @param destinationCards the destinationCards drew by Player
     */
    public void drawDestCard(List<DestinationCard> destinationCards) {
        getHand().addDestinationCards(destinationCards);
    }

    /**
     * Used by save/load system to restore score.
     *
     * @param score restored score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Used by save/load system to restore train pieces.
     *
     * @param remainingTrainPieces restored train pieces
     */
    public void setRemainingTrainPieces(int remainingTrainPieces) {
        this.remainingTrainPieces = remainingTrainPieces;
    }

    /**
     * Returns the player's special ability.
     *
     * @return the player's ability
     */
    public PlayerAbility getAbility() {
        return ability;
    }

    /**
     * Grants the player a double turn.
     */
    public void grantDoubleTurn() {
        hasDoubleTurn = true;
    }

    /**
     * Checks whether the player currently has a double turn.
     *
     * @return true if the player has a double turn; false otherwise
     */
    public boolean hasDoubleTurn() {
        return hasDoubleTurn;
    }

    /**
     * Consumes the player's double turn.
     */
    public void consumeDoubleTurn() {
        hasDoubleTurn = false;
    }

    /**
     * Restores the player's double-turn state.
     *
     * @param hasDoubleTurn true if the player should have a double turn; false otherwise
     */
    public void restoreDoubleTurn(boolean hasDoubleTurn) {
        this.hasDoubleTurn = hasDoubleTurn;
    }
}
