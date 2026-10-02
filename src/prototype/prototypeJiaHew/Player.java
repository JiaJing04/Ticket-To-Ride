package prototype.prototypeJiaHew;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<DestinationCard> destinationCards = new ArrayList<>();
    private List<TrainCard> trainCards = new ArrayList<>();
    private final String name;

    public Player(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public List<DestinationCard> getDestinationCards()
    {
        return destinationCards;
    }

    public List<TrainCard> getTrainCards()
    {
        return trainCards;
    }

    public void drawDestinationCards(List<DestinationCard> cards)
    {
        destinationCards.addAll(cards);
    }

    public void drawTrainCards(List<TrainCard> cards)
    {
        trainCards.addAll(cards);
    }

    public int getHandSize()
    {
        return destinationCards.size() + trainCards.size();
    }
}
