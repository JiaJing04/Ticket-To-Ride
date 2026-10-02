package game.setup;

import game.card.CardColour;
import game.card.DestinationCard;
import game.card.TrainCard;
import game.character.Player;
import game.deck.DestinationCardDeck;
import game.deck.TrainCardDeck;
import game.endgame.DestinationTicketScoring;
import game.endgame.EndGameService;
import game.endgame.LongestRouteScoring;
import game.main.Game;
import game.map.Board;
import game.map.City;
import game.map.Route;
import game.map.RouteColour;
import game.map.RouteGroup;
import game.strategy.routeclaiming.GrayRouteClaimStrategy;
import game.strategy.routeclaiming.RouteClaimStrategy;
import game.strategy.routeclaiming.StandardRouteClaimStrategy;
import game.strategy.routeclaiming.FerryRouteClaimStrategy;
import game.strategy.routescoring.RouteScoringStrategy;
import game.strategy.routescoring.StandardRouteScoring;
import game.turnmanager.LastRoundTracker;
import game.turnmanager.TurnMediator;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a game using the London map.
 *
 * @author Hew Jia Jing
 */
public class LondonGameBuilder implements GameBuilder {

    private static final int TRAIN_PIECES = 17;
    private static final int TRAIN_CARD_STARTING_HAND_SIZE = 4;
    private static final int DEST_SCORING_TIE_BREAKER_ORDER = 1;
    private static final int LONGEST_ROUTE_TIE_BREAKER_ORDER = 2;

    /**
     * Game instance created by this builder.
     */
    private Game newGame;

    /**
     * Game board for the London map.
     */
    private Board board;

    /**
     * Train card deck used in the game.
     */
    private TrainCardDeck trainCardDeck;

    /**
     * Destination card deck used in the game.
     */
    private DestinationCardDeck destinationCardDeck;

    /**
     * Players participating in the game.
     */
    private List<Player> players;

    /**
     * Service used to apply end-game scoring and determine the winner.
     */
    private EndGameService endGameService;

    /**
     * Strategy used to score claimed routes.
     */
    private RouteScoringStrategy routeScoringStrategy;

    /**
     * Mediator used to manage turn actions.
     */
    private TurnMediator turnMediator;

    /**
     * Tracker used to manage final-round state.
     */
    private LastRoundTracker lastRoundTracker;

    /**
     * Strategy used to claim gray routes.
     */
    private final RouteClaimStrategy grayStrategy = new GrayRouteClaimStrategy();

    /**
     * Strategy used to claim standard coloured routes.
     */
    private final RouteClaimStrategy standardStrategy = new StandardRouteClaimStrategy();

    private final RouteClaimStrategy ferryOneWildStrategy =
            new FerryRouteClaimStrategy(1);

    private final RouteClaimStrategy ferryTwoWildStrategy =
            new FerryRouteClaimStrategy(2);

