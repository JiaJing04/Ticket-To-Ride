package game.ui.gameplay;

import game.character.Player;
import game.ui.utils.ThemeUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel to show all players information
 *
 * @author Hew Jia Jing
 */
public class PlayerInfoPanel extends JPanel {

    /**
     * Constructs a new PlayerInfoPanel.
     */
    public PlayerInfoPanel() {

        setPreferredSize(new Dimension(230, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ThemeUtils.DARK_BROWN);
        setBorder(BorderFactory.createEmptyBorder(18, 14,
                18, 14));
    }

    /**
     * Updates the panel with the latest transient player state.
     *
     * @param players       The list of all players in the game.
     * @param currentPlayer The player whose turn it currently is.
     */
    public void updatePlayers(List<Player> players, Player currentPlayer) {
        removeAll();

        JLabel title = new JLabel("Players");
        title.setForeground(ThemeUtils.GOLD);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(14));

        // Delegate the creation of complex UI elements to widgets
        for (Player player : players) {
            boolean isCurrent = (player == currentPlayer);
            PlayerCardWidget card = new PlayerCardWidget(player, isCurrent);
            add(card);
            add(Box.createVerticalStrut(12));
        }

        revalidate();
        repaint();
    }

    /**
     * An inner class responsible solely for formatting and rendering the data
     * and background of a single player's card.
     */
    private class PlayerCardWidget extends JPanel {

        private final boolean isCurrent;

        PlayerCardWidget(Player player, boolean isCurrent) {
            this.isCurrent = isCurrent;

            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(15, 14, 12, 14));
            setMaximumSize(new Dimension(200, 118));
            setAlignmentX(Component.LEFT_ALIGNMENT);

            // Setup Name Row
            JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            nameRow.setOpaque(false);
            nameRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            nameRow.setMaximumSize(new Dimension(180, 26));

            PlayerBadgeWidget badge = new PlayerBadgeWidget(ThemeUtils.playerColour(player.getColour()));

            JLabel name = new JLabel((isCurrent ? "▶ " : "") + player.getName());
            name.setForeground(Color.WHITE);
            name.setFont(new Font("Serif", Font.BOLD, 18));

            nameRow.add(badge);
            nameRow.add(name);

            // Setup Stats
            JLabel score = statLabel("Score: " + player.getScore());
            JLabel pieces = statLabel("Trains: " + player.getRemainingTrainPieces());
            JLabel ability = statLabel(
                    "Ability: " + player.getAbility().getName()
            );

            add(nameRow);
            add(Box.createVerticalStrut(8));
            add(score);
            add(Box.createVerticalStrut(3));
            add(pieces);
            add(Box.createVerticalStrut(3));
            add(ability);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Shadow
            g2.setColor(new Color(0, 0, 0, 75));
            g2.fillRoundRect(5, 7, w - 7, h - 7, 18, 18);

            // Main vintage panel
            g2.setColor(isCurrent ? new Color(125, 82, 38) : ThemeUtils.PANEL_BROWN);
            g2.fillRoundRect(0, 0, w - 7, h - 7, 18, 18);

            // Gold top accent
            g2.setColor(isCurrent ? ThemeUtils.GOLD : new Color(175, 135, 68));
            g2.fillRoundRect(0, 0, w - 7, 9, 18, 18);

            // Inner highlight
            g2.setColor(new Color(255, 245, 200, 35));
            g2.drawRoundRect(7, 13, w - 21, h - 27, 12, 12);

            // Border
            g2.setColor(isCurrent ? ThemeUtils.GOLD : ThemeUtils.MID_BROWN);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(0, 0, w - 7, h - 7, 18, 18);

            g2.dispose();
        }
    }

    /**
     * A widget purely responsible for drawing the 3D player token badge.
     */
    private static class PlayerBadgeWidget extends JLabel {

        private final Color badgeColor;

        PlayerBadgeWidget(Color color) {
            this.badgeColor = color;
            setPreferredSize(new Dimension(20, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillOval(2, 3, 17, 17);

            g2.setColor(badgeColor);
            g2.fillOval(1, 1, 16, 16);

            g2.setColor(new Color(255, 255, 255, 160));
            g2.fillOval(4, 3, 5, 4);

            g2.setColor(new Color(45, 25, 15));
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawOval(1, 1, 16, 16);

            g2.dispose();
        }
    }

    /**
     * A method to generate consistently styled labels for player statistics.
     *
     * @param text The statistical data string to display.
     * @return A fully formatted and aligned JLabel ready to be added to
     * the panel.
     */
    private JLabel statLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(ThemeUtils.CREAM);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}
