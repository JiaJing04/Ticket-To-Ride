package game.command;

/**
 * Represents the cost of executing an action.
 *
 * @author Ng Zi Wei
 */
public enum ActionCost {
    FREE(0),
    HALF(1),
    FULL(2);

    private final int cost;

    ActionCost(int cost) {
        this.cost = cost;
    }

    /**
     * Returns the numeric cost value.
     *
     * @return cost value
     */
    public int getCost() {
        return cost;
    }
}
