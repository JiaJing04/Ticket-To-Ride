package game.main.dto;

import game.card.DestinationCard;

import java.util.List;

/**
 * DTO class to represent DestinationCard to be sent to frontend
 *
 * @author JiaMing
 */
public class TicketDecisionCardDTO {

    /**
     * Unique identifier of the destination card.
     */
    private final int id;

    /**
     * Name of the starting city.
     */
    private final String from;

    /**
     * Name of the destination city.
     */
    private final String to;

    /**
     * Points awarded for completing this destination card.
     */
    private final int points;

    /**
     * Constructs a DTO for a destination card.
     *
     * @param id     unique identifier of the card
     * @param from   name of the starting city
     * @param to     name of the destination city
     * @param points points awarded for completing the card
     */
    public TicketDecisionCardDTO(int id, String from, String to, int points) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.points = points;
    }

    /**
     * Converts a DestinationCard domain model into a DTO.
     *
     * @param destinationCard destination card to convert
     * @return corresponding DTO representation
     */
    public static TicketDecisionCardDTO convertModel2DTO(DestinationCard destinationCard) {
        return new TicketDecisionCardDTO(
                destinationCard.getId(),
                destinationCard.getFrom().getName(),
                destinationCard.getTo().getName(),
                destinationCard.getPoints());
    }

    /**
     * Converts a list of DestinationCard domain models into DTOs.
     *
     * @param destinationCardList list of destination cards to convert
     * @return list of DTO representations
     */
    public static List<TicketDecisionCardDTO> convertModels2DTOs(List<DestinationCard> destinationCardList) {
        return destinationCardList.stream()
                .map(TicketDecisionCardDTO::convertModel2DTO)
                .toList();
    }

    /**
     * Returns the unique identifier of the card.
     *
     * @return card id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the starting city.
     *
     * @return starting city name
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the name of the destination city.
     *
     * @return destination city name
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the points awarded for completing the card.
     *
     * @return point value
     */
    public int getPoints() {
        return points;
    }
}
