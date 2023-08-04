package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;

import java.util.Optional;
import java.util.UUID;

public interface DataManager {

    Player savePlayer(Player player);

    Optional<Player> findPlayerById(UUID id);

    Optional<String> findPlayerSecretByPlayerId(UUID id);

    Boolean isPlayerSecretValid(UUID playerId, String secret);

    Board saveBoard(Board board);

    Optional<Board> findBoardById(UUID id);

}
