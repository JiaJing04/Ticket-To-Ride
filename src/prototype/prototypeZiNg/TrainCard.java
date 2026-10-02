package prototype.prototypeZiNg;

public class TrainCard {

    private String colour;
    private String trainType;

    public TrainCard(String colour, String trainType){
        this.colour = colour;
        this.trainType = trainType;
    }

    public String getColour() {
        return colour;
    }

    public String getTrainType() {
        return trainType;
    }

    @Override
    public String toString() {
        return "TrainCard " +
                "colour=" + colour +
                ", trainType=" + trainType;
    }
}