    /**
     * Builds the board for London map.
     */
    @Override
    public void buildBoard(int playerCount) {
        board = new Board();

        City regentPark = new City("Regent's Park", 185, 80);
        City kingsCross = new City("King's Cross", 530, 55);
        City bakerStreet = new City("Baker Street", 50, 190);
        City britishMuseum = new City("British Museum", 355, 290);
        City theCharterHouse = new City("The Charterhouse", 665, 170);
        City brickLane = new City("Brick Lane", 950, 220);
        City stPauls = new City("St Paul's", 700, 320);
        City towerOfLondon = new City("Tower of London", 950, 380);
        City piccadillyCircus = new City("Piccadilly Circus", 230, 430);
        City trafalgarSquare = new City("Trafalgar Square", 445, 500);
        City coventGarden = new City("Covent Garden", 455, 405);
        City globeTheatre = new City("Globe Theatre", 790, 480);
        City waterloo = new City("Waterloo", 630, 540);
        City hydePark = new City("Hyde Park", 155, 620);
        City buckinghamPalace = new City("Buckingham Palace", 310, 670);
        City bigBen = new City("Big Ben", 500, 650);
        City elephantCastle = new City("Elephant & Castle", 780, 700);

        board.addCity(regentPark);
        board.addCity(kingsCross);
        board.addCity(bakerStreet);
        board.addCity(britishMuseum);
        board.addCity(theCharterHouse);
        board.addCity(brickLane);
        board.addCity(stPauls);
        board.addCity(towerOfLondon);
        board.addCity(piccadillyCircus);
        board.addCity(trafalgarSquare);
        board.addCity(coventGarden);
        board.addCity(globeTheatre);
        board.addCity(waterloo);
        board.addCity(hydePark);
        board.addCity(buckinghamPalace);
        board.addCity(bigBen);
        board.addCity(elephantCastle);

        // Double routes
        addDoubleRoute(
                new Route("Hyde Park - Piccadilly Circus (1)", hydePark, piccadillyCircus, 2,
                        RouteColour.GREY, grayStrategy, false),
                new Route("Hyde Park - Piccadilly Circus (2)", hydePark, piccadillyCircus, 2,
                        RouteColour.GREY, grayStrategy, false),
                playerCount
        );

        addDoubleRoute(
                new Route("British Museum - Covent Garden (1)", britishMuseum, coventGarden, 1,
                        RouteColour.GREY, grayStrategy, false),
                new Route("British Museum - Covent Garden (2)", britishMuseum, coventGarden, 1,
                        RouteColour.GREY, grayStrategy, false),
                playerCount
        );

        addDoubleRoute(
                new Route("Covent Garden - St Paul's (1)", coventGarden, stPauls, 3,
                        RouteColour.GREY, grayStrategy, false),
                new Route("Covent Garden - St Paul's (2)", coventGarden, stPauls, 3,
                        RouteColour.GREY, grayStrategy, false),
                playerCount
        );

        addDoubleRoute(
                new Route("Piccadilly Circus - Covent Garden (1)", piccadillyCircus, coventGarden
                        , 1, RouteColour.GREEN, standardStrategy, false),
                new Route("Piccadilly Circus - Covent Garden (2)", piccadillyCircus, coventGarden
                        , 1, RouteColour.YELLOW, standardStrategy, false),
                playerCount
        );

        addDoubleRoute(
                new Route("Tower of London - St Paul's (1)", towerOfLondon, stPauls, 3,
                        RouteColour.YELLOW, standardStrategy, false),
                new Route("Tower of London - St Paul's (2)", towerOfLondon, stPauls, 3,
                        RouteColour.PINK, standardStrategy, false),
                playerCount
        );

        addDoubleRoute(
                new Route("Hyde Park - Buckingham Palace (1)", hydePark, buckinghamPalace, 1,
                        RouteColour.YELLOW, standardStrategy, false),
                new Route("Hyde Park - Buckingham Palace (2)", hydePark, buckinghamPalace, 1,
                        RouteColour.ORANGE, standardStrategy, false),
                playerCount
        );

        addDoubleRoute(
                new Route("Covent Garden - Trafalgar Square (1)", coventGarden, trafalgarSquare,
                        1, RouteColour.PINK, standardStrategy, false),
                new Route("Covent Garden - Trafalgar Square (2)", coventGarden, trafalgarSquare,
                        1, RouteColour.BLACK, standardStrategy, false),
                playerCount
        );

        addDoubleRoute(
                new Route("Piccadilly Circus - Trafalgar Square (1)", piccadillyCircus,
                        trafalgarSquare, 1, RouteColour.BLUE, standardStrategy, false),
                new Route("Piccadilly Circus - Trafalgar Square (2)", piccadillyCircus,
                        trafalgarSquare, 1, RouteColour.ORANGE, standardStrategy, false),
                playerCount
        );

        // Ferry routes
        addDoubleRoute(
                new Route("St Paul's - Globe Theatre (1)", stPauls, globeTheatre, 2,
                        RouteColour.GREY, ferryOneWildStrategy, true),
                new Route("St Paul's - Globe Theatre (2)", stPauls, globeTheatre, 2,
                        RouteColour.GREY, ferryOneWildStrategy, true),
                playerCount
        );

        board.addRoute(new Route("Waterloo - Trafalgar Square", waterloo, trafalgarSquare, 2,
                RouteColour.GREY, ferryTwoWildStrategy, true));

        // Grey colour routes
        board.addRoute(new Route("Baker Street - Piccadilly Circus", bakerStreet,
                piccadillyCircus, 4, RouteColour.GREY, grayStrategy, false));
        board.addRoute(new Route("Buckingham Palace - Trafalgar Square", buckinghamPalace,
                trafalgarSquare, 2, RouteColour.GREY, grayStrategy, false));
        board.addRoute(new Route("Big Ben - Trafalgar Square", bigBen, trafalgarSquare, 1,
                RouteColour.GREY, grayStrategy, false));
        board.addRoute(new Route("British Museum - Piccadilly Circus", britishMuseum,
                piccadillyCircus, 2, RouteColour.GREY, grayStrategy, false));
        board.addRoute(new Route("Tower of London - Globe Theatre", towerOfLondon, globeTheatre,
                3, RouteColour.GREY, grayStrategy, false));

        // Green colour routes
        board.addRoute(new Route("Regent's Park - King's Cross", regentPark, kingsCross, 3,
                RouteColour.GREEN, standardStrategy, false));
        board.addRoute(new Route("The Charterhouse - Brick Lane", theCharterHouse, brickLane, 3,
                RouteColour.GREEN, standardStrategy, false));
        board.addRoute(new Route("Globe Theatre - Elephant & Castle", globeTheatre,
                elephantCastle, 2, RouteColour.GREEN, standardStrategy, false));
        board.addRoute(new Route("Buckingham Palace - Big Ben", buckinghamPalace, bigBen, 2,
                RouteColour.GREEN, standardStrategy, false));

        // Yellow colour routes
        board.addRoute(new Route("Regent's Park - British Museum", regentPark, britishMuseum, 3,
                RouteColour.YELLOW, standardStrategy, false));
        board.addRoute(new Route("Big Ben - Elephant & Castle", bigBen, elephantCastle, 3,
                RouteColour.YELLOW, standardStrategy, false));

        // Pink colour routes
        board.addRoute(new Route("King's Cross - The Charterhouse", kingsCross, theCharterHouse,
                3, RouteColour.PINK, standardStrategy, false));
        board.addRoute(new Route("Waterloo - Globe Theatre", waterloo, globeTheatre, 2,
                RouteColour.PINK, standardStrategy, false));
        board.addRoute(new Route("Piccadilly Circus - Buckingham Palace", piccadillyCircus,
                buckinghamPalace, 2, RouteColour.PINK, standardStrategy, false));

        // Blue colour routes
        board.addRoute(new Route("Baker Street - Regent's Park", bakerStreet, regentPark, 2,
                RouteColour.BLUE, standardStrategy, false));
        board.addRoute(new Route("British Museum - The Charterhouse", britishMuseum,
                theCharterHouse, 4, RouteColour.BLUE, standardStrategy, false));
        board.addRoute(new Route("Waterloo - Big Ben", waterloo, bigBen, 1, RouteColour.BLUE,
                standardStrategy, false));
        board.addRoute(new Route("Tower of London - Brick Lane", towerOfLondon, brickLane, 3,
                RouteColour.BLUE, standardStrategy, false));

        // Black colour routes
        board.addRoute(new Route("Baker Street - Hyde Park", bakerStreet, hydePark, 4,
                RouteColour.BLACK, standardStrategy, false));
        board.addRoute(new Route("King's Cross - British Museum", kingsCross, britishMuseum, 2,
                RouteColour.BLACK, standardStrategy, false));
        board.addRoute(new Route("The Charterhouse - St Paul's", theCharterHouse, stPauls, 1,
                RouteColour.BLACK, standardStrategy, false));
        board.addRoute(new Route("Elephant & Castle - Tower of London", elephantCastle,
                towerOfLondon, 4, RouteColour.BLACK, standardStrategy, false));

        // Orange colour routes
        board.addRoute(new Route("Brick Lane - St Paul's", brickLane, stPauls, 3,
                RouteColour.ORANGE, standardStrategy, false));
        board.addRoute(new Route("Baker Street - British Museum", bakerStreet, britishMuseum, 4,
                RouteColour.ORANGE, standardStrategy, false));
        board.addRoute(new Route("Waterloo - Elephant & Castle", waterloo, elephantCastle, 2,
                RouteColour.ORANGE, standardStrategy, false));
    }

