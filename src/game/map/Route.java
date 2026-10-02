package game.map;

import game.card.TrainCard;
import game.character.Player;
import game.strategy.routeclaiming.RouteClaimStrategy;

import java.util.List;

/**
 * Represents a route on the game board.
 *
 * @author Hew Jia Jing
 */
public class Route {

    /**
     * Unique identifier of the route.
     */
    private final String id;

    /**
     * One endpoint city of the route.
     */
    private final City cityA;

    /**
     * The other endpoint city of the route.
     */
    private final City cityB;

    /**
     * Length of the route.
     */
    private final int length;

    /**
     * Colour of the route.
     */
    private final RouteColour colour;

    /**
     * Whether the route has been claimed.
     */
    private boolean isClaimed;

    /**
     * Player who claimed the route, or null if unclaimed.
     */
    private Player owner;

    /**
     * Strategy used to validate and execute route claiming.
     */
    private final RouteClaimStrategy routeClaimStrategy;

    /**
     * The route group this route belongs to (e.g. DoubleRoute).
     */
    private RouteGroup routeGroup;

    /**
     * Whether this route is a ferry route.
     */
    private final boolean ferry;

    /**
     * Creates a route between two cities.
     *
     * @param id     the unique route identifier
     * @param cityA  the first connected city
     * @param cityB  the second connected city
     * @param length the number of train cards required
     * @param colour the colour requirement of the route
     */
    public Route(String id, City cityA, City cityB,
                 int length, RouteColour colour,
                 RouteClaimStrategy routeClaimStrategy, boolean ferry) {
        this.id = id;
        this.cityA = cityA;
        this.cityB = cityB;
        this.length = length;
        this.colour = colour;
        this.ferry = ferry;
        this.isClaimed = false;
        this.owner = null;
        this.routeClaimStrategy = routeClaimStrategy;
        this.routeGroup = null;
    }

    /**
     * Returns whether this is a ferry route.
     *
     * @return true if ferry route
     */
    public boolean isFerry() {
        return ferry;
    }

    /**
     * Returns the route ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the first city.
     */
    public City getCityA() {
        return cityA;
    }

    /**
     * Returns the second city.
     */
    public City getCityB() {
        return cityB;
    }

    /**
     * Returns the route length.
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the route colour.
     */
    public RouteColour getColour() {
        return colour;
    }

    /**
     * Returns whether this route has been claimed.
     *
     * @return true if the route has been claimed; false otherwise
     */
    public boolean isClaimed() {
        return isClaimed;
    }

    /**
     * Returns the route owner, or null if unclaimed.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this route and marks it as claimed.
     *
     * @param player the player claiming the route
     */
    public void setOwner(Player player) {
        this.owner = player;
        this.isClaimed = true;
    }

    /**
     * Checks whether the given player can claim this route.
     *
     * @param playerToClaim     the player attempting to claim the route
     * @param trainCardsToClaim the train cards used for the claim
     * @return true if the route can be claimed, otherwise false
     */
    public boolean canClaim(Player playerToClaim,
                            List<TrainCard> trainCardsToClaim) {
        if (isClaimed || playerToClaim.getRemainingTrainPieces() < getLength()) {
            return false;
        }

        if (routeGroup != null
                && !routeGroup.canClaim(playerToClaim, this)) {
            return false;
        }

        return routeClaimStrategy.validate(this, playerToClaim,
                trainCardsToClaim);
    }

    /**
     * Checks whether this route connects the specified cities.
     *
     * @param from one city
     * @param to the other city
     * @return true if this route connects the two cities; false otherwise
     */
    public boolean connects(City from, City to) {
        return (cityA == from && cityB == to) || (cityA == to && cityB == from);
    }


    /**
     * Sets the route group that this route belongs to.
     *
     * @param routeGroup the route group to associate with this route
     */
    public void setRouteGroup(RouteGroup routeGroup) {
        this.routeGroup = routeGroup;
    }

    /**
     * Strategy used to validate and execute route claiming rules.
     *
     * @return the route-claiming strategy associated with this route
     */
    public RouteClaimStrategy getRouteClaimStrategy() {
        return routeClaimStrategy;
    }

    /**
     * Returns the city at the opposite end of this route from the given city.
     *
     * @param city one endpoint of the route
     * @return the other endpoint city, or null if the given city
     *     is not connected by this route
     */
    public City getOtherCity(City city) {
        if (cityA == city) return cityB;
        if (cityB == city) return cityA;
        return null;
    }

    /**
     * Checks whether this route is owned by the specified player.
     *
     * @param player the player to check
     * @return true if the route has been claimed by the player; false otherwise
     */
    public boolean isOwnedBy(Player player) {
        return isClaimed && owner == player;
    }
}
