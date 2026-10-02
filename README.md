# FIT3077 Ticket To Ride

## Project Organisation

This project covers a basic Ticket To Ride gameplay,
structured using a modular design,
where each package represents a specific responsibility in the game system.

## Core Packages

The project is structured into packages, where each package groups related classes based on responsibility.

- **card**
    - Contains card-related classes:
        - Card (abstract class)
        - TrainCard
        - DestinationCard
        - CardColour (enum)

- **character**
    - Represents player-related components:
        - Player
        - PlayerHand
        - PlayerColour (enum)

- **command**
    - Implements the Command Pattern for player actions:
        - ActionCommand
        - ClaimRouteCommand
        - DrawTrainCardCommand
        - DrawDestCommand
        - KeepDestCommand
        - InvalidCommand
        - EndTurnOutcome (enum)
        - ActionCost (enum)
        - ActionResult
        - ActionStatus (enum)

- **deck**
    - Handles card deck operations:
        - Deck (abstract class)
        - TrainCardDeck
        - DestinationCardDeck

- **endgame**
    - Manages end-game logic and scoring:
        - EndGameService
        - EndGameScoringStrategy
        - DestinationTicketScoring
        - LongestRouteScoring
        - TieBreakerStrategy

- **main**
    - Core game logic and entry point, including DTO classes to be sent to frontend:
        - dto
          - TicketDecisionCardDTO
          - GameResultDTO
        - Game
        - GameController
        - PendingDecisionVisitor

- **map**
    - Represents the game board and map elements:
        - Board
        - City
        - Route
        - RouteGroup
        - RouteColour (enum)

- **pendingdecision**
    - Handles the pending decisions:
        - PendingDecision
        - TicketDecision

- **request**
    - Handles user action requests:
        - ActionRequest
        - DrawTrainCardRequest
        - DrawDestCardRequest
        - ClaimRouteRequest
        - KeepDrawDestRequest

- **save**
    - Handles save/load of the game state:
        - mapper
            - AbilityFactory
            - DeckStateMapper
            - GameStateMapper
            - LastRoundStateMapper
            - PlayerStateMapper
            - RouteStateMapper
        - memento
            - GameMemento
            - PlayerMemento
            - RouteMemento
        - GameSerializer
        - SaveLoadService

- **setup**
    - Responsible for game initialization:
        - GameBuilder
        - LondonGameBuilder
        - GameSetupDirector
        - GameConfiguration
        - PlayerConfiguration
        - MapType (enum)

- **strategy**
    - Encapsulates interchangeable game logic:
        - playerability
            - ColourMasterAbility
            - FerryMasterAbility
            - PlayerAbility
        - routeclaiming
            - RouteClaimStrategy
            - StandardRouteClaimStrategy
            - GrayRouteClaimStrategy
            - FerryRouteClaimStrategy
        - routescoring
            - RouteScoringStrategy
            - StandardRouteScoring

- **turnmanager**
    - Controls turn flow and decisions:
        - TurnMediator
        - LastRoundTracker

- **ui**
    - Handles user interaction (Java Swing):
        - GameUI
        - SwingGameUI
        - MainFrame

## Documentation & Resources

The repository also includes some supporting materials for understanding the system design and game:

- **Sprint 3 UML Class Diagram**
    - Located in: `/docs/UML Class Diagram FinalVersion.pdf`
    - Describes the overall system architecture, and class relationships.

- **Sprint 3 Design Document**
    - Located in: `/docs/Sprint3_jhew0010.pdf`
    - Describes the overall system architecture, and design decisions. 
    - Includes Antipatterns identified, Design Rationale and additional supporting documentations

- **Game Assets**
    - Located in: `/resources/`
    - Includes images such as:
        - Train cards (front and back)
        - Destination tickets (front and back)

- **Sprint 2 UML Class Diagram**
    - Located in: `/docs/Sprint_2_Iteration/UML Class Diagram Final Version.pdf`
    - Describes the overall system architecture, and class relationships.

- **Sprint 2 Design Document**
    - Located in: `/docs/Sprint2_A2_MA_Friday02PM_08.pdf`
    - Describes the overall system architecture, and design decisions.
    - Includes Design Rationale and additional supporting documentations

## Tech Stack

- Java
- Java Swing

## Command to rebuild and run jar file
Please write each line onto your terminal 
1. rm -rf build dist
2. mkdir -p build dist
3. javac -encoding UTF-8 -d build -sourcepath src src/game/Main.java
4. cp -R src/resources build/resources
5. jar cfe dist/Sprint3_jhew0010_executable.jar game.Main -C build .
8. java -jar dist/Sprint3_jhew0010_executable.jar

Important Note: 
This build command only runs on macOS.
Requires Java 21 or later.
