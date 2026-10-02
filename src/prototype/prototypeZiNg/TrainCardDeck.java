package prototype.prototypeZiNg;

import java.util.ArrayList;

public class TrainCardDeck {

    private ArrayList<TrainCard> faceUpPile = new ArrayList<>();
    private ArrayList<TrainCard> inDeckPile = new ArrayList<>();

    public TrainCardDeck(){}

    public TrainCard drawFaceUp(int slot){
        TrainCard card = faceUpPile.get(slot);
        faceUpPile.remove(slot);
        return card;
    }

    public TrainCard drawFromDeck(){
        if (inDeckPile.isEmpty()){
            return null;
        }
        TrainCard card = inDeckPile.get(inDeckPile.size()-1);
        inDeckPile.remove(inDeckPile.size()-1);
        return card;
    }

    public ArrayList<TrainCard> getFaceUpPile() {
        return faceUpPile;
    }

    public ArrayList<TrainCard> getInDeckPile() {
        return inDeckPile;
    }

    public void setFaceUpPile(ArrayList<TrainCard> faceUpPile) {
        this.faceUpPile = faceUpPile;
    }

    public void setInDeckPile(ArrayList<TrainCard> inDeckPile) {
        this.inDeckPile = inDeckPile;
    }

}
