package game.character;

import game.card.CardColour;
import game.card.DestinationCard;
import game.card.TrainCard;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the cards currently held by a player.
 *
 * @author Hew Jia Jing
 */
public class PlayerHand {

    /**
     * Stores train cards grouped by colour.
     */
    private final Map<CardColour, List<TrainCard>> trainCards;

    /**
     * Stores all destination cards in the player's hand.
     */
    private final List<DestinationCard> destinationCards;

    /**
     * Creates an empty player hand.
     */
    public PlayerHand() {
        trainCards = new EnumMap<>(CardColour.class);
        destinationCards = new ArrayList<>();

        for (CardColour colour : CardColour.values()) {
            trainCards.put(colour, new ArrayList<>());
        }
    }

    /**
     * Returns the player's train cards grouped by colour.
     */
    public Map<CardColour, List<TrainCard>> getTrainCards() {
        Map<CardColour, List<TrainCard>> copy = new EnumMap<>(CardColour.class);

        for (Map.Entry<CardColour, List<TrainCard>> entry : trainCards.entrySet()) {
            copy.put(entry.getKey(), List.copyOf(entry.getValue()));
        }

        return copy;
    }

    /**
     * Returns the player's destination cards.
     */
    public List<DestinationCard> getDestinationCards() {
        return List.copyOf(destinationCards);
    }

    /**
     * Returns the number of locomotive cards in the hand.
     */
    public int getLocomotiveCount() {
        return getTrainCardCountByColour(CardColour.WILD);
    }

    /**
     * Returns the number of train cards for the given colour.
     *
     * @param colour the card colour to count
     * @return the number of cards with the given colour
     */
    public int getTrainCardCountByColour(CardColour colour) {
        return trainCards.get(colour).size();
    }

    /**
     * Adds train cards to the hand.
     *
     * @param cards the train cards to add
     */
    public void addTrainCards(List<TrainCard> cards) {
        for (TrainCard card : cards) {
            trainCards.get(card.getColour()).add(card);
        }
    }

    /**
     * Removes all train cards from the player's hand.
     */
    public void clearTrainCards() {
        trainCards.values().forEach(List::clear);
    }

    /**
     * Removes all destination cards from the player's hand.
     */
    public void clearDestinationCards() {
        destinationCards.clear();
    }

    /**
     * Adds destination cards to the hand.
     *
     * @param cards the destination cards to add
     */
    public void addDestinationCards(List<DestinationCard> cards) {
        destinationCards.addAll(cards);
    }

    /**
     * Removes train cards from the hand.
     *
     * @param cards the train cards to remove
     */
    public void removeTrainCards(List<TrainCard> cards) {
        for (TrainCard card : cards) {
            trainCards.get(card.getColour()).remove(card);
        }
    }

    /**
     * Removes destination cards from the hand.
     *
     * @param cards the destination cards to remove
     */
    public void removeDestinationCards(List<DestinationCard> cards) {
        destinationCards.removeAll(cards);
    }

    /**
     * Finds train cards by their IDs.
     *
     * @param ids the card IDs to search for
     * @return matching train cards
     */
    public List<TrainCard> getTrainCardByIds(List<Integer> ids) {
        List<TrainCard> result = new ArrayList<>();

        for (List<TrainCard> cardsByColour : trainCards.values()) {
            for (TrainCard card : cardsByColour) {
                if (ids.contains(card.getId())) {
                    result.add(card);
                }
            }
        }

        return result;
    }

    /**
     * Finds destination cards by their IDs.
     *
     * @param ids the card IDs to search for
     * @return matching destination cards
     */
    public List<DestinationCard> getDestinationCardByIds(List<Integer> ids) {
        List<DestinationCard> result = new ArrayList<>();

        for (DestinationCard card : destinationCards) {
            if (ids.contains(card.getId())) {
                result.add(card);
            }
        }

        return result;
    }

    /**
     * Checks whether the hand has enough cards of any one colour, including
     * locomotives.
     *
     * @param needed the number of cards required
     * @return true if any colour plus locomotives reaches the needed amount
     */
    public boolean hasEnoughOfAnyColour(int needed) {
        for (CardColour colour : CardColour.values()) {
            if (colour == CardColour.WILD) {
                continue;
            }

            int count = getTrainCardCountByColour(colour) + getLocomotiveCount();

            if (count >= needed) {
                return true;
            }
        }

        return false;
    }
}
