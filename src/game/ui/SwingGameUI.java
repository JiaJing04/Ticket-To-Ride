package game.ui;

import game.character.Player;
import game.main.GameController;
import game.main.dto.GameResultDTO;
import game.main.dto.TicketDecisionCardDTO;
import game.request.ClaimRouteRequest;
import game.request.DrawDestCardRequest;
import game.request.DrawTrainCardRequest;
import game.request.KeepDrawDestRequest;
import game.ui.gameplay.DeckPanel;
import game.ui.gameplay.EndGameDialog;
import game.ui.gameplay.GameBoardPanel;
import game.ui.gameplay.GameButton;
import game.ui.gameplay.HeaderPanel;
import game.ui.gameplay.PlayerHandPanel;
import game.ui.gameplay.PlayerInfoPanel;
import game.ui.gameplay.TicketSelectionDialog;
import game.ui.utils.ThemeUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bridges UI panels and the game controller.
 *
 * @author Hew Jia Jing
 */
public class SwingGameUI implements GameUI {

    private final JPanel mainPanel;
    private final GameController controller;
    private final GameBoardPanel boardPanel;
    private final PlayerInfoPanel playerInfoPanel;
    private final PlayerHandPanel handPanel;
    private final DeckPanel deckPanel;
    private final HeaderPanel headerPanel;
    private final List<Integer> selectedTrainCardIds = new ArrayList<>();

    private String selectedRouteId;
    private Runnable onReturnToMenu;

    /**
     * Constructs a Swing game UI.
     *
     * @param controller the game controller
     */
    public SwingGameUI(GameController controller) {
        this.controller = controller;

        mainPanel = new JPanel(new BorderLayout(8, 8));
        mainPanel.setBackground(ThemeUtils.DARK_BROWN);

        boardPanel = new GameBoardPanel(controller.getGame().getBoard());
        playerInfoPanel = new PlayerInfoPanel();
        handPanel = new PlayerHandPanel();
        deckPanel = new DeckPanel(controller.getGame().getTrainCardDeck(),
                controller.getGame().getDestinationCardDeck());
        headerPanel = new HeaderPanel("London", this::onSaveGameClicked);

        setupListener();
        layoutComponents();
        refresh();
    }

    /**
     * Prompts the player for a save file name and saves the game.
     */
    private void onSaveGameClicked() {
        String name = JOptionPane.showInputDialog(mainPanel, "Enter save file name:");
        if (name != null && !name.isBlank()) {
            controller.saveGame(name + ".txt");
        }
    }

    /**
     * Maps sub-panel events to controller requests.
     */
    private void setupListener() {
        deckPanel.setOnFaceUpDrawn(this::onTrainCardClicked);
        deckPanel.setOnDeckDrawn(() -> onTrainCardClicked(-1));
        deckPanel.setOnDestinationDeckDrawn(this::onDrawDestinationCardsClicked);

        boardPanel.setOnRouteSelected(routeId -> {
            selectedRouteId = routeId;
            showMessage("Selected route: " + routeId);
        });

        handPanel.setOnTrainCardSelected(cardId -> {
            if (selectedTrainCardIds.contains(cardId)) {
                selectedTrainCardIds.remove(Integer.valueOf(cardId));
            } else {
                selectedTrainCardIds.add(cardId);
            }
        });
    }

