package game.ui.gameplay;

import game.main.dto.TicketDecisionCardDTO;
import game.ui.utils.ResourceUtils;
import game.ui.utils.ThemeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * A specialized dialog for handling the destination ticket selection process.
 *
 * @author Ng Zi Wei
 */
public class TicketSelectionDialog {

    private final JDialog dialog;
    private final List<TicketSelectionWidget> widgets = new ArrayList<>();
    private BiConsumer<List<Integer>, List<Integer>> onConfirmed;

    /**
     * Constructs a new selection dialog.
     *
     * @param parent The parent component used to locate the dialog.
     * @param cards  The list of destination cards offered to the player.
     */
    public TicketSelectionDialog(Component parent, List<TicketDecisionCardDTO> cards, int minKeep) {

        Window owner = SwingUtilities.getWindowAncestor(parent);
        this.dialog = new JDialog(owner);

        dialog.setTitle("Select Destination Tickets");
        dialog.setModal(true);
        dialog.getContentPane().setBackground(ThemeUtils.DARK_BROWN);
        dialog.setLayout(new BorderLayout(10, 10));

        // Prevent accidental closure; forces user to use the 'Confirm' button logic
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        setupUI(cards, minKeep);
    }

    /**
     * Initializes the UI components and layout.
     */
    private void setupUI(List<TicketDecisionCardDTO> cards, int minKeep) {

        // Header Instruction
        JLabel instruction = new JLabel("Choose at least " + minKeep + " tickets to keep:", JLabel.CENTER);
        instruction.setForeground(ThemeUtils.GOLD);
        instruction.setFont(new Font("Serif", Font.BOLD, 18));
        instruction.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        dialog.add(instruction, BorderLayout.NORTH);

        // Selection Area
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,
                25, 20));
        selectionPanel.setOpaque(false);

        for (TicketDecisionCardDTO card : cards) {
            TicketSelectionWidget widget = new TicketSelectionWidget(card);
            widgets.add(widget);
            selectionPanel.add(widget);
        }

        JScrollPane scrollPane = new JScrollPane(selectionPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Confirm Button
        JButton confirmButton = new JButton("Confirm Selections");
        confirmButton.setBackground(ThemeUtils.GOLD);
        confirmButton.setForeground(ThemeUtils.DARK_BROWN);
        confirmButton.setFont(new Font("Serif", Font.BOLD, 16));
        confirmButton.addActionListener(e -> validateAndConfirm());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 25, 10));
        buttonPanel.add(confirmButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(dialog.getOwner());
    }

    /**
     * Validates that the selection meets domain requirements before closing.
     */
    private void validateAndConfirm() {
        List<Integer> keepIds = new ArrayList<>();
        List<Integer> discardIds = new ArrayList<>();

        for (TicketSelectionWidget w : widgets) {
            if (w.isSelected()) keepIds.add(w.getCard().getId());
            else discardIds.add(w.getCard().getId());
        }

        dialog.dispose();
        if (onConfirmed != null) {
            onConfirmed.accept(keepIds, discardIds);
        }
    }

    /**
     * Sets the callback listener for when the selection is
     * validated and confirmed.
     *
     * @param listener A BiConsumer receiving the list of kept IDs and
     *                 discarded IDs.
     */
    public void setOnConfirmed(BiConsumer<List<Integer>,
            List<Integer>> listener) {
        this.onConfirmed = listener;
    }

    /**
     * Displays the modal dialog.
     */
    public void show() {
        dialog.setVisible(true);
    }

    /**
     * Renders a single Destination Ticket with selection feedback.
     */
    private static class TicketSelectionWidget extends JPanel {
        private final TicketDecisionCardDTO card;
        private final JCheckBox checkBox;
        private static final int W = 180, H = 120;

        public TicketSelectionWidget(TicketDecisionCardDTO card) {
            this.card = card;
            setLayout(new BorderLayout(5, 8));
            setOpaque(false);

            // Custom painted image panel for "stained paper" effect
            JPanel imagePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Background Card Base
                    g2.setColor(ThemeUtils.MID_BROWN);
                    g2.fillRoundRect(0, 0, W, H, 15, 15);

                    // Resource Delegation
                    Image img = ResourceUtils.getDestImage(card.getPoints(),
                            card.getFrom(),
                            card.getTo());

                    if (img != null) {
                        g2.drawImage(img, 0, 0, W, H, this);
                        // Aesthetic tint for vintage feel
                        g2.setColor(new Color(139, 115, 85, 100));
                        g2.fillRoundRect(0, 0, W, H, 15, 15);
                    }

                    // Selection Border Feedback
                    if (checkBox.isSelected()) {
                        g2.setColor(new Color(50, 255, 50)); // Success Green
                        g2.setStroke(new BasicStroke(5f));
                        g2.drawRoundRect(2, 2, W - 4, H - 4, 15, 15);
                    }
                    g2.dispose();
                }
            };

            imagePanel.setPreferredSize(new Dimension(W, H));
            imagePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            imagePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    checkBox.setSelected(!checkBox.isSelected());
                    repaint();
                }
            });

            checkBox = new JCheckBox("KEEP", true);
            checkBox.setOpaque(false);
            checkBox.setForeground(ThemeUtils.GOLD);
            checkBox.setFont(new Font("SansSerif", Font.BOLD, 12));
            checkBox.setHorizontalAlignment(SwingConstants.CENTER);
            checkBox.addActionListener(e -> repaint());

            add(imagePanel, BorderLayout.CENTER);
            add(checkBox, BorderLayout.SOUTH);
        }

        public boolean isSelected() {
            return checkBox.isSelected();
        }

        public TicketDecisionCardDTO getCard() {
            return card;
        }
    }
}
