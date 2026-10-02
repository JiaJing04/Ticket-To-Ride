package game.ui.gameplay;

import game.card.TrainCard;
import game.deck.DestinationCardDeck;
import game.deck.TrainCardDeck;
import game.ui.utils.ResourceUtils;
import game.ui.utils.ThemeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.function.Consumer;

import static game.ui.utils.ThemeUtils.fallbackColour;

/**
 * Panel displayed on the left side of the board showing:
 * - 5 face-up train cards
 * - Train face-down draw pile
 * - Destination card draw pile
 *
 * @author Hew Jia Jing
 */
public class DeckPanel extends JPanel {

    private final TrainCardDeck trainDeck;
    private final DestinationCardDeck destDeck;

    private static final int CARD_W = 80, CARD_H = 54;

    private Consumer<Integer> onFaceUpDrawn;
    private Runnable onDeckDrawn;
    private Runnable onDestinationDeckDrawn;

    /**
     * Constructs a new DeckPanel
     */
    public DeckPanel(TrainCardDeck trainDeck, DestinationCardDeck destDeck) {
        this.trainDeck = trainDeck;
        this.destDeck = destDeck;

        setPreferredSize(new Dimension(130, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ThemeUtils.DARK_BROWN);
        setBorder(BorderFactory.createEmptyBorder(14, 12, 14,
                12));
    }

    /**
     * Fired when the player clicks a face-up train card. Receives slot index (0–4).
     */
    public void setOnFaceUpDrawn(Consumer<Integer> listener) {
        this.onFaceUpDrawn = listener;
    }

    /**
     * Fired when the player clicks the face-down train draw pile.
     */
    public void setOnDeckDrawn(Runnable listener) {
        this.onDeckDrawn = listener;
    }

    /**
     * Fired when the player clicks the destination card draw pile.
     */
    public void setOnDestinationDeckDrawn(Runnable listener) {
        this.onDestinationDeckDrawn = listener;
    }

    /**
     * This method refreshes the UI by reading the current state of the injected
     * deck models.
     */
    public void refresh() {
        removeAll();

        add(sectionTitle("Train Deck"));
        add(Box.createVerticalStrut(10));

        // Render the 5 face-up train cards
        List<TrainCard> faceUp = trainDeck.getFaceUpPile();
        for (int slot = 0; slot < 5; slot++) {
            TrainCard card = slot < faceUp.size() ? faceUp.get(slot) : null;
            FaceUpCardWidget widget = new FaceUpCardWidget(card, slot);
            widget.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(widget);
            add(Box.createVerticalStrut(6));
        }

        add(Box.createVerticalStrut(4));

        // Render the face-down train card draw pile
        DrawPileWidget trainPile = new DrawPileWidget(
                trainDeck.getInDeckPile().size(),
                ResourceUtils.getTrainBackImage(),
                null,
                new Color(25, 50, 95),
                new Color(15, 30, 60),
                "Train pile",
                () -> {
                    if (onDeckDrawn != null) onDeckDrawn.run();
                }
        );
        trainPile.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(trainPile);

        add(Box.createVerticalStrut(8));
        add(sectionTitle("Discard"));
        add(Box.createVerticalStrut(6));

        // Show face-up top card on discard pile, or empty slot when pile is empty
        int discardCount = trainDeck.getDiscardedPile().size();
        List<TrainCard> discardedPileList = trainDeck.getDiscardedPile();
        TrainCard discardTop = discardCount > 0 ? discardedPileList.get(discardCount - 1) : null;
        DrawPileWidget discardedPile = new DrawPileWidget(
                discardCount,
                null,
                discardTop,
                new Color(95, 35, 35),
                new Color(45, 18, 18),
                "Discarded",
                null
        );
        discardedPile.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(discardedPile);

        // Visual separator between decks
        add(Box.createVerticalStrut(14));
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        sep.setForeground(ThemeUtils.MID_BROWN);
        add(sep);
        add(Box.createVerticalStrut(12));

        add(sectionTitle("Dest Deck"));
        add(Box.createVerticalStrut(10));

        // Render the face-down destination card draw pile
        DrawPileWidget destPile = new DrawPileWidget(
                destDeck.getInDeckPile().size(),
                ResourceUtils.getDestBackImage(),
                null,
                new Color(28, 75, 38),
                new Color(15, 45, 22),
                "Ticket pile",
                () -> {
                    if (onDestinationDeckDrawn != null) onDestinationDeckDrawn.run();
                }
        );
        destPile.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(destPile);

        // Pushes all content to the top
        add(Box.createVerticalGlue());

        revalidate();
        repaint();
    }

    /**
     * An inner class responsible solely for rendering a single face-up train
     * card and handling its mouse interactions.
     */
    private class FaceUpCardWidget extends JPanel {

        private final TrainCard card;
        private final int slot;
        private boolean hovered = false;

        FaceUpCardWidget(TrainCard card, int slot) {
            this.card = card;
            this.slot = slot;
            setPreferredSize(new Dimension(CARD_W, CARD_H));
            setMaximumSize(new Dimension(CARD_W, CARD_H));
            setOpaque(false);

            if (card != null) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setToolTipText("Draw: " + card.getColour() + " #" + card.getId());
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (onFaceUpDrawn != null) onFaceUpDrawn.accept(slot);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = quality(g);
            int w = getWidth(), h = getHeight();

            // Render empty slot placeholder if no card exists
            if (card == null) {
                g2.setColor(new Color(60, 40, 22, 120));
                g2.fill(new RoundRectangle2D.Float(2, 2, w - 4, h - 4, 10, 10));
                g2.setColor(new Color(255, 255, 255, 30));
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND, 0, new float[]{4, 3}, 0));
                g2.draw(new RoundRectangle2D.Float(2, 2, w - 4, h - 4, 10, 10));
                g2.dispose();
                return;
            }

            // Calculate vertical offset for hover animation
            int dy = hovered ? -2 : 0;

            // Render glowing aura if hovered
            if (hovered) {
                g2.setColor(new Color(255, 230, 80, 100));
                g2.setStroke(new BasicStroke(4));
                g2.draw(new RoundRectangle2D.Float(1, dy + 1, w - 2, h - 2, 12, 12));
            }

            Image img = ResourceUtils.getTrainImage(card.getColour());
            if (img != null) {
                g2.drawImage(img, 2, dy + 2, w - 4, h - 4, this);
            } else {
                paintTrainFallback(g2, w, h, dy, card);
            }

            // Render crisp outer border on hover
            if (hovered) {
                g2.setColor(new Color(255, 230, 80, 200));
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Float(2, dy + 2, w - 4,
                        h - 4, 10, 10));
            }

