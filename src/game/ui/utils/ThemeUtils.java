package game.ui.utils;

import game.card.CardColour;
import game.character.PlayerColour;
import game.map.RouteColour;

import java.awt.*;


/**
 * Provides UI theme colors and mappings from domain colors to UI colors.
 *
 * @author Hew Jia Jing
 */
public final class ThemeUtils {

    private ThemeUtils() {
    }

    public static final Color DARK_BROWN = new Color(76, 52, 32);
    public static final Color MID_BROWN = new Color(120, 80, 45);
    public static final Color PANEL_BROWN = new Color(88, 62, 38);
    public static final Color CREAM = new Color(255, 238, 190);
    public static final Color GOLD = new Color(255, 224, 150);

    /**
     * Maps a CardColour to its corresponding UI color.
     *
     * @param colour the card colour
     * @return the UI color representation
     */
    public static Color cardColour(CardColour colour) {
        return switch (colour) {
            case BLUE -> new Color(45, 90, 190);
            case GREEN -> new Color(50, 150, 75);
            case YELLOW -> new Color(225, 190, 45);
            case BLACK -> new Color(35, 35, 35);
            case ORANGE -> new Color(220, 125, 35);
            case PINK -> new Color(220, 95, 165);
            case WILD -> new Color(120, 80, 160);
        };
    }

    /**
     * Maps a PlayerColour to its corresponding UI color.
     *
     * @param colour the player colour
     * @return the UI color representation
     */
    public static Color playerColour(PlayerColour colour) {
        return switch (colour) {
            case RED -> new Color(190, 45, 45);
            case BLUE -> new Color(45, 90, 190);
            case GREEN -> new Color(50, 150, 75);
            case YELLOW -> new Color(225, 190, 45);
            case BLACK -> new Color(35, 35, 35);
        };
    }

    /**
     * Maps a RouteColour to its corresponding UI color.
     *
     * @param colour the route colour
     * @return the UI color representation
     */
    public static Color routeColour(RouteColour colour) {
        return switch (colour) {
            case BLUE -> new Color(45, 90, 190);
            case GREEN -> new Color(50, 150, 75);
            case YELLOW -> new Color(225, 190, 45);
            case BLACK -> new Color(35, 35, 35);
            case ORANGE -> new Color(220, 125, 35);
            case PINK -> new Color(220, 95, 165);
            case GREY -> new Color(155, 155, 155);
        };
    }

    /**
     * Maps a Colour Name to its corresponding UI color.
     *
     * @param colour the route colour
     * @return the UI color representation
     */
    public static Color fallbackColour(String colour) {
        return switch (colour) {
            case "RED" -> new Color(185, 40, 40);
            case "BLUE" -> new Color(40, 85, 185);
            case "GREEN" -> new Color(45, 145, 70);
            case "YELLOW" -> new Color(210, 175, 35);
            case "BLACK" -> new Color(35, 35, 35);
            case "WHITE" -> new Color(200, 200, 190);
            case "ORANGE" -> new Color(210, 115, 30);
            case "PINK" -> new Color(210, 85, 155);
            case "GREY" -> new Color(140, 140, 140);
            case "WILD" -> new Color(120, 60, 180);
            default -> new Color(100, 100, 100);
        };
    }
}
