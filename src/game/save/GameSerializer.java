package game.save;

import game.character.PlayerColour;
import game.save.memento.GameMemento;
import game.save.memento.PlayerMemento;
import game.save.memento.RouteMemento;
import game.setup.MapType;

import java.util.ArrayList;
import java.util.List;

/**
 * Serializes and deserializes game state using a custom text-based format.
 *
 * @author Hew Jia Jing
 */
public class GameSerializer {

    /**
     * Converts a game memento into its serialized text representation.
     *
     * @param memento the game state to serialize
     * @return the serialized game state
     */
    public String serialize(GameMemento memento) {
        StringBuilder builder = new StringBuilder();

        builder.append("SAVE_VERSION=2\n");
        builder.append("MAP_TYPE=").append(memento.getMapType()).append("\n");
        builder.append("CURRENT_PLAYER_INDEX=")
                .append(memento.getCurrentPlayerIndex()).append("\n");
        builder.append("REMAINING_ACTION_COST=")
                .append(memento.getRemainingActionCost()).append("\n");
        builder.append("SETUP_PHASE=")
                .append(memento.isSetupPhase()).append("\n");

        builder.append("LAST_ROUND_TRIGGERED=")
                .append(memento.isLastRoundTriggered()).append("\n");
        builder.append("LAST_ROUND_TURNS_REMAINING=")
                .append(memento.getLastRoundTurnsRemaining()).append("\n");
        builder.append("LAST_ROUND_TRIGGERING_PLAYER=")
                .append(memento.getLastRoundTriggeringPlayer() == null
                        ? ""
                        : memento.getLastRoundTriggeringPlayer())
                .append("\n");

        builder.append("\n[PLAYERS]\n");

        for (PlayerMemento player : memento.getPlayers()) {
            builder.append("PLAYER=")
                    .append(player.getName()).append(",")
                    .append(player.getColour()).append(",")
                    .append(player.getScore()).append(",")
                    .append(player.getRemainingTrainPieces()).append(",")
                    .append(player.getAbilityName()).append(",")
                    .append(player.hasDoubleTurn())
                    .append("\n");

            builder.append("TRAIN_CARDS=")
                    .append(join(player.getTrainCardIds()))
                    .append("\n");

            builder.append("DESTINATION_CARDS=")
                    .append(join(player.getDestinationCardIds()))
                    .append("\n");
        }

        builder.append("\n[ROUTES]\n");

        for (RouteMemento route : memento.getClaimedRoutes()) {
            builder.append("CLAIMED_ROUTE=")
                    .append(route.getRouteId()).append(",")
                    .append(route.getOwnerName())
                    .append("\n");
        }

        builder.append("\n[TRAIN_DECK]\n");
        builder.append("DRAW_PILE=")
                .append(join(memento.getTrainDeckCards()))
                .append("\n");
        builder.append("FACE_UP=")
                .append(join(memento.getTrainFaceUpCards()))
                .append("\n");
        builder.append("DISCARD=")
                .append(join(memento.getTrainDiscardCards()))
                .append("\n");

        builder.append("\n[DESTINATION_DECK]\n");
        builder.append("DRAW_PILE=")
                .append(join(memento.getDestinationDeckCards()))
                .append("\n");

        return builder.toString();
    }

