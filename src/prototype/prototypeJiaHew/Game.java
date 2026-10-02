package prototype.prototypeJiaHew;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private List<DestinationCard> destinationDeck;
    private List<TrainCard> trainDeck;

    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public Game(Player player1, Player player2, List<DestinationCard> destinationDeck, List<TrainCard> trainDeck)
    {
        this.player1 = player1;
        this.player2 = player2;
        this.destinationDeck = destinationDeck;
        this.trainDeck = trainDeck;
    }

    public String startGame()
    {
        this.currentPlayer = player1;
        return "Welcome to Ticket To Ride!!!";
    }

    public void playGame()
    {
        boolean flag = true;
        while (flag) {
            int action = showActionMenu();

            if (action == 1)
            {
                // draw 3 destination cards
                List<DestinationCard> drawn = drawFromDeck(destinationDeck, 3);
                if (drawn.isEmpty())
                {
                    System.out.println("No destination cards left in the deck!");
                } else
                {
                    currentPlayer.drawDestinationCards(drawn);
                    System.out.println(currentPlayer + " drew destination cards: " + drawn);
                }

            } else if (action == 2)
            {
                // draw 2 train cards
                List<TrainCard> drawn = drawFromDeck(trainDeck, 2);
                if (drawn.isEmpty())
                {
                    System.out.println("No train cards left in the deck!");
                } else
                {
                    currentPlayer.drawTrainCards(drawn);
                    System.out.println(currentPlayer + " drew train cards: " + drawn);
                }
            }

            // check end condition: both decks are empty
            if (destinationDeck.isEmpty() && trainDeck.isEmpty())
            {
                Player winner = player1.getHandSize() > player2.getHandSize() ? player1 : player2;
                System.out.println("\nGAME OVER! Both decks are empty!");
                System.out.println("Player " + player1 + " total cards: " + player1.getHandSize());
                System.out.println("Player " + player2 + " total cards: " + player2.getHandSize());
                System.out.println("Winner is Player " + winner);
                flag = false;
            } else
            {
                switchPlayer();
            }
        }
    }

    public int showActionMenu()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Current Player: " + currentPlayer + " ---");
        System.out.println("1. Draw 3 Destination Cards (remaining: " + destinationDeck.size() + ")");
        System.out.println("2. Draw 2 Train Cards (remaining: " + trainDeck.size() + ")");
        System.out.print("Choose an action: ");

        int choice = 0;
        boolean flag = true;
        while (flag)
        {
            choice = scanner.nextInt();
            if (choice == 1 || choice == 2)
            {
                flag = false;
            } else
            {
                System.out.println("Invalid selection. Please choose 1 or 2.");
            }
        }

        return choice;
    }

    private <T> List<T> drawFromDeck(List<T> deck, int count)
    {
        List<T> drawn = new ArrayList<>();
        for (int i = 0; i < count && !deck.isEmpty(); i++)
        {
            drawn.add(deck.remove(0));
        }
        return drawn;
    }

    private void switchPlayer()
    {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }
}
