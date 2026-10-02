package prototype.prototypeJiaYu;

import java.util.ArrayList;
import java.util.List;

public class MainJiaMing {
    public static void main(String[] args) {
        System.out.println("This is a simple Ticket To Ride prototype");
        Route route1 = new Route("RouteA");
        Route route2 = new Route("RouteB");
        Route route3 = new Route("RouteC");
        List<Route> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);

        Player player1 = new Player("A");
        Player player2 = new Player("B");

        Game game = new Game(player1, player2, routes);

        game.startGame();
        game.playGame();
    }
}