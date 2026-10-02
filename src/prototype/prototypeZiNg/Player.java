package prototype.prototypeZiNg;

public class Player {

    private PlayerHand playerHand;
    private final String name;

    public Player(String name)
    {
        this.name = name;
        this.playerHand = new PlayerHand();
    }

    public PlayerHand getPlayerHand() {
        return playerHand;
    }

    public String getName() {
        return name;
    }
}