    /**
     * Reconstructs a game memento from serialized text data.
     *
     * @param data the serialized game state
     * @return the reconstructed game memento
     */
    public GameMemento deserialize(String data) {
        GameMemento memento = new GameMemento();

        String section = "";
        PlayerMemento currentPlayer = null;

        for (String rawLine : data.lines().toList()) {
            String line = rawLine.trim();

            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("[") && line.endsWith("]")) {
                section = line;
                continue;
            }

            if (line.startsWith("MAP_TYPE=")) {
                memento.setMapType(
                        MapType.valueOf(value(line, "MAP_TYPE="))
                );
                continue;
            }

            if (line.startsWith("CURRENT_PLAYER_INDEX=")) {
                memento.setCurrentPlayerIndex(
                        Integer.parseInt(value(line, "CURRENT_PLAYER_INDEX="))
                );
                continue;
            }

            if (line.startsWith("REMAINING_ACTION_COST=")) {
                memento.setRemainingActionCost(
                        Integer.parseInt(value(line, "REMAINING_ACTION_COST="))
                );
                continue;
            }

            if (line.startsWith("SETUP_PHASE=")) {
                memento.setSetupPhase(
                        Boolean.parseBoolean(value(line, "SETUP_PHASE="))
                );
                continue;
            }

            if (line.startsWith("LAST_ROUND_TRIGGERED=")) {
                memento.setLastRoundTriggered(
                        Boolean.parseBoolean(value(line, "LAST_ROUND_TRIGGERED="))
                );
                continue;
            }

            if (line.startsWith("LAST_ROUND_TURNS_REMAINING=")) {
                memento.setLastRoundTurnsRemaining(
                        Integer.parseInt(value(line, "LAST_ROUND_TURNS_REMAINING="))
                );
                continue;
            }

            if (line.startsWith("LAST_ROUND_TRIGGERING_PLAYER=")) {
                String name = value(line, "LAST_ROUND_TRIGGERING_PLAYER=");
                memento.setLastRoundTriggeringPlayer(
                        name.isBlank() ? null : name
                );
                continue;
            }

            if ("[PLAYERS]".equals(section)) {
                if (line.startsWith("PLAYER=")) {
                    String[] parts = value(line, "PLAYER=").split(",");

                    currentPlayer =
                            new PlayerMemento();

                    currentPlayer.setName(parts[0]);
                    currentPlayer.setColour(
                            PlayerColour.valueOf(parts[1])
                    );
                    currentPlayer.setScore(
                            Integer.parseInt(parts[2])
                    );
                    currentPlayer.setRemainingTrainPieces(
                            Integer.parseInt(parts[3])
                    );
                    currentPlayer.setAbilityName(parts[4]);
                    currentPlayer.setHasDoubleTurn(
                            Boolean.parseBoolean(parts[5])
                    );

                    memento.getPlayers().add(currentPlayer);
                    continue;
                }

                if (line.startsWith("TRAIN_CARDS=")
                        && currentPlayer != null) {

                    currentPlayer.getTrainCardIds().addAll(
                            parseIds(value(line, "TRAIN_CARDS="))
                    );
                    continue;
                }

                if (line.startsWith("DESTINATION_CARDS=")
                        && currentPlayer != null) {

                    currentPlayer.getDestinationCardIds().addAll(
                            parseIds(value(line, "DESTINATION_CARDS="))
                    );
                    continue;
                }
            }

            if ("[ROUTES]".equals(section)) {
                if (line.startsWith("CLAIMED_ROUTE=")) {
                    String[] parts =
                            value(line, "CLAIMED_ROUTE=").split(",", 2);

                    RouteMemento route =
                            new RouteMemento();

                    route.setRouteId(parts[0]);
                    route.setOwnerName(parts[1]);

                    memento.getClaimedRoutes().add(route);
                    continue;
                }
            }

            if ("[TRAIN_DECK]".equals(section)) {
                if (line.startsWith("DRAW_PILE=")) {
                    memento.getTrainDeckCards().addAll(
                            parseIds(value(line, "DRAW_PILE="))
                    );
                    continue;
                }

                if (line.startsWith("FACE_UP=")) {
                    memento.getTrainFaceUpCards().addAll(
                            parseIds(value(line, "FACE_UP="))
                    );
                    continue;
                }

                if (line.startsWith("DISCARD=")) {
                    memento.getTrainDiscardCards().addAll(
                            parseIds(value(line, "DISCARD="))
                    );
                    continue;
                }
            }

            if ("[DESTINATION_DECK]".equals(section)) {
                if (line.startsWith("DRAW_PILE=")) {
                    memento.getDestinationDeckCards().addAll(
                            parseIds(value(line, "DRAW_PILE="))
                    );
                }
            }
        }

        return memento;
    }

    /**
     * Extracts the value portion of a key-value line.
     *
     * @param line the complete line containing a key-value pair
     * @param prefix the key prefix to remove
     * @return the extracted value
     */
    private String value(String line, String prefix) {
        return line.substring(prefix.length());
    }

    /**
     * Joins a list of card IDs into a comma-separated string.
     *
     * @param ids the IDs to join
     * @return a comma-separated string containing the IDs
     */
    private String join(List<Integer> ids) {
        StringBuilder builder = new StringBuilder();

        for (Integer id : ids) {
            builder.append(id).append(",");
        }

        return builder.toString();
    }

    /**
     * Parses a comma-separated list of IDs into integers.
     *
     * @param value the string containing comma-separated IDs
     * @return a list of parsed integer IDs
     * @throws NumberFormatException if an ID cannot be parsed as an integer
     */
    private List<Integer> parseIds(String value) {
        List<Integer> ids = new ArrayList<>();

        if (value == null || value.isBlank()) {
            return ids;
        }

        for (String part : value.split(",")) {
            if (!part.isBlank()) {
                ids.add(Integer.parseInt(part.trim()));
            }
        }

        return ids;
    }
}
