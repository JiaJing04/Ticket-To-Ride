package game.ui.setup;

import game.character.PlayerColour;
import game.setup.GameConfiguration;
import game.setup.MapType;
import game.setup.PlayerConfiguration;
import game.strategy.playerability.ColourMasterAbility;
import game.strategy.playerability.FerryMasterAbility;
import game.strategy.playerability.PlayerAbility;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class SetupPanel extends JPanel {

    private final JTextField p1Name;
    private final JTextField p2Name;
    private final JComboBox<PlayerColour> p1Colour;
    private final JComboBox<PlayerColour> p2Colour;
    private final JComboBox<String> p1Ability;
    private final JComboBox<String> p2Ability;
    private final JComboBox<MapType> mapTypeBox;
    private final SetupCompleteListener listener;

    public SetupPanel(SetupCompleteListener listener) {
        this.listener = listener;

        setLayout(new BorderLayout(24, 10));
        setBorder(BorderFactory.createEmptyBorder(30, 70, 30, 70));

        p1Name = new JTextField("Player 1");
        p2Name = new JTextField("Player 2");

        p1Colour = new JComboBox<>(PlayerColour.values());
        p2Colour = new JComboBox<>(PlayerColour.values());
        p2Colour.setSelectedItem(PlayerColour.BLUE);

        p1Ability = new JComboBox<>(new String[]{
                "Ferry Master",
                "Colour Master"
        });

        p2Ability = new JComboBox<>(new String[]{
                "Ferry Master",
                "Colour Master"
        });
        p2Ability.setSelectedItem("Colour Master");

        mapTypeBox = new JComboBox<>(MapType.values());

        add(createTitle(), BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setOpaque(false);

        centerWrapper.add(createPlayersPanel());
        centerWrapper.add(Box.createVerticalStrut(25));
        centerWrapper.add(createMapSelectionPanel());

        add(centerWrapper, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(76, 52, 32),
                0, getHeight(), new Color(28, 20, 14));

        graphics2D.setPaint(gradient);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
    }

    private JLabel createTitle() {
        JLabel title = new JLabel("Ticket To Ride", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 46));
        title.setForeground(new Color(255, 224, 150));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        return title;
    }

    private JPanel createMapSelectionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);

        JLabel label = createFormLabel("Select Map: ");
        label.setFont(new Font("Serif", Font.BOLD, 20));
        label.setForeground(new Color(255, 224, 150));

        mapTypeBox.setPreferredSize(new Dimension(200, 35));
        mapTypeBox.setFont(new Font("SansSerif", Font.BOLD, 16));

        panel.add(label);
        panel.add(mapTypeBox);
        return panel;
    }

    private JPanel createPlayersPanel() {
        JPanel playersPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        playersPanel.setOpaque(false);

        playersPanel.add(createPlayerPanel("Player 1", p1Name, p1Colour, p1Ability));
        playersPanel.add(createPlayerPanel("Player 2", p2Name, p2Colour, p2Ability));

        return playersPanel;
    }

    private JPanel createPlayerPanel(
            String title,
            JTextField nameField,
            JComboBox<PlayerColour> colourBox,
            JComboBox<String> abilityBox) {

        JPanel panel = new JPanel(new GridLayout(4, 1, 12, 12));
        panel.setBackground(new Color(88, 62, 38));
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(210, 170, 100), 2),
                        BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titleLabel.setForeground(new Color(255, 230, 170));

        JLabel nameLabel = createFormLabel("Name");
        JLabel colourLabel = createFormLabel("Colour");
        JLabel abilityLabel = createFormLabel("Ability");

        nameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        colourBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        abilityBox.setFont(new Font("SansSerif", Font.PLAIN, 16));

        panel.add(titleLabel);
        panel.add(createInputRow(nameLabel, nameField));
        panel.add(createInputRow(colourLabel, colourBox));
        panel.add(createInputRow(abilityLabel, abilityBox));

        return panel;
    }

    private JPanel createInputRow(JLabel label, java.awt.Component input) {
        JPanel row = new JPanel(new BorderLayout(10, 5));
        row.setOpaque(false);

        row.add(label, BorderLayout.NORTH);
        row.add(input, BorderLayout.CENTER);

        return row;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        startButton.setBackground(new Color(168, 112, 54));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createEmptyBorder(14, 34, 14, 34));
        startButton.addActionListener(event -> submit());

        panel.add(startButton);
        return panel;
    }

    private void submit() {
        String name1 = p1Name.getText().trim();
        String name2 = p2Name.getText().trim();

        if (name1.isEmpty() || name2.isEmpty()) {
            showError("Names cannot be empty.");
            return;
        }

        PlayerColour c1 = (PlayerColour) p1Colour.getSelectedItem();
        PlayerColour c2 = (PlayerColour) p2Colour.getSelectedItem();

        if (c1 == c2) {
            showError("Players must have different colours.");
            return;
        }

        PlayerAbility ability1 = createAbility((String) p1Ability.getSelectedItem());
        PlayerAbility ability2 = createAbility((String) p2Ability.getSelectedItem());

        listener.onSetupComplete(
                new GameConfiguration(
                        List.of(
                                new PlayerConfiguration(name1, c1, ability1),
                                new PlayerConfiguration(name2, c2, ability2)
                        ),
                        (MapType) mapTypeBox.getSelectedItem()
                )
        );
    }

    private PlayerAbility createAbility(String selectedAbility) {
        return switch (selectedAbility) {
            case "Ferry Master" -> new FerryMasterAbility();
            case "Colour Master" -> new ColourMasterAbility();
            default -> throw new IllegalArgumentException(
                    "Unknown ability: " + selectedAbility
            );
        };
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Setup Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
