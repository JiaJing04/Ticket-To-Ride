package game.map;

import game.character.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a game board that stores the cities and routes
 * for a specific game map.
 *
 * @author Hew Jia Jing
 */
public class Board {

    /**
     * Cities available on the board.
     */
    private final List<City> cities;

    /**
     * Routes connecting the cities on the board.
     */
    private final List<Route> routes;

    /**
     * Creates an empty board.
     */
    public Board() {
        this.cities = new ArrayList<>();
        this.routes = new ArrayList<>();
    }

    /**
     * Adds a city to the board.
     */
    public void addCity(City city) {
        cities.add(city);
    }

    /**
     * Adds a route to the board.
     */
    public void addRoute(Route route) {
        routes.add(route);
    }

    /**
     * Returns all cities on the board.
     */
    public List<City> getCities() {
        return List.copyOf(cities);
    }

    /**
     * Returns all routes on the board.
     */
    public List<Route> getRoutes() {
        return List.copyOf(routes);
    }

    /**
     * Returns the route with the matching ID, or null if none exists.
     */
    public Route getRouteById(String routeId) {
        for (Route route : routes) {
            if (route.getId().equals(routeId)) {
                return route;
            }
        }

        return null;
    }

    /**
     * Returns all routes claimed by the given player.
     */
    public List<Route> getRoutesClaimedBy(Player player) {
        List<Route> claimedRoutes = new ArrayList<>();

        for (Route route : routes) {
            if (route.isOwnedBy(player)) {
                claimedRoutes.add(route);
            }
        }

        return claimedRoutes;
    }

    /**
     * Returns all unclaimed routes.
     */
    public List<Route> getAvailableRoutes() {
        List<Route> availableRoutes = new ArrayList<>();

        for (Route route : routes) {
            if (!route.isClaimed()) {
                availableRoutes.add(route);
            }
        }

        return availableRoutes;
    }

    /**
     * Checks whether two cities are connected by routes owned by the
     * given player.
     *
     * @param player the player whose routes are considered
     * @param from   the starting city
     * @param to     the destination city
     * @return true if a connected path exists, false otherwise
     */
    public boolean isPathConnected(Player player, City from, City to) {
        if (player == null || from == null || to == null) {
            return false;
        }

        Set<City> visited = new HashSet<>();
        return dfs(player, from, to, visited);
    }

    /**
     * Performs a depth-first search to determine if a path exists between two
     * cities using only routes owned by the given player.
     *
     * @param player  the player whose claimed routes are used
     * @param current the current city being explored
     * @param target  the destination city
     * @param visited set of cities already visited during the search
     * @return true if a path exists from current to target, false otherwise
     */
    private boolean dfs(Player player, City current,
                        City target, Set<City> visited) {
        if (current == target) {
            return true;
        }

        visited.add(current);

        for (Route route : routes) {
            if (!route.isOwnedBy(player)) {
                continue;
            }

            City next = route.getOtherCity(current);

            if (next != null && !visited.contains(next)) {
                if (dfs(player, next, target, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the city with the matching name, or null if none exists.
     *
     * @param name the city name
     * @return the City
     */
    public City getCityByName(String name) {
        for (City city : cities) {
            if (city.getName().equalsIgnoreCase(name)) {
                return city;
            }
        }
        return null;
    }

}
