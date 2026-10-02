package game;

import game.main.GameController;
import game.ui.MainFrame;
import game.ui.SwingGameUI;
import game.ui.gameplay.MainMenuPanel;
import game.ui.setup.SetupPanel;

import javax.swing.*;

/**
 * Sets up and runs the game application.
 *
 * @author Ng Zi Wei
 */
public class Main {

    private Main() {
    }

    /**
     * Starts the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::start);
    }

    /**
     * Initializes and launches the UI.
     */
    private static void start() {

        MainFrame frame = new MainFrame();

        SetupPanel setupPanel =
                new SetupPanel(config -> {

                    GameController controller = new GameController();
                    controller.onStartGame(config);

                    SwingGameUI gameUi = new SwingGameUI(controller);
                    controller.setGameUI(gameUi);

                    gameUi.setOnReturnToMenu(() -> {
                        frame.showScreen(MainFrame.MENU_SCREEN);
                        frame.removeScreen(gameUi.getPanel());
                    });

                    frame.addScreen(MainFrame.GAME_SCREEN, gameUi.getPanel());
                    frame.showScreen(MainFrame.GAME_SCREEN);

                    controller.beginSetupPhase();
                });

        MainMenuPanel menuPanel = new MainMenuPanel(
                () -> frame.showScreen(MainFrame.SETUP_SCREEN),

                () -> {
                    String name = JOptionPane.showInputDialog(frame, "Enter save file name:");
                    if (name != null && !name.isBlank()) {
                        try {
                            GameController controller = new GameController();
                            controller.loadGame(name + ".txt");

                            SwingGameUI gameUi = new SwingGameUI(controller);
                            controller.setGameUI(gameUi);

                            gameUi.showMessage("Game loaded successfully.");

                            gameUi.setOnReturnToMenu(() -> {
                                frame.showScreen(MainFrame.MENU_SCREEN);
                                frame.removeScreen(gameUi.getPanel());
                            });

                            frame.addScreen(MainFrame.GAME_SCREEN, gameUi.getPanel());
                            frame.showScreen(MainFrame.GAME_SCREEN);

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(
                                    frame,
                                    "Failed to load game: " + ex.getMessage(),
                                    "Load Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
        );

        frame.addScreen(MainFrame.MENU_SCREEN, menuPanel);
        frame.addScreen(MainFrame.SETUP_SCREEN, setupPanel);

        frame.showScreen(MainFrame.MENU_SCREEN);
        frame.setVisible(true);
    }
}
