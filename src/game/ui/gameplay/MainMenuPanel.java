package game.ui.gameplay;

import game.ui.gameplay.GameButton;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(Runnable onNewGame, Runnable onLoadGame) {
        setLayout(new GridBagLayout());

        JPanel box = new JPanel(new GridLayout(3, 1, 12, 12));

        JLabel title = new JLabel("Ticket To Ride", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));

        GameButton newGameButton = new GameButton("New Game");
        GameButton loadGameButton = new GameButton("Load Game");

        newGameButton.addActionListener(e -> onNewGame.run());
        loadGameButton.addActionListener(e -> onLoadGame.run());

        box.add(title);
        box.add(newGameButton);
        box.add(loadGameButton);

        add(box);
    }
}
