package game.save.mapper;

import game.card.DestinationCard;
import game.card.TrainCard;
import game.character.Player;
import game.main.Game;
import game.save.memento.GameMemento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maps deck state between a live game and its memento representation.
 *
 * @author Hew Jia Jing
 */
public class DeckStateMapper {

    /**
     * Saves all deck state into the given memento.
     *
     * @param game the game whose deck state should be saved
     * @param memento the memento to update
     */
    public void save(Game game, GameMemento memento) {
        saveTrainDeck(game, memento);
        saveDestinationDeck(game, memento);
    }

    /**
     * Restores all deck state from the given memento.
     *
     * @param game the game to restore
     * @param memento the saved game state
     * @param trainCardsById train cards indexed by ID
     * @param destinationCardsById destination cards indexed by ID
     */
    public void restore(
            Game game,
            GameMemento memento,
            Map<Integer, TrainCard> trainCardsById,
            Map<Integer, DestinationCard> destinationCardsById) {

        restoreTrainDeck(game, memento, trainCardsById);
        restoreDestinationDeck(game, memento, destinationCardsById);
    }

    /**
     * Indexes all train cards in the game by card ID.
     *
     * @param game the game containing the train cards
     * @return a map of train card IDs to train cards
     */
    public Map<Integer, TrainCard> indexTrainCards(Game game) {
        Map<Integer, TrainCard> cards = new HashMap<>();

        for (TrainCard card : game.getTrainCardDeck().getInDeckPile()) {
            cards.put(card.getId(), card);
        }

        for (TrainCard card : game.getTrainCardDeck().getFaceUpPile()) {
            if (card != null) {
                cards.put(card.getId(), card);
            }
        }

        for (TrainCard card : game.getTrainCardDeck().getDiscardedPile()) {
            cards.put(card.getId(), card);
        }

        for (Player player : game.getPlayers()) {
            for (List<TrainCard> cardsByColour
                    : player.getHand().getTrainCards().values()) {
                for (TrainCard card : cardsByColour) {
                    cards.put(card.getId(), card);
                }
            }
        }

        return cards;
    }

    /**
     * Indexes all destination cards in the game by card ID.
     *
     * @param game the game containing the destination cards
     * @return a map of destination card IDs to destination cards
     */
    public Map<Integer, DestinationCard> indexDestinationCards(Game game) {
        Map<Integer, DestinationCard> cards = new HashMap<>();

        for (DestinationCard card
                : game.getDestinationCardDeck().getInDeckPile()) {
            cards.put(card.getId(), card);
        }

        for (Player player : game.getPlayers()) {
            for (DestinationCard card : player.getHand().getDestinationCards()) {
                cards.put(card.getId(), card);
            }
        }

        return cards;
    }

    /**
     * Saves the train deck state into the memento.
     *
     * @param game the game whose train deck should be saved
     * @param memento the destination memento
     */
    private void saveTrainDeck(Game game, GameMemento memento) {
        for (TrainCard card : game.getTrainCardDeck().getInDeckPile()) {
            memento.getTrainDeckCards().add(card.getId());
        }

        for (TrainCard card : game.getTrainCardDeck().getFaceUpPile()) {
            if (card != null) {
                memento.getTrainFaceUpCards().add(card.getId());
            }
        }

        for (TrainCard card : game.getTrainCardDeck().getDiscardedPile()) {
            memento.getTrainDiscardCards().add(card.getId());
        }
    }

    /**
     * Saves the destination deck state into the memento.
     *
     * @param game the game whose destination deck should be saved
     * @param memento the destination memento
     */
    private void saveDestinationDeck(Game game, GameMemento memento) {
        for (DestinationCard card
                : game.getDestinationCardDeck().getInDeckPile()) {
            memento.getDestinationDeckCards().add(card.getId());
        }
    }

    /**
     * Restores the train deck state from the memento.
     *
     * @param game the game to restore
     * @param memento the saved game state
     * @param trainCardsById train cards indexed by ID
     */
    private void restoreTrainDeck(
            Game game,
            GameMemento memento,
            Map<Integer, TrainCard> trainCardsById) {

        game.getTrainCardDeck().setInDeckPile(
                toTrainCards(memento.getTrainDeckCards(), trainCardsById)
        );

        game.getTrainCardDeck().setFaceUpPile(
                toTrainCards(memento.getTrainFaceUpCards(), trainCardsById)
        );

        game.getTrainCardDeck().setDiscardedPile(
                toTrainCards(memento.getTrainDiscardCards(), trainCardsById)
        );
    }

    /**
     * Restores the destination deck state from the memento.
     *
     * @param game the game to restore
     * @param memento the saved game state
     * @param destinationCardsById destination cards indexed by ID
     */
    private void restoreDestinationDeck(
            Game game,
            GameMemento memento,
            Map<Integer, DestinationCard> destinationCardsById) {

        game.getDestinationCardDeck().setInDeckPile(
                toDestinationCards(
                        memento.getDestinationDeckCards(),
                        destinationCardsById
                )
        );
    }

    /**
     * Converts a list of train card IDs into train card objects.
     *
     * @param ids the train card IDs to resolve
     * @param cardsById train cards indexed by ID
     * @return the resolved train cards
     * @throws IllegalArgumentException if a card ID cannot be found
     */
    private List<TrainCard> toTrainCards(
            List<Integer> ids,
            Map<Integer, TrainCard> cardsById) {

        List<TrainCard> cards = new ArrayList<>();

        for (Integer id : ids) {
            TrainCard card = cardsById.get(id);

            if (card == null) {
                throw new IllegalArgumentException(
                        "Unknown train card id: " + id
                );
            }

            cards.add(card);
        }

        return cards;
    }

    /**
     * Converts a list of destination card IDs into destination card objects.
     *
     * @param ids the destination card IDs to resolve
     * @param cardsById destination cards indexed by ID
     * @return the resolved destination cards
     * @throws IllegalArgumentException if a card ID cannot be found
     */
    private List<DestinationCard> toDestinationCards(
            List<Integer> ids,
            Map<Integer, DestinationCard> cardsById) {

        List<DestinationCard> cards = new ArrayList<>();

        for (Integer id : ids) {
            DestinationCard card = cardsById.get(id);

            if (card == null) {
                throw new IllegalArgumentException(
                        "Unknown destination card id: " + id
                );
            }

            cards.add(card);
        }

        return cards;
    }
}