    /**
     * Helps to group 2 parallel routes together and add them to the board
     *
     * @param route1      Route 1 to be added in double group
     * @param route2      Route 2 to be added in double group
     * @param playerCount The player count in game
     */
    private void addDoubleRoute(Route route1, Route route2, int playerCount) {
        RouteGroup group = new RouteGroup(2);

        group.addRoute(route1);
        group.addRoute(route2);

        board.addRoute(route1);
        board.addRoute(route2);
    }

    /**
     * Builds the decks for London map.
     */
    @Override
    public void buildDecks() {
        trainCardDeck = createTrainDeck();
        destinationCardDeck = createDestinationDeck();
    }

    /**
     * Builds the TrainCardDeck.
     *
     * @return The created TrainCardDeck.
     */
    private TrainCardDeck createTrainDeck() {
        TrainCardDeck deck = new TrainCardDeck();

        int trainCardId = 1; // Local counter for train card IDs

        for (CardColour colour : CardColour.values()) {

            int numberOfCardsToGenerate = (colour == CardColour.WILD) ? 8 : 6;

            for (int i = 0; i < numberOfCardsToGenerate; i++) {
                deck.addCard(new TrainCard(trainCardId++, colour));
            }
        }

        // Randomise deck order before use
        deck.shuffle();

        return deck;
    }

