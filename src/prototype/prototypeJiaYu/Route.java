package prototype.prototypeJiaYu;

public class Route {
    private Player claimedBy;

    private String name;

    public Route(String name) {
        this.name = name;
    }

    public Player getOwner() {
        return claimedBy;
    }

    public void setClaimedBy(Player player) {
        claimedBy = player;
    }

    @Override
    public String toString() {
        return name;
    }
}
