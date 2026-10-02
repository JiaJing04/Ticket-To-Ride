package game.ui.gameplay;

import game.ui.utils.ThemeUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Panel to show top header section of the main game screen.
 *
 * @author Hew Jia Jing
 */
public class HeaderPanel extends JPanel {

    /**
     * Cosntructs a new HeaderPanel
     *
     * @param mapName The name of the currently active map
     */
    public HeaderPanel(String mapName, Runnable onSaveClicked) {
        setLayout(new BorderLayout());

        setBackground(ThemeUtils.DARK_BROWN);
        setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        String displayMap = (mapName != null && !mapName.trim().isEmpty())
                ? mapName : "Unknown Map";

        JLabel title = new JLabel("Ticket To Ride : " + displayMap, JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 34));
        title.setForeground(ThemeUtils.GOLD);

        GameButton saveButton = new GameButton("Save Game");
        saveButton.addActionListener(e -> onSaveClicked.run());

        add(title, BorderLayout.CENTER);
        add(saveButton, BorderLayout.EAST);
    }
}