    /**
     * Assembles the high-level UI layout.
     */
    private void layoutComponents() {
        GameButton claimRouteButton = new GameButton("Claim Route");
        claimRouteButton.addActionListener(
                e -> onClaimRouteClicked(selectedRouteId, selectedTrainCardIds));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(handPanel, BorderLayout.CENTER);
        bottomPanel.add(claimRouteButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(deckPanel, BorderLayout.WEST);
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(playerInfoPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Refreshes the UI components using the current game state.
     */
    @Override
    public void refresh() {
        clearTempData();

        this.selectedRouteId = null;
        this.selectedTrainCardIds.clear();
        Player currentPlayer = controller.getGame().getCurrentPlayer();
        if (currentPlayer == null) return;

        handPanel.updateDisplay(
                currentPlayer.getHand().getDestinationCards(),
                currentPlayer.getHand().getTrainCards(),
                currentPlayer.getName(),
                ThemeUtils.playerColour(currentPlayer.getColour()),
                currentPlayer.getRemainingTrainPieces(),
                currentPlayer.getScore()
        );

        playerInfoPanel.updatePlayers(controller.getGame().getPlayers(), currentPlayer);
        deckPanel.refresh();
        boardPanel.repaint();
    }

    /**
     * Clears temporary UI state.
     */
    private void clearTempData() {
        boardPanel.resetRouteId();
        this.selectedRouteId = null;
        this.selectedTrainCardIds.clear();
    }

    /**
     * Prompts the player to select destination tickets to keep.
     *
     * @param cards   the drawn destination cards
     * @param minKeep minimum number of cards to keep
     */
    @Override
    public void promptTicketSelection(List<TicketDecisionCardDTO> cards, int minKeep) {
        TicketSelectionDialog dialog = new TicketSelectionDialog(mainPanel, cards, minKeep);
        dialog.setOnConfirmed(this::onTicketsSelectionConfirmed);
        dialog.show();
    }

    /**
     * Handles completion of a destination ticket selection.
     *
     * @param keepIds    IDs of tickets to keep
     * @param discardIds IDs of tickets to discard
     */
    @Override
    public void onTicketsSelectionConfirmed(List<Integer> keepIds, List<Integer> discardIds) {
        controller.handleRequest(new KeepDrawDestRequest(keepIds, discardIds));
    }

    /**
     * Triggers the draw train card action.
     *
     * @param slot index of the clicked slot, or a special value for the deck
     */
    @Override
    public void onTrainCardClicked(int slot) {
        controller.handleRequest(new DrawTrainCardRequest(slot));
    }

    /**
     * Triggers the claim route action.
     *
     * @param routeId              unique identifier of the selected route
     * @param selectedTrainCardIds IDs of the train cards chosen to claim the route
     */
    @Override
    public void onClaimRouteClicked(String routeId, List<Integer> selectedTrainCardIds) {
        if (selectedRouteId == null) {
            showError("Please select a route first.");
            return;
        }

        if (selectedTrainCardIds == null || selectedTrainCardIds.isEmpty()) {
            showError("Please select train cards first.");
            return;
        }

        controller.handleRequest(
                new ClaimRouteRequest(selectedRouteId, new ArrayList<>(selectedTrainCardIds)));
    }

    /**
     * Triggers the draw destination card action.
     */
    @Override
    public void onDrawDestinationCardsClicked() {
        controller.handleRequest(new DrawDestCardRequest());
    }

    /**
     * Returns the root panel for this UI component.
     *
     * @return the root panel
     */
    public JPanel getPanel() {
        return mainPanel;
    }

    /**
     * Shows a message dialog.
     *
     * @param message message to display
     */
    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(mainPanel, message, "Message",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows an error dialog.
     *
     * @param error error message to display
     */
    @Override
    public void showError(String error) {
        JOptionPane.showMessageDialog(mainPanel, error, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays the final game results.
     *
     * @param gameResult the DTO containing the final game outcome
     */
    @Override
    public void showEndGameResult(GameResultDTO gameResult) {
        EndGameDialog dialog = new EndGameDialog(
                mainPanel,
                gameResult.getLeaderboard(),
                gameResult.getWinner(),
                gameResult.getIsDraw(),
                onReturnToMenu
        );
        dialog.setVisible(true);
    }

    /**
     * Sets the callback executed when returning to the main menu.
     *
     * @param callback callback used to switch screens or reset the application
     */
    @Override
    public void setOnReturnToMenu(Runnable callback) {
        this.onReturnToMenu = callback;
    }

    /**
     * Shows a warning when the final round is triggered.
     */
    @Override
    public void showLastRoundWarning() {
        JOptionPane.showMessageDialog(mainPanel,
                "A player has 2 or fewer trains remaining!\n" +
                        "This is the final round. Everyone gets one last turn.",
                "⚠️ Last Round Triggered!",
                JOptionPane.WARNING_MESSAGE);
    }
}
