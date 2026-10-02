package game.ui.gameplay;

import game.ui.utils.ThemeUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Custom button for the game UI.
 *
 * @author Hew Jia Jing
 */
public class GameButton extends JButton {

    /**
     * Creates a game button with the given label.
     *
     * @param text button label
     */
    public GameButton(String text) {
        super(text);

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        setFont(new Font("Serif", Font.BOLD, 14));
        setForeground(ThemeUtils.CREAM);
        setBackground(ThemeUtils.MID_BROWN);

        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(ThemeUtils.GOLD);
                setForeground(Color.BLACK);
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(ThemeUtils.MID_BROWN);
                setForeground(ThemeUtils.CREAM);
                repaint();
            }
        });
    }

    /**
     * Paints the button with rounded corners and background color.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        super.paintComponent(g);
    }
}
