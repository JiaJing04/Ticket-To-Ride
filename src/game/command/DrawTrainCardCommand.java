package game.command;

import game.card.TrainCard;
import game.character.Player;
import game.deck.TrainCardDeck;

/**
 * Executes the action of drawing a train card.
 *
 * @author Hew Jia Jing
 */
public class DrawTrainCardCommand implements ActionCommand {

    /**
     * Slot index indicating where to draw from.
     * A special value represents drawing from the deck instead of face-up cards.
     */
    private final int slot;

    /**
     * Deck used to draw train cards.
     */
    private final TrainCardDeck trainCardDeck;

    /**
     * Constructs a draw train card command.
     *
     * @param slot          slot index to draw from the deck or face-up pile
     * @param trainCardDeck train card deck
     */
    public DrawTrainCardCommand(int slot, TrainCardDeck trainCardDeck) {
        this.slot = slot;
        this.trainCardDeck = trainCardDeck;
    }

    /**
     * Executes the draw train card action.
     *
     * @param currentPlayer player performing the action
     * @return result of the action execution
     */
    @Override
    public ActionResult execute(Player currentPlayer) {
        // only condition to fail are drawing invalid face-up
        if (!trainCardDeck.canDraw(slot)) {
            return ActionResult.fail("Invalid face-up card slot.");
        }

        TrainCard drawnCard = trainCardDeck.drawBySlot(slot);

        if (drawnCard == null) {
            return ActionResult.fail("No train card is available to draw.");
        }

        currentPlayer.drawTrainCard(drawnCard);

        if (drawnCard.isLocomotive() && slot != trainCardDeck.getDeckSlot()) {
            return ActionResult.success("Drew a face-up locomotive card. No more actions can be done.");
        }

        return ActionResult.success("Drew a train card.");
    }

    /**
     * Returns the action cost of drawing this card.
     *
     * @return the action cost
     */
    @Override
    public ActionCost getCost() {
        if (slot == trainCardDeck.getDeckSlot()) {
            return ActionCost.HALF;
        }

        TrainCard faceUpCard = trainCardDeck.peekFaceUp(slot);

        if (faceUpCard != null && faceUpCard.isLocomotive()) {
            return ActionCost.FULL;
        }

        return ActionCost.HALF;
    }
}
