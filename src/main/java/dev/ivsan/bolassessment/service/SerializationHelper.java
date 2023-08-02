package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;

public interface SerializationHelper {

    String serializePlayer(Player player);

    Player deserializePlayer(String serializedPlayer);

    String serializeBoard(Board board);

    Board deserializeBoard(String serializedBoard);

}
