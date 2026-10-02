package game.save.mapper;

import game.card.DestinationCard;
import game.card.TrainCard;
import game.character.Player;
import game.character.PlayerHand;
import game.main.Game;
import game.save.memento.GameMemento;
import game.save.memento.PlayerMemento;
import game.setup.PlayerConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Maps player state between live game objects and their memento representations.
 *
 * @author Hew Jia Jing
 */
public class PlayerStateMapper {

    /**
     * Factory used to reconstruct player abilities during restoration.
     */
    private final AbilityFactory abilityFactory = new AbilityFactory();

    /**
     * Saves all player state into the given memento.
     *
     * @param game the game whose player state should be saved
     * @param memento the destination memento
     */
    public void save(Game game, GameMemento memento) {
        for (Player player : game.getPlayers()) {
            PlayerMemento snapshot = new PlayerMemento();

            snapshot.setName(player.getName());
            snapshot.setColour(player.getColour());
            snapshot.setScore(player.getScore());
            snapshot.setRemainingTrainPieces(player.getRemainingTrainPieces());
            snapshot.setAbilityName(player.getAbility().getName());
            snapshot.setHasDoubleTurn(player.hasDoubleTurn());

            for (List<TrainCard> cardsByColour
                    : player.getHand().getTrainCards().values()) {
                for (TrainCard card : cardsByColour) {
                    snapshot.getTrainCardIds().add(card.getId());
                }
            }

            for (DestinationCard card
                    : player.getHand().getDestinationCards()) {
                snapshot.getDestinationCardIds().add(card.getId());
            }

            memento.getPlayers().add(snapshot);
        }
    }

    /**
     * Converts saved player information into player configurations used
     * during game reconstruction.
     *
     * @param memento the saved game state
     * @return the player configurations derived from the saved state
     */
    public List<PlayerConfiguration> toPlayerConfigs(GameMemento memento) {
        List<PlayerConfiguration> playerConfigs = new ArrayList<>();

        for (PlayerMemento playerSnapshot : memento.getPlayers()) {
            playerConfigs.add(
                    new PlayerConfiguration(
                            playerSnapshot.getName(),
                            playerSnapshot.getColour(),
                            abilityFactory.createAbility(
                                    playerSnapshot.getAbilityName()
                            )
                    )
            );
        }

        return playerConfigs;
    }


    /**
     * Restores all player state from the given memento.
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

        for (PlayerMemento snapshot : memento.getPlayers()) {
            Player player = findPlayerByName(game, snapshot.getName());

            player.setScore(snapshot.getScore());
            player.setRemainingTrainPieces(
                    snapshot.getRemainingTrainPieces()
            );
            player.restoreDoubleTurn(snapshot.hasDoubleTurn());

            PlayerHand hand = player.getHand();
            hand.clearTrainCards();
            hand.clearDestinationCards();

            hand.addTrainCards(
                    toTrainCards(snapshot.getTrainCardIds(), trainCardsById)
            );

            hand.addDestinationCards(
                    toDestinationCards(
                            snapshot.getDestinationCardIds(),
                            destinationCardsById
                    )
            );
        }
    }

    /**
     * Finds a player by name.
     *
     * @param game the game containing the players
     * @param name the player name to search for
     * @return the matching player
     * @throws IllegalArgumentException if no player with the given name exists
     */
    public Player findPlayerByName(Game game, String name) {
        for (Player player : game.getPlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }

        throw new IllegalArgumentException(
                "Player not found: " + name
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
