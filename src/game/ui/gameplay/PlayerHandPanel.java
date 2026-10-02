package game.ui.gameplay;

import game.card.CardColour;
import game.card.DestinationCard;
import game.card.TrainCard;
import game.ui.utils.ResourceUtils;
import game.ui.utils.ThemeUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Panel displays the cards hold by Current Player.
 *
 * @author Hew Jia Jing
 */
public class PlayerHandPanel extends JPanel {

    private static final int PANEL_HEIGHT = 160;
    private static final int TICKET_SECTION_WIDTH = 380;
    private static final int INFO_SECTION_WIDTH = 220;

    private final JPanel ticketsList;
    private final JPanel trainsList;
    private final JPanel playerInfoContainer;

    private Consumer<Integer> onTrainCardSelected;

    /**
     * Constructs a new PlayerHandPanel.
     */
    public PlayerHandPanel() {

        setPreferredSize(new Dimension(0, PANEL_HEIGHT));
        setLayout(new BorderLayout(15, 0));
        setBackground(ThemeUtils.DARK_BROWN);
        setBorder(new EmptyBorder(10, 15, 10, 15));

        // Initialize sub-containers
        this.ticketsList = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        this.trainsList = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        this.playerInfoContainer = new JPanel(new BorderLayout());

        setupSections();
    }

    /**
     * This method initializes and positions the internal layout containers for
     * tickets, train cards, and player information.
     */
    private void setupSections() {
        // Prepare transparent containers
        ticketsList.setOpaque(false);
        trainsList.setOpaque(false);
        playerInfoContainer.setOpaque(false);
        playerInfoContainer.setPreferredSize(new Dimension(INFO_SECTION_WIDTH,
                0));

        // Add to main layout with scrolling where necessary
        add(createLabeledSection("Destination Tickets", ticketsList,
                TICKET_SECTION_WIDTH), BorderLayout.WEST);
        add(createLabeledSection("Train Cards", trainsList, 0),
                BorderLayout.CENTER);
        add(playerInfoContainer, BorderLayout.EAST);
    }

