package game.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Manages the main application window and screen navigation.
 *
 * @author Hew Jia Jing
 */
public class MainFrame extends JFrame {

    public static final String SETUP_SCREEN = "setup";
    public static final String GAME_SCREEN = "game";
    public static final String MENU_SCREEN = "menu";

    private final CardLayout layout;
    private final JPanel container;

    /**
     * Constructs the main application frame.
     */
    public MainFrame() {
        setTitle("Ticket to Ride");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layout = new CardLayout();
        container = new JPanel(layout);

        add(container);
    }

    /**
     * Adds a screen to the frame.
     *
     * @param name the unique identifier of the screen
     * @param panel the panel representing the screen
     */
    public void addScreen(String name, JPanel panel) {
        container.add(panel, name);
    }

    /**
     * Displays the specified screen.
     *
     * @param name the identifier of the screen to display
     */
    public void showScreen(String name) {
        layout.show(container, name);
    }

    /**
     * Removes a screen from the frame.
     *
     * @param panel the panel to remove
     */
    public void removeScreen(Component panel) {
        container.remove(panel);
        container.revalidate();
        container.repaint();
    }
}
