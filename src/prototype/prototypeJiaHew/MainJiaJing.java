package prototype.prototypeJiaHew;

import java.util.ArrayList;
import java.util.List;

public class MainJiaJing {

    public static void main(String[] args)
    {
        // set up destination deck
        List<DestinationCard> destinationDeck = new ArrayList<>();
        destinationDeck.add(new DestinationCard("Johor-Seremban"));
        destinationDeck.add(new DestinationCard("Penang-KL"));
        destinationDeck.add(new DestinationCard("Selangor-Pahang"));
        destinationDeck.add(new DestinationCard("Terengganu-Kelantan"));
        destinationDeck.add(new DestinationCard("Penang-Terengganu"));
        destinationDeck.add(new DestinationCard("Johor-Penang"));

        // set up train deck
        List<TrainCard> trainDeck = new ArrayList<>();
        trainDeck.add(new TrainCard("Red"));
        trainDeck.add(new TrainCard("Blue"));
        trainDeck.add(new TrainCard("Green"));
        trainDeck.add(new TrainCard("Yellow"));
        trainDeck.add(new TrainCard("Black"));
        trainDeck.add(new TrainCard("White"));
        trainDeck.add(new TrainCard("Orange"));
        trainDeck.add(new TrainCard("Pink"));

        // set up players
        Player player1 = new Player("A");
        Player player2 = new Player("B");

        // start game
        Game game = new Game(player1, player2, destinationDeck, trainDeck);
        System.out.println(game.startGame());
        game.playGame();
    }
}