    /**
     * Creates a standardized sub-panel containing a title and a scrollable content area.
     *
     * @param title   The text to display above the content.
     * @param content The main JPanel containing the actual data widgets.
     * @param width   The preferred width of this section (0 allows flexible sizing).
     * @return A constructed JComponent ready to be added to the main layout.
     */
    private JComponent createLabeledSection(String title, JPanel content,
                                            int width) {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        if (width > 0) container.setPreferredSize(new Dimension(width, 0));

        JLabel label = new JLabel(title);
        label.setForeground(ThemeUtils.GOLD);
        label.setFont(new Font("Serif", Font.BOLD, 15));
        container.add(label, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        container.add(scroll, BorderLayout.CENTER);
        return container;
    }

    /**
     * Registers a listener to be notified when a user selects a train card widget.
     *
     * @param listener A Consumer that accepts the unique ID of the selected card.
     */
    public void setOnTrainCardSelected(Consumer<Integer> listener) {
        this.onTrainCardSelected = listener;
    }

    /**
     * Rebuilds the user interface to reflect the current state of the active player.
     *
     * @param tickets    A list of the player's active destination tickets.
     * @param trainCards A mapping of CardColour to the list of train cards the player holds of that color.
     * @param playerName The name of the player whose turn it currently is.
     * @param themeColor The UI theme color associated with the player's logical color.
     * @param trainsLeft The number of physical train pieces the player has remaining.
     * @param score      The player's current accumulated score.
     */
    public void updateDisplay(List<DestinationCard> tickets,
                              Map<CardColour, List<TrainCard>> trainCards,
                              String playerName, Color themeColor,
                              int trainsLeft, int score) {

        // Update Tickets
        ticketsList.removeAll();
        if (tickets != null) {
            for (DestinationCard t : tickets) {
                ticketsList.add(new TicketWidget(t.getFrom().getName(),
                        t.getTo().getName(), t.getPoints()));
            }
        }

        // Update Train Cards
        trainsList.removeAll();
        if (trainCards != null) {
            trainCards.forEach((cardColour, cards) -> {
                for (TrainCard card : cards) {
                    trainsList.add(new SingleTrainCardWidget(cardColour, card.getId()));
                }
            });
        }

        // Update Player Identity
        playerInfoContainer.removeAll();
        JLabel playerTitle = new JLabel("Current Turn", JLabel.RIGHT);
        playerTitle.setForeground(ThemeUtils.GOLD);
        playerTitle.setFont(new Font("Serif", Font.BOLD, 15));
        playerInfoContainer.add(playerTitle, BorderLayout.NORTH);
        playerInfoContainer.add(new PlayerInfoWidget(playerName, themeColor,
                trainsLeft, score), BorderLayout.CENTER);

        revalidate();
        repaint();
    }
    
    /**
     * A lightweight widget that renders a train card.
     */
    private class SingleTrainCardWidget extends JPanel {
        private final CardColour cardColour;
        private final int cardId;
        private boolean selected = false;

        private static final int TRAIN_CARD_W = 60, TRAIN_CARD_H = 85;

        SingleTrainCardWidget(CardColour cardColour, int cardId) {
            this.cardColour = cardColour;
            this.cardId = cardId;
            setPreferredSize(new Dimension(TRAIN_CARD_W, TRAIN_CARD_H));
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    selected = !selected;
                    if (onTrainCardSelected != null) {
                        onTrainCardSelected.accept(cardId);
                    }
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Image img = ResourceUtils.getTrainImage(cardColour);

            if (img != null) {
                g2.drawImage(img, 0, 0, TRAIN_CARD_W, TRAIN_CARD_H, this);
            } else {
                g2.setColor(ThemeUtils.cardColour(cardColour));
                g2.fillRoundRect(0, 0, TRAIN_CARD_W, TRAIN_CARD_H, 8, 8);
            }

            // Draw selection highlight
            if (selected) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(4));
                g2.drawRoundRect(2, 2, TRAIN_CARD_W - 4, TRAIN_CARD_H - 4, 8, 8);
            }
        }
    }

    /**
     * A lightweight widget that renders a single destination ticket.
     */
    private class TicketWidget extends JPanel {
        private final String loc1, loc2;
        private final int points;

        private static final int TICKET_W = 120, TICKET_H = 85;

        TicketWidget(String loc1, String loc2, int points) {
            this.loc1 = loc1;
            this.loc2 = loc2;
            this.points = points;
            setPreferredSize(new Dimension(TICKET_W, TICKET_H));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            Image img = ResourceUtils.getDestImage(points, loc1, loc2);

            if (img != null) {
                g2.drawImage(img, 0, 0, TICKET_W, TICKET_H, this);
            } else {
                g2.setColor(new Color(245, 235, 210));
                g2.fillRoundRect(2, 2, TICKET_W - 4,
                        TICKET_H - 4, 8, 8);
                g2.setColor(Color.BLACK);
                g2.drawString(points + " pts", 10, 20);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.drawString(loc1 + " to " + loc2, 10, 40);
            }
        }
    }

    /**
     * A lightweight widget that displays current player statistics.
     */
    private class PlayerInfoWidget extends JPanel {
        private final String name;
        private final Color playerColor;
        private final int trainsLeft, score;

        PlayerInfoWidget(String name, Color playerColor, int trainsLeft, int score) {
            this.name = name;
            this.playerColor = playerColor;
            this.trainsLeft = trainsLeft;
            this.score = score;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int w = getWidth(), h = getHeight() - 5;

            g2.setColor(ThemeUtils.PANEL_BROWN);
            g2.fillRoundRect(0, 5, w, h, 12, 12);
            g2.setColor(playerColor);
            g2.fillRoundRect(0, 5, w, 6, 12, 12);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            g2.drawString("▶ " + name, 15, 30);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g2.setColor(ThemeUtils.CREAM);
            g2.drawString("Score: " + score, 15, 55);
            g2.drawString("Trains left: " + trainsLeft, 15, 75);
        }
    }

}
