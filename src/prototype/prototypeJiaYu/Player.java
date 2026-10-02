package prototype.prototypeJiaYu;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Route> routeClaimed = new ArrayList<>();

    private final String name;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Route> getRouteClaimed() {
        return routeClaimed;
    }

    public void claimRoute(Route route) {
        routeClaimed.add(route);

        route.setClaimedBy(this);
    }
}