            drawIdBadge(g2, card.getId(), 4, dy + 4);
            g2.dispose();
        }
    }

    /**
     * A widget representing a thick, face-down stack of cards.
     */
    private static class DrawPileWidget extends JPanel {

        private final int count;
        private final Image backImage;
        private final TrainCard topCard;
        private final Color gradTop;
        private final Color gradBottom;
        private final String label;
        private final Runnable onClick;
        private boolean hovered = false;

        DrawPileWidget(int count, Image backImage, TrainCard topCard,
                       Color gradTop, Color gradBottom,
                       String label, Runnable onClick) {
            this.count = count;
            this.backImage = backImage;
            this.topCard = topCard;
            this.gradTop = gradTop;
            this.gradBottom = gradBottom;
            this.label = label;
            this.onClick = onClick;

            setPreferredSize(new Dimension(CARD_W, CARD_H + 14));
            setMaximumSize(new Dimension(CARD_W, CARD_H + 14));
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText(label + " (" + count + " remaining)");

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (onClick != null) onClick.run();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = quality(g);
            int w = getWidth();
            int dy = hovered ? -2 : 0;

            // Render empty slot placeholder when pile is empty (no image and no top card)
            if (backImage == null && topCard == null) {
                g2.setColor(new Color(60, 40, 22, 120));
                g2.fill(new RoundRectangle2D.Float(2, dy + 2, w - 4, CARD_H - 4, 10, 10));
                g2.setColor(new Color(255, 255, 255, 30));
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND, 0, new float[]{4, 3}, 0));
                g2.draw(new RoundRectangle2D.Float(2, dy + 2, w - 4, CARD_H - 4, 10, 10));

                // "0 left" badge
                String countStr = "0 left";
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                FontMetrics fm = g2.getFontMetrics();
                int badgeW = fm.stringWidth(countStr) + 10;
                int badgeX = (w - badgeW) / 2;
                int badgeY = dy + CARD_H + 1;
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRoundRect(badgeX, badgeY, badgeW, 15, 8, 8);
                g2.setColor(ThemeUtils.CREAM);
                g2.drawString(countStr, badgeX + 5, badgeY + 11);

                g2.dispose();
                return;
            }

            // Draw stacked shadow effect to represent deck thickness
            for (int i = 3; i >= 1; i--) {
                g2.setColor(new Color(20, 12, 6, 70));
                g2.fill(new RoundRectangle2D.Float(2 + i, dy + 2 + i, w - 4, CARD_H - 4, 10, 10));
            }

            if (hovered) {
                g2.setColor(new Color(255, 230, 80, 90));
                g2.setStroke(new BasicStroke(4));
                g2.draw(new RoundRectangle2D.Float(1, dy + 1, w - 2, CARD_H, 12, 12));
            }

            // Draw face-up top card if available, otherwise back image or fallback
            if (topCard != null) {
                Image faceImg = ResourceUtils.getTrainImage(topCard.getColour());
                if (faceImg != null) {
                    g2.drawImage(faceImg, 2, dy + 2, w - 4, CARD_H - 4, this);
                } else {
                    paintTrainFallback(g2, w, CARD_H, dy, topCard);
                }
                drawIdBadge(g2, topCard.getId(), 4, dy + 4);
            } else if (backImage != null) {
                g2.drawImage(backImage, 2, dy + 2, w - 4, CARD_H - 4, this);
            } else {
                paintFallbackBack(g2, w, dy);
            }

            if (hovered) {
                g2.setColor(new Color(255, 230, 80, 200));
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Float(2, dy + 2, w - 4, CARD_H - 4, 10, 10));
            }

            // Render remaining card count badge
            String countStr = count + " left";
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            FontMetrics fm = g2.getFontMetrics();
            int badgeW = fm.stringWidth(countStr) + 10;
            int badgeX = (w - badgeW) / 2;
            int badgeY = dy + CARD_H + 1;

            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRoundRect(badgeX, badgeY, badgeW, 15, 8, 8);
            g2.setColor(ThemeUtils.CREAM);
            g2.drawString(countStr, badgeX + 5, badgeY + 11);

            g2.dispose();
        }

        /**
         * This method draws a generative back cover if the image file fails
         * to load.
         */
        private void paintFallbackBack(Graphics2D g2, int w, int dy) {
            g2.setPaint(new GradientPaint(0, dy, gradTop, 0, dy + CARD_H, gradBottom));
            g2.fill(new RoundRectangle2D.Float(2, dy + 2, w - 4, CARD_H - 4, 10, 10));

            g2.setColor(new Color(255, 255, 255, 35));
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(6, dy + 6, w - 12, CARD_H - 12, 7, 7));

            g2.setFont(new Font("Serif", Font.BOLD, 16));
            g2.setColor(new Color(255, 255, 255, 60));
            drawCentered(g2, label.contains("Ticket") ? "🎫" : "🚂", w / 2, dy + CARD_H / 2 + 6);

            g2.setColor(new Color(255, 255, 255, 40));
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(2, dy + 2, w - 4, CARD_H - 4, 10, 10));
        }
    }

    /**
     * This method draws a generative train card front if the image file fails
     * to load.
     */
    private static void paintTrainFallback(Graphics2D g2, int w, int h, int dy, TrainCard card) {
        Color base = fallbackColor(card.getColour().name());
        g2.setPaint(new GradientPaint(0, dy, base.brighter(), 0, h, base));
        g2.fill(new RoundRectangle2D.Float(2, dy + 2, w - 4, h - 4, 10, 10));

        g2.setColor(new Color(255, 255, 255, 55));
        g2.setStroke(new BasicStroke(1.3f));
        g2.draw(new RoundRectangle2D.Float(2, dy + 2, w - 4, h - 4, 10, 10));

        if (card.isLocomotive()) {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
            g2.setColor(Color.WHITE);
            drawCentered(g2, "★", w / 2, h / 2 + dy + 5);
        }

        g2.setFont(new Font("SansSerif", Font.BOLD, 8));
        g2.setColor(new Color(255, 255, 255, 200));
        drawCentered(g2, card.isLocomotive() ? "LOCO" : card.getColour().name(), w / 2, h - 6 + dy);
    }

    /**
     * This method maps a card's logical color to an AWT color for fallback
     * drawing.
     */
    private static Color fallbackColor(String name) {
        return fallbackColour(name);
    }

    /**
     * This method applies high-quality rendering hints to a Graphics2D context.
     */
    private static Graphics2D quality(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        return g2;
    }

    /**
     * This method centers text horizontally around a coordinate.
     */
    private static void drawCentered(Graphics2D g2, String text, int cx, int cy) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, cx - fm.stringWidth(text) / 2, cy);
    }

    /**
     * This method renders the small semi-transparent ID badge in the corner of
     * a card.
     */
    private static void drawIdBadge(Graphics2D g2, int id, int x, int y) {
        g2.setFont(new Font("SansSerif", Font.BOLD, 8));
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(x, y, 22, 13, 6, 6);
        g2.setColor(new Color(255, 255, 255, 210));
        g2.drawString("#" + id, x + 2, y + 10);
    }

    /**
     * This method formats the headers for the UI sections.
     */
    private static JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text, JLabel.CENTER);
        lbl.setForeground(ThemeUtils.GOLD);
        lbl.setFont(new Font("Serif", Font.BOLD, 15));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        return lbl;
    }
}
