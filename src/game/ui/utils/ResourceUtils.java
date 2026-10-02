package game.ui.utils;

import game.card.CardColour;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides utilities for loading, formatting, and caching image resources.
 *
 * @author Ng Zi Wei
 */
public class ResourceUtils {

    private static final Map<String, Image> CACHE = new HashMap<>();

    private ResourceUtils() {}

    /**
     * Returns the image for a train card color.
     *
     * @param colour domain enum representing the train card color
     * @return the loaded image, or null if not found
     */
    public static Image getTrainImage(CardColour colour) {
        return getImage("resources/trains/" + colour.name().toLowerCase()
                + ".png");
    }

    /**
     * Returns the image for a destination ticket based on points and cities.
     *
     * @param points point value of the destination ticket
     * @param loc1 first city name
     * @param loc2 second city name
     * @return the loaded image, or null if neither permutation exists
     */
    public static Image getDestImage(int points, String loc1, String loc2) {
        String path = String.format("resources/dest/%d_%s_%s.png",
                points, format(loc1), format(loc2));
        Image img = getImage(path);

        // Fallback for reversed city names
        if (img == null) {
            img = getImage(String.format("resources/dest/%d_%s_%s.png", points,
                    format(loc2), format(loc1)));
        }
        return img;
    }

    /**
     * Formats a city name to match file naming conventions.
     *
     * @param city raw city name from the domain model
     * @return string for file paths
     */
    private static String format(String city) {
        if (city == null) return "";

        // Handle specific game overrides
        String formatted = city.replace("St Paul's", "SaintPaul")
                .replace("Saint Paul", "SaintPaul")
                .replace("Elephant & Castle", "ElephantCastle")
                .replace("Tower of London", "TowerOfLondon"); ;

        // Strip out all remaining spaces and punctuation for the file name
        return formatted.replace(" ", "")
                .replace("'", "");
    }

    /**
     * Returns an image from cache or loads it if not present.
     *
     * @param path relative resource path
     * @return cached or loaded image, or null on failure
     */
    private static Image getImage(String path) {
        return CACHE.computeIfAbsent(path, p -> {
            try (InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream(p)) {
                if (is != null) return ImageIO.read(is);
            } catch (Exception ignored) {}
            return null;
        });
    }

    /**
     * Returns the image for the back of a train card.
     *
     * @return the loaded image, or null if not found
     */
    public static Image getTrainBackImage() {
        return getImage("resources/trains/back.png");
    }

    /**
     * Returns the image for the back of a destination ticket.
     *
     * @return the loaded image, or null if not found
     */
    public static Image getDestBackImage() {
        return getImage("resources/dest/back.png");
    }
}
