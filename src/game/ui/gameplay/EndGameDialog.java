package game.ui.gameplay;

import game.character.Player;
import game.ui.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * This represents the end game dialog showing the game winner and leader board.
 *
 * @author Ng Zi Wei
 */
public class EndGameDialog extends JDialog {

    private static final Font HEADER_FONT = new Font("Serif", Font.BOLD, 26);
    private static final Font HEADLINE_FONT = new Font("Serif", Font.BOLD, 20);
    private static final Font LEADERBOARD_FONT = new Font("SansSerif", Font.BOLD, 18);

    /**
     * Constructs the end game dialog
     *
     * @param parent          The parent component for centering the dialog.
     * @param leaderboard     The sorted list of players by score.
     * @param winner          The winning player, or null if the game is a draw.
     * @param isDraw          whether the game ended in a draw
     * @param onCloseCallback The action to execute when the dialog is closed.
     */
    public EndGameDialog(Component parent,
                         List<Player> leaderboard,
                         Player winner,
                         boolean isDraw,
                         Runnable onCloseCallback) {
        setTitle("Final Results");
        setModal(true);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                executeCloseAction(onCloseCallback);
            }
        });

        // --- Layout Initialization ---
        JPanel mainWrapperPanel = new JPanel(new BorderLayout(15, 15));
        mainWrapperPanel.setBackground(ThemeUtils.DARK_BROWN);

        Border lineBorder = BorderFactory.createLineBorder(ThemeUtils.GOLD, 3, true);
        Border marginBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        mainWrapperPanel.setBorder(BorderFactory.createCompoundBorder(marginBorder, lineBorder));
        setContentPane(mainWrapperPanel);

        // Header section
        JPanel headerPanel = createHeaderPanel(isDraw, winner);

        // Leaderboard Section
        JPanel leaderboardPanel = createLeaderboardPanel(isDraw, leaderboard, winner);

        // Footer Section
        JPanel buttonPanel = createFooterPanel(onCloseCallback);

        // Assemble all components
        mainWrapperPanel.add(headerPanel, BorderLayout.NORTH);
        mainWrapperPanel.add(leaderboardPanel, BorderLayout.CENTER);
        mainWrapperPanel.add(buttonPanel, BorderLayout.SOUTH);

        setSize(420, 360);
        setLocationRelativeTo(parent);
    }

    /**
     * This method handles closing the dialog and triggering the callback.
     */
    private void executeCloseAction(Runnable onCloseCallback) {
        dispose(); // Destroy the dialog window
        if (onCloseCallback != null) {
            onCloseCallback.run(); // Trigger the return-to-menu navigation
        }
    }

    /**
     * This method creates the header panel containing the title and dynamic
     * winner announcement.
     */
    private JPanel createHeaderPanel(boolean isDraw, Player winner) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0,
                5, 0));

        JLabel titleLabel = new JLabel("Game Over!", JLabel.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(ThemeUtils.GOLD);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String headlineText = isDraw
                ? "It's a draw! No one wins this time."
                : winner.getName() + " is the winner!";

        JLabel headlineLabel = new JLabel(headlineText, JLabel.CENTER);
        headlineLabel.setFont(HEADLINE_FONT);
        headlineLabel.setForeground(ThemeUtils.CREAM);
        headlineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(headlineLabel);

        return headerPanel;
    }

    /**
     * This method creates the leaderboard panel, displaying players and their
     * scores in a formatted list.
     */
    private JPanel createLeaderboardPanel(boolean isDraw, List<Player> leaderboard, Player winner) {
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel,
                BoxLayout.Y_AXIS));
        leaderboardPanel.setOpaque(false);
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(10,
                50, 10, 50));

        int rank = 1;
        for (Player player : leaderboard) {
            JPanel rowPanel = new JPanel(new BorderLayout());
            rowPanel.setOpaque(false);
            rowPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

            JLabel nameLabel = new JLabel(rank + ". " + player.getName());
            nameLabel.setFont(LEADERBOARD_FONT);

            JLabel scoreLabel = new JLabel(player.getScore() + " pts");
            scoreLabel.setFont(LEADERBOARD_FONT);

            // Determine Color (Gold for winner, Cream for others)
            Color rowColor = (!isDraw && player.equals(winner)) ?
                    ThemeUtils.GOLD : ThemeUtils.CREAM;
            nameLabel.setForeground(rowColor);
            scoreLabel.setForeground(rowColor);

            rowPanel.add(nameLabel, BorderLayout.WEST);
            rowPanel.add(scoreLabel, BorderLayout.EAST);

            leaderboardPanel.add(rowPanel);
            leaderboardPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            rank++;
        }

        return leaderboardPanel;
    }

    /**
     * This method creates the footer panel containing the close button and
     * its action listener.
     */
    private JPanel createFooterPanel(Runnable onCloseCallback) {
        JButton closeButton = new JButton("Close");
        closeButton.setFocusPainted(false);

        // Single, clean Action Listener for both disposing and routing
        closeButton.addActionListener(e -> {
            dispose();
            if (onCloseCallback != null) {
                onCloseCallback.run();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);

        return buttonPanel;
    }

}
