package prototype.prototypeJiaYu;

import java.util.List;
import java.util.Scanner;

public class Game {
    private List<Route> routeOnMap;

    private Player player1;

    private Player player2;

    private Player currentPlayer;

    public Game(Player player1, Player player2, List<Route> routeOnMap) {
        this.player1 = player1;
        this.player2 = player2;
        this.routeOnMap = routeOnMap;
    }

    public String startGame() {
        this.currentPlayer = player1;
        return "hello this is a Ticket To Ride prototype." ;
    }

    public void playGame() {
        boolean flag = true;
        while (flag) {
            int routeIndex = showMenuAndWaitInput();
            claimRoute(currentPlayer, routeIndex - 1);

            if (routeOnMap.isEmpty()) {
                Player winner = player1.getRouteClaimed().size() > player2.getRouteClaimed().size() ? player1 : player2;
                System.out.println("GAME OVER! No routes left!");
                System.out.println("Player " + player1 + " claimed routes: " + player1.getRouteClaimed());
                System.out.println("Player " + player2 + " claimed routes: " + player2.getRouteClaimed());
                System.out.println("Winner is Player " + winner);
                flag = false;

            }
            else {
                if (currentPlayer == player1) {
                    currentPlayer = player2;
                }
                else {
                    currentPlayer = player1;
                }
            }
        }
    }

    public void claimRoute(Player player, int routeIndex) {
        Route route = this.routeOnMap.get(routeIndex);
        player.claimRoute(route);
        routeOnMap.remove(route);
    }

    public int showMenuAndWaitInput() {
        Scanner scanner = new Scanner(System.in);

        // Show menu
        for (int i = 0; i < routeOnMap.size(); i++) {
            System.out.println((i + 1) + ". " + routeOnMap.get(i));
        }
        System.out.println("Current Player: " + currentPlayer);
        System.out.print("Select a route: ");
        
        int choice = 0;
        boolean flag = true;
        while (flag) {
            choice = scanner.nextInt();
            if (choice > 0 && choice <= routeOnMap.size()) {
                flag = false;
            }
            else {
                System.out.println("Invalid selection. Please choose again");
            }
        }
        
        return choice;
    }
}