    /**
     * Builds the DestinationCardDeck.
     *
     * @return The created DestinationCardDeck.
     */
    private DestinationCardDeck createDestinationDeck() {
        DestinationCardDeck deck = new DestinationCardDeck();

        int destId = 1; // Local counter for destination card IDs

        deck.addCard(new DestinationCard(destId++, board.getCityByName("Baker Street"), board.getCityByName("Big Ben"), 7));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("King's Cross"), board.getCityByName("Trafalgar Square"), 5));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("The Charterhouse"), board.getCityByName("Piccadilly Circus"), 6));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("King's Cross"), board.getCityByName("Big Ben"), 7));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("King's Cross"), board.getCityByName("Globe Theatre"), 6));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("Brick Lane"), board.getCityByName("Elephant & Castle"), 7));

        deck.addCard(new DestinationCard(destId++, board.getCityByName("St Paul's"), board.getCityByName("Big Ben"), 5));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("Buckingham Palace"), board.getCityByName("Brick Lane"), 11));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("Brick Lane"), board.getCityByName("Covent Garden"), 6));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("Tower of London"), board.getCityByName("Big Ben"), 7));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("Buckingham Palace"), board.getCityByName("Tower of London"), 10));

        deck.addCard(new DestinationCard(destId++, board.getCityByName("Baker Street"), board.getCityByName("St Paul's"), 9));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("Globe Theatre"), board.getCityByName("Buckingham Palace"), 6));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("Hyde Park"), board.getCityByName("Tower of London"), 11));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("Regent's Park"), board.getCityByName("Big Ben"), 8));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("British Museum"), board.getCityByName("Buckingham Palace"), 4));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("Waterloo"), board.getCityByName("Hyde Park"), 5));

        deck.addCard(new DestinationCard(destId++, board.getCityByName("King's Cross"), board.getCityByName("Waterloo"), 8));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("Brick Lane"), board.getCityByName("Trafalgar Square"), 8));
        deck.addCard(new DestinationCard(destId++, board.getCityByName("Regent's Park"), board.getCityByName("Tower of London"), 12));

        // Randomise deck order before use
        deck.shuffle();

        return deck;
    }

    /**
     * Builds the services for London map
     */
    @Override
    public void buildServices() {
        endGameService = new EndGameService();

        // Scoring strategies used at end-game evaluation
        LongestRouteScoring longestRouteScoring = new LongestRouteScoring(LONGEST_ROUTE_TIE_BREAKER_ORDER);
        DestinationTicketScoring destinationTicketScoring = new DestinationTicketScoring(DEST_SCORING_TIE_BREAKER_ORDER);

        // Register scoring strategies into end game service
        endGameService.addEndGameScoringStrategy(longestRouteScoring);
        endGameService.addEndGameScoringStrategy(destinationTicketScoring);

        // Register tiebreaker strategies into end game service
        endGameService.addTieBreakerStrategy(longestRouteScoring);
        endGameService.addTieBreakerStrategy(destinationTicketScoring);

        routeScoringStrategy = new StandardRouteScoring();
        turnMediator = new TurnMediator();
        lastRoundTracker = new LastRoundTracker();
    }

    /**
     * Builds the players for London map
     *
     * @param playerConfigs player setup information
     */
    @Override
    public void buildPlayers(List<PlayerConfiguration> playerConfigs) {
        players = new ArrayList<>();

        for (PlayerConfiguration config : playerConfigs) {
            players.add(new Player(config.getName(), config.getColour(), TRAIN_PIECES,
                    config.getAbility()));
        }
    }

    /**
     * Deals starting cards to players for London map version.
     */
    @Override
    public void dealStartingHands() {

        // Deal 4 train cards to each player
        for (Player player : players) {
            List<TrainCard> startingCards = new ArrayList<>();

            for (int i = 0; i < TRAIN_CARD_STARTING_HAND_SIZE; i++) {
                TrainCard card = trainCardDeck.drawBySlot(trainCardDeck.getDeckSlot());
                if (card != null) {
                    startingCards.add(card);
                }
            }

            player.getHand().addTrainCards(startingCards);
        }

        // After dealing, reveal 5 face-up cards for the table
        trainCardDeck.initialiseFaceUpPile();
    }

    /**
     * Returns the completed London map game.
     *
     * @param mapType the map type used by the game
     * @return The completed London map game.
     */
    @Override
    public Game getResult(MapType mapType) {
        if (newGame == null) {
            newGame = new Game(
                    players,
                    board,
                    trainCardDeck,
                    destinationCardDeck,
                    turnMediator,
                    lastRoundTracker,
                    endGameService,
                    routeScoringStrategy,
                    mapType);
        }
        return newGame;
    }
}
