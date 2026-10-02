package prototype.prototypeZiNg;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private TrainCardDeck trainCardDeck;
    private List<Player> players;
    private int playerIndex;
    private Scanner scanner;

    public Game(List<Player> players, TrainCardDeck trainCardDeck) {
        this.trainCardDeck = trainCardDeck;
        this.players = players;
        this.playerIndex = 0;
        this.scanner = new Scanner(System.in);
    }

    public void playGame() {
        boolean running = true;
        while (running) {
            // Check if game should even continue
            if (checkIsGameOver()) break;

            Player curPlayer = players.get(playerIndex);
            System.out.println("\n===========================");
            System.out.println("Current Player: " + curPlayer.getName());

            for (int draw = 1; draw <= 2; draw++) {
                if (checkIsGameOver()) {
                    running = false;
                    break;
                }

                executeDrawAction(curPlayer, draw);
            }

            if (!running) {
                break;
            }

            switchPlayer();
        }
        endGame();
    }

    private boolean checkIsGameOver() {
        return trainCardDeck.getFaceUpPile().isEmpty() &&
                trainCardDeck.getInDeckPile().isEmpty();
    }

    private void executeDrawAction(Player player, int drawNumber) {
        boolean deckAvailable = !trainCardDeck.getInDeckPile().isEmpty();
        boolean faceUpAvailable = !trainCardDeck.getFaceUpPile().isEmpty();

        showFaceUpCards();
        System.out.println("\nDraw (" + drawNumber + "/2)");

        if (deckAvailable && faceUpAvailable) {

            int action = getValidIntInput("1. Draw from face up\n" +
                    "2. Draw from deck\n" +
                    "Choose action: ", 1, 2);
            if (action == 1) {
                drawFromFaceUp(player);
            }
            else {
                drawFromDeck(player);
            }
        }
        else if (faceUpAvailable) {
            int action = getValidIntInput("1. Draw from face up\n" +
                    "Choose action: ", 1, 1);
            if (action == 1) {
                drawFromFaceUp(player);
            }
        }
        else if (deckAvailable) {
            int action = getValidIntInput("1. Draw from deck\n" +
                    "Choose action: ", 1, 1);
            if (action == 1) {
                drawFromDeck(player);
            }
        }
    }

    private void drawFromDeck(Player player) {
        TrainCard card = trainCardDeck.drawFromDeck();

        if (card != null) {
            player.getPlayerHand().addTrainCard(card);

            System.out.println(player.getName() +
                    " took card from the deck.");
        }
    }

    private void drawFromFaceUp(Player player) {
        List<TrainCard> faceUpCards = trainCardDeck.getFaceUpPile();
        int choice = getValidIntInput("Choose card number (1-"
                + faceUpCards.size() + "): ", 1, faceUpCards.size());

        TrainCard card = trainCardDeck.drawFaceUp(choice - 1);
        player.getPlayerHand().addTrainCard(card);
        System.out.println(player.getName() + " took " + card);
    }

    private int getValidIntInput(String prompt, int min, int max) {
        int choice = -1;
        while (true) {
            System.out.print(prompt);

            try {
                choice = Integer.parseInt(scanner.next());
                if (choice >= min && choice <= max) break;
            } catch (NumberFormatException e) {
                // Not an integer
            }
            System.out.println("Invalid input. " +
                    "Please enter a number between " +
                    min + " and " + max + ".");
        }
        return choice;
    }

    public void showFaceUpCards() {

        System.out.println("\n--- Face Up Cards ---");
        ArrayList<TrainCard> faceUpCards = trainCardDeck.getFaceUpPile();

        if (faceUpCards.isEmpty()) {
            System.out.println("[ Empty ]");
        } else {
            for (int i = 0; i < faceUpCards.size(); i++) {
                System.out.println((i + 1) + ") " + faceUpCards.get(i));
            }
        }
    }

    private void switchPlayer() {
        playerIndex = (playerIndex + 1) % players.size();
    }

    private void endGame() {
        System.out.println("\n=======================");
        System.out.println("       GAME OVER       ");
        System.out.println("=======================");

        Player winner = players.get(0);
        for (Player p : players) {
            int count = p.getPlayerHand().getTrainCards().size();

            System.out.println(p.getName() + ": " + count + " cards");

            if (count > winner.getPlayerHand().getTrainCards().size()) {
                winner = p;
            }
        }
        System.out.println("\nThe Winner is: " + winner.getName() + "!");
    }
}
