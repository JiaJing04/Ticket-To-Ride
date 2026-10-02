package game.save.mapper;

import game.strategy.playerability.ColourMasterAbility;
import game.strategy.playerability.FerryMasterAbility;
import game.strategy.playerability.PlayerAbility;

/**
 * Creates PlayerAbility instances based on ability names using Factory pattern
 *
 * @author Hew Jia Jing
 */
public class AbilityFactory {

    /**
     * Creates a player ability corresponding to the given ability name.
     *
     * @param abilityName the name of the ability to create
     * @return the corresponding player ability instance
     * @throws IllegalArgumentException if the ability name is not recognized
     */
    public PlayerAbility createAbility(String abilityName) {
        return switch (abilityName) {
            case "Ferry Master" -> new FerryMasterAbility();
            case "Colour Master" -> new ColourMasterAbility();
            default -> throw new IllegalArgumentException(
                    "Unknown ability: " + abilityName
            );
        };
    }
}
