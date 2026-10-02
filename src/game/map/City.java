package game.map;

/**
 * Represents a city on the game board.
 *
 * @author Hew Jia Jing
 */
public class City {

    /**
     * Name of the city.
     */
    private final String name;

    /**
     * X-coordinate of the city on the board.
     */
    private final int x;

    /**
     * Y-coordinate of the city on the board.
     */
    private final int y;

    /**
     * Creates a city with a name and board coordinates.
     *
     * @param name the city name
     * @param x    the x-coordinate on the board image
     * @param y    the y-coordinate on the board image
     */
    public City(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the city name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate.
     */
    public int getY() {
        return y;
    }
}
