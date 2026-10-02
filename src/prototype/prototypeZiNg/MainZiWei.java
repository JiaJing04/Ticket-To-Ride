package prototype.prototypeZiNg;


import java.util.ArrayList;
import java.util.List;

public class MainZiWei {

    public static void main(String[] var0) {

        ArrayList<TrainCard> faceUpCards = new ArrayList<>();
        faceUpCards.add(new TrainCard("Red", "Standard"));
        faceUpCards.add(new TrainCard("Blue", "Standard"));
        faceUpCards.add(new TrainCard("Green", "Standard"));
        faceUpCards.add(new TrainCard("Yellow", "Standard"));
        faceUpCards.add(new TrainCard("Black", "Standard"));

        ArrayList<TrainCard> inDeckCards = new ArrayList<>();
        inDeckCards.add(new TrainCard("Red", "Standard"));
        inDeckCards.add(new TrainCard("Blue", "Standard"));
        inDeckCards.add(new TrainCard("Green", "Standard"));
        inDeckCards.add(new TrainCard("Yellow", "Standard"));
        inDeckCards.add(new TrainCard("Black", "Standard"));

        TrainCardDeck trainCardDeck = new TrainCardDeck();
        trainCardDeck.setFaceUpPile(faceUpCards);
        trainCardDeck.setInDeckPile(inDeckCards);

        // set up players
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));

        Game game = new Game(players, trainCardDeck);
        game.playGame();

    }
}
