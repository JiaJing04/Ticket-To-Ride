package game.save.memento;

/**
 * Memento object that stores the ownership state of a claimed route.
 *
 * @author Hew Jia Jing
 */
public class RouteMemento {

    /**
     * Identifier of the claimed route.
     */
    private String routeId;

    /**
     * Name of the player who owns the route.
     */
    private String ownerName;

    /**
     * Returns the route identifier.
     *
     * @return the route ID
     */
    public String getRouteId() {
        return routeId;
    }

    /**
     * Sets the route identifier.
     *
     * @param routeId the route ID
     */
    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    /**
     * Returns the name of the route owner.
     *
     * @return the owner's name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Sets the name of the route owner.
     *
     * @param ownerName the owner's name
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
