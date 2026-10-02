package prototype.prototypeZiNg;

import java.util.ArrayList;
import java.util.List;

public class PlayerHand {

    private ArrayList<TrainCard> trainCards = new ArrayList<>();

    public PlayerHand(){}

    public List<TrainCard> getTrainCards()
    {
        return trainCards;
    }

    public void addTrainCard(TrainCard card){
        trainCards.add(card);
    }

}
